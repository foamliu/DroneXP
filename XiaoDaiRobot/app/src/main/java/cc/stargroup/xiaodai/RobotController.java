package cc.stargroup.xiaodai;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Foam on 2016/12/15.
 */

public class RobotController {
    private static final String TAG = MainActivity.class.getSimpleName();
    String address = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public RobotController() {

    }

    public void init(String address) {
        this.address = address;

        new ConnectBT().execute(); //Call the class to connect
    }

    public void forward()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("A".toString().getBytes());
            }
            catch (IOException e)
            {
                Log.e(TAG, "Error");
            }
        }
    }

    public void backward()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("B".toString().getBytes());
            }
            catch (IOException e)
            {
                Log.e(TAG, "Error");
            }
        }
    }

    public void left()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("C".toString().getBytes());
            }
            catch (IOException e)
            {
                Log.e(TAG, "Error");
            }
        }
    }

    public void right()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("D".toString().getBytes());
            }
            catch (IOException e)
            {
                Log.e(TAG, "Error");
            }
        }
    }
    public void stop()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("E".toString().getBytes());
            }
            catch (IOException e)
            {
                Log.e(TAG, "Error");
            }
        }
    }
    public void spin_left()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("F".toString().getBytes());
            }
            catch (IOException e)
            {
                Log.e(TAG, "Error");
            }
        }
    }
    public void spin_right()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("G".toString().getBytes());
            }
            catch (IOException e)
            {
                Log.e(TAG, "Error");
            }
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                Log.e(TAG, "Connection Failed. Is it a SPP Bluetooth? Try again.");
                //finish();
            }
            else
            {
                Log.d(TAG, "Connected.");
                isBtConnected = true;
            }
        }
    }
}
