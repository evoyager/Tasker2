package com.gusar.tasker2.dialog;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.gusar.tasker2.R;
import com.gusar.tasker2.Utils;
import com.gusar.tasker2.model.ModelTask;

import java.util.Calendar;

/**
 * Created by Evgeniy on 25.12.2015.
 */
public class AddingTaskDialogFragment extends DialogFragment {
    private AddingTaskListener addingTaskListener;

    public interface AddingTaskListener {
        void onTaskAdded(ModelTask modelTask);

        void onTaskAddingCancel();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.addingTaskListener = (AddingTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditingTaskListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Builder builder = new Builder(getActivity());
        builder.setTitle(R.string.dialog_title);
        View container = getActivity().getLayoutInflater().inflate(R.layout.dialog_task, null);
        TextInputLayout tilTitle = (TextInputLayout) container.findViewById(R.id.tilDialogTaskTitle);
        final EditText etTitle = tilTitle.getEditText();
        TextInputLayout tilDate = (TextInputLayout) container.findViewById(R.id.tilDialogTaskDate);
        final EditText etDate = tilDate.getEditText();
        TextInputLayout tilTime = (TextInputLayout) container.findViewById(R.id.tilDialogTaskTime);
        final EditText etTime = tilTime.getEditText();
        Spinner spPriority = (Spinner) container.findViewById(R.id.spDialogTaskPriority);
        tilTitle.setHint(getResources().getString(R.string.task_title));
        tilDate.setHint(getResources().getString(R.string.task_date));
        tilTime.setHint(getResources().getString(R.string.task_time));
        builder.setView(container);
        final ModelTask task = new ModelTask();
        spPriority.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.select_dialog_item, getResources().getStringArray(R.array.priority_levels)));
        spPriority.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.setPriority(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final Calendar calendar = Calendar.getInstance();
//        calendar.set(11, calendar.get(11) + 1);
        etDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDate.length() == 0) {
                    etDate.setText(" ");
                }
                new DatePickerFragment() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDate.setText(Utils.getDate(calendar.getTimeInMillis()));
                    }

                    public void onCancel(DialogInterface dialog) {
                        etDate.setText(null);
                    }
                }.show(AddingTaskDialogFragment.this.getFragmentManager(), "DatePickerFragment");
            }
        });

        etTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTime.length() == 0) {
                    etTime.setText(" ");
                }
                new TimePickerFragment() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        etTime.setText(Utils.getTime(calendar.getTimeInMillis()));
                    }

                    public void onCancel(DialogInterface dialog) {
                        etTime.setText(null);
                    }
                }.show(AddingTaskDialogFragment.this.getFragmentManager(), "TimePickerFragment");
            }
        });

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                task.setTitle(etTitle.getText().toString());
                task.setStatus(1);
                if (!(etDate.length() == 0 && etTime.length() == 0)) {
                    task.setDate(calendar.getTimeInMillis());
//                    AlarmHelper.getInstance.setAlarm(task);
                }
                task.setStatus(1);
                AddingTaskDialogFragment.this.addingTaskListener.onTaskAdded(task);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener () {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddingTaskDialogFragment.this.addingTaskListener.onTaskAddingCancel();
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        final TextInputLayout textInputLayout = tilTitle;
        alertDialog.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(-1);
                if (etTitle.length() == 0) {
                    positiveButton.setEnabled(false);
                    textInputLayout.setError(AddingTaskDialogFragment.this.getResources().getString(R.string.dialog_error_empty_title));
                }
                etTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            textInputLayout.setError(AddingTaskDialogFragment.this.getResources().getString(R.string.dialog_error_empty_title));
                            return;
                        }
                        positiveButton.setEnabled(true);
                        textInputLayout.setErrorEnabled(false);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });


        return alertDialog;
    }
}
