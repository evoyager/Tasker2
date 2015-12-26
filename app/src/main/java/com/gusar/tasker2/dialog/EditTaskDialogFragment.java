package com.gusar.tasker2.dialog;

/**
 * Created by Evgeniy on 26.12.2015.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
//import com.gusar.tasker2.AlarmHelper;
import com.gusar.tasker2.R;
import com.gusar.tasker2.Utils;
import com.gusar.tasker2.model.ModelTask;
import java.util.Calendar;

public class EditTaskDialogFragment extends DialogFragment {
    private EditingTaskListener editingTaskListener;

    public interface EditingTaskListener {
        void onTaskEdited(ModelTask modelTask);
    }

    public static EditTaskDialogFragment newInstance(ModelTask task) {
        EditTaskDialogFragment editTaskDialogFragment = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", task.getTitle());
        args.putLong("date", task.getDate());
        args.putInt("priority", task.getPriority());
        args.putLong("timeStamp", task.getTimeStamp());
        editTaskDialogFragment.setArguments(args);
        return editTaskDialogFragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.editingTaskListener = (EditingTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditingTaskListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final ModelTask task = new ModelTask(args.getString("title"), args.getLong("date", 0), args.getInt("priority", 0), 0, args.getLong("timeStamp", 0));
        Builder builder = new Builder(getActivity());
        builder.setTitle(R.string.dialog_editing_title);
        View container = getActivity().getLayoutInflater().inflate(R.layout.dialog_task, null);
        TextInputLayout tilTitle = (TextInputLayout) container.findViewById(R.id.tilDialogTaskTitle);
        final EditText etTitle = tilTitle.getEditText();
        TextInputLayout tilDate = (TextInputLayout) container.findViewById(R.id.tilDialogTaskDate);
        final EditText etDate = tilDate.getEditText();
        TextInputLayout tilTime = (TextInputLayout) container.findViewById(R.id.tilDialogTaskTime);
        final EditText etTime = tilTime.getEditText();
        Spinner spPriority = (Spinner) container.findViewById(R.id.spDialogTaskPriority);
        etTitle.setText(task.getTitle());
        etTitle.setSelection(etTitle.length());
        if (task.getDate() != 0) {
            etDate.setText(Utils.getDate(task.getDate()));
            etTime.setText(Utils.getTime(task.getDate()));
        }
        tilTitle.setHint(getResources().getString(R.string.task_title));
        tilDate.setHint(getResources().getString(R.string.task_date));
        tilTime.setHint(getResources().getString(R.string.task_time));
        builder.setView(container);
        spPriority.setAdapter(new ArrayAdapter(getActivity(), 17367049, getResources().getStringArray(R.array.priority_levels)));
        spPriority.setSelection(task.getPriority());
        spPriority.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                task.setPriority(position);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        final Calendar calendar = Calendar.getInstance();
//        calendar.set(11, calendar.get(11) + 1);
        if (!(etDate.length() == 0 && etTime.length() == 0)) {
            calendar.setTimeInMillis(task.getDate());
        }
        final Calendar calendar2 = calendar;
        etDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (etDate.length() == 0) {
                    etDate.setText(" ");
                }
                new DatePickerFragment() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar2.set(Calendar.YEAR, year);
                        calendar2.set(Calendar.MONTH, monthOfYear);
                        calendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDate.setText(Utils.getDate(calendar2.getTimeInMillis()));
                    }

                    public void onCancel(DialogInterface dialog) {
                        etDate.setText(null);
                    }
                }.show(EditTaskDialogFragment.this.getFragmentManager(), "DatePickerFragment");
            }
        });
//        calendar2 = calendar;
        etTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (etTime.length() == 0) {
                    etTime.setText(" ");
                }
                new TimePickerFragment() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        etTime.setText(Utils.getTime(calendar2.getTimeInMillis()));
                    }

                    public void onCancel(DialogInterface dialog) {
                        etTime.setText(null);
                    }
                }.show(EditTaskDialogFragment.this.getFragmentManager(), "TimePickerFragment");
            }
        });
        final ModelTask modelTask = task;
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                modelTask.setTitle(etTitle.getText().toString());
                modelTask.setStatus(1);
                if (etDate.length() == 0 && etTime.length() == 0) {
                    modelTask.setDate(0);
                } else {
                    modelTask.setDate(calendar.getTimeInMillis());
//                    AlarmHelper.getInstance().setAlarm(modelTask);
                }
                modelTask.setStatus(1);
                EditTaskDialogFragment.this.editingTaskListener.onTaskEdited(modelTask);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        final TextInputLayout textInputLayout = tilTitle;
        alertDialog.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(-1);
                if (etTitle.length() == 0) {
                    positiveButton.setEnabled(false);
                    textInputLayout.setError(EditTaskDialogFragment.this.getResources().getString(R.string.dialog_error_empty_title));
                }
                etTitle.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            textInputLayout.setError(EditTaskDialogFragment.this.getResources().getString(R.string.dialog_error_empty_title));
                            return;
                        }
                        positiveButton.setEnabled(true);
                        textInputLayout.setErrorEnabled(false);
                    }

                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        });
        return alertDialog;
    }
}