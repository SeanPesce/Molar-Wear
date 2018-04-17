package mw.molarwear.util;

import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;

import mw.molarwear.data.classes.AnalysisData;

/**
 * @author Sean Pesce
 */

public class AnalysisUtil {

    public static <T> T[] concatenate(@NonNull T[] a, @NonNull T[] b) {
        // Source: https://stackoverflow.com/a/80503/7891239
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    public static void sort(List<String> list) {
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
    }

    public static void addIfNotContains(List<String> list, String query) {
        addIfNotContains(list, query, false, false);
    }

    public static void addIfNotContains(List<String> list, String query, boolean ignoreCase) {
        addIfNotContains(list, query, ignoreCase, false);
    }

    public static void addIfNotContains(List<String> list, String query, boolean ignoreCase, boolean sortAfterAdding) {
        boolean add = true;
        if (ignoreCase) {
            for (String s : list) {
                if (s.equalsIgnoreCase(query)) {
                    add = false;
                    break;
                }
            }
        } else {
            for (String s : list) {
                if (s.equals(query)) {
                    add = false;
                    break;
                }
            }
        }
        if (add) {
            list.add(query);
            if (sortAfterAdding) { // Only sort if list was changed
                sort(list);
            }
        }
    }

    public static void addIfNotContainsIgnoreCase(List<String> list, String query) {
        addIfNotContains(list, query, true, false);
    }

    public static void addIfNotContainsIgnoreCase(List<String> list, String query, boolean sortAfterAdding) {
        addIfNotContains(list, query, true, sortAfterAdding);
    }

    public static void removeIgnoreCase(List<String> list, String query) {
        removeIgnoreCase(list, query, false);
    }

    public static void removeIgnoreCase(List<String> list, String query, boolean removeAll) {
        for (int i = 0; i < list.size();) {
            if (list.get(i).equalsIgnoreCase(query)) {
                list.remove(i);
                if (!removeAll) {
                    break;
                }
            } else {
                i++;
            }
        }
    }

    public static void removeAllIgnoreCase(List<String> list, String query) {
        removeIgnoreCase(list, query, true);
    }

    public static boolean containsIgnoreCase(List<String> list, String query) {
        for (String s : list) {
            if (s.equalsIgnoreCase(query)) {
                return true;
            }
        }
        return false;
    }

    public static int indexOfIgnoreCase(List<String> list, String query) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equalsIgnoreCase(query)) {
                return i;
            }
        }
        return -1;
    }

    public static int sum(@NonNull Integer[] data) {
        int sum = 0;
        for (Integer i : data) {
            sum += i;
        }
        return sum;
    }

    public static double sum(@NonNull Double[] data) {
        double sum = 0;
        for (Double i : data) {
            sum += i;
        }
        return sum;
    }

    public static Double mean(@NonNull Integer[] data) {
        return (data.length == 0) ? null : (((double)sum(data)) / data.length);
    }

    public static Double mean(@NonNull Double[] data) {
        return (data.length == 0) ? null : (sum(data) / data.length);
    }

    public static Double standardDeviation(@NonNull Integer[] data) {
        Double[] meanAndSd = meanAndStandardDeviation(data);
        return (meanAndSd == null) ? null : meanAndSd[1];
    }

    public static Double standardDeviation(@NonNull Double[] data) {
        Double[] meanAndSd = meanAndStandardDeviation(data);
        return (meanAndSd == null) ? null : meanAndSd[1];
    }

    public static Double[] meanAndStandardDeviation(@NonNull Integer[] data) {
        Double mean = mean(data);
        if (mean == null) {
            return null;
        }
        Double val = 0.0;
        for (Integer i : data) {
            val += Math.pow((i - mean), 2);
        }
        val /= data.length;
        return new Double[]{mean, Math.pow(val, 0.5)};
    }

    public static Double[] meanAndStandardDeviation(@NonNull Double[] data) {
        Double mean = mean(data);
        if (mean == null) {
            return null;
        }
        Double val = 0.0;
        for (Double i : data) {
            val += Math.pow((i - mean), 2);
        }
        val /= data.length;
        return new Double[]{mean, Math.pow(val, 0.5)};
    }

    public static AnalysisData<Integer> analyze(@NonNull Integer[] data) {
        return AnalysisData.newInstance(data);
    }

    public static AnalysisData<Double> analyze(@NonNull Double[] data) {
        return AnalysisData.newInstance(data);
    }
}
