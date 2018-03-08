package mw.molarwear.gui.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.gui.fragment.SubjectBasicInfoFragment;
import mw.molarwear.gui.fragment.SubjectMolarsViewFragment;
import mw.molarwear.gui.fragment.SubjectNotesFragment;
import mw.molarwear.gui.fragment.SubjectsListFragment;
import mw.molarwear.gui.list.SubjectArrayAdapter;
import mw.molarwear.util.AppUtility;
import mw.molarwear.util.History;

/**
 *
 * @author Sean Pesce
 */

public class ViewProjectActivity extends AppCompatActivity {

    public static final String PROJECT_INDEX_ARG_KEY = "projectIndex";
    public static final String SUBJECT_INDEX_ARG_KEY = "subjectIndex";
    public static final String SUBJECT_MOLAR_ARG_KEY = "molarId";

    private int _projectIndex = AdapterView.INVALID_POSITION;
    private MolarWearProject _project = null;

    private History<Integer> _editorViewHistory = new History<>(2);
    private boolean _saveCurrentEditorView = true;

    // GUI
    private Toolbar  _toolbarSecondary;
    private Drawable _icClose;
    private Drawable _icBack;
    private Drawable _icSave;
    private TextView _lblTitle;
    private TextView _lblSubjTitle;

    private BottomNavigationView _bottomNav;

    private FloatingActionButton _btNewSubject;
    private          ImageButton _btSaveProject;
    private          ImageButton _btEditSubject;
    private          ImageButton _btDeleteSubject;

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

        _viewSwitcher     = findViewById(R.id.view_switcher);
        _subjectsView     = findViewById(R.id.view_list_subjects);
        _editorView       = findViewById(R.id.view_edit_subject);
        _editorBasicInfo  = findViewById(R.id.fragment_edit_subject_basic_info_container);
        _editorMolarData  = findViewById(R.id.fragment_edit_subject_molar_data_container);
        _editorNotes      = findViewById(R.id.fragment_edit_subject_notes_container);

        _toolbarSecondary = findViewById(R.id.toolbar_secondary);
        _lblTitle         = findViewById(R.id.lbl_proj_view_title);
        _lblSubjTitle     = findViewById(R.id.lbl_subj_view_title);
        _btSaveProject    = findViewById(R.id.bt_toolbar_save_proj);
        _btNewSubject     = findViewById(R.id.bt_new_subject);
        _btEditSubject    = findViewById(R.id.bt_toolbar_edit_subj);
        _btDeleteSubject  = findViewById(R.id.bt_toolbar_delete_subj);

        _lblListHeader    = findViewById(R.id.lbl_subjects_list_header);
        _listHeaderDiv    = findViewById(R.id.border_bottom_subjects_list_header);

        _btSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
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
    public void onResume() {
        super.onResume();
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
        // @TODO: Check if project is saved
        if (showingSubjectEditor() && _editorViewHistory.isEmpty()) {
            closeSubjectEditor();
        } else if (showingSubjectEditor()) {
            _saveCurrentEditorView = false;
            _bottomNav.setSelectedItemId(_editorViewHistory.goBack());
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
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

    public ImageButton getBtSave() {
        return _btSaveProject;
    }

    public ImageButton getBtEditSubject() {
        return _btEditSubject;
    }

    public ImageButton getBtDeleteSubject() {
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


    //////////// Mutators ////////////

    @Override
    public void setTitle(CharSequence title) {
        _lblTitle.setText(title);
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        _lblTitle.setText(getResources().getString(titleId));
    }

    public void setSubjectTitle(CharSequence title) {
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
            _editorViewHistory.clear();
            updateEditorView();
            _viewSwitcher.showNext();
            updateNavBar();
            updateToolbar();
        }
    }

    public void closeSubjectEditor() {
        if (_viewSwitcher != null && !showingSubjectsList()) {
            _editorViewHistory.clear();
            _viewSwitcher.showPrevious();
            updateNavBar();
            updateToolbar();
        }
    }

    public boolean onBottomNavigationItemSelected(int itemId) {
        ScrollView scrollView = null;
        boolean saveHistory = _saveCurrentEditorView;
        _saveCurrentEditorView = true;
        switch (itemId) {
            case R.id.navigation_basic_info:
                scrollView = findViewById(R.id.scrollview_subject_basic_info);
                if (scrollView != null) {
                    if (_bottomNav.getSelectedItemId() != R.id.navigation_basic_info) {
                        if (saveHistory) {
                            _editorViewHistory.add(_bottomNav.getSelectedItemId());
                        }
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
                if (scrollView != null) {
                    if (_bottomNav.getSelectedItemId() != R.id.navigation_molar_data) {
                        if (saveHistory) {
                            _editorViewHistory.add(_bottomNav.getSelectedItemId());
                        }
                    } else {
                        scrollView.smoothScrollTo(0, 0);
                    }
                }
                _editorBasicInfo.setVisibility(View.GONE);
                _editorNotes.setVisibility(View.GONE);
                _editorMolarData.setVisibility(View.VISIBLE);
                return true;

            case R.id.navigation_notes:
                scrollView = findViewById(R.id.scrollview_subject_notes);
                if (scrollView != null) {
                    if (_bottomNav.getSelectedItemId() != R.id.navigation_notes) {
                        if (saveHistory) {
                            _editorViewHistory.add(_bottomNav.getSelectedItemId());
                        }
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

    public void updateNavBar() {
        if (showingSubjectsList()) {
            getSupportActionBar().setHomeAsUpIndicator(_icClose);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(_icBack);
        }
    }

    public void updateToolbar() {
        if (showingSubjectsList()) {
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
        // @TODO: Fix color compatibility
        if (Build.VERSION.SDK_INT >= 21) {
            _btSaveProject.getDrawable().setTint(getResources().getColor(
                (_project.isSaved()) ? R.color.colorPrimaryLight2 : R.color.background_light) );
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

        if (infoFragment == null
            || infoFragment.getProjectIndex() != _projectIndex
            || infoFragment.getSubjectIndex() != _listFragment.selectionIndex()) {

            infoFragment = SubjectBasicInfoFragment.newInstance(_projectIndex, _listFragment.selectionIndex());
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_edit_subject_basic_info_container, infoFragment, "frag_edit_subj_info")
                .commit();

            SubjectMolarsViewFragment molarsFragment = SubjectMolarsViewFragment.newInstance(_projectIndex, _listFragment.selectionIndex());
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_edit_subject_molar_data_container, molarsFragment, "frag_edit_subj_molar_data")
                .commit();

            SubjectNotesFragment notesFragment = SubjectNotesFragment.newInstance(_projectIndex, _listFragment.selectionIndex());
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_edit_subject_notes_container, notesFragment, "frag_edit_subj_notes")
                .commit();

            _bottomNav.setSelectedItemId(R.id.navigation_basic_info);
        }
    }
}
