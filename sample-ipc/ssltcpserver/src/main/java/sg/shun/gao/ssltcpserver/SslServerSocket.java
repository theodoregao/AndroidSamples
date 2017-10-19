package sg.shun.gao.ssltcpserver;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

/**
 * Created by shun on 10/17/17.
 */

public class SslServerSocket {

    private static final int PORT = 1986;

    private Context context;
    private SslServerSocketCallback callback;
    private boolean running;

    public SslServerSocket(Context context, SslServerSocketCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void start() {
        running = true;
        new Thread() {
            @Override
            public void run() {
                init();
            }
        }.start();
    }

    private void init() {
        String password = "user123";
        SSLServerSocket sslServerSocket = null;

        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(context.getResources().openRawResource(R.raw.p12), password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password.toCharArray());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
            ServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            sslServerSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(PORT);
            Log.v(SslServer.TAG, "server socket start");

            while (running) {
                SSLSocket client = (SSLSocket) sslServerSocket.accept();
                Log.v(SslServer.TAG, "new client connected");
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                String message;
                while (true) {
                    int d = in.read();
                    Log.v(SslServer.TAG, "read: " + d);
//                    out.write("hello from server");
//                    out.flush();
//                    message = in.readLine();
//                    if (message != null && message.length() > 0) {
//                        Log.v(SslServer.TAG, "Client's message: " + message);
//                        Log.v(SslServer.TAG, "Responding same message: " + message);
//                        out.write(message);
//                        out.flush();
//                        out.close();
//                        in.close();
//                        client.close();
//                    } else {
//                        Log.v(SslServer.TAG, "got : " + message);
//                    }
                }
            }

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }

    public static interface SslServerSocketCallback {
        void onClientConnected(SSLSocket client);
    }
}
