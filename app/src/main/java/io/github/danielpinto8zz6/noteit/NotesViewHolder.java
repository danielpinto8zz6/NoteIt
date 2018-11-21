package io.github.danielpinto8zz6.noteit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NotesViewHolder extends RecyclerView.ViewHolder {

    final TextView title;
    final TextView text;
//    final TextView notifyDate;

    public NotesViewHolder(View view) {
        super(view);

        title = (TextView) view.findViewById(R.id.item_note_title);
        text = (TextView) view.findViewById(R.id.item_note_description);
//        notifyDate = (TextView) view.findViewById(R.id.note_notify_date);
    }

}
