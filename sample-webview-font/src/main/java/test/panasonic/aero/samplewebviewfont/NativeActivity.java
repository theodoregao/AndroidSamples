package test.panasonic.aero.samplewebviewfont;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class NativeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);

        TextView andalewtj = findViewById(R.id.text_andalewtj);
        andalewtj.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/andalewtj.ttf"));

        TextView chcry = findViewById(R.id.text_chcry);
        chcry.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/BLKCHCRY.TTF"));

//        TextView courier = findViewById(R.id.text_courier);
//        courier.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/andalewtj.ttf"));

        TextView epimodem = findViewById(R.id.text_epimodem);
        epimodem.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/EPIMODEM.TTF"));
    }
}
