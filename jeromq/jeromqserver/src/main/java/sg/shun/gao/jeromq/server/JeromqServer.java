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
                    String message = "《漢密爾頓》（Hamilton）是一齣關於美國開國元勳亞歷山大·漢密爾頓的音樂劇，由林-曼猷·米藍達編劇作詞填曲[1]。這齣劇是受到2004年朗·車諾所寫的傳記《亞歷山大·漢密爾頓》所啟發, 在美國受到票房及劇評肯定 這齣劇在2015年2月於外百老匯的公眾劇院首演時完售。在2015年8月登上百老匯的理察·羅傑斯劇院。劇作在百老匯獲得熱烈好評以及空前的票房紀錄，獲得葛蘭美獎最佳音樂劇專輯及普立茲獎戲劇獎。外百老匯的演出贏得2015年戲劇桌獎（或譯：紐約戲劇獎）十四項提名，得到傑出音樂劇等七項獎項。百老匯的演出獲得破紀錄的十六項東尼獎提名。";
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < 1024 * 512; i++) stringBuilder.append("msg ");
                    if (isPublishA) {
                        publisher.sendMore("A");
                        publisher.send("message from channel A: " + message.toString());
                    }
                    if (isPublishB) {
                        publisher.sendMore("B");
                        publisher.send("message from channel B: " + stringBuilder.toString());
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
