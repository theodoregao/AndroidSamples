package jeromq;

import android.util.Log;

import org.zeromq.ZMQ;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Publisher extends Thread implements Runnable {

	private static final String TAG = Publisher.class.getSimpleName();

	private boolean mRunning;
	private String mPublisherIp;
	private int mPort;
	private BlockingQueue<JeroMessage> mOutGoingMessages;

	public Publisher(String mPublisherIp, int port) {
		this.mPublisherIp = mPublisherIp;
		this.mPort = port;
		mOutGoingMessages = new ArrayBlockingQueue<>(1024);
	}

	public void publishMessage(Channel channel, String message) {
		mOutGoingMessages.add(new JeroMessage(channel, message));
	}

	public void terminate() {
		mRunning = false;
	}

	@Override
	public void run() {
		super.run();

		ZMQ.Context ctx = ZMQ.context(1);
		ZMQ.Socket publisher = ctx.socket(ZMQ.PUB);

		publisher.bind(Util.formUrl(mPublisherIp, mPort));

//		final ZMQ.Socket monitor = ctx.socket(ZMQ.PAIR);
//		monitor.subscribe("inproc://monitor.socket");
//		new Thread() {
//			@Override
//			public void run() {
//				while (true) {
//					ZMQ.Event event = ZMQ.Event.recv(monitor);
//					Log.v(TAG, "receive event: " + event.getEvent());
//				}
//			}
//		}.start();

		mRunning = true;

		while (mRunning) {
			try {
				JeroMessage message = mOutGoingMessages.take();
//				Log.v(TAG, "sendMore() " + message.getChannel().getChannelName());
				publisher.sendMore(message.getChannel().getChannelName());
//				Log.v(TAG, "send() " + message.getMessage());
				publisher.send(message.getMessage().getBytes());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		mOutGoingMessages.clear();
		publisher.close();
		ctx.term();
	}

}