package jeromq;

import org.zeromq.ZMQ;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Publisher extends Thread implements Runnable {

	private static final String TAG = Publisher.class.getSimpleName();

	private String mPublisherIp;
	private int mPort;
	private volatile boolean mRunning;
	private BlockingQueue<JeroMessage> mOutGoingMessages;
	private ZMQ.Context mContext;
	private ZMQ.Socket mPublisher;

	public Publisher(String mPublisherIp, int port) {
		this.mPublisherIp = mPublisherIp;
		this.mPort = port;
		mOutGoingMessages = new ArrayBlockingQueue<>(1024);

	}

	public void startPublisher() {
		mContext = ZMQ.context(1);
		mPublisher = mContext.socket(ZMQ.PUB);
		mPublisher.bind(Util.formUrl(mPublisherIp, mPort));
	}

	public void stopPublisher() {
		mRunning = false;
		interrupt();
		mOutGoingMessages.clear();
		mPublisher.disconnect(Util.formUrl(mPublisherIp, mPort));
		mPublisher.unbind(Util.formUrl(mPublisherIp, mPort));
		mPublisher.close();
		mPublisher = null;
		mContext.term();
		mPublisher = null;
	}

	public void publishMessage(Channel channel, String message) {
		mOutGoingMessages.add(new JeroMessage(channel, message));
	}

	@Override
	public void run() {
		super.run();

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
				if (message != null) {
//				Log.v(TAG, "sendMore() " + message.getChannel().getChannelName());
					mPublisher.sendMore(message.getChannel().getChannelName());
//				Log.v(TAG, "send() " + message.getMessage());
					mPublisher.send(message.getMessage().getBytes());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}