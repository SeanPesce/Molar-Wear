package mw.molarwear.gui.dialog;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.data.classes.dental.molar.enums.Wear;
import mw.molarwear.gui.chart.AnalysisChart;
import mw.molarwear.gui.chart.MultipleAnalysisChart;
import mw.molarwear.util.AppUtil;

/**
 * A dialog box that displays analysis data for an individual.
 *
 * @author Sean Pesce
 *
 * @see    BasicDialog
 * @see    MultipleAnalysisChart
 * @see    MolarWearSubject
 * @see    Wear
 */

public class MultipleAnalysisDialog extends BasicDialog {

    private static final int      _TITLE_ID = R.string.dlg_title_multiple_analysis;
    private static final int        _MSG_ID = R.string.dlg_msg_multiple_analysis;
    private static final int _POS_BT_TXT_ID = R.string.act_export;
    private static final int _NEG_BT_TXT_ID = R.string.dlg_bt_close;

    @LayoutRes
    private static final int _LAYOUT = R.layout.dialog_multiple_analysis;

    private MultipleAnalysisChart _chart;
    private List<MolarWearSubject> _subjects;

    public MultipleAnalysisDialog(@NonNull AppCompatActivity activity) {
        super(new DialogStringData(activity, _TITLE_ID, _MSG_ID, _POS_BT_TXT_ID, _NEG_BT_TXT_ID), _LAYOUT);
        _subjects = new ArrayList<>();
        initialize();
    }

//    @SuppressWarnings("ConstantConditions")
//    public MultipleAnalysisDialog(@NonNull AppCompatActivity activity, @NonNull MolarWearSubject subject) {
//        super(new DialogStringData(activity, _TITLE_ID, _MSG_ID, _POS_BT_TXT_ID, _NEG_BT_TXT_ID), _LAYOUT);
//        _subjects = new ArrayList<>();
//        if (subject != null) {
//            _subjects.add(subject);
//        }
//        initialize();
//    }

    @SuppressWarnings("ConstantConditions")
    public MultipleAnalysisDialog(@NonNull AppCompatActivity activity, @NonNull MolarWearSubject[] subjects) {
        super(new DialogStringData(activity, _TITLE_ID, _MSG_ID, _POS_BT_TXT_ID, _NEG_BT_TXT_ID), _LAYOUT);
        _subjects = new ArrayList<>();
        if (subjects != null && subjects.length > 0) {
            _subjects.addAll(Arrays.asList(subjects));
        }
        initialize();
    }

    @SuppressWarnings("ConstantConditions")
    public MultipleAnalysisDialog(@NonNull AppCompatActivity activity, @NonNull List<MolarWearSubject> subjects) {
        super(new DialogStringData(activity, _TITLE_ID, _MSG_ID, _POS_BT_TXT_ID, _NEG_BT_TXT_ID), _LAYOUT);
        if (subjects != null) {
            _subjects = subjects;
        } else {
            _subjects = new ArrayList<>();
        }
        initialize();
    }

    @SuppressWarnings("ConstantConditions")
    public MultipleAnalysisDialog(@NonNull AppCompatActivity activity, @NonNull Collection<MolarWearSubject> subjects) {
        super(new DialogStringData(activity, _TITLE_ID, _MSG_ID, _POS_BT_TXT_ID, _NEG_BT_TXT_ID), _LAYOUT);
        _subjects = new ArrayList<>();
        if (subjects != null && subjects.size() > 0) {
            _subjects.addAll(subjects);
        }
        initialize();
    }


    protected void initialize() {

        AppCompatSpinner dataSetSpinner = _body.findViewById(R.id.spinner_subj_analysis_display);
        dataSetSpinner.setAdapter(new ArrayAdapter<>(_activity,
                                              R.layout.support_simple_spinner_dropdown_item,
                                              AnalysisChart.MolarSet.DESCRIPTION));
        dataSetSpinner.setSelection((_chart != null) ? _chart.getDisplayDataSet() : AnalysisChart.MolarSet.DEFAULT);

        dataSetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (_chart != null) {
                    _chart.setDisplayDataSet(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        AppCompatSpinner groupingSpinner = _body.findViewById(R.id.spinner_analysis_group_by);
        groupingSpinner.setAdapter(new ArrayAdapter<>(_activity,
                                    R.layout.support_simple_spinner_dropdown_item,
                                    MultipleAnalysisChart.Grouping.DESCRIPTION));
        groupingSpinner.setSelection((_chart != null) ? _chart.getGrouping() : MultipleAnalysisChart.Grouping.DEFAULT);

        groupingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (_chart != null) {
                    _chart.setGrouping(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        setPosBt(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_chart != null) {
                    final String fileName    = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date()),
                                 subFolder   = MultipleAnalysisChart.getGallerySubfolder(_activity),
                                 description = getExportDescription();

                    final Bitmap.CompressFormat[] formats = Bitmap.CompressFormat.values();
                    final List<String> formatNames = new ArrayList<>();
                    for (Bitmap.CompressFormat f : formats) {
                        formatNames.add(f.toString());
                    }

                    final RadioGroupDialog<String> dlg = new RadioGroupDialog<>(
                        new DialogStringData(_activity, R.string.dlg_title_export_chart, "", R.string.act_export, R.string.dlg_bt_cancel),
                        formatNames, _activity.getString(R.string.dlg_msg_export_chart), 0);

                    dlg.setPosBt(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int index = dlg.getCheckedRadioButtonIndex();
                            if (index < 0) {
                                index = 0;
                            }

                            if (!_chart.saveToGallery(fileName, subFolder, description, formats[index], 100)) {
                                // Export failed
                                final BasicDialog errDlg = new BasicDialog(new DialogStringData(_activity,
                                        R.string.err_export_failed,
                                        R.string.err_export_chart_failed,
                                        R.string.dlg_bt_ok,
                                        R.string.dlg_bt_cancel));
                                errDlg.setOnDismissListener(new OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) { show(); }
                                });
                                errDlg.show();
                            } else {
                                AppUtil.printToast(_activity,
                                    String.format(_activity.getString(R.string.out_msg_export_success), subFolder + File.separator + fileName));
                                show();
                            }
                        }
                    });
                    dlg.show();
                }
            }
        });

        //_body.setPadding(AppUtil.dpToPixels(8), 0, AppUtil.dpToPixels(8), 0);
        updateChart();
    }


    //////////// Accessors ////////////

    public List<MolarWearSubject> subjects() { return _subjects; }

    public String getExportDescription() {
        return   _activity.getString(R.string.dlg_title_multiple_analysis)
               + System.getProperty("line.separator")
               + "N = " + _subjects.size();
    }


    //////////// Mutators ////////////

    public void updateChart() {
        if (_chart != null) {
            ((LinearLayout)_body).removeView(_chart);
        }
        if (_subjects != null) {
            _chart = new MultipleAnalysisChart(_activity, _subjects);
            _chart.setPadding(AppUtil.dpToPixels(_activity, 8), _chart.getPaddingTop(), AppUtil.dpToPixels(_activity, 8), _chart.getPaddingBottom());
            AppCompatSpinner dataSetSpinner = _body.findViewById(R.id.spinner_subj_analysis_display);
            _chart.setDisplayDataSet(dataSetSpinner.getSelectedItemPosition());
            AppCompatSpinner groupingSpinner = _body.findViewById(R.id.spinner_analysis_group_by);
            _chart.setGrouping(groupingSpinner.getSelectedItemPosition());

            ((LinearLayout)_body).addView(_chart);
//            if (_subject.dataPoints() == 0) {
//                _chart.setMinimumHeight(AppUtil.dpToPixels(32));
//            }
            _chart.invalidate();
        } else {
            _chart = null;
        }
    }

}
