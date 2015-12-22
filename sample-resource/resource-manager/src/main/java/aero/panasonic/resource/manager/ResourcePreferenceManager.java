package aero.panasonic.resource.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by shungao on 12/21/15.
 */
public class ResourcePreferenceManager {

    private SharedPreferences sharedPreferences;

    public ResourcePreferenceManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putLayout(String packageName, int layout, int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("layout_" + layout, packageName + ":" + id);
        editor.commit();
    }

    public String getLayout(int layout) {
        return sharedPreferences.getString("layout_" + layout, "");
    }

    public void putDrawable(String packageName, int drawable, int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("drawable_" + drawable, packageName + ":" + id);
        editor.commit();
    }

    public String getDrawable(int drawable) {
        return sharedPreferences.getString("drawable_" + drawable, "");
    }

    public void putResourceId(String packageName, int resourceId, int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id_" + resourceId, packageName + ":" + id);
        editor.commit();
    }

    public String getResourceId(int resourceId) {
        return sharedPreferences.getString("id_" + resourceId, "");
    }
}
