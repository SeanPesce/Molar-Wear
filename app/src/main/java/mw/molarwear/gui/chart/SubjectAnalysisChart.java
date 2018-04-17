package mw.molarwear.gui.chart;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mw.molarwear.R;
import mw.molarwear.data.classes.AnalysisData;
import mw.molarwear.data.classes.MolarWearSubject;

/**
 * @author Sean Pesce
 */

public class SubjectAnalysisChart extends AnalysisChart {


    public static final class MolarSet {
        public static final int BOTH  = 0;
        public static final int LEFT  = 1;
        public static final int RIGHT = 2;
        public static final int PREF  = 3; // Side with most complete data (or right side if equal)
    }

    public static String[] DISPLAY_SET_DESC = { "Both", "Left", "Right", "Max N" };

    public static final IValueFormatter CANDLE_VALUE_FORMATTER = new IValueFormatter() {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            CandleEntry e = (CandleEntry) entry;
            //return (SubjectAnalysisChart.DISPLAY_MEAN) ? ("mean = " + e.getOpen() + "\nSD  = " + (e.getHigh() - e.getOpen())) : "";
            return (SubjectAnalysisChart.DISPLAY_MEAN) ? "mean = " + e.getOpen() : "";
        }
    };

    public static final IValueFormatter SCATTER_VALUE_FORMATTER = new IValueFormatter() {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            //return (SubjectAnalysisChart.DISPLAY_MEAN) ? "mean = " + entry.getY() : "";
            return "";
        }
    };

    private static final int[] _COLORS = { Color.BLUE, Color.RED, Color.GREEN, Color.GRAY };


    public static boolean     DISPLAY_MEAN = false;
    public static   float       POINT_SIZE = 16f; // Size of mean point in chart (dp)
    @IntRange(from=0,to=3)
    public static     int DEFAULT_DATA_SET = MolarSet.PREF;


    private final MolarWearSubject _subject;

    private CandleDataSet _candleDataSetL  = null;
    private CandleDataSet _candleDataSetR  = null;
    private CandleDataSet _candleDataSetLR = null;
    private CandleData    _candleData      = null;

    private ScatterDataSet _scatterDataSetL  = null;
    private ScatterDataSet _scatterDataSetR  = null;
    private ScatterDataSet _scatterDataSetLR = null;
    private ScatterData    _scatterData      = null;

    private boolean[] _displaySetData = { false, false, false, false };



    public SubjectAnalysisChart(@NonNull Activity activity, @NonNull MolarWearSubject subject) {
        super(activity);
        _subject = subject;
        initialize();
    }

    public SubjectAnalysisChart(@NonNull Activity activity, @NonNull AttributeSet attrs, @NonNull MolarWearSubject subject) {
        super(activity, attrs);
        _subject = subject;
        initialize();
    }

    public SubjectAnalysisChart(@NonNull Activity activity, @NonNull AttributeSet attrs, int defStyle, @NonNull MolarWearSubject subject) {
        super(activity, attrs, defStyle);
        _subject = subject;
        initialize();
    }



    private void initialize() {

        _displaySetData[SubjectAnalysisChart.DEFAULT_DATA_SET] = true;

        AnalysisData<Integer> L1 = _subject.L1().analyzeWear(),
                              L2 = _subject.L2().analyzeWear(),
                              L3 = _subject.L3().analyzeWear(),
                              R1 = _subject.R1().analyzeWear(),
                              R2 = _subject.R2().analyzeWear(),
                              R3 = _subject.R3().analyzeWear(),
                              M1 = _subject.analyzeWearM1(),
                              M2 = _subject.analyzeWearM2(),
                              M3 = _subject.analyzeWearM3();

        _candleDataSetL  = molarSetAnalysisDataToCandleDataSet(L1, L2, L3, "");
        _candleDataSetR  = molarSetAnalysisDataToCandleDataSet(R1, R2, R3, "");
        _candleDataSetLR = molarSetAnalysisDataToCandleDataSet(M1, M2, M3, "");

        _scatterDataSetL  = molarSetAnalysisDataToScatterDataSet(L1, L2, L3, "");
        _scatterDataSetR  = molarSetAnalysisDataToScatterDataSet(R1, R2, R3, "");
        _scatterDataSetLR = molarSetAnalysisDataToScatterDataSet(M1, M2, M3, "");

        if (_candleDataSetL != null) {
            _candleDataSetL.setForm(Legend.LegendForm.NONE);
            _candleDataSetL.setValueFormatter(SubjectAnalysisChart.CANDLE_VALUE_FORMATTER);
        }
        if (_candleDataSetR != null) {
            _candleDataSetR.setForm(Legend.LegendForm.NONE);
            _candleDataSetR.setValueFormatter(SubjectAnalysisChart.CANDLE_VALUE_FORMATTER);
        }
        if (_candleDataSetLR != null) {
            _candleDataSetLR.setForm(Legend.LegendForm.NONE);
            _candleDataSetLR.setValueFormatter(SubjectAnalysisChart.CANDLE_VALUE_FORMATTER);
        }
        if (_scatterDataSetL != null) {
            _scatterDataSetL.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
            _scatterDataSetL.setForm(Legend.LegendForm.NONE);
            _scatterDataSetL.setValueFormatter(SubjectAnalysisChart.SCATTER_VALUE_FORMATTER);
            _scatterDataSetL.setScatterShapeSize(SubjectAnalysisChart.POINT_SIZE);
        }
        if (_scatterDataSetR != null) {
            _scatterDataSetR.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
            _scatterDataSetR.setForm(Legend.LegendForm.NONE);
            _scatterDataSetR.setValueFormatter(SubjectAnalysisChart.SCATTER_VALUE_FORMATTER);
            _scatterDataSetR.setScatterShapeSize(SubjectAnalysisChart.POINT_SIZE);
        }
        if (_scatterDataSetLR != null) {
            _scatterDataSetLR.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
            _scatterDataSetLR.setForm(Legend.LegendForm.NONE);
            _scatterDataSetLR.setValueFormatter(SubjectAnalysisChart.SCATTER_VALUE_FORMATTER);
            _scatterDataSetLR.setScatterShapeSize(SubjectAnalysisChart.POINT_SIZE);
        }

        _candleData  = new CandleData();
        _scatterData = new ScatterData();

        updateDisplay();
    }


    public int getDisplayDataSet() {
        if (_displaySetData[MolarSet.BOTH]) {
            return MolarSet.BOTH;
        } else if (_displaySetData[MolarSet.LEFT]) {
            return MolarSet.LEFT;
        } else if (_displaySetData[MolarSet.RIGHT]) {
            return MolarSet.RIGHT;
        } else {
            return MolarSet.PREF;
        }
    }


    public void setDisplayDataSet(@IntRange(from=0, to=3) int molarSet) {
        switch (molarSet) {
            case MolarSet.BOTH:
                _displaySetData[MolarSet.BOTH]  = true;
                _displaySetData[MolarSet.LEFT]  = false;
                _displaySetData[MolarSet.RIGHT] = false;
                _displaySetData[MolarSet.PREF]  = false;
                updateDisplay();
                break;
            case MolarSet.LEFT:
                _displaySetData[MolarSet.BOTH]  = false;
                _displaySetData[MolarSet.LEFT]  = true;
                _displaySetData[MolarSet.RIGHT] = false;
                _displaySetData[MolarSet.PREF]  = false;
                updateDisplay();
                break;
            case MolarSet.RIGHT:
                _displaySetData[MolarSet.BOTH]  = false;
                _displaySetData[MolarSet.LEFT]  = false;
                _displaySetData[MolarSet.RIGHT] = true;
                _displaySetData[MolarSet.PREF]  = false;
                updateDisplay();
                break;
            case MolarSet.PREF:
                _displaySetData[MolarSet.BOTH]  = false;
                _displaySetData[MolarSet.LEFT]  = false;
                _displaySetData[MolarSet.RIGHT] = false;
                _displaySetData[MolarSet.PREF]  = true;
                updateDisplay();
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("UnnecessaryUnboxing")
    public void updateDisplay() {
        clear();
        _candleData.clearValues();
        _scatterData.clearValues();

        if (_displaySetData[MolarSet.LEFT] && _candleDataSetL != null) {
            _candleData.addDataSet(_candleDataSetL);
            _scatterData.addDataSet(_scatterDataSetL);
        }

        if (_displaySetData[MolarSet.RIGHT] && _candleDataSetR != null) {
            _candleData.addDataSet(_candleDataSetR);
            _scatterData.addDataSet(_scatterDataSetR);
        }

        if (_displaySetData[MolarSet.BOTH] && _candleDataSetLR != null) {
            _candleData.addDataSet(_candleDataSetLR);
            _scatterData.addDataSet(_scatterDataSetLR);
        }

        if (_displaySetData[MolarSet.PREF]) {
            Character c = _subject.preferredSideForAnalysis();
            if (c == null || c.charValue() == 'R') {
                _candleData.addDataSet(_candleDataSetR);
                _scatterData.addDataSet(_scatterDataSetR);
            } else {
                _candleData.addDataSet(_candleDataSetL);
                _scatterData.addDataSet(_scatterDataSetL);
            }
        }

        if (_candleData.getDataSetCount() > 0) {
            for (int i = 0; i < _candleData.getDataSets().size(); i++) {
                CandleDataSet  cs = (CandleDataSet)  _candleData.getDataSets().get(i);
                ScatterDataSet ss = (ScatterDataSet) _scatterData.getDataSets().get(i);
                cs.setShadowColor(SubjectAnalysisChart._COLORS[i]);
                cs.setColor(SubjectAnalysisChart._COLORS[i]);
                ss.setColor(SubjectAnalysisChart._COLORS[i]);
            }
            CombinedData combinedData = new CombinedData();
            combinedData.setData(_candleData);
            combinedData.setData(_scatterData);
            setData(combinedData);
        }

        invalidate();
    }


    public static CandleDataSet molarSetAnalysisDataToCandleDataSet(@NonNull AnalysisData<Integer> M1,
                                                                    @NonNull AnalysisData<Integer> M2,
                                                                    @NonNull AnalysisData<Integer> M3) {
        return molarSetAnalysisDataToCandleDataSet(M1, M2, M3, null);
    }

    @SuppressWarnings("ConstantConditions")
    public static CandleDataSet molarSetAnalysisDataToCandleDataSet(@NonNull AnalysisData<Integer> M1,
                                                                    @NonNull AnalysisData<Integer> M2,
                                                                    @NonNull AnalysisData<Integer> M3,
                                                                    String label) {
        CandleEntry e1 = (M1 != null && M1.getDataSize() > 0) ?
            new CandleEntry(1,
                M1.getMean().floatValue()+M1.getStandardDeviation().floatValue(),
                M1.getMean().floatValue()-M1.getStandardDeviation().floatValue(),
                M1.getMean().floatValue(),
                M1.getMean().floatValue())
            : null;

        CandleEntry e2 = (M2 != null && M2.getDataSize() > 0) ?
            new CandleEntry(2,
                M2.getMean().floatValue()+M2.getStandardDeviation().floatValue(),
                M2.getMean().floatValue()-M2.getStandardDeviation().floatValue(),
                M2.getMean().floatValue(),
                M2.getMean().floatValue())
            : null;

        CandleEntry e3 = (M3 != null && M3.getDataSize() > 0) ?
            new CandleEntry(3,
                M3.getMean().floatValue()+M3.getStandardDeviation().floatValue(),
                M3.getMean().floatValue()-M3.getStandardDeviation().floatValue(),
                M3.getMean().floatValue(),
                M3.getMean().floatValue())
            : null;

        List<CandleEntry> entries = new ArrayList<>();
        if (e1 != null) {
            entries.add(e1);
        }
        if (e2 != null) {
            entries.add(e2);
        }
        if (e3 != null) {
            entries.add(e3);
        }
        return entries.isEmpty() ? null : new CandleDataSet(entries, label);
    }


    public static ScatterDataSet molarSetAnalysisDataToScatterDataSet(@NonNull AnalysisData<Integer> M1,
                                                                      @NonNull AnalysisData<Integer> M2,
                                                                      @NonNull AnalysisData<Integer> M3) {
        return molarSetAnalysisDataToScatterDataSet(M1, M2, M3, null);
    }

    @SuppressWarnings("ConstantConditions")
    public static ScatterDataSet molarSetAnalysisDataToScatterDataSet(@NonNull AnalysisData<Integer> M1,
                                                                      @NonNull AnalysisData<Integer> M2,
                                                                      @NonNull AnalysisData<Integer> M3,
                                                                      String label) {
        Entry e1 = (M1 != null && M1.getDataSize() > 0) ? new Entry(1, M1.getMean().floatValue()) : null;
        Entry e2 = (M2 != null && M2.getDataSize() > 0) ? new Entry(2, M2.getMean().floatValue()) : null;
        Entry e3 = (M3 != null && M3.getDataSize() > 0) ? new Entry(3, M3.getMean().floatValue()) : null;

        List<Entry> entries = new ArrayList<>();
        if (e1 != null) {
            entries.add(e1);
        }
        if (e2 != null) {
            entries.add(e2);
        }
        if (e3 != null) {
            entries.add(e3);
        }
        return entries.isEmpty() ? null : new ScatterDataSet(entries, label);
    }


    public static String getGallerySubfolder(@NonNull Activity activity) {
        return AnalysisChart.getGallerySubfolder(activity) + File.separator + activity.getString(R.string.individuals);
    }
}
