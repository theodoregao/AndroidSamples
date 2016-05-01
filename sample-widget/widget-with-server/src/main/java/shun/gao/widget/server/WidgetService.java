package shun.gao.widget.server;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new shun.gao.widget.server.RemoteViewsFactory(this.getApplicationContext(), intent);
    }
}