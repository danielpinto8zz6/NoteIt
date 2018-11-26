package io.github.danielpinto8zz6.noteit.ui;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.danielpinto8zz6.noteit.R;
import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NoteDao;
import io.github.danielpinto8zz6.noteit.notes.NotesManager;

import static io.github.danielpinto8zz6.noteit.Constants.STATUS_ARCHIVED;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private final Activity activity;

    private List<Integer> selectedIds = new ArrayList<>();
    private NotesManager notes;

    public NotesAdapter(NotesManager notes, Activity activity) {
        this.notes = notes;
        this.activity = activity;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.item_note, parent, false);

        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder notesViewHolder, int i) {
        NotesViewHolder holder = notesViewHolder;

        Note note = notes.get(i);

        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());

        int id = note.getId();

        if (selectedIds.contains(id)) {
            //if item is selected then,set foreground color of FrameLayout.
            holder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(activity, R.color.colorControlActivated)));
        } else {
            //else remove selected item color.
            holder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)));
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Note getItem(int position) {
        return notes.get(position);
    }

    public void setSelectedIds(List<Integer> selectedIds) {
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        final CardView rootView;
        final TextView title;
        final TextView content;

        NotesViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.item_note_title);
            content = view.findViewById(R.id.item_note_content);
            rootView = view.findViewById(R.id.root_view);
        }
    }
}