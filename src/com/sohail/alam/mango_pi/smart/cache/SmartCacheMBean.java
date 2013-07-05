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
import java.util.concurrent.TimeUnit;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 4/7/13
 * Time: 8:27 AM
 */
@JMXBean(description = "Smart Cache MBean")
public class SmartCacheMBean<T extends SmartCache> {

    private static final String MBEAN_NAME = "MangoPI:Module=SmartCache-" + System.nanoTime();
    private SmartCache cache;

    public SmartCacheMBean(T cache) {
        if (cache == null)
            throw new NullPointerException("The Instance of the Cache can not be null");

        if (cache instanceof DefaultSmartCache)
            this.cache = (DefaultSmartCache) cache;
        else
            this.cache = cache;
    }

    private SmartCacheMBean() {

    }

    /**
     * Start the MBean service
     */
    public void startService() throws SmartCacheException {
        try {
            MBeanService.startService(this, MBEAN_NAME);
        } catch (Exception e) {
            throw new SmartCacheException("Smart Cache was unable to start the Smart Cache MBean Service: " + e.getMessage(), e);
        }
    }

    /**
     * Stop the MBean Service
     */
    public void stopService() throws SmartCacheException {
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

    @JMXBeanOperation(name = "startAutoCleaner", description = "Start the auto cleaner service for the given Smart Cache")
    public boolean startAutoCleaner(@JMXBeanParameter(name = "Expiry Duration", description = "The TTL Value for the Cache elements") final long EXPIRY_DURATION,
                                    @JMXBeanParameter(name = "Start Task Delay", description = "The delay after which the auto cleaner task starts") final long START_TASK_DELAY,
                                    @JMXBeanParameter(name = "Repeat Task Delay", description = "The delay after which the auto cleaner task repeats itself") final long REPEAT_TASK_DELAY,
                                    @JMXBeanParameter(name = "Time Unit", description = "The Time Unit in which the previous two parameters are defined") final TimeUnit TIME_UNIT,
                                    @JMXBeanParameter(name = "Callback Class Object", description = "An object of the Callback Class (can be null)") final Object CALLBACK_CLASS_OBJECT,
                                    @JMXBeanParameter(name = "Callback Method", description = "The Callback Method (can be null)") final Method CALLBACK_METHOD) {

        try {
            return this.cache.startAutoCleaner(EXPIRY_DURATION, START_TASK_DELAY, REPEAT_TASK_DELAY, TIME_UNIT, CALLBACK_CLASS_OBJECT, CALLBACK_METHOD);
        } catch (SmartCacheException e) {
            e.printStackTrace();
        }
        return false;
    }

    @JMXBeanOperation(name = "stopAutoCleaner", description = "Stop the auto cleaner service for the given Smart Cache")
    public void stopAutoCleaner() {
        this.cache.stopAutoCleaner();
    }

    @JMXBeanOperation(name = "getDeletedEntriesCounter", description = "Gets the total number of entries deleted for this Smart Cache (available only if the cache is created using DefaultSmartCache)")
    public long getDeletedEntriesCounter() throws SmartCacheException {
        if (cache instanceof DefaultSmartCache) {
            return ((DefaultSmartCache) cache).getDeletedEntriesCounter();
        } else {
            throw new SmartCacheException("Given Cache is not an instance of DefaultSmartCache");
        }
    }

    @JMXBeanOperation(name = "resetDeletedEntriesCounter", description = "Resets the total number of entries deleted for this Smart Cache to zero (available only if the cache is created using DefaultSmartCache)")
    public void resetDeletedEntriesCounter() throws SmartCacheException {
        if (cache instanceof DefaultSmartCache) {
            ((DefaultSmartCache) cache).resetDeletedEntriesCounter();
        } else {
            throw new SmartCacheException("Given Cache is not an instance of DefaultSmartCache");
        }
    }
}
