package io.github.danielpinto8zz6.noteit.ui;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private final NotesAdapter mAdapter;
    private MainActivity mActivity;

    public SwipeToDeleteCallback(MainActivity activity, NotesAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mActivity = activity;
        mAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int position = viewHolder.getAdapterPosition();
        mActivity.archiveItem(position);
    }
}