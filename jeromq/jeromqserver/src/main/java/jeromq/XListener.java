package jeromq;

import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class XListener extends Thread implements Runnable {

	private static final String TAG = null;
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
			System.out.println("jeromq.XListener loop started..");

			String msg = new String(subscriberX.recvStr());
			System.out.println("Listener Received: " + "MSG :" + msg);
			publisherX.send(msg.getBytes(), 0);
		}
	}

}