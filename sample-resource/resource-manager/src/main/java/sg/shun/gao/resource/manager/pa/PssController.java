package sg.shun.gao.resource.manager.pa;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.SeekBar;
import android.widget.TextView;

import sg.shun.gao.lib.IEventListener;
import sg.shun.gao.lib.Resource;
import sg.shun.gao.resource.manager.ResourceController;

/**
 * Created by Theodore on 2016/1/10.
 */
public class PssController {
    private static final String TAG = PssController.class.getSimpleName();

    private ResourceController mResourceController;
    private WindowManager mWindowManager;
    private IEventListener mEventListener;

    private View mViewPa;
    private View mViewPss;

    public PssController(Context context) {
        mResourceController = new ResourceController(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setEventListener(IEventListener eventListener) {
        mEventListener = eventListener;
    }

    public void paOn() {
        if (mWindowManager == null) {
            Log.e(TAG, "paOn error with window manager null");
            return;
        }
        if (mViewPa == null) {
            mViewPa = mResourceController.getResourceLayout(Resource.layout.pa_layout);
            if (mViewPa == null) {
                Log.e(TAG, "paOn view is null");
                return;
            }

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
            params.gravity = Gravity.CENTER;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;

            mWindowManager.addView(mViewPa, params);
        } else {
            mViewPa.setVisibility(View.VISIBLE);
        }

        TextView pa_title = (TextView) mViewPa.findViewById(mResourceController.getResourceId(Resource.id.pa_title));
        if (pa_title != null)
            pa_title.setText(mResourceController.getResourceString(Resource.string.pa_title));

        TextView pa_message = (TextView) mViewPa.findViewById(mResourceController.getResourceId(Resource.id.pa_message));
        if (pa_message != null) {
            pa_message.setCompoundDrawablesWithIntrinsicBounds(mResourceController.getResourceDrawable(Resource.drawable.pa_icon), null, null, null);
            pa_message.setText(mResourceController.getResourceString(Resource.string.pa_message));

            Animation animation = mResourceController.getResourceAnimation(Resource.anim.pa_on);
            pa_message.startAnimation(animation);
        }
    }

    public void paOff() {
        if (mViewPa != null) mViewPa.setVisibility(View.INVISIBLE);
    }

    public void pssOn() {
        if (mWindowManager == null) {
            Log.e(TAG, "paOn error with window manager null");
            return;
        }
        if (mViewPss == null) {
            mViewPss = mResourceController.getResourceLayout(Resource.layout.pss_layout);
            if (mViewPss == null) {
                Log.e(TAG, "pssOn view is null");
                return;
            }

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            ;
            params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
            params.gravity = Gravity.CENTER;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;

            mWindowManager.addView(mViewPss, params);
        } else {
            mViewPss.setVisibility(View.VISIBLE);
        }

        TextView pss_title = (TextView) mViewPss.findViewById(mResourceController.getResourceId(Resource.id.pss_title));
        if (pss_title != null)
            pss_title.setText(mResourceController.getResourceString(Resource.string.pss_title));

        SeekBar pss_brightness = (SeekBar) mViewPss.findViewById(mResourceController.getResourceId(Resource.id.pss_brightness));
        if (pss_brightness != null)
            pss_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mEventListener != null) try {
                        mEventListener.onBrightnessChanged(progress);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
    }

    public void pssOff() {
        if (mViewPss != null) mViewPss.setVisibility(View.INVISIBLE);
    }

}