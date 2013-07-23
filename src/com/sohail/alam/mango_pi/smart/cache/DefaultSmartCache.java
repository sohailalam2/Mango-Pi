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

import com.sohail.alam.mango_pi.smart.cache.mbeans.DefaultSmartCacheManager;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.sohail.alam.mango_pi.smart.cache.SmartCacheHistoryImpl.SMART_CACHE_HISTORY;

/**
 * User: Sohail Alam
 * Version: 1.1.6
 * Date: 9/6/13
 * Time: 1:14 AM
 */
public class DefaultSmartCache<K, V extends SmartCachePojo>
        extends AbstractSmartCache<K, V>
        implements SmartCacheHistory<K, V> {

    private AtomicLong totalCacheSize;

    /**
     * Instantiates a new {@link DefaultSmartCache}
     *
     * @param cacheName     the cache name (must be unique if more than one Smart Cache
     *                      is instantiated in the application)
     * @param activateMBean This indicates whether to activate the SmartCache MBean.
     *
     * @throws SmartCacheException Throws any SmartCacheException whatsoever.
     */
    public DefaultSmartCache(String cacheName, boolean activateMBean) throws SmartCacheException {
        super(cacheName, false);
        totalCacheSize = new AtomicLong(0);// This represents the total numberOfEntries of the Cache in bytes
        if (activateMBean) {
            new DefaultSmartCacheManager<DefaultSmartCache, K, V>(this).startSmartCacheMBeanService();
        }
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
        super.put(key, data, ttl, timeUnit);
        incrementTotalCacheSize(data.size());
    }

    /**
     * Put the Data of type {@link V} into the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache},
     * corresponding to the Key of type {@link K}
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
        decrementTotalCacheSize(get(key).size());
        SMART_CACHE_HISTORY.addToHistory(reason, key, get(key));
        return super.remove(key, reason);
    }

    /**
     * Removes an entire Map from the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache} and returns it
     *
     * @return dataMap Map of type {@link java.util.concurrent.ConcurrentMap} having Key of type {@link K} and Data of type {@link V}
     */
    @Override
    public ConcurrentMap<K, V> removeAll(String reason) {
        for (K key : keySet()) {
            decrementTotalCacheSize(get(key).size());
        }
        return super.removeAll(reason);
    }

    /**
     * Get the total numberOfEntries of the data stored in Smart Cache
     *
     * @return The total numberOfEntries of the Smart Cache
     */
    @Override
    public long totalCacheSize() {
        return totalCacheSize.get();
    }

    /**
     * Increment the total numberOfEntries of the data stored in Smart Cache by the given amount
     *
     * @return The total numberOfEntries of the Smart Cache after increment
     */
    private long incrementTotalCacheSize(long size) {
        return totalCacheSize.addAndGet(size);
    }

    /**
     * Decrement the total numberOfEntries of the data stored in Smart Cache by the given amount
     *
     * @return The total numberOfEntries of the Smart Cache after decrement
     */
    private long decrementTotalCacheSize(long size) {
        return totalCacheSize.addAndGet(-size);
    }

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     * corresponding to the given KEY
     *
     * @param key the KEY
     *
     * @return Smart Cache Info
     */
    @Override
    public String smartCacheKeyInfo(K key) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%-20s", "Creation Time"));
        builder.append(String.format("%-20s", "Key"));
        builder.append(String.format("%-50s", "Value"));
        builder.append(SmartCacheUtils.createLine(90, '-'));
        V data = get(key);
        builder.append(String.format("%-20s", data.getCREATION_TIME()));
        builder.append(String.format("%-20s", key.toString()));
        builder.append(String.format("%-50s", data.toString()));
        builder.append(SmartCacheUtils.createLine(90, '-'));
        return builder.toString();
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
    public String smartCacheInfo(Set<K> keys) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%-20s", "Creation Time"));
        builder.append(String.format("%-20s", "Key"));
        builder.append(String.format("%-50s", "Value"));
        builder.append(SmartCacheUtils.createLine(90, '-'));
        // Iterate the SmartCache for all keys
        for (K key : keys) {
            V data = get(key);
            builder.append(String.format("%-20s", data.getCREATION_TIME()));
            builder.append(String.format("%-20s", key.toString()));
            builder.append(String.format("%-50s", data.toString()));
            builder.append("\n");
        }
        builder.append(SmartCacheUtils.createLine(90, '-'));
        return builder.toString();
    }

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     *
     * @return Smart Cache Info
     */
    @Override
    public String smartCacheFullInfo() {
        return smartCacheInfo(keySet());
    }

    /**
     * Add to history.
     *
     * @param reason the reason
     * @param key    the key
     * @param value  the value
     */
    @Override
    public void addToHistory(String reason, K key, V value) {
        SMART_CACHE_HISTORY.addToHistory(reason, key, value);
    }

    /**
     * Add all to history.
     *
     * @param reason  the reason
     * @param dataMap the data map
     */
    @Override
    public void addAllToHistory(String reason, ConcurrentMap<K, V> dataMap) {
        SMART_CACHE_HISTORY.addAllToHistory(reason, dataMap);
    }

    /**
     * View history.
     *
     * @param key the key
     *
     * @return the string
     */
    @Override
    public String smartCacheKeyHistory(K key) {
        return SMART_CACHE_HISTORY.smartCacheKeyHistory(key);
    }

    /**
     * View history.
     *
     * @param reason the reason
     *
     * @return the string
     */
    @Override
    public String smartCacheReasonHistory(String reason) {
        return SMART_CACHE_HISTORY.smartCacheReasonHistory(reason);
    }

    /**
     * View history.
     *
     * @return the string
     */
    @Override
    public String smartCacheAllHistory() {
        return SMART_CACHE_HISTORY.smartCacheAllHistory();
    }

    /**
     * Set the maximum number of entries after which the History is
     * deleted permanently.
     *
     * @param maxElementCount the max element count
     */
    @Override
    public void maxHistoryCount(int maxElementCount) {
        SMART_CACHE_HISTORY.maxHistoryCount(maxElementCount);
    }

    /**
     * Purges the contents of History into a user defined file.
     * By default the SmartCache will dump the data into a file named -
     * SmartCacheHistory_(current-date/time).txt
     *
     * @param filePath the absolute file path for the dump file.
     */
    @Override
    public String purgeSmartCacheHistory(String filePath) throws Exception {
        return SMART_CACHE_HISTORY.purgeSmartCacheHistory(filePath);
    }
}
