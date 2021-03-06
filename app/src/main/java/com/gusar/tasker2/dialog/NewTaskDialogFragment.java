package com.gusar.tasker2.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Dialog;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.gusar.tasker2.R;
import com.gusar.tasker2.Utils;
import com.gusar.tasker2.model.ModelTask;

import java.util.Calendar;

/**
 * Created by igusar on 12/28/15.
 */
public class NewTaskDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private NewTaskListener newTaskListener;

    public interface NewTaskListener {
        void onTaskAdded(ModelTask modelTask);

        void onTaskAddingCancel();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.newTaskListener = (NewTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditingTaskListener");
        }
    }

    int DIALOG_DATE = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("New task").setPositiveButton(R.string.newtask_ok, this)
                .setNegativeButton(R.string.newtask_cancel, this);
        View container = getActivity().getLayoutInflater().inflate(R.layout.new_task, null);
        final TextInputLayout tilTitle = (TextInputLayout) container.findViewById(R.id.tilNewTaskTitle);
        TextInputLayout tilDate = (TextInputLayout) container.findViewById(R.id.tilNewTaskDate);
        TextInputLayout tilTime = (TextInputLayout) container.findViewById(R.id.tilNewTaskTime);
        final EditText etTitle = (EditText) tilTitle.getEditText();
        final EditText etDate = (EditText) tilDate.getEditText();
        final EditText etTime = (EditText) tilTime.getEditText();
        tilTitle.setHint("Title");
        tilDate.setHint("Date");
        tilTime.setHint("Time");

        final Calendar calendar = Calendar.getInstance();

        View.OnClickListener oclEtDate = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDate.length() == 0)
                    etDate.setText(" ");

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
                }.show(NewTaskDialogFragment.this.getFragmentManager(), "DatePickerFragment");
            }
        };

        etDate.setOnClickListener(oclEtDate);

        View.OnClickListener oclEtTime = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etTime.length() == 0)
                    etTime.setText(" ");

                new TimePickerFragment() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        etTime.setText(Utils.getTime(calendar.getTimeInMillis()));
                    }

                    public void onCancel(DialogInterface dialog) {
                        etDate.setText(null);
                    }
                }.show(NewTaskDialogFragment.this.getFragmentManager(), "TimePickerFragment");
            }
        };

        etTime.setOnClickListener(oclEtTime);

        final ModelTask task = new ModelTask();

        Spinner spPriority = (Spinner) container.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.priority_levels));
        spPriority.setAdapter(adapter);
        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.setPriority(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adb.setView(container);

        adb.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                task.setTitle(etTitle.getText().toString());
                task.setStatus(1);
                if(!(etDate.length() == 0 && etTime.length() == 0)) {
                    task.setDate(calendar.getTimeInMillis());

                }
                task.setStatus(1);
                NewTaskDialogFragment.this.newTaskListener.onTaskAdded(task);
                dialog.dismiss();
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NewTaskDialogFragment.this.newTaskListener.onTaskAddingCancel();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = adb.create();
        final TextInputLayout textInputLayout = tilTitle;
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button okButton = ((AlertDialog) dialog).getButton(-1);
                if (etTitle.length() == 0) {
                    okButton.setEnabled(false);
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Please, enter the title");
//                    etTitle.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
                etTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            okButton.setEnabled(false);
                            textInputLayout.setError("Please, enter the title");
                            return;
                        }
                        else okButton.setEnabled(true);
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

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
