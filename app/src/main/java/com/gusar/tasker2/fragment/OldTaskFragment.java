package com.gusar.tasker2.fragment;

import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
//import com.gusar.tasker2.AlarmHelper;
import com.gusar.tasker2.MainActivity;
import com.gusar.tasker2.MainActivityReminder;
import com.gusar.tasker2.R;
import com.gusar.tasker2.adapter.TaskAdapter;
import com.gusar.tasker2.dialog.EditTaskDialogFragment;
import com.gusar.tasker2.model.Item;
import com.gusar.tasker2.model.ModelTask;

public abstract class OldTaskFragment extends Fragment {
    public MainActivityReminder activity;
    protected TaskAdapter adapter;
//    protected AlarmHelper alarmHelper;
    protected LayoutManager layoutManager;
    protected RecyclerView recyclerView;

    public abstract void addTask(ModelTask modelTask, boolean z);

    public abstract void addTaskFromDB();

    public abstract void checkAdapter();

    public abstract void findTasks(String str);

    public abstract void moveTask(ModelTask modelTask);

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            this.activity = (MainActivityReminder) getActivity();
        }
//        this.alarmHelper = AlarmHelper.getInstance();
        addTaskFromDB();
    }

    public void updateTask(ModelTask task) {
        this.adapter.updateTask(task);
    }

    public void removeTaskDialog(int location) {
        Builder dialogBuilder = new Builder(getActivity());
        dialogBuilder.setMessage(R.string.dialog_removing_message);
        Item item = this.adapter.getItem(location);
        if (item.isTask()) {
            final long timeStamp = ((ModelTask) item).getTimeStamp();
            final boolean[] isRemoved = new boolean[]{false};
            final int i = location;
            dialogBuilder.setPositiveButton(R.string.dialog_remove, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    OldTaskFragment.this.adapter.removeItem(i);
                    isRemoved[0] = true;
                    Snackbar snackbar = Snackbar.make(OldTaskFragment.this.getActivity().findViewById(R.id.coordinator), R.string.removed, 0);
                    snackbar.setAction(R.string.dialog_cancel, new View.OnClickListener() {
                        public void onClick(View v) {
                            OldTaskFragment.this.addTask(OldTaskFragment.this.activity.dbHelper.query().getTask(timeStamp), false);
                            isRemoved[0] = false;
                        }
                    });
                    snackbar.getView().addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                        public void onViewAttachedToWindow(View v) {
                        }

                        public void onViewDetachedFromWindow(View v) {
                            if (isRemoved[0]) {
//                                OldTaskFragment.this.alarmHelper.removeAlarm(timeStamp);
                                OldTaskFragment.this.activity.dbHelper.removeTask(timeStamp);
                            }
                        }
                    });
                    snackbar.show();
                    dialog.dismiss();
                }
            });
            dialogBuilder.setNegativeButton(R.string.dialog_cancel, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        dialogBuilder.show();
    }

    public void showTaskEditDialog(ModelTask task) {
        EditTaskDialogFragment.newInstance(task).show(getActivity().getFragmentManager(), "EditingTaskDialogFragment");
    }

    public void removeAllTasks() {
        this.adapter.removeAllItems();
    }
}
