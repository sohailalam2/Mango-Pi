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
 * Annotation type used to mark and describe a class as a JMX bean.
 *
 * @author Udo Klimaschewski
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface JMXBean {
    /**
     * The name of the class describing this bean, default is the full Java
     * class name.
     *
     * @return The class name.
     */
    String className() default "";

    /**
     * The description for this bean, empty by default.
     *
     * @return The description
     */
    String description() default "";

    /**
     * Resource bundle key that will be used to load the description text from
     * the bundle using the current {@link java.util.Locale#getDefault()}.
     *
     * @return The resource bundle key.
     */
    String descriptionKey() default "";

    /**
     * If a resource bundle name is set, the descriptions and names of beans,
     * attributes, operations and parameters can be placed in a resource bundle.
     * Typically, this is a properties file for each locale or language. If a
     * resource bundle name is specified, the the annotation attributes
     * <code>nameKey</code> and <code>descriptionKey</code> will be used to
     * search the bundle for translations.
     *
     * @return The resource bundle name.
     */
    String resourceBundleName() default "";
}
