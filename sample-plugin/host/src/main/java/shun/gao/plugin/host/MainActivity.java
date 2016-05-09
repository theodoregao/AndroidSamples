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
import android.widget.TextView;
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

        Log.e(TAG, "TextView.class.getClassLoader(): " + TextView.class.getClassLoader());
        Log.e(TAG, "CustomButton.class.getClassLoader(): " + CustomButton.class.getClassLoader());

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
            Log.e(TAG, "otherContent.getClassLoader(): " + otherContext.getClassLoader());
            View view = getLayoutInflater().inflate(otherContext.getResources().getLayout(shun.gao.plugin.sdk.R.layout.activity_main), null);
//            View view = View.inflate(otherContext, shun.gao.plugin.sdk.R.layout.activity_main, null);
            setContentView(view);
            Log.e(TAG, "findViewById(shun.gao.plugin.sdk.R.id.button).getClass().getCanonicalName(): " + findViewById(shun.gao.plugin.sdk.R.id.button).getClass().getCanonicalName());
            Log.e(TAG, "findViewById(shun.gao.plugin.sdk.R.id.button).getClass().getClassLoader(): " + findViewById(shun.gao.plugin.sdk.R.id.button).getClass().getClassLoader());
            Log.e(TAG, "CustomButton.class.getClassLoader(): " + CustomButton.class.getClassLoader());
            CustomButton button = (CustomButton) findViewById(shun.gao.plugin.sdk.R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "XXXXXXXXXXXX", Toast.LENGTH_LONG).show();
                }
            });
            Log.e(TAG, "button: " + button.getClass().getCanonicalName());
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

    public void onClick(View view) {
        Toast.makeText(this, "onClick()", Toast.LENGTH_LONG).show();
    }
}
