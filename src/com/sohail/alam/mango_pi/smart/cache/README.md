# SMART CACHE

The SmartCache is an independent module of Mango Pi, which provides you a very simple and easy to use
in-memory caching system. There are basically four components to this SmartCache module,

* SmartCache                (Interface)
    * Declaring all the methods that the SmartCache module supports

* AbstractSmartCache        (Abstract Class)
    * An abstract class that defines all of the SmartCache methods, except the 'startAutoCleaner' and 'stopAutoCleaner' methods.
    To make things easy for you, AbstractSmartCache overrides the 'startAutoCleaner()' method which
    uses Java's Reflection API, but only returns 'false'. This was done to make it easy for you to
    extend the AbstractSmartCache. If you need Reflection, simply override this method.

* SmartCachePojo            (Class)
    * A class that the SmartCache module uses in order to find out the creation time of the cache,
    also stores a unique name for this instance of Cache (which you can configure).

* SmartCacheEventListener   (Interface)
    * An interface which contains the events signature. You can add Listener to your cache to be
    notified when an event occurs, such as when an item is added or removed from the cache.

There is also a 'DefaultSmartCache' helper class which implements all the methods of 'SmartCache' interface.
This helper class will clear the cache for you in background thread, according to the TTL value that you set for the cache elements.

**PLEASE NOTE:** *Your data structure must extend the 'SmartCachePojo' class in order for this helper class (DefaultSmartCache) to work. (See the example)s*

It is an ongoing project. Please feel free to contribute to this Project.

## Dependencies

Smart Cache depends upon the following sub-packages of Mango-Pi.

* JMX Wrapper (by Udo Klimaschewski)

* Utils -> MBeanService.java

## How to use

* Lets Define a custom data structure that we need to save in SmartCache (See the example)

SmartCacheData.java

```java
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
}
```

* Now lets Start the SmartCache service and insert data into the cache. We will also provide a callback method to SmartCache.

TestSmartCache.java

```java
public class TestSmartCache {

    public TestSmartCache() {

        logger.info("Started with Test Smart Cache");

        // Instantiate the Smart Cache (Here we take advantage of the helper class DefaultSmartCache)
        final DefaultSmartCache<String, SmartCacheData, MyCacheListener> mySmartCache =
              new DefaultSmartCache<String, SmartCacheData, MyCacheListener>("MyCache");

        // The Callback method which will be invoked by the Smart Cache when deleting an entry from the Cache
        // This uses Java's Reflection API. Alternatively you can add SmartCacheEventListener to do the same job
        // If you do not want the overhead of Reflection.
           Method method = null;
           try {
               method = getClass().getDeclaredMethod("callback", SmartCachePojo.class);
           } catch (Exception e) {
               e.printStackTrace();
           }

           // Start the auto cleaner service with the callback method, as created above
           mySmartCache.startAutoCleaner(200, 0, 500, TimeUnit.MILLISECONDS, this, method);

           // Finally put the data into the Smart Cache
           for (int i = 0; i < 1000; i++) {
                mySmartCache.put("key" + i, new SmartCacheData("DATA" + i, createData(NUMBER_OF_CHUNKS, CHUNK_SIZE)));
        }
    }

    // This is your callback method. NOTE that it MUST take one argument of type V extends SmartCachePojo
    public void callback(SmartCachePojo data) {
         logger.info("Deleted Data which was created at: " + data.TIME_STAMP);
    }
}
```

* Optionally, we can simply implement SmartCacheEventListener and get callbacks
(this will not use Java Reflection API)

MyCacheListener.java

```java
public class MyCacheListener implements SmartCacheEventListener {

     public MyCacheListener() {
        System.out.println("Added SmartCacheEventListener");
     }

     @Override
     public void onCreateCacheEntry(SmartCachePojo createdEntry) {
         System.out.println("Data Inserted into Cache: " + createdEntry.getTIME_STAMP());
     }

     @Override
     public void onDeleteCacheEntry(SmartCachePojo deletedEntry) {
         System.out.println("Data Deleted from the Cache: " + deletedEntry.getTIME_STAMP());
     }
}
```

Now simply add the above listener to your Cache.

TestSmartCache.java

```java
// Instantiate the Smart Cache (Here we take advantage of the helper class DefaultSmartCache)
   final DefaultSmartCache<String, SmartCacheData, MyCacheListener> mySmartCache =
         new DefaultSmartCache<String, SmartCacheData, MyCacheListener>("MyCache");

// Add an Event Listener to the Cache
mySmartCache.addSmartCacheEventsListener(new MyCacheListener());
```

If you want you can start the Auto Cleaner Service for with this Listener

TestSmartCache.java

```java
// Start the auto cleaner service
mySmartCache.startAutoCleaner(200, 0, 500, TimeUnit.MILLISECONDS);
```

You are all done!! Enjoy :)