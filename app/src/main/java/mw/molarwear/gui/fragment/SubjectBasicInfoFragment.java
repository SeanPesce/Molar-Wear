package mw.molarwear.gui.fragment;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.gui.activity.ViewProjectActivity;

/**
 *
 *
 * @author Sean Pesce
 *
 * @see Fragment
 * @see mw.molarwear.data.classes.MolarWearSubject
 * @see ViewProjectActivity
 */

public class SubjectBasicInfoFragment extends Fragment {

    /**
     * Create a new instance of {@link SubjectBasicInfoFragment}, initialized to
     * show the data for the {@link mw.molarwear.data.classes.MolarWearSubject subject} at
     *  'subjectIndex' in the subjects list of the {@link mw.molarwear.data.classes.MolarWearProject project}
     *  at 'projectIndex'.
     *
     * @param projectIndex the index of the {@link mw.molarwear.data.classes.MolarWearProject project}
     *                      containing the {@link mw.molarwear.data.classes.MolarWearSubject subject}.
     * @param subjectIndex the index of the {@link mw.molarwear.data.classes.MolarWearSubject subject}
     *                      whose data will be represented by the new {@link SubjectBasicInfoFragment}.
     *
     * @return a new instance of {@link SubjectBasicInfoFragment}, initialized to show the data for the
     *          {@link mw.molarwear.data.classes.MolarWearSubject subject} at 'subjectIndex' in the
     *          subjects list of the {@link mw.molarwear.data.classes.MolarWearProject project} at
     *          'projectIndex'.
     */
    public static SubjectBasicInfoFragment newInstance(int projectIndex, int subjectIndex) {
        SubjectBasicInfoFragment f = new SubjectBasicInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, projectIndex);
        args.putInt(ViewProjectActivity.SUBJECT_INDEX_ARG_KEY, subjectIndex);
        f.setArguments(args);
        return f;
    }

    private int _projectIndex = AdapterView.INVALID_POSITION;
    private int _subjectIndex = AdapterView.INVALID_POSITION;
    private MolarWearSubject _subject;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.view_subject_basic_info, container, false);

        if (args != null) {
            _projectIndex = args.getInt(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, _projectIndex);
            _subjectIndex = args.getInt(ViewProjectActivity.SUBJECT_INDEX_ARG_KEY, _subjectIndex);
        }

        if (_projectIndex >= 0 && _subjectIndex >= 0) {

            _subject = ProjectHandler.PROJECTS.get(_projectIndex).getSubject(_subjectIndex);

            final TextInputLayout txtId = view.findViewById(R.id.txt_subject_id);
            final TextInputLayout txtSiteId = view.findViewById(R.id.txt_site_id);
            final TextInputLayout txtAge = view.findViewById(R.id.txt_subject_age);
            final RadioGroup rgSex = view.findViewById(R.id.radio_group_subject_sex);

            txtId.getEditText().setText(_subject.id());
            txtSiteId.getEditText().setText((_subject.siteId() != null) ? _subject.siteId() : "");
            txtAge.getEditText().setText((_subject.age() >= 0 && _subject.age() <= MolarWearSubject.MAX_AGE) ? (Integer.toString(_subject.age())) : "");
            if (_subject.sex() == MolarWearSubject.SEX.MALE) {
                rgSex.check(R.id.radio_sex_m);
            } else if (_subject.sex() == MolarWearSubject.SEX.FEMALE) {
                rgSex.check(R.id.radio_sex_f);
            } else {
                rgSex.check(R.id.radio_sex_u);
            }

            txtId.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!validateSubjectIdInput(txtId.getEditText().getText().toString())) {
                        txtId.requestFocus();
                        txtId.setError("Invalid ID");
                    } else {
                        txtId.requestFocus();
                        txtId.setError(null);
                        getActivityDerived().setSubjectTitle(s.toString());
                    }
                }
            });

            txtSiteId.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!validateSiteIdInput(txtSiteId.getEditText().getText().toString())) {
                        txtSiteId.requestFocus();
                        txtSiteId.setError("Invalid site ID");
                    } else {
                        txtSiteId.requestFocus();
                        txtSiteId.setError(null);
                    }
                }
            });

            txtAge.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!validateAgeInput(txtAge.getEditText().getText().toString())) {
                        txtAge.requestFocus();
                        txtAge.setError("Invalid age");
                    } else {
                        txtAge.requestFocus();
                        txtAge.setError(null);
                    }
                }
            });

            rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup rg, int checkedId) {
                    if (checkedId == R.id.radio_sex_m) {
                        _subject.setSex(MolarWearSubject.SEX.MALE);
                    } else if (checkedId == R.id.radio_sex_f) {
                        _subject.setSex(MolarWearSubject.SEX.FEMALE);
                    } else {
                        _subject.setSex(MolarWearSubject.SEX.UNKNOWN);
                    }
                    getActivityDerived().setEdited();
                }
            });
        } else {
            // Invalid project or subject index
            getActivityDerived().closeSubjectEditor();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle inState) {
        super.onViewStateRestored(inState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, _projectIndex);
        outState.putInt(ViewProjectActivity.SUBJECT_INDEX_ARG_KEY, _subjectIndex);
        super.onSaveInstanceState(outState);
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


    //////////// Actions ////////////

    public boolean validateSubjectIdInput(String subjectId) {
        String id = subjectId.trim();
        if (id.length() > 0) {
            _subject.setId(id);
            getActivityDerived().setEdited();
            return true;
        } else {
            return false;
        }
    }

    public boolean validateSiteIdInput(String siteId) {
        _subject.setSiteId(siteId.trim());
        getActivityDerived().setEdited();
        return true;
    }

    public boolean validateAgeInput(String ageInput) {
        if (ageInput.length() == 0) {
            _subject.setAge(MolarWearSubject.UNKNOWN_AGE);
            getActivityDerived().setEdited();
            return true;
        }
        try {
            int age = Integer.parseInt(ageInput);
            if (age >= 0 && age <= MolarWearSubject.MAX_AGE) {
                _subject.setAge(age);
                getActivityDerived().setEdited();
                return true;
            }
        } catch (NumberFormatException e) {

        }
        return false;
    }
}
