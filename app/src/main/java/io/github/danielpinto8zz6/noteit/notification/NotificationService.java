package io.github.danielpinto8zz6.noteit.notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import io.github.danielpinto8zz6.noteit.R;
import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NoteDao;
import io.github.danielpinto8zz6.noteit.ui.EditNoteActivity;
import io.github.danielpinto8zz6.noteit.ui.EditTaskActivity;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ACTIVE;

public class NotificationService extends IntentService {
    private static final String CHANNEL_ID = "io.github.danielpinto8zz6.noteit.channelId";

    public NotificationService() {
        super("Notification service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int id = intent.getIntExtra("note_id", -1);
        if (id != -1) {
            Note note = NoteDao.loadRecordById(id);

            if (note == null || note.getStatus() != STATUS_ACTIVE) {
                // Don't notify since it's not active or doesn't exists (deleted)
                return;
            }

            int type = note.getType();
            Intent i = null;
            switch (type) {
                case 0:
                    i = new Intent(getBaseContext(), EditNoteActivity.class);
                    i.putExtra("note", note);
                    break;
                case 1:
                    i = new Intent(getApplication(), EditTaskActivity.class);
                    i.putExtra("note", note);
                    break;
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder mBuilder = new Notification.Builder(getBaseContext());
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(note.getTitle())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder.setChannelId(CHANNEL_ID);
            }

            // Sets an ID for the notification
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(getBaseContext().NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        getString(R.string.app_name),
                        IMPORTANCE_DEFAULT
                );
                mNotifyMgr.createNotificationChannel(channel);
            }

            mNotifyMgr.notify(note.getId(), mBuilder.build());
            note.setNotify_date(null);
            NoteDao.updateRecord(note);
        }
    }
}