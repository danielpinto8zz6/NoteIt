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
    private final String key2 = "@#hello123$";
    private final byte[] bMsg = msg.getBytes();
    private final byte[] bKey = key.getBytes();
    private final byte[] bIv = iv.getBytes();
    private byte[] ans = new byte[0];
    private byte[] ans2 = new byte[0];
    private byte[] bAns = new byte[0];
    private String ansBase64 = null;
    private String deansBase64 = null;
    private byte[] deByteAns = new byte[0];

    @Before
    public void setUpEncryption() {
        try {
            ans = AESHelper.encrypt(iv, key, msg.getBytes());
            ans2 = AESHelper.encrypt(iv, key2, msg.getBytes());
            ansBase64 = AESHelper.encryptStrAndToBase64(iv, key, msg);
            bAns = AESHelper.encrypt(bIv, bKey, bMsg);
            new String(ans, "UTF-8");
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
    public void Encryption2_isCorrect() {
        try {
            byte[] deans2 = AESHelper.decrypt(iv, key2, ans2);
            assertEquals(msg, new String(deans2, "UTF-8"));
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

    @Test
    public void EncryptionBytes_isCorrect() {
        try {
            deByteAns = AESHelper.decrypt(bIv, bKey, bAns);
            assertEquals(bMsg, deByteAns);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    Ask
     */
    @Test
    public void EncryptionFiles_isCorrect() {
        try {
            AESHelper.encryptfile("path", key);
            assertEquals(bMsg, deByteAns);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
