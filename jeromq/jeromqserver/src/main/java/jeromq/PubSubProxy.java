package jeromq;

import org.zeromq.ZMQ;

public class PubSubProxy {

	public static void main(String[] args) throws InterruptedException {
		ZMQ.Context ctx = ZMQ.context(1);

		ZMQ.Socket publisherX = ctx.socket(ZMQ.XPUB);
		publisherX.bind("tcp://localhost:6001");
		ZMQ.Socket subscriberX = ctx.socket(ZMQ.XSUB);
		subscriberX.bind("tcp://localhost:6000");

		XSender xSender = new XSender(ctx, subscriberX, publisherX);
		XListener xListener = new XListener(ctx, publisherX, subscriberX);
		xSender.start();
		xListener.start();
		xSender.join();
		xListener.join();
	}

}
