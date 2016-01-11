package sg.shun.gao.resource.manager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import sg.shun.gao.lib.IEventListener;
import sg.shun.gao.lib.IResourceManager;
import sg.shun.gao.resource.manager.pa.PssController;

public class ResourceManager extends Service {
    private static final String TAG = ResourceManager.class.getSimpleName();

    private ResourceController mResourceController;
    private PssController mPssController;

    IBinder binder = new IResourceManager.Stub() {

        @Override
        public void registerLayoutResource(int resourceLayout, String packageName, int id) throws RemoteException {
            Log.v(TAG, "registerLayoutResource() " + resourceLayout + ", " + packageName + ", " + id);
            mResourceController.putResourceLayout(packageName, resourceLayout, id);
        }

        @Override
        public void registerDrawableResource(int resourceDrawable, String packageName, int id) throws RemoteException {
            Log.v(TAG, "registerDrawableResource() " + resourceDrawable + ", " + packageName + ", " + id);
            mResourceController.putResourceDrawable(packageName, resourceDrawable, id);
        }

        @Override
        public void registerStringResource(int resourceString, String packageName, int id) {
            Log.v(TAG, "registerStringResource() " + resourceString + ", " + packageName + ", " + id);
            mResourceController.putResourceString(packageName, resourceString, id);
        }

        @Override
        public void registerAnimationResource(int resourceAnimation, String packageName, int id) {
            Log.v(TAG, "registerAnimationResource() " + resourceAnimation + ", " + packageName + ", " + id);
            mResourceController.putResourceAnimation(packageName, resourceAnimation, id);
        }

        @Override
        public void registerIdResource(int resourceId, String packageName, int id) throws RemoteException {
            Log.v(TAG, "registerIdResource() " + resourceId + ", " + packageName + ", " + id);
            mResourceController.putResourceId(packageName, resourceId, id);
        }

        @Override
        public void setOnEventListener(IEventListener eventListener) throws RemoteException {
            mPssController.setEventListener(eventListener);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mResourceController = new ResourceController(this);
        mPssController = new PssController(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand() " + intent.getAction());

        String action = intent.getAction();

        if (action.equals("sg.shun.gao.action.PA_ON"))
            mPssController.paOn();
        else if (action.equals("sg.shun.gao.action.PA_OFF"))
            mPssController.paOff();
        else if (action.equals("sg.shun.gao.action.PSS_ON"))
            mPssController.pssOn();
        else if (action.equals("sg.shun.gao.action.PSS_OFF"))
            mPssController.pssOff();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
