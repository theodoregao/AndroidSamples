package sg.shun.gao.binderpool.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import sg.shun.gao.aidl.IBinderPool;
import sg.shun.gao.aidl.ICompute;
import sg.shun.gao.aidl.ISecurityCenter;

public class BinderPoolClient extends AppCompatActivity {

    private static final String TAG = BinderPoolClient.class.getSimpleName();

    private static final int BINDER_SECURITY_CENTER = 0;
    private static final int BINDER_COMPUTE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool_client);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_encrypt:
                bindEncrypt();
                break;

            case R.id.action_compute:
                bindCompute();
                break;

            default:
                break;
        }
    }

    private void bindEncrypt() {
        Intent intent = new Intent("sg.shun.gao.ACTION_BINDER_POOL");
        intent.setPackage("sg.shun.gao.binderpool.server");
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (iBinder != null) {
                    IBinderPool binder = IBinderPool.Stub.asInterface(iBinder);
                    if (binder != null) try {
                        ISecurityCenter securityCenter = ISecurityCenter.Stub.asInterface(binder.queryBinder(BINDER_SECURITY_CENTER));
                        Log.v(TAG, securityCenter.encrypt("string"));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, BIND_AUTO_CREATE);
    }

    private void bindCompute() {
        Intent intent = new Intent("sg.shun.gao.ACTION_BINDER_POOL");
        intent.setPackage("sg.shun.gao.binderpool.server");
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

                if (iBinder != null) {
                    IBinderPool binder = IBinderPool.Stub.asInterface(iBinder);
                    if (binder != null) try {
                        ICompute compute = ICompute.Stub.asInterface(binder.queryBinder(BINDER_COMPUTE));
                        Log.v(TAG, "1 + 2 = " + compute.add(1, 2));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, BIND_AUTO_CREATE);
    }
}
