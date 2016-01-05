package com.gusar.tasker2.dialog;

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
import android.widget.EditText;
import android.widget.Spinner;

import com.gusar.tasker2.R;
import com.gusar.tasker2.model.ModelTask;

/**
 * Created by igusar on 12/28/15.
 */
public class NewTaskDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

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
        EditText etDate = (EditText) tilDate.getEditText();
        EditText etTime = (EditText) tilTime.getEditText();
        tilTitle.setHint("Title");
        tilDate.setHint("Date");
        tilTime.setHint("Time");

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
                    etTitle.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
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
