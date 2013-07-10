/*
 * Copyright 2013 The Mango Pi Project
 *
 * The Mango Pi Project licenses this file to you under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *              http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.sohail.alam.mango_pi.validator.ipport;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This {@link IPPortValidator} class exposes the methods which can validate a given String to be an IP Address
 * or an integer to be a valid Port number.
 * <p/>
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 9/6/13
 * Time: 6:02 PM
 */
public class IPPortValidator {

    private final String IP_V4_REGEX = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    private final String IP_V6_REGEX = "^([\\dA-F]{1,4}:|((?=.*(::))(?!.*\\3.+\\3))\\3?)([\\dA-F]{1,4}(\\3|:\\b)|\\2){5}(([\\dA-F]{1,4}(\\3|:\\b|$)|\\2){2}|(((2[0-4]|1\\d|[1-9])?\\d|25[0-5])\\.?\\b){4})\\z";
    private Pattern VALID_IPV4_PATTERN = null;
    private Pattern VALID_IPV6_PATTERN = null;

    /**
     * Private Constructor for Singleton Class
     */
    private IPPortValidator() throws PatternSyntaxException {
        VALID_IPV4_PATTERN = Pattern.compile(IP_V4_REGEX, Pattern.CASE_INSENSITIVE);
        VALID_IPV6_PATTERN = Pattern.compile(IP_V6_REGEX, Pattern.CASE_INSENSITIVE);
    }

    public static IPPortValidator validate() {
        return SingletonHolder.instance;
    }

    /**
     * This method verifies the given String to be a valid IPv4 address,
     * however this method by no means finds whether the given String is the correct IP that you are looking
     * for, or whether your machine can reach the given address.
     *
     * @param ip the String representing the IP address to be verified
     *
     * @return {@code true} if given String is of IPv4 type, otherwise {@code false}
     */
    public boolean isIPv4(String ip) {
        return VALID_IPV4_PATTERN.matcher(ip).matches();
    }

    /**
     * This method verifies the given String to be a valid IPv6 address,
     * however this method by no means finds whether the given String is the correct IP that you are looking
     * for, or whether your machine can reach the given address.
     *
     * @param ip the String representing the IP address to be verified
     *
     * @return {@code true} if given String is of IPv6 type, otherwise {@code false}
     */
    public boolean isIPv6(String ip) {
        return VALID_IPV6_PATTERN.matcher(ip).matches();
    }

    /**
     * This method verifies the given String to be a valid IP address,
     * however this method by no means finds whether the given String is the correct IP that you are looking
     * for, or whether your machine can reach the given address.
     *
     * @param ip the String representing the IP address to be verified
     *
     * @return {@code true} if given String is of IP type, otherwise {@code false}
     */
    public boolean isValidIP(String ip) {
        return (isIPv4(ip) || isIPv6(ip));
    }

    /**
     * This method verifies the given integer parameter to be a valid Port Number,
     * however this method by no means finds whether the given integer is the correct Port that you are looking
     * for, or whether your machine can connect using the given port.
     *
     * @param port the port
     *
     * @return the boolean
     */
    public boolean isValidPort(int port) {
        return (port > 0 && port <= 65535);
    }

    /**
     * This method extracts the IP address from a given String of the format IP:PORT
     *
     * @param address the address in the form IP:PORT (Example: 10.10.10.10:9090)
     *
     * @return the IP from the given address (Example: 10.10.10.10)
     */
    public String extractIP(String address) {

        String arr[];
        String ip = null;

        if (address.contains(":")) {
            arr = address.split(":");
            ip = arr[0];
        }
        return ip;
    }

    /**
     * This method extracts the Port number from a given String of the format IP:PORT
     *
     * @param address the address in the form IP:PORT (Example: 10.10.10.10:9090)
     *
     * @return the Port from the given address (Example: 9090),
     *         and -1 if either the PORT is null or empty, or the port number does not lie in correct range of Ports
     *
     * @throws NumberFormatException If the PORT is not a number
     */
    public int extractPort(String address) throws NumberFormatException {

        String arr[] = address.split(":");
        if (arr[1] != null && !arr[1].isEmpty()) {
            int port = 0;
            if (isValidPort((port = Integer.parseInt(arr[1])))) {
                return port;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * The type Singleton holder.
     * <p/>
     * Initialization on Demand Holder (IODH) idiom which requires very little code and
     * has zero synchronization overhead. Zero, as in even faster than volatile.
     * IODH requires the same number of lines of code as plain old synchronization, and it's faster than DCL!
     * <p/>
     * {@code SOURCE: http://blog.crazybob.org/2007/01/lazy-loading-singletons.html}
     */
    private static class SingletonHolder {
        /**
         * The Instance.
         */
        static IPPortValidator instance = new IPPortValidator();
    }

}
