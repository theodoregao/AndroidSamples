package shun.gao.plugin.host;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import shun.gao.plugin.sdk.CustomButton;

public class MainActivity extends AppCompatActivity {

//    private AssetManager mAssetManager;
//    private Resources mResources;
//
//    private String apkFileName = "sdk-debug.apk";
//    private String dexpath = null;
//    private File fileRelease = null;
//    private DexClassLoader classLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        dexpath = Environment.getExternalStorageDirectory() + File.separator + apkFileName;

//        classLoader = new DexClassLoader(dexpath, fileRelease.getAbsolutePath(), null, getClassLoader());

//        loadResources();

        try {
            final Context otherContext = createPackageContext("shun.gao.plugin.resource", Context.CONTEXT_IGNORE_SECURITY);
            View view = View.inflate(otherContext, shun.gao.plugin.sdk.R.layout.activity_main, null);
            setContentView(view);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        CustomButton customButton = (CustomButton) findViewById(shun.gao.plugin.sdk.R.id.button);
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Custom Button onClick()", Toast.LENGTH_LONG).show();
            }
        });
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
}
