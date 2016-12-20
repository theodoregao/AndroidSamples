package sg.shun.gao.test.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SampleAidl extends AppCompatActivity {

    private static final String TAG = SampleAidl.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_aidl);

        Intent intent = new Intent();
        intent.setAction("sg.shun.gao.ACTION_SEND_RECEIVE_DATA");
        intent.setPackage("sg.shun.gao.test.aidl.server");
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
                    long timestamp = System.currentTimeMillis();
                    StringBuilder data = new StringBuilder();
                    int length = 1024 * 128;
                    for (int i = 0; i < length; i++) data.append('a');
                    int count = 8 * 1024;
                    for (int i = 0; i < count; i++)
                        try {
                            api.sendReceiveData(data.toString());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    Log.v(TAG, "time consumed: " + (System.currentTimeMillis() - timestamp));
                }
            }.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}
