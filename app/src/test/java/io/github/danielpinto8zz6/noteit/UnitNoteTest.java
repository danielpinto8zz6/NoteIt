package io.github.danielpinto8zz6.noteit;

import org.junit.Before;

import org.junit.Test;

import io.github.danielpinto8zz6.noteit.notes.Note;

import static io.github.danielpinto8zz6.noteit.notes.NotesManager.ACTIVE;
import static org.junit.Assert.assertEquals;

public class UnitNoteTest {

    private Note note1;
    private Note note2;
    private Note note3;
    private Note note4 = null;
    //EditNoteActivity.addNoteTest("Note-1", "");

    @Before
    public void setUpNote() {
        note1 = new Note();
        note2 = new Note();
        note3 = new Note();
        note4 = new Note();
    }

    @Test
    public void NoteStatus_isCorrect() {
        assertEquals(java.util.Optional.ofNullable(ACTIVE), note1.getStatus());
    }

    @Test
    public void writeTitle_isCorrect() {
        assertEquals("Note-1", note1.getTitle());
    }

    @Test
    public void writeBody_isCorrect() {
        assertEquals("Hello world!", note1.getContent());
    }

    @Test
    public void NoteStatusIfBodyEmpty_isCorrect() {
        assertEquals(java.util.Optional.ofNullable(ACTIVE), note2.getStatus());
    }

    @Test
    public void TitleInBodyEmpty_isCorrect() {
        assertEquals("Empty body", note2.getTitle());
    }

    @Test
    public void BodyIsEmpty_isCorrect() {
        assertEquals("", note2.getContent());
    }

    @Test
    public void NoteStatusIfTitleEmpty_isCorrect() {
        assertEquals(java.util.Optional.ofNullable(ACTIVE), note3.getStatus());
    }

    @Test
    public void writeTitleEmoty_isCorrect() {
        assertEquals("", note3.getTitle());
    }

    @Test
    public void NoteEmoty_isCorrect() {
        assertEquals("Empty title", note3.getContent());
    }
}
