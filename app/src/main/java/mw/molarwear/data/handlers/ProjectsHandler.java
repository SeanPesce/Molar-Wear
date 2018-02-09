package mw.molarwear.data.handlers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.TextInputDialog;
import mw.molarwear.gui.dialog.TwoButtonDialog;
import mw.molarwear.gui.list.ProjectListView;
import mw.molarwear.util.AppUtility;
import mw.molarwear.util.FileUtility;

import static java.io.File.separator;

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

        // Load existing projects from internal storage
        String [] projs = AppUtility.CONTEXT.fileList();
        for (String s : projs) {
            if (s.endsWith(FileUtility.FILE_EXT_SERIALIZED_DATA)) {
                MolarWearProject p = (MolarWearProject) FileUtility.readSerializable(s);
                if (p == null) {
                    Snackbar.make(_listView, "Error loading " + s, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    addProject(p);
                }
            }
        }

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

        _dlgCreateProject.setPositiveButton(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked "Create" button
                boolean created = createProject(new MolarWearProject((!_dlgCreateProject.text().isEmpty()) ? _dlgCreateProject.text() : _dlgCreateProject.textInputHint()));
                _dlgCreateProject.eraseText();
                _dlgCreateProject.setTextInputHint(R.string.dlg_hint_new_project);
                if (created) {
                    // @TODO: Open new project
                }
            }
        });

        _dlgCreateProject.setNegativeButton(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked "Cancel" button
                _dlgCreateProject.eraseText();
                _dlgCreateProject.setTextInputHint(R.string.dlg_hint_new_project);
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

        ImageButton    btEditProj = (ImageButton) layoutButtonBar.findViewById(R.id.bt_listitem_proj_edit);
        ImageButton   btShareProj = (ImageButton) layoutButtonBar.findViewById(R.id.bt_listitem_proj_share);
        ImageButton btDetailsProj = (ImageButton) layoutButtonBar.findViewById(R.id.bt_listitem_proj_details);
        ImageButton  btDeleteProj = (ImageButton) layoutButtonBar.findViewById(R.id.bt_listitem_proj_delete);

        // Select new item
        _selectedItem = index;
        if (layoutButtonBar.getVisibility() == View.GONE) {
            if (expandChild) {
                layoutButtonBar.setVisibility(View.VISIBLE);
                btEditProj.setVisibility(View.VISIBLE);
                btShareProj.setVisibility(View.VISIBLE);
                btDetailsProj.setVisibility(View.VISIBLE);
                btDeleteProj.setVisibility(View.VISIBLE);
            }
        } else {
            btEditProj.setVisibility(View.GONE);
            btShareProj.setVisibility(View.GONE);
            btDetailsProj.setVisibility(View.GONE);
            btDeleteProj.setVisibility(View.GONE);
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
            prevSelection.setBackgroundColor(getResources().getColor(R.color.background_light));
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

    public void removeProject(int index) {
        FileUtility.deletePrivate(PROJECTS.get(index).title() + FileUtility.FILE_EXT_SERIALIZED_DATA);
        PROJECTS.remove(index);
        TITLES.remove(index);
        notifyDataSetChanged();
    }

    public boolean createProject(MolarWearProject newProject) {
        String fileName = newProject.title() + FileUtility.FILE_EXT_SERIALIZED_DATA;
        if (new File(AppUtility.CONTEXT.getFilesDir().toString() + separator + fileName).exists()) {
            // Failed to create project (one with same name already exists)
            TwoButtonDialog existsDlg = new TwoButtonDialog(new DialogStringData(_activity,
                                        getResources().getString(R.string.err_proj_create_fail),
                                        getResources().getString(R.string.err_proj_create_fail_exists)));
            existsDlg.show();
            return false;
        }
        if (FileUtility.saveSerializable(newProject, fileName)) {
            addProject(newProject, true);
        } else {
            // Failed to create project (unknown reason)
            Snackbar.make(_listView, getResources().getString(R.string.err_proj_create_fail),
                          Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        }
        return true;
    }

    public void deleteProject(final int index) {
        if (index >= 0 && index < PROJECTS.size()) {
            TwoButtonDialog confirmDlg1 = new TwoButtonDialog(new DialogStringData(_activity,
                                          R.string.dlg_title_del_proj_conf,
                                          R.string.dlg_msg_del_proj_conf,
                                          R.string.dlg_bt_yes,
                                          R.string.dlg_bt_no));

            confirmDlg1.setPositiveButton(new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Second confirmation dialog
                    TwoButtonDialog confirmDlg2 = new TwoButtonDialog(new DialogStringData(_activity,
                                                  R.string.dlg_title_del_proj_conf2,
                                                  R.string.dlg_msg_del_proj_conf2,
                                                  R.string.dlg_bt_continue,
                                                  R.string.dlg_bt_cancel));

                    confirmDlg2.setPositiveButton(new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            removeProject(index);
                        }
                    });
                    confirmDlg2.show();
                }
            });
            confirmDlg1.show();
        }
    }

    public void deleteProject(MolarWearProject project) {
        deleteProject(indexOfProject(project));
    }



    //////////// Utility ////////////

    public void openCreateProjectDialog() {
        String baseFileName = AppUtility.CONTEXT.getFilesDir().toString()
                              + separator
                              + getResources().getString(R.string.dlg_hint_new_project);
        File checkExists = new File(baseFileName + FileUtility.FILE_EXT_SERIALIZED_DATA);
        if (checkExists.exists()) {
            int i = 1;
            checkExists = new File(baseFileName + "(" + i + ")" + FileUtility.FILE_EXT_SERIALIZED_DATA);
            while (checkExists.exists()) {
                checkExists = new File(baseFileName + "(" + ++i + ")" + FileUtility.FILE_EXT_SERIALIZED_DATA);
            }
            _dlgCreateProject.setTextInputHint(getResources().getString(R.string.dlg_hint_new_project) + "(" + i + ")");
        }
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
