package io.github.danielpinto8zz6.noteit;

import android.app.Application;
import android.content.Context;

import com.evernote.android.job.JobManager;

import io.github.danielpinto8zz6.noteit.notes.DbManager;
import io.github.danielpinto8zz6.noteit.notification.NoteItJobCreator;

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DbManager.setConfig(context);
        JobManager.create(this).addJobCreator(new NoteItJobCreator());
    }

    public static Context getContext() {
        return context;
    }

}
