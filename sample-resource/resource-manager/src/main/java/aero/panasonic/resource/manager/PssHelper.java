package aero.panasonic.resource.manager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import aero.panasonic.lib.Resource;

/**
 * Created by shungao on 12/16/15.
 */
public class PssHelper {

    private static final String TAG = PssHelper.class.getSimpleName();

    static WindowManager windowManager;
    static View paAlert;
    static WindowManager.LayoutParams params;

    public static void showPaAlert(Context context) {
        ResourceManager resourceManager = ResourceManager.getInstance();
        Log.v(TAG, "showPaAlert()");
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (paAlert == null) {
            paAlert = resourceManager.getResourceLayout(Resource.layout.layout_pa);
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;

//            params.gravity = Gravity.TOP | Gravity.LEFT;
//            params.x = 0;
//            params.y = 100;
//            params.height = 100;
//            params.width = 100;
            windowManager.addView(paAlert, params);
        }
    }

    public static void dismissPaAlert() {
        Log.v(TAG, "dismissPaAlert()");
        if (windowManager != null && paAlert != null) {
            windowManager.removeView(paAlert);
            paAlert = null;
        }
    }
}
