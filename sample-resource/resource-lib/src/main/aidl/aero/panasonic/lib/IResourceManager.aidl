// IResourceManager.aidl
package aero.panasonic.lib;

// Declare any non-default types here with import statements

interface IResourceManager {
//    oneway void registerResource(int resource, String packageName, int id);
    oneway void registerLayoutResource(int layoutResource, String packageName, int id);
    oneway void registerIdResource(int resourceId, String packageName, int id);
    oneway void registerDrawableResource(int drawableResource, String packageName, int id);
}
