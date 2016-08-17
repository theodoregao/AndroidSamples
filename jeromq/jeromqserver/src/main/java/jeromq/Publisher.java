package jeromq;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;

public class Publisher extends Thread implements Runnable {

	private static final String TAG = "jeromq.Publisher";
	private Context ctx;

	public Publisher(Context z_context) {
		this.ctx = z_context;
	}

	@Override
	public void run() {

		super.run();

		ZMQ.Socket publisher = ctx.socket(ZMQ.PUB);
		publisher.connect("tcp://localhost:6000");
		
		System.out.println("jeromq.Publisher loop started..");

		int count = 0;
		publisher.sendMore("B".getBytes());
		while (true) {
			count++;
//			publisher.sendMore("A".getBytes());
//			publisher.send(("A Hello " + count).getBytes(), 0);
//			publisher.sendMore("B".getBytes());
			publisher.send(("B Hello " + count).getBytes(), 0);
//			publisher.sendMore("C".getBytes());
//			publisher.send(("C Hello " + count).getBytes(), 0);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Publisher publisher = new Publisher(ZMQ.context(1));
		publisher.start();
		publisher.join();
	}

}