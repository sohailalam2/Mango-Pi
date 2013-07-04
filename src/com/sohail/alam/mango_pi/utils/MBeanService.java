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

package com.sohail.alam.mango_pi.utils;

import com.sohail.alam.mango_pi.jmx.wrapper.JMXBeanWrapper;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * The type M bean service.
 *
 * @author sohail.alam
 */
public class MBeanService {

    /**
     * Private Constructor
     */
    private MBeanService() {
    }

    /**
     * Start the MBean Service
     *
     * @param MBEAN_OBJECT The Class Object which is used for starting the MBean
     *                     Service (usually it is this)
     * @param MBEAN_NAME   The name of the MBean
     *
     * @throws MalformedObjectNameException
     * @throws IntrospectionException
     * @throws NotCompliantMBeanException
     * @throws InstanceAlreadyExistsException
     * @throws MBeanRegistrationException
     */
    public static void startService(final Object MBEAN_OBJECT, final String MBEAN_NAME) throws MalformedObjectNameException, IntrospectionException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {

        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        // register the jmx html adapter
        ObjectName adapterName;
        adapterName = new ObjectName(MBEAN_NAME);
        JMXBeanWrapper jmxBeanWrapper = new JMXBeanWrapper(MBEAN_OBJECT);
        server.registerMBean(jmxBeanWrapper, adapterName);
    }

    /**
     * Stop the given MBean Service
     *
     * @param MBEAN_NAME The name of the MBean\
     *
     * @throws MalformedObjectNameException
     * @throws MBeanRegistrationException
     * @throws InstanceNotFoundException
     */
    public static void stopService(final String MBEAN_NAME) throws MalformedObjectNameException, MBeanRegistrationException, InstanceNotFoundException {
        ManagementFactory.getPlatformMBeanServer().unregisterMBean(new ObjectName(MBEAN_NAME));
    }
}
