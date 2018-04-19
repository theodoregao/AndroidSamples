package test.panasonic.aero.samplewebviewfont;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

public class NativeActivity extends AppCompatActivity {

    private static final String TAG = NativeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        TextView tradbdo = findViewById(R.id.text_tradbdo);
        tradbdo.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/tradbdo.ttf"));

        TextView andalewtj = findViewById(R.id.text_andalewtj);
        andalewtj.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/andalewtj.ttf"));

        TextView andalwtj = findViewById(R.id.text_andalwtj);
        andalwtj.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/andalwtj.ttf"));

        TextView epimodem = findViewById(R.id.text_epimodem);
        epimodem.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/EPIMODEM.TTF"));
    }
}
