package mw.molarwear.gui.fragment;

import android.support.v4.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.gui.activity.ViewProjectActivity;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.TextInputDialog;
import mw.molarwear.gui.dialog.TwoButtonDialog;
import mw.molarwear.gui.list.SubjectArrayAdapter;
import mw.molarwear.util.FileUtility;

/**
 *
 *
 * @author Sean Pesce
 */

public class SubjectsListFragment extends ListFragment {

    public static final String SELECTION_INDEX_KEY   = "curSubjChoice";

    private int _selectionIndex = AdapterView.INVALID_POSITION;


    /**
     * Create a new instance of {@link SubjectsListFragment}, initialized to
     * show the data for the {@link mw.molarwear.data.classes.MolarWearProject project} at 'index'.
     *
     * @param index the index of the {@link mw.molarwear.data.classes.MolarWearProject project}
     *              whose data will be represented by the new {@link SubjectsListFragment}.
     *
     * @return a new instance of {@link SubjectsListFragment}, initialized to show the data for the
     *          {@link mw.molarwear.data.classes.MolarWearProject project} at 'index'.
     */
    public static SubjectsListFragment newInstance(int index) {
        // Reference: https://github.com/linuxjava/Support4Demos/blob/master/app/src/main/java/com/example/android/supportv4/app/FragmentLayoutSupport.java
        SubjectsListFragment f = new SubjectsListFragment();
        // Supply project index as argument
        Bundle args = new Bundle();
        args.putInt(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, index);
        f.setArguments(args);
        return f;
    }

    private View.OnClickListener _editSubjectIdListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final TextInputDialog dlg = new TextInputDialog(
                new DialogStringData(getActivity(),
                    R.string.dlg_title_edit_subject,
                    "",
                    R.string.dlg_bt_submit,
                    R.string.dlg_bt_cancel),
                getProject().getSubject(_selectionIndex).id());

            dlg.textInput().setFilters(new InputFilter[] { FileUtility.WHITESPACE_FILTER });
            dlg.setText(getProject().getSubject(_selectionIndex).id());
            dlg.setPositiveButton(new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked "Submit" button
                    if (dlg.text().length() > 0 && !dlg.text().equals(dlg.textInputHint())) {
                        if (getProject().hasSubjectWithId(dlg.text())) {
                            final TwoButtonDialog existsDlg = new TwoButtonDialog(new DialogStringData(getActivity(),
                                R.string.err_subj_edit_id_fail,
                                R.string.err_subj_edit_id_fail_exists));
                            existsDlg.show();
                        } else {
                            setSubjectId(_selectionIndex, dlg.text());
                        }
                    }
                }
            });
            dlg.show();
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Populate list with subject IDs
        setListAdapter(getProject().createSubjectArrayAdapter(this));

        getBtNewSubject().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSelection();
                final String defaultSubjectId = MolarWearSubject.DEFAULT_ID;
                String hint = defaultSubjectId;
                if (getProject().hasSubjectWithId(hint)) {
                    int i = 1;
                    hint = defaultSubjectId + i;
                    while (getProject().hasSubjectWithId(hint)) {
                        i++;
                        hint = defaultSubjectId + i;
                    }
                }
                final TextInputDialog dlg = new TextInputDialog(new DialogStringData(getActivity(),
                    R.string.dlg_title_new_subject,
                    "",
                    R.string.dlg_bt_add,
                    R.string.dlg_bt_cancel),
                    hint);
                dlg.textInput().setFilters(new InputFilter[] { FileUtility.WHITESPACE_FILTER });
                dlg.setText(hint);
                dlg.setPositiveButton(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked "Add" button
                        final String newId = (!dlg.text().isEmpty()) ? dlg.text() : dlg.textInputHint();
                        if (getProject().hasSubjectWithId(newId)) {
                            final TwoButtonDialog existsDlg = new TwoButtonDialog(new DialogStringData(getActivity(),
                                R.string.err_subj_create_fail,
                                R.string.err_subj_create_fail_exists));
                            existsDlg.show();
                        } else {
                            add(new MolarWearSubject(newId));
                        }
                    }
                });
                dlg.show();
            }
        });

        getBtEditSubject().setOnClickListener(_editSubjectIdListener);
        getBtEditSubject2().setOnClickListener(_editSubjectIdListener);

        getBtDeleteSubject().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwoButtonDialog dlg = new TwoButtonDialog(new DialogStringData(getActivity(),
                    getActivity().getResources().getString(R.string.dlg_title_del_subj_conf)
                        + " (\"" + getProject().getSubject(_selectionIndex).id() + "\")",
                    R.string.dlg_msg_del_subj_conf,
                    R.string.dlg_bt_yes,
                    R.string.dlg_bt_no));
                dlg.setPositiveButton(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        remove(_selectionIndex);
                    }
                });
                dlg.show();
            }
        });

        if (savedInstanceState != null) {
            // Restore last state for index of selected item
            selectItem(savedInstanceState.getInt(SELECTION_INDEX_KEY, AdapterView.INVALID_POSITION));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText(" ");
        TextView emptyView = (TextView)getListView().getEmptyView();
        emptyView.setText(getActivity().getString(R.string.no_subjects));
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
        emptyView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryLight3));
    }

    @Override
    public void onViewStateRestored(Bundle inState) {
        super.onViewStateRestored(inState);
        if (inState != null) {
            _selectionIndex = inState.getInt(SELECTION_INDEX_KEY, _selectionIndex);
        }

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
        outState.putInt(SELECTION_INDEX_KEY, _selectionIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (_selectionIndex == position) {
            clearSelection();
        } else {
            selectItem(position);
        }
    }

    public ViewProjectActivity getActivityDerived() {
        return (ViewProjectActivity) super.getActivity();
    }

    public void selectItem(int index) {
        selectItem(index, true);
    }

    public void selectItem(int index, boolean updateToolbar) {
        _selectionIndex = index;
        getListView().setItemChecked(_selectionIndex, true);
        if (_selectionIndex >= 0 && _selectionIndex < ((SubjectArrayAdapter)getListAdapter()).getCount()) {
            if (updateToolbar) {
                getBtEditSubject().setVisibility(View.VISIBLE);
                getBtDeleteSubject().setVisibility(View.VISIBLE);
            }
        } else {
            clearSelection();
        }
    }

    public void clearSelection() {
        getListView().setItemChecked(_selectionIndex, false);
        _selectionIndex = AdapterView.INVALID_POSITION;
        getBtEditSubject().setVisibility(View.GONE);
        getBtDeleteSubject().setVisibility(View.GONE);
    }


    //////////// Accessors ////////////

    public MolarWearProject project() { return getProject(); }
    public MolarWearSubject get(int index) { return getProject().getSubject(index); }
    public int selectionIndex() { return _selectionIndex; }
    public int projectIndex() { return getProjectIndex(); }
    public boolean isSaved() { return getProject().isSaved(); }

    public FloatingActionButton getBtNewSubject() {
        return getActivityDerived().getBtNewSubject();
    }

    public ImageButton getBtSave() {
        return getActivityDerived().getBtSave();
    }

    public ImageButton getBtEditSubject() {
        return getActivityDerived().getBtEditSubject();
    }

    public ImageButton getBtEditSubject2() {
        return getActivityDerived().getBtEditSubject2();
    }

    public ImageButton getBtDeleteSubject() {
        return getActivityDerived().getBtDeleteSubject();
    }

    public int getProjectIndex() {
        return getActivityDerived().getProjectIndex();
    }

    public MolarWearProject getProject() {
        return getActivityDerived().getProject();
    }


    //////////// Mutators ////////////

    public void add(@NonNull MolarWearSubject subject) {
        getProject().addSubject(subject);
        getActivityDerived().notifyDataSetChanged(false);
    }

    public void remove(@IntRange(from=0) int index) {
        if (index == getProject().subjectCount() - 1) {
            clearSelection();
        }
        getProject().removeSubject(index);
        getActivityDerived().notifyDataSetChanged(false);
    }

    public void setSubjectId(int index, @NonNull String id) {
        if (id.length() > 0 && !id.equals(getProject().getSubject(index).id())) {
            getProject().getSubject(index).setId(id);
            getActivityDerived().setSubjectTitle(id);
            getActivityDerived().notifyDataSetChanged(true);
        }
    }
}
