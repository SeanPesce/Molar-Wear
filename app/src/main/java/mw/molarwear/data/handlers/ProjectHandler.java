package mw.molarwear.data.handlers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mw.molarwear.MolWearApp;
import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.data.classes.ProjectDeserializer;
import mw.molarwear.data.classes.ProjectSerializer;
import mw.molarwear.gui.dialog.BasicDialog;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.MessageDialog;
import mw.molarwear.gui.dialog.RadioGroupDialog;
import mw.molarwear.gui.dialog.TextInputDialog;
import mw.molarwear.gui.fragment.ProjectsListFragment;
import mw.molarwear.util.AnalysisUtil;
import mw.molarwear.util.AppUtil;
import mw.molarwear.util.FileUtil;

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
    private static final ArrayList<String> DEFAULT_SITES  = new ArrayList<>();
    private static final ArrayList<String> DEFAULT_GROUPS = new ArrayList<>();

    private static boolean _INITIALIZED = false;
    public  static ProjectsListFragment projectsFragment = null;


    //////////// Accessors ////////////

    public static          boolean initialized()  { return _INITIALIZED;        }
    public static              int projectCount() { return PROJECTS.size();     }
    public static MolarWearProject get(int index) { return PROJECTS.get(index); }


    public static ArrayList<String> getDefaultGroupIDs() { return DEFAULT_GROUPS; }
    public static int getDefaultGroupIdCount() { return DEFAULT_GROUPS.size(); }

    public static List<String> getGroupIds() {
        return getGroupIds(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE);
    }

    public static List<String> getGroupIds(boolean ignoreCase) {
        return AnalysisUtil.getGroupIds(PROJECTS.toArray(new MolarWearProject[PROJECTS.size()]), ignoreCase, true, false);
    }

    public static ArrayList<String> getDefaultSiteIds() { return DEFAULT_SITES; }
    public static int getDefaultSiteIdCount() { return DEFAULT_SITES.size(); }

    public static List<String> getSiteIds() {
        return getSiteIds(MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE);
    }

    public static List<String> getSiteIds(boolean ignoreCase) {
        return AnalysisUtil.getSiteIds(PROJECTS.toArray(new MolarWearProject[PROJECTS.size()]), ignoreCase, true, false);
    }


    //////////// Mutators ////////////

    public static void sortDefaultSiteIds() { Collections.sort(DEFAULT_SITES, String.CASE_INSENSITIVE_ORDER); }

    public static void addDefaultSiteId(String siteId) {
        if (siteId != null) {
            AnalysisUtil.addIfNotContains(DEFAULT_SITES, siteId, MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE, false);
        }
    }

    public static void removeDefaultSiteId(@NonNull String siteId) {
        DEFAULT_SITES.remove(siteId);
    }

    public static void removeDefaultSiteId(@IntRange(from=0) int index) {
        DEFAULT_SITES.remove(index);
    }

    public static void clearDefaultSiteIds() { DEFAULT_SITES.clear(); }

    public static void sortDefaultGroupIds() { Collections.sort(DEFAULT_GROUPS, String.CASE_INSENSITIVE_ORDER); }

    public static void addDefaultGroupId(String groupId) {
        if (groupId != null) {
            AnalysisUtil.addIfNotContains(DEFAULT_GROUPS, groupId, MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE, false);
        }
    }

    public static void removeDefaultGroupId(@NonNull String groupId) {
        DEFAULT_GROUPS.remove(groupId);
    }

    public static void removeDefaultGroupId(@IntRange(from=0) int index) {
        DEFAULT_GROUPS.remove(index);
    }

    public static void clearDefaultGroupIds() { DEFAULT_GROUPS.clear(); }

    public static void sortDefaultIds() {
        sortDefaultSiteIds();
        sortDefaultGroupIds();
    }

    public static void clearDefaultIds() {
        clearDefaultSiteIds();
        clearDefaultGroupIds();
    }

    public static boolean loadProject(String filePath) {
        MolarWearProject p = FileUtil.readInternalSerializable(filePath);
        if (p == null) {
            AppUtil.printSnackBarMsg("Error loading " + filePath);
            return false;
        } else {
            addProject(p);
        }
        return true;
    }

    public static boolean importProject(String filePath) {
        final MolarWearProject p =
            (filePath.toLowerCase().endsWith(FileUtil.FILE_EXT_CSV)) ? MolarWearProject.fromCsv(filePath) :
                                    (filePath.toLowerCase().endsWith(FileUtil.FILE_EXT_JSON_DATA)) ? importJson(filePath) :
                                    ((MolarWearProject)FileUtil.readExternalSerializable(filePath));
        if (p == null) {
            AppUtil.printSnackBarMsg(AppUtil.getResources().getString(R.string.err_file_read_fail) + ":\n" + filePath);
            return false;
        } else {
            final String internalPath = FileUtil.getInternalPath(p.title() + FileUtil.FILE_EXT_JSON_DATA);
            if (new File(internalPath).exists()) {
                final TextInputDialog dlgEdit = new TextInputDialog(
                    new DialogStringData(AppUtil.CONTEXT,
                        R.string.dlg_title_rename_proj_import,
                        R.string.dlg_msg_rename_proj_import,
                        R.string.dlg_bt_submit,
                        R.string.dlg_bt_cancel),
                    p.title()
                );
                dlgEdit.textInput().setFilters(new InputFilter[] { FileUtil.WHITESPACE_FILTER });
                dlgEdit.setText(p.title());
                dlgEdit.setPosBt(new View.OnClickListener() {
                    public void onClick(View view) {
                        // User clicked "Submit" button
                        if (dlgEdit.text().length() > 0) {
                            if (new File(FileUtil.getInternalPath(dlgEdit.text() + FileUtil.FILE_EXT_JSON_DATA)).exists()) {
                                // Another project with the specified title already exists
                                dlgEdit.show();
                                dlgEdit.textInput().setError(AppUtil.CONTEXT.getString(R.string.err_proj_create_fail_exists));
                            } else {
                                // Rename the project
                                AppUtil.hideKeyboard(AppUtil.CONTEXT, dlgEdit.linearLayout());
                                p.setTitle(dlgEdit.text());
                                addProject(p);
                                if (p.save()) {
                                    AppUtil.printSnackBarMsg(R.string.out_msg_import_success);
                                } else {
                                    AppUtil.printSnackBarMsg(R.string.err_file_write_fail);
                                }
                                AppUtil.printSnackBarMsg(R.string.out_msg_import_success);
                                dlgEdit.dismiss();
                            }
                        } else {
                            dlgEdit.show();
                            dlgEdit.textInput().setError(AppUtil.CONTEXT.getString(R.string.err_proj_create_fail_empty_title));
                        }
                    }
                });
                dlgEdit.setNegBt(new View.OnClickListener() {
                    public void onClick(View view) {
                        // User clicked "Cancel" button
                        AppUtil.hideKeyboard(AppUtil.CONTEXT, dlgEdit.linearLayout());
                        AppUtil.printSnackBarMsg(R.string.out_msg_cancelled);
                    }
                });
                dlgEdit.show();
                return true;
            }
            addProject(p);
            p.setEdited();
            if (p.save()) {
                AppUtil.printSnackBarMsg(R.string.out_msg_import_success);
            } else {
                AppUtil.printSnackBarMsg(R.string.err_file_write_fail);
            }
        }
        return true;
    }

    public static boolean saveProject(int index) {
        return PROJECTS.get(index).save();
    }

    public static void addProject(MolarWearProject project) {
        PROJECTS.add(project);
        notifyDataSetChanged();
    }

    public static boolean createProject(MolarWearProject newProject) {
        String fileName = newProject.title() + FileUtil.FILE_EXT_JSON_DATA;
        if (new File(FileUtil.getInternalPath(fileName)).exists()) {
            // Failed to create project (one with same name already exists)
            return false;
        }
        if (exportJson(newProject, fileName, true)) {
            addProject(newProject);
            projectsFragment.clearSelection();
            projectsFragment.selectItem(PROJECTS.size()-1);
            notifyDataSetChanged();
        } else {
            // Failed to create project (unknown reason)
            AppUtil.printSnackBarMsg(AppUtil.getResources().getString(R.string.err_proj_create_fail));
            return false;
        }
        return true;
    }

    public static void removeProject(int index) {
        if (FileUtil.deletePrivate(PROJECTS.get(index).title() + FileUtil.FILE_EXT_JSON_DATA)) {
            if (projectsFragment.selectionIndex() >= (PROJECTS.size()-1)) {
                projectsFragment.clearSelection();
            }
            PROJECTS.remove(index);
            notifyDataSetChanged();
        } else {
            AppUtil.printSnackBarMsg("ERROR: Failed to delete project");
        }
    }

    public static void deleteProject(final int index) {
        if (index >= 0 && index < PROJECTS.size()) {
            BasicDialog confirmDlg1
                = new BasicDialog(new DialogStringData(AppUtil.CONTEXT,
                        AppUtil.getResources().getString(R.string.dlg_title_del_proj_conf)
                            + " (\"" + PROJECTS.get(index).title() + "\")",
                            R.string.dlg_msg_del_proj_conf,
                            R.string.dlg_bt_yes,
                            R.string.dlg_bt_no));

            confirmDlg1.setPosBt(new View.OnClickListener() {
                public void onClick(View view) {
                    if (PROJECTS.get(index).subjectCount() > 0) {
                        // Second confirmation dialog
                        BasicDialog confirmDlg2 = new BasicDialog(new DialogStringData(AppUtil.CONTEXT,
                            R.string.dlg_title_del_proj_conf2,
                            AppUtil.getResources().getString(R.string.dlg_msg_del_proj_conf2)
                                + "\n\n\"" + PROJECTS.get(index).title() + "\"",
                            R.string.dlg_bt_continue,
                            R.string.dlg_bt_cancel));

                        confirmDlg2.setPosBt(new View.OnClickListener() {
                            public void onClick(View view) {
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
            new DialogStringData(AppUtil.CONTEXT,
                R.string.dlg_title_edit_project,
                "",
                R.string.dlg_bt_submit,
                R.string.dlg_bt_cancel),
            PROJECTS.get(index).title()
        );
        dlgEdit.textInput().setFilters(new InputFilter[] { FileUtil.WHITESPACE_FILTER });
        dlgEdit.setText(PROJECTS.get(index).title());
        dlgEdit.setPosBt(new View.OnClickListener() {
            public void onClick(View view) {
                // User clicked "Submit" button
                //AppUtil.hideKeyboard(AppUtil.CONTEXT, dlgEdit.linearLayout());
                if (dlgEdit.text().length() > 0 && !dlgEdit.text().equals(dlgEdit.textInputHint())) {
                    if (new File(FileUtil.getInternalPath(dlgEdit.text() + FileUtil.FILE_EXT_JSON_DATA)).exists()) {
                        // Another project with the specified title already exists
                        dlgEdit.show();
                        dlgEdit.textInput().setError(AppUtil.CONTEXT.getString(R.string.err_proj_edit_title_fail_exists));
                    } else {
                        // Rename the project
                        AppUtil.hideKeyboard(AppUtil.CONTEXT, dlgEdit.linearLayout());
                        setTitle(index, dlgEdit.text());
                        dlgEdit.dismiss();
                    }
                } else {
                    dlgEdit.dismiss();
                }
            }
        });
        dlgEdit.setNegBt(new View.OnClickListener() {
            public void onClick(View view) {
                AppUtil.hideKeyboard(AppUtil.CONTEXT, dlgEdit.linearLayout());
            }
        });
        dlgEdit.show();
    }

    public static void setTitle(int index, String title) {
        String oldTitle = PROJECTS.get(index).title();

        if (!oldTitle.equals(title)) {
            PROJECTS.get(index).setTitle(title);
            File projFile = new File(FileUtil.getInternalPath(oldTitle
                + FileUtil.FILE_EXT_JSON_DATA));

            String newPath = FileUtil.getInternalPath(PROJECTS.get(index).title()
                + FileUtil.FILE_EXT_JSON_DATA);
            boolean success = projFile.renameTo(new File(newPath));
            if (success) {
                success = saveProject(index);
            }

            if (!success) {
                // Error when attempting to rename project file
                PROJECTS.get(index).setTitle(oldTitle);
                MessageDialog existsDlg = new MessageDialog(new DialogStringData(AppUtil.CONTEXT,
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
        final TextInputDialog dlg = new TextInputDialog(new DialogStringData(AppUtil.CONTEXT,
                                                                R.string.dlg_title_new_project,
                                                                "",
                                                                R.string.dlg_bt_create,
                                                                R.string.dlg_bt_cancel),
                                                        R.string.dlg_hint_new_project);
        dlg.textInput().setFilters(new InputFilter[] { FileUtil.WHITESPACE_FILTER });
        dlg.setText(MolarWearProject.DEFAULT_TITLE);
        dlg.setPosBt(new View.OnClickListener() {
            public void onClick(View view) {
                // User clicked "Create" button
                //AppUtil.hideKeyboard(AppUtil.CONTEXT, dlg.linearLayout());
                String fileName = ((!dlg.text().isEmpty()) ? dlg.text() : dlg.textInputHint()) + FileUtil.FILE_EXT_JSON_DATA;
                if (new File(FileUtil.getInternalPath(fileName)).exists()) {
                    // Failed to create project (one with same name already exists)
                    dlg.show();
                    dlg.textInput().setError(AppUtil.CONTEXT.getString(R.string.err_proj_create_fail_exists));
                } else {
                    AppUtil.hideKeyboard(AppUtil.CONTEXT, dlg.linearLayout());
                    createProject(new MolarWearProject((!dlg.text().isEmpty()) ? dlg.text() : dlg.textInputHint()));
                    dlg.dismiss();
                }
            }
        });
        dlg.setNegBt(new View.OnClickListener() {
            public void onClick(View view) {
                AppUtil.hideKeyboard(AppUtil.CONTEXT, dlg.linearLayout());
            }
        });

        // Determine default file name
        String baseFileName = AppUtil.CONTEXT.getFilesDir().toString()
                              + separator
                              + AppUtil.getResources().getString(R.string.dlg_hint_new_project);
        File checkExists = new File(baseFileName + FileUtil.FILE_EXT_JSON_DATA);
        if (checkExists.exists()) {
            int i = 1;
            checkExists = new File(baseFileName + "(" + i + ")" + FileUtil.FILE_EXT_JSON_DATA);
            while (checkExists.exists()) {
                checkExists = new File(baseFileName + "(" + ++i + ")" + FileUtil.FILE_EXT_JSON_DATA);
            }
            dlg.setTextInputHint(AppUtil.getResources().getString(R.string.dlg_hint_new_project) + "(" + i + ")");
            dlg.setText(AppUtil.getResources().getString(R.string.dlg_hint_new_project) + "(" + i + ")");
        }
        dlg.show();
    }

    public static void notifyDataSetChanged() {
        if (projectsFragment != null) {
            projectsFragment.notifyDataSetChanged();
        }
    }

    public static void openExportDialog(@NonNull final AppCompatActivity activity) {
        final RadioGroupDialog<String> dlgExport = new RadioGroupDialog<>(
            new DialogStringData(activity, R.string.dlg_title_export_csv_raw, "", R.string.act_export, R.string.dlg_bt_cancel),
            FileUtil.PROJECT_FILE_EXTENSIONS, activity.getString(R.string.dlg_msg_export_csv_raw), 0);
        dlgExport.setSize(AppUtil.dpToPixels(250), AppUtil.dpToPixels(250));
        dlgExport.setPosBt(new View.OnClickListener() {
            public void onClick(View view) {
                //FileUtil.createDirectoryChooser(this_, FileUtil.REQUEST_EXPORT_CSV);
                switch (dlgExport.getCheckedRadioButtonIndex()) {
                    case 0:
                        FileUtil.createDirectoryChooser(activity, FileUtil.REQUEST_EXPORT_CSV);
                        break;
                    case 1:
                        FileUtil.createDirectoryChooser(activity, FileUtil.REQUEST_EXPORT_SERIALIZED);
                        break;
                    case 2:
                        FileUtil.createDirectoryChooser(activity, FileUtil.REQUEST_EXPORT_JSON);
                        break;
                    default:
                        break;
                }
            }
        });
        dlgExport.show();
    }

    public static void handleExportRequestResult(final AppCompatActivity activity, int projectIndex, final int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            final List<Uri> files = com.nononsenseapps.filepicker.Utils.getSelectedFilesFromResult(data);
            if (!files.isEmpty()) {
                final MolarWearProject project = ProjectHandler.get(projectIndex);
                final String path = com.nononsenseapps.filepicker.Utils.getFileForUri(files.get(0)).getAbsolutePath();
                final String extension = (requestCode == FileUtil.REQUEST_EXPORT_CSV) ? FileUtil.FILE_EXT_CSV :
                                         (requestCode == FileUtil.REQUEST_EXPORT_SERIALIZED) ? FileUtil.FILE_EXT_SERIALIZED_DATA :
                                         (requestCode == FileUtil.REQUEST_EXPORT_JSON) ? FileUtil.FILE_EXT_JSON_DATA :
                                         FileUtil.FILE_EXT_XML;
                final String filePath = path + File.separator + project.title() + extension;
                if (new File(filePath).exists()) {
                    final BasicDialog dlg
                        = new BasicDialog(new DialogStringData(activity,
                        activity.getString(R.string.dlg_title_overwrite)
                            + " (\"" + project.title() + extension + "\")",
                        R.string.dlg_msg_overwrite,
                        R.string.dlg_bt_overwrite,
                        R.string.dlg_bt_cancel));

                    dlg.setPosBt(new View.OnClickListener() {
                        public void onClick(View view) {
                            boolean success = (requestCode == FileUtil.REQUEST_EXPORT_CSV) ? project.toCsv(filePath) :
                                              (requestCode == FileUtil.REQUEST_EXPORT_SERIALIZED) ? FileUtil.saveSerializableEx(project, filePath) :
                                              ProjectHandler.exportJson(project, filePath);
                            if (success) {
                                AppUtil.printToast(activity, activity.getString(R.string.out_msg_file_saved) + ":\n" + filePath);
                                //new MessageDialog(activity, activity.getString(R.string.out_msg_file_saved) + ":\n" + filePath);
                            } else {
                                AppUtil.printToast(activity, activity.getString(R.string.err_file_write_fail));
                                //new MessageDialog(activity, activity.getString(R.string.err_file_write_fail));
                            }
                        }
                    });
                    dlg.show();
                } else {
                    boolean success = (requestCode == FileUtil.REQUEST_EXPORT_CSV) ? project.toCsv(filePath) :
                                      (requestCode == FileUtil.REQUEST_EXPORT_SERIALIZED) ? FileUtil.saveSerializableEx(project, filePath) :
                                      ProjectHandler.exportJson(project, filePath);
                    if (success) {
                        AppUtil.printToast(activity, activity.getString(R.string.out_msg_file_saved) + ":\n" + filePath);
                        //new MessageDialog(activity, activity.getString(R.string.out_msg_file_saved) + ":\n" + filePath);
                    } else {
                        AppUtil.printToast(activity, activity.getString(R.string.err_file_write_fail));
                        //new MessageDialog(activity, activity.getString(R.string.err_file_write_fail));
                    }
                }
            } else {
                AppUtil.printToast(activity, activity.getString(R.string.out_msg_no_dir_sel));
                //new MessageDialog(activity, activity.getString(R.string.out_msg_no_dir_sel));
            }
            return;
        } else {
            AppUtil.printToast(activity, activity.getString(R.string.out_msg_no_dir_sel));
            //new MessageDialog(activity, activity.getString(R.string.out_msg_no_dir_sel));
            return;
        }
    }

    public static void loadProjects() {
        loadProjects(false);
    }

    public static void loadProjects(boolean deleteBadProjectFiles) {
        // Load existing projects from internal storage
        if (!deleteBadProjectFiles) {
            PROJECTS.clear();
            AppUtil.clearLoadErrors();
        }
        String [] projs = MolWearApp.getContext().fileList();
        for (String s : projs) {
            if (s.endsWith(FileUtil.FILE_EXT_JSON_DATA)) {
                MolarWearProject p = ProjectHandler.importJson(s, true);
                if (p == null) {
                    if (deleteBadProjectFiles) {
                        FileUtil.deletePrivate(s);
                    } else {
                        AppUtil.addLoadError();
                        //AppUtil.printToast(String.format(AppUtil.getResources().getString(R.string.err_proj_load_fail), s));
                        Log.e("ProjectHandler", String.format(AppUtil.getResources().getString(R.string.err_proj_load_fail), s));
                        AppUtil.log(String.format(AppUtil.getResources().getString(R.string.err_proj_load_fail), s));
                    }
                } else if (!deleteBadProjectFiles) {
                    PROJECTS.add(p);
                }
            }
        }
        _INITIALIZED = true;
    }

    @Nullable
    public static MolarWearProject importJson(@NonNull String fileName) {
        return importJson(fileName, false);
    }

    @Nullable
    public static MolarWearProject importJson(@NonNull String fileName, boolean internal) {
        final String jsonData = FileUtil.readText(fileName, internal);
        if (jsonData != null && !jsonData.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(MolarWearProject.class, new ProjectDeserializer());
            mapper.registerModule(module);
            try {
                return mapper.readValue(jsonData, MolarWearProject.class);
            } catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean exportJson(@NonNull MolarWearProject project, @NonNull String fileName) {
        return exportJson(project, fileName, false);
    }

    public static boolean exportJson(@NonNull MolarWearProject project, @NonNull String fileName, boolean internal) {
        try {
            SimpleModule module = new SimpleModule();
            module.addSerializer(new ProjectSerializer(MolarWearProject.class));
            ObjectMapper jsonMapper = new ObjectMapper();
            String jsonResult = jsonMapper.registerModule(module).writer(new DefaultPrettyPrinter()).writeValueAsString(project);
            final File file = new File(fileName);
            if (file.exists()) {
                if (!file.delete()) {
                    return false;
                }
            }
            return FileUtil.writeText(fileName, jsonResult, false, internal);
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
            //AppUtil.printToast(e.toString());
            return false;
        }
    }
}
