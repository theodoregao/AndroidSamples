package jeromq;

import android.util.Log;

import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class XListener extends Thread implements Runnable {
	private static final String TAG = XListener.class.getSimpleName();

	private Socket publisherX;
	private Context ctx;
	private Socket subscriberX;

	public XListener(Context ctx, Socket subscriberX, Socket publisherX) {
		this.ctx = ctx;
		this.subscriberX = subscriberX;
		this.publisherX = publisherX;

	}

	@Override
	public void run() {
		super.run();
		while (true) {
			Log.v(TAG, "jeromq.XListener loop started..");

			String msg = new String(subscriberX.recvStr());
			Log.v(TAG, "Listener Received: " + msg);
			publisherX.send(msg.getBytes(), 0);
		}
	}

}