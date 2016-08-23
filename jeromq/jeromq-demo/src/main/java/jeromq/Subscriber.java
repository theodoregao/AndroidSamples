package jeromq;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.zeromq.ZMQ;

public class Subscriber extends Thread implements Runnable {
	private static final String TAG = Subscriber.class.getSimpleName();

	private String proxyIp;
	private int port;	// 6001
	private ZMQ.Socket mulServiceSubscriber;
	private Handler handler;
	private String topic;

	public Subscriber(String proxyIp, int port) {
		this.proxyIp = proxyIp;
		this.port = port;
	}

	public void setMessageHandler(Handler handler) {
		this.handler = handler;
	}

	public void subscribe(final String topic) {
		new Thread() {
			@Override
			public void run() {
				if (mulServiceSubscriber != null) mulServiceSubscriber.subscribe(topic.getBytes());
				else Subscriber.this.topic = topic;
			}
		}.start();
	}

	public void subscribe(final Channel channel) {
		new Thread() {
			@Override
			public void run() {
				mulServiceSubscriber.subscribe(channel.toString().getBytes());
			}
		}.start();
	}

	public void unsubscribe(final Channel channel) {
		new Thread() {
			@Override
			public void run() {
				mulServiceSubscriber.unsubscribe(channel.toString().getBytes());
			}
		}.start();
	}

	@Override
	public void run() {

		super.run();

		ZMQ.Context ctx = ZMQ.context(1);
		mulServiceSubscriber = ctx.socket(ZMQ.SUB);
		mulServiceSubscriber.monitor("inproc://monitor.socket", ZMQ.EVENT_ALL);

		final ZMQ.Socket monitor = ctx.socket(ZMQ.PAIR);
		monitor.connect("inproc://monitor.socket");
		new Thread() {
			@Override
			public void run() {
				while (true) {
					ZMQ.Event event = ZMQ.Event.recv(monitor);
					Message msg = handler.obtainMessage();
					msg.obj = "receive event: " + event.getEvent();
					if (handler != null) handler.sendMessage(msg);
				}
			}
		}.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		mulServiceSubscriber.connect(Util.formUrl(proxyIp, port));

		if (!TextUtils.isEmpty(topic)) subscribe(topic);

		Log.v(TAG, "jeromq.Subscriber loop started..");

		while (true) {
			String channel = mulServiceSubscriber.recvStr();
			Log.v(TAG, "channel: " + channel);
			String content = mulServiceSubscriber.recvStr();
			Log.v(TAG, "content: " + content);
			if (handler != null) {
				Message msg = handler.obtainMessage();
				msg.obj = new JeroMessage(channel, content);
				handler.sendMessage(msg);
			}
		}
	}

}