package com.sohail.alam.mango_pi.examples.smart.cache.advanced_cleanup;

import com.sohail.alam.mango_pi.examples.smart.cache.MyCacheListener;
import com.sohail.alam.mango_pi.examples.smart.cache.SmartCacheData;
import com.sohail.alam.mango_pi.examples.smart.cache.TestSmartCache;
import com.sohail.alam.mango_pi.smart.cache.DeprecatedSmartCache;
import com.sohail.alam.mango_pi.smart.cache.SmartCacheException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: sohail.alam
 * Date: 11/7/13
 * Time: 3:13 PM
 */
public class AdvancedSmartCacheCleanup {

    private static final Logger LOGGER = Logger.getLogger(AdvancedSmartCacheCleanup.class.getName());
    private static final int NUMBER_OF_CHUNKS = 2;  // The number of chunks of binary data
    private static final int CHUNK_SIZE = 128; // The size of each chunk in bytes

    public static void main(String[] args) throws SmartCacheException, InterruptedException {
        LOGGER.info("Starting SmartCache Setup with SmartCacheEventListener");

        // Instantiate the Smart Cache
        // (Here we take advantage of the helper class DeprecatedSmartCache)
        final DeprecatedSmartCache<String, SmartCacheData> mySmartCache =
                new DeprecatedSmartCache<String, SmartCacheData>("Advanced_Test");

        // Add an Event Listener to the Cache, and Start the auto cleaner service
        mySmartCache.startAutoCleaner(new MyCacheListener());

        // Finally put the data into the Smart Cache with different TTL values
        mySmartCache.put("key 1", new SmartCacheData("DATA 1", TestSmartCache.createData(NUMBER_OF_CHUNKS, CHUNK_SIZE)), 5, TimeUnit.SECONDS);
        mySmartCache.put("key 2", new SmartCacheData("DATA 2", TestSmartCache.createData(NUMBER_OF_CHUNKS, CHUNK_SIZE)), 30, TimeUnit.SECONDS);
        mySmartCache.put("key 3", new SmartCacheData("DATA 3", TestSmartCache.createData(NUMBER_OF_CHUNKS, CHUNK_SIZE)), 15, TimeUnit.SECONDS);

    }

}
