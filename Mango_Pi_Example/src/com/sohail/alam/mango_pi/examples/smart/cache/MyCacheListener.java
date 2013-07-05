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

import java.util.logging.Logger;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 4/7/13
 * Time: 11:04 PM
 */
public class MyCacheListener implements SmartCacheEventListener {

    private final Logger LOGGER = Logger.getLogger(MyCacheListener.class.getName());

    public MyCacheListener() {
        LOGGER.info("Added SmartCacheEventListener");
    }

    @Override
    public void onCreateCacheEntry(SmartCachePojo createdEntry) {
        LOGGER.info("Data Inserted into Cache: " + createdEntry.getTIME_STAMP());
    }

    @Override
    public void onDeleteCacheEntry(SmartCachePojo deletedEntry) {
        LOGGER.info("Data Deleted from the Cache: " + deletedEntry.getTIME_STAMP());
    }
}