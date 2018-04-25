package mw.molarwear.util;

import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import mw.molarwear.data.analysis.AnalysisData;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.data.handlers.ProjectHandler;

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


    public static List<String> getGroupIds(@NonNull MolarWearProject[] projects) {
        return getGroupIds(projects, MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE, true, false);
    }

    public static List<String> getGroupIds(@NonNull MolarWearProject[] projects, boolean ignoreCase) {
        return getGroupIds(projects, ignoreCase, true, false);
    }

    public static List<String> getGroupIds(@NonNull MolarWearProject[] projects, boolean ignoreCase, boolean includeDefaults) {
        return getGroupIds(projects, ignoreCase, includeDefaults, false);
    }

    public static List<String> getGroupIds(@NonNull MolarWearProject[] projects, boolean ignoreCase, boolean includeDefaults, boolean allowEmptyString) {
        TreeSet<String> groups = new TreeSet<>(),
                        groupsLower = new TreeSet<>(); // For case-insensitive comparisons
        if (includeDefaults) {
            groups.addAll(ProjectHandler.getDefaultGroupIDs());
        }
        String s;
        for (MolarWearProject p : projects) {
            for (int i = 0; i < p.subjectCount(); i++) {
                MolarWearSubject subj = p.getSubject(i);
                s = ignoreCase ? subj.groupId().toLowerCase() : subj.groupId();
                TreeSet<String> set = ignoreCase ? groupsLower : groups;
                if ((allowEmptyString || !s.isEmpty()) && !set.contains(s)) {
                    groups.add(subj.groupId());
                    groupsLower.add(s);
                }
            }
        }
        if (groups.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<String> list = new ArrayList<>();
        list.addAll(groups);
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    public static List<String> getGroupIds(@NonNull MolarWearSubject[] subjects) {
        return getGroupIds(subjects, MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE, true, false);
    }

    public static List<String> getGroupIds(@NonNull MolarWearSubject[] subjects, boolean ignoreCase) {
        return getGroupIds(subjects, ignoreCase, true, false);
    }

    public static List<String> getGroupIds(@NonNull MolarWearSubject[] subjects, boolean ignoreCase, boolean includeDefaults) {
        return getGroupIds(subjects, ignoreCase, includeDefaults, false);
    }

    public static List<String> getGroupIds(@NonNull MolarWearSubject[] subjects, boolean ignoreCase, boolean includeDefaults, boolean allowEmptyString) {
        TreeSet<String> groups = new TreeSet<>(),
                        groupsLower = new TreeSet<>(); // For case-insensitive comparisons
        if (includeDefaults) {
            groups.addAll(ProjectHandler.getDefaultGroupIDs());
        }
        String s;
        for (MolarWearSubject subj : subjects) {
            s = ignoreCase ? subj.groupId().toLowerCase() : subj.groupId();
            TreeSet<String> set = ignoreCase ? groupsLower : groups;
            if ((allowEmptyString || !s.isEmpty()) && !set.contains(s)) {
                groups.add(subj.groupId());
                groupsLower.add(s);
            }
        }
        if (groups.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<String> list = new ArrayList<>();
        list.addAll(groups);
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    public static List<String> getSiteIds(@NonNull MolarWearProject[] projects) {
        return getSiteIds(projects, MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE, true, false);
    }

    public static List<String> getSiteIds(@NonNull MolarWearProject[] projects, boolean ignoreCase) {
        return getSiteIds(projects, ignoreCase, true, false);
    }

    public static List<String> getSiteIds(@NonNull MolarWearProject[] projects, boolean ignoreCase, boolean includeDefaults) {
        return getSiteIds(projects, ignoreCase, includeDefaults, false);
    }

    public static List<String> getSiteIds(@NonNull MolarWearProject[] projects, boolean ignoreCase, boolean includeDefaults, boolean allowEmptyString) {
        TreeSet<String> sites = new TreeSet<>(),
                        sitesLower = new TreeSet<>(); // For case-insensitive comparisons
        if (includeDefaults) {
            sites.addAll(ProjectHandler.getDefaultSiteIds());
        }
        String s;
        for (MolarWearProject p : projects) {
            for (int i = 0; i < p.subjectCount(); i++) {
                MolarWearSubject subj = p.getSubject(i);
                s = ignoreCase ? subj.siteId().toLowerCase() : subj.siteId();
                TreeSet<String> set = ignoreCase ? sitesLower : sites;
                if ((allowEmptyString || !s.isEmpty()) && !set.contains(s)) {
                    sites.add(subj.siteId());
                    sitesLower.add(s);
                }
            }
        }
        if (sites.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<String> list = new ArrayList<>();
        list.addAll(sites);
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    public static List<String> getSiteIds(@NonNull MolarWearSubject[] subjects) {
        return getSiteIds(subjects, MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE, true, false);
    }

    public static List<String> getSiteIds(@NonNull MolarWearSubject[] subjects, boolean ignoreCase) {
        return getSiteIds(subjects, ignoreCase, true, false);
    }

    public static List<String> getSiteIds(@NonNull MolarWearSubject[] subjects, boolean ignoreCase, boolean includeDefaults) {
        return getSiteIds(subjects, ignoreCase, includeDefaults, false);
    }

    public static List<String> getSiteIds(@NonNull MolarWearSubject[] subjects, boolean ignoreCase, boolean includeDefaults, boolean allowEmptyString) {
        TreeSet<String>      sites = new TreeSet<>(),
            sitesLower = new TreeSet<>(); // For case-insensitive comparisons
        if (includeDefaults) {
            sites.addAll(ProjectHandler.getDefaultSiteIds());
        }
        String s;
        for (MolarWearSubject subj : subjects) {
            s = ignoreCase ? subj.siteId().toLowerCase() : subj.siteId();
            TreeSet<String> set = ignoreCase ? sitesLower : sites;
            if ((allowEmptyString || !s.isEmpty()) && !set.contains(s)) {
                sites.add(subj.siteId());
                sitesLower.add(s);
            }
        }
        if (sites.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<String> list = new ArrayList<>();
        list.addAll(sites);
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
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
        final Double[] meanAndSd = meanAndStandardDeviation(data);
        return (meanAndSd == null) ? null : meanAndSd[1];
    }

    public static Double standardDeviation(@NonNull Double[] data) {
        final Double[] meanAndSd = meanAndStandardDeviation(data);
        return (meanAndSd == null) ? null : meanAndSd[1];
    }

    public static Double[] meanAndStandardDeviation(@NonNull Integer[] data) {
        final Double mean = mean(data);
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
        final Double mean = mean(data);
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


    // Multiple subject analysis

    public static AnalysisData<Integer> analyze(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreData(subjects));
    }

    public static AnalysisData<Integer> analyze(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreData(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeL(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataL(subjects));
    }

    public static AnalysisData<Integer> analyzeL(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataL(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeL1(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataL1(subjects));
    }

    public static AnalysisData<Integer> analyzeL1(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataL1(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeL2(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataL2(subjects));
    }

    public static AnalysisData<Integer> analyzeL2(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataL2(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeL3(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataL3(subjects));
    }

    public static AnalysisData<Integer> analyzeL3(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataL3(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeR(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataR(subjects));
    }

    public static AnalysisData<Integer> analyzeR(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataR(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeR1(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataR1(subjects));
    }

    public static AnalysisData<Integer> analyzeR1(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataR1(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeR2(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataR2(subjects));
    }

    public static AnalysisData<Integer> analyzeR2(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataR2(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeR3(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataR3(subjects));
    }

    public static AnalysisData<Integer> analyzeR3(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataR3(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeM1(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataM1(subjects));
    }

    public static AnalysisData<Integer> analyzeM1(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataM1(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeM2(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataM2(subjects));
    }

    public static AnalysisData<Integer> analyzeM2(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataM2(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeM3(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataM3(subjects));
    }

    public static AnalysisData<Integer> analyzeM3(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataM3(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzePreferred(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataPreferred(subjects));
    }

    public static AnalysisData<Integer> analyzePreferred(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataPreferred(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    // Preference calculated per individual
    public static AnalysisData<Integer> analyzeM1Preferred(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataM1Preferred(subjects));
    }

    public static AnalysisData<Integer> analyzeM1Preferred(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataM1Preferred(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeM2Preferred(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataM2Preferred(subjects));
    }

    public static AnalysisData<Integer> analyzeM2Preferred(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataM2Preferred(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static AnalysisData<Integer> analyzeM3Preferred(@NonNull MolarWearSubject[] subjects) {
        return AnalysisData.newInstance(wearScoreDataM3Preferred(subjects));
    }

    public static AnalysisData<Integer> analyzeM3Preferred(@NonNull Collection<MolarWearSubject> subjects) {
        return AnalysisData.newInstance(wearScoreDataM3Preferred(subjects.toArray(new MolarWearSubject[subjects.size()])));
    }

    public static Integer[] wearScoreData(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.wearScoreData());
        }
        return data;
    }

    public static Integer[] wearScoreDataL(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.wearScoreDataL());
        }
        return data;
    }

    public static Integer[] wearScoreDataL1(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.L1().wearScoreData());
        }
        return data;
    }

    public static Integer[] wearScoreDataL2(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.L2().wearScoreData());
        }
        return data;
    }

    public static Integer[] wearScoreDataL3(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.L3().wearScoreData());
        }
        return data;
    }

    public static Integer[] wearScoreDataR(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.wearScoreDataR());
        }
        return data;
    }

    public static Integer[] wearScoreDataR1(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.R1().wearScoreData());
        }
        return data;
    }

    public static Integer[] wearScoreDataR2(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.R2().wearScoreData());
        }
        return data;
    }

    public static Integer[] wearScoreDataR3(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.R3().wearScoreData());
        }
        return data;
    }

    public static Integer[] wearScoreDataM1(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.wearScoreDataM1());
        }
        return data;
    }

    public static Integer[] wearScoreDataM2(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.wearScoreDataM2());
        }
        return data;
    }

    public static Integer[] wearScoreDataM3(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.wearScoreDataM3());
        }
        return data;
    }

    public static Integer[] wearScoreDataPreferred(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            data = AnalysisUtil.concatenate(data, s.wearScoreDataPreferred());
        }
        return data;
    }

    // Preference calculated per individual
    @SuppressWarnings("UnnecessaryUnboxing")
    public static Integer[] wearScoreDataM1Preferred(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            final Character pref = s.preferredSideForAnalysis();
            if (pref != null) {
                data = AnalysisUtil.concatenate(data, (pref.charValue() == 'L') ? s.L1().wearScoreData() : s.R1().wearScoreData());
            }
        }
        return data;
    }

    @SuppressWarnings("UnnecessaryUnboxing")
    public static Integer[] wearScoreDataM2Preferred(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            final Character pref = s.preferredSideForAnalysis();
            if (pref != null) {
                data = AnalysisUtil.concatenate(data, (pref.charValue() == 'L') ? s.L2().wearScoreData() : s.R2().wearScoreData());
            }
        }
        return data;
    }

    @SuppressWarnings("UnnecessaryUnboxing")
    public static Integer[] wearScoreDataM3Preferred(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            final Character pref = s.preferredSideForAnalysis();
            if (pref != null) {
                data = AnalysisUtil.concatenate(data, (pref.charValue() == 'L') ? s.L3().wearScoreData() : s.R3().wearScoreData());
            }
        }
        return data;
    }

    // Preference calculated per tooth
    public static Integer[] wearScoreDataM1PreferredPerTooth(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            if (s.R1().dataPoints() >= s.L1().dataPoints()) {
                data = AnalysisUtil.concatenate(data, s.R1().wearScoreData());
            } else {
                data = AnalysisUtil.concatenate(data, s.L1().wearScoreData());
            }
        }
        return data;
    }

    public static Integer[] wearScoreDataM2PreferredPerTooth(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            if (s.R2().dataPoints() >= s.L2().dataPoints()) {
                data = AnalysisUtil.concatenate(data, s.R2().wearScoreData());
            } else {
                data = AnalysisUtil.concatenate(data, s.L2().wearScoreData());
            }
        }
        return data;
    }

    public static Integer[] wearScoreDataM3PreferredPerTooth(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        for (MolarWearSubject s : subjects) {
            if (s.R3().dataPoints() >= s.L3().dataPoints()) {
                data = AnalysisUtil.concatenate(data, s.R3().wearScoreData());
            } else {
                data = AnalysisUtil.concatenate(data, s.L3().wearScoreData());
            }
        }
        return data;
    }

    // Preference calculated cumulatively
    public static Integer[] wearScoreDataM1PreferredCumulative(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        if (dataPointCountR1(subjects) >= dataPointCountL1(subjects)) {
            for (MolarWearSubject s : subjects) {
                data = AnalysisUtil.concatenate(data, s.R1().wearScoreData());
            }
        } else {
            for (MolarWearSubject s : subjects) {
                data = AnalysisUtil.concatenate(data, s.L1().wearScoreData());
            }
        }
        return data;
    }

    public static Integer[] wearScoreDataM2PreferredCumulative(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        if (dataPointCountR2(subjects) >= dataPointCountL2(subjects)) {
            for (MolarWearSubject s : subjects) {
                data = AnalysisUtil.concatenate(data, s.R2().wearScoreData());
            }
        } else {
            for (MolarWearSubject s : subjects) {
                data = AnalysisUtil.concatenate(data, s.L2().wearScoreData());
            }
        }
        return data;
    }

    public static Integer[] wearScoreDataM3PreferredCumulative(@NonNull MolarWearSubject[] subjects) {
        Integer[] data = new Integer[]{};
        if (dataPointCountR3(subjects) >= dataPointCountL3(subjects)) {
            for (MolarWearSubject s : subjects) {
                data = AnalysisUtil.concatenate(data, s.R3().wearScoreData());
            }
        } else {
            for (MolarWearSubject s : subjects) {
                data = AnalysisUtil.concatenate(data, s.L3().wearScoreData());
            }
        }
        return data;
    }


    public static int dataPointCount(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            count += s.dataPoints();
        }
        return count;
    }

    public static int dataPointCountL(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            count += s.dataPointsL();
        }
        return count;
    }

    public static int dataPointCountL1(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            count += s.L1().dataPoints();
        }
        return count;
    }

    public static int dataPointCountL2(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            count += s.L2().dataPoints();
        }
        return count;
    }

    public static int dataPointCountL3(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            count += s.L3().dataPoints();
        }
        return count;
    }

    public static int dataPointCountR(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            count += s.dataPointsR();
        }
        return count;
    }

    public static int dataPointCountR1(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            count += s.R1().dataPoints();
        }
        return count;
    }

    public static int dataPointCountR2(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            count += s.R2().dataPoints();
        }
        return count;
    }

    public static int dataPointCountR3(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            count += s.R3().dataPoints();
        }
        return count;
    }


    public static int dataPointCountM1(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            count += s.dataPointsM1();
        }
        return count;
    }

    public static int dataPointCountM2(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            count += s.dataPointsM2();
        }
        return count;
    }

    public static int dataPointCountM3(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            count += s.dataPointsM3();
        }
        return count;
    }

    @SuppressWarnings("UnnecessaryUnboxing")
    public static int dataPointCountPreferred(@NonNull MolarWearSubject[] subjects) {
        int count = 0;
        for (MolarWearSubject s : subjects) {
            final Character pref = s.preferredSideForAnalysis();
            count += (pref == null) ? 0 : ((pref.charValue() == 'L') ? s.dataPointsL() : s.dataPointsR());
        }
        return count;
    }

    public static int wearScoreSum(@NonNull MolarWearSubject[] data) {
        int sum = 0;
        for (MolarWearSubject s : data) {
            sum += s.wearScoreSum();
        }
        return sum;
    }

    public static int wearScoreSumL(@NonNull MolarWearSubject[] data) {
        int sum = 0;
        for (MolarWearSubject s : data) {
            sum += s.wearScoreSumL();
        }
        return sum;
    }

    public static int wearScoreSumR(@NonNull MolarWearSubject[] data) {
        int sum = 0;
        for (MolarWearSubject s : data) {
            sum += s.wearScoreSumR();
        }
        return sum;
    }

    public static int wearScoreSumM1(@NonNull MolarWearSubject[] data) {
        int sum = 0;
        for (MolarWearSubject s : data) {
            sum += s.wearScoreSumM1();
        }
        return sum;
    }

    public static int wearScoreSumM2(@NonNull MolarWearSubject[] data) {
        int sum = 0;
        for (MolarWearSubject s : data) {
            sum += s.wearScoreSumM2();
        }
        return sum;
    }

    public static int wearScoreSumM3(@NonNull MolarWearSubject[] data) {
        int sum = 0;
        for (MolarWearSubject s : data) {
            sum += s.wearScoreSumM3();
        }
        return sum;
    }

    public static int wearScoreSumPreferred(@NonNull MolarWearSubject[] data) {
        int sum = 0;
        for (MolarWearSubject s : data) {
            sum += s.wearScoreSumPreferred();
        }
        return sum;
    }

    public static Double wearScoreMean(@NonNull MolarWearSubject[] data) {
        final int count = dataPointCount(data);
        return (data.length == 0 || count == 0) ? null : (((double)wearScoreSum(data)) / count);
    }

    public static Double wearScoreMeanL(@NonNull MolarWearSubject[] data) {
        final int count = dataPointCountL(data);
        return (data.length == 0 || count == 0) ? null : (((double)wearScoreSumL(data)) / count);
    }

    public static Double wearScoreMeanR(@NonNull MolarWearSubject[] data) {
        final int count = dataPointCountR(data);
        return (data.length == 0 || count == 0) ? null : (((double)wearScoreSumR(data)) / count);
    }

    public static Double wearScoreMeanM1(@NonNull MolarWearSubject[] data) {
        final int count = dataPointCountM1(data);
        return (data.length == 0 || count == 0) ? null : (((double)wearScoreSumM1(data)) / count);
    }

    public static Double wearScoreMeanM2(@NonNull MolarWearSubject[] data) {
        final int count = dataPointCountM2(data);
        return (data.length == 0 || count == 0) ? null : (((double)wearScoreSumM2(data)) / count);
    }

    public static Double wearScoreMeanM3(@NonNull MolarWearSubject[] data) {
        final int count = dataPointCountM3(data);
        return (data.length == 0 || count == 0) ? null : (((double)wearScoreSumM3(data)) / count);
    }

    public static Double wearScoreMeanPreferred(@NonNull MolarWearSubject[] data) {
        final int count = dataPointCountPreferred(data);
        return (data.length == 0 || count == 0) ? null : (((double)wearScoreSumPreferred(data)) / count);
    }

    public static Double wearScoreStandardDeviation(@NonNull MolarWearSubject[] data) {
        final Double[] meanAndSd = wearScoreMeanAndStandardDeviation(data);
        return (meanAndSd == null) ? null : meanAndSd[1];
    }

    public static Double wearScoreStandardDeviationL(@NonNull MolarWearSubject[] data) {
        final Double[] meanAndSd = wearScoreMeanAndStandardDeviationL(data);
        return (meanAndSd == null) ? null : meanAndSd[1];
    }

    public static Double wearScoreStandardDeviationR(@NonNull MolarWearSubject[] data) {
        final Double[] meanAndSd = wearScoreMeanAndStandardDeviationR(data);
        return (meanAndSd == null) ? null : meanAndSd[1];
    }

    public static Double wearScoreStandardDeviationM1(@NonNull MolarWearSubject[] data) {
        final Double[] meanAndSd = wearScoreMeanAndStandardDeviationM1(data);
        return (meanAndSd == null) ? null : meanAndSd[1];
    }

    public static Double wearScoreStandardDeviationM2(@NonNull MolarWearSubject[] data) {
        final Double[] meanAndSd = wearScoreMeanAndStandardDeviationM2(data);
        return (meanAndSd == null) ? null : meanAndSd[1];
    }

    public static Double wearScoreStandardDeviationM3(@NonNull MolarWearSubject[] data) {
        final Double[] meanAndSd = wearScoreMeanAndStandardDeviationM3(data);
        return (meanAndSd == null) ? null : meanAndSd[1];
    }

    public static Double wearScoreStandardDeviationPreferred(@NonNull MolarWearSubject[] data) {
        final Double[] meanAndSd = wearScoreMeanAndStandardDeviationPreferred(data);
        return (meanAndSd == null) ? null : meanAndSd[1];
    }

    public static Double[] wearScoreMeanAndStandardDeviation(@NonNull MolarWearSubject[] data) {
        final Double mean = wearScoreMean(data);
        if (mean == null) {
            return null;
        }
        Double val = 0.0;
        int dataPointCount = 0;
        for (MolarWearSubject s : data) {
            final Integer[] wearData = s.wearScoreData();
            dataPointCount += wearData.length;
            for (Integer i : wearData) {
                val += Math.pow((i - mean), 2);
            }
        }
        val /= dataPointCount;
        return new Double[]{mean, Math.pow(val, 0.5)};
    }

    public static Double[] wearScoreMeanAndStandardDeviationL(@NonNull MolarWearSubject[] data) {
        final Double mean = wearScoreMeanL(data);
        if (mean == null) {
            return null;
        }
        Double val = 0.0;
        int dataPointCount = 0;
        for (MolarWearSubject s : data) {
            final Integer[] wearData = s.wearScoreDataL();
            dataPointCount += wearData.length;
            for (Integer i : wearData) {
                val += Math.pow((i - mean), 2);
            }
        }
        val /= dataPointCount;
        return new Double[]{mean, Math.pow(val, 0.5)};
    }

    public static Double[] wearScoreMeanAndStandardDeviationR(@NonNull MolarWearSubject[] data) {
        final Double mean = wearScoreMeanR(data);
        if (mean == null) {
            return null;
        }
        Double val = 0.0;
        int dataPointCount = 0;
        for (MolarWearSubject s : data) {
            final Integer[] wearData = s.wearScoreDataR();
            dataPointCount += wearData.length;
            for (Integer i : wearData) {
                val += Math.pow((i - mean), 2);
            }
        }
        val /= dataPointCount;
        return new Double[]{mean, Math.pow(val, 0.5)};
    }

    public static Double[] wearScoreMeanAndStandardDeviationM1(@NonNull MolarWearSubject[] data) {
        final Double mean = wearScoreMeanM1(data);
        if (mean == null) {
            return null;
        }
        Double val = 0.0;
        int dataPointCount = 0;
        for (MolarWearSubject s : data) {
            final Integer[] wearData = s.wearScoreDataM1();
            dataPointCount += wearData.length;
            for (Integer i : wearData) {
                val += Math.pow((i - mean), 2);
            }
        }
        val /= dataPointCount;
        return new Double[]{mean, Math.pow(val, 0.5)};
    }

    public static Double[] wearScoreMeanAndStandardDeviationM2(@NonNull MolarWearSubject[] data) {
        final Double mean = wearScoreMeanM2(data);
        if (mean == null) {
            return null;
        }
        Double val = 0.0;
        int dataPointCount = 0;
        for (MolarWearSubject s : data) {
            final Integer[] wearData = s.wearScoreDataM2();
            dataPointCount += wearData.length;
            for (Integer i : wearData) {
                val += Math.pow((i - mean), 2);
            }
        }
        val /= dataPointCount;
        return new Double[]{mean, Math.pow(val, 0.5)};
    }

    public static Double[] wearScoreMeanAndStandardDeviationM3(@NonNull MolarWearSubject[] data) {
        final Double mean = wearScoreMeanM3(data);
        if (mean == null) {
            return null;
        }
        Double val = 0.0;
        int dataPointCount = 0;
        for (MolarWearSubject s : data) {
            final Integer[] wearData = s.wearScoreDataM3();
            dataPointCount += wearData.length;
            for (Integer i : wearData) {
                val += Math.pow((i - mean), 2);
            }
        }
        val /= dataPointCount;
        return new Double[]{mean, Math.pow(val, 0.5)};
    }

    public static Double[] wearScoreMeanAndStandardDeviationPreferred(@NonNull MolarWearSubject[] data) {
        final Double mean = wearScoreMeanPreferred(data);
        if (mean == null) {
            return null;
        }
        Double val = 0.0;
        int dataPointCount = 0;
        for (MolarWearSubject s : data) {
            final Integer[] wearData = s.wearScoreDataPreferred();
            dataPointCount += wearData.length;
            for (Integer i : wearData) {
                val += Math.pow((i - mean), 2);
            }
        }
        val /= dataPointCount;
        return new Double[]{mean, Math.pow(val, 0.5)};
    }
}
