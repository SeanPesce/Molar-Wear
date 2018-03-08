package mw.molarwear.data.classes;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import mw.molarwear.gui.fragment.SubjectsListFragment;
import mw.molarwear.gui.list.SubjectArrayAdapter;
import mw.molarwear.util.FileUtility;

/**
 * This class holds data for a molar wear research project.
 *
 * @author Sean Pesce
 *
 * @see    MolarWearSubject
 * @see    mw.molarwear.data.classes.dental.molar.Molar
 */

public class MolarWearProject implements Comparable<MolarWearProject>, Serializable {
  // @TODO: Improve compareTo method and file-related tasks

    ///////////////////////////////////////////////////////////////
    ///////////////////////// STATIC DATA /////////////////////////
    ///////////////////////////////////////////////////////////////

    public static String DEFAULT_TITLE = "Untitled Project";



    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    private final ArrayList<MolarWearSubject> _subjects;
    private       String  _title;
    private       boolean _saved = true;


    //////////// Constructors ////////////

    public MolarWearProject() {
        _subjects = new ArrayList<>();
        initialize(DEFAULT_TITLE);
    }

    public MolarWearProject(@NonNull String title) {
        _subjects = new ArrayList<>();
        initialize(title);
    }

    public MolarWearProject(@NonNull String title, @NonNull MolarWearSubject subject) {
        _subjects = new ArrayList<>();
        _subjects.add(subject);
        initialize(title);
    }

    public MolarWearProject(@NonNull String title, @NonNull ArrayList<MolarWearSubject> subjects) {
        _subjects = subjects;
        initialize(title);
    }


    private void initialize(String title) {
        if (title.length() > 0) {
            _title = title;
        } else {
            _title = DEFAULT_TITLE;
        }
    }


    //////////// Accessors ////////////

    public boolean isSaved() { return _saved; }
    public String title() { return _title; }
    public int subjectCount() { return _subjects.size(); }
    public MolarWearSubject getSubject(@IntRange(from=0) int index) { return _subjects.get(index); }


    //////////// Mutators ////////////

    public boolean save() {
        return setSaved(true);
    }

    public void setEdited() {
        setSaved(false);
    }

    public boolean setSaved(boolean saved) {
        _saved = saved;
        if (_saved) {
            boolean success = FileUtility.saveSerializable(this, _title + FileUtility.FILE_EXT_SERIALIZED_DATA);
            if (!success) {
                // @TODO: Handle save error
            }
            return success;
        }
        return true;
    }

    public void setTitle(@NonNull String title) {
        if (title.length() > 0 && !title.equals(_title)) {
            _title = title;
            _saved = false;
        }
    }

    public void addSubject(@NonNull MolarWearSubject subject) {
        _subjects.add(subject);
        _saved = false;
    }

    public void removeSubject(@NonNull MolarWearSubject subject) {
        if (_subjects.remove(subject)) {
            _saved = false;
        }
    }

    public void removeSubject(@IntRange(from=0) int index) {
        if (index >= 0 && index < _subjects.size()) {
            _subjects.remove(index);
            _saved = false;
        }
    }


    //////////// Utility ////////////

    public String toString() {
        return _title;
    }

    @Override
    public int compareTo(MolarWearProject other) {
        int firstCompRes = _title.compareTo(other.title());
        return (firstCompRes != 0) ? firstCompRes : ((_subjects.size() == other.subjectCount()) ? 0 : (((_subjects.size() > other.subjectCount()) ? -1 : 1)));
    }

    public SubjectArrayAdapter createSubjectArrayAdapter() {
        return new SubjectArrayAdapter(_subjects);
    }

    public SubjectArrayAdapter createSubjectArrayAdapter(SubjectsListFragment listFragment) {
        return new SubjectArrayAdapter(_subjects, listFragment);
    }
}
