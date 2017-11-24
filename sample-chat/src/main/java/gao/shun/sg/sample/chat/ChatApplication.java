package gao.shun.sg.sample.chat;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by Theodore on 2017/11/24.
 */

public class ChatApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
