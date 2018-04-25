package mw.molarwear.gui.chart;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.io.File;

import mw.molarwear.R;
import mw.molarwear.data.analysis.AnalysisData;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.gui.chart.data.MolarWearCandleDataSet;
import mw.molarwear.gui.chart.data.MolarWearScatterDataSet;

/**
 * @author Sean Pesce
 */

public class SubjectAnalysisChart extends AnalysisChart {


    private static final int[] _COLORS = { Color.BLUE, Color.RED, Color.GREEN, Color.GRAY };


    private final MolarWearSubject _subject;

    private MolarWearCandleDataSet _candleDataSetL  = null;
    private MolarWearCandleDataSet _candleDataSetR  = null;
    private MolarWearCandleDataSet _candleDataSetLR = null;
    private CandleData _candleData = null;

    private MolarWearScatterDataSet _scatterDataSetL  = null;
    private MolarWearScatterDataSet _scatterDataSetR  = null;
    private MolarWearScatterDataSet _scatterDataSetLR = null;
    private ScatterData _scatterData = null;

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

        _displaySetData[AnalysisChart.MolarSet.DEFAULT] = true;

        final AnalysisData<Integer>
                L1 = _subject.L1().analyzeWear(),
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


    public static String getGallerySubfolder(@NonNull Activity activity) {
        return AnalysisChart.getGallerySubfolder(activity) + File.separator + activity.getString(R.string.individuals);
    }
}
