package jeromq;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;

public class Subscriber extends Thread implements Runnable {

	private static final String TAG = "jeromq.Subscriber";
	private Context ctx;

	public Subscriber(Context z_context) {
		this.ctx = z_context;
	}

	@Override
	public void run() {

		super.run();

		ZMQ.Socket mulServiceSubscriber = ctx.socket(ZMQ.SUB);
		mulServiceSubscriber.connect("tcp://localhost:6001");
//		mulServiceSubscriber.subscribe("A".getBytes());
//		mulServiceSubscriber.subscribe("B".getBytes());
		mulServiceSubscriber.subscribe("C".getBytes());

		System.out.println("jeromq.Subscriber loop started..");

		while (true) {
			String content = new String(mulServiceSubscriber.recv(0));
			System.out.println("jeromq.Subscriber Received : " + content);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Subscriber subscriber = new Subscriber(ZMQ.context(1));
		subscriber.start();
		subscriber.join();
	}

}