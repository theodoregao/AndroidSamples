package gao.shun.sg.sample.windowservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class WindowService extends Service {
    private static final String TAG = WindowService.class.getSimpleName();
    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.v(TAG, "onTouch()");
            return true;
        }
    };
    private WindowManager mWindowManager;
    private ImageView mImageView;

    public WindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand() " + intent.getAction());
        String action = intent.getAction();
        if (action.equals("gao.shun.sg.sample.windowservice.SHOW_FLOAT_WINDOW")) {
            showFloatWindow();
        } else if (action.equals("gao.shun.sg.sample.windowservice.HIDE_FLOAT_WINDOW")) {
            hideFloatWindow();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);

        showFloatWindow();
    }

    private void showFloatWindow() {
        Log.v(TAG, "showFloatWindow()");

        hideFloatWindow();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.x = -50;
        layoutParams.y = -50;

        mImageView = new ImageView(this);
        mImageView.setImageResource(R.mipmap.ic_launcher);
        mImageView.setOnTouchListener(onTouchListener);
        mWindowManager.addView(mImageView, layoutParams);
    }

    private void hideFloatWindow() {
        Log.v(TAG, "hideFloatWindow()");
        if (mImageView != null) {
            mWindowManager.removeView(mImageView);
            mImageView = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        hideFloatWindow();
    }
}
