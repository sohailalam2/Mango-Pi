package com.sohail.alam.mango_pi.smart.cache.mbeans;

import com.sohail.alam.mango_pi.smart.cache.SmartCacheException;
import com.sohail.alam.mango_pi.smart.cache.SmartCachePojo;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/7/13
 * Time: 12:50 PM
 */
@Deprecated
public interface DeprecatedSmartCacheManagerMBean<K, V extends SmartCachePojo>
        extends AbstractSmartCacheManagerMBean<K, V> {

    /**
     * Start auto cleaner.
     *
     * @param EXPIRY_DURATION   the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY  The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY The time after which the auto cleaner task repeats itself
     * @param TIME_UNIT         The time unit in which the above are defined
     *
     * @return the boolean
     *
     * @throws com.sohail.alam.mango_pi.smart.cache.SmartCacheException
     *          the smart cache exception
     */
    @Deprecated
    public boolean startAutoCleanerWithTimeUnit(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT) throws SmartCacheException;

    /**
     * Start auto cleaner.
     *
     * @param EXPIRY_DURATION   the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY  The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY The time after which the auto cleaner task repeats itself
     *
     * @return the boolean
     *
     * @throws SmartCacheException the smart cache exception
     */
    @Deprecated
    public boolean startAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY) throws SmartCacheException;

    /**
     * Start auto cleaner reflection.
     *
     * @param EXPIRY_DURATION       the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY      The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY     The time after which the auto cleaner task repeats itself
     * @param TIME_UNIT             The time unit in which the above are defined
     * @param CALLBACK_CLASS_OBJECT The Object of the Class in which the Callback method resides
     * @param CALLBACK_METHOD       The Callback Method
     *
     * @return the boolean
     *
     * @throws SmartCacheException the smart cache exception
     */
    @Deprecated
    public boolean startAutoCleanerReflection(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT, final Object CALLBACK_CLASS_OBJECT, final Method CALLBACK_METHOD) throws SmartCacheException;

    /**
     * Stop auto cleaner.
     */
    @Deprecated
    public void stopAutoCleaner();

    /**
     * Restart auto cleaner.
     *
     * @param EXPIRY_DURATION   the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY  The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY The time after which the auto cleaner task repeats itself
     * @param TIME_UNIT         The time unit in which the above are defined
     *
     * @return the boolean
     *
     * @throws SmartCacheException the smart cache exception
     */
    @Deprecated
    public boolean rescheduleAutoCleanerWithTimeUnit(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT) throws SmartCacheException;

    /**
     * Restart auto cleaner.
     *
     * @param EXPIRY_DURATION   the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY  The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY The time after which the auto cleaner task repeats itself
     *
     * @return the boolean
     *
     * @throws SmartCacheException the smart cache exception
     */
    @Deprecated
    public boolean rescheduleAutoCleaner(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY) throws SmartCacheException;

    /**
     * Restart auto cleaner reflection.
     *
     * @param EXPIRY_DURATION       the The TTL Value for the Cache entry, after which it will be auto deleted.
     * @param START_TASK_DELAY      The time after which the auto cleaner task starts
     * @param REPEAT_TASK_DELAY     The time after which the auto cleaner task repeats itself
     * @param TIME_UNIT             The time unit in which the above are defined
     * @param CALLBACK_CLASS_OBJECT The Object of the Class in which the Callback method resides
     * @param CALLBACK_METHOD       The Callback Method
     *
     * @return the boolean
     *
     * @throws SmartCacheException the smart cache exception
     */
    @Deprecated
    public boolean rescheduleAutoCleanerReflection(final long EXPIRY_DURATION, final long START_TASK_DELAY, final long REPEAT_TASK_DELAY, final TimeUnit TIME_UNIT, final Object CALLBACK_CLASS_OBJECT, final Method CALLBACK_METHOD) throws SmartCacheException;

}
