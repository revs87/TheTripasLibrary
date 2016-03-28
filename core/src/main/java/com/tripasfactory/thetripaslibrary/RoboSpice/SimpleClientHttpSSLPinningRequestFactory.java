package com.tripasfactory.thetripaslibrary.RoboSpice;


import com.tripasfactory.thetripaslibrary.Configs;
import com.tripasfactory.thetripaslibrary.Security.PubKeyManager;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;


public class SimpleClientHttpSSLPinningRequestFactory extends SimpleClientHttpRequestFactory {

    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod)
            throws IOException {

        try {
            HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
//            httpsConnection.setRequestMethod("HEAD");
//            httpsConnection.setConnectTimeout(PubKeyManager.CERTIFICATE_TIMEOUT_CONNECTION);
//            httpsConnection.setReadTimeout(PubKeyManager.CERTIFICATE_TIMEOUT_CONNECTION);
//            L.d("SECURITY URL", "Connecting..." + PubKeyManager.CERTIFICATE_VALIDATION_URL);
            TrustManager tm[] = {new PubKeyManager()};
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tm, null);
            httpsConnection.setSSLSocketFactory(context.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            if (!Configs.HIDE_VERBOSE_LOGGING) {
                throw new IOException("NoSuchAlgorithmException", e);
            } else {
                throw new IOException("", e);
            }
        } catch (KeyManagementException e) {
            if (!Configs.HIDE_VERBOSE_LOGGING) {
                throw new IOException("KeyManagementException", e);
            } else {
                throw new IOException("", e);
            }
        }

        super.prepareConnection(connection, httpMethod);
    }
}
