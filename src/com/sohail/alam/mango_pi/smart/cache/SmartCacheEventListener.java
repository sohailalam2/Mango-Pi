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
 * This {@link SmartCacheEventListener} interface exposes the events that the SmartCache system
 * is able to fire when certain conditions are met.
 * <p/>
 * You should implement this {@link SmartCacheEventListener} interface and add the listener class
 * to your Cache instance, if you want to be notified when something happens, like data is deleted
 * from the Cache.
 * <p/>
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 4/7/13
 * Time: 9:21 PM
 */
public interface SmartCacheEventListener<V extends SmartCachePojo> {

    /**
     * Event for On create cache entry.
     * Whenever an element is inserted into the SmartCache, this event gets fired,
     * with the latest entry that was inserted into that cache.
     *
     * @param createdEntry the entry which was inserted into the Cache
     */
    public void onCreateCacheEntry(V createdEntry);

    /**
     * Event for On delete cache entry.
     * Whenever an element is deleted from the SmartCache, this event gets fired,
     * with the deleted entry being passed as the method parameter.
     *
     * @param deletedEntry the entry which was deleted from the Cache
     */
    public void onDeleteCacheEntry(V deletedEntry);
}
