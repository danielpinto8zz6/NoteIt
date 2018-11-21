package io.github.danielpinto8zz6.noteit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.danielpinto8zz6.noteit.model.Note;

public class NotesAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Note> notes;

    public NotesAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_note, parent, false);

        NotesViewHolder holder = new NotesViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        NotesViewHolder holder = (NotesViewHolder) viewHolder;

        Note note  = notes.get(position) ;

        holder.title.setText(note.getTitle());
        holder.text.setText(note.getDescription());
//        holder.notifyDate.setText(note.getNotifyDate().toString());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}