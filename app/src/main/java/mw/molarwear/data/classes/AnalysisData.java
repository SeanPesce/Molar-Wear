package mw.molarwear.data.classes;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import mw.molarwear.util.AnalysisUtil;

/**
 * @author Sean Pesce
 */

public class AnalysisData<T extends Number> {

    private    T[] _data = null;

    private      T  _sum = null;
    private Double _mean = null;
    private Double   _sd = null;


    public static AnalysisData<Integer> newInstance(@NonNull Integer[] data) {
        return new AnalysisData<>(data);
    }

    public static AnalysisData<Double> newInstance(@NonNull Double[] data) {
        return new AnalysisData<>(data);
    }

    @SuppressWarnings("unchecked")
    private AnalysisData(@NonNull T[] data) {
        _data = data.clone();
        if (data.length > 0) {
            if (data[0].getClass() == Integer.class) {
                Integer[] intData = (Integer[])_data;
                Integer intSum = AnalysisUtil.sum(intData);
                _sum = (T)intSum;
                Double[] meanSd = AnalysisUtil.meanAndStandardDeviation(intData);
                if (meanSd != null) {
                    _mean = meanSd[0];
                    _sd   = meanSd[1];
                }
            } else if (data[0].getClass() == Double.class) {
                Double[] doubleData = (Double[])_data;
                Double doubleSum = AnalysisUtil.sum(doubleData);
                _sum = (T)doubleSum;
                Double[] meanSd = AnalysisUtil.meanAndStandardDeviation(doubleData);
                if (meanSd != null) {
                    _mean = meanSd[0];
                    _sd   = meanSd[1];
                }
            }
        }
    }


    //////////// Accessors ////////////

    public T getData(@IntRange(from=0) int index) {
        if (_data != null && index < _data.length && index >= 0) {
            return _data[index];
        }
        return null;
    }

    public int getDataSize() {
        return (_data == null) ? 0 : _data.length;
    }

    public T getSum() {
        return _sum;
    }

    public Double getMean() {
        return _mean;
    }

    public Double getStandardDeviation() {
        return _sd;
    }

}
