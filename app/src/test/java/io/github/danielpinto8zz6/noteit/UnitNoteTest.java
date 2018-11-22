package io.github.danielpinto8zz6.noteit;

import org.junit.Before;
import io.github.danielpinto8zz6.noteit.model.Note;
import org.junit.Test;

import static io.github.danielpinto8zz6.noteit.model.Note.Status.ACTIVE;
import static org.junit.Assert.*;

public class UnitNoteTest {

    Note note1 = null;

    @Before
    public void setUpNote() {
        note1 = new Note("Note-1", "Hello world!");
    }

    @Test
    public void NoteStatus_isCorrect() {
        assertEquals(ACTIVE, note1.getStatus());
    }

    @Test
    public void writeTitle_isCorrect() {
        assertEquals("Note-1", note1.getTitle());
    }

    @Test
    public void writeBody_isCorrect() {
        assertEquals("Hello world!", note1.getDescription());
    }

}
