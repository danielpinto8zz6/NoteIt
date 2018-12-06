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
import android.widget.TextView;

import java.util.ArrayList;

import io.github.danielpinto8zz6.noteit.R;

public class TaskListAdapterView extends RecyclerView.Adapter<TaskListAdapterView.TaskListViewHolder> {
    private ArrayList<TaskListItem> taskList;
    private Context context;

    public TaskListAdapterView(Context context, ArrayList<TaskListItem> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_checklist_view, viewGroup, false);

        return new TaskListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder viewHolder, int i) {
        TaskListItem item = taskList.get(i);

        viewHolder.caption.setText(item.getCaption());
        viewHolder.checkBox.setChecked(item.isChecked());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskListViewHolder extends RecyclerView.ViewHolder {
        TextView caption;
        CheckBox checkBox;

        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);

            caption = itemView.findViewById(R.id.item_caption);
            checkBox = itemView.findViewById(R.id.item_checkbox);
        }
    }
}