package test.shun.gao.jnicallback;

/**
 * Created by shun on 5/1/17.
 */

public class Native {
    static native void registerCallback(JavaCallback callback);

    static {
        System.loadLibrary("callback");
    }
}
