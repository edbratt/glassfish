/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.admin.amx.util;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
Provides:
- methods to convert arrays of primitive types to corresponding arrays of Object types
- conversion to/from Set
 */
public final class ArrayConversion
{
    private ArrayConversion()
    {
        // disallow instantiation
    }

    private static Object[] convert(Object simpleArray)
    {
        if (!ClassUtil.objectIsPrimitiveArray(simpleArray))
        {
            throw new IllegalArgumentException();
        }

        final Class theClass = ClassUtil.getArrayElementClass(simpleArray.getClass());

        final int numItems = Array.getLength(simpleArray);

        final Class elementClass = ClassUtil.primitiveClassToObjectClass(theClass);

        final Object[] result = (Object[]) Array.newInstance(elementClass, numItems);

        for (int i = 0; i < numItems; ++i)
        {
            result[i] = Array.get(simpleArray, i);
        }

        return (result);
    }

    /**
    Convert an an array of primitive types to an array of Objects of non-primitive
    types eg int to Integer.

    @param array        the array to convert
     */
    public static Object[] toAppropriateType(Object array)
    {
        return convert(array);
    }

    public static Boolean[] toBooleans(boolean[] array)
    {
        return ((Boolean[]) convert(array));
    }

    public static Character[] toCharacters(char[] array)
    {
        return ((Character[]) convert(array));
    }

    public static Byte[] toBytes(byte[] array)
    {
        return ((Byte[]) convert(array));
    }

    public static Short[] toShorts(short[] array)
    {
        return ((Short[]) convert(array));
    }

    public static Integer[] toIntegers(int[] array)
    {
        return ((Integer[]) convert(array));
    }

    public static Long[] toLongs(long[] array)
    {
        return ((Long[]) convert(array));
    }

    public static Float[] toFloats(float[] array)
    {
        return ((Float[]) convert(array));
    }

    public static Double[] toDoubles(double[] array)
    {
        return ((Double[]) convert(array));
    }

    /**
    Create an array whose type is elementType[] of specified size.

    @param elementType        the type of each entry of the array
    @param size                        the number of elements
     */
    public static Object[] createObjectArrayType(final Class elementType, final int size)
    {
        final Object[] result = (Object[]) Array.newInstance(elementType, size);

        return (result);
    }

    public static Object[] subArray(final Object[] in, int start, int end)
    {
        final int count = 1 + (end - start);
        final Object[] result = (Object[]) Array.newInstance(ClassUtil.getArrayElementClass(in.getClass()), count);

        for (int i = 0; i < count; ++i)
        {
            result[i] = in[i + start];
        }

        return (result);
    }

    public static <T> Set<T> toSet(T[] array)
    {
        Set<T> theSet;
        if (array.length == 0)
        {
            theSet = Collections.emptySet();
        }
        else if (array.length == 1)
        {
            theSet = Collections.singleton(array[ 0]);
        }
        else
        {
            theSet = new HashSet<T>();
            for (int i = 0; i < array.length; ++i)
            {
                theSet.add(array[i]);
            }
        }
        return (theSet);
    }

    /*
    Return true if every element of the array has *exactly* the same class.
     */
    public static boolean hasIdenticalElementClasses(final Object[] a)
    {
        boolean isUniform = true;

        if (a.length > 0)
        {
            final Class matchType = a[ 0].getClass();

            for (int i = 1; i < a.length; ++i)
            {
                if (a[i].getClass() != matchType)
                {
                    isUniform = false;
                    break;
                }
            }
        }

        return (isUniform);
    }

    /**
    Specialize the type of the array (if possible).  For example, if the
    array is an Object[] of Integer, return an Integer[] of Integer.

    @param a                        the array to specialize
    @return        a specialized array (if possible) otherwise the original array
     */
    public static Object[] specializeArray(final Object[] a)
    {
        Object[] result = a;

        if (hasIdenticalElementClasses(a) &&
            a.length != 0 &&
            a.getClass() == Object[].class)
        {
            result = createObjectArrayType(a[0].getClass(), a.length);

            System.arraycopy(a, 0, result, 0, a.length);
        }

        return (result);
    }

    /**
    Convert a Set to an array. If specialize is true, then provide
    the most specialized type possible via specializeArray()

    @param s                        the Set to convert
    @param specialize        decide whether to specialize the type or not
     */
    public static Object[] setToArray(final Set<?> s, boolean specialize)
    {
        Object[] result = setToArray(s);

        if (specialize && result.length != 0)
        {
            result = specializeArray(result);
        }

        return (result);
    }

    /**
    Convert a Set to an Object[].

    @param s                        the Set to convert
     */
    public static Object[] setToArray(final Set<?> s)
    {
        final Object[] out = new Object[s.size()];

        setToArray(s, out);

        return (out);
    }

    /**
    Convert a Set to an Object[].

    @param s                        the Set to convert
    @param out                        the output array, must be of size s.size()
     */
    public static Object[] setToArray(final Set<?> s, Object[] out)
    {
        if (out.length != s.size())
        {
            throw new IllegalArgumentException();
        }

        int i = 0;
        for (final Object o : s)
        {
            out[i] = o;
            ++i;
        }

        return (out);
    }

    public static <T> Set<T> arrayToSet(final T[] names)
    {
        final Set<T> set = new HashSet<T>();

        for (int i = 0; i < names.length; ++i)
        {
            set.add(names[i]);
        }

        return (set);
    }

}

