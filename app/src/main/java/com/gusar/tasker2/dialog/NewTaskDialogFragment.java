package com.gusar.tasker2.dialog;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Dialog;
import android.view.View;

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
        adb.setView(container);
        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
