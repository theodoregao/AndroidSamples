package jeromq;

import android.util.Log;

import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class XSender extends Thread implements Runnable {
	private static final String TAG = XSender.class.getSimpleName();

	private Socket publisherX;
	private Context ctx;
	private Socket subscriberX;

	public XSender(Context ctx, Socket subscriberX, Socket publisherX) {
		this.ctx = ctx;
		this.subscriberX = subscriberX;
		this.publisherX = publisherX;
	}

	@Override
	public void run() {
		super.run();
		Log.v(TAG, "run()");
		while (true) {
			// Read envelope with address
			String msg = new String(subscriberX.recv(0));
			Log.v(TAG, msg);
			Log.v(TAG, "" + Channel.isChannel(msg));
			if (Channel.isChannel(msg)) publisherX.sendMore(msg.getBytes());
			else publisherX.send(msg.getBytes());
		}

	}

}