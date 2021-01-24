package com.domain.no.wordclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by vitali on 19.03.2018.
 */

public class SettingsDialog extends AppCompatDialogFragment
{
    private EditText editConnectivity;
    private Spinner spinnerUebergang;
    private ExampleDialogListener listener;
    MainActivity mMain = new MainActivity();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder((getActivity()));

        LayoutInflater mInflater = getActivity().getLayoutInflater();
        View mView = mInflater.inflate(R.layout.layout_settings, null);

        editConnectivity = (EditText) mView.findViewById(R.id.editConnectivity);
        spinnerUebergang = (Spinner) mView.findViewById(R.id.spinnerUebergang);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mView.getContext(),R.array.Übergänge, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUebergang.setAdapter(adapter);

        //Hole Einstellungen aus der Main
        MainActivity mMain = new MainActivity();
        editConnectivity.setText(mMain.getSetting());

        mBuilder.setView(mView)
                .setTitle("Settings")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String textToBeSended = editConnectivity.getText().toString();
                        listener.applyTexts(textToBeSended);
                    }
                });
        //sendMessageText = getView().findViewById(R.id.edit_ip);
        return mBuilder.create();
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (ExampleDialogListener) context;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(context.toString()+"bla");
        }
    }

    public interface ExampleDialogListener{
        void applyTexts(String textToBeSended);
    }
}
