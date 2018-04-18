package mw.molarwear.gui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import mw.molarwear.R;
import mw.molarwear.data.classes.dental.enums.ToothMapping;
import mw.molarwear.data.classes.dental.molar.Molar;
import mw.molarwear.data.classes.dental.molar.SurfaceQuadrant;
import mw.molarwear.data.classes.dental.molar.WearImageCacheMap;
import mw.molarwear.data.classes.dental.molar.enums.Wear;
import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.gui.activity.ViewProjectActivity;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.PopupListDialog;
import mw.molarwear.gui.dialog.TextInputDialog;
import mw.molarwear.util.AppUtil;
import mw.molarwear.util.FileUtil;

/**
 *
 *
 * @author Sean Pesce
 *
 * @see Fragment
 * @see mw.molarwear.data.classes.dental.molar.Molar
 * @see ViewProjectActivity
 */

public class MolarDataFragment extends Fragment {
    /**
     * Create a new instance of {@link MolarDataFragment}, initialized to
     * show the data for the specified {@link mw.molarwear.data.classes.dental.molar.Molar molar} of
     *  the {@link mw.molarwear.data.classes.MolarWearSubject subject} at 'subjectIndex' in the
     *  subjects list of the {@link mw.molarwear.data.classes.MolarWearProject project} at 'projectIndex'.
     *
     * @param projectIndex the index of the {@link mw.molarwear.data.classes.MolarWearProject project}
     *                      containing the {@link mw.molarwear.data.classes.MolarWearSubject subject}.
     * @param subjectIndex the index of the {@link mw.molarwear.data.classes.MolarWearSubject subject}
     *                      containing the specified {@link mw.molarwear.data.classes.dental.molar.Molar molar}.
     * @param mapping      the index of the {@link mw.molarwear.data.classes.dental.molar.Molar molar}
     *                      to be represented by the new {@link MolarDataFragment}.
     *
     * @return a new instance of {@link MolarDataFragment}, initialized to show the data for the
     *          specified {@link mw.molarwear.data.classes.dental.molar.Molar molar} of the
     *          {@link mw.molarwear.data.classes.MolarWearSubject subject} at 'subjectIndex' in the
     *          subjects list of the {@link mw.molarwear.data.classes.MolarWearProject project} at
     *          'projectIndex'.
     */
    public static MolarDataFragment newInstance(int projectIndex, int subjectIndex, ToothMapping mapping) {
        MolarDataFragment f = new MolarDataFragment();
        Bundle args = new Bundle();
        args.putInt(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, projectIndex);
        args.putInt(ViewProjectActivity.SUBJECT_INDEX_ARG_KEY, subjectIndex);
        args.putInt(ViewProjectActivity.SUBJECT_MOLAR_ARG_KEY, mapping.index());
        f.setArguments(args);
        return f;
    }

    public final static String[] WEAR_PICKER_ITEMS = {
            "No data",
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
        };

    private int _projectIndex = AdapterView.INVALID_POSITION;
    private int _subjectIndex = AdapterView.INVALID_POSITION;
    private ToothMapping _toothMapping;
    private Molar _molar;

    private ImageView _imgQ1;
    private ImageView _imgQ2;
    private ImageView _imgQ3;
    private ImageView _imgQ4;

    private final ImageView[] _imgQs = { null, null, null, null };
    private final int[]     _imgIdsTopLeft = { R.drawable.q1_0,
                                               R.drawable.q1_1,
                                               R.drawable.q1_2,
                                               R.drawable.q1_3,
                                               R.drawable.q1_4,
                                               R.drawable.q1_5,
                                               R.drawable.q1_6,
                                               R.drawable.q1_7,
                                               R.drawable.q1_8,
                                               R.drawable.q1_unknown,
                                             };
    private final int[]    _imgIdsTopRight = { R.drawable.q2_0,
                                               R.drawable.q2_1,
                                               R.drawable.q2_2,
                                               R.drawable.q2_3,
                                               R.drawable.q2_4,
                                               R.drawable.q2_5,
                                               R.drawable.q2_6,
                                               R.drawable.q2_7,
                                               R.drawable.q2_8,
                                               R.drawable.q2_unknown,
                                             };
    private final int[]  _imgIdsBottomLeft = { R.drawable.q3_0,
                                               R.drawable.q3_1,
                                               R.drawable.q3_2,
                                               R.drawable.q3_3,
                                               R.drawable.q3_4,
                                               R.drawable.q3_5,
                                               R.drawable.q3_6,
                                               R.drawable.q3_7,
                                               R.drawable.q3_8,
                                               R.drawable.q3_unknown,
                                             };
    private final int[] _imgIdsBottomRight = { R.drawable.q4_0,
                                               R.drawable.q4_1,
                                               R.drawable.q4_2,
                                               R.drawable.q4_3,
                                               R.drawable.q4_4,
                                               R.drawable.q4_5,
                                               R.drawable.q4_6,
                                               R.drawable.q4_7,
                                               R.drawable.q4_8,
                                               R.drawable.q4_unknown,
                                             };
    private final int[][] _imgIds = { null, null, null, null };
    private final SurfaceQuadrant[] _quads = { null, null, null, null };

    public final View.OnClickListener wearDescriptionsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Wear.showWearDescriptionDialog(getActivityDerived());
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.card_molar_fragment, container, false);

        if (args != null) {
            _projectIndex = args.getInt(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, _projectIndex);
            _subjectIndex = args.getInt(ViewProjectActivity.SUBJECT_INDEX_ARG_KEY, _subjectIndex);
            final int toothIndex = args.getInt(ViewProjectActivity.SUBJECT_MOLAR_ARG_KEY, (_toothMapping != null) ? _toothMapping.index() : ToothMapping.MOLAR1_L_L.index());
            _toothMapping = ToothMapping.get(toothIndex);
        }

        if (_projectIndex >= 0 && _subjectIndex >= 0) {

            AppCompatImageButton btNotes = view.findViewById(R.id.bt_molar_notes);
            AppCompatImageButton btPhoto = view.findViewById(R.id.bt_molar_photo);

            final TextView label = view.findViewById(R.id.lbl_molar);

            switch (_toothMapping) {
                case MOLAR1_L_L:
                    _molar = ProjectHandler.PROJECTS.get(_projectIndex).getSubject(_subjectIndex).L1();
                    label.setText(R.string.lbl_subject_molar_l1);
                    break;
                case MOLAR2_L_L:
                    _molar = ProjectHandler.PROJECTS.get(_projectIndex).getSubject(_subjectIndex).L2();
                    label.setText(R.string.lbl_subject_molar_l2);
                    break;
                case MOLAR3_L_L:
                    _molar = ProjectHandler.PROJECTS.get(_projectIndex).getSubject(_subjectIndex).L3();
                    label.setText(R.string.lbl_subject_molar_l3);
                    break;
                case MOLAR1_L_R:
                    _molar = ProjectHandler.PROJECTS.get(_projectIndex).getSubject(_subjectIndex).R1();
                    label.setText(R.string.lbl_subject_molar_r1);
                    break;
                case MOLAR2_L_R:
                    _molar = ProjectHandler.PROJECTS.get(_projectIndex).getSubject(_subjectIndex).R2();
                    label.setText(R.string.lbl_subject_molar_r2);
                    break;
                case MOLAR3_L_R:
                default:
                    _molar = ProjectHandler.PROJECTS.get(_projectIndex).getSubject(_subjectIndex).R3();
                    label.setText(R.string.lbl_subject_molar_r3);
                    break;
            }

            _quads[0] = _molar.surface().q1();
            _quads[1] = _molar.surface().q2();
            _quads[2] = _molar.surface().q3();
            _quads[3] = _molar.surface().q4();

            if (_molar.left()) {
                _imgQ1 = view.findViewById(R.id.img_q_top_right);
                _imgQ2 = view.findViewById(R.id.img_q_top_left);
                _imgQ3 = view.findViewById(R.id.img_q_bottom_right);
                _imgQ4 = view.findViewById(R.id.img_q_bottom_left);
                _imgIds[0] = _imgIdsTopRight;
                _imgIds[1] = _imgIdsTopLeft;
                _imgIds[2] = _imgIdsBottomRight;
                _imgIds[3] = _imgIdsBottomLeft;
            } else {
                _imgQ1 = view.findViewById(R.id.img_q_top_left);
                _imgQ2 = view.findViewById(R.id.img_q_top_right);
                _imgQ3 = view.findViewById(R.id.img_q_bottom_left);
                _imgQ4 = view.findViewById(R.id.img_q_bottom_right);
                _imgIds[0] = _imgIdsTopLeft;
                _imgIds[1] = _imgIdsTopRight;
                _imgIds[2] = _imgIdsBottomLeft;
                _imgIds[3] = _imgIdsBottomRight;
            }

            _imgQs[0] = _imgQ1;
            _imgQs[1] = _imgQ2;
            _imgQs[2] = _imgQ3;
            _imgQs[3] = _imgQ4;

            _imgQ1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int currentScore = _molar.surface().q1Wear().score();

                    final PopupListDialog<String> dlg = new PopupListDialog<>(getActivityDerived(),
                        WEAR_PICKER_ITEMS,
                        R.layout.list_item_simple_checkable_large_center_align,
                        R.id.lbl_listitem_checkable);

                    final View title = getActivity().getLayoutInflater().inflate(R.layout.dialog_wear_picker_title, null);
                    dlg.setCustomTitle(title);
                    final ImageView btWearHelp = title.findViewById(R.id.bt_wear_descriptions);
                    btWearHelp.setOnClickListener(wearDescriptionsListener);

                    dlg.setFooterDividersEnabled(false)
                        .setHeaderDividersEnabled(false)
                        .setHighlighted(((currentScore == Wear.UNKNOWN.score()) ? 0 : currentScore+1), R.id.layout_listitem_large)
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final int score = position - 1;
                                if (score != currentScore) {
                                    _molar.surface().setQ1(score);
                                    updateElements();
                                    getActivityDerived().setEdited();
                                }
                            }
                        })
                        .setHighlightOnLongClick(false)
                        .setWidth(AppUtil.dpToPixels(300))
                        .useNegBt(true)
                        .setTitleAndGetInstance(R.string.dlg_title_set_wear_lvl)
                        .show();
                }
            });

            _imgQ2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int currentScore = _molar.surface().q2Wear().score();

                    final PopupListDialog<String> dlg = new PopupListDialog<>(getActivityDerived(),
                        WEAR_PICKER_ITEMS,
                        R.layout.list_item_simple_checkable_large_center_align,
                        R.id.lbl_listitem_checkable);
                    final View title = getActivity().getLayoutInflater().inflate(R.layout.dialog_wear_picker_title, null);
                    dlg.setCustomTitle(title);
                    final ImageView btWearHelp = title.findViewById(R.id.bt_wear_descriptions);
                    btWearHelp.setOnClickListener(wearDescriptionsListener);
                    dlg.setFooterDividersEnabled(false)
                        .setHeaderDividersEnabled(false)
                        .setHighlighted(((currentScore == Wear.UNKNOWN.score()) ? 0 : currentScore+1), R.id.layout_listitem_large)
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final int score = position - 1;
                                if (score != currentScore) {
                                    _molar.surface().setQ2(score);
                                    updateElements();
                                    getActivityDerived().setEdited();
                                }
                            }
                        })
                        .setWidth(AppUtil.dpToPixels(300))
                        .useNegBt(true)
                        .setTitleAndGetInstance(R.string.dlg_title_set_wear_lvl)
                        .show();
                }
            });

            _imgQ3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int currentScore = _molar.surface().q3Wear().score();

                    final PopupListDialog<String> dlg = new PopupListDialog<>(getActivityDerived(),
                        WEAR_PICKER_ITEMS,
                        R.layout.list_item_simple_checkable_large_center_align,
                        R.id.lbl_listitem_checkable);
                    final View title = getActivity().getLayoutInflater().inflate(R.layout.dialog_wear_picker_title, null);
                    dlg.setCustomTitle(title);
                    final ImageView btWearHelp = title.findViewById(R.id.bt_wear_descriptions);
                    btWearHelp.setOnClickListener(wearDescriptionsListener);
                    dlg.setFooterDividersEnabled(false)
                        .setHeaderDividersEnabled(false)
                        .setHighlighted(((currentScore == Wear.UNKNOWN.score()) ? 0 : currentScore+1), R.id.layout_listitem_large)
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final int score = position - 1;
                                if (score != currentScore) {
                                    _molar.surface().setQ3(score);
                                    updateElements();
                                    getActivityDerived().setEdited();
                                }
                            }
                        })
                        .setWidth(AppUtil.dpToPixels(300))
                        .useNegBt(true)
                        .setTitleAndGetInstance(R.string.dlg_title_set_wear_lvl)
                        .show();
                }
            });

            _imgQ4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int currentScore = _molar.surface().q4Wear().score();

                    final PopupListDialog<String> dlg = new PopupListDialog<>(getActivityDerived(),
                        WEAR_PICKER_ITEMS,
                        R.layout.list_item_simple_checkable_large_center_align,
                        R.id.lbl_listitem_checkable);
                    final View title = getActivity().getLayoutInflater().inflate(R.layout.dialog_wear_picker_title, null);
                    dlg.setCustomTitle(title);
                    final ImageView btWearHelp = title.findViewById(R.id.bt_wear_descriptions);
                    btWearHelp.setOnClickListener(wearDescriptionsListener);
                    dlg.setFooterDividersEnabled(false)
                        .setHeaderDividersEnabled(false)
                        .setHighlighted(((currentScore == Wear.UNKNOWN.score()) ? 0 : currentScore+1), R.id.layout_listitem_large)
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final int score = position - 1;
                                if (score != currentScore) {
                                    _molar.surface().setQ4(score);
                                    updateElements();
                                    getActivityDerived().setEdited();
                                }
                            }
                        })
                        .setWidth(AppUtil.dpToPixels(300))
                        .useNegBt(true)
                        .setTitleAndGetInstance(R.string.dlg_title_set_wear_lvl)
                        .show();
                }
            });

            btNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TextInputDialog dlg = new TextInputDialog(
                        new DialogStringData(getActivityDerived(),
                            R.string.dlg_title_molar_note,
                            R.string.dlg_msg_molar_note,
                            R.string.dlg_bt_ok,
                            R.string.dlg_bt_cancel),
                        R.string.dlg_hint_molar_note );

                    DisplayMetrics screenDimens = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(screenDimens);
                    dlg.setCloseOnEnterKeyPress(false)
                       .setMinWidthPx((int)(screenDimens.widthPixels*0.8));

                    dlg.textInput().setInputType(InputType.TYPE_CLASS_TEXT);
                    dlg.textInput().setSingleLine(false);
                    dlg.textInput().setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
                    dlg.textInput().setVerticalScrollBarEnabled(true);
                    dlg.textInput().setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
                    if (getActivity() != null) {
                        dlg.textInput().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryLight5));
                    }
                    dlg.textInput().setPadding(6, 0, 6, 6);
                    dlg.textInput().setLines(3);
                    dlg.textInput().setGravity(Gravity.TOP);
                    dlg.textInput().setMinWidth((int)(screenDimens.widthPixels*0.8)-24);
                    dlg.textInput().setMaxHeight(250);
                    dlg.setText(_molar.notes());
                    dlg.setPosBt(new View.OnClickListener() {
                        public void onClick(View view) {
                            // User clicked "Ok" button
                            AppUtil.hideKeyboard(getActivityDerived(), dlg.linearLayout());
                            if (!_molar.notes().equals(dlg.text())) {
                                _molar.setNotes(dlg.text());
                                getActivityDerived().setEdited();
                            }
                            dlg.dismiss();
                        }
                    });
                    dlg.setNegBt(new View.OnClickListener() {
                        public void onClick(View view) {
                            AppUtil.hideKeyboard(getActivityDerived(), dlg.linearLayout());
                        }
                    });
                    dlg.show();
                }
            });

//            btPhoto.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // @TODO
//                }
//            });

            updateElements();

        } else {
            // Invalid project or subject index
            getActivityDerived().closeSubjectEditor();
        }
        return view;
    }


    //////////// Actions ////////////

    public void updateElements() {
        for (int i = 0; i < _imgQs.length; i++) {
            if (_quads[i].wearScore() == Wear.UNKNOWN.score()) {
                _imgQs[i].setAlpha(0.5f);
                if (getActivity() != null) {
                    Drawable img = FileUtil.Cache.MOLAR_WEAR_IMAGES.get(_imgIds[i][9]);
                    if (img == null) {
                        img = FileUtil.resizeImage(getActivityDerived(), _imgIds[i][9], AppUtil.dpToPixels(WearImageCacheMap.WEAR_IMAGE_DIMENSION_DP));
                        FileUtil.Cache.MOLAR_WEAR_IMAGES.put(_imgIds[i][9], img);
                    }
                    _imgQs[i].setImageDrawable(img);
                }
            } else {
                _imgQs[i].setAlpha(1.0f);
                if (getActivity() != null) {
                    Drawable img = FileUtil.Cache.MOLAR_WEAR_IMAGES.get(_imgIds[i][_quads[i].wearScore()]);
                    if (img == null) {
                        img = FileUtil.resizeImage(getActivityDerived(), _imgIds[i][_quads[i].wearScore()], AppUtil.dpToPixels(WearImageCacheMap.WEAR_IMAGE_DIMENSION_DP));
                        FileUtil.Cache.MOLAR_WEAR_IMAGES.put(_imgIds[i][_quads[i].wearScore()], img);
                    }
                    _imgQs[i].setImageDrawable(img);
                }
            }
        }
    }


    //////////// Accessors ////////////

    public ViewProjectActivity getActivityDerived() {
        return (ViewProjectActivity) super.getActivity();
    }

    public int getProjectIndex() {
        return _projectIndex;
    }

    public int getSubjectIndex() {
        return _subjectIndex;
    }
}
