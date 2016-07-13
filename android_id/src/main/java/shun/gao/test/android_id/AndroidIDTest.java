package shun.gao.test.android_id;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;

public class AndroidIDTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_idtest);

        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(android_id);

        PackageManager pm = getPackageManager();
        ApplicationInfo appInfo = null;
        try {
            appInfo = pm.getApplicationInfo(getPackageName(), 0);
            String appFile = appInfo.sourceDir;
            long installed = new File(appFile).lastModified();
            textView.append("\n" + installed);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
