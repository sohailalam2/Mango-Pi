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

/**
 * <p>
 * This {@link SmartCachePojo} Class must be extended by all the Data Structures that need Smart Caching,
 * and are setup with the help of any built-in {@link SmartCache} helper classes such as {@link DeprecatedSmartCache}.
 * The reason being that these helper classes need the {@link SmartCachePojo#CREATION_TIME} value in order to successfully
 * enable and workout the {@link SmartCache#startAutoCleaner(long, long, long, java.util.concurrent.TimeUnit, Object, java.lang.reflect.Method)}
 * helper method.
 * </p>
 * <p>
 * Any Class that extends this {@link SmartCachePojo} class, automatically gets a value for {@link SmartCachePojo#CREATION_TIME},
 * when instantiated. This value is then taken as the creation time for the Cache Data.
 * </p>
 * User: Sohail Alam
 * Version: 1.1.6
 * Date: 9/6/13
 * Time: 12:36 AM
 */
public abstract class SmartCachePojo<K> {

    /**
     * This represents the Creation Time of the Cache Entry (in milliseconds)
     * It gets automatically initialized when Cache entry is created.
     */
    private final long CREATION_TIME;

    /**
     * This represents the Deletion Time of the Cache Entry (in milliseconds)
     * This is used to maintain the Smart Cache History
     */
    private long DELETION_TIME;

    /**
     * This represents the name of the Smart Cache Data (the current data).
     * By default it takes the value of SMART_CACHE_DATA_(the time when the cache was created).
     * You may override this value.
     */
    public String SMART_CACHE_DATA_NAME;

    /**
     * Instantiates a new Smart cache pojo.
     */
    public SmartCachePojo() {
        CREATION_TIME = System.currentTimeMillis();
        SMART_CACHE_DATA_NAME = "SMART_CACHE_DATA_" + CREATION_TIME;
    }

    /**
     * The time in milliseconds when this Cache Data was created.
     * This will be used while deleting the {@link SmartCache} entries, based on the expiry details.
     *
     * @return the Creation Time
     */
    public long getCREATION_TIME() {
        return CREATION_TIME;
    }

    /**
     * This abstract method MUST be defined in your implementation, and
     * it SHOULD return the total numberOfEntries of the data entry that is stored in the Cache.
     *
     * @return the total numberOfEntries of the data stored
     */
    public abstract long size();

    /**
     * Gets key for this Cache entry.
     *
     * @return the key
     */
    public abstract K getKey();

    /**
     * Sets the key for this Cache entry.
     *
     * @param key the key
     */
    public abstract void setKey(K key);
}
