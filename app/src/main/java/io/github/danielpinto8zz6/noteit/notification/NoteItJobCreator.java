package io.github.danielpinto8zz6.noteit.notification;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class NoteItJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case ShowNotificationJob.TAG:
                return new ShowNotificationJob();
            default:
                return null;
        }
    }
}