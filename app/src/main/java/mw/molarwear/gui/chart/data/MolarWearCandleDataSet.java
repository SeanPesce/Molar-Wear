package mw.molarwear.gui.chart.data;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import java.util.List;

import mw.molarwear.gui.chart.AnalysisChart;

/**
 * @author Sean Pesce
 *
 * @see    CandleDataSet
 */

public class MolarWearCandleDataSet extends CandleDataSet {

    public MolarWearCandleDataSet(List<CandleEntry> yValues, String label) {
        super(yValues, label);
        this.setForm(Legend.LegendForm.NONE);
        this.setValueFormatter(AnalysisChart.CANDLE_VALUE_FORMATTER);
        this.setShadowWidth(AnalysisChart.SHADOW_WIDTH);
    }
}
