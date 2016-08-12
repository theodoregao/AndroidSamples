package com.example.sample_nio_server;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.DatagramPacket;
import java.net.DatagramSocketImpl;
import java.net.DatagramSocketImplFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
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
    private static final String MULTICAST_HOST = "239.0.0.1";
    private static final int TCP_PORT = 8086;
    private static final int UDP_PORT = 8086;
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

        WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiManager.MulticastLock multicastLock = wm.createMulticastLock("NioServer");
        multicastLock.acquire();

        mHandler = new MessageHandler(Looper.getMainLooper());
        mEventSource = new EventSource();

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

        new Thread() {
            @Override
            public void run() {
                try {
                    MulticastSocket multicastSocket = new MulticastSocket(UDP_PORT);
                    multicastSocket.joinGroup(InetAddress.getByName(MULTICAST_HOST));
                    byte[] data = new byte[BUFFER_SIZE];
                    while(true) {
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        multicastSocket.receive(packet);
                        mEventSource.feedBytesWithSequenceNumber(data, packet.getOffset(), packet.getLength());
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void _pairChannelAndSocket(DatagramChannel channel, MulticastSocket socket) {
        try {
            socket.setReuseAddress(true);
            socket.setBroadcast(true);
            Field f = channel.getClass().getDeclaredField("socket");
            f.setAccessible(true);
            f.set(channel, socket);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void startserver() throws IOException {
        Selector selector = Selector.open();

        InetSocketAddress hostAddress = new InetSocketAddress(InetAddress.getByName(HOST), TCP_PORT);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(hostAddress);
        serverSocketChannel.configureBlocking(false);
//		serverSocketChannel.register(selector, serverSocketChannel.validOps());
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


//        DatagramChannel datagramChannel = DatagramChannel.open();
////        _pairChannelAndSocket(datagramChannel, multicastSocket);
//        MyDatagramSocketImplFactory factory = new MyDatagramSocketImplFactory();
//        factory.createDatagramSocketImpl().setOption(SocketOptions.IP_MULTICAST_IF, );
//        datagramChannel.socket().setDatagramSocketImplFactory(factory);
//        datagramChannel.socket().setBroadcast(true);
//        datagramChannel.socket().setReuseAddress(true);
////        datagramChannel.socket().bind(new InetSocketAddress(UDP_PORT));
//        datagramChannel.configureBlocking(false);
////		datagramChannel.register(selector, datagramChannel.validOps());
//        datagramChannel.register(selector, SelectionKey.OP_READ);

        mTcpBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        mTcpBuffer.order(ByteOrder.BIG_ENDIAN);
        mUdpBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        mUdpBuffer.order(ByteOrder.BIG_ENDIAN);

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
//                } else if (key.isReadable() && datagramChannel != null && channel == datagramChannel) {
//                    Log.v(TAG, "udp read");
//                    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
//                    datagramChannel.receive(buffer);
//                    mEventSource.feedBytesWithSequenceNumber(buffer.array(), 0, buffer.array().length);
//                    Log.v(TAG, "size = " + buffer.remaining());
//                    log(0, "udp read size = " + buffer.remaining());
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

    private static class MyDatagramSocketImplFactory implements DatagramSocketImplFactory {

        @Override
        public DatagramSocketImpl createDatagramSocketImpl() {

            MulticastSocket multicastSocket = null;
            try {
                multicastSocket = new MulticastSocket(TCP_PORT);
                multicastSocket.joinGroup(InetAddress.getByName(MULTICAST_HOST));
                return new MyDatagramSocketImpl(multicastSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new MyDatagramSocketImpl(multicastSocket);
        }
    }

    private static class MyDatagramSocketImpl extends DatagramSocketImpl {

        private MulticastSocket mMulticastSocket;

        MyDatagramSocketImpl(MulticastSocket multicastSocket) {
            mMulticastSocket = multicastSocket;
        }

        @Override
        protected void bind(int port, InetAddress addr) throws SocketException {
            mMulticastSocket.bind(InetSocketAddress.createUnresolved(addr.getHostAddress(), port));
        }

        @Override
        protected void close() {
            mMulticastSocket.close();
        }

        @Override
        protected void create() throws SocketException {

        }

        @Override
        protected byte getTTL() throws IOException {
            return mMulticastSocket.getTTL();
        }

        @Override
        protected int getTimeToLive() throws IOException {
            return mMulticastSocket.getTimeToLive();
        }

        @Override
        protected void join(InetAddress addr) throws IOException {
            mMulticastSocket.joinGroup(addr);
        }

        @Override
        protected void joinGroup(SocketAddress addr, NetworkInterface netInterface) throws IOException {
            mMulticastSocket.joinGroup(addr, netInterface);
        }

        @Override
        protected void leave(InetAddress addr) throws IOException {
            mMulticastSocket.leaveGroup(addr);
        }

        @Override
        protected void leaveGroup(SocketAddress addr, NetworkInterface netInterface) throws IOException {
            mMulticastSocket.leaveGroup(addr, netInterface);
        }

        @Override
        protected int peek(InetAddress sender) throws IOException {
            return 0;
        }

        @Override
        protected void receive(DatagramPacket pack) throws IOException {
            mMulticastSocket.receive(pack);
        }

        @Override
        protected void send(DatagramPacket pack) throws IOException {
            mMulticastSocket.send(pack);
        }

        @Override
        protected void setTimeToLive(int ttl) throws IOException {
            mMulticastSocket.setTimeToLive(ttl);
        }

        @Override
        protected void setTTL(byte ttl) throws IOException {
            mMulticastSocket.setTTL(ttl);
        }

        @Override
        protected int peekData(DatagramPacket pack) throws IOException {
            return 0;
        }

        @Override
        public Object getOption(int optID) throws SocketException {
            return null;
        }

        @Override
        public void setOption(int optID, Object val) throws SocketException {

        }
    }
}
