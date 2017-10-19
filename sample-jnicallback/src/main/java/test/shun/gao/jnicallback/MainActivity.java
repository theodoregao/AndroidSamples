package test.shun.gao.jnicallback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.text_view);

        Native.registerCallback(new JavaCallback() {
            @Override
            public void onValueReceived(final int value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("Receive from native: " + value);
                    }
                });

            }
        });
    }
}
