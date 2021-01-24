package com.domain.no.wordclock;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class StartUpActivity extends AppCompatActivity{


    //BT
    BluetoothConnectivity bt = new BluetoothConnectivity();
    Set<BluetoothDevice> pairedDevices;
    private Switch switchBT;
    private TextView textViewInfo;

    //Buttons
    private Button btnSUSearchDev;
    private Button btnSUConnect;

    //Dev spinner + tools
    private Spinner spinner;
    List<EspDevice> deviceList = new LinkedList<>();
    EspDevice setConnectDev = new EspDevice();

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        //BT
        bt.setBluetoothAdapter(BluetoothAdapter.getDefaultAdapter());

        btnSUSearchDev = findViewById(R.id.btnSUSearchDev);
        btnSUConnect = findViewById(R.id.btnSUConnect);
        spinner = findViewById(R.id.spinner);
        switchBT = findViewById(R.id.swiSUBluetooth);
        textViewInfo = findViewById(R.id.textViewInfo);

        btnSUSearchDev.setOnClickListener(btnListenerSU);
        btnSUConnect.setOnClickListener(btnListenerSU);
        switchBT.setOnClickListener(btnListenerSU);
        connectionMenuVisibility(false);

        checkBluetoothState();

        handler.postDelayed(runnable, 100);
    }

    public void onResume(){
        super.onResume();
        connectionMenuVisibility(false);
    }

    private void checkBluetoothState(){
        if( bt.getBluetoothAdapter().getState()==BluetoothAdapter.STATE_ON ){
            switchBT.setChecked(true);
            btnSUSearchDev.setVisibility(View.VISIBLE);
        }
        else{
            switchBT.setChecked(false);
            btnSUSearchDev.setVisibility(View.GONE);
        }
    }

    private void connectionMenuVisibility(boolean vis){
        if (vis == true){
            btnSUConnect.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            textViewInfo.setVisibility(View.VISIBLE);
        }
        else{
            btnSUConnect.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            textViewInfo.setVisibility(View.GONE);
        }
    }

    // Auswertung der Tasten
    private View.OnClickListener btnListenerSU = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            if(bt.getBluetoothAdapter() == null){
                Toast.makeText(StartUpActivity.this,"Sorry!\nYou mobile phone doesn't support bluetooth.", Toast.LENGTH_SHORT).show();
            }
            else{
                if ( view == btnSUSearchDev ){
                    if(bt.getBluetoothAdapter().isEnabled()){
                        search_bt_device();
                        if ( deviceList != null ){
                            connectionMenuVisibility(true);
                        }
                        else{
                            openErrorDialogInfo();
                        }
                    }
                    else{
                        // nop
                    }
                }
                else if ( view == btnSUConnect){
                    if(pickDev()){
                        openMainActivity();
                    }
                    else{
                        openErrorDialogInfo();
                    }
                }
                else if ( view == switchBT ){
                    if( switchBT.isChecked() ){
                        Intent enableTBIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(enableTBIntent);
                        btnSUSearchDev.setVisibility(View.VISIBLE);
                    }
                    else{
                        bt.getBluetoothAdapter().disable();
                        btnSUSearchDev.setVisibility(View.GONE);
                        connectionMenuVisibility(false);
                        deviceList.clear();
                    }
                }
                else{
                    Toast.makeText(StartUpActivity.this,"There is no operation for this button.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void openErrorDialogInfo(){
        openErrorDialogConnectioninfo dialog = new openErrorDialogConnectioninfo();
        dialog.show(getSupportFragmentManager(),"Info");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void search_bt_device(){
        deviceList.clear();
        try
        {
            pairedDevices = bt.getBluetoothAdapter().getBondedDevices();

            if ( pairedDevices.size() > 0 )
            {
                for ( BluetoothDevice bt : pairedDevices ) {
                    if( bt.getName().contains( setConnectDev.getDefDev() ) ) {
                        deviceList.add(new EspDevice(bt.getName(), bt.getAddress()));
                    }
                }
            }
        }
        catch(Exception we){
            Toast.makeText(getApplicationContext(),"Communication failed, sorry. Please restart the app and try again.", Toast.LENGTH_SHORT).show();
        }
        addItemsOnDropdownMenu();
    }

    public void addItemsOnDropdownMenu() {

        List<String> list = new LinkedList<>();
        for(EspDevice ed : deviceList){
            list.add(ed.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    public boolean pickDev(){
        boolean iRet = false;
        for(EspDevice ed : deviceList){
            if(ed.getName().contains(spinner.getSelectedItem().toString())){
                setConnectDev.setConName(ed.getName());
                setConnectDev.setConAddress(ed.getAdresse());
                iRet = true;
            }
        }
        return iRet;
    }

    public void openMainActivity(){
        Intent myMainActivity = new Intent(this, com.domain.no.wordclock.MainActivity.class);
        startActivity(myMainActivity);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if ( ( bt.getBluetoothAdapter().getState()== BluetoothAdapter.STATE_OFF ) && switchBT.isChecked() ) {
                // nop
            }
            // Rufe diese Methode zyklisch alle x ms
            handler.postDelayed(this, 1000);
        }
    };
}
