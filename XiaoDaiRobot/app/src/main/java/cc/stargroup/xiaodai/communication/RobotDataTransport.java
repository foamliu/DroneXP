package cc.stargroup.xiaodai.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class RobotDataTransport extends Thread {
    private static final String TAG = RobotDataTransport.class.getSimpleName();

    private BluetoothAdapter mBlueAdapter = null;
    private BluetoothSocket mBlueSocket = null;
    private BluetoothDevice mBlueRobot = null;
    private OutputStream mOut;
    private InputStream mIn;
    private boolean robotFound = false;
    private boolean connected = false;
    private String robotName;
    private List<String> mMessages = new ArrayList<>();
    private char DELIMITER = '#';

    private static RobotDataTransport transport = null;

    public static RobotDataTransport getInstance(String n) {
        return transport == null ? new RobotDataTransport(n) : transport;
    }

    public static RobotDataTransport getInstance() {
        return transport == null ? new RobotDataTransport() : transport;
    }


    private RobotDataTransport(String name) {
        transport = this;
        try {
            for (int i = 0; i < 2048; i++) {
                mMessages.add("");
            }
            robotName = name;
            mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBlueAdapter == null) {
                Log.e(TAG, "[#]Phone does not support bluetooth!!");
                return;
            }
            if (!isBluetoothEnabled()) {
                Log.e(TAG, "[#]Bluetooth is not activated!!");
            }

            Set<BluetoothDevice> paired = mBlueAdapter.getBondedDevices();
            if (paired.size() > 0) {
                for (BluetoothDevice d : paired) {
                    if (d.getName().equals(robotName)) {
                        mBlueRobot = d;
                        robotFound = true;
                        break;
                    }
                }
            }

            if (!robotFound)
                Log.e(TAG, "[#]There is not robot paired!!");

        } catch (Exception e) {
            Log.e(TAG, "[#]Error creating Bluetooth! : " + e.getMessage());
        }

    }

    RobotDataTransport() {
        this("XiaoDaiRobot");
    }

    public boolean isBluetoothEnabled() {
        return mBlueAdapter.isEnabled();
    }

    public boolean connect() {
        if (!robotFound)
            return false;
        try {
            Log.d(TAG, "Connecting to the robot...");

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mBlueSocket = mBlueRobot.createRfcommSocketToServiceRecord(uuid);
            mBlueSocket.connect();
            mOut = mBlueSocket.getOutputStream();
            mIn = mBlueSocket.getInputStream();
            connected = true;
            this.start();
            Log.d(TAG, mBlueAdapter.getName());
            Log.d(TAG, "Ok!!");
            return true;

        } catch (Exception e) {
            Log.e(TAG, "[#]Error while connecting: " + e.getMessage());
            return false;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void run() {

        while (true) {
            if (connected) {
                try {
                    byte ch, buffer[] = new byte[1024];
                    int i = 0;

                    while ((ch = (byte) mIn.read()) != DELIMITER) {
                        buffer[i++] = ch;
                    }
                    buffer[i] = '\0';

                    final String msg = new String(buffer);

                    onMessageReceived(msg.trim());
                    Log.d(TAG, "[Blue]:" + msg);

                } catch (IOException e) {
                    Log.e(TAG, "->[#]Failed to receive message: " + e.getMessage());
                }

            }
        }
    }

    private void onMessageReceived(String msg) {
        try {
            mMessages.add(msg);
            Log.d(TAG, msg);
            try {
                this.notify();
            } catch (IllegalMonitorStateException e) {
                //
            }
        } catch (Exception e) {
            Log.e(TAG, "->[#] Failed to receive message: " + e.getMessage());
        }
    }

    public boolean hasMensagem(int i) {
        try {
            String s = mMessages.get(i);
            if (s.length() > 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String getMenssage(int i) {
        return mMessages.get(i);
    }

    public void clearMessages() {
        mMessages.clear();
    }

    public int countMessages() {
        return mMessages.size();
    }

    public String getLastMessage() {
        if (countMessages() == 0)
            return "";
        return mMessages.get(countMessages() - 1);
    }

    public void sendMessage(String msg) {
        try {
            if (connected) {
                byte[] data = msg.getBytes("US-ASCII");
                mOut.write(data);
            }

        } catch (IOException e) {
            Log.e(TAG, "->[#]Error while sending message: " + e.getMessage());
        }
    }


}
