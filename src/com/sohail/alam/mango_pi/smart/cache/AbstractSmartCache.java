/*
 * Copyright 2013 The Mango Pi Project
 *
 * The Mango Pi Project licenses this file to you under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *              http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.sohail.alam.mango_pi.smart.cache;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sohail.alam.mango_pi.smart.cache.SmartCache.SmartCacheDeleteReason.EXPIRED;
import static com.sohail.alam.mango_pi.smart.cache.SmartCache.SmartCacheDeleteReason.PURGED;
import static com.sohail.alam.mango_pi.smart.cache.SmartCacheHistoryImpl.HISTORY;

/**
 * <p>
 * The heart of {@link SmartCache} implementation with this abstract class {@link AbstractSmartCache},
 * lies in the {@link ConcurrentMap} Data Structure provided by the
 * {@link java.util.concurrent} package. The difference in what {@link SmartCache} and {@link ConcurrentMap}
 * does is that, {@link SmartCache} goes a little further and provide you with a
 * solution to automatically delete entries from the {@link ConcurrentMap} Data Structure when the
 * Time To Live (TTL) for that entry expires, and fire events when something happens, like when
 * entries are deleted from the Cache.
 * </p>
 * <p>
 * Any concrete class, such as {@link DefaultSmartCache} that extends {@link AbstractSmartCache}
 * implements the {@link SmartCache#startAutoCleaner(long, long, long, java.util.concurrent.TimeUnit)},
 * {@link SmartCache#startAutoCleaner(long, long, long, java.util.concurrent.TimeUnit, Object, java.lang.reflect.Method)},
 * {@link SmartCache#startAutoCleaner(long, long, long, java.util.concurrent.TimeUnit, SmartCacheEventListener)}
 * methods, which checks the TTL value for each elements stored in the {@link SmartCache} and removes it
 * when expired. Also, {@link SmartCache#stopAutoCleaner()} method is
 * provided for you to cancel the Auto Cleaner Service when not needed.
 * </p>
 * <p>
 * But in order for this to work, your Data Structure must implement the {@link SmartCachePojo}, which
 * automatically sets a creation time {@link SmartCachePojo#TIME_STAMP}, which is then checked against the
 * current time and the TTL value that you should have entered while enabling the
 * {@link SmartCache#startAutoCleaner(long, long, long, java.util.concurrent.TimeUnit)}.
 * </p>
 * <p>
 * Alternatively you may choose to extend this abstract class {@link AbstractSmartCache} and
 * write your own logic for automatically cleaning/deleting entries from the map. Or you may choose
 * to persist the data into a database, when its TTL expires.
 * </p>
 * User: Sohail Alam
 * Version: 1.1.6
 * Date: 9/6/13
 * Time: 1:14 AM
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
    private boolean startAutoCleaner = false;
    private AtomicInteger canceledTasks;

    /**
     * Instantiates a new {@link DefaultSmartCache}
     *
     * @param cacheName     the cache name (must be unique if more than one Smart Cache is instantiated in the application)
     * @param activateMBean This indicates whether to activate the SmartCache MBean.
     *
     * @throws SmartCacheException Throws any SmartCacheException whatsoever.
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
        if (activateMBean) {
            new SmartCacheManager<AbstractSmartCache, K, V>(this).startSmartCacheMBeanService();
        }
    }

    /**
     * Checks whether the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache} is empty.
     *
     * @return {@code true} if empty, otherwise {@code false}
     */
    @Override
    public boolean isEmpty() {
        return SMART_CACHE_DATA.isEmpty();
    }

    /**
     * Checks whether the specified key is associated with any value in the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
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
     * Checks whether the specified value is associated with any key in the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
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
     * Put the Data of type {@link V} into the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}, corresponding to the Key of type {@link K}
     *
     * @param key  Any Key of type {@link K}
     * @param data Any Data of type {@link V}
     */
    @Override
    @Deprecated
    public void put(K key, V data) throws NullPointerException {
        SMART_CACHE_DATA.put(key, data);
        if (smartCacheEventListener != null)
            smartCacheEventListener.onCreateCacheEntry(key, data);
    }

    /**
     * Put the Data of type {@link V} into the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}, corresponding to the Key of type {@link K}
     *
     * @param key      Any Key of type
     * @param data     Any Data of type
     * @param ttl      the ttl value - The after which data will be auto deleted from the Cache
     * @param timeUnit the time unit for the TTL Value
     */
    @Override
    public void put(K key, V data, int ttl, TimeUnit timeUnit) {
        SMART_CACHE_DATA.put(key, data);
        if (startAutoCleaner)
            TASK_HOLDER.put(key, TASK_EXECUTOR.schedule(new AutoCleanerTask(key), ttl, timeUnit));
        else {
            ConcurrentHashMap<String, Object> temp = new ConcurrentHashMap<String, Object>();
            temp.put("TTL", ttl);
            temp.put("TIME_UNIT", timeUnit);
            temp.put("CURRENT_NANO", System.nanoTime());
            NON_SCHEDULED_TASKS.put(key, temp);
        }
        if (smartCacheEventListener != null)
            smartCacheEventListener.onCreateCacheEntry(key, data);
    }

    /**
     * Put the Data of type {@link V} into the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}, corresponding to the Key of type {@link K}
     *
     * @param key  Any Key of type {@link K}
     * @param data Any Data of type {@link V}
     * @param ttl  the ttl value - The after which data will be auto deleted from the Cache
     */
    @Override
    public void put(K key, V data, int ttl) throws NullPointerException {
        put(key, data, ttl, TimeUnit.SECONDS);
    }

    /**
     * Get the Data corresponding to the given Key from the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @param key The Key of type {@link K}
     *
     * @return The Data of type {@link V}
     */
    @Override
    public V get(K key) throws NullPointerException {
        V data;
        if ((data = SMART_CACHE_DATA.get(key)) != null) {
            return data;
        } else {
            return null;
        }
    }

    /**
     * Removes the Data corresponding to the given Key from the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @param key    The Key of type
     * @param reason the reason
     *
     * @return The Data of type
     *         that was removed
     */
    @Override
    public V remove(K key, String reason) {
        V data;
        if ((data = SMART_CACHE_DATA.remove(key)) != null) {
            if (data instanceof SmartCachePojo) {
                HISTORY.addToHistory(reason, key, (SmartCachePojo) data);
            }
            if (smartCacheEventListener != null)
                smartCacheEventListener.onDeleteCacheEntry(key, data);
            return data;
        } else {
            return null;
        }
    }

    /**
     * Puts an entire Map into the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @param dataMap Map of type {@link java.util.concurrent.ConcurrentMap} having Key of type {@link K} and Data of type {@link V}
     */
    @Override
    @Deprecated
    public void putAll(ConcurrentMap<K, V> dataMap) {
        SMART_CACHE_DATA.putAll(dataMap);
    }

    /**
     * Gets an entire Map from the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @return dataMap Map of type {@link java.util.concurrent.ConcurrentMap} having Key of type {@link K} and Data of type {@link V}
     */
    @Override
    public ConcurrentMap<K, V> getAll() {

        return SMART_CACHE_DATA;
    }

    /**
     * Removes an entire Map from the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache} and returns it
     *
     * @return dataMap Map of type {@link java.util.concurrent.ConcurrentMap} having Key of type {@link K} and Data of type {@link V}
     */
    @Override
    public ConcurrentMap<K, V> removeAll(String reason) {

        ConcurrentMap<K, V> tempData = SMART_CACHE_DATA;
        SMART_CACHE_DATA.clear();
        HISTORY.addAllToHistory(reason, tempData);
        return tempData;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are reflected in the set, and vice-versa.
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
     * and vice-versa. The collection supports element removal,
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
    public int size() {
        return SMART_CACHE_DATA.size();
    }

    /**
     * Gets SMART_CACHE_DATA data structure that holds all of the SmartCache entries
     *
     * @return the SMART_CACHE_DATA
     */
    public ConcurrentHashMap<K, V> getSMART_CACHE_DATA() {
        return SMART_CACHE_DATA;
    }

    /**
     * Add smart cache events listener.
     *
     * @param smartCacheEventListener the smart cache event listener
     */
    @Override
    public void addSmartCacheEventsListener(SmartCacheEventListener smartCacheEventListener) {
        this.smartCacheEventListener = smartCacheEventListener;
    }

    /**
     * Nothing to do here!! Only returns <code>false</code>
     */
    @Override
    @Deprecated
    public boolean startAutoCleaner(long EXPIRY_DURATION, long START_TASK_DELAY, long REPEAT_TASK_DELAY, TimeUnit TIME_UNIT, Object CALLBACK_CLASS_OBJECT, Method CALLBACK_METHOD) throws SmartCacheException {
        return false;
    }

    /**
     * Start auto cleaner.
     * This method starts the auto cleaner service and deletes the entries which has been timed out.
     * It is HIGHLY RECOMMENDED to use either this {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAutoCleaner(com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener)} method,
     * or the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAutoCleaner()} method for the cleanup process.
     *
     * @param smartCacheEventListener the smart cache event listener
     */
    @Override
    public void startAutoCleaner(SmartCacheEventListener smartCacheEventListener) {
        this.startAutoCleaner = true;
        this.smartCacheEventListener = smartCacheEventListener;

        // If any Cache entries were put into the Cache before the timer was started then
        // start the timer for them too
        if (!NON_SCHEDULED_TASKS.isEmpty()) {
            Set<K> keySet = NON_SCHEDULED_TASKS.keySet();
            ConcurrentHashMap<String, Object> temp;
            Integer ttl;
            TimeUnit timeUnit;
            Long putTime;
            long expiry;
            for (K key : keySet) {
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
     * Start auto cleaner.
     * This method starts the auto cleaner service and deletes the entries which has been timed out.
     * It is HIGHLY RECOMMENDED to use either the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAutoCleaner(com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener)} method,
     * or this {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAutoCleaner()} method for the cleanup process.
     */
    @Override
    public void startAutoCleaner() {
        startAutoCleaner(null);
    }

    /**
     * Stops the ongoing Auto Cleaner Service for the Cache data corresponding to a specific key.
     * This only works if you the Cache entries were made using the methods:
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#put(Object, Object, int, java.util.concurrent.TimeUnit)} and
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#put(Object, Object, int)} methods.
     * Also, the autoCleaner for this purpose must have been initiated using the one of the following methods:
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAutoCleaner()} or
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAutoCleaner(com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener)}.
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

        if (canceledTasks.getAndIncrement() >= 100) {
            TASK_EXECUTOR.purge();
            canceledTasks.set(0);
        }
    }

    /**
     * Stops the ongoing Auto Cleaner Service for the Cache data corresponding to all keys.
     * This only works if you the Cache entries were made using the methods:
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#put(Object, Object, int, java.util.concurrent.TimeUnit)} and
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#put(Object, Object, int)} methods.
     * Also, the autoCleaner for this purpose must have been initiated using the one of the following methods:
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAutoCleaner()} or
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#startAutoCleaner(com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener)}.
     *
     * @param removeEntry if <code>true</code> then the entry corresponding to this key is removed
     *                    from the Cache after stopping its cleanup task.
     */
    @Override
    public void stopAllAutoCleaner(boolean removeEntry) {
        Set<K> keySet = TASK_HOLDER.keySet();
        for (K key : keySet) {
            TASK_HOLDER.get(key).cancel(true);
            if (removeEntry)
                remove(key, EXPIRED);
        }
        TASK_EXECUTOR.purge();
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
        PURGE_EXECUTOR.execute(new PurgerClass(key));
        return false;
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
    public boolean purgeCacheEntry(Set<K> keys) {
        PURGE_EXECUTOR.execute(new PurgerClass(keys));
        return true;
    }

    /**
     * Purge the entire cache for backup purpose. Invoking this method will give a
     * callback to {@link com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener#onCachePurge(java.util.Map)}.
     *
     * @return the <code>true</code> if everything goes fine else <code>false</code>.
     */
    @Override
    public boolean purgeCacheEntries() {
        return purgeCacheEntry(keySet());
    }

    public String getCacheName() {
        return cacheName;
    }

    private final class PurgerClass implements Runnable {

        Set<K> keys = keySet();

        /**
         * Instantiates a new Purger class.
         *
         * @param keys the keys
         */
        public PurgerClass(Set<K> keys) {
            this.keys = keys;
        }

        /**
         * Instantiates a new Purger class.
         *
         * @param key the key
         */
        public PurgerClass(K key) {
            keys = new HashSet<K>(1);
            keys.add(key);
        }

        @Override
        public void run() {
            Map<K, V> cacheEntries = new HashMap<K, V>();
            if (smartCacheEventListener != null) {
                for (K key : keys) {
                    V value = SMART_CACHE_DATA.remove(key);
                    cacheEntries.put(key, value);
                    if (value instanceof SmartCachePojo) {
                        HISTORY.addToHistory(PURGED, key, (SmartCachePojo) value);
                    }
                }
                smartCacheEventListener.onCachePurge(cacheEntries);
            }
        }
    }

    private final class AutoCleanerTask implements Callable<V> {

        K key;

        AutoCleanerTask(K key) {
            this.key = key;
        }

        @Override
        public V call() throws Exception {
            return remove(key, EXPIRED);
        }
    }
}
