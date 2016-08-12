package sg.shun.gao.jeromq.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.zeromq.ZMQ;

public class JeromqClient extends AppCompatActivity {

    private static final String TAG = JeromqClient.class.getSimpleName();
    private boolean isSubscribeA;
    private boolean isSubscribeB;
    private Button buttonSubscribeA;
    private Button buttonSubscribeB;
    private ZMQ.Socket subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeromq_client);

        buttonSubscribeA = (Button) findViewById(R.id.action_subscribe_a);
        buttonSubscribeB = (Button) findViewById(R.id.action_subscribe_b);

        final TextView textViewA = (TextView) findViewById(R.id.text_a);
        final TextView textViewB = (TextView) findViewById(R.id.text_b);

        new Thread() {
            @Override
            public void run() {
                ZMQ.Context context = ZMQ.context(1);
                subscriber = context.socket(ZMQ.SUB);

                subscriber.connect("tcp://localhost:19861");
                while (!Thread.currentThread ().isInterrupted ()) {
                    // Read envelope with address
                    final String address = subscriber.recvStr ();
                    // Read message contents
                    final String contents = subscriber.recvStr ();
                    System.out.println(address + " : " + contents);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (address) {
                                case "A":
                                    textViewA.setText(contents);
                                    break;

                                case "B":
                                    textViewB.setText(contents);
                                    break;

                                default:
                                    break;
                            }
                        }
                    });
                }
                subscriber.close ();
                context.term ();
            }
        }.start();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_subscribe_a:
                toggleSubscribeA();
                break;

            case R.id.action_subscribe_b:
                toggleSubscribeB();
                break;

            default:
                break;
        }
    }

    private void toggleSubscribeA() {
        isSubscribeA = !isSubscribeA;
        buttonSubscribeA.setText(getString(isSubscribeA ? R.string.unsubscribe_channel_a: R.string.subscribe_channel_a));
        new Thread() {
            @Override
            public void run() {
                if (isSubscribeA) subscriber.subscribe("A".getBytes(ZMQ.CHARSET));
                else subscriber.unsubscribe("A".getBytes(ZMQ.CHARSET));
            }
        }.start();
    }

    private void toggleSubscribeB() {
        isSubscribeB = !isSubscribeB;
        buttonSubscribeB.setText(getString(isSubscribeB ? R.string.unsubscribe_channel_b: R.string.subscribe_channel_b));
        new Thread() {
            @Override
            public void run() {
                if (isSubscribeA) subscriber.subscribe("B".getBytes(ZMQ.CHARSET));
                else subscriber.unsubscribe("B".getBytes(ZMQ.CHARSET));
            }
        }.start();
    }
}
