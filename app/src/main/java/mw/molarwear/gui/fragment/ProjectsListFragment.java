package mw.molarwear.gui.fragment;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.ListView;
import android.widget.TextView;

import mw.molarwear.R;
import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.gui.list.ProjectArrayAdapter;

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

    private int _selectionIndex = AdapterView.INVALID_POSITION;

    // GUI
    private FloatingActionButton _btNewProject = null;
    private AppCompatImageButton   _btEditProj = null;
    private AppCompatImageButton _btDeleteProj = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Populate list with project titles
        setListAdapter(new ProjectArrayAdapter(ProjectHandler.PROJECTS, this));

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        _btNewProject = (FloatingActionButton) getActivity().findViewById(R.id.bt_new_project);
        _btEditProj   = (AppCompatImageButton) getActivity().findViewById(R.id.bt_toolbar_edit_proj);
        _btDeleteProj = (AppCompatImageButton) getActivity().findViewById(R.id.bt_toolbar_delete_proj);

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
            _btEditProj.setVisibility(View.VISIBLE);
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

    public int selectionIndex() { return _selectionIndex; }

}
