package sg.shun.gao.ssltcpserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SslServer extends AppCompatActivity {

    public static final String TAG = SslServer.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssl_server);

        SslServerSocket sslServerSocket = new SslServerSocket(this, null);
        sslServerSocket.start();
    }
}
