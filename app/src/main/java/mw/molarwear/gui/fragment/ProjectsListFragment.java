package mw.molarwear.gui.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.ListView;
import android.widget.TextView;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.gui.activity.MolWearMainActivity;
import mw.molarwear.gui.activity.ViewProjectActivity;
import mw.molarwear.gui.dialog.BasicDialog;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.MultipleAnalysisDialog;
import mw.molarwear.gui.dialog.PopupListDialog;
import mw.molarwear.gui.list.ProjectArrayAdapter;
import mw.molarwear.util.AppUtil;
import mw.molarwear.util.FileUtil;

/**
 *
 *
 * @author <a href="https://developer.android.com/guide/components/fragments.html">Android SDK Sample</a>
 * @author Sean Pesce
 *
 * @see ListFragment
 * @see mw.molarwear.data.classes.MolarWearProject
 * @see ProjectArrayAdapter
 * @see ProjectHandler
 */

public class ProjectsListFragment extends ListFragment {

    public static final String SELECTION_INDEX_KEY = "curProjChoice";


    public static final int OPTION_OPEN    = 0;
    public static final int OPTION_ANALYZE = 1;
    public static final int OPTION_EXPORT  = 2;
    public static final int OPTION_RENAME  = 3;
    public static final int OPTION_DELETE  = 4;
    public static final String[] LONG_PRESS_ITEMS = {
                                                        "Open",
                                                        "Analyze",
                                                        "Export",
                                                        "Rename",
                                                        "Delete"
                                                    };


    private final ProjectsListFragment _this = this;

    private int _selectionIndex = AdapterView.INVALID_POSITION;

    // GUI
    private FloatingActionButton _btNewProject = null;
    private AppCompatImageButton   _btEditProj = null;
    private AppCompatImageButton _btDeleteProj = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final View footer = LayoutInflater.from(getActivity()).inflate(R.layout.list_frag_footer, null, false);
        getListView().addFooterView(footer, null, false);
        getListView().setDividerHeight(0);

        // Populate list with project titles
        setListAdapter(new ProjectArrayAdapter(ProjectHandler.PROJECTS, this));

        _btNewProject = getActivity().findViewById(R.id.bt_new_project);
        _btEditProj   = getActivity().findViewById(R.id.bt_toolbar_edit_proj);
        _btDeleteProj = getActivity().findViewById(R.id.bt_toolbar_delete_proj);

        _btNewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectHandler.newProject();
            }
        });

        _btEditProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectHandler.editTitle(_selectionIndex);
            }
        });

        _btDeleteProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectHandler.deleteProject(_selectionIndex);
            }
        });

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
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            switch (pos) {
                                case OPTION_OPEN:
                                    Intent intent = new Intent(getActivity().getApplicationContext(), ViewProjectActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    intent.putExtra(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, _selectionIndex);
                                    getActivity().startActivity(intent);
                                    break;
                                case OPTION_ANALYZE:
                                    final MolarWearSubject[] subjects = new MolarWearSubject[ProjectHandler.get(_selectionIndex).subjectCount()];
                                    for (int i = 0; i < ProjectHandler.get(_selectionIndex).subjectCount(); i++) {
                                        subjects[i] = ProjectHandler.get(_selectionIndex).getSubject(i);
                                    }
                                    final MultipleAnalysisDialog dlgAnalyze = new MultipleAnalysisDialog(getActivityDerived(), subjects);
                                    dlgAnalyze.show();
                                    break;
                                case OPTION_EXPORT:
                                    ProjectHandler.openExportDialog(getActivityDerived());
                                    break;
                                case OPTION_RENAME:
                                    ProjectHandler.editTitle(_selectionIndex);
                                    break;
                                case OPTION_DELETE:
                                    ProjectHandler.deleteProject(_selectionIndex);
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
        emptyView.setText(getActivity().getString(R.string.no_projects));
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
        emptyView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryLight3));
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        ProjectHandler.projectsFragment = this;
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

    public void selectItem(int index) {
        selectItem(index, true);
    }

    public void selectItem(int index, boolean updateToolbar) {
        _selectionIndex = index;
        getListView().setItemChecked(_selectionIndex, true);
        if (_selectionIndex >= 0 && _selectionIndex < ProjectHandler.projectCount()) {
            if (updateToolbar) {
                updateToolbar();
            }
        } else {
            clearSelection();
        }
    }

    public void clearSelection() {
        getListView().setItemChecked(_selectionIndex, false);
        _selectionIndex = AdapterView.INVALID_POSITION;
        updateToolbar();
    }

    public void updateToolbar() {
        int listViewSelection = getListView().getCheckedItemPosition();
        if (listViewSelection >= 0 && listViewSelection < ProjectHandler.projectCount()) {
            if (listViewSelection != _selectionIndex) {
                _selectionIndex = listViewSelection;
            }
        } else if (_selectionIndex >= 0 && _selectionIndex < ProjectHandler.projectCount()) {
            if (listViewSelection != _selectionIndex) {
                getListView().setItemChecked(_selectionIndex, true);
            }
        }

        if (_selectionIndex >= 0 && _selectionIndex < ProjectHandler.projectCount()) {
            //_btEditProj.setVisibility(View.VISIBLE);
            _btDeleteProj.setVisibility(View.VISIBLE);
            //getActivity().setTitle(ProjectHandler.PROJECTS.get(_selectionIndex).title());
        } else {
            _btEditProj.setVisibility(View.GONE);
            _btDeleteProj.setVisibility(View.GONE);
            //getActivity().setTitle(getActivity().getResources().getString(R.string.title_choose_proj));
        }
    }

    public void notifyDataSetChanged() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                ((ProjectArrayAdapter)getListAdapter()).notifyDataSetChanged();
            }
        });
    }

    public MolWearMainActivity getActivityDerived() {
        return (MolWearMainActivity) super.getActivity();
    }

    public int selectionIndex() { return _selectionIndex; }

}
