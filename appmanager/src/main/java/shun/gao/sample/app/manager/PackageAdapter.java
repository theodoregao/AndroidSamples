package shun.gao.sample.app.manager;

import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shun on 5/9/16.
 */
public class PackageAdapter extends RecyclerView.Adapter<PackageHolder> {

    private List<PackageInfo> mPackageInfos;
    private List<ActivityManager.RunningAppProcessInfo> mRunningAppProcessInfos;
    private ActivityManager mActivityManager;

    public PackageAdapter(PackageManager packageManager, ActivityManager activityManager) {
//        mPackageInfos = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        mRunningAppProcessInfos = new ArrayList<>();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo: activityManager.getRunningAppProcesses()) {
            if (runningAppProcessInfo.pid != 0) mRunningAppProcessInfos.add(runningAppProcessInfo);
        }
        mActivityManager = activityManager;
    }

    public void reset() {
        mRunningAppProcessInfos = new ArrayList<>();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo: mActivityManager.getRunningAppProcesses()) {
            if (runningAppProcessInfo.pid != 0) mRunningAppProcessInfos.add(runningAppProcessInfo);
        }
        notifyDataSetChanged();
    }

    @Override
    public PackageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PackageHolder(View.inflate(parent.getContext(), R.layout.item_package, null));
    }

    @Override
    public void onBindViewHolder(PackageHolder holder, int position) {
        holder.render(mActivityManager, mRunningAppProcessInfos.get(position));
    }

    @Override
    public int getItemCount() {
        return mRunningAppProcessInfos.size();
    }
}
