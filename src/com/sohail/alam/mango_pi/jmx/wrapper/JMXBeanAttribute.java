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
 * Annotation used to mark and describe JMX bean attributes, by marking methods
 * in a class. If only the getter is marked, the attribute is read-only. If only
 * the setter is marked, the attribute write-only. if both methods are marked,
 * then the attribute is read-write enabled. Description and name can be
 * specified on either the setter or getter. The annotated method(s) has/have to
 * be public and must follow the JMX specification for attributes.
 *
 * @author Udo Klimaschewski
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface JMXBeanAttribute {
    /**
     * Name of this attribute, by default the standard Java bean syntax will be
     * used to create a name out of the getter/setter name.
     *
     * @return The name of the method.
     */
    String name() default "";

    /**
     * The description of this attribute, empty by default.
     *
     * @return The attribute description.
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
