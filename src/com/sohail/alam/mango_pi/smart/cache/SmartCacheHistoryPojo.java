package com.sohail.alam.mango_pi.smart.cache;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: sohail.alam
 * Date: 6/7/13
 * Time: 5:05 PM
 */
class SmartCacheHistoryPojo<K, V extends SmartCachePojo> {

    /**
     * The CREATION TIME.
     */
    public final Date CREATION_TIME;
    /**
     * The DELETION TIME.
     */
    public final Date DELETION_TIME;
    /**
     * The DELETE REASON.
     */
    public final String DELETE_REASON;
    /**
     * The KEY.
     */
    public final K KEY;
    /**
     * This represents the name of the Smart Cache Data (the current data).
     */
    public String SMART_CACHE_DATA_NAME;

    /**
     * Instantiates a new Smart cache history pojo.
     *
     * @param deleteReason the delete reason
     * @param key          the key
     * @param creationTime the creation time
     */
    public SmartCacheHistoryPojo(String deleteReason, K key, long creationTime) {
        this.CREATION_TIME = new Date(creationTime);
        this.DELETE_REASON = deleteReason;
        this.DELETION_TIME = new Date(System.nanoTime());
        this.KEY = key;
        this.SMART_CACHE_DATA_NAME = "";
    }

    /**
     * Instantiates a new Smart cache history pojo.
     *
     * @param deleteReason the delete reason
     * @param key          the key
     * @param pojo         the pojo
     */
    public SmartCacheHistoryPojo(String deleteReason, K key, V pojo) {
        this.CREATION_TIME = new Date(pojo.getTIME_STAMP());
        this.DELETE_REASON = deleteReason;
        this.DELETION_TIME = new Date(System.nanoTime());
        this.KEY = key;
        this.SMART_CACHE_DATA_NAME = pojo.SMART_CACHE_DATA_NAME;
    }

}
