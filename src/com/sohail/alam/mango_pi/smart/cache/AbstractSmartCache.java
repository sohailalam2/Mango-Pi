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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

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
 * when expired. Also, {@link com.sohail.alam.mango_pi.smart.cache.SmartCache#stopAutoCleaner()} method is
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
public abstract class AbstractSmartCache<K, V extends SmartCachePojo, E extends SmartCacheEventListener> implements SmartCache<K, V, E> {

    private final ConcurrentHashMap<K, V> SMART_CACHE_DATA;
    private SmartCacheEventListener smartCacheEventListener = null;

    /**
     * Instantiates a new {@link DefaultSmartCache}
     *
     * @param activateMBean This indicates whether to activate the SmartCache MBean.
     *
     * @throws SmartCacheException Throws any SmartCacheException whatsoever.
     */
    public AbstractSmartCache(boolean activateMBean) throws SmartCacheException {
        SMART_CACHE_DATA = new ConcurrentHashMap<K, V>();
        if (activateMBean) {
            new SmartCacheMBean<AbstractSmartCache>(this).startService();
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
    public void put(K key, V data) throws NullPointerException {
        SMART_CACHE_DATA.put(key, data);
        if (smartCacheEventListener != null)
            smartCacheEventListener.onCreateCacheEntry(data);
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
     * @param key The Key of type {@link K}
     *
     * @return The Data of type {@link V} that was removed
     */
    @Override
    public V remove(K key) throws NullPointerException {
        V data;
        if ((data = SMART_CACHE_DATA.remove(key)) != null) {
            if (smartCacheEventListener != null)
                smartCacheEventListener.onDeleteCacheEntry(data);
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
    public ConcurrentMap<K, V> removeAll() {

        ConcurrentMap<K, V> tempData = SMART_CACHE_DATA;
        SMART_CACHE_DATA.clear();
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
    public void addSmartCacheEventsListener(E smartCacheEventListener) {
        this.smartCacheEventListener = smartCacheEventListener;
    }

    /**
     * Nothing to do here!! Only returns <code>false</code>
     */
    @Override
    public boolean startAutoCleaner(long EXPIRY_DURATION, long START_TASK_DELAY, long REPEAT_TASK_DELAY, TimeUnit TIME_UNIT, Object CALLBACK_CLASS_OBJECT, Method CALLBACK_METHOD) throws SmartCacheException {
        return false;
    }
}
