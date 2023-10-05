/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.log4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

import org.apache.log4j.helpers.ThreadLocalMap;

/**
 * The MDC class is similar to the {@link NDC} class except that it is based on a map instead of a stack. It provides
 * <em>mapped diagnostic contexts</em>. A <em>Mapped Diagnostic Context</em>, or MDC in short, is an instrument for
 * distinguishing interleaved log output from different sources. Log output is typically interleaved when a server
 * handles multiple clients near-simultaneously.
 * <p/>
 * <p>
 * <b><em>The MDC is managed on a per thread basis</em></b>. A child thread automatically inherits a <em>copy</em> of
 * the mapped diagnostic context of its parent.
 * <p/>
 * <p>
 * The MDC class requires JDK 1.2 or above. Under JDK 1.1 the MDC will always return empty values but otherwise will not
 * affect or harm your application.
 * </p>
 *
 * <p>
 * Attention: the application is required to clean up. In web applications this can happen with creating a Servlet
 * Filter and overriding the onFilter method like:
 * </p>
 *
 * <pre>
 * try {
 *     MDC.put(myKey);
 *     chain.doFilter(request, response);
 * } finally {
 *     MDC.remove(myKey);
 * }
 * </pre>
 *
 * <p>
 * Please also see: {@link http://logging.apache.org/log4j/1.2/faq.html#mdcmemoryleak}
 * </p>
 *
 * @author Ceki G&uuml;lc&uuml;
 * @since 1.2
 */
public class MDC {

    final static MDC mdc = new MDC();

    static final int HT_SIZE = 7;

    final static boolean java1 = false;

    Object tlm;

    private Method removeMethod;

    private MDC() {
        if (!java1) {
            tlm = new ThreadLocalMap();
        }

        try {
            removeMethod = ThreadLocal.class.getMethod("remove");
        } catch (final NoSuchMethodException e) {
            // don't do anything - java prior 1.5
        }
    }

    /**
     * Put a context value (the <code>o</code> parameter) as identified with the <code>key</code> parameter into the
     * current thread's context map.
     * <p/>
     * <p>
     * If the current thread does not have a context map it is created as a side effect.
     */
    public static void put(final String key, final Object o) {
        if (mdc != null) {
            mdc.put0(key, o);
        }
    }

    /**
     * Get the context identified by the <code>key</code> parameter.
     * <p/>
     * <p>
     * This method has no side effects.
     */
    public static Object get(final String key) {
        if (mdc != null) {
            return mdc.get0(key);
        }
        return null;
    }

    /**
     * Remove the the context identified by the <code>key</code> parameter.
     */
    public static void remove(final String key) {
        if (mdc != null) {
            mdc.remove0(key);
        }
    }

    /**
     * Get the current thread's MDC as a hashtable. This method is intended to be used internally.
     */
    public static Hashtable<?, ?> getContext() {
        if (mdc != null) {
            return mdc.getContext0();
        } else {
            return null;
        }
    }

    /**
     * Remove all values from the MDC.
     *
     * @since 1.2.16
     */
    public static void clear() {
        if (mdc != null) {
            mdc.clear0();
        }
    }

    private void put0(final String key, final Object o) {
        if (java1 || tlm == null) {
            return;
        } else {
            Hashtable<String, Object> ht = (Hashtable<String, Object>) ((ThreadLocalMap) tlm).get();
            if (ht == null) {
                ht = new Hashtable<>(HT_SIZE);
                ((ThreadLocalMap) tlm).set(ht);
            }
            ht.put(key, o);
        }
    }

    private Object get0(final String key) {
        if (java1 || tlm == null) {
            return null;
        } else {
            final Hashtable<?, ?> ht = (Hashtable<?, ?>) ((ThreadLocalMap) tlm).get();
            if (ht != null && key != null) {
                return ht.get(key);
            } else {
                return null;
            }
        }
    }

    private void remove0(final String key) {
        if (!java1 && tlm != null) {
            final Hashtable<?, ?> ht = (Hashtable<?, ?>) ((ThreadLocalMap) tlm).get();
            if (ht != null) {
                ht.remove(key);
                // clean up if this was the last key
                if (ht.isEmpty()) {
                    clear0();
                }
            }
        }
    }

    private Hashtable<?, ?> getContext0() {
        if (java1 || tlm == null) {
            return null;
        } else {
            return (Hashtable<?, ?>) ((ThreadLocalMap) tlm).get();
        }
    }

    private void clear0() {
        if (!java1 && tlm != null) {
            final Hashtable<?, ?> ht = (Hashtable<?, ?>) ((ThreadLocalMap) tlm).get();
            if (ht != null) {
                ht.clear();
            }
            if (removeMethod != null) {
                // java 1.3/1.4 does not have remove - will suffer from a memory leak
                try {
                    removeMethod.invoke(tlm);
                } catch (final IllegalAccessException e) {
                    // should not happen
                } catch (final InvocationTargetException e) {
                    // should not happen
                }
            }
        }
    }
}
