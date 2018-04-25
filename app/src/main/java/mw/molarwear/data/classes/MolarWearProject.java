package mw.molarwear.data.classes;

import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import mw.molarwear.R;
import mw.molarwear.data.classes.dental.molar.Surface;
import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.gui.fragment.SubjectsListFragment;
import mw.molarwear.gui.list.SubjectArrayAdapter;
import mw.molarwear.util.AnalysisUtil;
import mw.molarwear.util.AppUtil;
import mw.molarwear.util.FileUtil;

/**
 * This class holds data for a molar wear research project.
 *
 * @author Sean Pesce
 *
 * @see    MolarWearSubject
 * @see    mw.molarwear.data.classes.dental.molar.Molar
 */

public class MolarWearProject implements Comparable<MolarWearProject>, Serializable {

    ///////////////////////////////////////////////////////////////
    ///////////////////////// STATIC DATA /////////////////////////
    ///////////////////////////////////////////////////////////////

    public static String  DEFAULT_TITLE = "Untitled Project";
    public static boolean DEFAULT_GROUP_ID_IGNORE_CASE = true;



    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    protected final ArrayList<MolarWearSubject> _subjects;
    protected       String  _title;
    protected       boolean _saved = true;


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


    protected void initialize(String title) {
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

    public List<String> getGroupIds() {
        return getGroupIds(DEFAULT_GROUP_ID_IGNORE_CASE);
    }

    public List<String> getGroupIds(boolean ignoreCase) {
        return AnalysisUtil.getGroupIds(_subjects.toArray(new MolarWearSubject[_subjects.size()]), ignoreCase, true, false);
    }

    public List<String> getSiteIds() {
        return getSiteIds(DEFAULT_GROUP_ID_IGNORE_CASE);
    }

    public List<String> getSiteIds(boolean ignoreCase) {
        return AnalysisUtil.getSiteIds(_subjects.toArray(new MolarWearSubject[_subjects.size()]), ignoreCase, true, false);
    }

    public ArrayList<MolarWearSubject> getGroup(@NonNull String groupName) {
        return getGroup(groupName, DEFAULT_GROUP_ID_IGNORE_CASE);
    }

    public ArrayList<MolarWearSubject> getGroup(@NonNull String groupName, boolean ignoreCase) {
        ArrayList<MolarWearSubject> group = new ArrayList<>();
        for (MolarWearSubject s : _subjects) {
            if (((!ignoreCase) && s.groupId().equals(groupName)) || (ignoreCase && s.groupId().equalsIgnoreCase(groupName))) {
                group.add(s);
            }
        }
        return group;
    }


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
            boolean success = ProjectHandler.exportJson(this, this.title() + FileUtil.FILE_EXT_JSON_DATA, true);
            if (!success) {
                // @TODO: Handle save error
                Log.w(this.getClass().getName(), AppUtil.getResources().getString(R.string.err_file_write_fail));
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

    public void setTitleNoModify(@NonNull String title) {
        // Set the title without marking the project as edited
        if (title.length() > 0 && !title.equals(_title)) {
            _title = title;
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
    public int compareTo(@NonNull MolarWearProject other) {
        int firstCompRes = _title.compareTo(other.title());
        return (firstCompRes != 0) ? firstCompRes : ((_subjects.size() == other.subjectCount()) ? 0 : (((_subjects.size() > other.subjectCount()) ? -1 : 1)));
    }

    public boolean hasSubjectWithId(@NonNull final String id) {
        for (MolarWearSubject subject : _subjects) {
            if (subject.id().toLowerCase().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public SubjectArrayAdapter createSubjectArrayAdapter() {
        return new SubjectArrayAdapter(_subjects);
    }

    public SubjectArrayAdapter createSubjectArrayAdapter(SubjectsListFragment listFragment) {
        return new SubjectArrayAdapter(_subjects, listFragment);
    }

    public boolean toCsv(@NonNull String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                "UniqueID", "IndividualID", "Sex", "Age", "Location", "Group", "Notes",
                "RM1Q1", "RM1Q2", "RM1Q3", "RM1Q4", "RM1SUM", "RM1AVG", "RM1SD", "RM1_Notes",
                "LM1Q1", "LM1Q2", "LM1Q3", "LM1Q4", "LM1SUM", "LM1AVG", "LM1SD", "LM1_Notes",
                "M1SUM", "M1AVG", "M1SD",
                "RM2Q1", "RM2Q2", "RM2Q3", "RM2Q4", "RM2SUM", "RM2AVG", "RM2SD", "RM2_Notes",
                "LM2Q1", "LM2Q2", "LM2Q3", "LM2Q4", "LM2SUM", "LM2AVG", "LM2SD", "LM2_Notes",
                "M2SUM", "M2AVG", "M2SD",
                "RM3Q1", "RM3Q2", "RM3Q3", "RM3Q4", "RM3SUM", "RM3AVG", "RM3SD", "RM3_Notes",
                "LM3Q1", "LM3Q2", "LM3Q3", "LM3Q4", "LM3SUM", "LM3AVG", "LM3SD", "LM3_Notes",
                "M3SUM", "M3AVG", "M3SD",
                "RM1-3SUM", "RM1-3AVG", "RM1-3SD",
                "LM1-3SUM", "LM1-3AVG", "LM1-3SD",
                "M1-3SUM", "M1-3AVG", "M1-3SD"
                ));

            for (int i = 0; i < _subjects.size(); i++) {
                csvPrinter.printRecord(_subjects.get(i).toCsvRow(i+2));
            }
            csvPrinter.flush();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static MolarWearProject fromCsv(@NonNull String path) {
        // Reference: https://www.callicoder.com/java-read-write-csv-file-apache-commons-csv/
        String name = new File(path).getName();
        if (name.endsWith(FileUtil.FILE_EXT_CSV)) {
            name = name.substring(0, name.lastIndexOf(FileUtil.FILE_EXT_CSV));
        }
        if (name.isEmpty()) {
            name = MolarWearProject.DEFAULT_TITLE;
        }
        MolarWearProject p = new MolarWearProject(name);

        Reader reader;
        CSVParser csvParser;
        List<CSVRecord> records;
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                reader = Files.newBufferedReader(Paths.get(path));
            } else {
                reader = new BufferedReader(new FileReader(path));
            }
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            records = csvParser.getRecords();
        } catch (IOException e) {
            return null;
        }

        try {
            CSVRecord r;
            MolarWearSubject s;
            for (int i = 1; i < records.size(); i++) {
                s = new MolarWearSubject();
                r = records.get(i);
                int j = 1;
                s.setId(r.get(j++));
                if (s.id().isEmpty()) {
                    return null;
                }
                switch (r.get(j++)) {
                    case "M":
                        s.setSex(MolarWearSubject.SEX.MALE);
                        break;
                    case "MU":
                        s.setSex(MolarWearSubject.SEX.MALE_UNCONFIRMED);
                        break;
                    case "F":
                        s.setSex(MolarWearSubject.SEX.FEMALE);
                        break;
                    case "FU":
                        s.setSex(MolarWearSubject.SEX.FEMALE_UNCONFIRMED);
                        break;
                    case "U":
                    default:
                        s.setSex(MolarWearSubject.SEX.UNKNOWN);
                        break;
                }
                s.setAge((r.get(j++).equals("NA")) ? MolarWearSubject.UNKNOWN_AGE : Integer.parseInt(r.get(j - 1)));
                s.setSiteId(r.get(j++));
                s.setGroupId(r.get(j++));
                s.setNotes(r.get(j++));

                // R1
                String[] molarData = new String[]{r.get(j++), r.get(j++), r.get(j++), r.get(j++)};
                j += 3;
                s.setR1(Surface.fromCsvData(molarData[0], molarData[1], molarData[2], molarData[3]));
                s.R1().setNotes(r.get(j++));

                // L1
                molarData[0] = r.get(j++);
                molarData[1] = r.get(j++);
                molarData[2] = r.get(j++);
                molarData[3] = r.get(j++);
                j += 3;
                s.setL1(Surface.fromCsvData(molarData[0], molarData[1], molarData[2], molarData[3]));
                s.L1().setNotes(r.get(j++));
                j += 3;

                // R2
                molarData[0] = r.get(j++);
                molarData[1] = r.get(j++);
                molarData[2] = r.get(j++);
                molarData[3] = r.get(j++);
                j += 3;
                s.setR2(Surface.fromCsvData(molarData[0], molarData[1], molarData[2], molarData[3]));
                s.R2().setNotes(r.get(j++));

                // L2
                molarData[0] = r.get(j++);
                molarData[1] = r.get(j++);
                molarData[2] = r.get(j++);
                molarData[3] = r.get(j++);
                j += 3;
                s.setL2(Surface.fromCsvData(molarData[0], molarData[1], molarData[2], molarData[3]));
                s.L2().setNotes(r.get(j++));
                j += 3;

                // R3
                molarData[0] = r.get(j++);
                molarData[1] = r.get(j++);
                molarData[2] = r.get(j++);
                molarData[3] = r.get(j++);
                j += 3;
                s.setR3(Surface.fromCsvData(molarData[0], molarData[1], molarData[2], molarData[3]));
                s.R3().setNotes(r.get(j++));

                // L3
                molarData[0] = r.get(j++);
                molarData[1] = r.get(j++);
                molarData[2] = r.get(j++);
                molarData[3] = r.get(j++);
                j += 3;
                s.setL3(Surface.fromCsvData(molarData[0], molarData[1], molarData[2], molarData[3]));
                s.L3().setNotes(r.get(j++));
                //j += 3;

                p.addSubject(s);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return p;
    }
}
