package com.sohail.alam.mango_pi.smart.cache;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 7/7/13
 * Time: 10:04 AM
 */
public interface SmartCacheManagerMBean<K> {


    /**
     * This is just an Example MBean Name for the Smart Cache.
     * Name Format: "MangoPI:Module=SmartCache-(THE UNIQUE NAME FOR THIS CACHE INSTANCE)".
     * <p/>
     * Here the Unique Name for this instance of the SmartCache is selected by the User,
     * while instantiating the SmartCache using the {@link AbstractSmartCache},
     * of any of the helper class that extends it, such as {@link DefaultSmartCache}.
     */
    public static final String MBEAN_NAME = "MangoPI:Module=SmartCache-(THE UNIQUE NAME FOR THIS CACHE INSTANCE)";

    /**
     * Stop Smart Cache MBean service.
     *
     * @throws SmartCacheException the smart cache exception
     */
    public void stopSmartCacheMBeanService() throws SmartCacheException;

    /**
     * Start auto cleaner.
     *
     * @param EXPIRY_DURATION   the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY  The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY The time after which the auto cleaner task repeats itself
     * @param TIME_UNIT         The time unit in which the above are defined
     *
     * @return the boolean
     *
     * @throws SmartCacheException the smart cache exception
     */
    public boolean startAutoCleanerWithTimeUnit(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT) throws SmartCacheException;

    /**
     * Start auto cleaner.
     *
     * @param EXPIRY_DURATION   the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY  The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY The time after which the auto cleaner task repeats itself
     *
     * @return the boolean
     *
     * @throws SmartCacheException the smart cache exception
     */
    public boolean startAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY) throws SmartCacheException;

    /**
     * Start auto cleaner reflection.
     *
     * @param EXPIRY_DURATION       the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY      The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY     The time after which the auto cleaner task repeats itself
     * @param TIME_UNIT             The time unit in which the above are defined
     * @param CALLBACK_CLASS_OBJECT The Object of the Class in which the Callback method resides
     * @param CALLBACK_METHOD       The Callback Method
     *
     * @return the boolean
     *
     * @throws SmartCacheException the smart cache exception
     */
    public boolean startAutoCleanerReflection(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT, final Object CALLBACK_CLASS_OBJECT, final Method CALLBACK_METHOD) throws SmartCacheException;

    /**
     * Stop auto cleaner.
     */
    public void stopAutoCleaner();

    /**
     * Restart auto cleaner.
     *
     * @param EXPIRY_DURATION   the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY  The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY The time after which the auto cleaner task repeats itself
     * @param TIME_UNIT         The time unit in which the above are defined
     *
     * @return the boolean
     *
     * @throws SmartCacheException the smart cache exception
     */
    public boolean rescheduleAutoCleanerWithTimeUnit(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT) throws SmartCacheException;

    /**
     * Restart auto cleaner.
     *
     * @param EXPIRY_DURATION   the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY  The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY The time after which the auto cleaner task repeats itself
     *
     * @return the boolean
     *
     * @throws SmartCacheException the smart cache exception
     */
    public boolean rescheduleAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY) throws SmartCacheException;

    /**
     * Restart auto cleaner reflection.
     *
     * @param EXPIRY_DURATION       the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY      The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY     The time after which the auto cleaner task repeats itself
     * @param TIME_UNIT             The time unit in which the above are defined
     * @param CALLBACK_CLASS_OBJECT The Object of the Class in which the Callback method resides
     * @param CALLBACK_METHOD       The Callback Method
     *
     * @return the boolean
     *
     * @throws SmartCacheException the smart cache exception
     */
    public boolean rescheduleAutoCleanerReflection(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT, final Object CALLBACK_CLASS_OBJECT, final Method CALLBACK_METHOD) throws SmartCacheException;

    /**
     * Checks if the Cache entry contains the given KEY
     *
     * @param key The exact KEY to search for
     *
     * @return true /false
     */
    public boolean containsKey(K key);

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
    public int size();

    /**
     * Gets the total number of entries in the Cache whose keys matches a certain pattern
     *
     * @param somePortionOfKey the some portion of KEY
     *
     * @return number of entries
     */
    public int numberOfEntries(K somePortionOfKey);

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     *
     * @return Smart Cache Info
     */
    public String allSmartCacheInfo();

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     * corresponding to the given KEY.
     *
     * @param key the KEY
     *
     * @return Smart Cache Info
     */
    public String smartCacheInfoForKey(K key);

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     * corresponding to the given set of Keys
     *
     * @param keys the keys
     *
     * @return Smart Cache Info
     */
    public String smartCacheInfoForKeySet(Set<K> keys);

    /**
     * Purges the entire cache
     *
     * @return the boolean
     */
    public boolean purgeAllCacheEntries();

    /**
     * Purges only the data corresponding to the given KEY
     *
     * @param key the KEY
     *
     * @return the boolean
     */
    public boolean purgeCacheEntry(K key);

    /**
     * Purge cache entry.
     *
     * @param keys the keys
     *
     * @return the boolean
     */
    public boolean purgeCacheEntries(Set<K> keys);

    /**
     * View history.
     *
     * @param key the key
     *
     * @return the string
     */
    public String viewHistoryForKey(K key);

    /**
     * View history.
     *
     * @param reason the reason
     *
     * @return the string
     */
    public String viewHistoryForReason(String reason);

    /**
     * View history.
     *
     * @return the string
     */
    public String viewAllHistory();

    /**
     * Set the maximum number of entries after which the History is
     * deleted permanently.
     *
     * @param maxElementCount the max element count
     */
    public void autoDeleteHistory(int maxElementCount);

    /**
     * Purges the contents of History into a user defined file.
     * By default the SmartCache will dump the data into a file named -
     * Smart_Cache_History_(current-date/time).txt
     *
     * @param filePath the absolute file path for the dump file.
     */
    public void purgeHistory(String filePath) throws Exception;

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

}
