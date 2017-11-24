package gao.shun.sg.sampleadmob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class BannerAdActivity extends AppCompatActivity {

    private static final String TAG = BannerAdActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_ad);

        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));

        final AdView adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.v(TAG, "onAdLoaded()");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.v(TAG, "ERROR_CODE_INTERNAL_ERROR: " + AdRequest.ERROR_CODE_INTERNAL_ERROR);
                Log.v(TAG, "ERROR_CODE_INVALID_REQUEST: " + AdRequest.ERROR_CODE_INVALID_REQUEST);
                Log.v(TAG, "ERROR_CODE_NETWORK_ERROR: " + AdRequest.ERROR_CODE_NETWORK_ERROR);
                Log.v(TAG, "ERROR_CODE_NO_FILL: " + AdRequest.ERROR_CODE_NO_FILL);
                Log.v(TAG, "onAdFailedToLoad() " + errorCode);
            }

            @Override
            public void onAdOpened() {
                Log.v(TAG, "onAdOpened()");
            }

            @Override
            public void onAdLeftApplication() {
                Log.v(TAG, "onAdLeftApplication()");
            }

            @Override
            public void onAdClosed() {
                Log.v(TAG, "onAdClosed()");
            }
        });
    }
}
