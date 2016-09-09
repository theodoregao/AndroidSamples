package metadata.test.panasonic.aero.jeromq_lib;

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

import inflight.InFlightMessage;
import inflight.InFlightMessageSource;
import jeromq.Channel;

public class MainActivity extends AppCompatActivity {

    private MessageHandler messageHandler;
    private LogAdapter mLogs;
    private String ip;
    private Channel channel;
    private InFlightMessageSource mInFlightMessageSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogs = new LogAdapter(this, R.layout.item_log, new ArrayList<LogItem>());
        ((ListView) findViewById(R.id.log)).setAdapter(mLogs);

        messageHandler = new MessageHandler();
        ip = getIPAddress();
        channel = new Channel(ip);
        messageHandler.sendMessage("IP: " + ip);
        mInFlightMessageSource = new InFlightMessageSource(ip);
        mInFlightMessageSource.setInFlightSourceListener(new InFlightMessageSource.OnInFlightMessageReceiveListener() {
            @Override
            public void onInFlightMessageReceived(Channel channel, InFlightMessage inFlightMessage) {
                messageHandler.sendMessage(inFlightMessage.toJson());
            }

            @Override
            public void onInFlightSourceError(InFlightMessageSource.InFlightMessageSourceError error) {
                messageHandler.sendMessage(error.name());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInFlightMessageSource != null) mInFlightMessageSource.stop();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_subscribe1:
                subscribe(((EditText)findViewById(R.id.ip1)).getText().toString());
                findViewById(R.id.ip1).setEnabled(false);
                findViewById(R.id.action_subscribe1).setEnabled(false);
                findViewById(R.id.action_unsubscribe1).setEnabled(true);
                break;

            case R.id.action_unsubscribe1:
                unsubscribe(((EditText)findViewById(R.id.ip1)).getText().toString());
                findViewById(R.id.ip1).setEnabled(true);
                findViewById(R.id.action_subscribe1).setEnabled(true);
                findViewById(R.id.action_unsubscribe1).setEnabled(false);
                break;

            case R.id.action_subscribe2:
                subscribe(((EditText)findViewById(R.id.ip2)).getText().toString());
                findViewById(R.id.ip2).setEnabled(false);
                findViewById(R.id.action_subscribe2).setEnabled(false);
                findViewById(R.id.action_unsubscribe2).setEnabled(true);
                break;

            case R.id.action_unsubscribe2:
                unsubscribe(((EditText)findViewById(R.id.ip2)).getText().toString());
                findViewById(R.id.ip2).setEnabled(true);
                findViewById(R.id.action_subscribe2).setEnabled(true);
                findViewById(R.id.action_unsubscribe2).setEnabled(false);
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

    public void subscribe(final String ip) {
        mInFlightMessageSource.connect(ip);
        mInFlightMessageSource.subscribe(new Channel(ip));
    }

    public void unsubscribe(final String ip) {
        mInFlightMessageSource.unSubscribe(new Channel(ip));
    }

    public void send(String msg) {
        InFlightMessage inFlightMessage = new InFlightMessage();
        inFlightMessage.setMethodName(msg);
        mInFlightMessageSource.publishMessage(channel, inFlightMessage);
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
