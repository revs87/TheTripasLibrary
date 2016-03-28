
package com.tripasfactory.thetripaslibrary.Security;

import android.app.Activity;
import android.os.AsyncTask;

import com.tripasfactory.thetripaslibrary.Configs;
import com.tripasfactory.thetripaslibrary.Interfaces.CertificateValidationInterface;
import com.tripasfactory.thetripaslibrary.Utils.L;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

// http://android-developers.blogspot.com/2009/05/painless-threading.html
public class FetchPubKeyTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = FetchPubKeyTask.class.getSimpleName();

    private final Activity context;
    private final String endpoint;

    private final CertificateValidationInterface validationInterface;
    private static int RESULT_OK = 0;
    private static int RESULT = 1;
    private static int RESULT_TIMEOUT = 2;

    public FetchPubKeyTask(Activity context, String endpoint, CertificateValidationInterface validationInterface) {
        this.context = context;
        this.endpoint = endpoint;
        this.validationInterface = validationInterface;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {

            URL url = new URL(endpoint);
            InputStream in = null;

            try {
                HttpsURLConnection httpsConnection = (HttpsURLConnection) url.openConnection();

                httpsConnection.setRequestMethod("HEAD");
                httpsConnection.setConnectTimeout(PubKeyManager.CERTIFICATE_TIMEOUT_CONNECTION);
                httpsConnection.setReadTimeout(PubKeyManager.CERTIFICATE_TIMEOUT_CONNECTION);
//                L.d("SECURITY URL", "Connecting..." + PubKeyManager.CERTIFICATE_VALIDATION_URL);
                L.d("SECURITY URL", "Connecting..." + endpoint);

                TrustManager tm[] = {new PubKeyManager()};
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tm, null);
                httpsConnection.setSSLSocketFactory(context.getSocketFactory());

                try {
                    httpsConnection.connect();

                    Certificate[] certs = httpsConnection.getServerCertificates();
                    X509Certificate[] x509Certificates = new X509Certificate[certs.length];
                    for (int i = 0; i < certs.length; i++) {
                        x509Certificates[i] = (X509Certificate) certs[i];
                    }

                    if (PubKeyManager.evaluateChain(x509Certificates, "RSA")) {
                        RESULT = RESULT_OK;
                    }

                } catch (CertificateException e) {
                    L.e(TAG, "CertificateException");
                    e.printStackTrace();
                } catch (SocketTimeoutException e) {
                    L.e(TAG, "SocketTimeoutException");
                    RESULT = RESULT_TIMEOUT;
                    e.printStackTrace();
                } catch (IOException e) {
                    L.e(TAG, "IOException: " + e.getMessage());
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }

            } catch (NoSuchAlgorithmException e) {
                if (!Configs.HIDE_VERBOSE_LOGGING) {
                    throw new IOException("NoSuchAlgorithmException", e);
                } else {
                    throw new IOException("", e);
                }
            } catch (ClassCastException e) {
                /* not https */
                if (!Configs.HIDE_VERBOSE_LOGGING) {
                    throw new IOException("ClassCastException", e);
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

        } catch (SocketTimeoutException e) {
            L.e(TAG, "SocketTimeoutException");
            RESULT = RESULT_TIMEOUT;
            e.printStackTrace();
        } catch (IOException e) {
            L.e(TAG, "IOException2: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (RESULT == RESULT_OK) {
            validationInterface.onAccept();
        } else if (RESULT == RESULT_TIMEOUT) {
            validationInterface.onTimeout();
        } else {
            validationInterface.onDeny();
        }
    }

}
