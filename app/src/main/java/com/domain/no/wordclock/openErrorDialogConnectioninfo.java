package com.domain.no.wordclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

public class openErrorDialogConnectioninfo extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Info message")
                .setMessage("Sorry, no bounded devices found. Please try to bound new device in you Bluetooth settings. For that just searched device with prefix \"Wordclock\". Than click on it and push pair.")
                .setPositiveButton("got it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return  builder.create();
    }
}
