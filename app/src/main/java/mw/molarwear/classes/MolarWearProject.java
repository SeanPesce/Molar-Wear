package mw.molarwear.classes;

import java.util.ArrayList;

/**
 * This class holds data for a molar wear research project.
 *
 * @author Sean Pesce
 * @see    MolarWearSubject
 * @see    mw.molarwear.classes.dental.molar.Molar
 */

public class MolarWearProject implements Comparable<MolarWearProject> {
  // @TODO: Finish/fix global handling of projects list
  // @TODO: Improve compareTo method

    ///////////////////////////////////////////////////////////////
    ///////////////////////// STATIC DATA /////////////////////////
    ///////////////////////////////////////////////////////////////

    private static       ArrayList<MolarWearProject> PROJECTS = new ArrayList<>();
    public  static final String DEFAULT_TITLE = "Untitled Project";



    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    private ArrayList<MolarWearSubject> _subjects;
    private String _title;


    //////////// Constructors ////////////

    public MolarWearProject() {
        _subjects = new ArrayList<>();
        this.setTitle(DEFAULT_TITLE);
    }

    public MolarWearProject(String title) {
        _subjects = new ArrayList<>();
        this.setTitle(title);
    }

    public MolarWearProject(String title, ArrayList<MolarWearSubject> subjects) {
        _subjects = subjects;
        this.setTitle(title);
    }


    //////////// Accessors ////////////

    public                      String title()    { return _title;    }
    public ArrayList<MolarWearSubject> subjects() { return _subjects; }


    //////////// Mutators ////////////

    public void setTitle(String title) {
        title = (title.length() > 0) ? title : DEFAULT_TITLE;
        for (int i = 1; ; i++) {
            if (!projectExists(title + " (" + i + ")") || i <= 0) {
                title += " (" + i + ")";
                break;
            }
        }
        _title = title;
    }

    public void setSubjects(ArrayList<MolarWearSubject> subjects) {
        _subjects = subjects;
    }

    public void addSubject(MolarWearSubject subject) {
        _subjects.add(subject);
    }

    public void removeSubject(MolarWearSubject subject) {
        _subjects.remove(subject);
    }

    public void removeSubject(int index) {
        _subjects.remove(index);
    }


    //////////// Utility ////////////

    @Override
    public int compareTo(MolarWearProject other) {
        int firstCompRes = _title.compareTo(other.title());
        return (firstCompRes != 0) ? firstCompRes : ((_subjects.size() == other.subjects().size()) ? 0 : (((_subjects.size() > other.subjects().size()) ? -1 : 1)));
    }

    /**
     * Searches the list of existing {@link MolarWearProject} to see if a project with the given
     *  title already exists.
     *
     * @param  title the project title to search for.
     * @return true if a project with the given name exists, and false otherwise.
     */
    public boolean projectExists(String title) {
        for(MolarWearProject p : PROJECTS) {
            if (title.equals(p.title())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches the list of existing {@link MolarWearProject} for an existing project with the given
     *  title.
     *
     * @param  title the project title to search for.
     * @return the index of the first project with the given name, or -1 if none was found.
     */
    public int indexOfProject(String title) {
        for(int i = 0; i < PROJECTS.size(); i++) {
            if (title.equals(PROJECTS.get(i).title())) {
                return i;
            }
        }
        return -1;
    }
}
