package gao.shun.sg.popserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PopupServer extends Service {

    private static final String ACTION_PA_ON = "sg.shun.gao.action.PA_ON";
    private static final String ACTION_PA_OFF = "sg.shun.gao.action.PA_OFF";
    private static final String ACTION_PSS_ON = "sg.shun.gao.action.PSS_ON";
    private static final String ACTION_PSS_OFF = "sg.shun.gao.action.PSS_OFF";

    PopupManager popupManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (popupManager == null) popupManager = new PopupManager(this);

        String action = intent.getAction();

        switch (action) {
            case ACTION_PA_ON:
                popupManager.paOn();
                break;

            case ACTION_PA_OFF:
                popupManager.paOff();
                break;

            case ACTION_PSS_ON:
                popupManager.pssOn();
                break;

            case ACTION_PSS_OFF:
                popupManager.pssOff();
                break;

            default:
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
