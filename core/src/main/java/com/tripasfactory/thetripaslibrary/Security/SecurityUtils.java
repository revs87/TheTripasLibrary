package com.tripasfactory.thetripaslibrary.Security;

import android.os.Build;
import android.support.annotation.NonNull;

import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtils {

    public static String generateSeedForCache() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 32; i++) {
            char temp = (char) (random.nextInt() % 256);
            sb.append(temp);
        }

        return sb.toString();
    }

    /**
     * Encrypts / Decrypts data.
     *
     * @param data The data to encrypt / decrypt.
     * @param seed The encrypt / decrypt seed.
     * @return The crypted data.
     * @throws Exception If some error occurs
     */
    public static byte[] encrypt(String data, String seed) throws Exception {
        return encrypt(data.getBytes(), seed);
    }

    /**
     * Encrypts / Decrypts data.
     *
     * @param data The data to encrypt / decrypt.
     * @param seed The encrypt / decrypt seed.
     * @return The crypted data.
     * @throws Exception If some error occurs
     */
    public static byte[] encrypt(byte[] data, @NonNull
    String seed) throws Exception {
        // if (Configs.LOG) L.d(TAG, "encrypt data = " + data + " seed= " +seed
        // );
        // API for generating symmetric cryptographic keys
        KeyGenerator keygen = KeyGenerator.getInstance(AES);

        // Returns a new instance of SecureRandom that utilizes the
        // SHA1 algorithm.
        SecureRandom secrand;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            secrand = SecureRandom.getInstance(SHA1PRNG, Crypto);
        } else {
            secrand = SecureRandom.getInstance(SHA1PRNG);
        }

        // Reseeds this SecureRandom instance with the specified seed.
        secrand.setSeed(seed.getBytes());

        // Initializes this KeyGenerator instance for the specified
        // key size (in bits) using the specified randomness source.
        keygen.init(128, secrand);
        // Generates a secret key.
        SecretKey seckey = keygen.generateKey();
        // Returns the encoded form of the key.
        byte[] rawKey = seckey.getEncoded();

        // Create a new SecretKeySpec for the key data and AES algorithm.
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, AES);
        // Creates a new Cipher for the specified transformation.
        // The installed providers are searched in order for an
        // implementation of the specified transformation.
        // The first found provider providing the transformation
        // is used to create the cipher.
        Cipher cipher = Cipher.getInstance(AES);

        // Initializes this cipher instance with the specified key.
        // The cipher is initialized for the specified operational
        // mode (one of: encryption, decryption, key wrapping or key unwrapping)
        // depending on opmode.
        // If this cipher instance needs any algorithm parameters
        // or random values that the specified key cannot provide,
        // the underlying implementation of this cipher is supposed
        // to generate the required parameters (using its provider or random
        // values).
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        // Finishes a multi-part transformation (encryption or decryption).
        // Processes the bytes in input buffer, and any bytes that
        // have been buffered in previous update calls.
        return cipher.doFinal(data);
    }

    private final static String AES = "AES";
    private final static String Crypto = "Crypto";
    private final static String SHA1PRNG = "SHA1PRNG";

    /**
     * Decrypts the received data with the received encryption seed.
     *
     * @param data The data to decrypt, as a byte array.
     * @param seed The encryption seed.
     * @return The decrypted data.
     * @throws Exception If some error occurs.
     */
    public static byte[] decrypt(byte[] data, String seed) throws Exception {

        // API for generating symmetric cryptographic keys
        KeyGenerator keygen = KeyGenerator.getInstance(AES);

        // Returns a new instance of SecureRandom that utilizes the
        // SHA1 algorithm.
        SecureRandom secrand;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            secrand = SecureRandom.getInstance(SHA1PRNG, Crypto);
        } else {
            secrand = SecureRandom.getInstance(SHA1PRNG);
        }

        // Reseeds this SecureRandom instance with the specified seed.
        secrand.setSeed(seed.getBytes());

        // Initializes this KeyGenerator instance for the specified
        // key size (in bits) using the specified randomness source.
        keygen.init(128, secrand);
        // Generates a secret key.
        SecretKey seckey = keygen.generateKey();
        // Returns the encoded form of the key.
        byte[] rawKey = seckey.getEncoded();

        // Create a new SecretKeySpec for the key data and AES algorithm.
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, AES);
        // Creates a new Cipher for the specified transformation.
        // The installed providers are searched in order for an
        // implementation of the specified transformation.
        // The first found provider providing the transformation
        // is used to create the cipher.
        Cipher cipher = Cipher.getInstance(AES);

        // Initializes this cipher instance with the specified key.
        // The cipher is initialized for the specified operational
        // mode (one of: encryption, decryption, key wrapping or key unwrapping)
        // depending on opmode.
        // If this cipher instance needs any algorithm parameters
        // or random values that the specified key cannot provide,
        // the underlying implementation of this cipher is supposed
        // to generate the required parameters (using its provider or random
        // values).
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        // Finishes a multi-part transformation (encryption or decryption).
        // Processes the bytes in input buffer, and any bytes that
        // have been buffered in previous update calls.
        return cipher.doFinal(data);
    }


}
