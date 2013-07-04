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
package com.sohail.alam.mango_pi.jmx.wrapper;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation used to mark and describe a JMX bean operation. The annotated
 * method has to be public and must follow the JMX specification for operations.
 *
 * @author Udo Klimaschewski
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface JMXBeanOperation {
    /**
     * Name of this operation, the method name by default.
     *
     * @return The name of the operation.
     */
    String name() default "";

    /**
     * The description of this operation, empty by default.
     *
     * @return The operation description.
     */
    String description() default "";

    /**
     * Resource bundle key that will be used to load the name from the bundle
     * using the current <code>java.util.Locale</code>. A
     * {@link JMXBean#resourceBundleName()} must be specified in the bean
     * annotation for this to work.
     *
     * @return The resource bundle key for the name.
     */
    String nameKey() default "";

    /**
     * Resource bundle key that will be used to load the description from the
     * bundle using the current <code>java.util.Locale</code>. A
     * {@link JMXBean#resourceBundleName()} must be specified in the bean
     * annotation for this to work.
     *
     * @return The resource bundle key for the description.
     */
    String descriptionKey() default "";

    /**
     * JMX operation impact type, {@link javax.management.MBeanOperationInfo#UNKNOWN} by default.
     *
     * @return The impact type.
     */
    IMPACT_TYPES impactType() default IMPACT_TYPES.UNKNOWN;

    /**
     * An enumeration of possible JMX impact types, used in
     * {@link com.sohail.alam.mango_pi.jmx.wrapper.JMXBeanOperation#impactType()}.
     *
     * @author Udo Klimaschewski
     */
    enum IMPACT_TYPES {
        INFO, ACTION, ACTION_INFO, UNKNOWN
    }

    ;

}
