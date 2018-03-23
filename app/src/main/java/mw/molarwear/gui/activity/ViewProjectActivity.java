package mw.molarwear.gui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.File;
import java.util.List;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.TwoButtonDialog;
import mw.molarwear.gui.fragment.SubjectBasicInfoFragment;
import mw.molarwear.gui.fragment.SubjectMolarsViewFragment;
import mw.molarwear.gui.fragment.SubjectNotesFragment;
import mw.molarwear.gui.fragment.SubjectsListFragment;
import mw.molarwear.gui.list.SubjectArrayAdapter;
import mw.molarwear.util.AppUtility;
import mw.molarwear.util.FileUtility;

/**
 *
 * @author Sean Pesce
 */

public class ViewProjectActivity extends AppCompatActivity {

    public static final String PROJECT_INDEX_ARG_KEY = "projectIndex";
    public static final String SUBJECT_INDEX_ARG_KEY = "subjectIndex";
    public static final String SUBJECT_MOLAR_ARG_KEY = "molarId";

    public static final int REQUEST_EXPORT_CSV = 102;
    public static final int REQUEST_EXPORT_SERIALIZED = 1995;

    private int _projectIndex = AdapterView.INVALID_POSITION;
    private MolarWearProject _project = null;

    private int _previousProjectIndex = AdapterView.INVALID_POSITION;
    private int _previousSubjectIndex = AdapterView.INVALID_POSITION;

    // GUI
    private Toolbar  _toolbarSecondary;
    private Drawable _icClose;
    private Drawable _icBack;
    private Drawable _icSave;
    private TextView _lblTitle;
    private TextView _lblSubjTitle;

    private BottomNavigationView _bottomNav;

    private FloatingActionButton _btNewSubject;
    private AppCompatImageButton _btNewSubjectTb;
    private AppCompatImageButton _btOptions;
    private AppCompatImageButton _btSaveProject;
    private AppCompatImageButton _btEditSubject;
    private AppCompatImageButton _btEditSubject2;
    private AppCompatImageButton _btDeleteSubject;


    private TextView _lblListHeader;
    private     View _listHeaderDiv;

    private ViewSwitcher _viewSwitcher;
    private         View _subjectsView;
    private         View _editorView;
    private  FrameLayout _editorBasicInfo;
    private  FrameLayout _editorMolarData;
    private  FrameLayout _editorNotes;

    private SubjectsListFragment _listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtility.CONTEXT = this;

        //_previousProjectIndex = AdapterView.INVALID_POSITION;
        //_previousSubjectIndex = AdapterView.INVALID_POSITION;

        setContentView(R.layout.activity_view_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _icClose = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_close)).mutate();
        DrawableCompat.setTint(_icClose, ContextCompat.getColor(this, R.color.background_light));
        _icBack = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_arrow_back)).mutate();
        DrawableCompat.setTint(_icBack, ContextCompat.getColor(this, R.color.background_light));
        _icSave = DrawableCompat.wrap(getResources().getDrawable(R.drawable.ic_save)).mutate();
        DrawableCompat.setTint(_icSave, ContextCompat.getColor(this, R.color.background_light));

        _listFragment = (SubjectsListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_list_subjects);

        _viewSwitcher     = findViewById(R.id.project_view_switcher);
        _subjectsView     = findViewById(R.id.view_list_subjects);
        _editorView       = findViewById(R.id.view_edit_subject);
        _editorBasicInfo  = findViewById(R.id.fragment_edit_subject_basic_info_container);
        _editorMolarData  = findViewById(R.id.fragment_edit_subject_molar_data_container);
        _editorNotes      = findViewById(R.id.fragment_edit_subject_notes_container);

        _toolbarSecondary = findViewById(R.id.toolbar_secondary);
        _lblTitle         = findViewById(R.id.lbl_proj_view_title);
        _lblSubjTitle     = findViewById(R.id.lbl_subj_view_title);
        _btOptions        = findViewById(R.id.bt_proj_view_options);
        _btSaveProject    = findViewById(R.id.bt_toolbar_save_proj);
        _btNewSubjectTb   = findViewById(R.id.bt_toolbar_new_subj);
        _btNewSubject     = findViewById(R.id.bt_new_subject);
        _btEditSubject    = findViewById(R.id.bt_toolbar_edit_subj);
        _btEditSubject2   = findViewById(R.id.bt_toolbar_secondary_edit_subj);
        _btDeleteSubject  = findViewById(R.id.bt_toolbar_delete_subj);

        _lblListHeader    = findViewById(R.id.lbl_subjects_list_header);
        _listHeaderDiv    = findViewById(R.id.border_bottom_subjects_list_header);

        AppUtility.VIEW = _viewSwitcher;

        _btSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        _btOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu optionsMenu = new PopupMenu(AppUtility.CONTEXT, view);
                optionsMenu.setGravity(Gravity.TOP);
                optionsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        onOptionsMenuItemClick(item);
                        return true;
                    }
                });
                MenuInflater inflater = optionsMenu.getMenuInflater();
                inflater.inflate(R.menu.options_project, optionsMenu.getMenu());
                optionsMenu.show();
            }
        });

        if (savedInstanceState == null) {
            _projectIndex = getIntent().getIntExtra(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, _projectIndex);
        } else {
            // App was resumed
            _projectIndex = savedInstanceState.getInt(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, _projectIndex);
            if (_projectIndex == AdapterView.INVALID_POSITION) {
                _projectIndex = getIntent().getIntExtra(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, _projectIndex);
            }
        }
        if (_projectIndex < 0) {
            finish();
            return;
        }
        _project = ProjectHandler.PROJECTS.get(_projectIndex);

        setTitle(_project.title());

        _bottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        _bottomNav.setOnNavigationItemSelectedListener(_onNavigationItemSelectedListener);

        updateNavBar();
        updateToolbar();
        updateSaveButton();
        updateSubjectListHeader();
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((!getProject().isSaved()) && AppUtility.getPrefAutoSave()) {
            save();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtility.CONTEXT = this;
        updateNavBar();
        updateToolbar();
        updateSaveButton();
        updateSubjectListHeader();
        //_bottomNav.setSelectedItemId(R.id.navigation_basic_info);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(PROJECT_INDEX_ARG_KEY, _projectIndex);
        super.onSaveInstanceState(outState);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener _onNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return onBottomNavigationItemSelected(item.getItemId());
        }
    };

    @Override
    public void onBackPressed() {
        if (showingSubjectEditor()) {
            closeSubjectEditor();
        } else {
            if (!getProject().isSaved()) {
                if (AppUtility.getPrefAutoSave()) {
                    save();
                    finish();
                } else {
                    final TwoButtonDialog dlg
                        = new TwoButtonDialog(new DialogStringData(this,
                        R.string.dlg_title_unsaved_changes,
                        R.string.dlg_msg_unsaved_changes,
                        R.string.dlg_bt_save,
                        R.string.dlg_bt_no_save));
                    dlg.setPositiveButton(new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            save();
                            finish();
                        }
                    });
                    dlg.setNegativeButton(new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    dlg.show();
                }
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //////////// Accessors ////////////

    public FloatingActionButton getBtNewSubject() {
        return _btNewSubject;
    }

    public AppCompatImageButton getBtNewSubjectTb() {
        return _btNewSubjectTb;
    }

    public AppCompatImageButton getBtSave() {
        return _btSaveProject;
    }

    public AppCompatImageButton getBtEditSubject() {
        return _btEditSubject;
    }

    public AppCompatImageButton getBtEditSubject2() {
        return _btEditSubject2;
    }

    public AppCompatImageButton getBtDeleteSubject() {
        return _btDeleteSubject;
    }

    public TextView getSubjectListHeader() {
        return _lblListHeader;
    }

    public View getSubjectListHeaderDiv() {
        return _listHeaderDiv;
    }

    public int getProjectIndex() {
        return _projectIndex;
    }

    public MolarWearProject getProject() {
        return _project;
    }

    public boolean showingSubjectsList() {
        return _viewSwitcher.getCurrentView().equals(_subjectsView);
    }

    public boolean showingSubjectEditor() {
        return _viewSwitcher.getCurrentView().equals(_editorView);
    }

    public int previousProjectIndex() { return _previousProjectIndex; }
    public int previousSubjectIndex() { return _previousSubjectIndex; }


    //////////// Mutators ////////////

    @Override
    public void setTitle(CharSequence title) {
        _lblTitle.setText(title);
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        _lblTitle.setText(getResources().getString(titleId));
    }

    public void setSubjectTitle(@NonNull String title) {
        _lblSubjTitle.setText(title);
    }

    public void setSubjectTitle(@StringRes int titleId) {
        _lblSubjTitle.setText(getResources().getString(titleId));
    }

    public void setTitleOriginal(CharSequence title) {
        super.setTitle(title);
    }

    public void setTitleOriginal(@StringRes int titleId) {
        super.setTitle(titleId);
    }

    public void setPreviousIndices(int projectIndex, int subjectIndex) {
        setPreviousProjectIndex(projectIndex);
        setPreviousSubjectIndex(subjectIndex);
    }

    public void setPreviousProjectIndex(int index) {
        if (index == AdapterView.INVALID_POSITION || (index >= 0 && index < ProjectHandler.projectCount())) {
            _previousProjectIndex = index;
        }
    }

    public void setPreviousSubjectIndex(int index) {
        if (index == AdapterView.INVALID_POSITION || (index >= 0 && index < getProject().subjectCount())) {
            _previousSubjectIndex = index;
        }
    }


    //////////// Actions ////////////

    public void notifyDataSetChanged(boolean markProjectEdited) {
        updateSubjectListHeader();
        runOnUiThread(new Runnable() {
            public void run() {
                ((SubjectArrayAdapter)_listFragment.getListAdapter()).notifyDataSetChanged();
            }
        });
        if (markProjectEdited) {
            setEdited();
        } else {
            updateSaveButton();
        }
    }

    public boolean save() {
        return setSaved(true);
    }

    public void setEdited() {
        setSaved(false);
    }

    public boolean setSaved(boolean saved) {
        boolean success = getProject().setSaved(saved);
        updateSaveButton();
        return success;
    }

    public void openSubjectEditor() {
        if (_viewSwitcher != null && !showingSubjectEditor()) {
            AppUtility.hideKeyboard(this, _viewSwitcher);
            updateEditorView();
            _viewSwitcher.showNext();
            updateNavBar();
            updateToolbar();
        }
    }

    public void closeSubjectEditor() {
        if (_viewSwitcher != null && !showingSubjectsList()) {
            AppUtility.hideKeyboard(this, _viewSwitcher);
            _viewSwitcher.showPrevious();
            updateNavBar();
            updateToolbar();
            clearEditorView();
        }
    }

    public boolean onBottomNavigationItemSelected(int itemId) {
        ScrollView scrollView = null;
        switch (itemId) {
            case R.id.navigation_basic_info:
                scrollView = findViewById(R.id.scrollview_subject_basic_info);
                if (scrollView != null) {
                    if (_bottomNav.getSelectedItemId() != R.id.navigation_basic_info) {
                        scrollView.scrollTo(0, 0);
                    } else {
                        scrollView.smoothScrollTo(0, 0);
                    }
                }
                _editorMolarData.setVisibility(View.GONE);
                _editorNotes.setVisibility(View.GONE);
                _editorBasicInfo.setVisibility(View.VISIBLE);
                return true;

            case R.id.navigation_molar_data:
                scrollView = findViewById(R.id.scrollview_subject_molar_data);
                if (scrollView != null && _bottomNav.getSelectedItemId() == R.id.navigation_molar_data) {
                    scrollView.smoothScrollTo(0, 0);
                }
                _editorBasicInfo.setVisibility(View.GONE);
                _editorNotes.setVisibility(View.GONE);
                _editorMolarData.setVisibility(View.VISIBLE);
                return true;

            case R.id.navigation_notes:
                scrollView = findViewById(R.id.scrollview_subject_notes);
                if (scrollView != null) {
                    if (_bottomNav.getSelectedItemId() != R.id.navigation_notes) {
                        scrollView.scrollTo(0, 0);
                    } else {
                        scrollView.smoothScrollTo(0, 0);
                    }
                }
                _editorBasicInfo.setVisibility(View.GONE);
                _editorMolarData.setVisibility(View.GONE);
                _editorNotes.setVisibility(View.VISIBLE);
                return true;
        }
        return false;
    }

    public void onOptionsMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.bt_settings:
                AppUtility.openPreferencesDialog(this);
                break;

            case R.id.bt_export:
                final TwoButtonDialog dlg
                    = new TwoButtonDialog(new DialogStringData(this,
                    R.string.dlg_title_export_csv_raw,
                    R.string.dlg_msg_export_csv_raw,
                    R.string.dlg_bt_csv,
                    R.string.dlg_bt_raw));
                final ViewProjectActivity this_ = this;
                dlg.setPositiveButton(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // CSV
                        FileUtility.createDirectoryChooser(this_, REQUEST_EXPORT_CSV);
                    }
                });
                dlg.setNegativeButton(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Serialized
                        FileUtility.createDirectoryChooser(this_, REQUEST_EXPORT_SERIALIZED);
                    }
                });
                dlg.show();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EXPORT_CSV || requestCode == REQUEST_EXPORT_SERIALIZED) {
            if (resultCode == Activity.RESULT_OK) {
                final List<Uri> files = com.nononsenseapps.filepicker.Utils.getSelectedFilesFromResult(data);
                if (!files.isEmpty()) {
                    final MolarWearProject project = ProjectHandler.get(_projectIndex);
                    final String path = com.nononsenseapps.filepicker.Utils.getFileForUri(files.get(0)).getAbsolutePath();
                    final String extension = (requestCode == REQUEST_EXPORT_CSV) ? FileUtility.FILE_EXT_CSV : FileUtility.FILE_EXT_SERIALIZED_DATA;
                    final String filePath = path + File.separator + project.title() + extension;
                    if (new File(filePath).exists()) {
                        final TwoButtonDialog dlg
                            = new TwoButtonDialog(new DialogStringData(this,
                            getString(R.string.dlg_title_overwrite)
                                + " (\"" + project.title() + extension + "\")",
                            R.string.dlg_msg_overwrite,
                            R.string.dlg_bt_overwrite,
                            R.string.dlg_bt_cancel));
                        dlg.setPositiveButton(new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean success = (requestCode == REQUEST_EXPORT_CSV) ? project.toCsv(filePath) : FileUtility.saveSerializableEx(project, filePath);
                                if (success) {
                                    AppUtility.printSnackBarMsg(getString(R.string.out_msg_file_saved)
                                        + ":\n" + filePath);
                                } else {
                                    AppUtility.printSnackBarMsg(getString(R.string.err_file_write_fail));
                                }
                            }
                        });
                        dlg.show();
                    } else {
                        boolean success = (requestCode == REQUEST_EXPORT_CSV) ? project.toCsv(filePath) : FileUtility.saveSerializableEx(project, filePath);
                        if (success) {
                            AppUtility.printSnackBarMsg(getString(R.string.out_msg_file_saved)
                                + ":\n" + filePath);
                        } else {
                            AppUtility.printSnackBarMsg(getString(R.string.err_file_write_fail));
                        }
                    }
                } else {
                    AppUtility.printSnackBarMsg(getString(R.string.out_msg_no_dir_sel));
                }
                return;
            } else {
                AppUtility.printSnackBarMsg(getString(R.string.out_msg_no_dir_sel));
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case FileUtility.REQUEST_CODE_EXT_STORAGE_PERMISSIONS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Access granted
                } else {
                    AppUtility.printSnackBarMsg(R.string.err_access_denied);
                }
                return;
            }
        }
    }

    public void updateNavBar() {
        if (showingSubjectsList()) {
            getSupportActionBar().setHomeAsUpIndicator(_icClose);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(_icBack);
        }
    }

    public void updateToolbar() {
        if (showingSubjectsList()) {
            _btNewSubjectTb.setVisibility(View.GONE);
            if (_listFragment.selectionIndex() != AdapterView.INVALID_POSITION) {
                _btEditSubject.setVisibility(View.VISIBLE);
                _btDeleteSubject.setVisibility(View.VISIBLE);
            } else {
                _btEditSubject.setVisibility(View.GONE);
                _btDeleteSubject.setVisibility(View.GONE);
            }
            _toolbarSecondary.setVisibility(View.GONE);
            setSubjectTitle("");
        } else {
            _btEditSubject.setVisibility(View.GONE);
            _btDeleteSubject.setVisibility(View.GONE);
            setSubjectTitle(ProjectHandler.PROJECTS.get(_projectIndex).getSubject(_listFragment.selectionIndex()).id());
            _btNewSubjectTb.setVisibility(View.VISIBLE);
            _toolbarSecondary.setVisibility(View.VISIBLE);
        }
    }

    public void updateSaveButton() {
        if (_project.isSaved()) {
            _btSaveProject.setEnabled(false);
            DrawableCompat.setTint(_icSave, ContextCompat.getColor(this, R.color.colorPrimaryLight2));
        } else {
            _btSaveProject.setEnabled(true);
            DrawableCompat.setTint(_icSave, ContextCompat.getColor(this, R.color.background_light));
        }

        if (Build.VERSION.SDK_INT >= 21) {
            _btSaveProject.getDrawable().setTint(getResources().getColor(
                (_project.isSaved()) ? R.color.colorPrimaryLight2 : R.color.background_light) );
        } else {
            _btSaveProject.getDrawable().mutate().setColorFilter(
                ContextCompat.getColor(this, _project.isSaved() ? R.color.colorPrimaryLight2 : R.color.background_light),
                PorterDuff.Mode.SRC_IN);
        }
    }

    public void updateSubjectListHeader() {
        if (_project.subjectCount() == 0) {
            _lblListHeader.setVisibility(View.GONE);
            _listHeaderDiv.setVisibility(View.GONE);
        } else {
            _lblListHeader.setVisibility(View.VISIBLE);
            //_listHeaderDiv.setVisibility(View.VISIBLE);
        }
    }

    public void updateEditorView() {
        SubjectBasicInfoFragment infoFragment =
            (SubjectBasicInfoFragment) getSupportFragmentManager().findFragmentByTag("frag_edit_subj_info");

        boolean resetBottomNav = !(_previousProjectIndex != AdapterView.INVALID_POSITION
                                   && _projectIndex == _previousProjectIndex
                                   && _previousSubjectIndex != AdapterView.INVALID_POSITION
                                   && _listFragment.selectionIndex() == _previousSubjectIndex);

        if (infoFragment == null
            || infoFragment.getProjectIndex() != _projectIndex
            || infoFragment.getSubjectIndex() != _listFragment.selectionIndex()) {

            infoFragment = SubjectBasicInfoFragment.newInstance(_projectIndex, _listFragment.selectionIndex());

            SubjectMolarsViewFragment molarsFragment = SubjectMolarsViewFragment.newInstance(_projectIndex, _listFragment.selectionIndex());

            SubjectNotesFragment notesFragment = SubjectNotesFragment.newInstance(_projectIndex, _listFragment.selectionIndex());

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_edit_subject_basic_info_container, infoFragment, "frag_edit_subj_info")
                .replace(R.id.fragment_edit_subject_molar_data_container, molarsFragment, "frag_edit_subj_molar_data")
                .replace(R.id.fragment_edit_subject_notes_container, notesFragment, "frag_edit_subj_notes").commit();

            if (resetBottomNav) {
                _bottomNav.setSelectedItemId(R.id.navigation_basic_info);
            }
        }
        _editorView.setVisibility(View.VISIBLE);
    }

    public void clearEditorView() {
        SubjectBasicInfoFragment infoFragment =
            (SubjectBasicInfoFragment) getSupportFragmentManager().findFragmentByTag("frag_edit_subj_info");

        if (infoFragment != null) {
            setPreviousIndices(_projectIndex, _listFragment.selectionIndex());

            SubjectMolarsViewFragment molarsFragment =
                    (SubjectMolarsViewFragment) getSupportFragmentManager().findFragmentByTag("frag_edit_subj_molar_data");

            SubjectNotesFragment notesFragment =
                    (SubjectNotesFragment) getSupportFragmentManager().findFragmentByTag("frag_edit_subj_notes");

            getSupportFragmentManager().beginTransaction().remove(infoFragment).remove(molarsFragment).remove(notesFragment).commit();
        } else {
            setPreviousIndices(AdapterView.INVALID_POSITION, AdapterView.INVALID_POSITION);
        }
        _editorView.setVisibility(View.GONE);
    }

    public void hideKeyboardOnEditBasicInfoRadioBtClick(@NonNull View view) {
        // Specified in view_subject_basic_info.xml (used in SubjectBasicInfoFragment)
        AppUtility.hideKeyboard(this, view);
        final LinearLayout linearLayout = (LinearLayout) view.getParent().getParent().getParent();
        linearLayout.requestFocus();
    }
}
