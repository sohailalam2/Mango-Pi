package com.sohail.alam.mango_pi.smart.cache.mbeans;

import com.sohail.alam.mango_pi.smart.cache.SmartCachePojo;

import java.util.Set;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 7/7/13
 * Time: 10:04 AM
 */
public interface DefaultSmartCacheManagerMBean<K, V extends SmartCachePojo>
        extends AbstractSmartCacheManagerMBean<K, V> {


    /**
     * Get the total size of the Cache in Bytes
     *
     * @return Number of Bytes
     */
    public long totalCacheSize();

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     *
     * @return Smart Cache Info
     */
    public String smartCacheFullInfo();

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     * corresponding to the given KEY.
     *
     * @param key the KEY
     *
     * @return Smart Cache Info
     */
    public String smartCacheKeyInfo(K key);

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     * corresponding to the given set of Keys
     *
     * @param keys the keys
     *
     * @return Smart Cache Info
     */
    public String smartCacheInfo(Set<K> keys);

    /**
     * View history.
     *
     * @param key the key
     *
     * @return the string
     */
    public String smartCacheKeyHistory(K key);

    /**
     * View history.
     *
     * @param reason the reason
     *
     * @return the string
     */
    public String smartCacheReasonHistory(String reason);

    /**
     * View history.
     *
     * @return the string
     */
    public String smartCacheAllHistory();

    /**
     * Set the maximum number of entries after which the History is
     * deleted permanently.
     *
     * @param maxElementCount the max element count
     */
    public void maxHistoryCount(int maxElementCount);

    /**
     * Purges the contents of History into a user defined file.
     * By default the SmartCache will dump the data into a file named -
     * Smart_Cache_History_(current-date/time).txt
     *
     * @param filePath the absolute file path for the dump file.
     */
    public void purgeSmartCacheHistory(String filePath) throws Exception;

}
