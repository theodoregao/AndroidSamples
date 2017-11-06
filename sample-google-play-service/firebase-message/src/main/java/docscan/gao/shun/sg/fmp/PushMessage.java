package docscan.gao.shun.sg.fmp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PushMessage extends AppCompatActivity {

    private static final String TAG = PushMessage.class.getSimpleName();

    private TextView text;
    private EditText editTextInstanceId;
    private EditText editTextRemoteInstanceId;
    private EditText editTextMessage;

    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_message);

        text = (TextView) findViewById(R.id.text) ;
        editTextInstanceId = (EditText) findViewById(R.id.instance_id);
        editTextRemoteInstanceId = (EditText) findViewById(R.id.remote_instance_id);
        editTextMessage = (EditText) findViewById(R.id.message);

        MessageCenter.getInstance().setMessageListener(new MessageCenter.MessageListener() {
            @Override
            public void onMessageReceived(final String message) {
                Log.v(TAG, "onMessageReceived() " + message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(message);
                    }
                });
            }
        });

        sharedPreferenceManager = new SharedPreferenceManager(this, new SharedPreferenceManager.InstanceIdListener() {
            @Override
            public void onInstanceIdReceived(final String instanceId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editTextInstanceId.setText(instanceId);
                    }
                });
            }
        });

        if (sharedPreferenceManager.hasInstanceId()) {
            Log.v(TAG, "get instance id: " + sharedPreferenceManager.getInstanceId());
            editTextInstanceId.setText(sharedPreferenceManager.getInstanceId());
        }
    }

    public void onClick(View view) {
        Log.v(TAG, "send message");
//        FirebaseMessaging fm = FirebaseMessaging.getInstance();
//        fm.send(new RemoteMessage.Builder(editTextRemoteInstanceId.getText().toString() + "@gcm.googleapis.com")
//                .setMessageId("" + System.currentTimeMillis())
//                .addData("my_message", editTextMessage.getText().toString())
//                .addData("my_action","action")
//                .build());
        sendMessage(editTextRemoteInstanceId.getText().toString(), editTextMessage.getText().toString());
    }

    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    OkHttpClient mClient = new OkHttpClient();
    public void sendMessage(final String recipient, final String message) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject data = new JSONObject();
                    data.put("message", message);
                    root.put("data", data);
                    root.put("registration_id", recipient);
                    root.put("to", recipient);
                    String result = postToFCM(root.toString());
                    Log.d(TAG, "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");
                    Toast.makeText(PushMessage.this, "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PushMessage.this, "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + "AIzaSyD6Fi4N1d-tW2La7RdF9C_xqU5RricKFr8")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
}
