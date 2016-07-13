package aero.panasonic.nsdprovider;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

public class NSDProvider extends AppCompatActivity {

    private static final String TAG = NSDProvider.class.getSimpleName();

    private ServerSocket mServerSocket;
    private NsdManager.RegistrationListener mRegistrationListener = new NsdManager.RegistrationListener() {
        @Override
        public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.d(TAG, "onRegistrationFailed() " + errorCode);
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.d(TAG, "onUnregistrationFailed() " + errorCode);
        }

        @Override
        public void onServiceRegistered(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "onServiceRegistered() ");
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "onServiceUnregistered() ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsdprovider);

        try {
            initializeNSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopNSD();
    }

    private void initializeNSD() throws IOException {
        NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();

        nsdServiceInfo.setServiceName("Smartest Service");
        nsdServiceInfo.setServiceType("_ep._tcp.");
        mServerSocket = new ServerSocket(0);
        nsdServiceInfo.setPort(mServerSocket.getLocalPort());

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        new CommunicationThread(mServerSocket.accept()).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        NsdManager nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        nsdManager.registerService(nsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    private void stopNSD() {
        NsdManager nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        nsdManager.unregisterService(mRegistrationListener);
    }
    DataOutputStream dataOutputStream;
    int count = 0;

    class CommunicationThread extends Thread {

        private Socket socket;

        public CommunicationThread(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            super.run();

            try {
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("message from server");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClick(View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (dataOutputStream != null) dataOutputStream.writeUTF("message from server: " + ++count);
                    else Log.e(TAG, "no connection!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
