package jeromq;

import android.util.Log;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Publisher extends Thread implements Runnable {
	private static final String TAG = Publisher.class.getSimpleName();

	private String proxyIp;
	private int port;	// 6000
	private BlockingQueue<JeroMessage> messages;

	public Publisher(String proxyIp, int port) {
		this.proxyIp = proxyIp;
		this.port = port;
		messages = new ArrayBlockingQueue<>(1024);
	}

	public void sendMessage(Channel channel, String message) {
		messages.add(new JeroMessage(channel, message));
	}

	@Override
	public void run() {
		super.run();

		ZMQ.Context ctx = ZMQ.context(1);
		ZMQ.Socket publisher = ctx.socket(ZMQ.PUB);
		publisher.connect(Util.formUrl(proxyIp, port));
		
		Log.v(TAG, "jeromq.Publisher loop started..");

//		publisher.sendMore(Channel.STATE.name().getBytes());

		while (true) {
			try {
				JeroMessage message = messages.take();
				Log.v(TAG, "sendMore() " + message.getChannel().toString());
				publisher.sendMore(message.getChannel());
				Log.v(TAG, "send() " + message.getMessage());
				publisher.send(message.getMessage().getBytes(), 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}