package test.panasonic.aero.samplewebviewfont;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_web_view:
                Log.v(TAG, "Web View");
                startActivity(new Intent(this, WebViewActivity.class));
                break;

            case R.id.action_native_view:
                Log.v(TAG, "Native View");
                startActivity(new Intent(this, NativeActivity.class));
                break;

            default:
                break;
        }
    }
}
