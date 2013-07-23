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

package com.sohail.alam.mango_pi.smart.cache.mbeans;

import com.sohail.alam.mango_pi.jmx.wrapper.JMXBean;
import com.sohail.alam.mango_pi.jmx.wrapper.JMXBeanOperation;
import com.sohail.alam.mango_pi.jmx.wrapper.JMXBeanParameter;
import com.sohail.alam.mango_pi.smart.cache.DeprecatedSmartCache;
import com.sohail.alam.mango_pi.smart.cache.SmartCacheException;
import com.sohail.alam.mango_pi.smart.cache.SmartCachePojo;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 4/7/13
 * Time: 8:27 AM
 */
@JMXBean(description = "Smart Cache MBean")
@Deprecated
public class DeprecatedSmartCacheManager<T extends DeprecatedSmartCache, K, V extends SmartCachePojo>
        extends AbstractSmartCacheManager<T, K, V>
        implements DeprecatedSmartCacheManagerMBean<K, V> {

    private DeprecatedSmartCache cache;

    /**
     * Instantiates a new Smart cache manager.
     *
     * @param cache the cache
     */
    public DeprecatedSmartCacheManager(T cache) {
        super(cache);
        this.cache = cache;
    }

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
    @Deprecated
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
    @Deprecated
    @JMXBeanOperation(name = "restartAutoCleaner", description = "Start the auto cleaner service for the given Smart Cache (With a callback method, using Smart Cache Event Listener API) [All are in seconds]")
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
    @Deprecated
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
    @Deprecated
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
    @Deprecated
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
    @Deprecated
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
    @Deprecated
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
     * Put the Data of type {@link V} into the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache},
     * corresponding to the Key of type {@link K}
     *
     * @param key      Any Key of type
     * @param data     Any Data of type
     * @param ttl      the ttl value - The after which data will be auto deleted from the Cache
     * @param timeUnit the time unit for the TTL Value
     */
    @Override
    public void put(K key, V data, int ttl, TimeUnit timeUnit) {
        super.put(key, data, ttl, timeUnit);
    }

    /**
     * Put the Data of type {@link V} into the
     * {@link com.sohail.alam.mango_pi.smart.cache.SmartCache},
     * corresponding to the Key of type {@link K}
     *
     * @param key  Any Key of type
     * @param data Any Data of type
     * @param ttl  the ttl value - The after which data will be auto deleted from the Cache
     */
    @Override
    public void put(K key, V data, int ttl) {
        super.put(key, data, ttl);
    }

    /**
     * Checks if the Cache entry contains the given KEY
     *
     * @param key The exact KEY to search for
     *
     * @return true /false
     */
    @Override
    public boolean containsValue(V key) {
        return super.containsValue(key);
    }
}
