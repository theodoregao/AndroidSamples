package com.example.sample_nio_server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer extends AppCompatActivity {

    private static final String TAG = NioServer.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nio_server);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                startserver();
                } catch (IOException ex) {
                    System.out.println("ERROR");
                }
            }
        }.start();
    }

    private void startserver() throws IOException {
        Selector selector = Selector.open();

        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 5454);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(hostAddress);
        serverSocketChannel.configureBlocking(false);
//		serverSocketChannel.register(selector, serverSocketChannel.validOps());
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.socket().setBroadcast(true);
        datagramChannel.socket().bind(new InetSocketAddress(5454));
        datagramChannel.configureBlocking(false);
//		datagramChannel.register(selector, datagramChannel.validOps());
        datagramChannel.register(selector, SelectionKey.OP_READ);

        SocketChannel tcpClient = null;
        int emptyCount = 0;

        int tcpCount = 0;
        int udpCount = 0;

        while (true) {
            System.out.println("waiting for select...");
            int noOfKeys = selector.select();
//			System.out.println("number of selected keys: " + noOfKeys);
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                System.out.println(key.toString());

                if (!key.isValid()) {
                    System.out.println("not valid key, continue");
                    continue;
                }

                Channel channel = key.channel();

                if (key.isAcceptable() && channel == serverSocketChannel) {

                    if (tcpClient != null) {
                        tcpClient.close();
                        System.out.println("close tcp client");
                    }

                    tcpClient = serverSocketChannel.accept();
                    tcpClient.configureBlocking(false);
                    tcpClient.register(selector, SelectionKey.OP_READ);
                    System.out.println("accept a new connection from client: " + tcpClient);
                    Log.v(TAG, "accept a new connection from client: " + tcpClient);
                } else if (key.isReadable() && channel == tcpClient) {
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    client.read(buffer);
                    String output = new String(buffer.array()).trim();
                    if (output.isEmpty()) {
                        if (emptyCount++ > 100) {
                            tcpClient.close();
                            tcpClient = null;
                        }
                    } else emptyCount = 0;
//					System.out.println("message from tcp: " + output);
                    System.out.println("tcp count: " + tcpCount++);
                    Log.v(TAG, "tcp count: " + tcpCount);
                } else if (key.isReadable() && channel == datagramChannel) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    datagramChannel.receive(buffer);
//					System.out.println("message from udp: " + new String(buffer.array()).trim());
                    System.out.println("udp count: " + udpCount++);
                    Log.v(TAG, "udp count: " + udpCount);
                }
            }
        }
    }
}
