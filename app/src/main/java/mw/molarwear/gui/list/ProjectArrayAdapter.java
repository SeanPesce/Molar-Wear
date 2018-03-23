package mw.molarwear.gui.list;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.gui.activity.ViewProjectActivity;
import mw.molarwear.gui.fragment.ProjectsListFragment;
import mw.molarwear.util.AppUtility;

/**
 * Custom {@link ArrayAdapter} class that holds a list of research {@link MolarWearProject projects}
 *  for displaying in a {@link android.widget.ListView ListView}.
 *
 * @author Sean Pesce
 *
 * @see ArrayAdapter
 * @see MolarWearProject
 * @see ProjectsListFragment
 */

public class ProjectArrayAdapter extends ArrayAdapter<MolarWearProject> {

    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    private final ArrayList<MolarWearProject> _projects;
    private ProjectsListFragment _listFragment = null;


    //////////// Constructors ////////////

    public ProjectArrayAdapter(@NonNull ArrayList<MolarWearProject> projects) {
        super(AppUtility.CONTEXT, 0, projects);
        _projects = projects;
    }

    public ProjectArrayAdapter(int resource, @NonNull ArrayList<MolarWearProject> projects) {
        super(AppUtility.CONTEXT, resource, projects);
        _projects = projects;
    }

    public ProjectArrayAdapter(@NonNull ArrayList<MolarWearProject> projects, @NonNull ProjectsListFragment listFragment) {
        super(AppUtility.CONTEXT, 0, projects);
        _projects = projects;
        _listFragment = listFragment;
    }

    public ProjectArrayAdapter(int resource, @NonNull ArrayList<MolarWearProject> projects, @NonNull ProjectsListFragment listFragment) {
        super(AppUtility.CONTEXT, resource, projects);
        _projects = projects;
        _listFragment = listFragment;
    }


    //////////// Accessors ////////////

    public int getCount() { return _projects.size(); }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Reference: https://gist.github.com/nikhilbansal97/f11551b4f6c28c377a363d2cd252b844#file-movieadapter-java
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(AppUtility.CONTEXT).inflate(R.layout.list_item_simple_checkable_1_txt_large, parent,false);
        }

        final MolarWearProject project = _projects.get(position);

        TextView lblTitle = (TextView) listItem.findViewById(R.id.lbl_listitem_checkable_1);
        lblTitle.setText(project.title());

        AppCompatImageButton btOpen = (AppCompatImageButton) listItem.findViewById(R.id.bt_listitem_checkable_1);
        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_listFragment != null) {
                    _listFragment.selectItem(position, false);
                    Intent i = new Intent(_listFragment.getActivity().getApplicationContext(), ViewProjectActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.putExtra(ViewProjectActivity.PROJECT_INDEX_ARG_KEY, position);
                    _listFragment.getActivity().startActivity(i);
                }
            }
        });

        return listItem;
    }

    public ProjectsListFragment getListFragment() { return _listFragment; }


    //////////// Mutators ////////////

    public void setListFragment(ProjectsListFragment listFragment) { _listFragment = listFragment; }
}
