package gao.shun.sg.shareduserid.app1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import sg.shun.gao.libs.android.share.SharedComponentManager;

public class SharedUserIdActivity1 extends AppCompatActivity {

    private static final String TAG = SharedUserIdActivity1.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ClassLoader classLoader = getClass().getClassLoader();
        int level = 0;
        while (classLoader != null) {
            Log.d(TAG, "SharedUserIdActivity1 (level " + level + "): " + classLoader);
            classLoader = classLoader.getParent();
            ++level;
        }

        setContentView(R.layout.activity_shared_user_id1);

        SharedComponentManager sharedComponentManager = SharedComponentManager.getInstance();
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(sharedComponentManager.getDescription());
    }
}
