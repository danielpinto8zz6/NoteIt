package io.github.danielpinto8zz6.noteit;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;

import io.github.danielpinto8zz6.noteit.encryption.AESHelper;

import static org.junit.Assert.assertEquals;

public class UnitAESTest {

    private final String path = "C:\\DATOS\\Universidad\\3-Tercero\\1\\GPS\\Demo-Note-it\\NoteIt\\app\\src\\main\\res\\EncriptionTestFile.txt";
    private final String password = "@#hello123$";
    private final String pathEncrypt = "C:\\DATOS\\Universidad\\3-Tercero\\1\\GPS\\Demo-Note-it\\NoteIt\\app\\src\\main\\res\\EncriptionTestFile.txt.crypt";
    private final String outPath = "C:\\DATOS\\Universidad\\3-Tercero\\1\\GPS\\Demo-Note-it\\NoteIt\\app\\src\\main\\res\\EncriptionTestFileOut.txt";
    private final File file = new File(path);
    private static String text = "";
    private static String textOut = "";


    @Before
    public void setUpEncryption() {
        try {
            //Read the content of the file to be encrypted
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((br.readLine() != null)) {
                text = text + br.readLine();
            }
            br.close();

            //Encrypt
            AESHelper.encryptfile(path, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Encryption_isCorrect() {
        try {
            //Decrypt
            AESHelper.decrypt(pathEncrypt, password, outPath);

            File fileOut = new File (outPath);

            BufferedReader br = new BufferedReader(new FileReader(fileOut));
            while ((br.readLine() != null)) {
                textOut = textOut + br.readLine();
            }
            br.close();

            assertEquals(text, textOut);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
