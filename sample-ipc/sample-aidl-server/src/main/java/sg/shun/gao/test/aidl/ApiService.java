package sg.shun.gao.test.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class ApiService extends Service {
    public ApiService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IBinder binder = new Api.Stub() {

        @Override
        public String sendReceiveData(String data) throws RemoteException {
            return data;
        }
    };
}
