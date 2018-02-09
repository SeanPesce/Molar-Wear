package mw.molarwear.gui.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import mw.molarwear.R;
import mw.molarwear.data.handlers.ProjectsHandler;

/**
 * Custom {@link ListView} class for organizing a list of {@link mw.molarwear.data.classes.MolarWearProject}
 *  objects.
 *
 * @author Sean Pesce
 *
 * @see    ListView
 * @see    ProjectsHandler
 * @see    mw.molarwear.data.classes.MolarWearProject
 */

public class ProjectListView extends ListView {

    private ProjectsHandler _projectsHandler    = null;
    private boolean         _expandNextNewChild = false;

    public ProjectListView(Context context) {
        super(context);
    }

    public ProjectListView(Context context, ProjectsHandler projectsHandler) {
        super(context);
        _projectsHandler = projectsHandler;
    }

    public ProjectListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProjectListView(Context context, AttributeSet attrs, ProjectsHandler projectsHandler) {
        super(context, attrs);
        _projectsHandler = projectsHandler;
    }

    public ProjectListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ProjectListView(Context context, AttributeSet attrs, int defStyleAttr, ProjectsHandler projectsHandler) {
        super(context, attrs, defStyleAttr);
        _projectsHandler = projectsHandler;
    }

    // Not supported in older versions of Android
//    public ProjectListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

//    public ProjectListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, ProjectsHandler projectsHandler) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        _projectsHandler = projectsHandler;
//    }


    public void onViewAdded(View child) {
        super.onViewAdded(child);

        final int index = this.getChildCount() - 1; // Index of new child view

        ImageButton    btOpenProj = child.findViewById(R.id.bt_listitem_proj_open);
        ImageButton    btEditProj = child.findViewById(R.id.bt_listitem_proj_edit);
        ImageButton   btShareProj = child.findViewById(R.id.bt_listitem_proj_share);
        ImageButton btDetailsProj = child.findViewById(R.id.bt_listitem_proj_details);
        ImageButton  btDeleteProj = child.findViewById(R.id.bt_listitem_proj_delete);

        btOpenProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _expandNextNewChild = false;
                if (_projectsHandler != null && _projectsHandler.selectionIndex() != index) {
                    _projectsHandler.setSelection(index, false);
                }
                // @TODO: Open project for editing
            }
        });

        // @TODO: Button listeners
//        btEditProj.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//
//            }
//        });
//
//        btShareProj.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//
//            }
//        });
//
//        btDetailsProj.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//
//            }
//        });
//
        btDeleteProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                _projectsHandler.deleteProject(index);
            }
        });

        if (_expandNextNewChild) {
            _expandNextNewChild = false;
            if (_projectsHandler != null && _projectsHandler.selectionIndex() != index) {
                _projectsHandler.setSelection(index, true);
            }
        }
    }

    public void setProjectsHandler(ProjectsHandler projectsHandler) {
        _projectsHandler = projectsHandler;
    }

    public void setExpandNextNewChild(boolean expandNextNewChild) {
        _expandNextNewChild = expandNextNewChild;
    }
}
