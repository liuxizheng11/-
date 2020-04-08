package com.rocedar.deviceplatform.device.bluetooth.impl.mjk;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.rocedar.base.RCLog;
import com.rocedar.deviceplatform.R;
import com.rocedar.deviceplatform.device.bluetooth.listener.RCBluetoothError;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class BluetoothMJKService extends Service {

    private String TAG = "RCDevice_mjkService";

    private BluetoothAdapter mAdapter;
    private Context context;

    private boolean isConnect = false;

    public static final String EXTRA_DATA = "com.rocedar.mjk.receive";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public BluetoothMJKService(Context context) {
        super();
        this.context = context;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    /**
     * 地址连接
     *
     * @param address mac address
     */
    public void connect(String address) {
        try {
            BluetoothDevice device = mAdapter.getRemoteDevice(address);
            connect(device);
        } catch (Exception e) {
            RCLog.e(TAG, "连接设备，找不到设备%s", address);
            Intent intent = new Intent(EXTRA_DATA);
            intent.putExtra("error", getString(R.string.rcdevice_mjk_can_find));
            intent.putExtra("status", RCBluetoothError.ERROR_CANNOT_FIND);
            context.sendBroadcast(intent);
        }
    }


    private void connect(BluetoothDevice device) {
        ConnectThread thread = new ConnectThread(device);
        thread.start();
    }


    private void manageConnectedSocket(BluetoothSocket socket) {
        mAdapter.cancelDiscovery();
        ConnectedThread thread = new ConnectedThread(socket);
        thread.start();
    }

    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            try {
                UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }


        public void run() {
            mAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (Exception e1) {
                e1.printStackTrace();
                try {
                    if (mmSocket.isConnected())
                        mmSocket.close();
                    else {
                        Intent intent = new Intent(EXTRA_DATA);
                        intent.putExtra("error", context.getString(R.string.rcdevice_connect_error));
                        intent.putExtra("status", RCBluetoothError.ERROR_CONNECT);
                        context.sendBroadcast(intent);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                return;
            }
            manageConnectedSocket(mmSocket);
        }


        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;


        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
        }


        public void run() {
            byte[] buffer = new byte[1024]; // buffer store for the stream
            int bytes; // bytes returned from read()

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String strDataToUi = bytes2String(buffer, bytes);

                    if (!TextUtils.isEmpty(strDataToUi)) {

                        Intent intent = new Intent(EXTRA_DATA);
                        intent.putExtra("signal", strDataToUi);
                        context.sendBroadcast(intent);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }


        private String bytes2String(byte[] data, int count) {
            if (data != null && data.length > 0) {
                StringBuilder stringBuilder = new StringBuilder(
                        data.length);
//                for (byte byteChar : data)
//                    stringBuilder.append(String.format("%02X ", byteChar));
                for (int i = 0; i < count; i++) {
                    stringBuilder.append(String.format("%02X ", data[i]));
                }

                return stringBuilder.toString();
            }
            return "";
//            ArrayList<String> result = new ArrayList<String>();
//            if (count <= 1) {
//                return null;
//            } else {
//
//                for (int i = 0; i < count; i++) {
//                    String myInt = Integer.toHexString((int) (b[i] & 0xFF));
//
//                    if (myInt.length() < 2) {
//                        result.add("0" + myInt);
//                    } else {
//                        result.add(myInt);
//                    }
//                }
//
//                try {
//                    String strDataContacted = listToString(result);
//                    if (strDataContacted.toUpperCase().startsWith("A4") && strDataContacted.toUpperCase().endsWith("00FF")) {
//
//                        String substring = strDataContacted.substring(2, 10);
//
//                        int i = Integer.parseInt(substring, 16);//16转10进制
//
//                        Log.d("bro", "strDataContacted : " + strDataContacted + "---substring : " + substring + "---i : " + i);
//                        return i + "";
//                    }
//                    Log.d("bro", "strDataContacted : " + strDataContacted + "---substring : 数据异常");
//                    return "";
//                } catch (Exception e) {
//
//                    return "";
//
//                } finally {
//
//                }
//            }

        }

        public String listToString(List list) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i));
            }
            return sb.toString();
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

}
