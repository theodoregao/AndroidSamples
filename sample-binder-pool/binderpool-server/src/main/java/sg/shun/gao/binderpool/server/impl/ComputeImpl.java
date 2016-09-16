package sg.shun.gao.binderpool.server.impl;

import android.os.RemoteException;

import sg.shun.gao.aidl.ICompute;

/**
 * Created by shun on 9/15/16.
 */

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
