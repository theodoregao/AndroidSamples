package aero.panasonic.sample.android;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

public class Base64Sample extends AppCompatActivity {

    public static final int CODE_RESPONSE = 1;
    static final String AUTHORITY = "aero.panasonic.inflight.service.provider";
    static final String KEY_REQUEST_RESPONSE = "response";
    static final String RESPONSE_URL = "content://" + AUTHORITY + "/data/" + KEY_REQUEST_RESPONSE;
    static final Uri RESPONSE_URI = Uri.parse(RESPONSE_URL);
    private static final String TAG = Base64Sample.class.getSimpleName();

    public static Uri parseKey(String key) {
        String base64Code = Base64.encodeToString(key.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        return Uri.withAppendedPath(RESPONSE_URI, base64Code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base64_sample);

        Log.v(TAG, parseKey("http://api.airpana.com/inflight/services/metadata/v1/search?seat_class=all&lang=eng&exact_match=0&text=jur").toString());
    }
}
