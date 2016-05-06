package shun.gao.plugin.sdk;

import android.content.Context;

/**
 * Created by Theodore on 2016/5/5.
 */
public class StringHelper {

    private Context context;

    public StringHelper(Context context) {
        this.context = context;
    }

    public String getStringById(int id) {
        return context.getString(id);
    }
}
