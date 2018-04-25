package mw.molarwear.gui.chart.data;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.util.List;

import mw.molarwear.gui.chart.AnalysisChart;

/**
 * @author Sean Pesce
 *
 * @see    ScatterDataSet
 */

public class MolarWearScatterDataSet extends ScatterDataSet {

    public MolarWearScatterDataSet(List<Entry> yValues, String label) {
        super(yValues, label);
        this.setForm(Legend.LegendForm.NONE);
        this.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        this.setValueFormatter(AnalysisChart.SCATTER_VALUE_FORMATTER);
        this.setScatterShapeSize(AnalysisChart.POINT_SIZE_DP);
    }
}
