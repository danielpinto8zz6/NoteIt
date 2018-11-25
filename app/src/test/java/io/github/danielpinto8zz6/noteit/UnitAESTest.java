package io.github.danielpinto8zz6.noteit;

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import io.github.danielpinto8zz6.noteit.encryption.AESHelper;

import static org.junit.Assert.assertEquals;

public class UnitAESTest {

    private final String msg = "123456";
    private final String key = "abcdef";
    private final String iv = "ABCDEF";
    private byte[] ans = new byte[0];
    private String ansBase64 = null;
    private String deansBase64 = null;

    @Before
    public void setUpEncryption() {
        try {
            ans = AESHelper.encrypt(iv, key, msg.getBytes());
            ansBase64 = AESHelper.encryptStrAndToBase64(iv, key, msg);
            new String(ans, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Encryption_isCorrect() {
        try {
            byte[] deans = AESHelper.decrypt(iv, key, ans);
            assertEquals(msg, new String(deans, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void EncryptionBase64_isCorrect() {
        try {
            deansBase64 = AESHelper.decryptStrAndFromBase64(iv, key, ansBase64);
            assertEquals(msg, deansBase64);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
