package test.panasonic.aero.ssltcpclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SslClient extends AppCompatActivity implements SslClientSocket.SslClientSocketCallback {

    private SslClientSocket sslClientSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssl_client);
        sslClientSocket = new SslClientSocket(this, this);
    }

    public void connect(View view) {
        sslClientSocket.connect(((EditText) findViewById(R.id.ip)).getText().toString());
    }

    @Override
    public void onMessageReceived(final String message) {
        final TextView textView = (TextView) findViewById(R.id.text);
        runOnUiThread(new Thread() {
            @Override
            public void run() {
                textView.setText(message);
            }
        });
    }
}
