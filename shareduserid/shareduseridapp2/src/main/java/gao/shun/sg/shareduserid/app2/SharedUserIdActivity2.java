package gao.shun.sg.shareduserid.app2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import sg.shun.gao.libs.android.share.SharedComponentManager;

public class SharedUserIdActivity2 extends AppCompatActivity {

    private static final String TAG = SharedUserIdActivity2.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ClassLoader classLoader = getClass().getClassLoader();
        int level = 0;
        while (classLoader != null) {
            Log.d(TAG, "SharedUserIdActivity2 (level " + level + "): " + classLoader);
            classLoader = classLoader.getParent();
            ++level;
        }

        setContentView(R.layout.activity_shared_user_id2);

        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(SharedComponentManager.getInstance().getDescription());
    }

    public void onClick(View view) {
        Log.v(TAG, "onClick()");
        TextView textView = SharedComponentManager.getInstance().getTextView();
        if (textView != null) {
            Log.v(TAG, textView.getText().toString());
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.container);
            linearLayout.addView(textView);
        } else {
            Log.v(TAG, "view is null");
        }

        String text = SharedComponentManager.getInstance().getText();
        if (text != null && !text.equals("")) {
            ((TextView) findViewById(R.id.text)).setText(text);
        }
    }
}
