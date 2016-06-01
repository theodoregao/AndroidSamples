// ISimpleAnalytics.aidl
package shun.gao.sample.usage.stats;

// Declare any non-default types here with import statements

interface ISimpleAnalytics {
    oneway void timestamp(String key);

    int getTimeCount(String key);
}
