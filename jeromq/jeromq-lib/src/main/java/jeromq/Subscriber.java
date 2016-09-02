package jeromq;

import org.zeromq.ZMQ;

import java.util.HashSet;
import java.util.Set;

public class Subscriber extends Thread implements Runnable {

	private boolean mRunning;
	private String mPublisherIp;
	private int mPort;
	private ZMQ.Socket mSubscriber;
	private Set<Channel> mSubscribedChannels;
	private OnJeroMessageReceiveListener mOnJeroMessageReceiveListener;

	public Subscriber(String mPublisherIp, int port) {
		this.mPublisherIp = mPublisherIp;
		this.mPort = port;
		mSubscribedChannels = new HashSet<>();
	}

	public void setOnJeroMessageReceiveListener(OnJeroMessageReceiveListener onJeroMessageReceiveListener) {
		mOnJeroMessageReceiveListener = onJeroMessageReceiveListener;
	}

	public void subscribe(final Channel channel) {
		if (mSubscriber != null) mSubscriber.subscribe(channel.getChannelName().getBytes());
		else mSubscribedChannels.add(channel);
	}

	public void unsubscribe(final Channel channel) {
		if (mSubscribedChannels.contains(channel)) {
			if (mSubscriber != null) mSubscriber.unsubscribe(channel.getChannelName().getBytes());
			else mSubscribedChannels.remove(channel);
		}
	}

	@Override
	public void run() {

		super.run();

		ZMQ.Context ctx = ZMQ.context(1);
		mSubscriber = ctx.socket(ZMQ.SUB);

//		mSubscriber.monitor("inproc://monitor.socket", ZMQ.EVENT_ALL);
//		final ZMQ.Socket monitor = ctx.socket(ZMQ.PAIR);
//		monitor.subscribe("inproc://monitor.socket");
//		new Thread() {
//			@Override
//			public void run() {
//				while (true) {
//					ZMQ.Event event = ZMQ.Event.recv(monitor);
//					Message msg = handler.obtainMessage();
//					msg.obj = "receive event: " + event.getEvent();
//					if (handler != null) handler.sendMessage(msg);
//				}
//			}
//		}.start();

		mSubscriber.connect(Util.formUrl(mPublisherIp, mPort));

		if (!mSubscribedChannels.isEmpty()) {
			for (Channel channel : mSubscribedChannels) subscribe(channel);
		}

		mRunning = true;
		while (mRunning) {
			Channel channel = new Channel(mSubscriber.recvStr());
			while (mSubscriber.hasReceiveMore()) {
				String content = mSubscriber.recvStr();
				if (mOnJeroMessageReceiveListener != null) mOnJeroMessageReceiveListener.onJeroMessageReceived(new JeroMessage(channel, content));
			}
		}

		mSubscriber.close();
		ctx.term();
	}

	public void terminate() {
		mRunning = false;
	}

	public interface OnJeroMessageReceiveListener {
		void onJeroMessageReceived(JeroMessage jeroMessage);
	}

}