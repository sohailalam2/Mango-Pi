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

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * This {@link SmartCache} interface exposes all the methods that this Smart Caching System is capable of doing.
 * If you want to write your own implementation of these method, feel free to do so. Just implement this {@link SmartCache }interface
 * and you will be ready with all method prototypes.
 * </p>
 * User: Sohail Alam
 * Version: 1.1.6
 * Date: 8/6/13
 * Time: 11:53 PM
 */
public interface SmartCache<K, V> {

    /**
     * Checks whether the specified key is associated with any value in the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @param key The Key of type {@link K}
     *
     * @return {@code true} if present, otherwise {@code false}
     */
    boolean containsKey(K key);

    /**
     * Checks whether the specified value is associated with any key in the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @param value The Key of type {@link V}
     *
     * @return {@code true} if present, otherwise {@code false}
     */
    boolean containsValue(V value);

    /**
     * Checks whether the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache} is empty.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    boolean isEmpty();

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
    void put(K key, V data, int ttl, TimeUnit timeUnit);

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
    void put(K key, V data, int ttl);

    /**
     * Get the Data corresponding to the given Key from the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @param key The Key of type {@link K}
     *
     * @return The Data of type {@link V}
     */
    V get(K key);

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
    V remove(K key, String reason);

    /**
     * Gets a copy of the entire Map from the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache}
     *
     * @return dataMap Map of type {@link java.util.concurrent.ConcurrentMap}
     *         having Key of type {@link K} and Data of type {@link V}
     */
    ConcurrentMap<K, V> copy();

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
    ConcurrentMap<K, V> removeAll(String reason);

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
    Set<K> keySet();

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
    Collection<V> values();

    /**
     * Get the number of key-value mappings in this map.
     * If the map contains more than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
     *
     * @return The number of key-value mappings in this map
     */
    int numberOfEntries();

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
    void startAllAutoCleaner(SmartCacheEventListener smartCacheEventListener);

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
    void startAllAutoCleaner();

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
    void stopAutoCleaner(K key, boolean removeEntry);

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
    void stopAllAutoCleaner(boolean removeEntry);

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
    void restartAutoCleaner(K key);

    /**
     * Adds a {@link SmartCacheEventListener} or replaces an existing one.
     * <p/>
     * If a listener is attached to the {@link SmartCache} instance, then
     * {@link SmartCache} can provide automatic callbacks on various internal events.
     *
     * @param smartCacheEventListener the smart cache event listener
     */
    void addSmartCacheEventsListener(SmartCacheEventListener smartCacheEventListener);

    /**
     * Purge the entire cache for backup purpose. Invoking this method will give a
     * callback to {@link SmartCacheEventListener#onCachePurge(java.util.Map)}.
     *
     * @return the <code>true</code> if everything goes fine else <code>false</code>.
     */
    public boolean purgeAllCacheEntries() throws ExecutionException, InterruptedException;

    /**
     * Purges only the data corresponding to the given set of KEYs.
     * Invoking this method will give a callback to the
     * {@link SmartCacheEventListener#onCachePurge(java.util.Map)}.
     *
     * @param keys the keys
     *
     * @return the boolean
     */
    public boolean purgeCacheEntries(Set<K> keys) throws ExecutionException, InterruptedException;

    /**
     * Purges only the data corresponding to the given KEY.
     * Invoking this method will give a callback to the
     * {@link SmartCacheEventListener#onSingleEntryPurge(Object, Object)}.
     *
     * @param key the KEY
     *
     * @return the <code>true</code> if everything goes fine else <code>false</code>.
     */
    public boolean purgeCacheEntry(K key);

    /**
     * Get the total numberOfEntries of the data stored in Smart Cache
     *
     * @return The total numberOfEntries of the Smart Cache
     */
    public long totalCacheSize();

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     * corresponding to the given KEY
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
     * Returns a formatted String that holds the information about the Smart Cache
     *
     * @return Smart Cache Info
     */
    public String smartCacheFullInfo();

    /**
     * The interface containing some of the possible reasons for deleting the Cache entry
     */
    public interface SmartCacheDeleteReason {

        /**
         * Cache entry was deleted due to the following reason: DELETED_BY_USER.
         */
        public static final String DELETED_BY_USER = "DELETED_BY_USER";
        /**
         * Cache entry was deleted due to the following reason: ERROR.
         */
        public static final String ERROR = "ERROR";
        /**
         * Cache entry was deleted due to the following reason: EXCEPTION.
         */
        public static final String EXCEPTION = "EXCEPTION";
        /**
         * Cache entry was deleted due to the following reason: EXPIRED.
         */
        public static final String EXPIRED = "EXPIRED";
        /**
         * Cache entry was deleted due to the following reason: PURGED.
         */
        public static final String PURGED = "PURGED";
        /**
         * Cache entry was deleted due to the following reason: SUCCESSFUL.
         */
        public static final String SUCCESSFUL = "SUCCESSFUL";
        /**
         * Cache entry was deleted due to the following reason: UNKNOWN.
         */
        public static final String UNKNOWN = "UNKNOWN";
        /**
         * Cache entry was deleted due to the following reason: UNSUCCESSFUL.
         */
        public static final String UNSUCCESSFUL = "UNSUCCESSFUL";
    }
}
