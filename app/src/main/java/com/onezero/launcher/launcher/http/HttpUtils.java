package com.onezero.launcher.launcher.http;

import android.content.Context;
import android.util.Log;

import com.onezero.launcher.launcher.api.GetAppTypeListService;
import com.onezero.launcher.launcher.utils.NetWorkUtils;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtils {
    private static final String URL_BASE = "http://www.yunarm.com/api/ysj/";
    private static final int NET_MAX = 30; //30秒  有网超时时间
    private static final int NO_NET_MAX = 60 * 60 * 24 * 7; //7天 无网超时时间
    private static OkHttpClient sOkHttpClient;


    public static Retrofit getRetrofit(String url, final Context context) {
        //应用程序拦截器
        Interceptor mInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.e("TAG", "拦截 网络 缓存");
                Request request = chain.request();
                if (!NetWorkUtils.isNetWorkEnable(context)) {//判断网络状态 无网络时
                    Log.e("TAG", "无网~~ 缓存");
                    request = request.newBuilder()
                            //Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache-Control:no-cache相同。为了确保缓存生效
                            .removeHeader("Pragma")
                            .header("Cache-Control", "private, only-if-cached, max-stale=" + NO_NET_MAX)
                            .build();
                } else {//有网状态
                    Log.e("TAG", "有网~~ 缓存");
                    request = request.newBuilder()
                            //Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache-Control:no-cache相同。为了确保缓存生效
                            .removeHeader("Pragma")
                            .header("Cache-Control", "private, max-age=" + NET_MAX)//添加缓存请求头
                            .build();
                }
                return chain.proceed(request);
            }
        };

        //网络拦截器
        Interceptor mNetInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.e("TAG", "拦截 应用 缓存");

                Request request = chain.request();
                if (!NetWorkUtils.isNetWorkEnable(context)) {//判断网络状态 无网络时
                    request = request.newBuilder()
                            //Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache-Control:no-cache相同。为了确保缓存生效
                            .removeHeader("Pragma")
                            .header("Cache-Control", "private, only-if-cached, max-stale=" + NO_NET_MAX)
                            .build();
                } else {
                    request = request.newBuilder()
                            //Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache-Control:no-cache相同。为了确保缓存生效
                            .removeHeader("Pragma")
                            .header("Cache-Control", "private, max-age=" + NET_MAX)//添加缓存请求头
                            .build();
                }
                return chain.proceed(request);
            }
        };

        File mFile = new File(context.getCacheDir() + "http");//储存目录
        long maxSize = 10 * 1024 * 1024; // 10 MB 最大缓存数
        Cache mCache = new Cache(mFile, maxSize);

        OkHttpClient mClient = new OkHttpClient.Builder()
//                .addInterceptor(mInterceptor)//应用程序拦截器
//                .addNetworkInterceptor(mNetInterceptor)//网络拦截器
//                .cache(mCache)//添加缓存
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(mClient)//添加OK
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 创建全局OkHttpClient对象
     * <p>
     * OkHttpClient 用于管理所有的请求，内部支持并发，
     * 所以我们不必每次请求都创建一个 OkHttpClient 对象，这是非常耗费资源的。接下来就是创建一个 Request 对象了
     *
     * @return
     */
    public static OkHttpClient getSOkHttpClient() {
        //创建okhttp的请求对象 参考地址  http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0106/2275.html

        if (sOkHttpClient == null) {

            sOkHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20000, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(20000, TimeUnit.SECONDS)//设置写的超时时间
                    .connectTimeout(20000, TimeUnit.SECONDS)//设置连接超时时间
                    .sslSocketFactory(createSSLSocketFactory())    //添加信任所有证书
                    .hostnameVerifier(new HostnameVerifier() {     //信任规则全部信任
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build();
        }
        return sOkHttpClient;
    }

    /**
     * 测试环境https添加全部信任
     * okhttp的配置
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    public static GetAppTypeListService createAppInfoListService(Context context) {
        return getRetrofit(URL_BASE + "external_device_pre_apps/", context).create(GetAppTypeListService.class);
    }
}
