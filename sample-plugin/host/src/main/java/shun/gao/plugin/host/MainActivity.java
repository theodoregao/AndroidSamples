package shun.gao.plugin.host;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import dalvik.system.DexClassLoader;
import shun.gao.plugin.sdk.CustomButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

//    private AssetManager mAssetManager;
//    private Resources mResources;

//    private String apkFileName = "sdk-debug.apk";
//    private String dexpath = null;
//    private File fileRelease = null;
//    private DexClassLoader classLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        dexpath = Environment.getExternalStorageDirectory() + File.separator + apkFileName;

//        fileRelease = getDir("dex", 0);
//        classLoader = new DexClassLoader(dexpath, fileRelease.getAbsolutePath(), null, getClassLoader());

//        loadResources();

//        Thread.currentThread().setContextClassLoader(classLoader);
//        Looper.getMainLooper().getThread().setContextClassLoader(classLoader);
//
//        Log.e(TAG, CustomButton.class.getClassLoader().toString());

        try {
            final Context otherContext = createPackageContext("shun.gao.plugin.resource", Context.CONTEXT_IGNORE_SECURITY | CONTEXT_INCLUDE_CODE);
            Log.e(TAG, "otherContent.getClasLoader(): " + otherContext.getClassLoader());
            View view = View.inflate(otherContext, shun.gao.plugin.sdk.R.layout.activity_main, null);
            setContentView(view);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

//    private void loadResources() {
//        try {
//            AssetManager assetManager = AssetManager.class.newInstance();
//            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
//            addAssetPath.invoke(assetManager, dexpath);
//            mAssetManager = assetManager;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Resources superRes = super.getResources();
//        superRes.getDisplayMetrics();
//        superRes.getConfiguration();
//        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
//    }

    private BroadcastReceiver CRITICAL_EVENT_RECEIVER = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), getString(R.string.ACTION_CRITICAL_EVENT))) {
                String params = intent.getStringExtra(getString(shun.gao.plugin.sdk.R.string.EXTRA_CRITICAL_EVENT));
                Toast.makeText(MainActivity.this, "intent: " + intent + ", params: " + params, Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.ACTION_CRITICAL_EVENT));
//        LocalBroadcastManager.getInstance(this).registerReceiver(CRITICAL_EVENT_RECEIVER, intentFilter);
        registerReceiver(CRITICAL_EVENT_RECEIVER, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(CRITICAL_EVENT_RECEIVER);
        unregisterReceiver(CRITICAL_EVENT_RECEIVER);
    }
}
