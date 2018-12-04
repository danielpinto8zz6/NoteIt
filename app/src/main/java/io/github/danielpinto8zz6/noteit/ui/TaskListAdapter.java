package io.github.danielpinto8zz6.noteit.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import io.github.danielpinto8zz6.noteit.R;

public class TaskListAdapter extends BaseAdapter {
    private ArrayList<TaskListItem> taskList;
    private LayoutInflater mInflater;

    public TaskListAdapter(Context context, ArrayList<TaskListItem> taskList) {
        this.taskList = taskList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notifyDataSetChanged();
    }

    public int getCount() {
        return taskList.size();
    }

    public TaskListItem getItem(int position) {
        return taskList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_checklist, null);

            holder.caption = (EditText) convertView.findViewById(R.id.item_caption);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.item_checkbox);
            holder.closeButton = (ImageButton) convertView.findViewById(R.id.item_close);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //Fill EditText with the value you have in data source
        holder.caption.setText(taskList.get(position).getCaption());
        holder.checkBox.setChecked(taskList.get(position).isChecked());

        //we need to update adapter once we finish with editing
        holder.caption.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                holder.closeButton.setVisibility(View.INVISIBLE);
                final EditText Caption = (EditText) v;
                TaskListItem item = taskList.get(position);
                if (item != null)
                    item.setCaption(Caption.getText().toString());
            } else {
                holder.closeButton.setVisibility(View.VISIBLE);
            }
        });

        holder.checkBox.setOnClickListener(v -> {
            final CheckBox checkBox = (CheckBox) v;
            TaskListItem item = taskList.get(position);
            if (item != null)
                item.setChecked(checkBox.isChecked());
        });

        holder.closeButton.setOnClickListener(v -> {
            holder.caption.setOnFocusChangeListener(null);
            taskList.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}

class ViewHolder {
    EditText caption;
    CheckBox checkBox;
    ImageButton closeButton;
}