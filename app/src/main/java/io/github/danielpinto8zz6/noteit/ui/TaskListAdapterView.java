package io.github.danielpinto8zz6.noteit.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import io.github.danielpinto8zz6.noteit.R;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {
    private ArrayList<TaskListItem> taskList;
    private Context context;
    private boolean edit = true;

    public TaskListAdapter(Context context, ArrayList<TaskListItem> taskList, boolean edit) {
        this.context = context;
        this.taskList = taskList;
        this.edit = edit;
    }

    public TaskListAdapter(Context context, ArrayList<TaskListItem> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_checklist, viewGroup, false);

        return new TaskListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder viewHolder, int i) {
        TaskListItem item = taskList.get(i);

        viewHolder.caption.setText(item.getCaption());
        viewHolder.checkBox.setChecked(item.isChecked());

        viewHolder.caption.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                viewHolder.closeButton.setVisibility(View.VISIBLE);
            } else {
                viewHolder.closeButton.setVisibility(View.INVISIBLE);
                item.setCaption(viewHolder.caption.getText().toString());
            }
        });

        viewHolder.checkBox.setOnClickListener(v -> {
            item.setChecked(viewHolder.checkBox.isChecked());
        });

        viewHolder.closeButton.setOnClickListener(v -> {
            viewHolder.caption.setOnFocusChangeListener(null);
            taskList.remove(item);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskListViewHolder extends RecyclerView.ViewHolder {
        EditText caption;
        CheckBox checkBox;
        ImageButton closeButton;

        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);

            caption = itemView.findViewById(R.id.item_caption);
            checkBox = itemView.findViewById(R.id.item_checkbox);
            closeButton = itemView.findViewById(R.id.item_close);
        }
    }
}