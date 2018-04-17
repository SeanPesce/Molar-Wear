package mw.molarwear.gui.chart;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.io.File;

import mw.molarwear.R;
import mw.molarwear.util.FileUtil;

/**
 * @author Sean Pesce
 */

public class AnalysisChart extends CombinedChart {

    public static final int REQUEST_EXPORT_CHART = 1540;

    protected final Activity _activity;

    public static final IAxisValueFormatter X_AXIS_VALUE_FORMATTER = new IAxisValueFormatter() {
        public final String[] molars = new String[] { "M1", "M2", "M3" };
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            switch ((int)value) {
                case 1:
                case 2:
                case 3:
                    return molars[(int)value-1];
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


    public static final DrawOrder[] DRAW_ORDER =
        {
            DrawOrder.BAR,
            DrawOrder.BUBBLE,
            DrawOrder.LINE,
            DrawOrder.CANDLE,
            DrawOrder.SCATTER
        };

    public static int DEFAULT_MIN_HEIGHT = 650;



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


    public void export() {
        FileUtil.createDirectoryChooser(_activity, AnalysisChart.REQUEST_EXPORT_CHART);
    }


    public static String getGallerySubfolder(@NonNull Activity activity) {
        return activity.getString(R.string.app_name) + File.separator + activity.getString(R.string.charts);
    }
}
