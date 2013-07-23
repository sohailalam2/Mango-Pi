package com.sohail.alam.mango_pi.smart.cache;

import com.sohail.alam.mango_pi.smart.cache.mbeans.AbstractSmartCacheManager;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.sohail.alam.mango_pi.smart.cache.SmartCache.SmartCacheDeleteReason.EXPIRED;
import static com.sohail.alam.mango_pi.smart.cache.SmartCache.SmartCacheDeleteReason.PURGED;
import static com.sohail.alam.mango_pi.smart.cache.SmartCacheHistoryImpl.SMART_CACHE_HISTORY;

/**
 * This {@link AbstractSmartCache} abstract Class implements {@link SmartCache}
 * and defines all the methods excepts those for the Total Cache Size and those for Smart Cache Info.
 * Any class that extends this {@link AbstractSmartCache} must define all these methods.
 * <p/>
 * It also initializes a Smart Cache MBean for management purpose (if enabled by User).
 * <p/>
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/7/13
 * Time: 12:15 PM
 */
public abstract class AbstractSmartCache<K, V> implements SmartCache<K, V> {

    private static final ArrayList<String> UNIQUE_CACHE_NAMES = new ArrayList<String>();
    private final ConcurrentHashMap<K, V> SMART_CACHE_DATA;
    private final ConcurrentHashMap<K, ScheduledFuture<V>> TASK_HOLDER;
    private final ConcurrentHashMap<K, ConcurrentHashMap<String, Object>> NON_SCHEDULED_TASKS;
    private final ScheduledThreadPoolExecutor TASK_EXECUTOR;
    private final ExecutorService PURGE_EXECUTOR;
    private SmartCacheEventListener smartCacheEventListener = null;
    private String cacheName = "SmartCache";
    private boolean startAutoCleaner = true;
    private AtomicInteger canceledTasks;
    private AtomicLong deletedEntriesCounter;

    /**
     * Instantiates a new {@link AbstractSmartCache}
     *
     * @param cacheName     the cache name (must be unique if more than one Smart Cache
     *                      is instantiated in the application)
     * @param activateMBean This indicates whether to activate the SmartCache MBean.
     *
     * @throws SmartCacheException Throws any SmartCacheException that might occur.
     */
    public AbstractSmartCache(String cacheName, boolean activateMBean) throws SmartCacheException {
        this.cacheName = cacheName;
        if (UNIQUE_CACHE_NAMES.contains(this.cacheName)) {
            throw new SmartCacheException("The Smart Cache Name: '" + cacheName
                    + "' is not unique, please select another name for this SmartCache instance");
        } else {
            UNIQUE_CACHE_NAMES.add(this.cacheName);
        }
        SMART_CACHE_DATA = new ConcurrentHashMap<K, V>();
        TASK_HOLDER = new ConcurrentHashMap<K, ScheduledFuture<V>>();
        TASK_EXECUTOR = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
        NON_SCHEDULED_TASKS = new ConcurrentHashMap<K, ConcurrentHashMap<String, Object>>();
        PURGE_EXECUTOR = Executors.newSingleThreadExecutor();
        canceledTasks = new AtomicInteger(0);
        deletedEntriesCounter = new AtomicLong(0);
        if (activateMBean) {
            new AbstractSmartCacheManager<AbstractSmartCache, K, V>(this).startSmartCacheMBeanService();
        }
    }

    /**
     * Checks whether the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache} is empty.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return SMART_CACHE_DATA.isEmpty();
    }

    /**
     * Checks whether the specified key is associated with any value in the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @param key The Key of type {@link K}
     *
     * @return {@code true} if present, otherwise {@code false}
     */
    @Override
    public boolean containsKey(K key) throws NullPointerException {
        return SMART_CACHE_DATA.containsKey(key);
    }

    /**
     * Checks whether the specified value is associated with any key in the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @param value The Key of type {@link V}
     *
     * @return {@code true} if present, otherwise {@code false}
     */
    @Override
    public boolean containsValue(V value) throws NullPointerException {
        return SMART_CACHE_DATA.containsValue(value);
    }

    /**
     * Put the Data of type {@link V} into the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache},
     * corresponding to the Key of type {@link K}.
     * <p/>
     * A TTL Value is associated with the cache entry and it is automatically deleted from the
     * Cache if the entry out lives its duration.
     * This can be prevented and the entry can live forever (until the user manually deletes it),
     * by setting the TTL value to -1.
     * <p/>
     * For the entries to be auto deleted the Smart Cache Auto Cleaner Service must be kept alive.
     * By default it is on.
     * <p/>
     * If the Auto Cleaner Service is not alive, then the entries will be deleted
     * (if and when it outlives) when the Auto Cleaner Service is started.
     * <p/>
     * If a {@link SmartCacheEventListener} is attached to this {@link SmartCache} instance,
     * then an appropriate callback is received in the method
     * {@link SmartCacheEventListener#onCreateCacheEntry(Object, Object)}.
     *
     * @param key      Any Key of type {@link K}
     * @param data     Any Data of type {@link V}
     * @param ttl      the ttl value - The after which data will be auto deleted from the Cache
     * @param timeUnit the time unit for the TTL Value
     */
    @Override
    public void put(K key, V data, int ttl, TimeUnit timeUnit) {
        SMART_CACHE_DATA.put(key, data);
        if (ttl > 0) {
            if (startAutoCleaner)
                TASK_HOLDER.put(key, TASK_EXECUTOR.schedule(new AutoCleanerTask(key), ttl, timeUnit));
            else {
                ConcurrentHashMap<String, Object> temp = new ConcurrentHashMap<String, Object>();
                temp.put("TTL", ttl);
                temp.put("TIME_UNIT", timeUnit);
                temp.put("CURRENT_NANO", System.nanoTime());
                NON_SCHEDULED_TASKS.put(key, temp);
            }
        }
        if (smartCacheEventListener != null)
            smartCacheEventListener.onCreateCacheEntry(key, data);
    }

    /**
     * Put the Data of type {@link V} into the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache},
     * corresponding to the Key of type {@link K}.
     * <p/>
     * A TTL Value is associated with the cache entry and it is automatically deleted from the
     * Cache if the entry out lives its duration.
     * This can be prevented and the entry can live forever (until the user manually deletes it),
     * by setting the TTL value to -1.
     * <p/>
     * For the entries to be auto deleted the Smart Cache Auto Cleaner Service must be kept alive.
     * By default it is on.
     * <p/>
     * If the Auto Cleaner Service is not alive, then the entries will be deleted
     * (if and when it outlives) when the Auto Cleaner Service is started.
     * <p/>
     * If a {@link SmartCacheEventListener} is attached to this {@link SmartCache} instance,
     * then an appropriate callback is received in the method
     * {@link SmartCacheEventListener#onCreateCacheEntry(Object, Object)}.
     *
     * @param key  Any Key of type {@link K}
     * @param data Any Data of type {@link V}
     * @param ttl  the ttl value - The after which data will be auto deleted from the Cache
     *             The Time Unit for this TTL Value defaults to Seconds.
     */
    @Override
    public void put(K key, V data, int ttl) throws NullPointerException {
        put(key, data, ttl, TimeUnit.SECONDS);
    }

    /**
     * Get the Data corresponding to the given Key from the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @param key The Key of type {@link K}
     *
     * @return The Data of type {@link V}
     */
    @Override
    public V get(K key) throws NullPointerException {
        return SMART_CACHE_DATA.get(key);
    }

    /**
     * Removes the Data corresponding to the given Key from the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     * <p/>
     * If a {@link SmartCacheEventListener} is attached to this {@link SmartCache} instance,
     * then an appropriate callback is received in the method
     * {@link SmartCacheEventListener#onDeleteCacheEntry(Object, Object, String)}.
     *
     * @param key    The Key of type
     * @param reason the reason for which the entry was deleted.
     *               This can contain any value, but preferably one of the values present in
     *               {@link SmartCache.SmartCacheDeleteReason}.
     *
     * @return The Data of type that was removed
     */
    @Override
    public V remove(K key, String reason) {
        V data;
        if ((data = SMART_CACHE_DATA.remove(key)) != null) {
            deletedEntriesCounter.incrementAndGet();
            if (smartCacheEventListener != null)
                smartCacheEventListener.onDeleteCacheEntry(key, data, reason);
            return data;
        } else {
            return null;
        }
    }

    /**
     * Gets a copy of the entire Map from the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @return dataMap Map of type {@link java.util.concurrent.ConcurrentMap}
     *         having Key of type {@link K} and Data of type {@link V}
     */
    @Override
    public ConcurrentMap<K, V> copy() {
        ConcurrentMap<K, V> copy = new ConcurrentHashMap<K, V>();
        copy.putAll(SMART_CACHE_DATA);
        return copy;
    }

    /**
     * Removes an entire Map from the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache} and returns it
     * <p/>
     * If a {@link SmartCacheEventListener} is attached to this {@link SmartCache} instance,
     * then an appropriate callback is received in the method
     * {@link SmartCacheEventListener#onDeleteCacheEntry(Object, Object, String)}
     * for <strong>every</strong> entry that is deleted.
     * <p/>
     * One could filter the entries on the basis of Deleted Reason.
     *
     * @return dataMap Map of type {@link java.util.concurrent.ConcurrentMap}
     *         having Key of type {@link K} and Data of type {@link V}
     */
    @Override
    public ConcurrentMap<K, V> removeAll(String reason) {
        ConcurrentMap<K, V> tempData = new ConcurrentHashMap<K, V>();
        tempData.putAll(SMART_CACHE_DATA);
        for (K key : keySet()) {
            remove(key, reason);
        }
        return tempData;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are reflected in the set,
     * and vice-versa.
     * <p/>
     * The set supports element removal, which removes the corresponding mapping from this map,
     * via the Iterator.remove, Set.remove, removeAll, retainAll, and clear operations.
     * It does not support the add or addAll operations.
     *
     * @return A set view of the keys contained in this map
     */
    @Override
    public Set<K> keySet() {
        return SMART_CACHE_DATA.keySet();
    }

    /**
     * Returns a Collection view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are reflected in the collection,
     * and vice-versa.
     * <p/>
     * The collection supports element removal,
     * which removes the corresponding mapping from this map, via the Iterator.remove,
     * Collection.remove, removeAll, retainAll, and clear operations.
     * It does not support the add or addAll operations.
     *
     * @return A Collection view of the values contained in this map
     */
    @Override
    public Collection<V> values() {
        return SMART_CACHE_DATA.values();
    }

    /**
     * Get the number of key-value mappings in this map.
     * If the map contains more than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
     *
     * @return The number of key-value mappings in this map
     */
    @Override
    public int numberOfEntries() {
        return SMART_CACHE_DATA.size();
    }

    /**
     * Adds a {@link SmartCacheEventListener} or replaces an existing one.
     * <p/>
     * If a listener is attached to the {@link SmartCache} instance, then
     * {@link SmartCache} can provide automatic callbacks on various internal events.
     *
     * @param smartCacheEventListener the smart cache event listener
     */
    @Override
    public void addSmartCacheEventsListener(SmartCacheEventListener smartCacheEventListener) {
        this.smartCacheEventListener = smartCacheEventListener;
    }

    /**
     * Starts the Smart Cache Auto Cleaner Service. This method starts the auto cleaner service and
     * deletes the entries which have been timed out.
     * <p/>
     * The Smart Cache Auto Cleaner service is started by default and you need not invoke this
     * method, unless you at some point stop the Auto Cleaner Service.
     * <p/>
     * This method is needed, if you have stopped the Auto Cleaner Service and have added elements
     * into the {@link SmartCache} which needs to be removed automatically after a certain duration.
     * <p/>
     * This method obsoletes those that are found in the {@link DeprecatedSmartCache} class.
     * It is <Strong>HIGHLY RECOMMENDED</Strong> that you use this method instead of the
     * deprecated once for new design/implementation.
     * <p/>
     * As an added benefit apart from performance gain, you now can store different elements
     * into the {@link SmartCache} with different TTL value.
     * <p/>
     * Optionally with this method you can pass a {@link SmartCacheEventListener} to
     * receive callbacks on certain events.
     *
     * @param smartCacheEventListener the smart cache event listener
     */
    @Override
    public void startAllAutoCleaner(SmartCacheEventListener smartCacheEventListener) {
        this.startAutoCleaner = true;
        this.smartCacheEventListener = smartCacheEventListener;

        // If any Cache entries were put into the Cache before the timer was started then
        // start the timer for them too
        if (!NON_SCHEDULED_TASKS.isEmpty()) {
            ConcurrentHashMap<String, Object> temp;
            Integer ttl;
            TimeUnit timeUnit;
            Long putTime;
            long expiry;
            for (K key : NON_SCHEDULED_TASKS.keySet()) {
                temp = NON_SCHEDULED_TASKS.get(key);
                ttl = (Integer) temp.get("TTL");
                timeUnit = (TimeUnit) temp.get("TIME_UNIT");
                putTime = (Long) temp.get("CURRENT_NANO");
                expiry = System.nanoTime() - (TimeUnit.NANOSECONDS.convert(putTime, timeUnit));
                if (expiry > 0)
                    TASK_HOLDER.put(key, TASK_EXECUTOR.schedule(new AutoCleanerTask(key), ttl, timeUnit));
                else
                    remove(key, EXPIRED);
            }
        }
    }

    /**
     * Starts the Smart Cache Auto Cleaner Service. This method starts the auto cleaner service and
     * deletes the entries which have been timed out.
     * <p/>
     * The Smart Cache Auto Cleaner service is started by default and you need not invoke this
     * method, unless you at some point stop the Auto Cleaner Service.
     * <p/>
     * This method is needed, if you have stopped the Auto Cleaner Service and have added elements
     * into the {@link SmartCache} which needs to be removed automatically after a certain duration.
     * <p/>
     * This method obsoletes those that are found in the {@link DeprecatedSmartCache} class.
     * It is <Strong>HIGHLY RECOMMENDED</Strong> that you use this method instead of the
     * deprecated once for new design/implementation.
     * <p/>
     * As an added benefit apart from performance gain, you now can store different elements
     * into the {@link SmartCache} with different TTL value.
     */
    @Override
    public void startAllAutoCleaner() {
        startAllAutoCleaner(this.smartCacheEventListener);
    }

    /**
     * Stops the ongoing Auto Cleaner Service for the Cache data corresponding to a specific key.
     * This method {@link #stopAutoCleaner(Object, boolean)} obsoletes those that are found in the
     * {@link DeprecatedSmartCache} class, and only works if the element was put into the {@link SmartCache}
     * using either of the follwoing methods:
     * {@link #put(Object, Object, int, java.util.concurrent.TimeUnit)} or
     * {@link #put(Object, Object, int)}.
     * <p/>
     * If removeEntry param is <code>false</code> then the entry is backed up and you can
     * restart the auto cleaner for this element with the given KEY, using the method
     * {@link #restartAutoCleaner(Object)}.
     *
     * @param key         the key
     * @param removeEntry if <code>true</code> then the entry corresponding to this key is removed
     *                    from the Cache after stopping its cleanup task.
     */
    @Override
    public void stopAutoCleaner(K key, boolean removeEntry) {
        TASK_HOLDER.get(key).cancel(true);

        if (removeEntry)
            remove(key, EXPIRED);
        else {
            // Backup the task for cleanup when needed
            ConcurrentHashMap<String, Object> temp = new ConcurrentHashMap<String, Object>();
            temp.put("REMAINING_TIME", TASK_HOLDER.get(key).getDelay(TimeUnit.NANOSECONDS));
            NON_SCHEDULED_TASKS.put(key, temp);
        }
        if (canceledTasks.getAndIncrement() >= 100) {
            TASK_EXECUTOR.purge();
            canceledTasks.set(0);
        }
    }

    /**
     * Stops all the ongoing Auto Cleaner Services.
     * This method {@link #stopAutoCleaner(Object, boolean)} obsoletes those that are found in the
     * {@link DeprecatedSmartCache} class, and only works if the element was put into the {@link SmartCache}
     * using either of the follwoing methods:
     * {@link #put(Object, Object, int, java.util.concurrent.TimeUnit)} or
     * {@link #put(Object, Object, int)}.
     * <p/>
     * If removeEntry param is <code>false</code> then the entries are backed up and you can
     * restart the auto cleaner for these element with the given KEY, using the method
     * {@link #restartAutoCleaner(Object)}.
     *
     * @param removeEntry if <code>true</code> then the entries are removed
     *                    from the Cache after stopping its cleanup task.
     */
    @Override
    public void stopAllAutoCleaner(boolean removeEntry) {
        Set<K> keySet = TASK_HOLDER.keySet();
        for (K key : keySet) {
            stopAutoCleaner(key, removeEntry);
        }
        TASK_EXECUTOR.purge();
    }

    /**
     * Starts the Smart Cache Auto Cleaner Service for the entry corresponding to the given KEY.
     * This method starts the auto cleaner service and deletes the entries which have been timed out.
     * <p/>
     * The Smart Cache Auto Cleaner service is started by default and you need not invoke this
     * method, unless you at some point stop the Auto Cleaner Service.
     * <p/>
     *
     * @param key The KEY for the element in SmartCache for which the Auto Cleaner Service should be started.
     */
    @Override
    public void restartAutoCleaner(K key) {
        // If the Cache entries was put into the Cache, starts its timer
        if (!NON_SCHEDULED_TASKS.isEmpty()) {
            Long remaining;
            if ((remaining = (Long) NON_SCHEDULED_TASKS.get(key).get("REMAINING_TIME")) != null) {
                long expiry = System.nanoTime() - remaining;
                if (expiry > 0)
                    TASK_HOLDER.put(key, TASK_EXECUTOR.schedule(new AutoCleanerTask(key), expiry, TimeUnit.NANOSECONDS));
                else
                    remove(key, EXPIRED);
            }
        }
    }

    /**
     * Purges only the data corresponding to the given KEY.
     * Invoking this method will give a callback to the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener#onSingleEntryPurge(Object, Object)}.
     *
     * @param key the KEY
     *
     * @return the <code>true</code> if everything goes fine else <code>false</code>.
     */
    @Override
    public boolean purgeCacheEntry(K key) {
        return PURGE_EXECUTOR.submit(new CachePurger(key)).isDone();
    }

    /**
     * Purges only the data corresponding to the given set of KEYs.
     * Invoking this method will give a callback to the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener#onCachePurge(java.util.Map)}.
     *
     * @param keys the keys
     *
     * @return the boolean
     */
    @Override
    public boolean purgeCacheEntries(Set<K> keys) throws ExecutionException, InterruptedException {
        return PURGE_EXECUTOR.submit(new CachePurger(keys)).get();
    }

    /**
     * Purge the entire cache for backup purpose. Invoking this method will give a
     * callback to {@link com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener#onCachePurge(java.util.Map)}.
     *
     * @return the <code>true</code> if everything goes fine else <code>false</code>.
     */
    @Override
    public boolean purgeAllCacheEntries() throws ExecutionException, InterruptedException {
        return purgeCacheEntries(keySet());
    }

    /**
     * Get the unique name for this Smart Cache Instance
     *
     * @return The unique name for this Smart Cache Instance
     */
    public String cacheName() {
        return cacheName;
    }

    /**
     * The Deleted Entries Counter increments whenever an item expires its TTL and is deleted from the
     * {@link SmartCache}.
     *
     * @return The value of
     */
    public long deletedEntriesCounter() {
        return deletedEntriesCounter.get();
    }

    /**
     * Resets the {@code deletedEntriesCounter} back to zero.
     */
    public void resetDeletedEntriesCounter() {
        deletedEntriesCounter = new AtomicLong(0);
    }

    /**
     * Class responsible for purging the cache entries
     */
    private final class CachePurger implements Callable<Boolean> {

        private Set<K> keys = keySet();

        /**
         * Instantiates a new Purger class.
         *
         * @param keys the keys
         */
        public CachePurger(Set<K> keys) {
            this.keys = keys;
        }

        /**
         * Instantiates a new Purger class.
         *
         * @param key the key
         */
        public CachePurger(K key) {
            keys = new HashSet<K>(1);
            keys.add(key);
        }

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         *
         * @throws Exception if unable to compute a result
         */
        @Override
        public Boolean call() throws Exception {
            Map<K, V> cacheEntries = new HashMap<K, V>();
            if (smartCacheEventListener != null) {
                for (K key : keys) {
                    V value = SMART_CACHE_DATA.remove(key);
                    cacheEntries.put(key, value);
                    if (value instanceof SmartCachePojo) {
                        SMART_CACHE_HISTORY.addToHistory(PURGED, key, (SmartCachePojo) value);
                    }
                }
                smartCacheEventListener.onCachePurge(cacheEntries);
                return true;
            } else return false;
        }
    }

    /**
     * Class responsible for the clean up operations
     */
    private final class AutoCleanerTask implements Callable<V> {

        private K key;

        AutoCleanerTask(K key) {
            this.key = key;
        }

        @Override
        public V call() throws Exception {
            return remove(key, EXPIRED);
        }
    }

}
