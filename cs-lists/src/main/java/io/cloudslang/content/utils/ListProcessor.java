/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListProcessor {

    public static String[] toArray(String list, String delimiter) {
        return toArrayFromNormalized(normalizeString(list, delimiter), normalizeDelimiter(delimiter));
    }

    public static int getIndex(String index, int listLength) throws Exception {
        index = index.toLowerCase().trim();
        try {
            return Integer.parseInt(index);
        } catch (NumberFormatException e) {
            if (index.equals("end")) {
                return listLength - 1;
            }
            if (index.startsWith("end-")) {
                index = index.substring(4, index.length()).trim();
            } else if (index.startsWith("end -")) {
                index = index.substring(5, index.length()).trim();
            }
            try {
                return listLength - 1 - Integer.parseInt(index);
            } catch (NumberFormatException f) {
                throw new NumberFormatException("Unable to parse index: " + index + " " + f.getMessage());
            }
        }
    }

    public static String[] reverse(String[] list) {
        String[] reversed = new String[list.length];
        for (int count = 0; count < list.length; count++) {
            reversed[reversed.length - count - 1] = list[count];
        }
        return reversed;
    }

    public static int[] reverse(int[] list) {
        int[] reversed = new int[list.length];
        for (int count = 0; count < list.length; count++) {
            reversed[reversed.length - count - 1] = list[count];
        }
        return reversed;
    }

    public static double[] reverse(double[] list) {
        double[] reversed = new double[list.length];
        for (int count = 0; count < list.length; count++) {
            reversed[reversed.length - count - 1] = list[count];
        }
        return reversed;
    }

    public static int[] toIntArray(String list, String delimiter) {
        String[] split = toArray(list, delimiter);
        int[] ints = new int[split.length];
        for (int count = 0; count < ints.length; count++) {
            ints[count] = Integer.parseInt(split[count]);
        }
        return ints;
    }

    public static double[] toDoubleArray(String list, String delimiter) {
        String[] split = toArray(list, delimiter);
        double[] dubs = new double[split.length];
        for (int count = 0; count < dubs.length; count++) {
            dubs[count] = Double.parseDouble(split[count]);
        }
        return dubs;
    }

    public static String toString(double[] list, String delimiter) {
        if (list.length == 0) {
            return "";
        }
        list = roundTo(list, 3);
        StringBuilder out = new StringBuilder("" + list[0]);
        for (int count = 1; count < list.length; count++) {
            out.append(delimiter).append(list[count]);
        }
        return out.toString();
    }

    public static String toString(int[] list, String delimiter) {
        if (list.length == 0)
            return "";
        StringBuilder out = new StringBuilder("" + list[0]);
        for (int count = 1; count < list.length; count++) {
            out.append(delimiter).append(list[count]);
        }
        return out.toString();
    }

    public static String toString(String[] list, String delimiter) {
        if (list.length == 0) {
            return "";
        }
        StringBuilder out = new StringBuilder(list[0]);
        for (int count = 1; count < list.length; count++) {
            out.append(delimiter).append(list[count]);
        }
        return out.toString();
    }

    public static String[] sort(String[] unsorted) {
        List<String> sorted = new ArrayList<>(unsorted.length);
        for (int count = 0; count < unsorted.length; count++) {
            sorted.add(unsorted[count]);
        }
        Collections.sort(sorted);
        return sorted.toArray(new String[unsorted.length]);
    }

    public static int[] sort(int[] unsorted) {
        List<Integer> sorted = new ArrayList<>(unsorted.length);
        for (int count = 0; count < unsorted.length; count++) {
            sorted.add(unsorted[count]);
        }
        Collections.sort(sorted);
        int[] out = new int[unsorted.length];
        for (int count = 0; count < out.length; count++) {
            out[count] = sorted.get(count);
        }
        return out;
    }

    public static double[] sort(double[] unsorted) {
        List<Double> sorted = new ArrayList<>(unsorted.length);
        for (int count = 0; count < unsorted.length; count++) {
            sorted.add(unsorted[count]);
        }
        Collections.sort(sorted);
        double[] out = new double[unsorted.length];
        for (int count = 0; count < out.length; count++) {
            out[count] = sorted.get(count);
        }
        return out;
    }

    public static int[] trimPercent(int[] array, int percent) {
        Integer[] d = new Integer[array.length];
        for (int count = 0; count < array.length; count++) {
            d[count] = array[count];
        }
        d = trimPercent(Integer.class, d, percent);
        array = new int[d.length];
        for (int count = 0; count < array.length; count++) {
            array[count] = d[count];
        }
        return array;
    }

    public static String[] trimPercent(String[] array, int percent) {
        String[] d = new String[array.length];
        for (int count = 0; count < array.length; count++) {
            d[count] = array[count];
        }
        d = trimPercent(String.class, d, percent);
        return d;
    }

    public static double[] trimPercent(double[] array, int percent) {
        Double[] d = new Double[array.length];
        for (int count = 0; count < array.length; count++) {
            d[count] = array[count];
        }
        d = trimPercent(Double.class, d, percent);
        array = new double[d.length];
        for (int count = 0; count < array.length; count++) {
            array[count] = d[count];
        }
        return array;
    }

    private static String normalizeString(String str, String sep) {
        String LITERAL_TAB = "\\" + "t";
        String LITERAL_NEW_LINE = "\\" + "n";
        String LITERAL_RETURN = "\\" + "r";

        if (sep.contains(LITERAL_TAB)) {
            str = str.replace(LITERAL_TAB, "\t");
        }
        if (sep.contains(LITERAL_NEW_LINE)) {
            str = str.replace(LITERAL_NEW_LINE, "\n");
        }
        if (sep.contains(LITERAL_RETURN)) {
            str = str.replace(LITERAL_RETURN, "\r");
        }
        return str;
    }

    private static String normalizeDelimiter(String delim) {
        delim = normalizeString(delim, delim);
        delim = regularExpressionReplace(delim, '\\');
        delim = regularExpressionReplace(delim, '|');
        delim = regularExpressionReplace(delim, '(');
        delim = regularExpressionReplace(delim, ')');
        delim = regularExpressionReplace(delim, '*');
        delim = regularExpressionReplace(delim, '+');
        delim = regularExpressionReplace(delim, '.');
        delim = regularExpressionReplace(delim, '?');
        delim = regularExpressionReplace(delim, '[');
        delim = regularExpressionReplace(delim, ']');
        delim = regularExpressionReplace(delim, '^');
        delim = regularExpressionReplace(delim, '{');
        delim = regularExpressionReplace(delim, '}');
        delim = regularExpressionReplace(delim, '$');
        return delim;
    }

    private static String regularExpressionReplace(String delim, char character) {
        int index;
        if ((index = delim.indexOf(character)) > -1) {
            String start = delim.substring(0, index);
            String end = "";
            if (index + 1 < delim.length()) {
                end = delim.substring(index + 1, delim.length());
            }
            delim = start + "\\" + character + regularExpressionReplace(end, character);

        }
        return delim;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> T[] trimPercent(Class<T> c, T[] array, int percent) {
        // how many elements do we need to trim?
        int trim = (int) Math.rint(array.length * ((double) percent / 100));

        // if it is odd make it even;
        if (trim % 2 != 0)
            trim--;

        // now divide it by two since that is where we will start in the list...
        trim /= 2;

        // and we will end that many before the end...
        int total = array.length - trim;

        T[] partial = (T[]) Array.newInstance(c, total - trim);
        // now create a new list
        for (int i = trim; i < total; i++) {
            partial[i - trim] = array[i];
        }
        return partial;
    }

    private static double roundTo(double value, int decimals) {
        double precision = Math.pow(10, decimals);
        return Math.rint(value * precision) / precision;
    }

    private static double[] roundTo(double[] values, int decimals) {
        double[] rounded = new double[values.length];
        for (int count = 0; count < values.length; count++) {
            rounded[count] = roundTo(values[count], decimals);
        }
        return rounded;
    }

    private static String[] toArrayFromNormalized(String list, String delimiter) {
        if (list.length() == 0) {
            return new String[0];
        }
        return list.split(delimiter);
    }


    /**
     * This method check if all elements of an array are null.
     * @param uncontainedArray element in array
     * @return any element that is found to be empty
     */
    public static boolean arrayElementsAreNull(String[] uncontainedArray) {
        boolean empty = true;
        for (Object ob : uncontainedArray) {
            if (ob != null) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    public static String[] getUncontainedArray(String[] subArray, String[] containerArray, boolean ignoreCase) {
        String[] uncontainedArray = new String[subArray.length];
        int index = 0;
        boolean found = false;
        for (String subStr : subArray) {
            for (String contStr : containerArray) {
                found = elementsAreEqual(subStr, contStr, ignoreCase);
                if (found) {
                    break;
                }
            }
            if (!found) {
                uncontainedArray[index] = subStr;
                index++;
            }
        }
        String[] newUncontainedArray = new String[index];
         System.arraycopy(uncontainedArray, 0, newUncontainedArray, 0, index);

        return newUncontainedArray;
    }

    public static boolean elementsAreEqual(String a, String b, boolean ignoreCase) {
        return ignoreCase ? StringUtils.equalsIgnoreCase(a, b) : StringUtils.equals(a, b);
    }
}