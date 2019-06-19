package com.onezero.launcher.launcher.http;

import android.content.Context;
import android.util.Log;

import com.onezero.launcher.launcher.api.GetAppTypeListService;
import com.onezero.launcher.launcher.utils.NetWorkUtils;

import java.io.File;
import java.io.IOException;

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

    public static GetAppTypeListService createAppInfoListService(Context context) {
        return getRetrofit(URL_BASE + "external_device_pre_apps/", context).create(GetAppTypeListService.class);
    }
}
