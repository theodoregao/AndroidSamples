package sg.shun.gao.binderpool.server.impl;

import android.os.RemoteException;

import sg.shun.gao.aidl.ISecurityCenter;

/**
 * Created by shun on 9/15/16.
 */

public class SecurityCenterImpl extends ISecurityCenter.Stub {
    @Override
    public String encrypt(String content) throws RemoteException {
        return "--" + content + "--";
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return password.replaceAll("--", "");
    }
}
