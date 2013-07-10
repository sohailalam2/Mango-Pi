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

import com.sohail.alam.mango_pi.jmx.wrapper.JMXBean;
import com.sohail.alam.mango_pi.jmx.wrapper.JMXBeanOperation;
import com.sohail.alam.mango_pi.jmx.wrapper.JMXBeanParameter;
import com.sohail.alam.mango_pi.utils.MBeanService;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 4/7/13
 * Time: 8:27 AM
 */
@JMXBean(description = "Smart Cache MBean")
public class SmartCacheManager<T extends AbstractSmartCache, K, V extends SmartCachePojo> implements SmartCacheManagerMBean<K> {

    private static String MBEAN_NAME;
    private final SmartCacheHistory HISTORY = SmartCacheHistoryImpl.HISTORY;
    private SmartCache cache;

    /**
     * Instantiates a new Smart cache manager.
     *
     * @param cache the cache
     */
    public SmartCacheManager(T cache) {
        if (cache == null)
            throw new NullPointerException("The Instance of the Cache can not be null");

        MBEAN_NAME = "MangoPI:Module=SmartCache-" + cache.getCacheName();
        this.cache = cache;
    }

    private SmartCacheManager() {

    }

    /**
     * Start Smart Cache MBean service
     *
     * @throws SmartCacheException the smart cache exception
     */
    public void startSmartCacheMBeanService() throws SmartCacheException {
        try {
            MBeanService.startService(this, MBEAN_NAME);
        } catch (Exception e) {
            throw new SmartCacheException("Smart Cache was unable to start the Smart Cache MBean Service: " + e.getMessage(), e);
        }
    }

    /**
     * Stop Smart Cache MBean service.
     *
     * @throws SmartCacheException the smart cache exception
     */
    @Override
    @JMXBeanOperation(name = "stopSmartCacheMBeanService", description = "Stop Smart Cache MBean service")
    public void stopSmartCacheMBeanService() throws SmartCacheException {
        try {
            MBeanService.stopService(MBEAN_NAME);
        } catch (Exception e) {
            throw new SmartCacheException("Smart Cache was unable to stop the Smart Cache MBean Service: " + e.getMessage(), e);
        }
    }

    /* ************************************************************************************
     *                              SMART cache ATTRIBUTES                                *
     * ************************************************************************************/

     /* ************************************************************************************
     *                               SMART cache OPERATIONS                                *
     * ************************************************************************************/

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
    @Override
    @JMXBeanOperation(name = "startAutoCleanerWithTimeUnit", description = "Start the auto cleaner service for the given Smart Cache (With a callback method, using Smart Cache Event Listener API)")
    public boolean startAutoCleanerWithTimeUnit(@JMXBeanParameter(name = "Expiry Duration", description = "The TTL Value for the Cache elements") final long EXPIRY_DURATION,
                                                @JMXBeanParameter(name = "Start Task Delay", description = "The delay after which the auto cleaner task starts") final long START_TASK_DELAY,
                                                @JMXBeanParameter(name = "Repeat Task Delay", description = "The delay after which the auto cleaner task repeats itself") final long REPEAT_TASK_DELAY,
                                                @JMXBeanParameter(name = "Time Unit", description = "The Time Unit in which the previous two parameters are defined") final TimeUnit TIME_UNIT) throws SmartCacheException {

        return this.cache.startAutoCleaner(EXPIRY_DURATION, START_TASK_DELAY, REPEAT_TASK_DELAY, TIME_UNIT);
    }

    /**
     * Start auto cleaner.
     *
     * @param EXPIRY_DURATION   the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY  The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY The time after which the auto cleaner task repeats itself
     *
     * @return the boolean
     *
     * @throws com.sohail.alam.mango_pi.smart.cache.SmartCacheException
     *          the smart cache exception
     */
    @Override
    @JMXBeanOperation(name = "startAutoCleaner", description = "Start the auto cleaner service for the given Smart Cache (With a callback method, using Smart Cache Event Listener API) [All are in seconds]")
    public boolean startAutoCleaner(@JMXBeanParameter(name = "Expiry Duration", description = "The TTL Value for the Cache elements (in seconds)") final long EXPIRY_DURATION,
                                    @JMXBeanParameter(name = "Start Task Delay", description = "The delay after which the auto cleaner task starts (in seconds)") final long START_TASK_DELAY,
                                    @JMXBeanParameter(name = "Repeat Task Delay", description = "The delay after which the auto cleaner task repeats itself (in seconds)") final long REPEAT_TASK_DELAY) throws SmartCacheException {
        return startAutoCleanerWithTimeUnit(EXPIRY_DURATION, START_TASK_DELAY, REPEAT_TASK_DELAY, TimeUnit.SECONDS);
    }

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
    @Override
    @JMXBeanOperation(name = "startAutoCleanerReflection", description = "Start the auto cleaner service for the given Smart Cache (With a callback method, using Java Reflection API)")
    public boolean startAutoCleanerReflection(@JMXBeanParameter(name = "Expiry Duration", description = "The TTL Value for the Cache elements") final long EXPIRY_DURATION,
                                              @JMXBeanParameter(name = "Start Task Delay", description = "The delay after which the auto cleaner task starts") final long START_TASK_DELAY,
                                              @JMXBeanParameter(name = "Repeat Task Delay", description = "The delay after which the auto cleaner task repeats itself") final long REPEAT_TASK_DELAY,
                                              @JMXBeanParameter(name = "Time Unit", description = "The Time Unit in which the previous two parameters are defined") final TimeUnit TIME_UNIT,
                                              @JMXBeanParameter(name = "Callback Class Object", description = "An object of the Callback Class (can be null)") final Object CALLBACK_CLASS_OBJECT,
                                              @JMXBeanParameter(name = "Callback Method", description = "The Callback Method (can be null)") final Method CALLBACK_METHOD) throws SmartCacheException {

        return this.cache.startAutoCleaner(EXPIRY_DURATION, START_TASK_DELAY, REPEAT_TASK_DELAY, TIME_UNIT, CALLBACK_CLASS_OBJECT, CALLBACK_METHOD);
    }

    /**
     * Stop auto cleaner.
     */
    @Override
    @JMXBeanOperation(name = "stopAutoCleaner", description = "Stop the auto cleaner service for the given Smart Cache")
    public void stopAutoCleaner() {
        this.cache.stopAutoCleaner();
    }

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
    @Override
    @JMXBeanOperation(name = "rescheduleAutoCleanerWithTimeUnit", description = "Restart the auto cleaner service for the given Smart Cache (With a callback method, using Smart Cache Event Listener API)")
    public boolean rescheduleAutoCleanerWithTimeUnit(@JMXBeanParameter(name = "Expiry Duration", description = "The TTL Value for the Cache elements") final long EXPIRY_DURATION,
                                                     @JMXBeanParameter(name = "Start Task Delay", description = "The delay after which the auto cleaner task starts") final long START_TASK_DELAY,
                                                     @JMXBeanParameter(name = "Repeat Task Delay", description = "The delay after which the auto cleaner task repeats itself") final long REPEAT_TASK_DELAY,
                                                     @JMXBeanParameter(name = "Time Unit", description = "The Time Unit in which the previous two parameters are defined") final TimeUnit TIME_UNIT) throws SmartCacheException {
        this.cache.stopAutoCleaner();
        return this.cache.startAutoCleaner(EXPIRY_DURATION, START_TASK_DELAY, REPEAT_TASK_DELAY, TIME_UNIT);
    }

    /**
     * Restart auto cleaner.
     *
     * @param EXPIRY_DURATION   the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY  The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY The time after which the auto cleaner task repeats itself
     *
     * @return the boolean
     *
     * @throws com.sohail.alam.mango_pi.smart.cache.SmartCacheException
     *          the smart cache exception
     */
    @Override
    @JMXBeanOperation(name = "rescheduleAutoCleaner", description = "Restart the auto cleaner service for the given Smart Cache (With a callback method, using Smart Cache Event Listener API)")
    public boolean rescheduleAutoCleaner(@JMXBeanParameter(name = "Expiry Duration", description = "The TTL Value for the Cache elements") final long EXPIRY_DURATION,
                                         @JMXBeanParameter(name = "Start Task Delay", description = "The delay after which the auto cleaner task starts") final long START_TASK_DELAY,
                                         @JMXBeanParameter(name = "Repeat Task Delay", description = "The delay after which the auto cleaner task repeats itself") final long REPEAT_TASK_DELAY) throws SmartCacheException {
        this.cache.stopAutoCleaner();
        return this.cache.startAutoCleaner(EXPIRY_DURATION, START_TASK_DELAY, REPEAT_TASK_DELAY, TimeUnit.SECONDS);
    }

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
    @Override
    @JMXBeanOperation(name = "rescheduleAutoCleanerReflection", description = "Restart the auto cleaner service for the given Smart Cache (With a callback method, using Java Reflection API)")
    public boolean rescheduleAutoCleanerReflection(@JMXBeanParameter(name = "Expiry Duration", description = "The TTL Value for the Cache elements") final long EXPIRY_DURATION,
                                                   @JMXBeanParameter(name = "Start Task Delay", description = "The delay after which the auto cleaner task starts") final long START_TASK_DELAY,
                                                   @JMXBeanParameter(name = "Repeat Task Delay", description = "The delay after which the auto cleaner task repeats itself") final long REPEAT_TASK_DELAY,
                                                   @JMXBeanParameter(name = "Time Unit", description = "The Time Unit in which the previous two parameters are defined") final TimeUnit TIME_UNIT,
                                                   @JMXBeanParameter(name = "Callback Class Object", description = "An object of the Callback Class (can be null)") final Object CALLBACK_CLASS_OBJECT,
                                                   @JMXBeanParameter(name = "Callback Method", description = "The Callback Method (can be null)") final Method CALLBACK_METHOD) throws SmartCacheException {
        this.cache.stopAutoCleaner();
        return this.cache.startAutoCleaner(EXPIRY_DURATION, START_TASK_DELAY, REPEAT_TASK_DELAY, TIME_UNIT, CALLBACK_CLASS_OBJECT, CALLBACK_METHOD);
    }

    /**
     * Checks if the Cache entry contains the given KEY
     *
     * @param key The exact KEY to search for
     *
     * @return true /false
     */
    @Override
    @JMXBeanOperation(name = "containsKey", description = "Checks if the Cache entry contains the given KEY")
    public boolean containsKey(@JMXBeanParameter(name = "The Key", description = "The Key for the Cache element") K key) {
        return cache.containsKey(key);
    }

    /**
     * Checks whether the cache is currently empty
     *
     * @return true /false
     */
    @Override
    @JMXBeanOperation(name = "isEmpty", description = "Checks whether the cache is currently empty")
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    /**
     * Gets the total number of entries in the Cache
     *
     * @return number of entries
     */
    @Override
    @JMXBeanOperation(name = "size", description = "Gets the total number of entries in the Cache")
    public int size() {
        return cache.size();
    }

    /**
     * Gets the total number of entries in the Cache whose keys matches a certain pattern
     *
     * @param somePortionOfKey the some portion of KEY
     *
     * @return number of entries
     */
    @Override
    @JMXBeanOperation(name = "numberOfEntries", description = "Gets the total number of entries in the Cache whose keys matches a certain pattern")
    public int numberOfEntries(@JMXBeanParameter(name = "Some Portion Of Key", description = "Some portion of the Key") K somePortionOfKey) {
        int count = 0;
        if (somePortionOfKey instanceof String) {
            if (somePortionOfKey != null) {
                Set<String> keySet = cache.keySet();
                for (String key : keySet) {
                    count = key.contains((String) somePortionOfKey) ? count++ : count;
                }
            }
        } else {
            return -1;
        }
        return count;
    }

    /**
     * Returns a formatted String that holds the information about the Smart Cache
     *
     * @return Smart Cache Info
     */
    @Override
    @JMXBeanOperation(name = "allSmartCacheInfo", description = "Returns a formatted String that holds the information about the Smart Cache")
    public String allSmartCacheInfo() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(String.format("%-20s", "Creation Time"));
        buffer.append(String.format("%-50s", "Key"));
        buffer.append(String.format("%-50s", "Value"));
        buffer.append("\n------------------------------------------------------------------------------------------\n");
        // Iterate the SmartCache for all keys
        Set<K> keySet = cache.keySet();
        for (K key : keySet) {
            V data = (V) cache.get(key);
            buffer.append(String.format("%-20s", data.getTIME_STAMP()));
            buffer.append(String.format("%-50s", key.toString()));
            buffer.append(String.format("%-50s", data.toString()));
            buffer.append("\n");
        }
        buffer.append("\n------------------------------------------------------------------------------------------\n");
        return buffer.toString();
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
    @JMXBeanOperation(name = "smartCacheInfoForKey", description = "Returns a formatted String that holds the information about the Smart Cache corresponding to the given KEY")
    public String smartCacheInfoForKey(@JMXBeanParameter(name = "The Key", description = "The Key for the Cache element") K key) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(String.format("%-20s", "Creation Time"));
        buffer.append(String.format("%-20s", "Key"));
        buffer.append(String.format("%-50s", "Value"));
        buffer.append("\n------------------------------------------------------------------------------------------\n");
        // Iterate the SmartCache for all keys
        V data = (V) cache.get(key);
        buffer.append(String.format("%-20s", data.getTIME_STAMP()));
        buffer.append(String.format("%-20s", key.toString()));
        buffer.append(String.format("%-50s", data.toString()));
        buffer.append("\n------------------------------------------------------------------------------------------\n");
        return buffer.toString();
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
    @JMXBeanOperation(name = "smartCacheInfoForKeySet", description = " Returns a formatted String that holds the information about the Smart Cache corresponding to the given set of Keys")
    public String smartCacheInfoForKeySet(@JMXBeanParameter(name = "The Set of Keys", description = "The set of Keys for the Cache elements") Set<K> keys) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(String.format("%-20s", "Creation Time"));
        buffer.append(String.format("%-20s", "Key"));
        buffer.append(String.format("%-50s", "Value"));
        buffer.append("\n------------------------------------------------------------------------------------------\n");
        // Iterate the SmartCache for all keys
        for (K key : keys) {
            V data = (V) cache.get(key);
            buffer.append(String.format("%-20s", data.getTIME_STAMP()));
            buffer.append(String.format("%-20s", key.toString()));
            buffer.append(String.format("%-50s", data.toString()));
        }
        buffer.append("\n------------------------------------------------------------------------------------------\n");
        return buffer.toString();
    }

    /**
     * Purges the entire cache
     *
     * @return the boolean
     */
    @Override
    @JMXBeanOperation(name = "purgeAllCacheEntries", description = "Purges the entire cache")
    public boolean purgeAllCacheEntries() {
        return cache.purgeCacheEntries();
    }

    /**
     * Purges only the data corresponding to the given KEY
     *
     * @param key the KEY
     *
     * @return the boolean
     */
    @Override
    @JMXBeanOperation(name = "purgeCacheEntry", description = "Purges only the data corresponding to the given KEY")
    public boolean purgeCacheEntry(@JMXBeanParameter(name = "The Key", description = "The Key for the Cache element") K key) {
        return cache.purgeCacheEntry(key);
    }

    /**
     * Purge cache entry.
     *
     * @param keys the keys
     *
     * @return the boolean
     */
    @Override
    @JMXBeanOperation(name = "purgeCacheEntries", description = "Purges only the data corresponding to the given KEY")
    public boolean purgeCacheEntries(@JMXBeanParameter(name = "The Set of Keys", description = "The set of Keys for the Cache elements") Set<K> keys) {
        return cache.purgeCacheEntry(keys);
    }

    /**
     * View history.
     *
     * @param key the key
     *
     * @return the string
     */
    @Override
    @JMXBeanOperation(name = "viewHistory", description = "View History")
    public String viewHistoryForKey(@JMXBeanParameter(name = "Key", description = "The Key for the Cache Element") K key) {
        return HISTORY.viewHistory(key);
    }

    /**
     * View history.
     *
     * @param reason the reason
     *
     * @return the string
     */
    @Override
    @JMXBeanOperation(name = "viewHistoryForReason", description = "View History")
    public String viewHistoryForReason(@JMXBeanParameter(name = "Reason", description = "The Reason for which the Cache element was put into History") String reason) {
        return HISTORY.viewHistoryForReason(reason);
    }

    /**
     * View history.
     *
     * @return the string
     */
    @Override
    @JMXBeanOperation(name = "viewAllHistory", description = "View History")
    public String viewAllHistory() {
        return HISTORY.viewAllHistory();
    }

    /**
     * Set the maximum number of entries after which the History is
     * deleted permanently.
     *
     * @param maxElementCount the max element count
     */
    @Override
    @JMXBeanOperation(name = "autoDeleteHistory", description = "Set the maximum number of entries after which the History is deleted permanently")
    public void autoDeleteHistory(@JMXBeanParameter(name = "Max Element Count", description = "The Maximum number of elements to keep in History") int maxElementCount) {
        HISTORY.autoDeleteHistory(maxElementCount);
    }

    /**
     * Purges the contents of History into a user defined file.
     * By default the SmartCache will dump the data into a file named -
     * SmartCacheHistory_(current-date/time).txt
     *
     * @param filePath the absolute file path for the dump file.
     */
    @Override
    @JMXBeanOperation(name = "purgeHistory", description = "Purges the contents of History into a user defined file")
    public void purgeHistory(@JMXBeanParameter(name = "File Path", description = "By default the SmartCache will dump the data into a file named - SmartCacheHistory_(current-date/time).txt") String filePath) throws Exception {
        HISTORY.purgeHistory(filePath);
    }

    /**
     * Gets deleted entries counter.
     *
     * @return the deleted entries counter
     *
     * @throws SmartCacheMBeanException the smart cache m bean exception
     */
    @Override
    @JMXBeanOperation(name = "getDeletedEntriesCounter", description = "Gets the total number of entries deleted for this Smart Cache (available only if the cache is created using DefaultSmartCache)")
    public long getDeletedEntriesCounter() throws SmartCacheMBeanException {
        if (cache instanceof DefaultSmartCache) {
            return ((DefaultSmartCache) cache).getDeletedEntriesCounter();
        } else {
            throw new SmartCacheMBeanException("Given Cache is not an instance of DefaultSmartCache");
        }
    }

    /**
     * Reset deleted entries counter.
     *
     * @throws SmartCacheMBeanException the smart cache m bean exception
     */
    @Override
    @JMXBeanOperation(name = "resetDeletedEntriesCounter", description = "Resets the total number of entries deleted for this Smart Cache to zero (available only if the cache is created using DefaultSmartCache)")
    public void resetDeletedEntriesCounter() throws SmartCacheMBeanException {
        if (cache instanceof DefaultSmartCache) {
            ((DefaultSmartCache) cache).resetDeletedEntriesCounter();
        } else {
            throw new SmartCacheMBeanException("Given Cache is not an instance of DefaultSmartCache");
        }
    }
}
