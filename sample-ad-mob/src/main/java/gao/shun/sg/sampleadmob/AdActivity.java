package gao.shun.sg.sampleadmob;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AdActivity extends AppCompatActivity {

    private static final String TAG = AdActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
    }

    public void onBannerClicked(View view) {
        startActivity(new Intent(this, BannerAdActivity.class));
    }

    public void onInterstitialClicked(View view) {
        startActivity(new Intent(this, InterstitialAdActivity.class));
    }

}
