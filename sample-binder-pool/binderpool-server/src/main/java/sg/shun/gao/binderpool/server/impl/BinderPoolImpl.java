package sg.shun.gao.binderpool.server.impl;

import android.os.IBinder;
import android.os.RemoteException;

import sg.shun.gao.aidl.IBinderPool;

/**
 * Created by shun on 9/15/16.
 */

public class BinderPoolImpl extends IBinderPool.Stub {

    private static final int BINDER_SECURITY_CENTER = 0;
    private static final int BINDER_COMPUTE = 2;

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        IBinder binder = null;
        switch (binderCode) {
            case BINDER_SECURITY_CENTER:
                binder = new SecurityCenterImpl();
                break;

            case BINDER_COMPUTE:
                binder = new ComputeImpl();
                break;

            default:
                break;
        }

        return binder;
    }
}
