package com.example.sample_nio_client;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SampleNioClient extends AppCompatActivity {

    private static final String TAG = SampleNioClient.class.getSimpleName();
    private static String HOST = "172.17.4.170";
    private static final int TCP_PORT = 52551;
    private static final int UDP_PORT = 52550;

    private static final int SLEEP_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_nio_client);

        HOST = getIPAddress(true);
    }

    public void onClick(final View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    switch (view.getId()) {
                        case R.id.action_ping:
                            ping();
                            break;

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
        TextView ip = (TextView) findViewById(R.id.ip);
        InetSocketAddress hoAddress = new InetSocketAddress(/*InetAddress.getByName(ip.getText().toString().trim())*/HOST, TCP_PORT);
        final SocketChannel socketChannel = SocketChannel.open(hoAddress);

        final Random random = new Random();

        for (int i = 0; i < 1; i++) {
            new Thread() {
                public void run() {
                    int j = 0;
                    for (int i = 0; i < 10; i++) {
                        ByteBuffer buffer = ByteBuffer.wrap(getData(i));
                        try {
                            socketChannel.write(buffer);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        buffer.clear();
                        try {
                            Thread.sleep(random.nextInt(1000));
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private void udp() {
        Log.v(TAG, "start tcp");
        final Random random = new Random();
        for (int i = 0; i < 1; i++) {
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
        ByteBuffer buf = ByteBuffer.allocate(1024);

        DatagramChannel datagramChannel = DatagramChannel.open();
//		datagramChannel.bind(new InetSocketAddress(9999));
        for (int i = 0; i < 1; i++) {
            buf.clear();
            buf.put(getUdpData());
            buf.flip();
            datagramChannel.send(buf, new InetSocketAddress(UDP_PORT));

            Thread.sleep(random.nextInt(1000));
        }
    }

    private byte[] getData(int value) {

        String testEvent = "{\n" +
                "   \"data\":{\n" +
                "      \"param1\":\"" + value % 2 + "\"\n" +
                "   },\n" +
                "   \"event_name\":\"core.exconnect.global_conn_enabled_change\"\n" +
                "}";

        String event2 = "{\n" +
                "   \"data\":{\n" +
                "      \"param1\":\"" + value * 1000 + "\"\n" +
                "   },\n" +
                "   \"event_name\":\"core.exconnect.total_coverage_remaining_change\"\n" +
                "}";

        String event3 = "{\n" +
                "   \"data\":{\n" +
                "      \"param1\":\"" + 2 * value * 1000 + "\"\n" +
                "   },\n" +
                "   \"event_name\":\"core.exconnect.time_until_coverage_change_change\"\n" +
                "}";

        int payloadSize = testEvent.getBytes().length;
        byte[] payloadSizeBytes = ByteBuffer.allocate(4).putInt(payloadSize).array();
        byte[] payloadsize2 = ByteBuffer.allocate(4).putInt(event2.getBytes().length).array();
        byte[] payloadsize3 = ByteBuffer.allocate(4).putInt(event3.getBytes().length).array();

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(payloadSizeBytes[2]);
            bos.write(payloadSizeBytes[3]);
            bos.write(testEvent.getBytes());
            bos.write(payloadsize2[2]);
            bos.write(payloadsize2[3]);
            bos.write(event2.getBytes());
            bos.write(payloadsize3[2]);
            bos.write(payloadsize3[3]);
            bos.write(event3.getBytes());
//            bos.write(payloadSizeBytes[2]);
//            bos.write(payloadSizeBytes[3]);
//            bos.write(testEvent.getBytes());
//            bos.write(invalidPayloadSizeBytes[2]);
//            bos.write(invalidPayloadSizeBytes[3]);
//            bos.write(invalidEvent.getBytes());
//            bos.write(payloadSizeBytes[2]);
//            bos.write(payloadSizeBytes[3]);
//            bos.write(testEvent.getBytes());
//            bos.write(payloadSizeBytes[2]);
//            bos.write(payloadSizeBytes[3]);
//            bos.write(testEvent.getBytes());
//            Log.v(TAG, new String(bos.toByteArray()));
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private byte[] getUdpData() {
        final String testEvent = "{\n" +
                "    \"event_name\":\"(String - Event Name)\",\n" +
                "    \"data\":\n" +
                "     { \n" +
                "        \"param1\":\"(String)\", \n" +
                "        \"param2\":\"(String)\"\n" +
                "     }\n" +
                "}";

        final String invalidEvent = "{\n" +
                "    \"event_name\":\"(String - Event Name)\",\n" +
                "    \"data\":\n" +
                "     { \n" +
                "        \"param1\":\"(String)\", \n" +
                "        \"param2\":\"(String)\"\n" +
                "     }\n" +
                "";
        int payloadSize = testEvent.getBytes().length;
        byte[] payloadSizeBytes = ByteBuffer.allocate(4).putInt(payloadSize).array();
        byte[] invalidPayloadSizeBytes = ByteBuffer.allocate(4).putInt(invalidEvent.getBytes().length).array();

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(new byte[] {0, 10, 0, 1});
            bos.write(payloadSizeBytes[2]);
            bos.write(payloadSizeBytes[3]);
            bos.write(testEvent.getBytes());
            bos.write(payloadSizeBytes[2]);
            bos.write(payloadSizeBytes[3]);
            bos.write(testEvent.getBytes());
            bos.write(invalidPayloadSizeBytes[2]);
            bos.write(invalidPayloadSizeBytes[3]);
            bos.write(invalidEvent.getBytes());
            bos.write(payloadSizeBytes[2]);
            bos.write(payloadSizeBytes[3]);
            bos.write(testEvent.getBytes());
            bos.write(payloadSizeBytes[2]);
            bos.write(payloadSizeBytes[3]);
            bos.write(testEvent.getBytes());
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private boolean ping(){
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try
        {
            TextView ip = (TextView) findViewById(R.id.ip);
            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 " + ip.getText().toString());
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(" mExitValue "+mExitValue);
            if(mExitValue==0){
                return true;
            }else{
                return false;
            }
        }
        catch (InterruptedException ignore)
        {
            ignore.printStackTrace();
            System.out.println(" Exception:"+ignore);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println(" Exception:"+e);
        }
        return false;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
}
