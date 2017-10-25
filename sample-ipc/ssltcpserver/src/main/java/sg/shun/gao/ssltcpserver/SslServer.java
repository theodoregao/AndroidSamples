package sg.shun.gao.ssltcpserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import javax.net.ssl.SSLSocket;

public class SslServer extends AppCompatActivity implements SslServerSocket.SslServerSocketCallback {

    public static final String TAG = SslServer.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssl_server);

        SslServerSocket sslServerSocket = new SslServerSocket(this, this);
        sslServerSocket.start();
    }

    @Override
    public void onClientConnected(final SSLSocket client) {
        final TextView textView = (TextView) findViewById(R.id.text);
        runOnUiThread(new Thread() {
            @Override
            public void run() {
                textView.setText("new client connected: " + client);
            }
        });
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
