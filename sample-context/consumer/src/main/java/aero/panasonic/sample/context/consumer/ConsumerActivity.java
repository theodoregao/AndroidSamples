package aero.panasonic.sample.context.consumer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class ConsumerActivity extends AppCompatActivity {

    private static final String TAG = ConsumerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);

        Intent intent = getIntent();

        int resourceId = intent.getIntExtra("layout_id", 0);
        if (resourceId == 0) return;

        int buttonTopId = intent.getIntExtra("buttonTop", 0);
        if (buttonTopId == 0) return;

        int buttonBottomId = intent.getIntExtra("buttonBottom", 0);
        if (buttonBottomId == 0) return;

        try {
            Context context = createPackageContext("aero.panasonic.sample.context.resource", Context.CONTEXT_IGNORE_SECURITY);

            View view = View.inflate(context, resourceId, null);

            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.layout);

            Button buttonTop = (Button) view.findViewById(buttonTopId);
            Button buttonBottom = (Button) view.findViewById(buttonBottomId);

            buttonTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ConsumerActivity.this, "Top button clicked", Toast.LENGTH_LONG).show();
                }
            });

            buttonBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ConsumerActivity.this, "Bottom button clicked", Toast.LENGTH_LONG).show();
                }
            });

            frameLayout.addView(view);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, e.getLocalizedMessage());
        }
    }
}
