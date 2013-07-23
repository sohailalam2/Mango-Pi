package com.sohail.alam.mango_pi.smart.cache;

import java.text.SimpleDateFormat;
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
    public final String CREATION_TIME;
    /**
     * The DELETION TIME.
     */
    public final String DELETION_TIME;
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
     * This holds tha total numberOfEntries of the data corresponding to the given key
     */
    public long DATA_SIZE;

    /**
     * Instantiates a new Smart cache history pojo.
     *
     * @param creationTime the creation time
     * @param deleteReason the delete reason
     * @param key          the key
     */
    public SmartCacheHistoryPojo(long creationTime, String deleteReason, K key, long dataSize) {
        this.CREATION_TIME = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(creationTime));
        this.DELETE_REASON = deleteReason;
        this.DELETION_TIME = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(System.currentTimeMillis()));
        this.KEY = key;
        this.SMART_CACHE_DATA_NAME = "";
        this.DATA_SIZE = dataSize;
    }

    /**
     * Instantiates a new Smart cache history pojo.
     *
     * @param deleteReason the delete reason
     * @param key          the key
     * @param pojo         the pojo
     */
    public SmartCacheHistoryPojo(String deleteReason, K key, V pojo) {
        this.CREATION_TIME = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(pojo.getCREATION_TIME()));
        this.DELETE_REASON = deleteReason;
        this.DELETION_TIME = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(System.currentTimeMillis()));
        this.KEY = key;
        this.SMART_CACHE_DATA_NAME = pojo.SMART_CACHE_DATA_NAME;
        this.DATA_SIZE = pojo.size();
    }

}
