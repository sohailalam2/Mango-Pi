package com.sohail.alam.mango_pi.smart.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 6/7/13
 * Time: 10:16 PM
 */
class SmartCacheHistoryImpl<K, V extends SmartCachePojo> implements SmartCacheHistory<K, V> {

    protected static final SmartCacheHistory HISTORY = new SmartCacheHistoryImpl();
    private final ConcurrentHashMap<K, SmartCacheHistoryPojo> SMART_CACHE_HISTORY;
    private AtomicInteger maxElementCount = new AtomicInteger(1000);

    private SmartCacheHistoryImpl() {
        SMART_CACHE_HISTORY = new ConcurrentHashMap<K, SmartCacheHistoryPojo>();
    }

    /**
     * Add to history.
     *
     * @param reason the reason
     * @param key    the key
     * @param value  the value
     */
    @Override
    public void addToHistory(String reason, K key, V value) {
        if (SMART_CACHE_HISTORY.size() >= maxElementCount.get())
            SMART_CACHE_HISTORY.clear();

        SMART_CACHE_HISTORY.put(key, new SmartCacheHistoryPojo<K, V>(reason, key, value));
    }

    /**
     * Add all to history.
     *
     * @param reason  the reason
     * @param dataMap the data map
     */
    @Override
    public void addAllToHistory(String reason, ConcurrentMap<K, V> dataMap) {
        if (dataMap != null) {
            for (K key : dataMap.keySet()) {
                addToHistory(reason, key, dataMap.get(key));
            }
        }
    }

    /**
     * View history.
     *
     * @param key the key
     *
     * @return the string
     */
    @Override
    public String viewHistoryForKey(K key) {
        StringBuffer buffer = new StringBuffer();
        SmartCacheHistoryPojo found = SMART_CACHE_HISTORY.get(key);
        buffer.append("Smart Cache History: ");
        buffer.append("\n------------------------------------------------------------------------------------------\n");
        buffer.append(String.format("%-15s", "REASON"));
        buffer.append(String.format("%-25s", "KEY"));
        buffer.append(String.format("%-35s", "CREATION TIME"));
        buffer.append(String.format("%-35s", "DELETION TIME"));
        buffer.append("\n------------------------------------------------------------------------------------------\n");
        buffer.append(String.format("%-15s", found.DELETE_REASON));
        buffer.append(String.format("%-25s", found.KEY));
        buffer.append(String.format("%-35s", found.CREATION_TIME));
        buffer.append(String.format("%-35s", found.DELETION_TIME));
        buffer.append("\n");
        return buffer.toString();
    }

    /**
     * View history.
     *
     * @param reason the reason
     *
     * @return the string
     */
    @Override
    public String viewHistoryForReason(String reason) {
        StringBuffer buffer = new StringBuffer();
        SmartCacheHistoryPojo foundPojo = null;
        boolean found = false;

        buffer.append("Smart Cache History: ");
        buffer.append("\n------------------------------------------------------------------------------------------\n");
        buffer.append(String.format("%-15s", "REASON"));
        buffer.append(String.format("%-25s", "KEY"));
        buffer.append(String.format("%-35s", "CREATION TIME"));
        buffer.append(String.format("%-35s", "DELETION TIME"));
        buffer.append("\n------------------------------------------------------------------------------------------\n");

        for (K key : SMART_CACHE_HISTORY.keySet()) {
            if ((foundPojo = SMART_CACHE_HISTORY.get(key)).DELETE_REASON.equals(reason)) {
                buffer.append(String.format("%-15s", foundPojo.DELETE_REASON));
                buffer.append(String.format("%-25s", foundPojo.KEY));
                buffer.append(String.format("%-35s", foundPojo.CREATION_TIME));
                buffer.append(String.format("%-35s", foundPojo.DELETION_TIME));
                buffer.append("\n");
                found = true;
            }
        }
        if (!found) {
            buffer.append("There are no history corresponding to the reason: " + reason);
            buffer.append("\n");
        }

        return buffer.toString();
    }

    /**
     * View history.
     *
     * @return the string
     */
    @Override
    public String viewAllHistory() {
        StringBuffer buffer = new StringBuffer();
        SmartCacheHistoryPojo foundPojo = null;

        buffer.append("Smart Cache History: ");
        buffer.append("\n------------------------------------------------------------------------------------------\n");
        buffer.append(String.format("%-15s", "REASON"));
        buffer.append(String.format("%-25s", "KEY"));
        buffer.append(String.format("%-35s", "CREATION TIME"));
        buffer.append(String.format("%-35s", "DELETION TIME"));
        buffer.append("\n------------------------------------------------------------------------------------------\n");

        for (K key : SMART_CACHE_HISTORY.keySet()) {
            foundPojo = SMART_CACHE_HISTORY.get(key);
            buffer.append(String.format("%-15s", foundPojo.DELETE_REASON));
            buffer.append(String.format("%-25s", foundPojo.KEY));
            buffer.append(String.format("%-35s", foundPojo.CREATION_TIME));
            buffer.append(String.format("%-35s", foundPojo.DELETION_TIME));
            buffer.append("\n");
        }
        return buffer.toString();
    }

    /**
     * Set the maximum number of entries after which the History is
     * deleted permanently.
     *
     * @param maxElementCount the max element count
     */
    @Override
    public void autoDeleteHistory(int maxElementCount) {
        this.maxElementCount.set(maxElementCount);
    }
}
