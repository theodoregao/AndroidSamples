/**
 * Dynamic1.java
 * com.youku.pluginsdk.imp
 * <p/>
 * Function�� TODO
 * <p/>
 * ver     date      		author
 * ��������������������������������������������������������������������
 * 2014-10-20 		Administrator
 * <p/>
 * Copyright (c) 2014, TNT All Rights Reserved.
 */

package com.pluginsdk.imp;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.pluginsdk.bean.Bean;
import com.pluginsdk.interfaces.IDynamic;
import com.pluginsdk.interfaces.YKCallBack;
import com.youku.pluginsdk.R;

/**
 * ClassName:Dynamic1
 *
 * @author jiangwei
 * @version
 * @since Ver 1.1
 * @Date 2014-10-20		����5:57:10
 */
public class Dynamic implements IDynamic {
    /**

     */
    public void methodWithCallBack(YKCallBack callback) {
        Bean bean = new Bean();
        bean.setName("PLUGIN_SDK_USER");
        callback.callback(bean);
    }

    public void showPluginWindow(Context context) {
        AlertDialog.Builder builder = new Builder(context);
        builder.setMessage("�Ի���");
        builder.setTitle(R.string.hello_world);
        builder.setNegativeButton("ȡ��", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();//.show();
        dialog.show();
    }

    public void startPluginActivity(Context context, Class<?> cls) {
        /**
         *����Ҫע�⼸��:
         *1�����������дһ��MainActivity�Ļ�������������Ҳ��һ��MainActivity��������Activity�����������е�MainActivity
         *2��������ｫMainActivityд��ȫ���Ļ������������⣬�ᱨ�Ҳ������Activity�Ĵ���
         */
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public String getStringForResId(Context context) {
        return context.getResources().getString(R.string.hello_world);
    }

}

