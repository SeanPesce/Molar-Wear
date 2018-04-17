package mw.molarwear.gui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.gui.activity.interfaces.DataCachingActivity;
import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.gui.dialog.BasicDialog;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.SubjectAnalysisDialog;
import mw.molarwear.gui.fragment.SubjectBasicInfoFragment;
import mw.molarwear.gui.fragment.SubjectMolarsViewFragment;
import mw.molarwear.gui.fragment.SubjectNotesFragment;
import mw.molarwear.gui.fragment.SubjectsListFragment;
import mw.molarwear.gui.list.SubjectArrayAdapter;
import mw.molarwear.util.AppUtil;
import mw.molarwear.util.FileUtil;

/**
 *
 * @author Sean Pesce
 */

public class ViewProjectActivity extends DataCachingActivity {

    public static final String PROJECT_INDEX_ARG_KEY = "projectIndex";
    public static final String SUBJECT_INDEX_ARG_KEY = "subjectIndex";
    public static final String SUBJECT_MOLAR_ARG_KEY = "molarId";


    private final ViewProjectActivity _this = this;

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

        setContentView(R.layout.activity_view_project);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            // Failed to update actionbar button
        }
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

        AppUtil.VIEW = _viewSwitcher;

        _btSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        _btOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu optionsMenu = new PopupMenu(_this, _this.findViewById(R.id.bt_proj_view_options_top));
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
                if (showingSubjectsList() && _listFragment.selectionIndex() == AdapterView.INVALID_POSITION) {
                    optionsMenu.getMenu().removeItem(R.id.bt_analyze_subject);
                }
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

        _bottomNav = findViewById(R.id.navigation);
        _bottomNav.setOnNavigationItemSelectedListener(_onNavigationItemSelectedListener);

        updateNavBar();
        updateToolbar();
        updateSaveButton();
        updateSubjectListHeader();
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((!getProject().isSaved()) && AppUtil.getPrefAutoSave()) {
            save();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
                if (AppUtil.getPrefAutoSave()) {
                    save();
                    finish();
                } else {
                    final BasicDialog dlg
                        = new BasicDialog(new DialogStringData(this,
                        R.string.dlg_title_unsaved_changes,
                        R.string.dlg_msg_unsaved_changes,
                        R.string.dlg_bt_save,
                        R.string.dlg_bt_no_save));
                    dlg.setPosBt(new View.OnClickListener() {
                        public void onClick(View view) {
                            save();
                            finish();
                        }
                    });
                    dlg.setNegBt(new View.OnClickListener() {
                        public void onClick(View view) {
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
            AppUtil.hideKeyboard(this, _viewSwitcher);
            updateEditorView();
            _viewSwitcher.showNext();
            updateNavBar();
            updateToolbar();
        }
    }

    public void closeSubjectEditor() {
        if (_viewSwitcher != null && !showingSubjectsList()) {
            AppUtil.hideKeyboard(this, _viewSwitcher);
            _viewSwitcher.showPrevious();
            updateNavBar();
            updateToolbar();
            clearEditorView();
        }
    }

    public boolean onBottomNavigationItemSelected(int itemId) {
        ScrollView scrollView;
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
                AppUtil.openPreferencesDialog(this);
                break;

            case R.id.bt_export:
                ProjectHandler.openExportDialog(this);
                break;

            case R.id.bt_analyze_subject:
                final SubjectAnalysisDialog dlgAnalyzeSubj = new SubjectAnalysisDialog(this, ProjectHandler.get(_projectIndex).getSubject(_listFragment.selectionIndex()));
                dlgAnalyzeSubj.show();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == FileUtil.REQUEST_EXPORT_CSV || requestCode == FileUtil.REQUEST_EXPORT_SERIALIZED || requestCode == FileUtil.REQUEST_EXPORT_JSON || requestCode == FileUtil.REQUEST_EXPORT_XML) {
            ProjectHandler.handleExportRequestResult(this, _projectIndex, requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case FileUtil.REQUEST_CODE_EXT_STORAGE_PERMISSIONS: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // Access denied
                    AppUtil.printSnackBarMsg(R.string.err_access_denied);
                }
                return;
            }
        }
    }

    public void updateNavBar() {
        try {
            if (showingSubjectsList()) {
                getSupportActionBar().setHomeAsUpIndicator(_icClose);
            } else {
                getSupportActionBar().setHomeAsUpIndicator(_icBack);
            }
        } catch (NullPointerException e) {
            // Failed to update actionbar button
            //AppUtil.printSnackBarMsg(R.string.err_update_navbar_fail);
        }
    }

    public void updateToolbar() {
        if (showingSubjectsList()) {
            _btNewSubjectTb.setVisibility(View.GONE);
            if (_listFragment.selectionIndex() != AdapterView.INVALID_POSITION) {
                //_btEditSubject.setVisibility(View.VISIBLE);
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
        AppUtil.hideKeyboard(this, view);
        final LinearLayout linearLayout = (LinearLayout) view.getParent().getParent().getParent();
        linearLayout.requestFocus();
    }
}
