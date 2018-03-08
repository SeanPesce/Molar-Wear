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

public class SubjectNotesFragment extends Fragment {
    /**
     * Create a new instance of {@link SubjectNotesFragment}, initialized to
     * show the data for the {@link mw.molarwear.data.classes.MolarWearSubject subject} at
     *  'subjectIndex' in the subjects list of the {@link mw.molarwear.data.classes.MolarWearProject project}
     *  at 'projectIndex'.
     *
     * @param projectIndex the index of the {@link mw.molarwear.data.classes.MolarWearProject project}
     *                      containing the {@link mw.molarwear.data.classes.MolarWearSubject subject}.
     * @param subjectIndex the index of the {@link mw.molarwear.data.classes.MolarWearSubject subject}
     *                      whose data will be represented by the new {@link SubjectNotesFragment}.
     *
     * @return a new instance of {@link SubjectNotesFragment}, initialized to show the data for the
     *          {@link mw.molarwear.data.classes.MolarWearSubject subject} at 'subjectIndex' in the
     *          subjects list of the {@link mw.molarwear.data.classes.MolarWearProject project} at
     *          'projectIndex'.
     */
    public static SubjectNotesFragment newInstance(int projectIndex, int subjectIndex) {
        SubjectNotesFragment f = new SubjectNotesFragment();
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
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.view_subject_notes, container, false);

        if (args != null) {
            _projectIndex = args.getInt(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, _projectIndex);
            _subjectIndex = args.getInt(ViewProjectActivity.SUBJECT_INDEX_ARG_KEY, _subjectIndex);
        }

        if (_projectIndex >= 0 && _subjectIndex >= 0) {

            _subject = ProjectHandler.PROJECTS.get(_projectIndex).getSubject(_subjectIndex);

            final TextInputLayout txtNotes = view.findViewById(R.id.txt_subject_notes);

            txtNotes.getEditText().setText(_subject.notes());

            txtNotes.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    _subject.setNotes(txtNotes.getEditText().getText().toString());
                    getActivityDerived().setEdited();
                }
            });
        } else {
            // Invalid project or subject index
            getActivityDerived().closeSubjectEditor();
        }
        return view;
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
