package sg.shun.gao.resource.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by shungao on 12/21/15.
 */
public class ResourcePreferenceManager {

    private SharedPreferences mSharedPreferences;

    public ResourcePreferenceManager(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear().commit();
    }

    public void putLayout(String packageName, int resourceLayout, int id) {
        mSharedPreferences.edit()
                .putString("layout_" + resourceLayout, packageName + ":" + id)
                .commit();
    }

    public String getLayout(int resourceLayout) {
        return mSharedPreferences.getString("layout_" + resourceLayout, "");
    }

    public void putDrawable(String packageName, int resourceDrawable, int id) {
        mSharedPreferences.edit()
                .putString("drawable_" + resourceDrawable, packageName + ":" + id)
                .commit();
    }

    public String getDrawable(int resourceDrawable) {
        return mSharedPreferences.getString("drawable_" + resourceDrawable, "");
    }

    public void putString(String packageName, int resourceString, int id) {
        mSharedPreferences.edit()
                .putString("string_" + resourceString, packageName + ":" + id)
                .commit();
    }

    public String getString(int resourceString) {
        return mSharedPreferences.getString("string_" + resourceString, "");
    }

    public void putAnimation(String packageName, int resourceAnimation, int id) {
        mSharedPreferences.edit()
                .putString("animation_" + resourceAnimation, packageName + ":" + id)
                .commit();
    }

    public String getAnimation(int resourceAnimation) {
        return mSharedPreferences.getString("animation_" + resourceAnimation, "");
    }

    public void putResourceId(String packageName, int resourceId, int id) {
        mSharedPreferences.edit()
                .putString("id_" + resourceId, packageName + ":" + id)
                .commit();
    }

    public String getResourceId(int resourceId) {
        return mSharedPreferences.getString("id_" + resourceId, "");
    }
}
