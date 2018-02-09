package mw.molarwear.data.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class holds data for a molar wear research project.
 *
 * @author Sean Pesce
 *
 * @see    MolarWearSubject
 * @see    mw.molarwear.data.classes.dental.molar.Molar
 * @see    mw.molarwear.data.handlers.ProjectsHandler
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
    private       String _title;
    private       String _filePath;


    //////////// Constructors ////////////

    public MolarWearProject() {
        _subjects = new ArrayList<>();
        initialize(DEFAULT_TITLE);
    }

    public MolarWearProject(String title) {
        _subjects = new ArrayList<>();
        initialize(title);
    }

    public MolarWearProject(String title, MolarWearSubject subject) {
        _subjects = new ArrayList<>();
        _subjects.add(subject);
        initialize(title);
    }

    public MolarWearProject(String title, ArrayList<MolarWearSubject> subjects) {
        _subjects = subjects;
        initialize(title);
    }


    private void initialize(String title) {
        this.setTitle(title);
    }


    //////////// Accessors ////////////

    public                      String title()    { return _title;    }
    public ArrayList<MolarWearSubject> subjects() { return _subjects; }


    //////////// Mutators ////////////

    public void setTitle(String title) {
        _title = (title == null || title.length() > 0) ? title : ((_title != null && _title.length() > 0) ? _title : DEFAULT_TITLE);
    }

    public void addSubject(MolarWearSubject subject)    { _subjects.add(subject); }

    public void removeSubject(MolarWearSubject subject) {
        _subjects.remove(subject);
    }

    public void removeSubject(int index) {
        _subjects.remove(index);
    }


    //////////// Utility ////////////

    public String toString() {
        return _title;
    }

    @Override
    public int compareTo(MolarWearProject other) {
        int firstCompRes = _title.compareTo(other.title());
        return (firstCompRes != 0) ? firstCompRes : ((_subjects.size() == other.subjects().size()) ? 0 : (((_subjects.size() > other.subjects().size()) ? -1 : 1)));
    }
}
