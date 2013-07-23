package com.sohail.alam.mango_pi.smart.cache.mbeans;

import com.sohail.alam.mango_pi.jmx.wrapper.JMXBean;
import com.sohail.alam.mango_pi.jmx.wrapper.JMXBeanOperation;
import com.sohail.alam.mango_pi.jmx.wrapper.JMXBeanParameter;
import com.sohail.alam.mango_pi.smart.cache.DefaultSmartCache;
import com.sohail.alam.mango_pi.smart.cache.SmartCachePojo;

import java.util.Set;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/7/13
 * Time: 4:09 PM
 */
@JMXBean(description = "Smart Cache MBean")
public class DefaultSmartCacheManager<T extends DefaultSmartCache, K, V extends SmartCachePojo>
        extends AbstractSmartCacheManager<T, K, V>
        implements DefaultSmartCacheManagerMBean<K, V> {

    private DefaultSmartCache cache;

    public DefaultSmartCacheManager(T cache) {
        super(cache);
        this.cache = cache;
    }

    /**
     * Get the total size of the Cache in Bytes
     *
     * @return Number of Bytes
     */
    @Override
    @JMXBeanOperation(name = "totalCacheSize",
            description = "Get the total size of the Cache in Bytes")
    public long totalCacheSize() {
        return cache.totalCacheSize();
    }

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     *
     * @return Smart Cache Info
     */
    @Override
    @JMXBeanOperation(name = "smartCacheFullInfo",
            description = "Displays the Entire Smart Cache Information")
    public String smartCacheFullInfo() {
        return this.cache.smartCacheFullInfo();
    }

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     * corresponding to the given KEY.
     *
     * @param key the KEY
     *
     * @return Smart Cache Info
     */
    @Override
    @JMXBeanOperation(name = "smartCacheKeyInfo",
            description = "Displays the Smart Cache Information for the entry corresponding to the KEY")
    public String smartCacheKeyInfo(@JMXBeanParameter(name = "The Key",
            description = "The Key for the Cache element") K key) {
        return this.cache.smartCacheKeyInfo(key);
    }

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     * corresponding to the given set of Keys
     *
     * @param keys the keys
     *
     * @return Smart Cache Info
     */
    @Override
    @JMXBeanOperation(name = "smartCacheInfo",
            description = "Displays the Smart Cache Information for the entries corresponding to the KEY SET")
    public String smartCacheInfo(@JMXBeanParameter(name = "The Set of Keys",
            description = "The set of Keys for the Cache elements") Set<K> keys) {
        return this.cache.smartCacheInfo(keys);
    }

    /**
     * View history.
     *
     * @param key the key
     *
     * @return the string
     */
    @Override
    @JMXBeanOperation(name = "smartCacheKeyHistory",
            description = "Displays the Smart Cache History Information for the corresponding KEY")
    public String smartCacheKeyHistory(@JMXBeanParameter(name = "The Key",
            description = "The Key for the History element") K key) {
        return this.cache.smartCacheKeyHistory(key);
    }

    /**
     * View history.
     *
     * @param reason the reason
     *
     * @return the string
     */
    @Override
    @JMXBeanOperation(name = "smartCacheReasonHistory",
            description = "Displays the Smart Cache History Information for the corresponding  REASON")
    public String smartCacheReasonHistory(@JMXBeanParameter(name = "The Reason",
            description = "The Reason for the History element") String reason) {
        return this.cache.smartCacheReasonHistory(reason);
    }

    /**
     * View history.
     *
     * @return the string
     */
    @Override
    @JMXBeanOperation(name = "smartCacheAllHistory",
            description = "Displays the Entire Smart Cache History Information")
    public String smartCacheAllHistory() {
        return this.cache.smartCacheAllHistory();
    }

    /**
     * Set the maximum number of entries after which the History is
     * deleted permanently.
     *
     * @param maxHistoryCount the max element count
     */
    @Override
    @JMXBeanOperation(name = "maxHistoryCount",
            description = "Set the maximum number of History elements to keep in memory before purging the History")
    public void maxHistoryCount(@JMXBeanParameter(name = "The Max History Count",
            description = "The Maximum number of History Elements to keep in memory") int maxHistoryCount) {
        this.cache.maxHistoryCount(maxHistoryCount);
    }

    /**
     * Purges the contents of History into a user defined file.
     * By default the SmartCache will dump the data into a file named -
     * Smart_Cache_History_(current-date/time).txt
     *
     * @param filePath the absolute file path for the dump file.
     */
    @Override
    @JMXBeanOperation(name = "purgeSmartCacheHistory",
            description = "Purges the Smart Cache History into a file.")
    public void purgeSmartCacheHistory(@JMXBeanParameter(name = "The File Path",
            description = "The fully qualified file path, or null if you want to use the default") String filePath) throws Exception {
        this.cache.purgeSmartCacheHistory(filePath);
    }
}
