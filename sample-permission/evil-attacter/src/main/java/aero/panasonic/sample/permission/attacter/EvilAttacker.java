package aero.panasonic.sample.permission.attacter;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EvilAttacker extends AppCompatActivity {

    static final String PROVIDER_NAME = "aero.panasonic.sample.provider";
    static final String URL = "content://" + PROVIDER_NAME + "/data";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final String KEY_SECRET = "secret";
    static final String KEY_PUBLIC = "public";
    private static final String TAG = EvilAttacker.class.getSimpleName();
    private static final String ACTION_LAUNCH_ACTIVITY = "aero.panasonic.sample.permission.provider.LAUNCH_ACTIVITY";
    private EditText editTextValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attacker);

        editTextValue = (EditText) findViewById(R.id.value);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.launchActivity:
                launchActivity();
                break;

            case R.id.queryPublic:
                queryPublic();
                break;

            case R.id.querySecret:
                querySecret();
                break;

            case R.id.writePublic:
                writePublic();
                break;

            case R.id.writeSecret:
                writeSecret();
                break;

            default:
                break;
        }
    }

    private void launchActivity() {
        Intent intent = new Intent();
        intent.setAction(ACTION_LAUNCH_ACTIVITY);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to launch activity", Toast.LENGTH_LONG).show();
        }
    }

    private void queryPublic() {
        Uri uri = Uri.parse(CONTENT_URI + "/" + KEY_PUBLIC);
        Cursor cursor = query(uri);
        if (cursor != null && cursor.moveToFirst()) {
            String publicValue = cursor.getString(cursor.getColumnIndex("value"));
            Log.v(TAG, "public value = " + publicValue);
            Toast.makeText(this, "get public value: " + publicValue, Toast.LENGTH_LONG).show();
        }
    }

    private void querySecret() {
        try {
            Uri uri = Uri.parse(CONTENT_URI + "/" + KEY_SECRET);
            Cursor cursor = query(uri);
            if (cursor != null && cursor.moveToFirst()) {
                String publicValue = cursor.getString(cursor.getColumnIndex("value"));
                Log.v(TAG, "secret value = " + publicValue);
                Toast.makeText(this, "get secret value: " + publicValue, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "uri is incorrect or permission denied", Toast.LENGTH_LONG).show();
        }
    }

    private Cursor query(Uri uri) {
        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            return cursor;
        } catch (Exception e) {
            Toast.makeText(this, "Unable query content", Toast.LENGTH_LONG).show();
        }

        return null;
    }

    private void writePublic() {
        update(Uri.parse(CONTENT_URI + "/" + KEY_PUBLIC), KEY_PUBLIC, editTextValue.getText().toString());
    }

    private void writeSecret() {
        update(Uri.parse(CONTENT_URI + "/" + KEY_SECRET), KEY_SECRET, editTextValue.getText().toString());
    }

    private void update(Uri uri, String key, String value) {
        ContentValues values = new ContentValues();
        values.put(key, value);

        try {
            getContentResolver().update(uri, values, null, null);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to write content", Toast.LENGTH_LONG).show();
        }
    }
}
