package sg.shun.gao.test.tcp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ApiService extends Service {

    private static final String TAG = ApiService.class.getSimpleName();

    private static final int SERVERPORT = 1986;

    private ServerSocket serverSocket;

    public ApiService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IBinder binder = new Api.Stub() {

        @Override
        public void start() {
            new Thread(new ServerThread()).start();
        }
    };

    class ServerThread implements Runnable {

        public void run() {
            Socket socket = null;
            try {
                serverSocket = new ServerSocket(SERVERPORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {

                try {

                    Log.v(TAG, "start server");
                    socket = serverSocket.accept();
                    Log.v(TAG, "accept");

                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class CommunicationThread implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;
        private BufferedWriter output;

        public CommunicationThread(Socket clientSocket) {

            this.clientSocket = clientSocket;

            try {

                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                this.output = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

            while (!Thread.currentThread().isInterrupted()) {

                try {

                    char[] buffer = new char[1024];

                    while (true) {
                        input.read(buffer);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
