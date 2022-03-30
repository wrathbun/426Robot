package com.example.EV3_controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class RobotConnection {
    private boolean isConnected = false;
    private BluetoothAdapter cv_btInterface = null;
    private Set<BluetoothDevice> cv_pairedDevices = null;
    private BluetoothDevice cv_btDevice = null;
    private BluetoothSocket cv_btSocket = null;
    private InputStream cv_is = null;
    private OutputStream cv_os = null;

    public RobotConnection() {}
    public BluetoothDevice getDevice() { return cv_btDevice; }


    public boolean isConnected() { return isConnected;}

    public String[] connectToRobot(String name) {
        BluetoothDevice lv_bd = null;
        try {
            cv_btInterface = BluetoothAdapter.getDefaultAdapter();
            if(!cv_btInterface.isEnabled())
                return new String[]{"false", "Enable bluetooth to connect to " +name};
            cv_pairedDevices = cv_btInterface.getBondedDevices();
            Iterator<BluetoothDevice> lv_it = cv_pairedDevices.iterator();
            while (lv_it.hasNext())  {
                lv_bd = lv_it.next();
                if (lv_bd.getName().equalsIgnoreCase(name)) {
                    cv_btDevice = lv_bd;
                    return new String[]{"true", name +"was found in paired list"};

                }
            }
            return new String[]{"false", name + " is not paired with this device"};
        }
        catch (Exception e) {
            return new String[]{"false","Failed to find" +name +": " + e.getMessage()};
        }
    }

    public String finishConnecting() {
        try  {
            cv_btSocket = ( cv_btDevice.createRfcommSocketToServiceRecord
                    (UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")));
            cv_btSocket.connect();

            cv_is = cv_btSocket.getInputStream();
            cv_os = cv_btSocket.getOutputStream();
            isConnected = true;
            return "Connected to " + cv_btDevice.getName() + " at " + cv_btDevice.getAddress();
        }
        catch (Exception e) {
            return "Error interacting with the robot: " + e.getMessage();
        }
    }

    public String drive(char direction, int power) {
        try {
            //1200xxxx800000AE000681320082840382B40001
            byte[] buffer = new byte[20];       // 0x12 command length

            buffer[0] = (byte) (20-2);
            buffer[1] = 0;

            buffer[2] = 34;
            buffer[3] = 12;

            buffer[4] = (byte) 0x80;

            buffer[5] = 0;
            buffer[6] = 0;

            buffer[7] = (byte) 0xae;
            buffer[8] = 0;

            switch(direction) {
                case 'r': buffer[9] =5;
                    break;
                case 'l': buffer[9] =3;
                    break;
                default: buffer[9]  = 6;
            }

            buffer[10] = (byte) 0x81;
            buffer[11] = (byte) (direction=='d'? -power : power);
            //power

            buffer[12] = 0;

            buffer[13] = (byte) 0x82; //move 45
            buffer[14] = (byte) 0x2D;
            buffer[15] = (byte) 0x00;

            buffer[16] = (byte) 0x82; //move 45
            buffer[17] = (byte) 0x2D;
            buffer[18] = (byte) 0x00;

            buffer[19] = 1;

            cv_os.write(buffer);
            cv_os.flush();
            return "";
        }
        catch (Exception e) {
            return "Error while driving the robot:" + e.getMessage();
        }
    }

    public String disconnectFromRobot() {
        try {
            cv_btSocket.close();
            cv_is.close();
            cv_os.close();
            isConnected = false;
            String ret = cv_btDevice.getName() + " was disconnected";
            cv_btDevice = null;
            return ret;
        } catch (Exception e) {
            return "Error while disconnecting from " + cv_btDevice.getName()+": " + e.getMessage();
        }
    }





}
