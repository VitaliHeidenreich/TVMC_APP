package com.domain.no.MyTVMC;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import petrov.kristiyan.colorpicker.ColorPicker;


public class MainActivity extends AppCompatActivity implements SettingsDialog.ExampleDialogListener{

    //Statische Variablen zum Einstellen der APP --> TODEL
    public static String textBuffer;
    public static int touchMoveButton = 0;

    EspDevice ed = new EspDevice();
    BluetoothConnectivity bt = new BluetoothConnectivity();

    String messageLog;
    private int mHour, mMinute;

    // Time Picker
    TimePickerDialog timePickerDialog;

    //Objekte anlegen
    private EditText receiveText;
    private TextView textViewColor;
    private TextView txtConnectionstatus;

    //Buttons
    private Button btnLED;
    private Button btnActualTime;
    private ImageButton iBtnLeft;
    private ImageButton iBtnRight;

    private Toolbar myToolbar;
    private CheckBox checkBoxTimeSetting;
    private CheckBox checkBoxAutoMove;

    ColorPickerDialog colorPickerDialog;
    int color = Color.parseColor("#33b5e5");

    //Für die Suche von Geräten
    String mArrayAdapter = "";

    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hauptprogramm);

        //Buttons
        btnLED = findViewById(R.id.btnSetColorToDef);
        iBtnLeft =  findViewById(R.id.iBtnLeft);
        iBtnRight =  findViewById(R.id.iBtnRight);
        btnActualTime = findViewById(R.id.sendAndroidTime);


        receiveText = findViewById(R.id.receiveMessage);


        textViewColor = findViewById(R.id.textViewColor);
        txtConnectionstatus =  findViewById(R.id.txtConnectionstatus);

        checkBoxTimeSetting = findViewById(R.id.checkBoxTimeSetting);
        checkBoxAutoMove = findViewById(R.id.checkBoxAutoMove);


        // Buttons
        btnLED.setOnClickListener(btnListener);
        btnActualTime.setOnClickListener(btnListener);
        checkBoxAutoMove.setOnClickListener(btnListener);
        checkBoxAutoMove.setChecked(false);

        // OnTouchListener
        iBtnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    checkBoxAutoMove.setChecked(false);
                    sendString("XA000000$\n");
                    sendString("XM000001$\n");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    checkBoxAutoMove.setChecked(false);
                    sendString("XA000000$\n");
                    sendString("XM000000$\n");
                }
                return true;
            }
        });
        iBtnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    checkBoxAutoMove.setChecked(false);
                    sendString("XA000000$\n");
                    sendString("XM000002$\n");
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    checkBoxAutoMove.setChecked(false);
                    sendString("XA000000$\n");
                    sendString("XM000000$\n");
                }
                return true;
            }
        });


        //Empfangsbox soll nicht editierbar sein
        receiveText.setKeyListener(null);


        myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        connectionStatus(false);
        connectToDevice();

        handler = new Handler();

        handler.postDelayed(readTextFromBT, 1);

        WifiManager wifiManager = (WifiManager)
                getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // WiFi einschalten
        //if (!wifiManager.isWifiEnabled()) {
        //    Toast.makeText(getApplicationContext(), "Turning WiFi ON...", Toast.LENGTH_LONG).show();
        //    wifiManager.setWifiEnabled(true);
        //}
    }


    /// ToDo ######################################################################################


    public void onStart(){
        super.onStart();
        try { bt.getBluetoothSocket().close();} catch (Exception e) {}
        connectToDevice();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void connectToDevice()
    {
        if( ed.getConAddress() != null )
        {
            BluetoothDevice dispositivo = bt.getBluetoothAdapter().getRemoteDevice(ed.getConAddress());
            //connects to the device's address and checks if it's available
            try
            {
                bt.setBluetoothSocket(dispositivo.createInsecureRfcommSocketToServiceRecord(bt.getUUID()));
                bt.getBluetoothSocket().connect();
                connectionStatus(true);
            }
            catch(Exception e){Toast.makeText(getApplicationContext(),"Nope! (2)", Toast.LENGTH_SHORT).show();}
        }
        else{
            Toast.makeText(MainActivity.this,
                    "No WordUhr device found. Only following devices are connected:\n"
                            + mArrayAdapter, Toast.LENGTH_SHORT).show();
        }
        // Input und Output anlegen
        try
        {
            bt.setTmpIn(bt.getBluetoothSocket().getInputStream());
            bt.setTmpOut(bt.getBluetoothSocket().getOutputStream());
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public String getSetting()
    {
        return textBuffer;
    }

    public void openDialog()
    {
        SettingsDialog settingDialog = new SettingsDialog();
        settingDialog.show(getSupportFragmentManager(),"My Dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater myMenuInflater = getMenuInflater();
        myMenuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.action_setting){
            openDialog();
        }
        if(item.getItemId()==R.id.action_about_us)
        {
            Toast.makeText(MainActivity.this,"No info yet", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    // ####################################################################################################
    // Auswertung der Tasten   ############################################################################
    // ####################################################################################################
    private View.OnClickListener btnListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {


            if (view == btnLED){
                openColorPickerDialog();
            }
            else if (view == btnActualTime){

                if(checkBoxTimeSetting.isChecked()) {
                    Calendar currentTime = Calendar.getInstance();
                    int hours   = currentTime.get(Calendar.HOUR_OF_DAY);
                    int minute  = currentTime.get(Calendar.MINUTE);
                    int sekunde = currentTime.get(Calendar.SECOND);
                    sendString("XT"
                            + ((hours<10)?("0" + hours):(hours))
                            + ((minute<10)?("0" + minute):(minute))
                            + ((sekunde<10)?("0" + sekunde):(sekunde)) +"$\n");
                }
                else{
                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    sendString("XT"
                                            + ((hourOfDay<10)?("0" + hourOfDay):(hourOfDay))
                                            + ((minute<10)?("0" + minute):(minute))
                                            + "00" +"$\n");
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                }
            }
            else if(view == checkBoxAutoMove)
            {
                if(checkBoxAutoMove.isChecked())
                {
                    sendString("XA111111$\n");
                }
                else
                {
                    sendString("XA000000$\n");
                }
            }
            else{
                Toast.makeText(MainActivity.this,"ERROR! BUTTON", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void sendString( String str )
    {
        try {
            if ( bt.getBluetoothSocket() != null ){
                bt.getTmpOut().write(str.getBytes());
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            connectionStatus(false);
        }
    }
    private String readString()
    {
        byte[] buffer = new byte[256];  // buffer store for the stream
        int bytes; // bytes returned from read()
        String readMessage = null;
        try {
            if ( (bt.getTmpIn()!=null) && ( bt.getTmpIn().available() > 0) ) {
                bytes = bt.getTmpIn().read(buffer);
                readMessage = new String(buffer, 0, bytes);
                Log.d(textBuffer, ">>>>>>>>>>>>>>>>>>> input data");
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            connectionStatus(false);
        }
        return readMessage;
    }

    // Settingsdialog Kommunikation
    @Override
    public void applyTexts(String textToBeSended, boolean boo){
        receiveText.setText(receiveText.getText().toString() + "\n" + "Sended: " + textToBeSended);
        sendString(textToBeSended);

        if( boo )
        {
            sendString("000XRA00007$\n");
            receiveText.setText(receiveText.getText().toString() + "\n" + "Sended: " + "XR777777$\n");
        }
    }

    private void connectionStatus( boolean con ){
        try{
            if(con){
                txtConnectionstatus.setText("You are connected to:\nName: "+ed.getConName()+"\nAddress: "+ed.getConAddress());
                txtConnectionstatus.setTextColor(Color.rgb(30,155,30));
                txtConnectionstatus.setBackgroundColor(Color.rgb(200, 250,200));
            }
            else{
                txtConnectionstatus.setText("Ups, connection lost!\nPlz restart the app.");
                txtConnectionstatus.setTextColor(Color.rgb(255,0,0));
                txtConnectionstatus.setBackgroundColor(Color.rgb(236, 168,178));
            }
        }
        catch(Exception e){
            Toast.makeText(MainActivity.this,e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    void displayText(){
        String myStg = readString();
        if(myStg!=null)
        {
            receiveText.setText(receiveText.getText() + myStg);
        }
    }


    private Runnable readTextFromBT = new Runnable() {
        @Override
        public void run() {
            try
            {
                displayText();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            handler.postDelayed(this, 1);
        }
    };

    public void openColorPickerDialog() {
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String> color = new ArrayList<>();
        color.add("#FF0000");
        color.add("#FF8000");
        color.add("#0000FF");
        color.add("#00FF00");
        color.add("#FF00FF");

        color.add("#00FFFF");
        color.add("#FFA500");
        color.add("#CD853F");
        color.add("#FFFFFF");
        color.add("#000000");

        colorPicker.setColors(color)
                .setColumns(5)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        String iRet = String.valueOf(color);
                        sendString("XF" + Integer.toHexString(color).substring(2) +"$\n");
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
    }

    public String convertAndLimit(String a){
        return a.substring(1);
    }



    /// +++ WIFI

}
