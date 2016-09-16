package sg.shun.gao.binderpool.server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import sg.shun.gao.binderpool.server.impl.BinderPoolImpl;

public class BinderPoolService extends Service {

    private Binder mBinderPool = new BinderPoolImpl();

    public BinderPoolService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }
}
