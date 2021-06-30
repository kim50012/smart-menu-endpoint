package com.basoft.core.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class SecurityUtil {
    public static String hashCredential(String oldCredential, String salt) {
        KeySpec spec = new PBEKeySpec(oldCredential.toCharArray(), salt.getBytes(), 65536, 128);
        byte[] hash = new byte[0];
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = f.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(hash);
    }
}
