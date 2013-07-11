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
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
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
     * Checks whether the specified key is associated with any value in the {@link SmartCache}
     *
     * @param key The Key of type
     *
     * @return if present, otherwise
     */
    boolean containsKey(K key);

    /**
     * Checks whether the specified value is associated with any key in the {@link SmartCache}
     *
     * @param value The Key of type
     *
     * @return if present, otherwise
     */
    boolean containsValue(V value);

    /**
     * Checks whether the {@link SmartCache} is empty.
     *
     * @return if empty, otherwise
     */
    boolean isEmpty();

    /**
     * Put the Data of type {@link V} into the {@link SmartCache}, corresponding to the Key of type {@link K}
     * <p/>
     * <strong>NOTE:</strong>
     * This method has been deprecated and it is HIGHLY RECOMMENDED to use {@link SmartCache#put(Object, Object, int)}
     * method to insert entries into the Cache with its own TTL value which can be different for
     * different entries. This can be used with the {@link SmartCache#startAutoCleaner()},
     * or {@link SmartCache#startAutoCleaner(SmartCacheEventListener)} method to delete the specific entry corresponding to its TTL value.
     *
     * @param key  Any Key of type
     * @param data Any Data of type
     */
    @Deprecated
    void put(K key, V data);

    /**
     * Put the Data of type {@link V} into the {@link SmartCache}, corresponding to the Key of type {@link K}
     *
     * @param key      Any Key of type
     * @param data     Any Data of type
     * @param ttl      the ttl value - The after which data will be auto deleted from the Cache
     * @param timeUnit the time unit for the TTL Value
     */
    void put(K key, V data, int ttl, TimeUnit timeUnit);

    /**
     * Put the Data of type {@link V} into the {@link SmartCache}, corresponding to the Key of type {@link K}
     *
     * @param key  Any Key of type
     * @param data Any Data of type
     * @param ttl  the ttl value - The after which data will be auto deleted from the Cache
     *             (By default it takes SECOND as TimeUnit)
     */
    void put(K key, V data, int ttl);

    /**
     * Get the Data corresponding to the given Key from the {@link SmartCache}
     *
     * @param key The Key of type
     *
     * @return The Data of type
     */
    V get(K key);

    /**
     * Removes the Data corresponding to the given Key from the {@link SmartCache}
     *
     * @param key    The Key of type
     * @param reason the reason
     *
     * @return The Data of type that was removed
     */
    V remove(K key, String reason);

    /**
     * Puts an entire Map into the {@link SmartCache}
     * This method has been deprecated as it does not allow to set individual TTL Values
     *
     * @param dataMap Map of type having Key of type and Data of type
     */
    @Deprecated
    void putAll(ConcurrentMap<K, V> dataMap);

    /**
     * Gets an entire Map from the {@link SmartCache}
     *
     * @return dataMap Map of type having Key of type and Data of type
     */
    ConcurrentMap<K, V> getAll();

    /**
     * Removes an entire Map from the {@link SmartCache} and returns it
     *
     * @param reason the reason
     *
     * @return dataMap Map of type having Key of type and Data of type
     */
    ConcurrentMap<K, V> removeAll(String reason);

    /**
     * Returns a Set view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are reflected in the set, and vice-versa.
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
     * and vice-versa. The collection supports element removal,
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
    int size();

    /**
     * Sets up an Auto Cleaner Service which will delete entries from the {@link SmartCache} System on expiry of its TTL (time to live)
     * <p/>
     * <strong>NOTE:</strong>
     * This method has been deprecated. It is HIGHLY RECOMMENDED that you use {@link SmartCache#startAutoCleaner()},
     * or {@link SmartCache#startAutoCleaner(SmartCacheEventListener)} method to delete the specific entry corresponding to its TTL value.
     *
     * @param EXPIRY_DURATION       The TTL value after which the entries will be deleted from the Smart Cache
     * @param START_TASK_DELAY      The initial delay after which this Auto Cleaner service starts
     * @param REPEAT_TASK_DELAY     The delay after which this Auto Cleaner service repeats itself
     * @param TIME_UNIT             The Unit of time for the previous parameters
     * @param CALLBACK_CLASS_OBJECT The Class Object which contains the callback method, if
     *                              then no callback is made
     * @param CALLBACK_METHOD       The callback method, if null then no callback is provided.
     *                              NOTE: The Callback method must accept only one parameter of type
     *
     * @return if the Auto Cleaner Service was setup correctly, otherwise returns
     *
     * @throws SmartCacheException the smart cache exception
     */
    @Deprecated
    boolean startAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT,
                             final Object CALLBACK_CLASS_OBJECT, final Method CALLBACK_METHOD) throws SmartCacheException;

    /**
     * Sets up an Auto Cleaner Service which will delete entries from the {@link SmartCache} System on expiry of its TTL (time to live)
     * <p/>
     * <strong>NOTE:</strong>
     * This method has been deprecated. It is HIGHLY RECOMMENDED that you use {@link SmartCache#startAutoCleaner()},
     * or {@link SmartCache#startAutoCleaner(SmartCacheEventListener)} method to delete the specific entry corresponding to its TTL value.
     *
     * @param EXPIRY_DURATION   The TTL value after which the entries will be deleted from the Smart Cache
     * @param START_TASK_DELAY  The initial delay after which this Auto Cleaner service starts
     * @param REPEAT_TASK_DELAY The delay after which this Auto Cleaner service repeats itself
     * @param TIME_UNIT         The Unit of time for the previous parameters
     *
     * @return if the Auto Cleaner Service was setup correctly, otherwise returns
     *
     * @throws SmartCacheException the smart cache exception
     */
    @Deprecated
    boolean startAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT) throws SmartCacheException;

    /**
     * Sets up an Auto Cleaner Service which will delete entries from the {@link SmartCache} System on expiry of its TTL (time to live)
     * <p/>
     * <strong>NOTE:</strong>
     * This method has been deprecated. It is HIGHLY RECOMMENDED that you use {@link SmartCache#startAutoCleaner()},
     * or {@link SmartCache#startAutoCleaner(SmartCacheEventListener)} method to delete the specific entry corresponding to its TTL value.
     *
     * @param EXPIRY_DURATION         The TTL value after which the entries will be deleted from the Smart Cache
     * @param START_TASK_DELAY        The initial delay after which this Auto Cleaner service starts
     * @param REPEAT_TASK_DELAY       The delay after which this Auto Cleaner service repeats itself
     * @param TIME_UNIT               The Unit of time for the previous parameters
     * @param smartCacheEventListener the smart cache event listener
     *
     * @return if the Auto Cleaner Service was setup correctly, otherwise returns
     *
     * @throws SmartCacheException the smart cache exception
     */
    @Deprecated
    boolean startAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT, final SmartCacheEventListener smartCacheEventListener) throws SmartCacheException;

    /**
     * Start auto cleaner.
     * This method starts the auto cleaner service and deletes the entries which has been timed out.
     * It is HIGHLY RECOMMENDED to use either this {@link SmartCache#startAutoCleaner(SmartCacheEventListener)} method,
     * or the {@link SmartCache#startAutoCleaner()} method for the cleanup process.
     *
     * @param smartCacheEventListener the smart cache event listener
     */
    void startAutoCleaner(SmartCacheEventListener smartCacheEventListener);

    /**
     * Start auto cleaner.
     * This method starts the auto cleaner service and deletes the entries which has been timed out.
     * It is HIGHLY RECOMMENDED to use either the {@link SmartCache#startAutoCleaner(SmartCacheEventListener)} method,
     * or this {@link SmartCache#startAutoCleaner()} method for the cleanup process.
     */
    void startAutoCleaner();


    /**
     * Stops the ongoing Auto Cleaner Service for the Cache data corresponding to a specific key.
     * This only works if you the Cache entries were made using the methods:
     * {@link SmartCache#put(Object, Object, int, java.util.concurrent.TimeUnit)} and
     * {@link SmartCache#put(Object, Object, int)} methods.
     * Also, the autoCleaner for this purpose must have been initiated using the one of the following methods:
     * {@link SmartCache#startAutoCleaner()} or
     * {@link SmartCache#startAutoCleaner(SmartCacheEventListener)}.
     *
     * @param key         the key
     * @param removeEntry if <code>true</code> then the entry corresponding to this key is removed
     *                    from the Cache after stopping its cleanup task.
     */
    void stopAutoCleaner(K key, boolean removeEntry);

    /**
     * Stops the ongoing Auto Cleaner Service for the Cache data corresponding to all keys.
     * This only works if you the Cache entries were made using the methods:
     * {@link SmartCache#put(Object, Object, int, java.util.concurrent.TimeUnit)} and
     * {@link SmartCache#put(Object, Object, int)} methods.
     * Also, the autoCleaner for this purpose must have been initiated using the one of the following methods:
     * {@link SmartCache#startAutoCleaner()} or
     * {@link SmartCache#startAutoCleaner(SmartCacheEventListener)}.
     *
     * @param removeEntry if <code>true</code> then the entry corresponding to this key is removed
     *                    from the Cache after stopping its cleanup task.
     */
    void stopAllAutoCleaner(boolean removeEntry);

    /**
     * Stops the ongoing Auto Cleaner Service
     * This method has been deprecated. It is HIGHLY RECOMMENDED to use the,
     * {@link SmartCache#stopAllAutoCleaner(boolean)} ()} or
     * the {@link SmartCache#stopAutoCleaner(Object, boolean)} methods.
     */
    @Deprecated
    void stopAutoCleaner();

    /**
     * Add smart cache events listener.
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
    public boolean purgeCacheEntries();

    /**
     * Purges only the data corresponding to the given KEY.
     * Invoking this method will give a callback to the
     * {@link SmartCacheEventListener#onSingleEntryPurge(Object, SmartCachePojo)}.
     *
     * @param key the KEY
     *
     * @return the <code>true</code> if everything goes fine else <code>false</code>.
     */
    public boolean purgeCacheEntry(K key);

    /**
     * Purges only the data corresponding to the given set of KEYs.
     * Invoking this method will give a callback to the
     * {@link SmartCacheEventListener#onCachePurge(java.util.Map)}.
     *
     * @param keys the keys
     *
     * @return the boolean
     */
    public boolean purgeCacheEntry(Set<K> keys);

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
