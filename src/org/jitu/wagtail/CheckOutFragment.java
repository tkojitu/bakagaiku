package org.jitu.wagtail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

public class CheckOutFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private static final String ARG_PATH = "ARG_PATH";

    private Dialog dialog;

    public static CheckOutFragment newInstance(String path) {
        CheckOutFragment frag = new CheckOutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PATH, path);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.check_out_dialog, null))
               .setPositiveButton(R.string.ok, this)
               .setNegativeButton(R.string.cancel, this);
        dialog = builder.create();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText et = (EditText)dialog.findViewById(R.id.check_out_path);
        if (et == null) {
            return;
        }
        String path = getArguments().getString(ARG_PATH);
        et.setText(path);
        et.setSelection(et.getText().length());
    }

    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
        case DialogInterface.BUTTON_POSITIVE:
            checkOut((Dialog)dialog);
            break;
        case DialogInterface.BUTTON_NEGATIVE:
            cancel();
            break;
        }
    }

    private void checkOut(Dialog dialog) {
        EditText et = (EditText)dialog.findViewById(R.id.check_out_path);
        if (et == null) {
            dismiss();
        }
        String path = et.getText().toString();
        ((RevisionActivity)getActivity()).checkOut(path);
    }

    private void cancel() {
        dismiss();
    }
}
