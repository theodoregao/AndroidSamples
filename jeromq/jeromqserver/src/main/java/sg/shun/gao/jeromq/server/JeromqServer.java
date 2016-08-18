package sg.shun.gao.jeromq.server;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import jeromq.Channel;
import jeromq.LogAdapter;
import jeromq.LogItem;
import jeromq.PubSubProxy;
import jeromq.Publisher;
import jeromq.Subscriber;

public class JeromqServer extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = JeromqServer.class.getSimpleName();
    private static final String proxyIp = "172.17.128.20";//"localhost";//
    private Publisher publisher;
    private Subscriber subscriber;

    private LogAdapter mLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeromq_server);

        mLogs = new LogAdapter(this, R.layout.item_log, new ArrayList<LogItem>());
        ((ListView) findViewById(R.id.log)).setAdapter(mLogs);

        ((CheckBox) findViewById(R.id.channel_state)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.channel_application)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.channel_command)).setOnCheckedChangeListener(this);

        subscriber = new Subscriber(proxyIp, 6001);
        subscriber.setMessageHandler(new MessageHandler());
        subscriber.start();

        publisher = new Publisher(proxyIp, 6000);
        publisher.start();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_send:
                send();
                break;

            case R.id.action_proxy:
                proxy();
                break;

            default:
                break;
        }
    }

    private void send() {
        Spinner spinnerChannel = (Spinner) findViewById(R.id.channels);
        Channel channel = Channel.getChannelByName(getResources().getStringArray(R.array.channels)[spinnerChannel.getSelectedItemPosition()]);
        publisher.sendMessage(channel, ((EditText) findViewById(R.id.message)).getText().toString());
    }

    private void proxy() {
        new PubSubProxy(proxyIp, 6001, 6000).start();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Channel channel = Channel.STATE;
        switch (compoundButton.getId()) {
            case R.id.channel_state:
                channel = Channel.STATE;
                break;

            case R.id.channel_application:
                channel = Channel.APPLICATION;
                break;

            case R.id.channel_command:
                channel = Channel.COMMAND;
                break;

            default:
                break;
        }

        if (b) subscriber.subscribe(channel);
        else subscriber.unsubscribe(channel);
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            mLogs.log(msg.obj.toString());
        }
    }

//    private void log(int logLevel, String message) {
//        Message msg = mHandler.obtainMessage();
//        msg.arg1 = logLevel;
//        msg.obj = message;
//        mHandler.sendMessage(msg);
//    }
}
