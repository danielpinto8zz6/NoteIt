package io.github.danielpinto8zz6.noteit.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.danielpinto8zz6.noteit.R;
import io.github.danielpinto8zz6.noteit.notes.Note;
import io.github.danielpinto8zz6.noteit.notes.NotesManager;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private final Context context;

    private List<Integer> selectedIds = new ArrayList<>();
    private NotesManager notes;

    public NotesAdapter(NotesManager notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(context)
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

        String notifyDate = note.getNotify_date();
        if (notifyDate != null) {
            notesViewHolder.notify_date.setText(note.getNotify_date());
            notesViewHolder.notify_date.setVisibility(View.VISIBLE);
        } else {
            notesViewHolder.notify_date.setVisibility(View.GONE);
        }

        if (note.getType() == 1) {
            notesViewHolder.content.setVisibility(View.GONE);
            notesViewHolder.listView.setVisibility(View.VISIBLE);

            ArrayList<TaskListItem> taskList = new ArrayList<>();

            try {
                JSONArray content = new JSONArray(note.getContent());
                for (int j = 0; j < content.length(); j++) {
                    JSONObject o = content.getJSONObject(j);
                    taskList.add(new TaskListItem(o.getBoolean("checked"), o.getString("name")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            TaskListAdapterView listAdapter = new TaskListAdapterView(context, taskList);
            notesViewHolder.listView.setAdapter(listAdapter);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            notesViewHolder.listView.setLayoutManager(mLayoutManager);
            listAdapter = new TaskListAdapterView(context, taskList);
            notesViewHolder.listView.setAdapter(listAdapter);
        } else {
            notesViewHolder.content.setVisibility(View.VISIBLE);
            notesViewHolder.listView.setVisibility(View.GONE);
        }

        int id = note.getId();

        if (selectedIds.contains(id)) {
            //if item is selected then,set foreground color of FrameLayout.
            notesViewHolder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(context, R.color.colorControlActivated)));
        } else {
            //else remove selected item color.
            notesViewHolder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
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
        final TextView notify_date;
        final RecyclerView listView;
        final FrameLayout color;

        NotesViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.item_note_title);
            content = view.findViewById(R.id.item_note_content);
            rootView = view.findViewById(R.id.root_view);
            notify_date = view.findViewById(R.id.item_note_notify_date);
            color = view.findViewById(R.id.item_note_color);
            listView = view.findViewById(R.id.task_list);
        }
    }
}