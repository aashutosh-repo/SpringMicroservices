package com.spring.core.utils;

import com.spring.core.error.CustomErrorMessage;
import com.spring.core.error.ErrorCode;

import javax.crypto.*;

import javax.crypto.spec.GCMParameterSpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;

public class EncryptionUtil {

    private EncryptionUtil(){}

    private static final String ALGORITHM = "AES/GCM/NoPadding";

    // AES Key Size (256 bits)
    private static final int AES_KEY_SIZE = 256;

    // GCM Parameters
    private static final int GCM_IV_LENGTH = 12; // 12 bytes (96 bits) for GCM recommended
    private static final int GCM_TAG_LENGTH = 128; // 128-bit authentication tag

    //This method is to Tokenize the Card Details
    public static SecretKey generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(AES_KEY_SIZE, SecureRandom.getInstanceStrong());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("AES algorithm not available", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while generating AES key", e);
        }
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    //this method to encrypt the network data
    public static String encrypt(String data, SecretKey secretKey) {
        try {
            byte[] iv = generateIV();
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] encryptedData = cipher.doFinal(data.getBytes());

            // Combine IV and encrypted data for transmission
            byte[] encryptedWithIV = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, encryptedWithIV, 0, iv.length);
            System.arraycopy(encryptedData, 0, encryptedWithIV, iv.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(encryptedWithIV);
        }catch (GeneralSecurityException e){
            throw new CustomErrorMessage(ErrorCode.ENCRYPTION_FAILED);
        }
    }

    //this method to decrypt the network data
    public static String decrypt(String encryptedData, SecretKey secretKey) {
        try {
            byte[] encryptedWithIV = Base64.getDecoder().decode(encryptedData);

            // Extract IV from the beginning of the encrypted data
            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(encryptedWithIV, 0, iv, 0, iv.length);

            // Extract the actual encrypted data
            byte[] encryptedBytes = new byte[encryptedWithIV.length - iv.length];
            System.arraycopy(encryptedWithIV, iv.length, encryptedBytes, 0, encryptedBytes.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] decryptedData = cipher.doFinal(encryptedBytes);
            return new String(decryptedData);
        }catch (IllegalArgumentException e) {
        // Handle specific known issues
        throw new RuntimeException(ErrorCode.DECRYPTION_FAILED);
    } catch (GeneralSecurityException e) {
        // Handle other security-related issues
        throw new CustomErrorMessage(ErrorCode.DECRYPTION_FAILED , "Decryption failed due to security error " +e);
    } catch (Exception e) {
        // Catch all unexpected errors
        throw new CustomErrorMessage(ErrorCode.DECRYPTION_FAILED ,  "An unknown error occurred during decryption" +e);
    }
    }
}
