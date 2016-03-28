
package com.tripasfactory.thetripaslibrary.Security;

import com.tripasfactory.thetripaslibrary.Configs;
import com.tripasfactory.thetripaslibrary.Utils.L;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by e466 on 09/07/2015.
 */
public class PubKeyManager implements X509TrustManager {

    private static final String TAG = PubKeyManager.class.getSimpleName();

    // public key (DER encoded, RSA type)
    public static final String PUB_KEY_UNTRUSTED_QUAL = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100b893b4c18d12a10f58a966d8f754b884be766dc5d8b885a0e821c17cdbd37fa5d31182ab890e3761a03d729f9d6770e86b230bf0e24af2fe9b7a6811a49f0787c8b3e531d963db581f74bd005a1f124d83724d13886fac5c1327202fad049244f38a034b3d74eb5b439edd32e89340406c250bf9e802b12d17c79a5f5f38ab7476e865e52408cd72e48c278f3da6fcf1dcd1072ce06c0177aae45955b6e7887528cd29dd0b2bd552b6c6f5f238da110024dc7b4adc59f1efe7c2ba78f2701118b24d1fd2c0fc39431650873a7abf6e40139db6302b2c1d9bb321c47da68eb7380275dcf90a297a89323b8efd2729e08bcbd58d8fd6618114d0079e0b53c40aeb0203010001";
    public static final String PUB_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100e035239f41470d97fe59a0cd94fef9a89ad315fddafb9af78914d73cc25ce9a41c37bd94b32b517a71d9b057cd72b6db5146bd191c3b64b76caf3aeb627185e45918520327b0aa7f3af09ac5cf3479a62d8a5371fb68a329d08ecacc0f6a8b5797712e2008355c8a8f689bac4779050402dfe1f09dccc259c22ffc43a7b04555295b99e5efa36007ba220dba1d4b2fe19fff083439d8b4e7b252b2399f310fcdbcfcc966191a7fe7597f5b08f22f58207af1c4476b0d2c4cce04f78a8c565abf04ff192e107e07a7bc1f78a15f247fd7e886e30f24801b9454665df31483990ecb148fdc26e27a872fb1826b4215deccea5b5044959b039e3b142bdac291fc510203010001";

    /* FetchSecretTask unused. Using FetchPubKeyTask */
    // public key encoded OpenSSLRSAPublicKey
    @Deprecated
    public static final String PUB_KEY_ENCODED = "b893b4c18d12a10f58a966d8f754b884be766dc5d8b885a0e821c17cdbd37fa5d31182ab890e3761a03d729f9d6770e86b230bf0e24af2fe9b7a6811a49f0787c8b3e531d963db581f74bd005a1f124d83724d13886fac5c1327202fad049244f38a034b3d74eb5b439edd32e89340406c250bf9e802b12d17c79a5f5f38ab7476e865e52408cd72e48c278f3da6fcf1dcd1072ce06c0177aae45955b6e7887528cd29dd0b2bd552b6c6f5f238da110024dc7b4adc59f1efe7c2ba78f2701118b24d1fd2c0fc39431650873a7abf6e40139db6302b2c1d9bb321c47da68eb7380275dcf90a297a89323b8efd2729e08bcbd58d8fd6618114d0079e0b53c40aeb";

    /* Timeout SSL checking */
    public static final int CERTIFICATE_TIMEOUT_CONNECTION = 1000 * 10;


    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {

        evaluateChain(chain, authType);
    }

    public static boolean evaluateChain(X509Certificate[] chain, String authType) throws CertificateException {
        if (!Configs.SHOULD_CHECK_SSL_PINNING) {
            return true;
        }

        boolean status = true;

        assert (chain != null);
        if (chain == null) {
            if (Configs.SHOULD_BLOCK_CONNECTION_ON_SSL_PINNING_FAIL) {
                if (!Configs.HIDE_VERBOSE_LOGGING) {
                    throw new IllegalArgumentException(
                            "checkServerTrusted: X509Certificate array is null");
                } else {
                    throw new IllegalArgumentException(
                            "");
                }
            }
            status = false;
        }

        assert (chain.length > 0);
        if (!(chain.length > 0)) {
            if (Configs.SHOULD_BLOCK_CONNECTION_ON_SSL_PINNING_FAIL) {
                if (!Configs.HIDE_VERBOSE_LOGGING) {
                    throw new IllegalArgumentException(
                            "checkServerTrusted: X509Certificate is empty");
                } else {
                    throw new IllegalArgumentException(
                            "");
                }
            }
            status = false;
        }

        assert (null != authType && authType.contains("RSA"));
        if (!(null != authType && authType.contains("RSA"))) {
            if (Configs.SHOULD_BLOCK_CONNECTION_ON_SSL_PINNING_FAIL) {
                if (!Configs.HIDE_VERBOSE_LOGGING) {
                    throw new CertificateException(
                            "checkServerTrusted: AuthType is not RSA");
                } else {
                    throw new CertificateException(
                            "");
                }
            }
            status = false;
        }

        // Perform customary SSL/TLS checks
        TrustManagerFactory tmf;
        try {
            if (Configs.SHOULD_CHECK_TRUSTED_ON_SSL_PINNING_FAIL) {
                tmf = TrustManagerFactory.getInstance("X509");
                tmf.init((KeyStore) null);

                /** Checks whether the specified certificate chain (partial or complete)
                 * can be validated and is trusted for server authentication for the
                 * specified key exchange algorithm. */
                TrustManager[] tmngrs = tmf.getTrustManagers();
                for (TrustManager trustManager : tmngrs) {
//                    ((X509TrustManager) trustManager).checkClientTrusted(
                    ((X509TrustManager) trustManager).checkServerTrusted(
                            chain, authType);
                }
            }
        } catch (Exception e) {
            L.e(TAG, "CertificateException TrustManager: " + e.getMessage());
            if (Configs.SHOULD_BLOCK_TRUSTED_ON_SSL_PINNING_FAIL) {
                if (!Configs.HIDE_VERBOSE_LOGGING) {
                    throw new CertificateException(e);
                } else {
                    throw new CertificateException("");
                }
            }
            status = false;
        }

        boolean publicKeyFound = false;
        L.i("Checking PK EXPECTED", PUB_KEY);

        /* Search the whole chain */
        for (int i = 0; i < chain.length; i++) {

            // Hack ahead: BigInteger and toString(). We know a DER encoded Public
            // Key starts with 0x30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is
            // no leading 0x00 to drop.
            RSAPublicKey pubkey = (RSAPublicKey) chain[i].getPublicKey();
            String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded())
                    .toString(16);

            /* To print */
//            X509Certificate info = chain[i];
//            info.getIssuerDN();

            // Pin it!
            final boolean expected = PUB_KEY.equalsIgnoreCase(encoded);
            assert (expected);
            if (expected) {
                L.i("Checking PK  OK", encoded);
                publicKeyFound = true;
            } else {
                L.e("Checking PK NOK", encoded);
            }
        }

        if (!publicKeyFound) {
            if (Configs.SHOULD_BLOCK_CONNECTION_ON_SSL_PINNING_FAIL) {
                if (!Configs.HIDE_VERBOSE_LOGGING) {
                    throw new CertificateException("checkServerTrusted: NOK");
                } else {
                    throw new CertificateException("");
                }
            }
            status = false;
        }

        return status;
    }

    public void checkClientTrusted(X509Certificate[] xcs, String string) {
        // throw new
        // UnsupportedOperationException("checkClientTrusted: Not supported yet.");
    }

    public X509Certificate[] getAcceptedIssuers() {
        // throw new
        // UnsupportedOperationException("getAcceptedIssuers: Not supported yet.");
        return null;
    }
}
