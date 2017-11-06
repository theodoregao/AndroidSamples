package sg.shun.gao.test.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

public class ApiService extends Service {

    private Handler handler;

    public ApiService() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IBinder binder = new Api.Stub() {

        @Override
        public void sendReceiveData(String data) throws RemoteException {
            handler.sendMessage(new Message());
//            return "";
        }
    };
}
