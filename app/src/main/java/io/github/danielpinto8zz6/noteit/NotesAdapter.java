package io.github.danielpinto8zz6.noteit;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NoteDao;

import static io.github.danielpinto8zz6.noteit.Constants.STATUS_FILED;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private Activity activity;
    private List<Note> notes;

    private Note mRecentlyFiledItem;
    private int mRecentlyFiledItemPosition;

    public NotesAdapter(List<Note> notes, Activity activity) {
        this.notes = notes;
        this.activity = activity;
    }

    public void setNotes (List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.item_note, parent, false);

        NotesViewHolder holder = new NotesViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder notesViewHolder, int i) {
        NotesViewHolder holder = (NotesViewHolder) notesViewHolder;

        Note note = notes.get(i);

        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void fileItem(int position) {
        mRecentlyFiledItem = notes.get(position);
        mRecentlyFiledItemPosition = position;
        Note note = notes.get(position);
        note.setStatus(STATUS_FILED);
        NoteDao.updateRecord(note);
        notes.remove(notes.get(position));
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = activity.findViewById(R.id.drawer_layout);
        final Snackbar snackbar = Snackbar.make(view, R.string.note_filed,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoFile();
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void undoFile() {
        notes.add(mRecentlyFiledItemPosition,
                mRecentlyFiledItem);
        notifyItemInserted(mRecentlyFiledItemPosition);
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView content;

        public NotesViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.item_note_title);
            content = (TextView) view.findViewById(R.id.item_note_content);
        }
    }
}