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

package com.sohail.alam.mango_pi.examples.validator.ipport;

import com.sohail.alam.mango_pi.validator.ipport.IPPort;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 9/6/13
 * Time: 6:28 PM
 */
public class ValidateIPPort {

    public static void main(String[] args) {

        // TRUE
        System.out.println("Validate IP   Address => " + IPPort.validate().isValidIP("127.0.0.1"));
        System.out.println("Validate IPv4 Address => " + IPPort.validate().isIPv4("127.0.0.1"));
        System.out.println("Validate IPv6 Address => " + IPPort.validate().isIPv6("2001:0db8:0000:0000:0000:ff00:0042:8329"));
        System.out.println("Validate IPv6 Address => " + IPPort.validate().isIPv6("2001:db8:0:0:0:ff00:42:8329"));
        System.out.println("Validate IPv6 Address => " + IPPort.validate().isIPv6("2001:db8::ff00:42:8329"));
        System.out.println("Validate IPv6 Address => " + IPPort.validate().isIPv6("0:0:0:0:0:0:0:1"));
        System.out.println("Validate Port Number  => " + IPPort.validate().isValidPort(1234));

        // FALSE
        System.out.println("Validate IPv6 Address => " + IPPort.validate().isIPv6("2001::1::3F"));
        System.out.println("Validate Port Number  => " + IPPort.validate().isValidPort(0));
        System.out.println("Validate Port Number  => " + IPPort.validate().isValidPort(65536));
    }
}
