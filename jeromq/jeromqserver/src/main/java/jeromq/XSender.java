package jeromq;

import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class XSender extends Thread implements Runnable {

	private static final String TAG = null;
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
		while (true) {
			// Read envelope with address
			System.out.println("jeromq.XSender loop started..");

			String msg = new String(subscriberX.recv(0));
			System.out.println("jeromq.XSender Received: " + "MSG :" + msg);
			publisherX.send(msg.getBytes(), 0);

		}

	}

}