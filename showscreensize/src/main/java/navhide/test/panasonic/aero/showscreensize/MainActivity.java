package navhide.test.panasonic.aero.showscreensize;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Log.v(TAG, "width: " + displayMetrics.widthPixels);
        Log.v(TAG, "height: " + displayMetrics.heightPixels);
        Log.v(TAG, "density: " + displayMetrics.density);
        Log.v(TAG, "densityDpi: " + displayMetrics.densityDpi);
    }
}
