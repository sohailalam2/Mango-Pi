package com.sohail.alam.mango_pi.smart.cache;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/7/13
 * Time: 12:16 PM
 */
public interface DeprecatedSmartCacheIntf<K, V> extends SmartCache<K, V> {

    /**
     * Put the Data of type {@link V} into the {@link SmartCache}, corresponding to the Key of type {@link K}
     * <p/>
     * <strong>NOTE:</strong>
     * This method has been deprecated and it is HIGHLY RECOMMENDED to use {@link SmartCache#put(Object, Object, int)}
     * method to insert entries into the Cache with its own TTL value which can be different for
     * different entries. This can be used with the {@link SmartCache#startAllAutoCleaner()},
     * or {@link SmartCache#startAllAutoCleaner(SmartCacheEventListener)} method to delete the specific entry corresponding to its TTL value.
     *
     * @param key  Any Key of type
     * @param data Any Data of type
     */
    @Deprecated
    void put(K key, V data);

    /**
     * Puts an entire Map into the {@link SmartCache}
     * This method has been deprecated as it does not allow to set individual TTL Values
     *
     * @param dataMap Map of type having Key of type and Data of type
     */
    @Deprecated
    void putAll(ConcurrentMap<K, V> dataMap);

    /**
     * Sets up an Auto Cleaner Service which will delete entries from the {@link SmartCache} System on expiry of its TTL (time to live)
     * <p/>
     * <strong>NOTE:</strong>
     * This method has been deprecated. It is HIGHLY RECOMMENDED that you use {@link SmartCache#startAllAutoCleaner()},
     * or {@link SmartCache#startAllAutoCleaner(SmartCacheEventListener)} method to delete the specific entry corresponding to its TTL value.
     *
     * @param EXPIRY_DURATION       The TTL value after which the entries will be deleted from the Smart Cache
     * @param START_TASK_DELAY      The initial delay after which this Auto Cleaner service starts
     * @param REPEAT_TASK_DELAY     The delay after which this Auto Cleaner service repeats itself
     * @param TIME_UNIT             The Unit of time for the previous parameters
     * @param CALLBACK_CLASS_OBJECT The Class Object which contains the callback method, if
     *                              then no callback is made
     * @param CALLBACK_METHOD       The callback method, if null then no callback is provided.
     *                              NOTE: The Callback method must accept only one parameter of type
     *
     * @return if the Auto Cleaner Service was setup correctly, otherwise returns
     *
     * @throws SmartCacheException the smart cache exception
     */
    @Deprecated
    boolean startAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT,
                             final Object CALLBACK_CLASS_OBJECT, final Method CALLBACK_METHOD) throws SmartCacheException;

    /**
     * Sets up an Auto Cleaner Service which will delete entries from the {@link SmartCache} System on expiry of its TTL (time to live)
     * <p/>
     * <strong>NOTE:</strong>
     * This method has been deprecated. It is HIGHLY RECOMMENDED that you use {@link SmartCache#startAllAutoCleaner()},
     * or {@link SmartCache#startAllAutoCleaner(SmartCacheEventListener)} method to delete the specific entry corresponding to its TTL value.
     *
     * @param EXPIRY_DURATION   The TTL value after which the entries will be deleted from the Smart Cache
     * @param START_TASK_DELAY  The initial delay after which this Auto Cleaner service starts
     * @param REPEAT_TASK_DELAY The delay after which this Auto Cleaner service repeats itself
     * @param TIME_UNIT         The Unit of time for the previous parameters
     *
     * @return if the Auto Cleaner Service was setup correctly, otherwise returns
     *
     * @throws SmartCacheException the smart cache exception
     */
    @Deprecated
    boolean startAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT) throws SmartCacheException;

    /**
     * Sets up an Auto Cleaner Service which will delete entries from the {@link SmartCache} System on expiry of its TTL (time to live)
     * <p/>
     * <strong>NOTE:</strong>
     * This method has been deprecated. It is HIGHLY RECOMMENDED that you use {@link SmartCache#startAllAutoCleaner()},
     * or {@link SmartCache#startAllAutoCleaner(SmartCacheEventListener)} method to delete the specific entry corresponding to its TTL value.
     *
     * @param EXPIRY_DURATION         The TTL value after which the entries will be deleted from the Smart Cache
     * @param START_TASK_DELAY        The initial delay after which this Auto Cleaner service starts
     * @param REPEAT_TASK_DELAY       The delay after which this Auto Cleaner service repeats itself
     * @param TIME_UNIT               The Unit of time for the previous parameters
     * @param smartCacheEventListener the smart cache event listener
     *
     * @return if the Auto Cleaner Service was setup correctly, otherwise returns
     *
     * @throws SmartCacheException the smart cache exception
     */
    @Deprecated
    boolean startAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT, final SmartCacheEventListener smartCacheEventListener) throws SmartCacheException;

    /**
     * Stops the ongoing Auto Cleaner Service
     * This method has been deprecated. It is HIGHLY RECOMMENDED to use the,
     * {@link SmartCache#stopAllAutoCleaner(boolean)} ()} or
     * the {@link SmartCache#stopAutoCleaner(Object, boolean)} methods.
     */
    @Deprecated
    void stopAutoCleaner();
}
