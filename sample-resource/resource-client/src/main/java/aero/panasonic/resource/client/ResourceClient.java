package aero.panasonic.resource.client;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import aero.panasonic.lib.IResourceManager;
import aero.panasonic.lib.Resource;
import aero.panasonic.lib.ResourceManager;

public class ResourceClient extends AppCompatActivity {

    private static final String TAG = ResourceClient.class.getSimpleName();
    ServiceConnection connection = new ServiceConnection() {
        private IResourceManager resourceManager;

        public void registerLayoutResource(int layoutResource, int id) {
            if (resourceManager != null)
                try {
                    resourceManager.registerLayoutResource(layoutResource, getPackageName(), id);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
        }

        public void registerDrawableResource(int drawableResource, int id) {
            if (resourceManager != null)
                try {
                    resourceManager.registerDrawableResource(drawableResource, getPackageName(), id);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
        }

        public void registerIdResource(int resourceId, int id) {
            if (resourceManager != null)
                try {
                    resourceManager.registerIdResource(resourceId, getPackageName(), id);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service != null) {
                resourceManager = IResourceManager.Stub.asInterface(service);
                if (resourceManager != null) {
                    Log.v(TAG, "bind resource manager succeed");

                    registerLayoutResource(Resource.layout.layout_pa, R.layout.view);
                    registerDrawableResource(Resource.drawable.pa_icon, R.mipmap.ic_launcher);
                    registerIdResource(Resource.id.button_ok, R.id.buttonTop);
                    registerIdResource(Resource.id.button_cancel, R.id.buttonBottom);
                } else {
                    Log.e(TAG, "bind resource manager error");
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_client);
        ResourceManager.bindResource(this, connection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ResourceManager.unbindResource(this, connection);
    }
}
