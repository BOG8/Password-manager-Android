package com.bog.password_manager_android;

import android.util.Log;

import java.security.AlgorithmParameters;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Олег on 11.04.2017.
 */

class PasswordCipher {
    private final static String LOG_TAG = PasswordCipher.class.getSimpleName();
    private final static int SIZE = 16;
    private final static int ITERATION_COUNT = 65536;
    private final static int KEY_LENGTH = 256;

    private static PasswordCipher instance;
    private String password;
    private byte[] iv;
    private byte[] cipherData;

    private PasswordCipher() {

    }

    synchronized static PasswordCipher getInstance() {
        if (instance == null) {
            instance = new PasswordCipher();
        }
        return instance;
    }

    private SecretKey generateKey() {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), new byte[SIZE], ITERATION_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (Exception e) {
            Log.e(LOG_TAG, "genKey: " + e.getMessage());
        }
        return null;
    }

    void encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, generateKey());
            AlgorithmParameters params = cipher.getParameters();
            iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            cipherData = cipher.doFinal(data.getBytes("UTF-8"));
        } catch (Exception e) {
            Log.e(LOG_TAG, "encrypt: " + e.getMessage());
        }
    }

    String decrypt(byte[] cipherData, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, generateKey(), new IvParameterSpec(iv));
            return new String(cipher.doFinal(cipherData), "UTF-8");
        } catch (Exception e) {
            Log.e(LOG_TAG, "decrypt: " + e.getMessage());
        }
        return null;
    }

    void setPassword(String password) {
        this.password = password;
    }

    String getIv() {
        return Converter.toString(iv);
    }

    String getCipherData() {
        return Converter.toString(cipherData);
    }

    void clearCipher() {
        iv = null;
        cipherData = null;
    }

    public String getPassword() {
        return password;
    }
}
