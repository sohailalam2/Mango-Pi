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

package com.sohail.alam.mango_pi.examples;

import com.sohail.alam.mango_pi.examples.smart.cache.TestSmartCache;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 9/6/13
 * Time: 10:54 AM
 */
public class TestMangoPiBootstrap {

    public static void main(String[] args) {

        System.out.println("Starting Smart Cache Test");
        new TestSmartCache();
    }
}
