package com.sohail.alam.mango_pi.smart.cache.mbeans;

import com.sohail.alam.mango_pi.smart.cache.SmartCacheException;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/7/13
 * Time: 12:46 PM
 */
public interface AbstractSmartCacheManagerMBean<K, V> {

    /**
     * ## NOTE ITS A DUMMY MBEAN NAME
     * <p/>
     * This is just an Example MBean Name for the Smart Cache.
     * Name Format: "MangoPI:Module=SmartCache-(THE UNIQUE NAME FOR THIS CACHE INSTANCE)".
     * <p/>
     * Here the Unique Name for this instance of the SmartCache is selected by the User,
     * while instantiating the SmartCache using the {@link com.sohail.alam.mango_pi.smart.cache.DefaultSmartCache},
     * of any of the helper class that extends it, such as {@link com.sohail.alam.mango_pi.smart.cache.DeprecatedSmartCache}.
     */
    public static final String MBEAN_NAME = "MangoPI:Module=SmartCache-(THE UNIQUE NAME FOR THIS CACHE INSTANCE)";

    /**
     * Stop Smart Cache MBean service.
     *
     * @throws com.sohail.alam.mango_pi.smart.cache.SmartCacheException
     *          the smart cache exception
     */
    public void stopMBeanService() throws SmartCacheException;

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
    public void put(K key, V data, int ttl, TimeUnit timeUnit);

    /**
     * Put the Data of type {@link V} into the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache},
     * corresponding to the Key of type {@link K}
     *
     * @param key  Any Key of type
     * @param data Any Data of type
     * @param ttl  the ttl value - The after which data will be auto deleted from the Cache
     */
    public void put(K key, V data, int ttl);

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
    public V remove(K key, String reason);

    /**
     * Removes an entire Map from the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache} and returns it
     *
     * @return dataMap Map of type {@link java.util.concurrent.ConcurrentMap} having Key of type {@link K} and Data of type {@link V}
     */
    public ConcurrentMap<K, V> removeAll(String reason);

    /**
     * Checks if the Cache entry contains the given KEY
     *
     * @param key The exact KEY to search for
     *
     * @return true /false
     */
    public boolean containsKey(K key);

    /**
     * Checks if the Cache entry contains the given KEY
     *
     * @param key The exact KEY to search for
     *
     * @return true /false
     */
    public boolean containsValue(V key);

    /**
     * Checks whether the cache is currently empty
     *
     * @return true /false
     */
    public boolean isEmpty();

    /**
     * Gets the total number of entries in the Cache
     *
     * @return number of entries
     */
    public int numberOfEntries();

    /**
     * Start auto cleaner.
     * This method starts the auto cleaner service and deletes the entries which has been timed out.
     * It is HIGHLY RECOMMENDED to use either this {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAllAutoCleaner(com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener)} method,
     * or the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAllAutoCleaner()} method for the cleanup process.
     */
    public void startAllAutoCleaner();

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
    public void stopAutoCleaner(K key, boolean removeEntry);

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
    public void stopAllAutoCleaner(boolean removeEntry);

    /**
     * This will restart the Auto Cleaner Service for the given key
     *
     * @param key The key for the Cache Entry
     */
    public void restartAutoCleaner(K key);

    /**
     * Gets the total number of entries in the Cache whose keys matches a certain pattern
     *
     * @param somePortionOfKey the some portion of KEY
     *
     * @return number of entries
     */
    public int numberOfEntries(K somePortionOfKey);

    /**
     * Purges the entire cache
     *
     * @return the boolean
     */
    public boolean purgeAllCacheEntries() throws ExecutionException, InterruptedException;

    /**
     * Purge cache entry.
     *
     * @param keys the keys
     *
     * @return the boolean
     */
    public boolean purgeCacheEntries(Set<K> keys) throws ExecutionException, InterruptedException;

    /**
     * Purges only the data corresponding to the given KEY
     *
     * @param key the KEY
     *
     * @return the boolean
     */
    public boolean purgeCacheEntry(K key);

    /**
     * Gets deleted entries counter.
     *
     * @return the deleted entries counter
     *
     * @throws SmartCacheMBeanException the smart cache m bean exception
     */
    public long getDeletedEntriesCounter() throws SmartCacheMBeanException;

    /**
     * Reset deleted entries counter.
     *
     * @throws SmartCacheMBeanException the smart cache m bean exception
     */
    public void resetDeletedEntriesCounter() throws SmartCacheMBeanException;

    /**
     * Get the unique name for this Smart Cache Instance
     *
     * @return The unique name for this Smart Cache Instance
     */
    public String cacheName();
}
