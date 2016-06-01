package shun.gao.sample.usage.stats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = SecondActivity.class.getSimpleName();

    private AnalyticsTimeInterval analyticsTimeInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        analyticsTimeInterval = new AnalyticsTimeInterval(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.print:
                print();
                break;

            case R.id.add_stamp:
                addStamp();
                break;

            case R.id.remove_stamp:
                removeStamp();
                break;
        }
    }

    private void print() {
        long time = analyticsTimeInterval.calculateTimeInterval(SecondActivity.class.getSimpleName() + " on") / 1000;
        Log.v(TAG, "time: " + time);

        ((TextView) findViewById(R.id.textView)).setText(time + " seconds");
    }

    private void addStamp() {
        analyticsTimeInterval.addTimeStamp(SecondActivity.class.getSimpleName() + " on");
    }

    private void removeStamp() {
        analyticsTimeInterval.addTimeStamp(SecondActivity.class.getSimpleName() + " off");
    }
}
