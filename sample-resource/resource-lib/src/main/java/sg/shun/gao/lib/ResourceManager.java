package sg.shun.gao.lib;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

/**
 * Created by shungao on 12/21/15.
 */
public class ResourceManager {
    public static void bindResource(Context context, ServiceConnection connection) {
        Intent intent = new Intent();
        intent.setAction("aero.panasonic.action.BIND_RESOURCE");
        intent.setPackage("aero.panasonic.resource.manager");
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public static void unbindResource(Context context, ServiceConnection connection) {
        context.unbindService(connection);
    }
}
