package mw.molarwear.gui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import mw.molarwear.R;
import mw.molarwear.data.classes.dental.enums.ToothMapping;
import mw.molarwear.data.classes.dental.molar.Molar;
import mw.molarwear.data.classes.dental.molar.SurfaceQuadrant;
import mw.molarwear.data.classes.dental.molar.enums.Wear;
import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.gui.activity.ViewProjectActivity;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.TextInputDialog;
import mw.molarwear.gui.dialog.WearPickerDialog;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.card_molar_fragment, container, false);

        if (args != null) {
            _projectIndex = args.getInt(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, _projectIndex);
            _subjectIndex = args.getInt(ViewProjectActivity.SUBJECT_INDEX_ARG_KEY, _subjectIndex);
            final int toothIndex = args.getInt(ViewProjectActivity.SUBJECT_MOLAR_ARG_KEY, (_toothMapping != null) ? _toothMapping.index() : ToothMapping.MOLAR1_L_L.index());
            _toothMapping = ToothMapping.get(toothIndex);
        }

        if (_projectIndex >= 0 && _subjectIndex >= 0) {

            ImageButton btNotes = view.findViewById(R.id.bt_molar_notes);
            ImageButton btPhoto = view.findViewById(R.id.bt_molar_photo);

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
                    final WearPickerDialog dlg = new WearPickerDialog(getActivity(), currentScore);

                    dlg.setPositiveButton(new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked "Ok" button
                            if (currentScore != dlg.selectionWear()) {
                                _molar.surface().setQ1(dlg.selectionWear());
                                updateElements();
                                getActivityDerived().setEdited();
                            }
                        }
                    });
                    dlg.show();
                }
            });

            _imgQ2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int currentScore = _molar.surface().q2Wear().score();
                    final WearPickerDialog dlg = new WearPickerDialog(getActivity(), currentScore);

                    dlg.setPositiveButton(new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked "Ok" button
                            if (currentScore != dlg.selectionWear()) {
                                _molar.surface().setQ2(dlg.selectionWear());
                                updateElements();
                                getActivityDerived().setEdited();
                            }
                        }
                    });
                    dlg.show();
                }
            });

            _imgQ3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int currentScore = _molar.surface().q3Wear().score();
                    final WearPickerDialog dlg = new WearPickerDialog(getActivity(), currentScore);

                    dlg.setPositiveButton(new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked "Ok" button
                            if (currentScore != dlg.selectionWear()) {
                                _molar.surface().setQ3(dlg.selectionWear());
                                updateElements();
                                getActivityDerived().setEdited();
                            }
                        }
                    });
                    dlg.show();
                }
            });

            _imgQ4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int currentScore = _molar.surface().q4Wear().score();
                    final WearPickerDialog dlg = new WearPickerDialog(getActivity(), currentScore);

                    dlg.setPositiveButton(new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked "Ok" button
                            if (currentScore != dlg.selectionWear()) {
                                _molar.surface().setQ4(dlg.selectionWear());
                                updateElements();
                                getActivityDerived().setEdited();
                            }
                        }
                    });
                    dlg.show();
                }
            });

            btNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TextInputDialog dlg = new TextInputDialog(
                        new DialogStringData(getActivity(),
                            R.string.dlg_title_molar_note,
                            R.string.dlg_msg_molar_note,
                            R.string.dlg_bt_ok,
                            R.string.dlg_bt_cancel),
                        R.string.dlg_hint_molar_note );

                    dlg.textInput().setInputType(InputType.TYPE_CLASS_TEXT);
                    dlg.textInput().setSingleLine(false);
                    dlg.textInput().setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
                    dlg.textInput().setVerticalScrollBarEnabled(true);
                    dlg.textInput().setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
                    dlg.textInput().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryLight5));
                    dlg.textInput().setPadding(6, 0, 6, 6);
                    dlg.textInput().setLines(3);
                    dlg.textInput().setGravity(Gravity.TOP);
                    dlg.textInput().setMaxHeight(250);
                    dlg.setText(_molar.notes());
                    dlg.setPositiveButton(new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked "Ok" button
                            if (!_molar.notes().equals(dlg.text())) {
                                _molar.setNotes(dlg.text());
                                getActivityDerived().setEdited();
                            }
                        }
                    });
                    dlg.show();
                }
            });

            btPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // @TODO
                }
            });

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
                _imgQs[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), _imgIds[i][9]));
            } else {
                _imgQs[i].setAlpha(1.0f);
                _imgQs[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), _imgIds[i][_quads[i].wearScore()]));
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
