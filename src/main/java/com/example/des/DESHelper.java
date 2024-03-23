package com.example.des;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DESHelper {

    private SecretKey secretKey;

    public DESHelper() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Nie udało się wygenerować klucza DES: " + e.getMessage());
        }
    }

    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.err.println("Wystąpił błąd podczas szyfrowania danych: " + e.getMessage());
        }
        return null;
    }

    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes);
        } catch (Exception e) {
            System.err.println("Wystąpił błąd podczas deszyfrowania danych: " + e.getMessage());
        }
        return null;
    }
}