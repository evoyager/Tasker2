package com.gusar.tasker2.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gusar.tasker2.R;
import com.gusar.tasker2.adapter.CurrentTasksAdapter;
import com.gusar.tasker2.database.DBHelper;
import com.gusar.tasker2.model.ModelSeparator;
import com.gusar.tasker2.model.ModelTask;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CurrentTaskFragment extends OldTaskFragment {
    OnTaskDoneListener onTaskDoneListener;

    public interface OnTaskDoneListener {
        void onTaskDone(ModelTask modelTask);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.onTaskDoneListener = (OnTaskDoneListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTaskDoneListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.rvCurrentTasks);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.adapter = new CurrentTasksAdapter(this);
        this.recyclerView.setAdapter(this.adapter);
        return rootView;
    }

    public void findTasks(String title) {
        checkAdapter();
        this.adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList();
        tasks.addAll(this.activity.dbHelper.query().getTasks("task_title LIKE ? AND task_status = ? OR task_status = ?", new String[]{"%" + title + "%", Integer.toString(1), Integer.toString(0)}, DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask((ModelTask) tasks.get(i), false);
        }
    }

    public void addTaskFromDB() {
        checkAdapter();
        this.adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList();
        tasks.addAll(this.activity.dbHelper.query().getTasks("task_status = ? OR task_status = ?", new String[]{Integer.toString(1), Integer.toString(0)}, DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask((ModelTask) tasks.get(i), false);
        }
    }

    public void addTask(ModelTask newTask, boolean saveToDB) {
        int position = -1;
        ModelSeparator separator = null;
        checkAdapter();
        for (int i = 0; i < this.adapter.getItemCount(); i++) {
            if (this.adapter.getItem(i).isTask()) {
                if (newTask.getDate() < ((ModelTask) this.adapter.getItem(i)).getDate()) {
                    position = i;
                    break;
                }
            }
        }
        if (newTask.getDate() != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(newTask.getDate());
            if (calendar.get(6) < Calendar.getInstance().get(6) && calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                newTask.setDateStatus(ModelSeparator.TYPE_OVERDUE);
                if (!this.adapter.containsSeparatorOverdue) {
                    this.adapter.containsSeparatorOverdue = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_OVERDUE);
                }
            } else if (calendar.get(6) == Calendar.getInstance().get(6) && calendar.get(1) == Calendar.getInstance().get(1)) {
                newTask.setDateStatus(ModelSeparator.TYPE_TODAY);
                if (!this.adapter.containsSeparatorToday) {
                    this.adapter.containsSeparatorToday = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_TODAY);
                }
            } else if (calendar.get(6) == Calendar.getInstance().get(6) + 1 && calendar.get(1) == Calendar.getInstance().get(1)) {
                newTask.setDateStatus(ModelSeparator.TYPE_TOMORROW);
                if (!this.adapter.containsSeparatorTomorrow) {
                    this.adapter.containsSeparatorTomorrow = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_TOMORROW);
                }
            } else if (calendar.get(6) > Calendar.getInstance().get(6) + 1 || calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
                newTask.setDateStatus(ModelSeparator.TYPE_FUTURE);
                if (!this.adapter.containsSeparatorFuture) {
                    this.adapter.containsSeparatorFuture = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_FUTURE);
                }
            }
        }
        if (position != -1) {
            if (!this.adapter.getItem(position - 1).isTask()) {
                if (position - 2 < 0 || !this.adapter.getItem(position - 2).isTask()) {
                    if (position - 2 < 0 && newTask.getDate() == 0) {
                        position--;
                    }
                } else if (((ModelTask) this.adapter.getItem(position - 2)).getDateStatus() == newTask.getDateStatus()) {
                    position--;
                }
            }
            if (separator != null) {
                this.adapter.addItem(position - 1, separator);
            }
            this.adapter.addItem(position, newTask);
        } else {
            if (separator != null) {
                this.adapter.addItem(separator);
            }
            this.adapter.addItem(newTask);
        }
        if (saveToDB) {
            this.activity.dbHelper.saveTask(newTask);
        }
    }

    public void moveTask(ModelTask task) {
        this.alarmHelper.removeAlarm(task.getTimeStamp());
        this.onTaskDoneListener.onTaskDone(task);
    }

    public void checkAdapter() {
        if (this.adapter == null) {
            this.adapter = new CurrentTasksAdapter(this);
            addTaskFromDB();
        }
    }
}
