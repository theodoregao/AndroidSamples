package com.example.sample_nio_server;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class NioServer extends AppCompatActivity {

    private static final String TAG = NioServer.class.getSimpleName();
    private static final String HOST = "172.17.4.173";
    private static final int PORT = 8086;
    private static final int BUFFER_SIZE = 2 * 1024;    // 2K buffer size
    private static final int NETWORK_ERROR_COUNT_LIMIT = 16;

    private EventSource mEventSource;
    private ByteBuffer mTcpBuffer;
    private ByteBuffer mUdpBuffer;
    private SocketChannel mTcpClient = null;

    private int mNetworkErrorCount = 0;

    private LogAdapter mLogs;
    private int mLogLevel;
    private MessageHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nio_server);

        mHandler = new MessageHandler(Looper.getMainLooper());

        mLogs = new LogAdapter(this, R.layout.item_log, new ArrayList<LogItem>());
        ((ListView) findViewById(R.id.list_log)).setAdapter(mLogs);

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

        InetSocketAddress hostAddress = new InetSocketAddress(InetAddress.getByName(HOST), PORT);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(hostAddress);
        serverSocketChannel.configureBlocking(false);
//		serverSocketChannel.register(selector, serverSocketChannel.validOps());
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.socket().setBroadcast(true);
        datagramChannel.socket().bind(new InetSocketAddress(PORT));
        datagramChannel.configureBlocking(false);
//		datagramChannel.register(selector, datagramChannel.validOps());
        datagramChannel.register(selector, SelectionKey.OP_READ);


        mTcpBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        mTcpBuffer.order(ByteOrder.BIG_ENDIAN);
        mUdpBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        mUdpBuffer.order(ByteOrder.BIG_ENDIAN);

        mEventSource = new EventSource();
        mEventSource.setListener(new EventSource.EventListener() {
            @Override
            public void onEventReceived(Event event) {
                Log.v(TAG, event.toString());
                log(0, event.toString());
            }

            @Override
            public void onError(EventSource.EventSourceError eventSourceError) {
                log(2, eventSourceError.name());
                if (eventSourceError == EventSource.EventSourceError.PackageFormatError && ++mNetworkErrorCount > NETWORK_ERROR_COUNT_LIMIT && mTcpClient != null) {
                    try {
                        mTcpClient.close();
                        mNetworkErrorCount = 0;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                log(2, "PackageFormatError, tcp disconnect");
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mTcpClient = null;
                }
            }
        });
        mEventSource.start();

        while (true) {
            System.out.println("waiting for select...");
            log(0, "waiting for select...");
            int noOfKeys = selector.select();
//			System.out.println("number of selected keys: " + noOfKeys);
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                if (!key.isValid()) {
                    System.out.println("not valid key, continue");
                    log(0, "not a valid key, continue");
                    continue;
                }

                Channel channel = key.channel();

                if (key.isAcceptable() && channel == serverSocketChannel) {
                    Log.v(TAG, "accept");
                    log(1, "accept new tcp connection");

                    if (mTcpClient != null) {
                        mTcpClient.close();
                        System.out.println("close tcp client");
                        log(1, "close previous tcp connection");
                    }

                    mTcpClient = serverSocketChannel.accept();
                    mTcpClient.configureBlocking(false);
                    mTcpClient.register(selector, SelectionKey.OP_READ);
                    Log.v(TAG, "accept a new connection from client: " + mTcpClient);
                    log(1, "establish a new tcp connection from client: " + mTcpClient);
                } else if (key.isReadable() && mTcpClient != null && channel == mTcpClient) {
                    Log.v(TAG, "tcp read");
                    mTcpBuffer.clear();
                    SocketChannel client = (SocketChannel) key.channel();
                    int size = client.read(mTcpBuffer);
                    Log.v(TAG, "size = " + size);
                    mEventSource.feedBytes(mTcpBuffer.array(), 0, size);
                    log(0, "tcp read size = " + size);
                } else if (key.isReadable() && datagramChannel != null && channel == datagramChannel) {
                    Log.v(TAG, "udp read");
                    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                    datagramChannel.receive(buffer);
                    mEventSource.feedBytesWithSequenceNumber(buffer.array());
                    Log.v(TAG, "size = " + buffer.remaining());
                    log(0, "udp read size = " + buffer.remaining());
                }
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_clear_log:
                mLogs.clear();
                break;

            case R.id.action_log_level:
                mLogLevel++;
                break;

            case R.id.action_status:
                log(3, mEventSource.toString());
                break;

            default:
                break;
        }
    }

    private class MessageHandler extends Handler {
        MessageHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 >= mLogLevel % 4) mLogs.log((String) msg.obj);
        }
    }

    private void log(int logLevel, String message) {
        Message msg = mHandler.obtainMessage();
        msg.arg1 = logLevel;
        msg.obj = message;
        mHandler.sendMessage(msg);
    }
}
