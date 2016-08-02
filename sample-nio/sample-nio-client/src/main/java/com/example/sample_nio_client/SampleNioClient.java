package com.example.sample_nio_client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class SampleNioClient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_nio_client);
    }

    public void onClick(final View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    switch (view.getId()) {
                        case R.id.action_tcp:
                            tcp();
                            break;

                        case R.id.action_udp:
                            udp();
                            break;

                        default:
                            break;
                    }
                }
                catch (IOException ex) {

                }
            }
        }.start();

    }

    private void tcp() throws IOException {
        InetSocketAddress hoAddress = new InetSocketAddress("localhost", 5454);
        final SocketChannel socketChannel = SocketChannel.open(hoAddress);
        System.out.println("client sending message to server");
        final String[] messages = new String[] {"first message", "second message", "Bye."};

        final Random random = new Random();

        for (int i = 0; i < 100; i++) {
            new Thread() {
                public void run() {
                    int i = 0;
                    boolean running = true;
                    while (running) {
                        byte[] message = messages[i++ % 3].getBytes();
                        ByteBuffer buffer = ByteBuffer.wrap(message);
                        try {
                            socketChannel.write(buffer);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            running = false;
                        }
                        buffer.clear();
                        try {
                            Thread.sleep(random.nextInt(100));
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }

    private void udp() {
        final Random random = new Random();
        for (int i = 0; i < 100; i++) {
            final int index = i;
            new Thread() {
                public void run() {
                    try {
                        sendMessage(index, random.nextInt(1000));
                    } catch (IOException | InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                };
            }.start();
        }
    }

    private void sendMessage(int index, int n) throws IOException, InterruptedException {
        Random random = new Random();
        String data = "data from udp server " + index;
        ByteBuffer buf = ByteBuffer.allocate(1024);

        DatagramChannel datagramChannel = DatagramChannel.open();
//		datagramChannel.bind(new InetSocketAddress(9999));
        for (int i = 0; i < n; i++) {
            String d = data + " " + i + "/" + n;
            buf.clear();
            buf.put(d.getBytes());
            buf.flip();
            datagramChannel.send(buf, new InetSocketAddress(5454));

            Thread.sleep(random.nextInt(1000));
        }
    }
}
