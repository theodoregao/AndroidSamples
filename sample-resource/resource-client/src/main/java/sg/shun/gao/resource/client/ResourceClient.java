package sg.shun.gao.resource.client;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import sg.shun.gao.lib.IResourceManager;
import sg.shun.gao.lib.Resource;
import sg.shun.gao.lib.ResourceManager;

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

        public void registerStringResource(int stringResource, int id) {
            if (resourceManager != null)
                try {
                    resourceManager.registerStringResource(stringResource, getPackageName(), id);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
        }

        public void registerAnimationResource(int animationResource, int id) {
            if (resourceManager != null)
                try {
                    resourceManager.registerAnimationResource(animationResource, getPackageName(), id);
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
                    registerLayoutResource(Resource.layout.pa_layout, R.layout.dialog_pa);
                    registerAnimationResource(Resource.anim.pa_on, R.anim.jump);
                    registerIdResource(Resource.id.pa_title, R.id.pa_title);
                    registerStringResource(Resource.string.pa_title, R.string.title_pa);
                    registerIdResource(Resource.id.pa_message, R.id.pa_message);
                    registerStringResource(Resource.string.pa_message, R.string.message_pa);
                    registerDrawableResource(Resource.drawable.pa_icon, R.drawable.ic_info_black_24dp);
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
