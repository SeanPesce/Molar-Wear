package mw.molarwear.data.handlers;

import android.content.DialogInterface;

import java.io.File;
import java.util.ArrayList;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.TextInputDialog;
import mw.molarwear.gui.dialog.TwoButtonDialog;
import mw.molarwear.gui.fragment.ProjectsListFragment;
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
 * @see    ProjectsListFragment
 */

public class ProjectHandler {

    private static final int NO_SELECTION_INDEX = -1;
    public  static final ArrayList<MolarWearProject> PROJECTS = new ArrayList<>();

    public  static ProjectsListFragment projectsFragment = null;
    private static int _openProject = NO_SELECTION_INDEX; // Index of currently-open project


    //////////// Accessors ////////////

    public static              int openProjectIndex()      { return _openProject;        }
    public static              int projectCount()          { return PROJECTS.size();     }
    public static MolarWearProject get(int index)          { return PROJECTS.get(index); }


    //////////// Mutators ////////////

    public static boolean loadProject(String filePath) {
        MolarWearProject p = (MolarWearProject) FileUtility.readInternalSerializable(filePath);
        if (p == null) {
            AppUtility.printSnackBarMsg("Error loading " + filePath);
            return false;
        } else {
            addProject(p);
        }
        return true;
    }

    public static boolean importProject(String filePath) {
        MolarWearProject p = (MolarWearProject) FileUtility.readExternalSerializable(filePath);
        if (p == null) {
            AppUtility.printSnackBarMsg("Error loading " + filePath);
            return false;
        } else {
            if (new File(FileUtility.getInternalPath(p.title() + FileUtility.FILE_EXT_SERIALIZED_DATA)).exists()) {
                // @TODO: Open dialog to change name of imported project
                AppUtility.printSnackBarMsg(R.string.err_proj_create_fail_exists);
                return false;
            }
            addProject(p);
        }
        return true;
    }

    public static boolean saveProject(int index) {
        return PROJECTS.get(index).save();
//        return FileUtility.saveSerializable(PROJECTS.get(index),
//                                   PROJECTS.get(index).title() + FileUtility.FILE_EXT_SERIALIZED_DATA);
    }

    public static void addProject(MolarWearProject project) {
        PROJECTS.add(project);
        notifyDataSetChanged();
    }

    public static boolean createProject(MolarWearProject newProject) {
        String fileName = newProject.title() + FileUtility.FILE_EXT_SERIALIZED_DATA;
        if (new File(AppUtility.CONTEXT.getFilesDir().toString() + separator + fileName).exists()) {
            // Failed to create project (one with same name already exists)
            TwoButtonDialog existsDlg = new TwoButtonDialog(new DialogStringData(AppUtility.CONTEXT,
                                        R.string.err_proj_create_fail,
                                        R.string.err_proj_create_fail_exists));
            existsDlg.show();
            return false;
        }
        if (FileUtility.saveSerializable(newProject, fileName)) {
            addProject(newProject);
            AppUtility.CONTEXT.runOnUiThread(new Runnable() {
                public void run() {
                    if (projectsFragment != null) {
                        projectsFragment.setSelection(PROJECTS.size() - 1);
                    }
                }
            });
        } else {
            // Failed to create project (unknown reason)
            AppUtility.printSnackBarMsg(AppUtility.getResources().getString(R.string.err_proj_create_fail));
            return false;
        }
        return true;
    }

    public static void removeProject(int index) {
        if (FileUtility.deletePrivate(PROJECTS.get(index).title() + FileUtility.FILE_EXT_SERIALIZED_DATA)) {
            PROJECTS.remove(index);
            notifyDataSetChanged();
        } else {
            AppUtility.printSnackBarMsg("ERROR: Failed to delete project");
        }
    }

    public static void deleteProject(final int index) {
        if (index >= 0 && index < PROJECTS.size()) {
            TwoButtonDialog confirmDlg1
                = new TwoButtonDialog(new DialogStringData(AppUtility.CONTEXT,
                            AppUtility.getResources().getString(R.string.dlg_title_del_proj_conf)
                                + " (\"" + PROJECTS.get(index).title() + "\")",
                             R.string.dlg_msg_del_proj_conf,
                             R.string.dlg_bt_yes,
                             R.string.dlg_bt_no));

            confirmDlg1.setPositiveButton(new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (PROJECTS.get(index).subjectCount() > 0) {
                        // Second confirmation dialog
                        TwoButtonDialog confirmDlg2 = new TwoButtonDialog(new DialogStringData(AppUtility.CONTEXT,
                            R.string.dlg_title_del_proj_conf2,
                            AppUtility.getResources().getString(R.string.dlg_msg_del_proj_conf2)
                                + "\n\n\"" + PROJECTS.get(index).title() + "\"",
                            R.string.dlg_bt_continue,
                            R.string.dlg_bt_cancel));

                        confirmDlg2.setPositiveButton(new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeProject(index);
                            }
                        });
                        confirmDlg2.show();
                    } else {
                        // No need for second confirmation if project is empty
                        removeProject(index);
                    }
                }
            });
            confirmDlg1.show();
        }
    }

    public static void editTitle(final int index) {
        final TextInputDialog dlgEdit = new TextInputDialog(
            new DialogStringData(AppUtility.CONTEXT,
                R.string.dlg_title_edit_project,
                "",
                R.string.dlg_bt_submit,
                R.string.dlg_bt_cancel),
            PROJECTS.get(index).title()
        );
        dlgEdit.setText(PROJECTS.get(index).title());
        dlgEdit.setPositiveButton(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked "Submit" button
                if (dlgEdit.text().length() > 0 && !dlgEdit.text().equals(dlgEdit.textInputHint())) {
                    if (new File(FileUtility.getInternalPath(dlgEdit.text() + FileUtility.FILE_EXT_SERIALIZED_DATA)).exists()) {
                        // Another project with the specified title already exists
                        TwoButtonDialog existsDlg = new TwoButtonDialog(new DialogStringData(AppUtility.CONTEXT,
                            R.string.err_proj_edit_title_fail,
                            R.string.err_proj_edit_title_fail_exists));
                        existsDlg.show();
                    } else {
                        // Rename the project
                        setTitle(index, dlgEdit.text());
                    }
                }
            }
        });
        dlgEdit.show();
    }

    public static void setTitle(int index, String title) {
        String oldTitle = PROJECTS.get(index).title();

        if (!oldTitle.equals(title)) {
            PROJECTS.get(index).setTitle(title);
            File projFile = new File(FileUtility.getInternalPath(oldTitle
                + FileUtility.FILE_EXT_SERIALIZED_DATA));

            String newPath = FileUtility.getInternalPath(PROJECTS.get(index).title()
                + FileUtility.FILE_EXT_SERIALIZED_DATA);
            boolean success = projFile.renameTo(new File(newPath));
            if (success) {
                success = saveProject(index);
            }

            if (!success) {
                // Error when attempting to rename project file
                PROJECTS.get(index).setTitle(oldTitle);
                TwoButtonDialog existsDlg = new TwoButtonDialog(new DialogStringData(AppUtility.CONTEXT,
                    R.string.err_proj_edit_title_fail,
                    R.string.err_proj_edit_title_fail_rename_file));
                existsDlg.show();
            }
            notifyDataSetChanged();
        }
    }



    //////////// Utility ////////////

    public static void newProject() {
        // Initialize project creation dialog
        final TextInputDialog dlg = new TextInputDialog(new DialogStringData(AppUtility.CONTEXT,
                                                                R.string.dlg_title_new_project,
                                                                "",
                                                                R.string.dlg_bt_create,
                                                                R.string.dlg_bt_cancel),
                                                        R.string.dlg_hint_new_project);

        dlg.setText(MolarWearProject.DEFAULT_TITLE);
        dlg.setPositiveButton(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked "Create" button
                boolean created = createProject(new MolarWearProject((!dlg.text().isEmpty()) ? dlg.text() : dlg.textInputHint()));
                if (created) {
                    // @TODO: Open new project?
                }
            }
        });

        // Determine default file name
        String baseFileName = AppUtility.CONTEXT.getFilesDir().toString()
                              + separator
                              + AppUtility.getResources().getString(R.string.dlg_hint_new_project);
        File checkExists = new File(baseFileName + FileUtility.FILE_EXT_SERIALIZED_DATA);
        if (checkExists.exists()) {
            int i = 1;
            checkExists = new File(baseFileName + "(" + i + ")" + FileUtility.FILE_EXT_SERIALIZED_DATA);
            while (checkExists.exists()) {
                checkExists = new File(baseFileName + "(" + ++i + ")" + FileUtility.FILE_EXT_SERIALIZED_DATA);
            }
            dlg.setTextInputHint(AppUtility.getResources().getString(R.string.dlg_hint_new_project) + "(" + i + ")");
            dlg.setText(AppUtility.getResources().getString(R.string.dlg_hint_new_project) + "(" + i + ")");
        }
        dlg.show();
    }

    public static void notifyDataSetChanged() {
        if (projectsFragment != null) {
            projectsFragment.notifyDataSetChanged();
        }
    }


    public static void loadProjects() {
        // Load existing projects from internal storage
        String [] projs = AppUtility.CONTEXT.fileList();
        for (String s : projs) {
            if (s.endsWith(FileUtility.FILE_EXT_SERIALIZED_DATA)) {
                MolarWearProject p = (MolarWearProject) FileUtility.readInternalSerializable(s);
                if (p == null) {
                    if (AppUtility.VIEW != null) {
                        AppUtility.printSnackBarMsg("Error loading " + s);
                    } else {
                        // @TODO: Handle case where AppUtility.VIEW is null
                    }
                } else {
                    PROJECTS.add(p);
                }
            }
        }
    }
}
