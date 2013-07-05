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

package com.sohail.alam.mango_pi.smart.cache;

/**
 * The {@link SmartCacheException}s are thrown in cases where the SmartCache System is
 * not certain that whatever it has implemented will certainly work, for example,
 * when you start the {@link SmartCache#startAutoCleaner(long, long, long, java.util.concurrent.TimeUnit, Object, java.lang.reflect.Method)}
 * with the callback method, the SmartCache System may not be able to invoke the method and hence
 * throws {@link SmartCacheException}.
 * <p/>
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 4/7/13
 * Time: 8:59 PM
 */
public class SmartCacheException extends Exception {
    public SmartCacheException() {
        super();
    }

    public SmartCacheException(String message) {
        super(message);
    }

    public SmartCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartCacheException(Throwable cause) {
        super(cause);
    }
}
