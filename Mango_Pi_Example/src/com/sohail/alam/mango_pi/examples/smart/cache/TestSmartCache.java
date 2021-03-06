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
import com.sohail.alam.mango_pi.smart.cache.DeprecatedSmartCache;
import com.sohail.alam.mango_pi.smart.cache.SmartCachePojo;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * User: Sohail Alam
 * Version: 1.1.6
 * Date: 9/6/13
 * Time: 1:29 AM
 */
public class TestSmartCache {

    private final int NUMBER_OF_CHUNKS = 10;  // The number of chunks of binary data
    private final int CHUNK_SIZE = 1 * 1024; // The numberOfEntries of each chunk in bytes

    /**
     * Constructor
     */
    public TestSmartCache() throws Exception {

        System.out.println("Started with Test Smart Cache");

        testWithListener();

        //testWithReflection();
    }

    /**
     * Private method to create binary data
     *
     * @param NUMBER_OF_CHUNKS The number of Chunks of the binary data
     * @param CHUNK_SIZE       The numberOfEntries of each chunk in bytes
     *
     * @return a byte[][] containing the binary data
     */
    public static byte[][] createData(final int NUMBER_OF_CHUNKS, final int CHUNK_SIZE) {

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
        System.err.println("Deleted Data which was created at: " + data.getCREATION_TIME());
    }

    private void testWithListener() throws Exception {

        System.out.println("Starting SmartCache Setup with SmartCacheEventListener");

        // Instantiate the Smart Cache
        // (Here we take advantage of the helper class DeprecatedSmartCache)
        final DefaultSmartCache<String, SmartCacheData> mySmartCache =
                new DefaultSmartCache<String, SmartCacheData>("Test", true);

        // This is just for testing the MBeans and Uniqueness of SmartCache names
        // It will throw Exception if the names are same.
        // final DeprecatedSmartCache<String, SmartCacheData> mySmartCache2 =
        //          new DeprecatedSmartCache<String, SmartCacheData>("Test");

        // Add an Event Listener to the Cache and Start the auto cleaner service
        mySmartCache.addSmartCacheEventsListener(new MyCacheListener());
//        mySmartCache.startAllAutoCleaner();

        // Finally put the data into the Smart Cache
        for (int i = 0; i < 1000; i++) {
            mySmartCache.put("key" + i, new SmartCacheData("DATA" + i, createData(NUMBER_OF_CHUNKS, CHUNK_SIZE)), (i));
            Thread.sleep(100);
        }
    }

    private void testWithReflection() throws Exception {

        System.out.println("Starting SmartCache Setup with Reflection Callback");

        // Instantiate the Smart Cache
        // (Here we take advantage of the helper class DeprecatedSmartCache)
        final DeprecatedSmartCache<String, SmartCacheData> mySmartCache =
                new DeprecatedSmartCache<String, SmartCacheData>("Test1");

        // The Callback method which will be invoked by the Smart Cache when deleting an entry from the Cache
        Method method = null;
        try {
            method = getClass().getDeclaredMethod("callback", SmartCachePojo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start the auto cleaner service
        mySmartCache.startAutoCleaner(200, 0, 500, TimeUnit.MILLISECONDS, this, method);

        // Finally put the data into the Smart Cache
        for (int i = 0; i < 1000; i++) {
            mySmartCache.put("key" + i, new SmartCacheData("DATA" + i, createData(NUMBER_OF_CHUNKS, CHUNK_SIZE)));
        }
    }
}
