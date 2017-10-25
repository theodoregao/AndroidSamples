package test.panasonic.aero.ssltcpclient;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by shun on 10/25/17.
 */

public class SslClientSocket {

    private static final String TAG = SslClientSocket.class.getSimpleName();

    private static final int PORT = 1986;

    private Context context;
    private String ip;
    private boolean running;
    private SslClientSocketCallback callback;

    public SslClientSocket(Context context, SslClientSocketCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void connect(String ip) {
        this.ip = ip;
        running = true;
        new Thread() {
            @Override
            public void run() {
                try {
                    init();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnrecoverableKeyException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void init() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException, UnrecoverableKeyException {
        String password = "user123";

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(context.getResources().openRawResource(R.raw.p12), password.toCharArray());
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        keyStore.setCertificateEntry("pem", certificateFactory.generateCertificate(context.getResources().openRawResource(R.raw.pem)));

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        SSLSocket sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(ip, PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

        Log.v(TAG, "writing");
        out.write("message from client");
        out.flush();
        Log.v(TAG, "write done");

        Log.v(TAG, "reading");
        String message = in.readLine();
        if (callback != null) callback.onMessageReceived(message);

        in.close();
        out.close();
    }

    public interface SslClientSocketCallback {
        void onMessageReceived(String message);
    }

}
