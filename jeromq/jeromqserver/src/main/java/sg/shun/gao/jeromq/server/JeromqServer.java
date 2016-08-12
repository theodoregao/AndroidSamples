package sg.shun.gao.jeromq.server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.zeromq.ZMQ;

import java.util.Date;

public class JeromqServer extends AppCompatActivity {

    private static final String TAG = JeromqServer.class.getSimpleName();
    private Button buttonA;
    private Button buttonB;
    private boolean isPublishA;
    private boolean isPublishB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeromq_server);

        buttonA = (Button) findViewById(R.id.action_publish_a);
        buttonB = (Button) findViewById(R.id.action_publish_b);

        new Thread() {
            @Override
            public void run() {
                ZMQ.Context context = ZMQ.context(1);
                ZMQ.Socket publisher = context.socket(ZMQ.PUB);
                publisher.bind("tcp://*:19861");
                while (!Thread.currentThread().isInterrupted()) {
                    Date date = new Date();
                    if (isPublishA) {
                        publisher.sendMore("A");
                        publisher.send("message from channel A: " + date.toString());
                    }
                    if (isPublishB) {
                        publisher.sendMore("B");
                        publisher.send("message from channel B: " + date.toString());
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                publisher.close();
                context.term();
            }
        }.start();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_publish_a:
                toggleChannelA();
                break;

            case R.id.action_publish_b:
                toggleChannelB();
                break;

            default:
                break;
        }
    }

    private void toggleChannelA() {
        isPublishA = !isPublishA;
        buttonA.setText(getString(!isPublishA ? R.string.publish_a : R.string.stop_publish_a));
    }

    private void toggleChannelB() {
        isPublishB = !isPublishB;
        buttonB.setText(getString(!isPublishB ? R.string.publish_b : R.string.stop_publish_b));
    }
}
