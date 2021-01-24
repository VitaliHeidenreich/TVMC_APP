package com.domain.no.wordclock;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnectivity {
    private static BluetoothAdapter bluetoothAdapter = null;
    private static BluetoothSocket bluetoothSocket = null;
    private static InputStream tmpIn = null;
    private static OutputStream tmpOut = null;
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public  OutputStream getTmpOut() {
        return tmpOut;
    }

    public  void setTmpOut(OutputStream tmpOut) {
        BluetoothConnectivity.tmpOut = tmpOut;
    }

    public InputStream getTmpIn() {
        return tmpIn;
    }

    public void setTmpIn(InputStream tmpIn) {
        BluetoothConnectivity.tmpIn = tmpIn;
    }


    public void setBluetoothAdapter( BluetoothAdapter bt ){ bluetoothAdapter = bt; }

    public BluetoothAdapter getBluetoothAdapter(){
        return bluetoothAdapter;
    }

    public void setBluetoothSocket( BluetoothSocket ba ){
        bluetoothSocket = ba;
    }

    public BluetoothSocket getBluetoothSocket(){ return bluetoothSocket; }

    public static UUID getUUID() {
        return uuid;
    }

}
