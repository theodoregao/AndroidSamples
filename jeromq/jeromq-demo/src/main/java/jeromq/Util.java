package jeromq;

/**
 * Created by shun on 8/17/16.
 */

public class Util {
    public static String formUrl(String ip, int port) {
        return "tcp://" + ip + ":" + port;
    }
}
