package mw.molarwear.gui.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.gui.fragment.SubjectsListFragment;
import mw.molarwear.util.AppUtility;

/**
 * Custom {@link ArrayAdapter} class that holds a list of research {@link MolarWearSubject subjects}
 *  for viewing in a {@link android.widget.ListView}.
 *
 * @author Sean Pesce
 *
 * @see ArrayAdapter
 * @see MolarWearSubject
 * @see SubjectsListFragment
 */

public class SubjectArrayAdapter extends ArrayAdapter<MolarWearSubject> {

    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    private final ArrayList<MolarWearSubject> _subjects;
    private SubjectsListFragment _listFragment = null;


    //////////// Constructors ////////////

    public SubjectArrayAdapter(@NonNull ArrayList<MolarWearSubject> subjects) {
        super(AppUtility.CONTEXT, 0, subjects);
        _subjects = subjects;
    }

    public SubjectArrayAdapter(int resource, @NonNull ArrayList<MolarWearSubject> subjects) {
        super(AppUtility.CONTEXT, resource, subjects);
        _subjects = subjects;
    }

    public SubjectArrayAdapter(@NonNull ArrayList<MolarWearSubject> subjects, @NonNull SubjectsListFragment listFragment) {
        super(AppUtility.CONTEXT, 0, subjects);
        _subjects = subjects;
        _listFragment = listFragment;
    }

    public SubjectArrayAdapter(int resource, @NonNull ArrayList<MolarWearSubject> subjects, @NonNull SubjectsListFragment listFragment) {
        super(AppUtility.CONTEXT, resource, subjects);
        _subjects = subjects;
        _listFragment = listFragment;
    }


    //////////// Accessors ////////////

    public int getCount() { return _subjects.size(); }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Reference: https://gist.github.com/nikhilbansal97/f11551b4f6c28c377a363d2cd252b844#file-movieadapter-java
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(AppUtility.CONTEXT).inflate(R.layout.list_item_simple_checkable_1_icon, parent,false);
        }

        final MolarWearSubject subject = _subjects.get(position);

        TextView lblId = (TextView) listItem.findViewById(R.id.lbl_listitem_checkable_1);
        lblId.setText(subject.id());

        ImageButton btOpen = (ImageButton) listItem.findViewById(R.id.bt_listitem_checkable_1);
        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_listFragment != null) {
                    _listFragment.selectItem(position, false);
                    _listFragment.getActivityDerived().openSubjectEditor();
                }
            }
        });

        return listItem;
    }

    public SubjectsListFragment getListFragment() { return _listFragment; }


    //////////// Mutators ////////////

    public void setListFragment(SubjectsListFragment listFragment) { _listFragment = listFragment; }
}
