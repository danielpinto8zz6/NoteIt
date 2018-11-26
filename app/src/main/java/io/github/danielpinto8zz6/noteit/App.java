package io.github.danielpinto8zz6.noteit;

import android.app.Application;
import android.content.Context;

import io.github.danielpinto8zz6.noteit.notes.DbManager;

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DbManager.setConfig(context);
    }

    public static Context getContext() {
        return context;
    }

}
