package mw.molarwear.gui.fragment;

import android.content.DialogInterface;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.gui.activity.ViewProjectActivity;
import mw.molarwear.gui.dialog.BasicDialog;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.PopupListDialog;
import mw.molarwear.gui.dialog.SubjectAnalysisDialog;
import mw.molarwear.gui.dialog.TextInputDialog;
import mw.molarwear.gui.list.SubjectArrayAdapter;
import mw.molarwear.util.AppUtil;
import mw.molarwear.util.FileUtil;

/**
 *
 *
 * @author Sean Pesce
 */

public class SubjectsListFragment extends ListFragment {

    public static final String SELECTION_INDEX_KEY   = "curSubjChoice";

    public static final int OPTION_OPEN = 0;
    public static final int OPTION_ANALYZE = 1;
    public static final int OPTION_RENAME = 2;
    public static final int OPTION_DELETE = 3;
    public static final String[] LONG_PRESS_ITEMS = {
        "Open",
        "Analyze",
        "Rename",
        "Delete"
    };


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

    private View.OnClickListener _newSubjectListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
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
            final TextInputDialog dlg = new TextInputDialog(new DialogStringData(getActivityDerived(),
                R.string.dlg_title_new_subject,
                "",
                R.string.dlg_bt_add,
                R.string.dlg_bt_cancel),
                hint);
            dlg.textInput().setFilters(new InputFilter[] { FileUtil.WHITESPACE_FILTER });
            dlg.setText(hint);
            dlg.setPosBt(new View.OnClickListener() {
                public void onClick(View v) {
                    // User clicked "Add" button
                    AppUtil.hideKeyboard(getActivityDerived(), dlg.linearLayout());
                    final String newId = (!dlg.text().isEmpty()) ? dlg.text() : dlg.textInputHint();
                    if (getProject().hasSubjectWithId(newId)) {
                        dlg.show();
                        dlg.textInput().setError(getActivity().getString(R.string.err_subj_create_fail_exists));
                        //AppUtil.printToast(getActivity(), R.string.err_subj_create_fail);
                    } else {
                        getActivityDerived().closeSubjectEditor();
                        clearSelection();
                        final int count = getProject().subjectCount();
                        add(new MolarWearSubject(newId));
                        if (view.equals(getActivityDerived().getBtNewSubjectTb()) && (getProject().subjectCount() == (count+1))) {
                            getActivityDerived().openSubjectEditor();
                            AppUtil.printToast(getActivityDerived(), "Opened new individual \"" + getActivityDerived().getProject().getSubject(_selectionIndex).id() + "\"", Toast.LENGTH_SHORT);
                        }
                        dlg.dismiss();
                    }
                }
            });
            dlg.setNegBt(new View.OnClickListener() {
                public void onClick(View view) {
                    AppUtil.hideKeyboard(getActivity(), dlg.linearLayout());
                }
            });
            dlg.show();
        }
    };

    private View.OnClickListener _editSubjectIdListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final TextInputDialog dlg = new TextInputDialog(
                new DialogStringData(getActivityDerived(),
                    R.string.dlg_title_edit_subject,
                    "",
                    R.string.dlg_bt_submit,
                    R.string.dlg_bt_cancel),
                getProject().getSubject(_selectionIndex).id());

            dlg.textInput().setFilters(new InputFilter[] { FileUtil.WHITESPACE_FILTER });
            dlg.setText(getProject().getSubject(_selectionIndex).id());
            dlg.setPosBt(new View.OnClickListener() {
                public void onClick(View view) {
                    // User clicked "Submit" button
                    AppUtil.hideKeyboard(getActivity(), dlg.linearLayout());
                    if (dlg.text().length() > 0 && !dlg.text().equals(dlg.textInputHint())) {
                        if (getProject().hasSubjectWithId(dlg.text())) {
//                            new MessageDialog(new DialogStringData(getActivity(),
//                                R.string.err_subj_edit_id_fail,
//                                R.string.err_subj_edit_id_fail_exists));
                            dlg.show();
                            dlg.textInput().setError(getActivity().getString(R.string.err_subj_edit_id_fail_exists));
                        } else {
                            setSubjectId(_selectionIndex, dlg.text());
                            dlg.dismiss();
                        }
                    } else {
                        dlg.dismiss();
                    }
                }
            });
            dlg.setNegBt(new View.OnClickListener() {
                public void onClick(View view) {
                    AppUtil.hideKeyboard(getActivity(), dlg.linearLayout());
                }
            });
            dlg.show();
        }
    };

    private View.OnClickListener _deleteSubjectListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BasicDialog dlg = new BasicDialog(new DialogStringData(getActivityDerived(),
                getActivity().getResources().getString(R.string.dlg_title_del_subj_conf)
                    + " (\"" + getProject().getSubject(_selectionIndex).id() + "\")",
                R.string.dlg_msg_del_subj_conf,
                R.string.dlg_bt_yes,
                R.string.dlg_bt_no));
            dlg.setPosBt(new View.OnClickListener() {
                public void onClick(View view) {
                    getActivityDerived().setPreviousIndices(AdapterView.INVALID_POSITION, AdapterView.INVALID_POSITION);
                    remove(_selectionIndex);
                }
            });
            dlg.show();
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final View footer = LayoutInflater.from(getActivity()).inflate(R.layout.list_frag_footer, null, false);
        getListView().addFooterView(footer, null, false);
        getListView().setDividerHeight(0);

        // Populate list with subject IDs
        setListAdapter(getProject().createSubjectArrayAdapter(this));

        getBtNewSubject().setOnClickListener(_newSubjectListener);
        getBtNewSubjectTb().setOnClickListener(_newSubjectListener);

        getBtEditSubject().setOnClickListener(_editSubjectIdListener);
        getBtEditSubject2().setOnClickListener(_editSubjectIdListener);

        getBtDeleteSubject().setOnClickListener(_deleteSubjectListener);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position, true);
                final PopupListDialog<String> dlg = new PopupListDialog<>(getActivityDerived(),
                        LONG_PRESS_ITEMS,
                        android.R.layout.simple_spinner_dropdown_item)
                    .setFooterDividersEnabled(false)
                    .setHeaderDividersEnabled(false)
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case OPTION_OPEN:
                                    getActivityDerived().openSubjectEditor();
                                    break;
                                case OPTION_ANALYZE:
                                    final SubjectAnalysisDialog dlgAnalyzeSubj = new SubjectAnalysisDialog(getActivityDerived(), getProject().getSubject(_selectionIndex));
                                    dlgAnalyzeSubj.show();
                                    break;
                                case OPTION_RENAME:
                                    _editSubjectIdListener.onClick(view);
                                    break;
                                case OPTION_DELETE:
                                    _deleteSubjectListener.onClick(view);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                dlg.show();
                return true;
            }
        });

        if (savedInstanceState != null) {
            // Restore last state for index of selected item
            selectItem(savedInstanceState.getInt(SELECTION_INDEX_KEY, AdapterView.INVALID_POSITION));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
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
                //getBtEditSubject().setVisibility(View.VISIBLE);
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

    public AppCompatImageButton getBtNewSubjectTb() {
        return getActivityDerived().getBtNewSubjectTb();
    }

    public AppCompatImageButton getBtSave() {
        return getActivityDerived().getBtSave();
    }

    public AppCompatImageButton getBtEditSubject() {
        return getActivityDerived().getBtEditSubject();
    }

    public AppCompatImageButton getBtEditSubject2() {
        return getActivityDerived().getBtEditSubject2();
    }

    public AppCompatImageButton getBtDeleteSubject() {
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
        selectItem(getProject().subjectCount()-1);
        getActivityDerived().notifyDataSetChanged(false);
        //getActivityDerived().openSubjectEditor();
    }

    public void remove(@IntRange(from=0) int index) {
        if (index >= getProject().subjectCount() - 1) {
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
