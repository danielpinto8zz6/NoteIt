package io.github.danielpinto8zz6.noteit.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.danielpinto8zz6.noteit.R;
import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NotesManager;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private final Activity activity;

    private List<Integer> selectedIds = new ArrayList<>();
    private NotesManager notes;

    public NotesAdapter(NotesManager notes, Activity activity) {
        this.notes = notes;
        this.activity = activity;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.item_note, parent, false);

        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder notesViewHolder, int i) {

        Note note = notes.get(i);

        notesViewHolder.title.setText(note.getTitle());
        notesViewHolder.content.setText(note.getContent());

        String color = note.getColor();
        if (color != null && !Objects.equals(color, "")) {
            notesViewHolder.color.setBackgroundColor(Color.parseColor(color));
        } else {
            notesViewHolder.color.setBackgroundColor(Color.WHITE);
        }

        int id = note.getId();

        if (selectedIds.contains(id)) {
            //if item is selected then,set foreground color of FrameLayout.
            notesViewHolder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(activity, R.color.colorControlActivated)));
        } else {
            //else remove selected item color.
            notesViewHolder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(activity, android.R.color.transparent)));
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
        final ImageView color;

        NotesViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.item_note_title);
            content = view.findViewById(R.id.item_note_content);
            rootView = view.findViewById(R.id.root_view);
            color = view.findViewById(R.id.item_note_color);
        }
    }
}