package io.github.danielpinto8zz6.noteit.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import io.github.danielpinto8zz6.noteit.R;
import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NoteDao;
import io.github.danielpinto8zz6.noteit.ui.MainActivity;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ACTIVE;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "io.github.danielpinto8zz6.noteit.channelId";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, MainActivity.class);

        int id = intent.getIntExtra("id", -1);

        if (id == -1)
            return;

        Note note = NoteDao.loadRecordById(id);

        if (note == null || note.getStatus() != STATUS_ACTIVE) {
            // Don't notify since it's not active or doesn't exists (deleted)
            return;
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        Notification notification = builder.setContentTitle(context.getString(R.string.app_name))
                .setContentText((note.getTitle() != null) ? note.getTitle() : note.getContent())
                .setTicker(context.getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.app_name),
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notification);
    }
}