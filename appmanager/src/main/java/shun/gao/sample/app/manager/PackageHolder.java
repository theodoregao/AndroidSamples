package shun.gao.sample.app.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by shun on 5/9/16.
 */
public class PackageHolder extends RecyclerView.ViewHolder {

    private static final String TAG = PackageHolder.class.getSimpleName();

    private View mItemView;
    private TextView mId;
    private Button mButtonForceStop;
    private TextView mPackageName;
    private TextView mPid;

    public PackageHolder(View itemView) {
        super(itemView);

        mItemView = itemView;
        mPackageName = (TextView) itemView.findViewById(R.id.package_name);
        mButtonForceStop = (Button) itemView.findViewById(R.id.button_force_stop);
        mPid = (TextView) itemView.findViewById(R.id.pid);
        mId = (TextView) itemView.findViewById(R.id.id);
    }

    public void render(final ActivityManager activityManager, final ActivityManager.RunningAppProcessInfo runningAppProcessInfo) {
        mPackageName.setText(runningAppProcessInfo.processName);
        mPid.setText("" + runningAppProcessInfo.pid);
        mId.setText("" + getAdapterPosition());
        mButtonForceStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "onClick() " + runningAppProcessInfo.processName);
                forceStopProgress(activityManager, runningAppProcessInfo.processName);
            }
        });
    }
    public static void forceStopProgress(ActivityManager am, String pkgName) {

        try {
            Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(am, pkgName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
