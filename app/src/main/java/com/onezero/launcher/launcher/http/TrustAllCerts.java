package com.onezero.launcher.launcher.http;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by benchengzhou on 2017/11/13  10:09 .
 * 作者邮箱： mappstore@163.com
 * 功能描述： 测试环境添加https全部信任
 * 类    名： TrustAllCerts
 * 备    注：
 */


public class TrustAllCerts implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {}

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
}
