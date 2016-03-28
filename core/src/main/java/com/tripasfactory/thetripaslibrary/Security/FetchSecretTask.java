
package com.tripasfactory.thetripaslibrary.Security;

import android.app.Activity;
import android.os.AsyncTask;


import com.tripasfactory.thetripaslibrary.Interfaces.CertificateValidationInterface;
import com.tripasfactory.thetripaslibrary.Utils.L;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by e466 on 09/07/2015.
 */

// http://android-developers.blogspot.com/2009/05/painless-threading.html
public class FetchSecretTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = FetchSecretTask.class.getSimpleName();

    private final Activity context;
    private final String endpoint;

    private final CertificateValidationInterface validationInterface;
    private static int RESULT_OK = 0;
    private static int RESULT = 1;
    private static int RESULT_TIMEOUT = 2;

    public FetchSecretTask(Activity context, String endpoint, CertificateValidationInterface validationInterface) {
        this.context = context;
        this.endpoint = endpoint;
        this.validationInterface = validationInterface;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {

            URL url = new URL(endpoint);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("HEAD");
            urlConnection.setConnectTimeout(PubKeyManager.CERTIFICATE_TIMEOUT_CONNECTION);
            urlConnection.setReadTimeout(PubKeyManager.CERTIFICATE_TIMEOUT_CONNECTION);
//            L.d("SECURITY URL", "Connecting..." + PubKeyManager.CERTIFICATE_VALIDATION_URL);
            L.d("SECURITY URL", "Connecting..." + endpoint);

            InputStream in = urlConnection.getInputStream();

            try {
                Certificate[] certificates = urlConnection.getServerCertificates();
                X509Certificate cert = null;

                for (int i = 0; i < certificates.length; i++) {
                    cert = (X509Certificate) certificates[i];

                    String publicKey = cert.getPublicKey().toString().split("modulus=")[1];
                    publicKey = publicKey.split(",")[0];

//                    String issuerCN = cert.getIssuerDN().getName().split("CN=")[1];
//                    issuerCN = issuerCN.split(",OU=")[0];

                    if (publicKey.equals(PubKeyManager.PUB_KEY_ENCODED)
//                            && issuerCN.equals(PubKeyManager.ISSUER_CN))
                            ) {
                        RESULT = RESULT_OK;
                        L.wtf("SECURITY PK", publicKey);
//                        L.i("SECURITY CN", issuerCN);
                        // break;
                    } else {
                        L.e("SECURITY PK", publicKey);
//                        L.e("SECURITY CN", issuerCN);
                    }

                }

            } catch (Exception ex) {
                L.e(TAG, "Cert Exception");
                ex.printStackTrace();
            } finally {
                in.close();
            }
        } catch (SocketTimeoutException e) {
            L.e(TAG, "SocketTimeoutException");
            RESULT = RESULT_TIMEOUT;
            e.printStackTrace();
        } catch (IOException e) {
            L.e(TAG, "IOException");
            e.printStackTrace();
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
