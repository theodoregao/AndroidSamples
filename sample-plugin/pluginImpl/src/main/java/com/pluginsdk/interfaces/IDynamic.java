package com.pluginsdk.interfaces;

import android.content.Context;

public interface IDynamic {
    void methodWithCallBack(YKCallBack paramYKCallBack);

    void showPluginWindow(Context paramContext);

    void startPluginActivity(Context context, Class<?> cls);

    String getStringForResId(Context context);
}