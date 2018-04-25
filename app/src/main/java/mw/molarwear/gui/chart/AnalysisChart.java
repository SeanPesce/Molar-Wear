package mw.molarwear.gui.chart;

import android.app.Activity;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mw.molarwear.R;
import mw.molarwear.data.analysis.AnalysisData;
import mw.molarwear.gui.chart.data.MolarWearCandleDataSet;
import mw.molarwear.gui.chart.data.MolarWearScatterDataSet;
import mw.molarwear.util.FileUtil;

/**
 * @author Sean Pesce
 *
 * @see    CombinedChart
 */

public class AnalysisChart extends CombinedChart {

    public static final int REQUEST_EXPORT_CHART = 1540;

    public static final String[] MOLAR_LABELS = new String[] { "M1", "M2", "M3" };


    public static final class MolarSet {
        public static final int BOTH  = 0;
        public static final int LEFT  = 1;
        public static final int RIGHT = 2;
        public static final int PREF  = 3; // Side with most complete data (or right side if equal)

        public static final String[] DESCRIPTION = { "Both", "Left", "Right", "Max N" };
        @IntRange(from=0,to=3)
        public static final int DEFAULT = AnalysisChart.MolarSet.PREF;

        public static int getMinVal() { return 0; }
        public static int getMaxVal() { return 3; }
        public static int count()  { return AnalysisChart.MolarSet.getMaxVal() + 1; }
    }

    public static final IAxisValueFormatter X_AXIS_VALUE_FORMATTER = new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            switch ((int)value) {
                case 1:
                case 2:
                case 3:
                    return MOLAR_LABELS[(int)value-1];
                default:
                    break;
            }
            return "";
        }
    };

    public static final IAxisValueFormatter Y_AXIS_VALUE_FORMATTER = new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if ((int)value >= 0 && (int)value < 9) {
                return "" + (int)value;
            }
            return "";
        }
    };

    public static final IValueFormatter CANDLE_VALUE_FORMATTER = new IValueFormatter() {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            CandleEntry e = (CandleEntry) entry;
            //return (AnalysisChart.DISPLAY_MEAN) ? ("mean = " + e.getOpen() + "\nSD  = " + (e.getHigh() - e.getOpen())) : "";
            return (AnalysisChart.DISPLAY_MEAN) ? "mean = " + e.getOpen() : "";
        }
    };

    public static final IValueFormatter SCATTER_VALUE_FORMATTER = new IValueFormatter() {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            //return (AnalysisChart.DISPLAY_MEAN) ? "mean = " + entry.getY() : "";
            return "";
        }
    };

    public static final DrawOrder[] DRAW_ORDER =
        {
            DrawOrder.BAR,
            DrawOrder.BUBBLE,
            DrawOrder.LINE,
            DrawOrder.CANDLE,
            DrawOrder.SCATTER
        };

    public static int DEFAULT_MIN_HEIGHT = 650;
    public static float POINT_SIZE_DP = 8f; // Size of mean point in chart (dp)
    public static float SHADOW_WIDTH = 1f; // Width of candlestick shadow (px?)
    public static boolean DISPLAY_MEAN = false;


    protected final Activity _activity;

    public AnalysisChart(@NonNull Activity activity) {
        super(activity);
        _activity = activity;
        initialize();
    }

    public AnalysisChart(@NonNull Activity activity, @NonNull AttributeSet attrs) {
        super(activity, attrs);
        _activity = activity;
        initialize();
    }

    public AnalysisChart(@NonNull Activity activity, @NonNull AttributeSet attrs, int defStyle) {
        super(activity, attrs, defStyle);
        _activity = activity;
        initialize();
    }


    private void initialize() {

        getDescription().setText("");
        setNoDataText(getContext().getString(R.string.no_data));
        setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        _activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        setMinimumHeight((int)(displayMetrics.heightPixels / 2.5f));
        setMinimumWidth((int)(displayMetrics.widthPixels * 0.8f));

//        IValueFormatter valueFormatter = new IValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                return "";
//            }
//        };

        getXAxis().setValueFormatter(AnalysisChart.X_AXIS_VALUE_FORMATTER);

        getXAxis().setGranularity(1f);
        getXAxis().setAxisMinimum(0.5f);
        getXAxis().setAxisMaximum(3.5f);
        getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        getAxisLeft().setGranularity(1f);
        getAxisRight().setGranularity(1f);
        getAxisLeft().setAxisMinimum(0f);
        getAxisRight().setAxisMinimum(0f);
        getAxisLeft().setAxisMaximum(10f);
        getAxisRight().setAxisMaximum(10f);
        getAxisLeft().setLabelCount(10);
        getAxisRight().setLabelCount(10);
        getAxisLeft().setValueFormatter(AnalysisChart.Y_AXIS_VALUE_FORMATTER);
        getAxisRight().setValueFormatter(AnalysisChart.Y_AXIS_VALUE_FORMATTER);

        setBackgroundColor(ContextCompat.getColor(_activity, R.color.colorPrimaryLight6));

        setDrawOrder(DRAW_ORDER);

        invalidate();
    }


    public static MolarWearCandleDataSet molarSetAnalysisDataToCandleDataSet(@NonNull AnalysisData<Integer> M1,
                                                                             @NonNull AnalysisData<Integer> M2,
                                                                             @NonNull AnalysisData<Integer> M3) {
        return AnalysisChart.molarSetAnalysisDataToCandleDataSet(M1, M2, M3, null, 0, 0);
    }

    public static MolarWearCandleDataSet molarSetAnalysisDataToCandleDataSet(@NonNull AnalysisData<Integer> M1,
                                                                             @NonNull AnalysisData<Integer> M2,
                                                                             @NonNull AnalysisData<Integer> M3,
                                                                             String label) {
        return AnalysisChart.molarSetAnalysisDataToCandleDataSet(M1, M2, M3, label, 0, 0);
    }

    @SuppressWarnings("ConstantConditions")
    public static MolarWearCandleDataSet molarSetAnalysisDataToCandleDataSet(@NonNull AnalysisData<Integer> M1,
                                                                             @NonNull AnalysisData<Integer> M2,
                                                                             @NonNull AnalysisData<Integer> M3,
                                                                             String label,
                                                                             int dataPointCount,
                                                                             int index) {
        CandleEntry e1 = (M1 != null && M1.getDataSize() > 0) ?
            new CandleEntry(1f + getJitter(dataPointCount, index),
                M1.getMean().floatValue()+M1.getStandardDeviation().floatValue(),
                M1.getMean().floatValue()-M1.getStandardDeviation().floatValue(),
                M1.getMean().floatValue(),
                M1.getMean().floatValue())
            : null;

        CandleEntry e2 = (M2 != null && M2.getDataSize() > 0) ?
            new CandleEntry(2f + getJitter(dataPointCount, index),
                M2.getMean().floatValue()+M2.getStandardDeviation().floatValue(),
                M2.getMean().floatValue()-M2.getStandardDeviation().floatValue(),
                M2.getMean().floatValue(),
                M2.getMean().floatValue())
            : null;

        CandleEntry e3 = (M3 != null && M3.getDataSize() > 0) ?
            new CandleEntry(3f + getJitter(dataPointCount, index),
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
        return entries.isEmpty() ? null : new MolarWearCandleDataSet(entries, label);
    }


    public static MolarWearScatterDataSet molarSetAnalysisDataToScatterDataSet(@NonNull AnalysisData<Integer> M1,
                                                                               @NonNull AnalysisData<Integer> M2,
                                                                               @NonNull AnalysisData<Integer> M3) {
        return AnalysisChart.molarSetAnalysisDataToScatterDataSet(M1, M2, M3, null, 0, 0);
    }

    public static MolarWearScatterDataSet molarSetAnalysisDataToScatterDataSet(@NonNull AnalysisData<Integer> M1,
                                                                               @NonNull AnalysisData<Integer> M2,
                                                                               @NonNull AnalysisData<Integer> M3,
                                                                               String label) {
        return AnalysisChart.molarSetAnalysisDataToScatterDataSet(M1, M2, M3, label, 0, 0);
    }

    @SuppressWarnings("ConstantConditions")
    public static MolarWearScatterDataSet molarSetAnalysisDataToScatterDataSet(@NonNull AnalysisData<Integer> M1,
                                                                               @NonNull AnalysisData<Integer> M2,
                                                                               @NonNull AnalysisData<Integer> M3,
                                                                               String label,
                                                                               int dataPointCount,
                                                                               int index) {
        Entry e1 = (M1 != null && M1.getDataSize() > 0) ? new Entry(1f + getJitter(dataPointCount, index), M1.getMean().floatValue()) : null;
        Entry e2 = (M2 != null && M2.getDataSize() > 0) ? new Entry(2f + getJitter(dataPointCount, index), M2.getMean().floatValue()) : null;
        Entry e3 = (M3 != null && M3.getDataSize() > 0) ? new Entry(3f + getJitter(dataPointCount, index), M3.getMean().floatValue()) : null;

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
        return entries.isEmpty() ? null : new MolarWearScatterDataSet(entries, label);
    }


    public static float getJitter(int dataPointCount, int index) {
        final float max = 0.15f, range = max*2f;
        if (dataPointCount > 1 && index >= 0 && index < dataPointCount) {
            //Log.i("AnalysisChart", "count=" + dataPointCount + "  index=" + index + "  result=" + ((((float)index / (float)(dataPointCount-1)) * range)-max));
            return (((float)index / (float)(dataPointCount-1)) * range) - max;
        }
        return 0f;
    }


    public void export() {
        FileUtil.createDirectoryChooser(_activity, AnalysisChart.REQUEST_EXPORT_CHART);
    }


    public static String getGallerySubfolder(@NonNull Activity activity) {
        return activity.getString(R.string.app_name) + File.separator + activity.getString(R.string.charts);
    }
}
