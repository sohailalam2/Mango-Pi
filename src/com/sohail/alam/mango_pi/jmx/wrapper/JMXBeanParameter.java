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
 * Annotation used to describe a JMX bean operation parameter. The annotated
 * method parameter has to follow the JMX specification for operation
 * parameters. All parameters of a method will be used for the operation, even
 * if they are not annotated. Annotation of parameters is used only to further
 * describe the parameter and set a more meaningful name.
 *
 * @author Udo Klimaschewski
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface JMXBeanParameter {
    /**
     * Name of this parameter, <i>param1, param2, param3 etc.</i> by default.
     *
     * @return The name of the parameter.
     */
    String name() default "";

    /**
     * The description of this parameter, empty by default.
     *
     * @return The parameter description.
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
}
