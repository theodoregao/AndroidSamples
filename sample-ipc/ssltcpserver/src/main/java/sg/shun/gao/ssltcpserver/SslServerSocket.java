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

    private static final String TAG = SslServerSocket.class.getSimpleName();

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
                try {
                    init();
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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void init() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException, InterruptedException {
        String password = "user123";
        SSLServerSocket sslServerSocket = null;

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(context.getResources().openRawResource(R.raw.p12), password.toCharArray());
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password.toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
        ServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
        sslServerSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(PORT);
        Log.v(TAG, "server socket start");

        while (running) {
            SSLSocket client = (SSLSocket) sslServerSocket.accept();
            Log.v(TAG, "new client connected");
            if (callback != null) callback.onClientConnected(client);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            Thread.sleep(1000);
            Log.v(TAG, "reading");
            String message = in.readLine();
            Log.v(TAG, "read: " + message);

            if (callback != null) callback.onMessageReceived(message);
            Log.v(TAG, "writing");
            out.write("message from server");
            out.flush();
            Log.v(TAG, "write done");

            in.close();
            out.close();
        }

    }

    public interface SslServerSocketCallback {
        void onClientConnected(SSLSocket client);
        void onMessageReceived(String message);
    }
}
