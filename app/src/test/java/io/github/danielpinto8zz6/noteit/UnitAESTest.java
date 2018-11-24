package io.github.danielpinto8zz6.noteit;

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import android.util.Base64;
import io.github.danielpinto8zz6.noteit.encryption.AESHelper;

import static org.junit.Assert.assertEquals;

public class UnitAESTest {

    private String msg = "123456";
    private String key = "abcdef";
    private String iv = "ABCDEF";
    private byte[] ans = new byte[0];
    private String ansBase64 = null;
    private byte[] deans = new byte[0];
    private String deansBase64 = null;

    @Before
    public void setUpEncryption() {
        try {
            ans = AESHelper.encrypt(iv, key, msg.getBytes());
            ansBase64 = AESHelper.encryptStrAndToBase64(iv, key, msg);
            new String(ans, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Encryption_isCorrect() {
        try {
            deans = AESHelper.decrypt(iv, key, ans);
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
