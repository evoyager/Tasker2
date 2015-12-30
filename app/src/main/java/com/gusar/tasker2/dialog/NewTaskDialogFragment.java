package com.gusar.tasker2.dialog;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Dialog;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.gusar.tasker2.R;

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
        TextInputLayout tilTitle = (TextInputLayout) container.findViewById(R.id.tilNewTaskTitle);
        TextInputLayout tilDate = (TextInputLayout) container.findViewById(R.id.tilNewTaskDate);
        TextInputLayout tilTime = (TextInputLayout) container.findViewById(R.id.tilNewTaskTime);
        EditText etTitle = (EditText) tilTitle.getEditText();
        EditText etDate = (EditText) tilDate.getEditText();
        EditText etTime = (EditText) tilTime.getEditText();
        tilTitle.setHint("Title");
        tilDate.setHint("Date");
        tilTime.setHint("Time");
        etTitle.setError("Enter task title");

        adb.setView(container);
        AlertDialog alertDialog = adb.create();

        return alertDialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
