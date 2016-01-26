package gao.shun.sg.popserver;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Theodore on 2016/1/25.
 */
public class PopupManager {

    private static final String TAG = PopupManager.class.getSimpleName();

    private static final int APPWIDGET_HOST_ID = PopupManager.class.hashCode();

    Context context;
    Resources resources;
    WindowManager windowManager;

    AppWidgetManager appWidgetManager;
    AppWidgetHost appWidgetHost;

    FrameLayout viewPa;
    FrameLayout viewPss;

    public PopupManager(Context context) {
        this.context = context;
        resources = context.getResources();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetHost = new AppWidgetHost(context, APPWIDGET_HOST_ID);
    }

    public View getPaView() {
        List<AppWidgetProviderInfo> appWidgetInfos = new ArrayList<>();
        appWidgetInfos = appWidgetManager.getInstalledProviders();

        AppWidgetProviderInfo appWidgetProviderInfo = null;
        for (int i = 0; i < appWidgetInfos.size(); i++) {
            appWidgetProviderInfo = appWidgetInfos.get(i);
            if (appWidgetProviderInfo.provider.getPackageName().equals("gao.shun.sg.app.widget")
                    && appWidgetProviderInfo.provider.getClassName().toLowerCase().contains("pa")) {
                break;
            }
        }
        return appWidgetHost.createView(context, appWidgetHost.allocateAppWidgetId(), appWidgetProviderInfo);
    }

    public View getPssView() {
        List<AppWidgetProviderInfo> appWidgetInfos = new ArrayList<>();
        appWidgetInfos = appWidgetManager.getInstalledProviders();

        AppWidgetProviderInfo appWidgetProviderInfo = null;
        for (int i = 0; i < appWidgetInfos.size(); i++) {
            appWidgetProviderInfo = appWidgetInfos.get(i);
            if (appWidgetProviderInfo.provider.getPackageName().equals("gao.shun.sg.app.widget")
                    && appWidgetProviderInfo.provider.getClassName().toLowerCase().contains("pss")) {
                break;
            }
        }
        return appWidgetHost.createView(context, appWidgetHost.allocateAppWidgetId(), appWidgetProviderInfo);
    }

    public void paOn() {
        paOff();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
        params.gravity = Gravity.CENTER;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;

        viewPa = (FrameLayout) View.inflate(context, R.layout.activity_popup_server, null);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        viewPa.addView(getPaView(), layoutParams);

        windowManager.addView(viewPa, params);
    }

    public void paOff() {
        if (viewPa != null) windowManager.removeView(viewPa);
        viewPa = null;
    }

    public void pssOn() {
        pssOff();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
        params.gravity = Gravity.CENTER;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;

        viewPss = (FrameLayout) View.inflate(context, R.layout.activity_popup_server, null);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        viewPss.addView(getPssView(), layoutParams);

        windowManager.addView(viewPss, params);
    }

    public void pssOff() {
        if (viewPss != null) windowManager.removeView(viewPss);
        viewPss = null;
    }
}
