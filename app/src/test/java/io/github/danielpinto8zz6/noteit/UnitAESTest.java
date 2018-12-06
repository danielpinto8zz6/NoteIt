package io.github.danielpinto8zz6.noteit;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;

import io.github.danielpinto8zz6.noteit.encryption.AESHelper;

import static org.junit.Assert.assertEquals;

public class UnitAESTest {

    private final File file = new File("C:\\DATOS\\Universidad\\3-Tercero\\1\\GPS\\Demo-Note-it\\NoteIt\\app\\src\\main\\res\\EncriptionTestFile.txt");
    private final String path2 = "C:\\DATOS\\Universidad\\3-Tercero\\1\\GPS\\Demo-Note-it\\NoteIt\\app\\src\\main\\res";
    private final String path3 = "C:\\DATOS\\Universidad\\3-Tercero\\1\\GPS\\Demo-Note-it\\NoteIt\\app\\src\\main\\res\\EncriptionTestFileEmpty.txt";
    private final String path4 = "C:\\DATOS\\Universidad\\3-Tercero\\1\\GPS\\Demo-Note-it\\NoteIt\\app\\src\\main\\res\\EncriptionTestFileEmpty.cryp";
    private final String path5 = "C:\\DATOS\\Universidad\\3-Tercero\\1\\GPS\\Demo-Note-it\\NoteIt\\app\\src\\main\\res\\EncriptionTestFileEmptyOut.txt";
    private final String password = "@#hello123$";
    private final String outPath = "C:\\DATOS\\Universidad\\3-Tercero\\1\\GPS\\Demo-Note-it\\NoteIt\\app\\src\\main\\res\\EncriptionTestFileOut.txt";
    private final File file2 = new File(path2);
    private static String text = "";
    private static String textOut = "";
    public ExpectedException exception = ExpectedException.none();


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
            AESHelper.encryptfile(file.getPath(), password);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Encryption_isCorrect() {
        try {
            File fileEncrypt = new File("C:\\DATOS\\Universidad\\3-Tercero\\1\\GPS\\Demo-Note-it\\NoteIt\\app\\src\\main\\res\\EncriptionTestFile.crypy");
            //Decrypt
            AESHelper.decrypt(fileEncrypt.getPath(), password, outPath);

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

    @Test
    public void EncriptionIfPathWrong(){
        try {
            exception.expect(FileNotFoundException.class);
            AESHelper.encryptfile(path2, password);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void DecriptionIfPathWrong(){
        try {
            exception.expect(FileNotFoundException.class);
            AESHelper.encryptfile(path2, password);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void DecriptionIfFileEmpty(){
        try {
            AESHelper.encryptfile(path3, password);
            AESHelper.decrypt(path4, password, path5);

            File fileOut = new File (outPath);

            BufferedReader br = new BufferedReader(new FileReader(fileOut));
            while ((br.readLine() != null)) {
                textOut = textOut + br.readLine();
            }
            br.close();

            assertEquals("", textOut);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
