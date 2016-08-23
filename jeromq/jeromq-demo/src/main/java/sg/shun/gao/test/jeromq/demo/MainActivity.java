package sg.shun.gao.test.jeromq.demo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jeromq.LogAdapter;
import jeromq.LogItem;
import jeromq.Publisher;
import jeromq.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final int port = 6000;

    private MessageHandler messageHandler;
    private LogAdapter mLogs;
    private Publisher publisher;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogs = new LogAdapter(this, R.layout.item_log, new ArrayList<LogItem>());
        ((ListView) findViewById(R.id.log)).setAdapter(mLogs);

        messageHandler = new MessageHandler();
        ip = getIPAddress();
        messageHandler.sendMessage("IP: " + ip);

        publisher = new Publisher(getIPAddress(), port);
        publisher.start();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_connect1:
                connect(((EditText)findViewById(R.id.ip1)).getText().toString());
                findViewById(R.id.action_connect1).setEnabled(false);
                break;

            case R.id.action_connect2:
                connect(((EditText)findViewById(R.id.ip2)).getText().toString());
                findViewById(R.id.action_connect2).setEnabled(false);
                break;

            case R.id.action_send:
                send(((EditText) findViewById(R.id.text)).getText().toString());
                break;

            default:
                break;
        }
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            mLogs.log(msg.obj.toString());
        }

        public void sendMessage(String message) {
            Message msg = obtainMessage();
            msg.obj = message;
            sendMessage(msg);
        }
    }

    public void connect(String ip) {
        Subscriber subscriber = new Subscriber(ip, port);
        subscriber.setMessageHandler(messageHandler);
        subscriber.start();
        subscriber.subscribe(ip);
    }

    public void send(String msg) {
        publisher.sendMessage(ip, msg);
    }

    public static String getIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (isIPv4)
                            return sAddr;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }
}
