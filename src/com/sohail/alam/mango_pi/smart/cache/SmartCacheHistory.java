package com.sohail.alam.mango_pi.smart.cache;

import java.util.concurrent.ConcurrentMap;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 6/7/13
 * Time: 9:22 PM
 */
public interface SmartCacheHistory<K, V extends SmartCachePojo> {

    /**
     * Add to history.
     *
     * @param reason the reason
     * @param key    the key
     * @param value  the value
     */
    public void addToHistory(String reason, K key, V value);

    /**
     * Add all to history.
     *
     * @param reason  the reason
     * @param dataMap the data map
     */
    public void addAllToHistory(String reason, ConcurrentMap<K, V> dataMap);

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
     * SmartCacheHistory_(current-date/time).txt
     *
     * @param filePath the absolute file path for the dump file.
     */
    public String purgeSmartCacheHistory(String filePath) throws Exception;
}
