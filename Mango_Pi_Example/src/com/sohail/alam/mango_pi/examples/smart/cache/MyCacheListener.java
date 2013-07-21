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

package com.sohail.alam.mango_pi.examples.smart.cache;

import com.sohail.alam.mango_pi.smart.cache.SmartCacheEventListener;
import com.sohail.alam.mango_pi.smart.cache.SmartCachePojo;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 4/7/13
 * Time: 11:04 PM
 */
public class MyCacheListener<K, V extends SmartCachePojo> implements SmartCacheEventListener<K, V> {

    private final Logger LOGGER = Logger.getLogger(MyCacheListener.class.getName());

    public MyCacheListener() {
        LOGGER.info("Added SmartCacheEventListener");
    }

    /**
     * Event for On create cache entry.
     * Whenever an element is inserted into the SmartCache, this event gets fired,
     * with the latest entry that was inserted into that cache.
     *
     * @param key          the key
     * @param createdEntry the entry which was inserted into the Cache
     */
    @Override
    public void onCreateCacheEntry(Object key, SmartCachePojo createdEntry) {
        LOGGER.info("Cache Entry was CREATED => Key: " + key + " Time: " + new Date(createdEntry.getCREATION_TIME()));
    }

    /**
     * Event for On delete cache entry.
     * Whenever an element is deleted from the SmartCache, this event gets fired,
     * with the deleted entry being passed as the method parameter.
     *
     * @param key          the key
     * @param deletedEntry the entry which was deleted from the Cache
     * @param reason       the reason for which the cache entry was deleted.
     *                     This may contain one of the values present in
     *                     {@link com.sohail.alam.mango_pi.smart.cache.SmartCache.SmartCacheDeleteReason}
     *                     interface, or any other set by the user.
     */
    @Override
    public void onDeleteCacheEntry(K key, V deletedEntry, String reason) {
        LOGGER.info("Cache Entry was DELETED due to Reason: " + reason + " => Key: " + key
                + " Creation Time: " + new Date(deletedEntry.getCREATION_TIME())
                + " Deletion Time: " + new Date(System.currentTimeMillis()));
    }

    /**
     * Event for On Single Entry Purge.
     * Whenever an element is purged (deleted from cache for backup purpose),
     * this event gets fired, with the deleted entry being passed as the method parameter.
     *
     * @param key           the key
     * @param purgedElement the purged element
     */
    @Override
    public void onSingleEntryPurge(Object key, SmartCachePojo purgedElement) {
        LOGGER.info("Cache Entry was PURGED => Key: " + key + " Time: " + purgedElement.getCREATION_TIME());
    }

    /**
     * Event for On Cache Purge.
     * Whenever the entire Cache gets purged (deleted for backup purpose),
     * this event gets fired, with an Set containing the entire cache data.
     *
     * @param cacheList the cache list
     */
    @Override
    public void onCachePurge(Map cacheList) {
        LOGGER.info("Cache was PURGED : \n" + cacheList);
    }
}
