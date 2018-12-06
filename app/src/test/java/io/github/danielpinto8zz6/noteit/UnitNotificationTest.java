package io.github.danielpinto8zz6.noteit;

import org.junit.Test;
import org.junit.Before;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notification.NotificationService;
import io.github.danielpinto8zz6.noteit.ui.EditNoteActivity;

import static io.github.danielpinto8zz6.noteit.notes.NotesManager.ACTIVE;
import static org.junit.Assert.assertEquals;


public class UnitNotificationTest extends Activity{

    private Note note = new Note(1, "Note-1", "Hello World!", "4/12/2018", null, "4/12/2018", "white", ACTIVE, null, 0);
    private Context mContext = null;
    Date now = new Date();
    Date notify = new Date();
    AlarmManager alarmManager = null;
    Calendar cal = null;
    PendingIntent pi = null;

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Before
    public void setUpNotification() {
        setmContext(this);
        notify.setSeconds(notify.getSeconds() + 5);
        note.setNotify_date(Utils.getDateTime(notify));

        alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        cal = Calendar.getInstance();
        Intent i = new Intent(mContext, NotificationService.class);
        i.putExtra("note_id", note.getId());
        pi = PendingIntent.getService(mContext, note.getId(), i, PendingIntent.FLAG_UPDATE_CURRENT);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);

    }

    @Test
    public void notificationSetInNote_test(){
        now.setSeconds(now.getSeconds()+5);
        assertEquals(note.getNotify_date(), Utils.getDateTime(now));
    }

    @Test
    public void notificationSetInAlarm_test(){
        assertEquals(note.getNotify_date(), alarmManager.toString());
    }



}
