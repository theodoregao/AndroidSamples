package sg.shun.gao.lib;

import sg.shun.gao.lib.IEventListener;

interface IResourceManager {
    oneway void registerLayoutResource(int layoutResource, String packageName, int id);
    oneway void registerIdResource(int resourceId, String packageName, int id);
    oneway void registerDrawableResource(int drawableResource, String packageName, int id);
    oneway void registerStringResource(int stringResource, String packageName, int id);
    oneway void registerAnimationResource(int animationResource, String packageName, int id);

    oneway void setOnEventListener(IEventListener eventListener);
}
