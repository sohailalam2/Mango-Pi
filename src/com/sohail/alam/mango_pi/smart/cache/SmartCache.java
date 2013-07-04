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
public interface SmartCache<K, V, E extends SmartCacheEventListener> {

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
     *
     * @param key  Any Key of type
     * @param data Any Data of type
     */
    void put(K key, V data);

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
     * @param key The Key of type
     *
     * @return The Data of type
     *         that was removed
     */
    V remove(K key);

    /**
     * Puts an entire Map into the {@link SmartCache}
     *
     * @param dataMap Map of type
     *                having Key of type
     *                and Data of type
     */
    void putAll(ConcurrentMap<K, V> dataMap);

    /**
     * Gets an entire Map from the {@link SmartCache}
     *
     * @return dataMap Map of type
     *         having Key of type
     *         and Data of type
     */
    ConcurrentMap<K, V> getAll();

    /**
     * Removes an entire Map from the {@link SmartCache} and returns it
     *
     * @return dataMap Map of type
     *         having Key of type
     *         and Data of type
     */
    ConcurrentMap<K, V> removeAll();

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
     */
    boolean startAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT,
                             final Object CALLBACK_CLASS_OBJECT, final Method CALLBACK_METHOD) throws SmartCacheException;

    /**
     * Sets up an Auto Cleaner Service which will delete entries from the {@link SmartCache} System on expiry of its TTL (time to live)
     *
     * @param EXPIRY_DURATION   The TTL value after which the entries will be deleted from the Smart Cache
     * @param START_TASK_DELAY  The initial delay after which this Auto Cleaner service starts
     * @param REPEAT_TASK_DELAY The delay after which this Auto Cleaner service repeats itself
     * @param TIME_UNIT         The Unit of time for the previous parameters
     *
     * @return if the Auto Cleaner Service was setup correctly, otherwise returns
     */
    boolean startAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT) throws SmartCacheException;

    /**
     * Sets up an Auto Cleaner Service which will delete entries from the {@link SmartCache} System on expiry of its TTL (time to live)
     *
     * @param EXPIRY_DURATION         The TTL value after which the entries will be deleted from the Smart Cache
     * @param START_TASK_DELAY        The initial delay after which this Auto Cleaner service starts
     * @param REPEAT_TASK_DELAY       The delay after which this Auto Cleaner service repeats itself
     * @param TIME_UNIT               The Unit of time for the previous parameters
     * @param smartCacheEventListener the smart cache event listener
     *
     * @return if the Auto Cleaner Service was setup correctly, otherwise returns
     */
    boolean startAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT, final SmartCacheEventListener smartCacheEventListener) throws SmartCacheException;

    /**
     * Stops the ongoing Auto Cleaner Service
     */
    void stopAutoCleaner();

    /**
     * Add smart cache events listener.
     *
     * @param smartCacheEventListener the smart cache event listener
     */
    void addSmartCacheEventsListener(E smartCacheEventListener);
}
