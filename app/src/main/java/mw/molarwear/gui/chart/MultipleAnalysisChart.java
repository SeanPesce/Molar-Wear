package mw.molarwear.gui.chart;

import android.app.Activity;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import mw.molarwear.R;
import mw.molarwear.data.analysis.AnalysisData;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.gui.chart.data.MolarWearCandleDataSet;
import mw.molarwear.gui.chart.data.MolarWearScatterDataSet;
import mw.molarwear.util.AnalysisUtil;

/**
 * @author Sean Pesce
 */

public class MultipleAnalysisChart extends AnalysisChart {

    public static final class DataSetType {
        // Left
        public static final int L1 = 0;
        public static final int L2 = 1;
        public static final int L3 = 2;
        // Right
        public static final int R1 = 3;
        public static final int R2 = 4;
        public static final int R3 = 5;
        // Both
        public static final int M1 = 6;
        public static final int M2 = 7;
        public static final int M3 = 8;
        // Preferred
        public static final int M1_PREF = 9;
        public static final int M2_PREF = 10;
        public static final int M3_PREF = 11;

        public static int getMinVal() { return 0;  }
        public static int getMaxVal() { return 11; }
        public static int count()  { return MultipleAnalysisChart.DataSetType.getMaxVal() + 1; }
    }

    public static final class Grouping {
        public static final int NONE = 0;
        public static final int INDIVIDUAL = 1;
        public static final int SEX = 2;
        public static final int GROUP = 3;
        public static final int SITE = 4;

        public static final String[] DESCRIPTION = { "-", "Individual", "Sex", "Group", "Site" };
        @IntRange(from=0,to=4)
        public static final int DEFAULT = MultipleAnalysisChart.Grouping.NONE;

        public static int getMinVal() { return 0; }
        public static int getMaxVal() { return 4; }
        public static int count()  { return MultipleAnalysisChart.Grouping.getMaxVal() + 1; }
    }

    private static final int[] _COLORS = { 0xFF3348BA, 0xFFD13838, 0xFF32BA39, 0xFF8A32BA, 0xFF39C1D3, 0xFFEF9243 };


    private final List<MolarWearSubject> _subjects;

    private final MolarWearCandleDataSet[]  _candleDataSetCumulative  = new MolarWearCandleDataSet[MultipleAnalysisChart.MolarSet.count()];
    private final MolarWearScatterDataSet[] _scatterDataSetCumulative = new MolarWearScatterDataSet[MultipleAnalysisChart.MolarSet.count()];

    private final MolarWearCandleDataSet[][]  _candleDataSetSex  = new MolarWearCandleDataSet[MolarWearSubject.SEX.values().length][MultipleAnalysisChart.MolarSet.count()];
    private final MolarWearScatterDataSet[][] _scatterDataSetSex = new MolarWearScatterDataSet[MolarWearSubject.SEX.values().length][MultipleAnalysisChart.MolarSet.count()];

    private final List<MolarWearCandleDataSet[]>  _candleDataSetIndividuals  = new ArrayList<>();
    private final List<MolarWearScatterDataSet[]> _scatterDataSetIndividuals = new ArrayList<>();

    private final List<MolarWearCandleDataSet[]>  _candleDataSetGroups  = new ArrayList<>();
    private final List<MolarWearScatterDataSet[]> _scatterDataSetGroups = new ArrayList<>();

    private final List<MolarWearCandleDataSet[]>  _candleDataSetSites  = new ArrayList<>();
    private final List<MolarWearScatterDataSet[]> _scatterDataSetSites = new ArrayList<>();

    private CandleData  _candleData  = null;
    private ScatterData _scatterData = null;

    //private boolean[] _displaySetData = { false, false, false, false };
    @IntRange(from=0,to=3)
    private int _displaySet = AnalysisChart.MolarSet.DEFAULT;
    @IntRange(from=0,to=3)
    private int _grouping = MultipleAnalysisChart.Grouping.DEFAULT;


//    @SuppressWarnings("ConstantConditions")
//    public MultipleAnalysisChart(@NonNull Activity activity, @NonNull MolarWearSubject subject) {
//        super(activity);
//        _subjects = new ArrayList<>();
//        if (subject != null) {
//            _subjects.add(subject);
//        }
//        initialize();
//    }

    @SuppressWarnings("ConstantConditions")
    public MultipleAnalysisChart(@NonNull Activity activity, @NonNull MolarWearSubject[] subjects) {
        super(activity);
        _subjects = new ArrayList<>();
        if (subjects != null && subjects.length > 0) {
            _subjects.addAll(Arrays.asList(subjects));
        }
        initialize();
    }

    @SuppressWarnings("ConstantConditions")
    public MultipleAnalysisChart(@NonNull Activity activity, @NonNull List<MolarWearSubject> subjects) {
        super(activity);
        if (subjects != null) {
            _subjects = subjects;
        } else {
            _subjects = new ArrayList<>();
        }
        initialize();
    }

    @SuppressWarnings("ConstantConditions")
    public MultipleAnalysisChart(@NonNull Activity activity, @NonNull Collection<MolarWearSubject> subjects) {
        super(activity);
        _subjects = new ArrayList<>();
        if (subjects != null && subjects.size() > 0) {
            _subjects.addAll(subjects);
        }
        initialize();
    }

    @SuppressWarnings("ConstantConditions")
    public MultipleAnalysisChart(@NonNull Activity activity, @NonNull AttributeSet attrs, @NonNull MolarWearSubject[] subjects) {
        super(activity, attrs);
        _subjects = new ArrayList<>();
        if (subjects != null && subjects.length > 0) {
            _subjects.addAll(Arrays.asList(subjects));
        }
        initialize();
    }

    @SuppressWarnings("ConstantConditions")
    public MultipleAnalysisChart(@NonNull Activity activity, @NonNull AttributeSet attrs, @NonNull List<MolarWearSubject> subjects) {
        super(activity, attrs);
        if (subjects != null) {
            _subjects = subjects;
        } else {
            _subjects = new ArrayList<>();
        }
        initialize();
    }

    @SuppressWarnings("ConstantConditions")
    public MultipleAnalysisChart(@NonNull Activity activity, @NonNull AttributeSet attrs, @NonNull Collection<MolarWearSubject> subjects) {
        super(activity, attrs);
        _subjects = new ArrayList<>();
        if (subjects != null && subjects.size() > 0) {
            _subjects.addAll(subjects);
        }
        initialize();
    }

    @SuppressWarnings("ConstantConditions")
    public MultipleAnalysisChart(@NonNull Activity activity, @NonNull AttributeSet attrs, int defStyle, @NonNull MolarWearSubject[] subjects) {
        super(activity, attrs, defStyle);
        _subjects = new ArrayList<>();
        if (subjects != null && subjects.length > 0) {
            _subjects.addAll(Arrays.asList(subjects));
        }
        initialize();
    }

    @SuppressWarnings("ConstantConditions")
    public MultipleAnalysisChart(@NonNull Activity activity, @NonNull AttributeSet attrs, int defStyle, @NonNull List<MolarWearSubject> subjects) {
        super(activity, attrs, defStyle);
        if (subjects != null) {
            _subjects = subjects;
        } else {
            _subjects = new ArrayList<>();
        }
        initialize();
    }

    @SuppressWarnings("ConstantConditions")
    public MultipleAnalysisChart(@NonNull Activity activity, @NonNull AttributeSet attrs, int defStyle, @NonNull Collection<MolarWearSubject> subjects) {
        super(activity, attrs, defStyle);
        _subjects = new ArrayList<>();
        if (subjects != null && subjects.size() > 0) {
            _subjects.addAll(subjects);
        }
        initialize();
    }


    @SuppressWarnings("UnnecessaryUnboxing")
    private void initialize() {

        final MolarWearSubject[] subjectsArray = _subjects.toArray(new MolarWearSubject[_subjects.size()]);

        // Initialize buckets for sorting subjects by sex
        final MolarWearSubject.SEX[] sexes = MolarWearSubject.SEX.values();
        final List<List<MolarWearSubject>> subjectsBySex = new ArrayList<>();
        for (int i = 0; i < sexes.length; i++) {
            subjectsBySex.add(new ArrayList<MolarWearSubject>());
        }

        // Initialize buckets for sorting subjects by group & site
        final List<String> groupIds = AnalysisUtil.getGroupIds(subjectsArray, MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE, false, true),
                           siteIds  = AnalysisUtil.getSiteIds(subjectsArray, MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE, false, true);
        final HashMap<String, List<MolarWearSubject>> subjectsByGroup = new HashMap<>(),
                                                      subjectsBySite  = new HashMap<>();
        for (String g : groupIds) {
            if (MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE) {
                subjectsByGroup.put(g.toUpperCase(), new ArrayList<MolarWearSubject>());
            } else {
                subjectsByGroup.put(g, new ArrayList<MolarWearSubject>());
            }
        }
        for (String s : siteIds) {
            if (MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE) {
                subjectsBySite.put(s.toUpperCase(), new ArrayList<MolarWearSubject>());
            } else {
                subjectsBySite.put(s, new ArrayList<MolarWearSubject>());
            }
        }


        final List<AnalysisData<Integer>> data = new ArrayList<>();
        for (int i = 0; i < MultipleAnalysisChart.DataSetType.count(); i++) {
            data.add(AnalysisData.newInstance(new Integer[0]));
        }


        // Cumulative data
        data.set(DataSetType.L1, AnalysisUtil.analyzeL1(subjectsArray));
        data.set(DataSetType.L2, AnalysisUtil.analyzeL2(subjectsArray));
        data.set(DataSetType.L3, AnalysisUtil.analyzeL3(subjectsArray));
        data.set(DataSetType.R1, AnalysisUtil.analyzeR1(subjectsArray));
        data.set(DataSetType.R2, AnalysisUtil.analyzeR2(subjectsArray));
        data.set(DataSetType.R3, AnalysisUtil.analyzeR3(subjectsArray));
        data.set(DataSetType.M1, AnalysisUtil.analyzeM1(subjectsArray));
        data.set(DataSetType.M2, AnalysisUtil.analyzeM2(subjectsArray));
        data.set(DataSetType.M3, AnalysisUtil.analyzeM3(subjectsArray));
        data.set(DataSetType.M1_PREF, AnalysisUtil.analyzeM1Preferred(subjectsArray));
        data.set(DataSetType.M2_PREF, AnalysisUtil.analyzeM2Preferred(subjectsArray));
        data.set(DataSetType.M3_PREF, AnalysisUtil.analyzeM3Preferred(subjectsArray));

        _candleDataSetCumulative[MolarSet.LEFT]  = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.L1), data.get(DataSetType.L2), data.get(DataSetType.L3), "");
        _candleDataSetCumulative[MolarSet.RIGHT] = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.R1), data.get(DataSetType.R2), data.get(DataSetType.R3), "");
        _candleDataSetCumulative[MolarSet.BOTH]  = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.M1), data.get(DataSetType.M2), data.get(DataSetType.M3), "");
        _candleDataSetCumulative[MolarSet.PREF]  = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.M1_PREF), data.get(DataSetType.M2_PREF), data.get(DataSetType.M3_PREF), "");

        _scatterDataSetCumulative[MolarSet.LEFT]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.L1), data.get(DataSetType.L2), data.get(DataSetType.L3), "");
        _scatterDataSetCumulative[MolarSet.RIGHT] = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.R1), data.get(DataSetType.R2), data.get(DataSetType.R3), "");
        _scatterDataSetCumulative[MolarSet.BOTH]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.M1), data.get(DataSetType.M2), data.get(DataSetType.M3), "");
        _scatterDataSetCumulative[MolarSet.PREF]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.M1_PREF), data.get(DataSetType.M2_PREF), data.get(DataSetType.M3_PREF), "");



        for (int i = 0; i < _subjects.size(); i++) {

            // Individual grouping data
            data.set(DataSetType.L1, _subjects.get(i).L1().analyzeWear());
            data.set(DataSetType.L2, _subjects.get(i).L2().analyzeWear());
            data.set(DataSetType.L3, _subjects.get(i).L3().analyzeWear());
            data.set(DataSetType.R1, _subjects.get(i).R1().analyzeWear());
            data.set(DataSetType.R2, _subjects.get(i).R2().analyzeWear());
            data.set(DataSetType.R3, _subjects.get(i).R3().analyzeWear());
            data.set(DataSetType.M1, _subjects.get(i).analyzeWearM1());
            data.set(DataSetType.M2, _subjects.get(i).analyzeWearM2());
            data.set(DataSetType.M3, _subjects.get(i).analyzeWearM3());
            final Character side = _subjects.get(i).preferredSideForAnalysis();
            if (side == null) {
                data.set(DataSetType.M1_PREF, null);
                data.set(DataSetType.M2_PREF, null);
                data.set(DataSetType.M3_PREF, null);
            } else if (side.charValue() == 'L') {
                data.set(DataSetType.M1_PREF, data.get(DataSetType.L1));
                data.set(DataSetType.M2_PREF, data.get(DataSetType.L2));
                data.set(DataSetType.M3_PREF, data.get(DataSetType.L3));
            } else {
                data.set(DataSetType.M1_PREF, data.get(DataSetType.R1));
                data.set(DataSetType.M2_PREF, data.get(DataSetType.R2));
                data.set(DataSetType.M3_PREF, data.get(DataSetType.R3));
            }
            _candleDataSetIndividuals.add(new MolarWearCandleDataSet[MultipleAnalysisChart.MolarSet.count()]);
            _scatterDataSetIndividuals.add(new MolarWearScatterDataSet[MultipleAnalysisChart.MolarSet.count()]);
            _candleDataSetIndividuals.get(i)[MolarSet.LEFT]   = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.L1), data.get(DataSetType.L2), data.get(DataSetType.L3), "", _subjects.size(), i);
            _candleDataSetIndividuals.get(i)[MolarSet.RIGHT]  = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.R1), data.get(DataSetType.R2), data.get(DataSetType.R3), "", _subjects.size(), i);
            _candleDataSetIndividuals.get(i)[MolarSet.BOTH]   = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.M1), data.get(DataSetType.M2), data.get(DataSetType.M3), "", _subjects.size(), i);
            _candleDataSetIndividuals.get(i)[MolarSet.PREF]   = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.M1_PREF), data.get(DataSetType.M2_PREF), data.get(DataSetType.M3_PREF), "", _subjects.size(), i);
            _scatterDataSetIndividuals.get(i)[MolarSet.LEFT]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.L1), data.get(DataSetType.L2), data.get(DataSetType.L3), "", _subjects.size(), i);
            _scatterDataSetIndividuals.get(i)[MolarSet.RIGHT] = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.R1), data.get(DataSetType.R2), data.get(DataSetType.R3), "", _subjects.size(), i);
            _scatterDataSetIndividuals.get(i)[MolarSet.BOTH]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.M1), data.get(DataSetType.M2), data.get(DataSetType.M3), "", _subjects.size(), i);
            _scatterDataSetIndividuals.get(i)[MolarSet.PREF]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.M1_PREF), data.get(DataSetType.M2_PREF), data.get(DataSetType.M3_PREF), "", _subjects.size(), i);


            // Sort by sex
            switch(_subjects.get(i).sex()) {
                case MALE:
                    subjectsBySex.get(0).add(_subjects.get(i));
                    break;
                case MALE_UNCONFIRMED:
                    subjectsBySex.get(1).add(_subjects.get(i));
                    break;
                case FEMALE:
                    subjectsBySex.get(2).add(_subjects.get(i));
                    break;
                case FEMALE_UNCONFIRMED:
                    subjectsBySex.get(3).add(_subjects.get(i));
                    break;
                default: // UNKNOWN
                    subjectsBySex.get(4).add(_subjects.get(i));
                    break;
            }

            // Sort by group & site
            subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? _subjects.get(i).groupId().toUpperCase() : _subjects.get(i).groupId()).add(_subjects.get(i));
            subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? _subjects.get(i).siteId().toUpperCase() : _subjects.get(i).siteId()).add(_subjects.get(i));
        }

        // Sex grouping data
        for (int i = 0; i < sexes.length; i++) {
            data.set(DataSetType.L1, AnalysisUtil.analyzeL1(subjectsBySex.get(i)));
            data.set(DataSetType.L2, AnalysisUtil.analyzeL2(subjectsBySex.get(i)));
            data.set(DataSetType.L3, AnalysisUtil.analyzeL3(subjectsBySex.get(i)));
            data.set(DataSetType.R1, AnalysisUtil.analyzeR1(subjectsBySex.get(i)));
            data.set(DataSetType.R2, AnalysisUtil.analyzeR2(subjectsBySex.get(i)));
            data.set(DataSetType.R3, AnalysisUtil.analyzeR3(subjectsBySex.get(i)));
            data.set(DataSetType.M1, AnalysisUtil.analyzeM1(subjectsBySex.get(i)));
            data.set(DataSetType.M2, AnalysisUtil.analyzeM2(subjectsBySex.get(i)));
            data.set(DataSetType.M3, AnalysisUtil.analyzeM3(subjectsBySex.get(i)));
            data.set(DataSetType.M1_PREF, AnalysisUtil.analyzeM1Preferred(subjectsBySex.get(i)));
            data.set(DataSetType.M2_PREF, AnalysisUtil.analyzeM2Preferred(subjectsBySex.get(i)));
            data.set(DataSetType.M3_PREF, AnalysisUtil.analyzeM3Preferred(subjectsBySex.get(i)));
            _candleDataSetSex[i][MolarSet.LEFT]   = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.L1), data.get(DataSetType.L2), data.get(DataSetType.L3), "", sexes.length, i);
            _candleDataSetSex[i][MolarSet.RIGHT]  = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.R1), data.get(DataSetType.R2), data.get(DataSetType.R3), "", sexes.length, i);
            _candleDataSetSex[i][MolarSet.BOTH]   = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.M1), data.get(DataSetType.M2), data.get(DataSetType.M3), "", sexes.length, i);
            _candleDataSetSex[i][MolarSet.PREF]   = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.M1_PREF), data.get(DataSetType.M2_PREF), data.get(DataSetType.M3_PREF), "", sexes.length, i);
            _scatterDataSetSex[i][MolarSet.LEFT]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.L1), data.get(DataSetType.L2), data.get(DataSetType.L3), "", sexes.length, i);
            _scatterDataSetSex[i][MolarSet.RIGHT] = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.R1), data.get(DataSetType.R2), data.get(DataSetType.R3), "", sexes.length, i);
            _scatterDataSetSex[i][MolarSet.BOTH]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.M1), data.get(DataSetType.M2), data.get(DataSetType.M3), "", sexes.length, i);
            _scatterDataSetSex[i][MolarSet.PREF]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.M1_PREF), data.get(DataSetType.M2_PREF), data.get(DataSetType.M3_PREF), "", sexes.length, i);
        }


        // Group ID grouping data
        for (int i = 0; i < groupIds.size(); i++) {
            data.set(DataSetType.L1, AnalysisUtil.analyzeL1(subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? groupIds.get(i).toUpperCase() : groupIds.get(i))));
            data.set(DataSetType.L2, AnalysisUtil.analyzeL2(subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? groupIds.get(i).toUpperCase() : groupIds.get(i))));
            data.set(DataSetType.L3, AnalysisUtil.analyzeL3(subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? groupIds.get(i).toUpperCase() : groupIds.get(i))));
            data.set(DataSetType.R1, AnalysisUtil.analyzeR1(subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? groupIds.get(i).toUpperCase() : groupIds.get(i))));
            data.set(DataSetType.R2, AnalysisUtil.analyzeR2(subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? groupIds.get(i).toUpperCase() : groupIds.get(i))));
            data.set(DataSetType.R3, AnalysisUtil.analyzeR3(subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? groupIds.get(i).toUpperCase() : groupIds.get(i))));
            data.set(DataSetType.M1, AnalysisUtil.analyzeM1(subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? groupIds.get(i).toUpperCase() : groupIds.get(i))));
            data.set(DataSetType.M2, AnalysisUtil.analyzeM2(subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? groupIds.get(i).toUpperCase() : groupIds.get(i))));
            data.set(DataSetType.M3, AnalysisUtil.analyzeM3(subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? groupIds.get(i).toUpperCase() : groupIds.get(i))));
            data.set(DataSetType.M1_PREF, AnalysisUtil.analyzeM1Preferred(subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? groupIds.get(i).toUpperCase() : groupIds.get(i))));
            data.set(DataSetType.M2_PREF, AnalysisUtil.analyzeM2Preferred(subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? groupIds.get(i).toUpperCase() : groupIds.get(i))));
            data.set(DataSetType.M3_PREF, AnalysisUtil.analyzeM3Preferred(subjectsByGroup.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? groupIds.get(i).toUpperCase() : groupIds.get(i))));
            _candleDataSetGroups.add(new MolarWearCandleDataSet[MultipleAnalysisChart.MolarSet.count()]);
            _scatterDataSetGroups.add(new MolarWearScatterDataSet[MultipleAnalysisChart.MolarSet.count()]);
            _candleDataSetGroups.get(i)[MolarSet.LEFT]   = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.L1), data.get(DataSetType.L2), data.get(DataSetType.L3), "", groupIds.size(), i);
            _candleDataSetGroups.get(i)[MolarSet.RIGHT]  = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.R1), data.get(DataSetType.R2), data.get(DataSetType.R3), "", groupIds.size(), i);
            _candleDataSetGroups.get(i)[MolarSet.BOTH]   = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.M1), data.get(DataSetType.M2), data.get(DataSetType.M3), "", groupIds.size(), i);
            _candleDataSetGroups.get(i)[MolarSet.PREF]   = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.M1_PREF), data.get(DataSetType.M2_PREF), data.get(DataSetType.M3_PREF), "", groupIds.size(), i);
            _scatterDataSetGroups.get(i)[MolarSet.LEFT]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.L1), data.get(DataSetType.L2), data.get(DataSetType.L3), "", groupIds.size(), i);
            _scatterDataSetGroups.get(i)[MolarSet.RIGHT] = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.R1), data.get(DataSetType.R2), data.get(DataSetType.R3), "", groupIds.size(), i);
            _scatterDataSetGroups.get(i)[MolarSet.BOTH]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.M1), data.get(DataSetType.M2), data.get(DataSetType.M3), "", groupIds.size(), i);
            _scatterDataSetGroups.get(i)[MolarSet.PREF]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.M1_PREF), data.get(DataSetType.M2_PREF), data.get(DataSetType.M3_PREF), "", groupIds.size(), i);
        }


        // Site grouping data
        for (int i = 0; i < siteIds.size(); i++) {
            data.set(DataSetType.L1, AnalysisUtil.analyzeL1(subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? siteIds.get(i).toUpperCase() : siteIds.get(i))));
            data.set(DataSetType.L2, AnalysisUtil.analyzeL2(subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? siteIds.get(i).toUpperCase() : siteIds.get(i))));
            data.set(DataSetType.L3, AnalysisUtil.analyzeL3(subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? siteIds.get(i).toUpperCase() : siteIds.get(i))));
            data.set(DataSetType.R1, AnalysisUtil.analyzeR1(subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? siteIds.get(i).toUpperCase() : siteIds.get(i))));
            data.set(DataSetType.R2, AnalysisUtil.analyzeR2(subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? siteIds.get(i).toUpperCase() : siteIds.get(i))));
            data.set(DataSetType.R3, AnalysisUtil.analyzeR3(subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? siteIds.get(i).toUpperCase() : siteIds.get(i))));
            data.set(DataSetType.M1, AnalysisUtil.analyzeM1(subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? siteIds.get(i).toUpperCase() : siteIds.get(i))));
            data.set(DataSetType.M2, AnalysisUtil.analyzeM2(subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? siteIds.get(i).toUpperCase() : siteIds.get(i))));
            data.set(DataSetType.M3, AnalysisUtil.analyzeM3(subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? siteIds.get(i).toUpperCase() : siteIds.get(i))));
            data.set(DataSetType.M1_PREF, AnalysisUtil.analyzeM1Preferred(subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? siteIds.get(i).toUpperCase() : siteIds.get(i))));
            data.set(DataSetType.M2_PREF, AnalysisUtil.analyzeM2Preferred(subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? siteIds.get(i).toUpperCase() : siteIds.get(i))));
            data.set(DataSetType.M3_PREF, AnalysisUtil.analyzeM3Preferred(subjectsBySite.get(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE ? siteIds.get(i).toUpperCase() : siteIds.get(i))));
            _candleDataSetSites.add(new MolarWearCandleDataSet[MultipleAnalysisChart.MolarSet.count()]);
            _scatterDataSetSites.add(new MolarWearScatterDataSet[MultipleAnalysisChart.MolarSet.count()]);
            _candleDataSetSites.get(i)[MolarSet.LEFT]   = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.L1), data.get(DataSetType.L2), data.get(DataSetType.L3), "", siteIds.size(), i);
            _candleDataSetSites.get(i)[MolarSet.RIGHT]  = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.R1), data.get(DataSetType.R2), data.get(DataSetType.R3), "", siteIds.size(), i);
            _candleDataSetSites.get(i)[MolarSet.BOTH]   = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.M1), data.get(DataSetType.M2), data.get(DataSetType.M3), "", siteIds.size(), i);
            _candleDataSetSites.get(i)[MolarSet.PREF]   = molarSetAnalysisDataToCandleDataSet(data.get(DataSetType.M1_PREF), data.get(DataSetType.M2_PREF), data.get(DataSetType.M3_PREF), "", siteIds.size(), i);
            _scatterDataSetSites.get(i)[MolarSet.LEFT]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.L1), data.get(DataSetType.L2), data.get(DataSetType.L3), "", siteIds.size(), i);
            _scatterDataSetSites.get(i)[MolarSet.RIGHT] = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.R1), data.get(DataSetType.R2), data.get(DataSetType.R3), "", siteIds.size(), i);
            _scatterDataSetSites.get(i)[MolarSet.BOTH]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.M1), data.get(DataSetType.M2), data.get(DataSetType.M3), "", siteIds.size(), i);
            _scatterDataSetSites.get(i)[MolarSet.PREF]  = molarSetAnalysisDataToScatterDataSet(data.get(DataSetType.M1_PREF), data.get(DataSetType.M2_PREF), data.get(DataSetType.M3_PREF), "", siteIds.size(), i);
        }


        _candleData  = new CandleData();
        _scatterData = new ScatterData();

        updateDisplay();
    }


    public int getDisplayDataSet() {
        return _displaySet;
    }

    public int getGrouping() {
        return _grouping;
    }


    public void setDisplayDataSet(int molarSet) {
        if (molarSet >= MolarSet.getMinVal() && molarSet <= MolarSet.getMaxVal()) {
            _displaySet = molarSet;
            updateDisplay();
        }
    }

    public void setGrouping(int grouping) {
        if (grouping >= Grouping.getMinVal() && grouping <= Grouping.getMaxVal()) {
            _grouping = grouping;
            updateDisplay();
        }
    }

    @SuppressWarnings("UnnecessaryUnboxing")
    public void updateDisplay() {
        clear();
        _candleData.clearValues();
        _scatterData.clearValues();

        if (_grouping == MultipleAnalysisChart.Grouping.NONE) {
            if (_displaySet == MolarSet.LEFT && _candleDataSetCumulative[MolarSet.LEFT] != null) {
                _candleData.addDataSet(_candleDataSetCumulative[MolarSet.LEFT]);
                _scatterData.addDataSet(_scatterDataSetCumulative[MolarSet.LEFT]);
            } else if (_displaySet == MolarSet.RIGHT && _candleDataSetCumulative[MolarSet.RIGHT] != null) {
                _candleData.addDataSet(_candleDataSetCumulative[MolarSet.RIGHT]);
                _scatterData.addDataSet(_scatterDataSetCumulative[MolarSet.RIGHT]);
            } else if (_displaySet == MolarSet.BOTH && _candleDataSetCumulative[MolarSet.BOTH] != null) {
                _candleData.addDataSet(_candleDataSetCumulative[MolarSet.BOTH]);
                _scatterData.addDataSet(_scatterDataSetCumulative[MolarSet.BOTH]);
            } else if (_displaySet == MolarSet.PREF && _candleDataSetCumulative[MolarSet.PREF] != null) {
                _candleData.addDataSet(_candleDataSetCumulative[MolarSet.PREF]);
                _scatterData.addDataSet(_scatterDataSetCumulative[MolarSet.PREF]);
            }
        } else if (_grouping == MultipleAnalysisChart.Grouping.INDIVIDUAL) {
            for (int i = 0; i < _candleDataSetIndividuals.size(); i++) {
                if (_displaySet == MolarSet.LEFT && _candleDataSetIndividuals.get(i)[MolarSet.LEFT] != null) {
                    _candleData.addDataSet(_candleDataSetIndividuals.get(i)[MolarSet.LEFT]);
                    _scatterData.addDataSet(_scatterDataSetIndividuals.get(i)[MolarSet.LEFT]);
                } else if (_displaySet == MolarSet.RIGHT && _candleDataSetIndividuals.get(i)[MolarSet.RIGHT] != null) {
                    _candleData.addDataSet(_candleDataSetIndividuals.get(i)[MolarSet.RIGHT]);
                    _scatterData.addDataSet(_scatterDataSetIndividuals.get(i)[MolarSet.RIGHT]);
                } else if (_displaySet == MolarSet.BOTH && _candleDataSetIndividuals.get(i)[MolarSet.BOTH] != null) {
                    _candleData.addDataSet(_candleDataSetIndividuals.get(i)[MolarSet.BOTH]);
                    _scatterData.addDataSet(_scatterDataSetIndividuals.get(i)[MolarSet.BOTH]);
                } else if (_displaySet == MolarSet.PREF && _candleDataSetIndividuals.get(i)[MolarSet.PREF] != null) {
                    _candleData.addDataSet(_candleDataSetIndividuals.get(i)[MolarSet.PREF]);
                    _scatterData.addDataSet(_scatterDataSetIndividuals.get(i)[MolarSet.PREF]);
                }
            }
        } else if (_grouping == MultipleAnalysisChart.Grouping.SEX) {
            for (int i = 0; i < _candleDataSetSex[i].length; i++) {
                if (_displaySet == MolarSet.LEFT && _candleDataSetSex[i][MolarSet.LEFT] != null) {
                    _candleData.addDataSet(_candleDataSetSex[i][MolarSet.LEFT]);
                    _scatterData.addDataSet(_scatterDataSetSex[i][MolarSet.LEFT]);
                } else if (_displaySet == MolarSet.RIGHT && _candleDataSetSex[i][MolarSet.RIGHT] != null) {
                    _candleData.addDataSet(_candleDataSetSex[i][MolarSet.RIGHT]);
                    _scatterData.addDataSet(_scatterDataSetSex[i][MolarSet.RIGHT]);
                } else if (_displaySet == MolarSet.BOTH && _candleDataSetSex[i][MolarSet.BOTH] != null) {
                    _candleData.addDataSet(_candleDataSetSex[i][MolarSet.BOTH]);
                    _scatterData.addDataSet(_scatterDataSetSex[i][MolarSet.BOTH]);
                } else if (_displaySet == MolarSet.PREF && _candleDataSetSex[i][MolarSet.PREF] != null) {
                    _candleData.addDataSet(_candleDataSetSex[i][MolarSet.PREF]);
                    _scatterData.addDataSet(_scatterDataSetSex[i][MolarSet.PREF]);
                }
            }
        } else if (_grouping == MultipleAnalysisChart.Grouping.GROUP) {
            for (int i = 0; i < _candleDataSetGroups.size(); i++) {
                if (_displaySet == MolarSet.LEFT && _candleDataSetGroups.get(i)[MolarSet.LEFT] != null) {
                    _candleData.addDataSet(_candleDataSetGroups.get(i)[MolarSet.LEFT]);
                    _scatterData.addDataSet(_scatterDataSetGroups.get(i)[MolarSet.LEFT]);
                } else if (_displaySet == MolarSet.RIGHT && _candleDataSetGroups.get(i)[MolarSet.RIGHT] != null) {
                    _candleData.addDataSet(_candleDataSetGroups.get(i)[MolarSet.RIGHT]);
                    _scatterData.addDataSet(_scatterDataSetGroups.get(i)[MolarSet.RIGHT]);
                } else if (_displaySet == MolarSet.BOTH && _candleDataSetGroups.get(i)[MolarSet.BOTH] != null) {
                    _candleData.addDataSet(_candleDataSetGroups.get(i)[MolarSet.BOTH]);
                    _scatterData.addDataSet(_scatterDataSetGroups.get(i)[MolarSet.BOTH]);
                } else if (_displaySet == MolarSet.PREF && _candleDataSetGroups.get(i)[MolarSet.PREF] != null) {
                    _candleData.addDataSet(_candleDataSetGroups.get(i)[MolarSet.PREF]);
                    _scatterData.addDataSet(_scatterDataSetGroups.get(i)[MolarSet.PREF]);
                }
            }
        } else if (_grouping == MultipleAnalysisChart.Grouping.SITE) {
            for (int i = 0; i < _candleDataSetSites.size(); i++) {
                if (_displaySet == MolarSet.LEFT && _candleDataSetSites.get(i)[MolarSet.LEFT] != null) {
                    _candleData.addDataSet(_candleDataSetSites.get(i)[MolarSet.LEFT]);
                    _scatterData.addDataSet(_scatterDataSetSites.get(i)[MolarSet.LEFT]);
                } else if (_displaySet == MolarSet.RIGHT && _candleDataSetSites.get(i)[MolarSet.RIGHT] != null) {
                    _candleData.addDataSet(_candleDataSetSites.get(i)[MolarSet.RIGHT]);
                    _scatterData.addDataSet(_scatterDataSetSites.get(i)[MolarSet.RIGHT]);
                } else if (_displaySet == MolarSet.BOTH && _candleDataSetSites.get(i)[MolarSet.BOTH] != null) {
                    _candleData.addDataSet(_candleDataSetSites.get(i)[MolarSet.BOTH]);
                    _scatterData.addDataSet(_scatterDataSetSites.get(i)[MolarSet.BOTH]);
                } else if (_displaySet == MolarSet.PREF && _candleDataSetSites.get(i)[MolarSet.PREF] != null) {
                    _candleData.addDataSet(_candleDataSetSites.get(i)[MolarSet.PREF]);
                    _scatterData.addDataSet(_scatterDataSetSites.get(i)[MolarSet.PREF]);
                }
            }
        }

        if (_candleData.getDataSetCount() > 0) {
            for (int i = 0; i < _candleData.getDataSets().size(); i++) {
                final CandleDataSet  cs = (CandleDataSet)  _candleData.getDataSets().get(i);
                final ScatterDataSet ss = (ScatterDataSet) _scatterData.getDataSets().get(i);
                final int color = i % _COLORS.length;
                cs.setShadowColor(MultipleAnalysisChart._COLORS[color]);
                cs.setColor(MultipleAnalysisChart._COLORS[color]);
                ss.setColor(MultipleAnalysisChart._COLORS[color]);
            }
            CombinedData combinedData = new CombinedData();
            combinedData.setData(_candleData);
            combinedData.setData(_scatterData);
            setData(combinedData);
        }

        invalidate();
    }


    public static String getGallerySubfolder(@NonNull Activity activity) {
        return AnalysisChart.getGallerySubfolder(activity) + File.separator + activity.getString(R.string.multiple);
    }
}
