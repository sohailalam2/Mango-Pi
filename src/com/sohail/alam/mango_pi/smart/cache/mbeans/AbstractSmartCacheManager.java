package com.sohail.alam.mango_pi.smart.cache.mbeans;

import com.sohail.alam.mango_pi.jmx.wrapper.JMXBean;
import com.sohail.alam.mango_pi.jmx.wrapper.JMXBeanOperation;
import com.sohail.alam.mango_pi.jmx.wrapper.JMXBeanParameter;
import com.sohail.alam.mango_pi.smart.cache.AbstractSmartCache;
import com.sohail.alam.mango_pi.smart.cache.SmartCache;
import com.sohail.alam.mango_pi.smart.cache.SmartCacheException;
import com.sohail.alam.mango_pi.utils.MBeanService;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/7/13
 * Time: 12:45 PM
 */
@JMXBean(description = "Smart Cache MBean")
public class AbstractSmartCacheManager<T extends AbstractSmartCache, K, V> implements AbstractSmartCacheManagerMBean<K, V> {

    private static String MBEAN_NAME;
    private SmartCache cache;

    public AbstractSmartCacheManager(T cache) {
        if (cache == null)
            throw new NullPointerException("The Instance of the Cache can not be null");

        MBEAN_NAME = "MangoPI:Module=SmartCache-" + cache.cacheName();
        this.cache = cache;
    }

    /**
     * Start Smart Cache MBean service
     *
     * @throws com.sohail.alam.mango_pi.smart.cache.SmartCacheException
     *          the smart cache exception
     */
    public void startSmartCacheMBeanService() throws SmartCacheException {
        try {
            MBeanService.startService(this, MBEAN_NAME);
        } catch (Exception e) {
            throw new SmartCacheException("Smart Cache was unable to start the Smart Cache MBean Service: " + e.getMessage(), e);
        }
    }

    /**
     * Stop Smart Cache MBean service.
     *
     * @throws com.sohail.alam.mango_pi.smart.cache.SmartCacheException
     *          the smart cache exception
     */
    @Override
    @JMXBeanOperation(name = "stopMBeanService",
            description = "Stops The Smart Cache MBean service")
    public void stopMBeanService() throws SmartCacheException {
        try {
            MBeanService.stopService(MBEAN_NAME);
        } catch (Exception e) {
            throw new SmartCacheException("Smart Cache was unable to stop the Smart Cache MBean Service: " + e.getMessage(), e);
        }
    }

    /**
     * Put the Data of type {@link V} into the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache},
     * corresponding to the Key of type {@link K}
     *
     * @param key      Any Key of type
     * @param data     Any Data of type
     * @param ttl      the ttl value - The after which data will be auto deleted from the Cache
     * @param timeUnit the time unit for the TTL Value
     */
    @Override
    @JMXBeanOperation(name = "put",
            description = "Put the DATA corresponding to a KEY, and set its TTL Value")
    public void put(@JMXBeanParameter(name = "The Key",
            description = "The Key for the Cache element") K key, @JMXBeanParameter(name = "The Data",
            description = "The Data to be stored") V data, @JMXBeanParameter(name = "The TTL Value",
            description = "The TTL Value for the Cache Element, -1 to keep data forever") int ttl, @JMXBeanParameter(name = "The TimeUnit",
            description = "The TimeUnit for the TTL Value") TimeUnit timeUnit) {

        this.cache.put(key, data, ttl, timeUnit);
    }

    /**
     * Put the Data of type {@link V} into the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache},
     * corresponding to the Key of type {@link K}
     *
     * @param key  Any Key of type
     * @param data Any Data of type
     * @param ttl  the ttl value - The after which data will be auto deleted from the Cache
     */
    @Override
    @JMXBeanOperation(name = "put",
            description = "Put the DATA corresponding to a KEY, and set its TTL Value")
    public void put(@JMXBeanParameter(name = "The Key",
            description = "The Key for the Cache element") K key, @JMXBeanParameter(name = "The Data",
            description = "The Data to be stored") V data, @JMXBeanParameter(name = "The TTL Value",
            description = "The TTL Value for the Cache Element, -1 to keep data forever") int ttl) {

        this.cache.put(key, data, ttl);
    }

    /**
     * Removes the Data corresponding to the given Key from the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @param key    The Key of type
     * @param reason the reason
     *
     * @return The Data of type
     *         that was removed
     */
    @Override
    @JMXBeanOperation(name = "remove",
            description = "Removes the Data corresponding to the given Key from the SmartCache")
    public V remove(@JMXBeanParameter(name = "The Key",
            description = "The Key for the Cache element") K key, @JMXBeanParameter(name = "The Reason",
            description = "The Reason for which the data was removed") String reason) {
        return (V) this.cache.remove(key, reason);
    }

    /**
     * Removes an entire Map from the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache} and returns it
     *
     * @return dataMap Map of type {@link java.util.concurrent.ConcurrentMap} having Key of type {@link K} and Data of type {@link V}
     */
    @Override
    @JMXBeanOperation(name = "removeAll",
            description = "Removes ALL the Data from the SmartCache")
    public ConcurrentMap<K, V> removeAll(@JMXBeanParameter(name = "The Reason",
            description = "The Reason for which the data were removed") String reason) {
        return this.cache.removeAll(reason);
    }

    /**
     * Checks if the Cache entry contains the given KEY
     *
     * @param key The exact KEY to search for
     *
     * @return true /false
     */
    @Override
    @JMXBeanOperation(name = "containsKey",
            description = "Checks if the Cache entry contains the given KEY")
    public boolean containsKey(@JMXBeanParameter(name = "The Key",
            description = "The Key for the Cache element") K key) {
        return this.cache.containsKey(key);
    }

    /**
     * Checks if the Cache entry contains the given KEY
     *
     * @param key The exact KEY to search for
     *
     * @return true /false
     */
    @Override
    @JMXBeanOperation(name = "containsValue",
            description = "Checks if the Cache entry contains the given VALUE")
    public boolean containsValue(@JMXBeanParameter(name = "The Value",
            description = "Checks the existence of this Value") V key) {
        return this.cache.containsValue(key);
    }

    /**
     * Checks whether the cache is currently empty
     *
     * @return true /false
     */
    @Override
    @JMXBeanOperation(name = "isEmpty",
            description = "Checks whether the cache is currently empty")
    public boolean isEmpty() {
        return this.cache.isEmpty();
    }

    /**
     * Gets the total number of entries in the Cache
     *
     * @return number of entries
     */
    @Override
    @JMXBeanOperation(name = "numberOfEntries",
            description = "Gets the total number of entries in the Cache")
    public int numberOfEntries() {
        return this.cache.numberOfEntries();
    }

    /**
     * Start auto cleaner.
     * This method starts the auto cleaner service and deletes the entries which has been timed out.
     * It is HIGHLY RECOMMENDED to use either this {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAllAutoCleaner(com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener)} method,
     * or the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAllAutoCleaner()} method for the cleanup process.
     */
    @Override
    @JMXBeanOperation(name = "startAllAutoCleaner",
            description = "Start Auto Cleaner for the pending entries (if any)")
    public void startAllAutoCleaner() {
        this.cache.startAllAutoCleaner();
    }

    /**
     * Stops the ongoing Auto Cleaner Service for the Cache data corresponding to a specific key.
     * This only works if you the Cache entries were made using the methods:
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#put(Object, Object, int)} and
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#put(Object, Object, int, java.util.concurrent.TimeUnit)} methods.
     * Also, the autoCleaner for this purpose must have been initiated using the one of the following methods:
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAllAutoCleaner()} or
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAllAutoCleaner(com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener)}.
     *
     * @param key         the key
     * @param removeEntry if <code>true</code> then the entry corresponding to this key is removed
     *                    from the Cache after stopping its cleanup task.
     */
    @Override
    @JMXBeanOperation(name = "stopAutoCleaner",
            description = "Stops the Auto Cleaner for the entry with the given KEY. Optionally you can remove the entry immediately.)")
    public void stopAutoCleaner(@JMXBeanParameter(name = "The Key",
            description = "The Key for the entry") K key, @JMXBeanParameter(name = "Remove Entry (true/false)",
            description = "Whether to remove the entry from the Cache or keep it forever") boolean removeEntry) {

        this.cache.stopAutoCleaner(key, removeEntry);
    }

    /**
     * Stops the ongoing Auto Cleaner Service for the Cache data corresponding to all keys.
     * This only works if you the Cache entries were made using the methods:
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#put(Object, Object, int)} and
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#put(Object, Object, int, java.util.concurrent.TimeUnit)} methods.
     * Also, the autoCleaner for this purpose must have been initiated using the one of the following methods:
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAllAutoCleaner()} or
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAllAutoCleaner(com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener)}.
     *
     * @param removeEntry if <code>true</code> then the entry corresponding to this key is removed
     *                    from the Cache after stopping its cleanup task.
     */
    @Override
    @JMXBeanOperation(name = "stopAllAutoCleaner",
            description = "Stops the Auto Cleaner for all the entries. Optionally you can remove the entry immediately.)")
    public void stopAllAutoCleaner(@JMXBeanParameter(name = "Remove Entry (true/false)",
            description = "Whether to remove the entries from the Cache or keep it forever") boolean removeEntry) {

        this.cache.stopAllAutoCleaner(removeEntry);
    }

    /**
     * This will restart the Auto Cleaner Service for the given key
     *
     * @param key The key for the Cache Entry
     */
    @Override
    @JMXBeanOperation(name = "restartAutoCleaner",
            description = "Restarts the Auto Cleaner Service for the the Cache element associated with the specified KEY")
    public void restartAutoCleaner(@JMXBeanParameter(name = "The Key",
            description = "The Key for the entry") K key) {
        this.cache.restartAutoCleaner(key);
    }

    /**
     * Gets the total number of entries in the Cache whose keys matches a certain pattern
     *
     * @param somePortionOfKey the some portion of KEY
     *
     * @return number of entries
     */
    @Override
    @JMXBeanOperation(name = "numberOfEntries",
            description = "Gets the total number of entries in the Cache whose keys matches a certain pattern")
    public int numberOfEntries(@JMXBeanParameter(name = "Some Portion Of Key",
            description = "Some portion of the Key") K somePortionOfKey) {

        int count = 0;
        Set<V> keys = this.cache.keySet();
        for (V key : keys) {
            if (String.valueOf(key).contains(String.valueOf(somePortionOfKey)))
                count++;
        }

        return count;
    }

    /**
     * Purges the entire cache
     *
     * @return the boolean
     */
    @Override
    @JMXBeanOperation(name = "purgeAllCacheEntries",
            description = "Purges the entire cache")
    public boolean purgeAllCacheEntries() throws ExecutionException, InterruptedException {
        return this.cache.purgeAllCacheEntries();
    }

    /**
     * Purge cache entry.
     *
     * @param keys the keys
     *
     * @return the boolean
     */
    @Override
    @JMXBeanOperation(name = "purgeCacheEntries",
            description = "Purges only the data corresponding to the given set of KEYs")
    public boolean purgeCacheEntries(@JMXBeanParameter(name = "The Set of Keys",
            description = "The set of Keys for the Cache elements") Set<K> keys) throws ExecutionException, InterruptedException {
        return this.cache.purgeCacheEntries(keys);
    }

    /**
     * Purges only the data corresponding to the given KEY
     *
     * @param key the KEY
     *
     * @return the boolean
     */
    @Override
    @JMXBeanOperation(name = "purgeCacheEntry",
            description = "Purges only the data corresponding to the given KEY")
    public boolean purgeCacheEntry(@JMXBeanParameter(name = "The Key",
            description = "The Key for the Cache element which is to be purged") K key) {
        return this.cache.purgeCacheEntry(key);
    }

    /**
     * Gets deleted entries counter.
     *
     * @return the deleted entries counter
     *
     * @throws com.sohail.alam.mango_pi.smart.cache.mbeans.SmartCacheMBeanException
     *          the smart cache m bean exception
     */
    @Override
    @JMXBeanOperation(name = "getDeletedEntriesCounter",
            description = "Gets the total number of entries deleted for this Smart Cache")
    public long getDeletedEntriesCounter() throws SmartCacheMBeanException {
        return ((AbstractSmartCache) this.cache).deletedEntriesCounter();
    }

    /**
     * Reset deleted entries counter.
     *
     * @throws com.sohail.alam.mango_pi.smart.cache.mbeans.SmartCacheMBeanException
     *          the smart cache m bean exception
     */
    @Override
    @JMXBeanOperation(name = "resetDeletedEntriesCounter",
            description = "Resets the total number of entries deleted for this Smart Cache to zero")
    public void resetDeletedEntriesCounter() throws SmartCacheMBeanException {
        ((AbstractSmartCache) this.cache).resetDeletedEntriesCounter();
    }

    /**
     * Get the unique name for this Smart Cache Instance
     *
     * @return The unique name for this Smart Cache Instance
     */
    @Override
    @JMXBeanOperation(name = "cacheName",
            description = "Get the unique name for this Smart Cache Instance")
    public String cacheName() {
        return ((AbstractSmartCache) this.cache).cacheName();
    }
}
