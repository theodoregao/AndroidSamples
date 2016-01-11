package sg.shun.gao.resource.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.lang.ref.WeakReference;

/**
 * Created by Theodore on 2016/1/10.
 */
public class ResourceController {
    private static final String TAG = ResourceController.class.getSimpleName();

    private WeakReference<Context> mContext;
    private ResourcePreferenceManager mResourcePreferenceManager;

    public ResourceController(Context context) {
        this.mContext = new WeakReference<Context>(context);
        mResourcePreferenceManager = new ResourcePreferenceManager(context);
    }

    public void putResourceLayout(String packageName, int resourceLayout, int id) {
        mResourcePreferenceManager.putLayout(packageName, resourceLayout, id);
    }

    public View getResourceLayout(int resourceLayout) {
        String layoutResource = mResourcePreferenceManager.getLayout(resourceLayout);
        if (layoutResource.length() > 0) {
            String[] args = layoutResource.split(":");
            if (args.length == 2) {
                try {
                    Context context = mContext.get().createPackageContext(args[0], Context.CONTEXT_IGNORE_SECURITY);
                    if (context != null) {
                        View view = View.inflate(context, Integer.parseInt(args[1]), null);
                        if (view != null) return view;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void putResourceDrawable(String packageName, int resourceDrawable, int id) {
        mResourcePreferenceManager.putDrawable(packageName, resourceDrawable, id);
    }

    public Drawable getResourceDrawable(int resourceDrawable) {
        String drawableResource = mResourcePreferenceManager.getDrawable(resourceDrawable);
        if (drawableResource.length() > 0) {
            String[] args = drawableResource.split(":");
            if (args.length == 2) {
                try {
                    Context context = mContext.get().createPackageContext(args[0], Context.CONTEXT_IGNORE_SECURITY);
                    if (context != null) {
                        return context.getDrawable(Integer.parseInt(args[1]));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void putResourceString(String packageName, int resourceString, int id) {
        mResourcePreferenceManager.putString(packageName, resourceString, id);
    }

    public String getResourceString(int resourceString) {
        String stringResource = mResourcePreferenceManager.getString(resourceString);
        if (stringResource.length() > 0) {
            String[] args = stringResource.split(":");
            if (args.length == 2) {
                try {
                    Context context = mContext.get().createPackageContext(args[0], Context.CONTEXT_IGNORE_SECURITY);
                    if (context != null) {
                        return context.getString(Integer.parseInt(args[1]));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void putResourceAnimation(String packageName, int resourceAnimation, int id) {
        mResourcePreferenceManager.putAnimation(packageName, resourceAnimation, id);
    }

    public Animation getResourceAnimation(int animation) {
        String animationResource = mResourcePreferenceManager.getAnimation(animation);
        if (animationResource.length() > 0) {
            String[] args = animationResource.split(":");
            if (args.length == 2) {
                try {
                    Context context = mContext.get().createPackageContext(args[0], Context.CONTEXT_IGNORE_SECURITY);
                    if (context != null) {
                        return AnimationUtils.loadAnimation(context, Integer.parseInt(args[1]));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void putResourceId(String packageName, int resourceId, int id) {
        mResourcePreferenceManager.putResourceId(packageName, resourceId, id);
    }

    public int getResourceId(int resourceId) {
        String idResource = mResourcePreferenceManager.getResourceId(resourceId);
        if (idResource.length() > 0) {
            String[] args = idResource.split(":");
            if (args.length == 2) {
                return Integer.parseInt(args[1]);
            }
        }
        return 0;
    }

}
