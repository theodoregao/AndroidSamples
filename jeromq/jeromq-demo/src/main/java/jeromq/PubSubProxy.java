package jeromq;

import org.zeromq.ZMQ;

public class PubSubProxy extends Thread {

	private static final String TAG = PubSubProxy.class.getSimpleName();

	private String ip;
	private int portXPub;		// 6001
	private int portXSub;		// 6000

	public PubSubProxy(String ip, int portXPub, int portXSub) {
		this.ip = ip;
		this.portXPub = portXPub;
		this.portXSub = portXSub;
	}

	@Override
	public void run() {
		ZMQ.Context ctx = ZMQ.context(1);

		ZMQ.Socket publisherX = ctx.socket(ZMQ.XPUB);
		publisherX.bind(Util.formUrl(ip, portXPub));
		ZMQ.Socket subscriberX = ctx.socket(ZMQ.XSUB);
		subscriberX.bind(Util.formUrl(ip, portXSub));

		ZMQ.proxy(publisherX, subscriberX, null);

		publisherX.close();
		subscriberX.close();
		ctx.term();
	}

}
