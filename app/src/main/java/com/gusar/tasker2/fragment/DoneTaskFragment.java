package com.gusar.tasker2.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gusar.tasker2.R;
import com.gusar.tasker2.adapter.DoneTasksAdapter;
import com.gusar.tasker2.database.DBHelper;
import com.gusar.tasker2.model.ModelTask;
import java.util.ArrayList;
import java.util.List;

public class DoneTaskFragment extends OldTaskFragment {
    OnTaskRestoreListener onTaskRestoreListener;

    public interface OnTaskRestoreListener {
        void onTaskRestore(ModelTask modelTask);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.onTaskRestoreListener = (OnTaskRestoreListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTaskRestoreListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_done_task, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDoneTasks);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.adapter = new DoneTasksAdapter(this);
        this.recyclerView.setAdapter(this.adapter);
        return rootView;
    }

    public void findTasks(String title) {
        checkAdapter();
        this.adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList();
        tasks.addAll(this.activity.dbHelper.query().getTasks("task_status = ? AND task_title LIKE ?", new String[]{Integer.toString(2), "%" + title + "%"}, DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask((ModelTask) tasks.get(i), false);
        }
    }

    public void addTaskFromDB() {
        checkAdapter();
        this.adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList();
        tasks.addAll(this.activity.dbHelper.query().getTasks(DBHelper.SELECTION_STATUS, new String[]{Integer.toString(2)}, DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask((ModelTask) tasks.get(i), false);
        }
    }

    public void addTask(ModelTask newTask, boolean saveToDB) {
        int position = -1;
        checkAdapter();
        for (int i = 0; i < this.adapter.getItemCount(); i++) {
            if (this.adapter.getItem(i).isTask()) {
                if (newTask.getDate() < ((ModelTask) this.adapter.getItem(i)).getDate()) {
                    position = i;
                    break;
                }
            }
        }
        if (position != -1) {
            this.adapter.addItem(position, newTask);
        } else {
            this.adapter.addItem(newTask);
        }
        if (saveToDB) {
            this.activity.dbHelper.saveTask(newTask);
        }
    }

    public void moveTask(ModelTask task) {
        if (task.getDate() != 0) {
//            this.alarmHelper.setAlarm(task);
        }
        this.onTaskRestoreListener.onTaskRestore(task);
    }

    public void checkAdapter() {
        if (this.adapter == null) {
            this.adapter = new DoneTasksAdapter(this);
            addTaskFromDB();
        }
    }
}
