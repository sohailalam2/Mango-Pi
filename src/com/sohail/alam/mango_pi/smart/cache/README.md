# SMART T

The SmartCache is an independent module of Mango Pi, which provides you a very simple and easy to use
in-memory caching system. There are basically three components to this SmartCache module,

* SmartCache            (Interface)
    * Declaring all the methods that the SmartCache module supports
* AbstractSmartCache    (Abstract Class)
    * An abstract class that defines all of the SmartCache methods, except the 'startAutoCleaner' and 'stopAutoCleaner' methods
* SmartCachePojo        (Class)
    * A class that the SmartCache module uses in order to find out the creation time of the cache


There is also a 'DefaultSmartCache' helper class which implements all the methods of 'SmartCache' interface.
This helper class will clear the cache for you in background thread, according to the TTL value that you set for the cache elements.

PLEASE NOTE: Your data structure must extend the 'SmartCachePojo' class in order for this helper class to work. (See the example)s

It is an ongoing project. Please feel free to contribute to this Project.

## How to use

* Lets Define a custom data structure that we need to save in SmartCache (See the example)

> SmartCacheData.java

    public class SmartCacheData extends SmartCachePojo {

        private String key;
        private byte[][] data;

        public SmartCacheData() {
            super();
        }

        public SmartCacheData(String key, byte[][] data) {
            this.setKey(key);
            this.setData(data);
        }

        .....
        ..... Some Getters and Setters
        .....
    }


* Now lets Start the SmartCache service and insert data into the cache. We will also provide a callback method to SmartCache.

> TestSmartCache.java

    public class TestSmartCache {

        .....
        .....  Some variables and Constants
        .....

        public TestSmartCache() {

                logger.info("Started with Test Smart Cache");

                // Instantiate the Smart Cache (Here we take advantage of the helper class DefaultSmartCache)
                final DefaultSmartCache<String, SmartCacheData> mySmartCache =
                        new DefaultSmartCache<String, SmartCacheData>();

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

        .....
        ..... Some other methods
        .....

        public void callback(SmartCachePojo data) {
                logger.info("Deleted Data which was created at: " + data.TIME_STAMP);
        }
    }

