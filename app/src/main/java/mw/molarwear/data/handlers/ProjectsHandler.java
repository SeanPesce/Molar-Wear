package mw.molarwear.data.handlers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.TextInputDialog;
import mw.molarwear.gui.list.ProjectListView;

/**
 * This handler class allows the user to view, organize, create, modify, etc. sets of
 *  {@link MolarWearProject} objects.
 *
 * @author Sean Pesce
 *
 * @see    MolarWearProject
 * @see    ProjectListView
 * @see    mw.molarwear.gui.activity.ChooseProjectActivity
 */

public class ProjectsHandler {

    ///////////////////////////////////////////////////////////////
    ///////////////////////// STATIC DATA /////////////////////////
    ///////////////////////////////////////////////////////////////

    private static final int NO_SELECTION_INDEX = -1;
    private static final ArrayList<MolarWearProject> PROJECTS = new ArrayList<>();
    private static final ArrayList<String>             TITLES = new ArrayList<>();


    public static int projectCount() {
        return PROJECTS.size();
    }


    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    private final Activity _activity;


    private int _selectedItem = NO_SELECTION_INDEX; // Index of selected project

    // GUI
    private      ProjectListView _listView         = null;
    private             TextView _emptyView        = null;
    private ArrayAdapter<String> _arrayAdapter     = null;
    private      TextInputDialog _dlgCreateProject = null;


    //////////// Constructors ////////////

    public ProjectsHandler(Activity activity) {
        _activity = activity;
        initialize();
    }

    public ProjectsHandler(Activity activity, int selectedItem) {
        _activity = activity;
        if (selectedItem >= 0 && selectedItem < PROJECTS.size()) {
            _selectedItem = selectedItem;
        }
        initialize();
    }


    private void initialize() {
        // Projects list
        _listView = (ProjectListView) findViewById(R.id.listview_projects);
        _listView.setProjectsHandler(this);

        _emptyView = (TextView) findViewById(R.id.lbl_listview_projects_empty);
        _listView.setEmptyView(_emptyView);

        _arrayAdapter = new ArrayAdapter<>(_activity, R.layout.list_item_project, R.id.lbl_listitem_proj_title, TITLES);
        _listView.setAdapter(_arrayAdapter);

        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int index, long id) {
                _listView.setExpandNextNewChild(false);
                setSelection(index);
            }
        });

        // @TODO: Delete the test code below when it's no longer needed
        addProject(new MolarWearProject("Test Project 1"));
        addProject(new MolarWearProject("Test Project 2"));
        addProject(new MolarWearProject("Test Project 3 (long title) Test Test Test Test Test Test Test"));
        // End of test code

        // "New project" dialog
        initCreateProjDialog();
    }


    private void initCreateProjDialog() {
        _dlgCreateProject = new TextInputDialog(
                                     new DialogStringData(_activity,
                                                          R.string.dlg_title_new_project,
                                                          "",
                                                          R.string.dlg_bt_create,
                                                          R.string.dlg_bt_cancel),
                                     R.string.dlg_hint_new_project);

        _dlgCreateProject.setPositiveButton(R.string.dlg_bt_create, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked "Create" button
                addProject(new MolarWearProject(_dlgCreateProject.text()), true);
                _dlgCreateProject.eraseText();
                // @TODO: Create file
            }
        });
    }


    //////////// Accessors ////////////

    public <T extends View> T findViewById(int id) {
        return _activity.findViewById(id);
    }

    public Resources getResources() {
        return _activity.getResources();
    }

    public int indexOfChildView(View child) {
        return _listView.indexOfChild(child);
    }


    public int indexOfProject(MolarWearProject project) {
        for (int i = 0; i < PROJECTS.size(); i++) {
            if (project == PROJECTS.get(i)) {
                return i;
            }
        }
        return -1;
    }


    public int selectionIndex() {
        return _selectedItem;
    }


    public View selectedChild() {
        if (_selectedItem >= 0 && _selectedItem < _listView.getChildCount()) {
            return _listView.getChildAt(_selectedItem);
        } else {
            return null;
        }
    }


    public MolarWearProject selectedProject() {
        if (_selectedItem >= 0 && _selectedItem < PROJECTS.size()) {
            return PROJECTS.get(_selectedItem);
        } else {
            return null;
        }
    }


    //////////// Mutators ////////////

    public void setSelection(View child) {
        int index = indexOfChildView(child);
        setSelection(index, true);
    }


    public void setSelection(View child, boolean expandChild) {
        int index = indexOfChildView(child);
        setSelection(index, expandChild);
    }


    public void setSelection(int index) {
        setSelection(index, true);
    }


    public void setSelection(int index, boolean expandChild) {
        // If invalid index was passed, return
        if (index < 0 || index >= _listView.getChildCount()) {
            return;
        }

        // Un-select previous selection
        if(_selectedItem != index) {
            clearSelection();
        }

        final View childView = _listView.getChildAt(index);
        LinearLayout newSelection    = (LinearLayout) childView.findViewById(R.id.layout_listitem_proj);
        LinearLayout layoutButtonBar = (LinearLayout) childView.findViewById(R.id.layout_listitem_proj_buttons);
        TextView     lblTitle        = (TextView)     childView.findViewById(R.id.lbl_listitem_proj_title);

        // Select new item
        _selectedItem = index;
        if (layoutButtonBar.getVisibility() == View.GONE) {
            if (expandChild) {
                layoutButtonBar.setVisibility(View.VISIBLE);
            }
        } else {
            layoutButtonBar.setVisibility(View.GONE);
        }
        lblTitle.setTextColor(_activity.getResources().getColor(R.color.grayDark));
        newSelection.setBackgroundColor(_activity.getResources().getColor(R.color.colorPrimaryLight5));
    }


    public void clearSelection() {
        if(_selectedItem >= 0 && _selectedItem < _listView.getChildCount()) {
            LinearLayout prevSelection  = (LinearLayout) _listView.getChildAt(_selectedItem).findViewById(R.id.layout_listitem_proj);
            LinearLayout prevSelButtons = (LinearLayout) _listView.getChildAt(_selectedItem).findViewById(R.id.layout_listitem_proj_buttons);
            TextView prevSelLabel       = (TextView)     _listView.getChildAt(_selectedItem).findViewById(R.id.lbl_listitem_proj_title);
            prevSelButtons.setVisibility(View.GONE);
            prevSelection.setBackgroundColor(Color.WHITE);
            prevSelLabel.setTextColor(_activity.getResources().getColor(R.color.gray));
        }
        _selectedItem = NO_SELECTION_INDEX;
    }

    public void addProject(MolarWearProject project) {
        this.addProject(project, false);
    }

    public void addProject(MolarWearProject project, boolean expandNewChild) {
        PROJECTS.add(project);
        TITLES.add(project.title());
        this.notifyDataSetChanged(expandNewChild);
    }

    public void deleteProject(int index) {
        if (index >= 0 && index < PROJECTS.size()) {
            PROJECTS.remove(index);
            TITLES.remove(index);
            this.notifyDataSetChanged();
            // @TODO: Delete file
        }
    }

    public void deleteProject(MolarWearProject project) {
        deleteProject(indexOfProject(project));
    }



    //////////// Utility ////////////

    public void openCreateProjectDialog() {
        _dlgCreateProject.show();
    }

    public void notifyDataSetChanged() {
        notifyDataSetChanged(false);
    }

    public void notifyDataSetChanged(final boolean expandNewestChild) {
        _activity.runOnUiThread(new Runnable() {
            public void run() {
                _listView.setExpandNextNewChild(expandNewestChild);
                _arrayAdapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * Returns a newly-created list of project titles (only the titles; no other data).
     *
     * @return {@link List} of {@link String}s containing the title of each project
     */
    public static ArrayList<String> titlesList() {
        ArrayList<String> titles = new ArrayList<>();
        for (MolarWearProject p : PROJECTS) {
            titles.add(p.title());
        }
        return titles;
    }

    /**
     * Searches the list of existing {@link MolarWearProject}s to see if a project with the given
     *  title already exists.
     *
     * @param  title the project title to search for.
     * @return true if a project with the given name exists, and false otherwise.
     */
    public static boolean projectWithTitleExists(String title) {
        for(MolarWearProject p : PROJECTS) {
            if (title.equals(p.title())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches the list of existing {@link MolarWearProject} for an existing project with the given
     *  title.
     *
     * @param  title the project title to search for.
     * @return the index of the first project with the given name, or -1 if none was found.
     */
    public static int indexOfProjectWithTitle(String title) {
        for(int i = 0; i < PROJECTS.size(); i++) {
            if (title.equals(PROJECTS.get(i).title())) {
                return i;
            }
        }
        return -1;
    }
}
