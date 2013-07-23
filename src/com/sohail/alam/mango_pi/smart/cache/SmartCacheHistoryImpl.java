package com.sohail.alam.mango_pi.smart.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 6/7/13
 * Time: 10:16 PM
 */
class SmartCacheHistoryImpl<K, V extends SmartCachePojo> implements SmartCacheHistory<K, V> {

    protected static final SmartCacheHistory SMART_CACHE_HISTORY = new SmartCacheHistoryImpl();
    private final ConcurrentHashMap<K, SmartCacheHistoryPojo> HISTORY;
    private final ExecutorService HISTORY_PURGER;
    private AtomicLong maxHistoryCount;
    private String filePath;

    private SmartCacheHistoryImpl() {
        HISTORY = new ConcurrentHashMap<K, SmartCacheHistoryPojo>();
        HISTORY_PURGER = Executors.newSingleThreadExecutor();
        maxHistoryCount = new AtomicLong(1000);
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
        if (HISTORY.size() >= maxHistoryCount.get())
            try {
                purgeSmartCacheHistory(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }

        HISTORY.put(key, new SmartCacheHistoryPojo<K, V>(reason, key, value));
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
    public String smartCacheKeyHistory(K key) {
        StringBuilder builder = new StringBuilder();
        SmartCacheHistoryPojo found = HISTORY.get(key);
        builder.append("Smart Cache History: ");
        builder.append(SmartCacheUtils.createLine(175, '-'));
        builder.append(String.format("%-15s", "REASON"));
        builder.append(String.format("%-50s", "KEY"));
        builder.append(String.format("%-20s", "NAME"));
        builder.append(String.format("%-20s", "SIZE"));
        builder.append(String.format("%-35s", "CREATION TIME"));
        builder.append(String.format("%-35s", "DELETION TIME"));
        builder.append(SmartCacheUtils.createLine(175, '-'));

        builder.append(String.format("%-15s", found.DELETE_REASON));
        builder.append(String.format("%-50s", found.KEY));
        builder.append(String.format("%-20s", found.DATA_SIZE));
        builder.append(String.format("%-20s", found.SMART_CACHE_DATA_NAME));
        builder.append(String.format("%-35s", found.CREATION_TIME));
        builder.append(String.format("%-35s", found.DELETION_TIME));
        builder.append("\r\n");

        builder.append(SmartCacheUtils.createLine(175, '-'));
        return builder.toString();
    }

    /**
     * View history.
     *
     * @param reason the reason
     *
     * @return the string
     */
    @Override
    public String smartCacheReasonHistory(String reason) {
        StringBuilder builder = new StringBuilder();
        SmartCacheHistoryPojo foundPojo = null;
        boolean found = false;

        builder.append("Smart Cache History: ");
        builder.append(SmartCacheUtils.createLine(175, '-'));
        builder.append(String.format("%-15s", "REASON"));
        builder.append(String.format("%-50s", "KEY"));
        builder.append(String.format("%-20s", "NAME"));
        builder.append(String.format("%-20s", "SIZE"));
        builder.append(String.format("%-35s", "CREATION TIME"));
        builder.append(String.format("%-35s", "DELETION TIME"));
        builder.append(SmartCacheUtils.createLine(175, '-'));

        for (K key : HISTORY.keySet()) {
            if ((foundPojo = HISTORY.get(key)).DELETE_REASON.equalsIgnoreCase(reason)) {
                builder.append(String.format("%-15s", foundPojo.DELETE_REASON));
                builder.append(String.format("%-50s", foundPojo.KEY));
                builder.append(String.format("%-20s", foundPojo.SMART_CACHE_DATA_NAME));
                builder.append(String.format("%-20s", foundPojo.DATA_SIZE));
                builder.append(String.format("%-35s", foundPojo.CREATION_TIME));
                builder.append(String.format("%-35s", foundPojo.DELETION_TIME));
                builder.append("\r\n");
                found = true;
            }
        }
        if (!found) {
            builder.append("There are no history corresponding to the reason: " + reason);
            builder.append("\r\n");
        }

        builder.append(SmartCacheUtils.createLine(175, '-'));
        return builder.toString();
    }

    /**
     * View history.
     *
     * @return the string
     */
    @Override
    public String smartCacheAllHistory() {
        StringBuilder builder = new StringBuilder();
        SmartCacheHistoryPojo foundPojo = null;

        builder.append("Smart Cache History: ");
        builder.append(SmartCacheUtils.createLine(175, '-'));
        builder.append(String.format("%-15s", "REASON"));
        builder.append(String.format("%-50s", "KEY"));
        builder.append(String.format("%-20s", "NAME"));
        builder.append(String.format("%-20s", "SIZE"));
        builder.append(String.format("%-35s", "CREATION TIME"));
        builder.append(String.format("%-35s", "DELETION TIME"));
        builder.append(SmartCacheUtils.createLine(175, '-'));

        for (K key : HISTORY.keySet()) {
            foundPojo = HISTORY.get(key);
            builder.append(String.format("%-15s", foundPojo.DELETE_REASON));
            builder.append(String.format("%-50s", foundPojo.KEY));
            builder.append(String.format("%-20s", foundPojo.DATA_SIZE));
            builder.append(String.format("%-20s", foundPojo.SMART_CACHE_DATA_NAME));
            builder.append(String.format("%-35s", foundPojo.CREATION_TIME));
            builder.append(String.format("%-35s", foundPojo.DELETION_TIME));
            builder.append("\r\n");
        }
        builder.append(SmartCacheUtils.createLine(175, '-'));
        return builder.toString();
    }

    /**
     * Set the maximum number of entries after which the History is
     * deleted permanently.
     *
     * @param maxHistoryCount the max element count
     */
    @Override
    public void maxHistoryCount(int maxHistoryCount) {
        this.maxHistoryCount.set(maxHistoryCount);
    }

    /**
     * Purges the contents of History into a user defined file.
     * By default the SmartCache will dump the data into a file named -
     * SmartCacheHistory_(current-date/time).txt
     *
     * @param filePath the absolute file path for the dump file.
     */
    @Override
    public String purgeSmartCacheHistory(String filePath) throws Exception {
        this.filePath = filePath;

        return HISTORY_PURGER.submit(new HistoryPurgerClass(filePath)).get();
    }

    /**
     * Gets file path.
     *
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets file path.
     *
     * @param filePath the file path
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Class Responsible for purging the {@link SmartCacheHistory} into a file
     * for CDR purposes.
     */
    private final class HistoryPurgerClass implements Callable<String> {
        private File file;

        /**
         * Instantiates a new History purger class.
         *
         * @param filePath the file path
         */
        public HistoryPurgerClass(String filePath) {
            String directory = "./SMART_CACHE";
            String fileName = "History_" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + ".txt";

            if (filePath != null) {
                if (!filePath.isEmpty()) {
                    if (filePath.contains("/")) {
                        directory = filePath.substring(0, filePath.lastIndexOf("/") + 1);
                        fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                    } else if (filePath.contains("\\")) {
                        directory = filePath.substring(0, filePath.lastIndexOf("\\") + 1);
                        fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
                    } else {
                        fileName = filePath;
                    }
                }
            }
            // Create the directories if not present
            File dir = new File(directory);
            dir.mkdirs();

            file = new File(dir.getAbsoluteFile() + "/" + fileName);
        }

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         *
         * @throws Exception if unable to compute a result
         */
        @Override
        public String call() throws Exception {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(smartCacheAllHistory().getBytes());
            fos.flush();
            fos.close();
            return "Smart Cache History was successfully purged into file => " +
                    file.getAbsolutePath();
        }
    }
}
