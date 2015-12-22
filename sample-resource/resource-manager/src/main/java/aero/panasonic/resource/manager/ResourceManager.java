package aero.panasonic.resource.manager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import aero.panasonic.lib.IResourceManager;
import aero.panasonic.lib.Resource;

public class ResourceManager extends Service {
    private static final String TAG = ResourceManager.class.getSimpleName();

    private static ResourceManager instance;

    ResourcePreferenceManager resourcePreferenceManager;
    IBinder binder = new IResourceManager.Stub() {

        @Override
        public void registerLayoutResource(int layoutResource, String packageName, int id) throws RemoteException {
            Log.v(TAG, "registerLayoutResource() " + layoutResource + ", " + packageName + ", " + id);
            resourcePreferenceManager.putLayout(packageName, layoutResource, id);
        }

        @Override
        public void registerIdResource(int resourceId, String packageName, int id) throws RemoteException {
            Log.v(TAG, "registerIdResource() " + resourceId + ", " + packageName + ", " + id);
            resourcePreferenceManager.putResourceId(packageName, resourceId, id);
        }

        @Override
        public void registerDrawableResource(int drawableResource, String packageName, int id) throws RemoteException {
            Log.v(TAG, "registerDrawableResource() " + drawableResource + ", " + packageName + ", " + id);
            resourcePreferenceManager.putDrawable(packageName, drawableResource, id);
        }
    };

    public static ResourceManager getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        resourcePreferenceManager = new ResourcePreferenceManager(this);

        instance = this;
    }

    public View getResourceLayout(int layout) {
        String layoutResource = resourcePreferenceManager.getLayout(Resource.layout.layout_pa);
        if (layoutResource.length() > 0) {
            String[] args = layoutResource.split(":");
            if (args.length == 2) {
                try {
                    Context context = createPackageContext(args[0], Context.CONTEXT_IGNORE_SECURITY);
                    if (context != null) {
                        View view = View.inflate(context, Integer.parseInt(args[1]), null);
                        if (view != null) return view;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return View.inflate(this, R.layout.view, null);
    }

    public Drawable getResourceDrawable(int drawable) {
        String drawableResource = resourcePreferenceManager.getDrawable(Resource.drawable.pa_icon);
        if (drawableResource.length() > 0) {
            String[] args = drawableResource.split(":");
            if (args.length == 2) {
                try {
                    Context context = createPackageContext(args[0], Context.CONTEXT_IGNORE_SECURITY);
                    if (context != null) {
                        return context.getDrawable(drawable);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return getDrawable(R.mipmap.ic_launcher);
    }

    public int getId(int resourceId) {
        String idResource = resourcePreferenceManager.getResourceId(resourceId);
        if (idResource.length() > 0) {
            String[] args = idResource.split(":");
            if (args.length == 2) {
                return Integer.parseInt(args[1]);
            }
        }
        return 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand() " + intent.getAction());

        Log.v(TAG, "layout: " + resourcePreferenceManager.getLayout(Resource.layout.layout_pa));
        Log.v(TAG, "drawable: " + resourcePreferenceManager.getDrawable(Resource.drawable.pa_icon));
        Log.v(TAG, "id ok: " + resourcePreferenceManager.getResourceId(Resource.id.button_ok));
        Log.v(TAG, "id cancel: " + resourcePreferenceManager.getResourceId(Resource.id.button_cancel));

        if (intent.getAction().equals("aero.panasonic.action.PA_ON"))
            PssHelper.showPaAlert(this);
        else PssHelper.dismissPaAlert();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
