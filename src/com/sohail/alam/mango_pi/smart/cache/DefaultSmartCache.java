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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * this concrete class {@link DefaultSmartCache} extends {@link AbstractSmartCache} and
 * implements the {@link SmartCache#startAutoCleaner(long, long, long, java.util.concurrent.TimeUnit, Object, java.lang.reflect.Method)}
 * method, which checks the TTL value for each elements stored in the {@link SmartCache} and removes it
 * when expired.
 * </p>
 * <p>
 * But in order for this to work, your Data Structure must implement the {@link SmartCachePojo}, which
 * automatically sets a creation time {@link SmartCachePojo#TIME_STAMP}, which is then checked against the
 * current time and the TTL value that you should have entered while enabling the
 * {@link SmartCache#startAutoCleaner(long, long, long, java.util.concurrent.TimeUnit, Object, java.lang.reflect.Method)}.
 * </p>
 * User: Sohail Alam
 * Version: 1.1.6
 * Date: 9/6/13
 * Time: 12:27 AM
 */
public class DefaultSmartCache<K, V extends SmartCachePojo> extends AbstractSmartCache<K, V> {

    private Executors autoCleanerExecutor;
    private ScheduledExecutorService autoCleanerService = null;
    private AtomicLong deletedEntriesCounter = new AtomicLong(0);

    /**
     * Instantiates a new {@link DefaultSmartCache}
     */
    public DefaultSmartCache() {
        super();
    }

    /**
     * Sets up an Auto Cleaner Service which will delete entries from the {@link com.sohail.alam.mango_pi.smart.cache.SmartCache} System on expiry of its TTL (time to live)
     *
     * @param EXPIRY_DURATION       The TTL value after which the entries will be deleted from the Smart Cache
     * @param START_TASK_DELAY      The initial delay after which this Auto Cleaner service starts
     * @param REPEAT_TASK_DELAY     The delay after which this Auto Cleaner service repeats itself
     * @param TIME_UNIT             The Unit of time for the previous parameters
     * @param CALLBACK_CLASS_OBJECT The Class Object which contains the callback method, if {@code null} then no callback is made
     * @param CALLBACK_METHOD       The callback method, if null then no callback is provided.
     *                              NOTE: The Callback method must accept only one parameter of type {@code V extends SmartCachePojo}
     *
     * @return {@code true} if the Auto Cleaner Service was setup correctly, otherwise returns {@code false}
     */
    @Override
    public boolean startAutoCleaner(long EXPIRY_DURATION, long START_TASK_DELAY, long REPEAT_TASK_DELAY, TimeUnit TIME_UNIT,
                                    final Object CALLBACK_CLASS_OBJECT, final Method CALLBACK_METHOD) {

        final long expiry = TimeUnit.NANOSECONDS.convert(EXPIRY_DURATION, TIME_UNIT);

        autoCleanerService = autoCleanerExecutor.newSingleThreadScheduledExecutor();
        try {
            autoCleanerService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    for (K key : SMART_CACHE_DATA.keySet()) {
                        if ((System.nanoTime() - SMART_CACHE_DATA.get(key).TIME_STAMP) >= expiry) {
                            // Increment the deletedEntriesCounter
                            deletedEntriesCounter.incrementAndGet();
                            // Delete and Provide Callback if needed
                            if (CALLBACK_CLASS_OBJECT != null && CALLBACK_METHOD != null) {
                                try {
                                    CALLBACK_METHOD.invoke(CALLBACK_CLASS_OBJECT, SMART_CACHE_DATA.remove(key));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                SMART_CACHE_DATA.remove(key);
                            }
                        }
                    }
                }
            }, START_TASK_DELAY, REPEAT_TASK_DELAY, TIME_UNIT);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Stops the ongoing Auto Cleaner Service
     */
    @Override
    public void stopAutoCleaner() throws SecurityException {
        if (autoCleanerService != null)
            autoCleanerService.shutdown();
    }

    /**
     * The Deleted Entries Counter increments whenever an item expires its TTL and is deleted from the
     * {@link SmartCache}.
     *
     * @return The {@code long} value of {@code deletedEntriesCounter}
     */
    public long getDeletedEntriesCounter() {
        return deletedEntriesCounter.get();
    }

    /**
     * Resets the {@code deletedEntriesCounter} back to zero.
     */
    public void resetDeletedEntriesCounter() {
        deletedEntriesCounter = new AtomicLong(0);
    }
}
