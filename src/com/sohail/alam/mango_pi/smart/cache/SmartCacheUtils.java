package com.sohail.alam.mango_pi.smart.cache;

import java.io.File;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 22/7/13
 * Time: 8:06 PM
 */
class SmartCacheUtils {

    public static String createLine(int number, char character) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        for (int i = 0; i < number; i++)
            builder.append(character);
        builder.append("\n");

        return builder.toString();
    }

    public static void createDir() {
        File directory = new File("./SMART_CACHE");
        if (!directory.exists())
            directory.mkdirs();
    }
}
