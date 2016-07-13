package shun.gao.sample.app.manager;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.Process;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AppManager extends AppCompatActivity {

    private static final String TAG = AppManager.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

//        Toast.makeText(this, "uid: " + Process.myUid(), Toast.LENGTH_LONG).show();
//        Toast.makeText(this, Shell.SU.version(true), Toast.LENGTH_SHORT).show();

//        Toast.makeText(this, "" + Shell.SU.available(), Toast.LENGTH_LONG).show();

        RecyclerView recyclerViewPackages = (RecyclerView) findViewById(R.id.recycler_view_packages);
        recyclerViewPackages.setLayoutManager(new LinearLayoutManager(this));
        final PackageAdapter packageAdapter = new PackageAdapter(getPackageManager(), (ActivityManager) getSystemService(ACTIVITY_SERVICE));
        recyclerViewPackages.setAdapter(packageAdapter);

        final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        Button buttonForceStopAll = (Button) findViewById(R.id.button_force_stop_all);
        buttonForceStopAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo: activityManager.getRunningAppProcesses()) {
                    if (runningAppProcessInfo.pid != 0 && !TextUtils.equals(runningAppProcessInfo.processName, "shun.gao.sample.app.manager")) {
//                        forceStopPackage(activityManager, runningAppProcessInfo.processName);

//                        android.os.Process.killProcess(runningAppProcessInfo.pid);
//                        forceStop(runningAppProcessInfo);
                    }
                }

                for (ApplicationInfo applicationInfo: getPackageManager().getInstalledApplications(PackageManager.GET_ACTIVITIES)) {
                    if (!TextUtils.equals(applicationInfo.packageName, "shun.gao.sample.app.manager"))
                    {
                        forceStopPackage(activityManager, applicationInfo.packageName);
//                        activityManager.killBackgroundProcesses(applicationInfo.packageName);
                    }
                }

                packageAdapter.reset();
            }
        });
    }

    private void forceStop(ActivityManager.RunningAppProcessInfo appProcess) {
        try
        {
            String command = "kill -9 " + appProcess.pid + "\n";
            Log.v(TAG, command);
            Process suProcess = Runtime.getRuntime().exec(command);
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

            os.flush();
            os.writeBytes("am force-stop aero.panasonic.widget\n");//+appProcess.processName + "\n");

            os.flush();
            os.close();
            suProcess.waitFor();

        }

        catch (IOException ex)
        {
            Log.e(TAG, ex.toString());
//            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (SecurityException ex)
        {
            Log.e(TAG, ex.toString());
//            Toast.makeText(getApplicationContext(), "Can't get root access2",
//                    Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
//            Toast.makeText(getApplicationContext(), "Can't get root access3",
//                    Toast.LENGTH_LONG).show();
        }
    }


    public static void forceStopPackage(ActivityManager am, String pkgName) {

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


    public static void forceStopRecent(ActivityManager am, int taskId) {

        try {
            Method forceStopPackage = am.getClass().getDeclaredMethod("removeTask", Integer.class, Integer.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(am, taskId, 1);
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
