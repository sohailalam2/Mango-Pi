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

package com.sohail.alam.mango_pi.algorithms;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This {@link RoundRobin} class is responsible for fetching an element from the given List using
 * Round Robin algorithm.
 * <p/>
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 11/6/13
 * Time: 8:10 AM
 */
public class RoundRobin<K extends List<V>, V> {

    // The counter for Round Robin
    private static AtomicInteger RRCounter = new AtomicInteger(0);

    /**
     * Public Constructor
     */
    public RoundRobin() {
    }

    /**
     * This method gets an element of type {@link V} from the given List
     *
     * @param list The List containing the elements
     *
     * @return One element from the List using Round Robin Algorithm
     *
     * @throws IndexOutOfBoundsException May Throw IndexOutOfBoundsException
     */
    public V get(K list) throws IndexOutOfBoundsException {
        V selectedElement;
        if (list.isEmpty()) {
            return null;
        }
        if (RRCounter.get() < list.size()) {
            selectedElement = list.get(RRCounter.get());
            RRCounter.set(RRCounter.incrementAndGet() % list.size());
        } else {
            selectedElement = list.get(0);
            RRCounter.set(0);
        }
        return selectedElement;
    }

}
