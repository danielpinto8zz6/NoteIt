package io.github.danielpinto8zz6.noteit;

import org.junit.Before;

import org.junit.Test;

import io.github.danielpinto8zz6.noteit.notes.Note;

import static io.github.danielpinto8zz6.noteit.notes.NotesManager.ACTIVE;
import static org.junit.Assert.assertEquals;

import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.ui.EditNoteActivity;

import static io.github.danielpinto8zz6.noteit.notes.NotesManager.ACTIVE;
import static org.junit.Assert.assertEquals;

public class UnitNoteTest {

    private Note note1;
    private Note note2;
    private Note note3;
    //EditNoteActivity.addNoteTest("Note-1", "");

    @Before
    public void setUpNote() {
        note1 = new Note(1, "Note-1", "Hello World!", "4/12/2018", null, "4/12/2018", "white", ACTIVE, null, 0);
        note2 = new Note(2, "Note-2", "", "4/12/2018", null, "4/12/2018", "white", ACTIVE, null, 0);
        note3 = new Note(3, "", "Note-3", "4/12/2018", null, "4/12/2018", "white", ACTIVE, null, 0);

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
        assertEquals("Hello World!", note1.getContent());
    }

    @Test
    public void modDate_isCorrect() {
        assertEquals("4/12/2018", note1.getEdited_date());
    }

    @Test
    public void color_isCorrect() {
        assertEquals("white", note1.getColor());
    }

    @Test
    public void noteStatusIfBodyEmpty_isCorrect() {
        assertEquals(java.util.Optional.ofNullable(ACTIVE), note2.getStatus());
    }

    @Test
    public void titleInBodyEmpty_isCorrect() {
        assertEquals("Note-2", note2.getTitle());
    }

    @Test
    public void bodyIsEmpty_isCorrect() {
        assertEquals("", note2.getContent());
    }

    @Test
    public void titleInEmptyTitle_isCorrect() {
        assertEquals("", note3.getTitle());
    }

    @Test
    public void bodyInEmptyTitle_isCorrect() {
        assertEquals("Note-3", note3.getContent());
    }
}