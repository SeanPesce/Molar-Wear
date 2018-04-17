package mw.molarwear.gui.dialog;

import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.data.classes.dental.molar.enums.Wear;
import mw.molarwear.gui.chart.SubjectAnalysisChart;
import mw.molarwear.util.AppUtil;

/**
 * A dialog box that displays analysis data for an individual.
 *
 * @author Sean Pesce
 *
 * @see    BasicDialog
 * @see    mw.molarwear.data.classes.MolarWearSubject
 * @see    Wear
 */

public class SubjectAnalysisDialog extends BasicDialog {

    private static final int      _TITLE_ID = R.string.dlg_title_subject_analysis;
    private static final int        _MSG_ID = R.string.dlg_msg_subject_analysis;
    private static final int _POS_BT_TXT_ID = R.string.act_export;
    private static final int _NEG_BT_TXT_ID = R.string.dlg_bt_close;

    private SubjectAnalysisChart _chart;
    private     MolarWearSubject _subject;

    public SubjectAnalysisDialog(@NonNull AppCompatActivity activity) {
        super(new DialogStringData(activity, _TITLE_ID, _MSG_ID, _POS_BT_TXT_ID, _NEG_BT_TXT_ID), R.layout.dialog_subject_analysis);
        initialize();
    }

    public SubjectAnalysisDialog(@NonNull AppCompatActivity activity, @NonNull MolarWearSubject subject) {
        super(new DialogStringData(activity, _TITLE_ID, _MSG_ID, _POS_BT_TXT_ID, _NEG_BT_TXT_ID), R.layout.dialog_subject_analysis);
        _subject = subject;
        initialize();
    }


    protected void initialize() {

//        RadioGroup rgDisplay = _layout.findViewById(R.id.radio_group_subj_analysis_display);

        AppCompatSpinner spinner = _body.findViewById(R.id.spinner_subj_analysis_display);
        spinner.setAdapter(new ArrayAdapter<>(_activity,
                                              R.layout.support_simple_spinner_dropdown_item,
                                              SubjectAnalysisChart.DISPLAY_SET_DESC));

        spinner.setSelection((_chart != null) ? _chart.getDisplayDataSet() : SubjectAnalysisChart.DEFAULT_DATA_SET);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (_chart != null) {
                    _chart.setDisplayDataSet(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

//        switch ((_chart != null) ? _chart.getDisplayDataSet() : SubjectAnalysisChart.DEFAULT_DATA_SET) {
//            case SubjectAnalysisChart.MolarSet.BOTH:
//                rgDisplay.check(R.id.radio_display_both);
//                break;
//            case SubjectAnalysisChart.MolarSet.LEFT:
//                rgDisplay.check(R.id.radio_display_left);
//                break;
//            case SubjectAnalysisChart.MolarSet.RIGHT:
//                rgDisplay.check(R.id.radio_display_right);
//                break;
//            case SubjectAnalysisChart.MolarSet.PREF:
//                rgDisplay.check(R.id.radio_display_preferred);
//                break;
//            default:
//                break;
//        }
//
//        rgDisplay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup rg, int checkedId) {
//                if (checkedId == R.id.radio_display_left) {
//                    if (_chart != null) {
//                        _chart.setDisplayDataSet(SubjectAnalysisChart.MolarSet.LEFT);
//                    }
//                } else if (checkedId == R.id.radio_display_right) {
//                    if (_chart != null) {
//                        _chart.setDisplayDataSet(SubjectAnalysisChart.MolarSet.RIGHT);
//                    }
//                } else if (checkedId == R.id.radio_display_both) {
//                    if (_chart != null) {
//                        _chart.setDisplayDataSet(SubjectAnalysisChart.MolarSet.BOTH);
//                    }
//                } else if (checkedId == R.id.radio_display_preferred) {
//                    if (_chart != null) {
//                        _chart.setDisplayDataSet(SubjectAnalysisChart.MolarSet.PREF);
//                    }
//                } else {
//                    if (_chart != null) {
//                        _chart.setDisplayDataSet(SubjectAnalysisChart.DEFAULT_DATA_SET);
//                    }
//                }
//            }
//        });


        setPosBt(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_chart != null) {
                    final String fileName    = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date()),
                                 subFolder   = SubjectAnalysisChart.getGallerySubfolder(_activity),
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
                                errDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
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

    public MolarWearSubject subject() { return _subject; }

    public String getExportDescription() {
        return   _activity.getString(R.string.dlg_title_subject_analysis)
               + System.getProperty("line.separator")
               + _activity.getString(R.string.lbl_subject_id) + ": " + _subject.id()
               + System.getProperty("line.separator")
               + _activity.getString(R.string.lbl_site_id) + ": " + _subject.siteId()
               + System.getProperty("line.separator")
               + _activity.getString(R.string.lbl_group_id) + ": " + _subject.groupId();
    }


    //////////// Mutators ////////////

    public void updateChart() {
        if (_chart != null) {
            ((LinearLayout)_body).removeView(_chart);
        }
        if (_subject != null) {
            _chart = new SubjectAnalysisChart(_activity, _subject);
            _chart.setPadding(AppUtil.dpToPixels(_activity, 8), _chart.getPaddingTop(), AppUtil.dpToPixels(_activity, 8), _chart.getPaddingBottom());
            AppCompatSpinner spinner = _body.findViewById(R.id.spinner_subj_analysis_display);
            _chart.setDisplayDataSet(spinner.getSelectedItemPosition());
//            RadioGroup rgDisplay = _layout.findViewById(R.id.radio_group_subj_analysis_display);
//            switch (rgDisplay.getCheckedRadioButtonId()) {
//                case R.id.radio_display_both:
//                    _chart.setDisplayDataSet(SubjectAnalysisChart.MolarSet.BOTH);
//                    break;
//                case R.id.radio_display_left:
//                    _chart.setDisplayDataSet(SubjectAnalysisChart.MolarSet.LEFT);
//                    break;
//                case R.id.radio_display_right:
//                    _chart.setDisplayDataSet(SubjectAnalysisChart.MolarSet.RIGHT);
//                    break;
//                case R.id.radio_display_preferred:
//                    _chart.setDisplayDataSet(SubjectAnalysisChart.MolarSet.PREF);
//                    break;
//                default:
//                    _chart.setDisplayDataSet(SubjectAnalysisChart.DEFAULT_DATA_SET);
//                    break;
//            }

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
