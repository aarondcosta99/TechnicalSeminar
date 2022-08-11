package com.example.technicalseminar.util;

import android.util.Base64;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class CipherUtils {
    private static final String PASSWORD = "202010005";
    private static final String ALGORITHM = "DES";
    public static String encrypt(@NonNull String value){
        try{
            DESKeySpec keySpec = new DESKeySpec(PASSWORD.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey key = keyFactory.generateSecret(keySpec);
            byte[] clearText = value.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            return Base64.encodeToString(cipher.doFinal(clearText),Base64.DEFAULT);
        }catch (InvalidKeyException| InvalidKeySpecException| NoSuchAlgorithmException| BadPaddingException| NoSuchPaddingException| IllegalBlockSizeException e){
            e.printStackTrace();
        }
        return value;
    }
    public static String decrypt(@NonNull String value){
        try{
            DESKeySpec keySpec = new DESKeySpec(PASSWORD.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey key = keyFactory.generateSecret(keySpec);
            byte[] encryptedPwdBytes = Base64.decode(value,Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] decryptedValueBytes = (cipher.doFinal(encryptedPwdBytes));
            return new String(decryptedValueBytes);
        }catch (InvalidKeyException| InvalidKeySpecException| NoSuchAlgorithmException| BadPaddingException| NoSuchPaddingException| IllegalBlockSizeException e){
            e.printStackTrace();
        }
        return value;
    }
}
