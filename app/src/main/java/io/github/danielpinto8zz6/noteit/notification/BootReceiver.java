package io.github.danielpinto8zz6.noteit.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.github.danielpinto8zz6.noteit.Utils;
import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NoteDao;

public class BootReceiver extends BroadcastReceiver {
    public static final String NOTE_ID = "note_id";

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String currentDateTime = Utils.getCurrentDateTime();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ArrayList<Note> notes = NoteDao.loadAllRecords("status = ?", new String[]{"0"}, null, null, "create_date desc");


        for (Note note : notes) {
            Date date = Utils.getDateFromString(note.getNotify_date());
            Date currentDate = Calendar.getInstance().getTime();
            if (note.getNotify_date() != null && date.before(currentDate)) {
                int notification_id = note.getId();

                //Create the intent that is fired by AlarmManager
                Intent i = new Intent(context, NotificationService.class);
                i.putExtra(NOTE_ID, notification_id);

                PendingIntent pi = PendingIntent.getService(context, notification_id, i, PendingIntent.FLAG_UPDATE_CURRENT);

                Calendar cal = Calendar.getInstance();
                cal.setTime(Utils.getDateFromString(note.getNotify_date()));

                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            }
        }
    }
}