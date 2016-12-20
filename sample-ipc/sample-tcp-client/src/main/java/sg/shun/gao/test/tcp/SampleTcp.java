package sg.shun.gao.test.tcp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SampleTcp extends AppCompatActivity {

    private static final String TAG = SampleTcp.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_aidl);

        Intent intent = new Intent();
        intent.setAction("sg.shun.gao.ACTION_START_TCP");
        intent.setPackage("sg.shun.gao.test.tcp.server");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.v(TAG, "onServiceConnected() " + componentName.getPackageName() + ":" + componentName.getClassName());

            final Api api = Api.Stub.asInterface(iBinder);

            new Thread() {
                @Override
                public void run() {
                    try {
                        api.start();
                        Thread.sleep(3000);
                        new Thread(new ClientThread()).start();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private Socket socket;
    private static final int SERVERPORT = 1986;

    class ClientThread implements Runnable {

        private BufferedReader input;
        private BufferedWriter output;

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName("localhost");
                socket = new Socket(serverAddr, SERVERPORT);
                Log.v(TAG, "stream");

                this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                Log.v(TAG, "start");
                long timestamp = System.currentTimeMillis();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < 1024 * 1024; i++) stringBuilder.append('a');
                String data = stringBuilder.toString();

                int count = 1024;

                for (int i = 0; i < count; i++) {
                    output.write(data);
                }

                Log.v(TAG, "time consumed: " + (System.currentTimeMillis() - timestamp));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
