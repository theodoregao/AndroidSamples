package aero.panasonic.sample.android;

import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class UriMatchSample extends AppCompatActivity {

    private static final String TAG = UriMatchSample.class.getSimpleName();

    private static final String SCHEMA = "content://";
    private static final String AUTHORITY = "sg.shun.gao.authority";

    private static final String URI_ITEMS = "/items";
    private static final String URI_ITEM = "/items/#";
    private static final String URI_ITEM_ = "/items/*";

    private static final int ITEMS = 1;
    private static final int ITEM = 2;
    private static final int ITEM_ = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uri_match_sample);

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, URI_ITEMS, ITEMS);
        uriMatcher.addURI(AUTHORITY, URI_ITEM, ITEM);
        uriMatcher.addURI(AUTHORITY, URI_ITEM_, ITEM_);

        Log.v(TAG, SCHEMA + AUTHORITY + ": " + uriMatcher.match(Uri.parse(SCHEMA + AUTHORITY)));
        Log.v(TAG, SCHEMA + AUTHORITY + URI_ITEMS + ": " + uriMatcher.match(Uri.parse(SCHEMA + AUTHORITY + URI_ITEMS)));
        Log.v(TAG, SCHEMA + AUTHORITY + URI_ITEMS + "/0" + ": " + uriMatcher.match(Uri.parse(SCHEMA + AUTHORITY + URI_ITEMS + "/0")));
        Log.v(TAG, SCHEMA + AUTHORITY + URI_ITEMS + "/1024" + ": " + uriMatcher.match(Uri.parse(SCHEMA + AUTHORITY + URI_ITEMS + "/1024")));
        Log.v(TAG, SCHEMA + AUTHORITY + URI_ITEMS + "/a_b_c_1" + ": " + uriMatcher.match(Uri.parse(SCHEMA + AUTHORITY + URI_ITEMS + "/a_b_c_1")));
    }
}
