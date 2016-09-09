package jeromq;

import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.util.HashSet;
import java.util.Set;

import zmq.ZError;

public class Subscriber extends Thread implements Runnable {

	private volatile boolean mRunning;
	private String mPublisherIp;
	private int mPort;
	private ZMQ.Socket mSubscriber;
	private Set<Channel> mSubscribedChannels;
	private OnJeroMessageReceiveListener mOnJeroMessageReceiveListener;
	private ZMQ.Context mContext;

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

	public void stopSubscriber() {
		mRunning = false;
		mSubscriber.disconnect(Util.formUrl(mPublisherIp, mPort));
		mSubscriber.close();
		mSubscriber = null;
		mContext.term();
		mContext = null;
		mSubscribedChannels.clear();
	}

	@Override
	public void run() {

		super.run();

		mContext = ZMQ.context(1);
		mSubscriber = mContext.socket(ZMQ.SUB);

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

		mSubscriber.setReceiveTimeOut(1000);
		mSubscriber.connect(Util.formUrl(mPublisherIp, mPort));

		if (!mSubscribedChannels.isEmpty()) {
			for (Channel channel : mSubscribedChannels) subscribe(channel);
		}

		mRunning = true;
		while (mRunning) {
			try {
				byte[] bytes = mSubscriber.recv();
				if (bytes != null) {
					Channel channel = new Channel(new String(bytes));
					while (mSubscriber.hasReceiveMore()) {
						String content = mSubscriber.recvStr();
						if (mOnJeroMessageReceiveListener != null)
							mOnJeroMessageReceiveListener.onJeroMessageReceived(new JeroMessage(channel, content));
					}
				}
			} catch (ZError.IOException e) {
				e.printStackTrace();
			} catch (ZMQException e) {
				e.printStackTrace();
			}
		}
	}

	public interface OnJeroMessageReceiveListener {
		void onJeroMessageReceived(JeroMessage jeroMessage);
	}

}