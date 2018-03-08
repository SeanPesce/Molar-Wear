package mw.molarwear.gui.fragment;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.data.classes.dental.enums.ToothMapping;
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

public class SubjectMolarsViewFragment extends Fragment {
    /**
     * Create a new instance of {@link SubjectMolarsViewFragment}, initialized to
     * show the data for the {@link mw.molarwear.data.classes.MolarWearSubject subject} at
     *  'subjectIndex' in the subjects list of the {@link mw.molarwear.data.classes.MolarWearProject project}
     *  at 'projectIndex'.
     *
     * @param projectIndex the index of the {@link mw.molarwear.data.classes.MolarWearProject project}
     *                      containing the {@link mw.molarwear.data.classes.MolarWearSubject subject}.
     * @param subjectIndex the index of the {@link mw.molarwear.data.classes.MolarWearSubject subject}
     *                      whose data will be represented by the new {@link SubjectMolarsViewFragment}.
     *
     * @return a new instance of {@link SubjectMolarsViewFragment}, initialized to show the data for the
     *          {@link mw.molarwear.data.classes.MolarWearSubject subject} at 'subjectIndex' in the
     *          subjects list of the {@link mw.molarwear.data.classes.MolarWearProject project} at
     *          'projectIndex'.
     */
    public static SubjectMolarsViewFragment newInstance(int projectIndex, int subjectIndex) {
        SubjectMolarsViewFragment f = new SubjectMolarsViewFragment();
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
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.view_subject_molar_data, container, false);

        if (args != null) {
            _projectIndex = args.getInt(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, _projectIndex);
            _subjectIndex = args.getInt(ViewProjectActivity.SUBJECT_INDEX_ARG_KEY, _subjectIndex);
        }

        if (_projectIndex >= 0 && _subjectIndex >= 0) {

            _subject = ProjectHandler.PROJECTS.get(_projectIndex).getSubject(_subjectIndex);

        } else {
            // Invalid project or subject index
            getActivityDerived().closeSubjectEditor();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MolarDataFragment l1Fragment =
            (MolarDataFragment) getChildFragmentManager().findFragmentByTag("frag_edit_subj_molar_data_l1");

        if (l1Fragment == null || l1Fragment.getProjectIndex() != _projectIndex || l1Fragment.getSubjectIndex() != _subjectIndex) {

            l1Fragment = MolarDataFragment.newInstance(_projectIndex, _subjectIndex, ToothMapping.MOLAR1_L_L);
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_molar_l1_container, l1Fragment, "frag_edit_subj_molar_data_l1").commit();

            MolarDataFragment l2Fragment = MolarDataFragment.newInstance(_projectIndex, _subjectIndex, ToothMapping.MOLAR2_L_L);
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_molar_l2_container, l2Fragment, "frag_edit_subj_molar_data_l2").commit();

            MolarDataFragment l3Fragment = MolarDataFragment.newInstance(_projectIndex, _subjectIndex, ToothMapping.MOLAR3_L_L);
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_molar_l3_container, l3Fragment, "frag_edit_subj_molar_data_l3").commit();

            MolarDataFragment r1Fragment = MolarDataFragment.newInstance(_projectIndex, _subjectIndex, ToothMapping.MOLAR1_L_R);
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_molar_r1_container, r1Fragment, "frag_edit_subj_molar_data_r1").commit();

            MolarDataFragment r2Fragment = MolarDataFragment.newInstance(_projectIndex, _subjectIndex, ToothMapping.MOLAR2_L_R);
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_molar_r2_container, r2Fragment, "frag_edit_subj_molar_data_r2").commit();

            MolarDataFragment r3Fragment = MolarDataFragment.newInstance(_projectIndex, _subjectIndex, ToothMapping.MOLAR3_L_R);
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_molar_r3_container, r3Fragment, "frag_edit_subj_molar_data_r3").commit();
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
