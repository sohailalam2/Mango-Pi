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


import com.sohail.alam.mango_pi.smart.cache.DefaultSmartCache;
import com.sohail.alam.mango_pi.smart.cache.SmartCachePojo;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * User: Sohail Alam
 * Version: 1.1.6
 * Date: 9/6/13
 * Time: 1:29 AM
 */
public class TestSmartCache {

    private final Logger logger = Logger.getLogger(TestSmartCache.class.getName());
    private final int NUMBER_OF_CHUNKS = 10;  // The number of chunks of binary data
    private final int CHUNK_SIZE = 1 * 1024 * 1024; // The size of each chunk in bytes

    /**
     * Constructor
     */
    public TestSmartCache() throws Exception {

        logger.info("Started with Test Smart Cache");

        // Instantiate the Smart Cache (Here we take advantage of the helper class DefaultSmartCache)
        final DefaultSmartCache<String, SmartCacheData, MyCacheListener> mySmartCache =
                new DefaultSmartCache<String, SmartCacheData, MyCacheListener>();

        // Add an Event Listener to the Cache
        mySmartCache.addSmartCacheEventsListener(new MyCacheListener());

        // The Callback method which will be invoked by the Smart Cache when deleting an entry from the Cache
        Method method = null;
        try {
            method = getClass().getDeclaredMethod("callback", SmartCachePojo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start the auto cleaner service
        mySmartCache.startAutoCleaner(200, 0, 500, TimeUnit.MILLISECONDS, this, method);

        // This is just for checking the number of entries in SmartCache and number of deleted entries
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                logger.info("Total Elements Present in Cache   : " + mySmartCache.size());
//                logger.info("Total Elements Deleted from Cache : " + mySmartCache.getDeletedEntriesCounter());
//            }
//        }, 0, 1000, TimeUnit.MILLISECONDS);

        // Finally put the data into the Smart Cache
        for (int i = 0; i < 1000; i++) {
            mySmartCache.put("key" + i, new SmartCacheData("DATA" + i, createData(NUMBER_OF_CHUNKS, CHUNK_SIZE)));
        }
    }

    /**
     * Private method to create binary data
     *
     * @param NUMBER_OF_CHUNKS The number of Chunks of the binary data
     * @param CHUNK_SIZE       The size of each chunk in bytes
     *
     * @return a byte[][] containing the binary data
     */
    private byte[][] createData(final int NUMBER_OF_CHUNKS, final int CHUNK_SIZE) {

        byte[][] data = new byte[NUMBER_OF_CHUNKS][CHUNK_SIZE];
        for (int i = 0; i < NUMBER_OF_CHUNKS; i++) {
            for (int j = 0; j < CHUNK_SIZE; j++) {
                data[i][j] = 'A';
            }
        }
        return data;
    }

    /**
     * The callback method.
     * This method is used by the SmartCache to provide callback when deleting an entry.
     * The entry which is deleted from the cache is passed to this method as an argument and
     * you are free to do whatever you want to do with it, such as persist it in a database etc.
     *
     * @param data The deleted entry from the Smart Cache
     */
    public void callback(SmartCachePojo data) {
//        logger.info("Deleted Data which was created at: " + data.getTIME_STAMP());
    }
}
