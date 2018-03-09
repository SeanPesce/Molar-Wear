package mw.molarwear.data.classes;

import java.io.Serializable;
import java.util.ArrayList;

import mw.molarwear.data.classes.dental.enums.ToothMapping;
import mw.molarwear.data.classes.dental.molar.Molar;
import mw.molarwear.data.classes.dental.molar.Surface;

/**
 * This class holds molar wear data for a single human subject (generally a deceased individual).
 *
 * <p>
 * <b>Note:</b> Data is collected exclusively about lower molars for each study subject;
 *  upper molar data is not recorded.
 * </p>
 *
 * @author Sean Pesce
 *
 * @see    Molar
 * @see    Surface
 * @see    mw.molarwear.data.classes.dental.molar.enums.Wear
 */

public class MolarWearSubject implements Comparable<MolarWearSubject>, Serializable {

    ///////////////////////////////////////////////////////////////
    ///////////////////////// STATIC DATA /////////////////////////
    ///////////////////////////////////////////////////////////////

    public enum SEX {
        MALE,
        MALE_UNCONFIRMED,
        FEMALE,
        FEMALE_UNCONFIRMED,
        UNKNOWN
    }

    public static final int    UNKNOWN_AGE = -1;
    public static final int        MAX_AGE = 150;
    public static       String  DEFAULT_ID = "Unknown Subject";


    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    private String _id;      // ID of subject
    private String _siteId;  // ID of site
    private String _groupId; // ID of group (there can be multiple groups at 1 site)
    private    int _age;
    private    SEX _sex;
    private String _notes;
    private  Molar _R1, _R2, _R3, _L1, _L2, _L3;


    //////////// Constructors ////////////

    public MolarWearSubject() {
        this.initialize(DEFAULT_ID, "","", UNKNOWN_AGE, SEX.UNKNOWN, "",
                        new Surface(), new Surface(), new Surface(),
                        new Surface(), new Surface(), new Surface()    );
    }

    public MolarWearSubject(String id) {
        this.initialize(id,"", "", UNKNOWN_AGE, SEX.UNKNOWN, "",
            new Surface(), new Surface(), new Surface(),
            new Surface(), new Surface(), new Surface()    );
    }

    public MolarWearSubject(String id, String siteId, String groupId, int age, SEX sex, String notes) {
        this.initialize(id, siteId, groupId, age, sex, notes,
                        new Surface(), new Surface(), new Surface(),
                        new Surface(), new Surface(), new Surface() );
    }

    public MolarWearSubject(String id, String siteId, String groupId, int age, SEX sex, String notes,
                            Surface R1, Surface R2, Surface R3, Surface L1, Surface L2, Surface L3) {
        this.initialize(id, siteId, groupId, age, sex, notes, R1, R2, R3, L1, L2, L3);
    }


    private void initialize(String id, String siteId, String groupId, int age, SEX sex, String notes,
                            Surface R1, Surface R2, Surface R3, Surface L1, Surface L2, Surface L3) {
        _id     = id;
        _siteId = siteId;
        _groupId = groupId;
        _age    = age;
        _sex    = sex;
        _notes  = notes;
        _R1     = new Molar(ToothMapping.MOLAR1_L_R, R1);
        _R2     = new Molar(ToothMapping.MOLAR2_L_R, R2);
        _R3     = new Molar(ToothMapping.MOLAR3_L_R, R3);
        _L1     = new Molar(ToothMapping.MOLAR1_L_L, L1);
        _L2     = new Molar(ToothMapping.MOLAR2_L_L, L2);
        _L3     = new Molar(ToothMapping.MOLAR3_L_L, L3);
    }

    @Override
    public int compareTo(MolarWearSubject other) {
        int firstCompRes = _id.compareTo(other.id());
        return (firstCompRes != 0) ? firstCompRes : _siteId.compareTo(other.siteId());
    }


    //////////// Accessors ////////////

    public String id()      { return _id;     }
    public String siteId()  { return _siteId; }
    public String groupId() { return _groupId; }
    public    int age()     { return _age;    }
    public    SEX sex()     { return _sex;    }
    public String notes()   { return _notes;  }
    public  Molar R1()      { return _R1;     }
    public  Molar R2()      { return _R2;     }
    public  Molar R3()      { return _R3;     }
    public  Molar L1()      { return _L1;     }
    public  Molar L2()      { return _L2;     }
    public  Molar L3()      { return _L3;     }

    public int dataPointsL() {
        return _L1.dataPoints() + _L2.dataPoints() + _L3.dataPoints();
    }

    public int dataPointsR() {
        return _R1.dataPoints() + _R2.dataPoints() + _R3.dataPoints();
    }


    //////////// Mutators ////////////

    public void setId(String id)           { _id = id;                              }
    public void setSiteId(String siteId)   { _siteId = siteId;                      }
    public void setGroupId(String groupId) { _groupId = groupId;                    }
    public void setAge(int age)            { _age = (age >= 0) ? age : UNKNOWN_AGE; }
    public void setSex(SEX sex)            { _sex = sex;                            }
    public void setNotes(String notes)     { _notes = notes;                        }
    public void setR1(Surface R1)          { _R1.setSurface(R1);                    }
    public void setR2(Surface R2)          { _R2.setSurface(R2);                    }
    public void setR3(Surface R3)          { _R3.setSurface(R3);                    }
    public void setL1(Surface L1)          { _L1.setSurface(L1);                    }
    public void setL2(Surface L2)          { _L2.setSurface(L2);                    }
    public void setL3(Surface L3)          { _L3.setSurface(L3);                    }


    //////////// Utility ////////////

    public ArrayList<String> toCsvRow(int row) {
        ArrayList<String> data = new ArrayList<>();
        data.add(_siteId + "_" + _id);
        data.add(_id);
        switch(_sex) {
            case MALE:
                data.add("M");
                break;
            case MALE_UNCONFIRMED:
                data.add("MU");
                break;
            case FEMALE:
                data.add("F");
                break;
            case FEMALE_UNCONFIRMED:
                data.add("FU");
                break;
            case UNKNOWN:
            default:
                data.add("U");
                break;
        }
        data.add((_age == UNKNOWN_AGE) ? "NA" : ("" + _age));
        data.add(_siteId);
        data.add(_groupId);
        data.add(_notes);

        int dpR1 = _R1.dataPoints(),
            dpR2 = _R2.dataPoints(),
            dpR3 = _R3.dataPoints(),
            dpL1 = _L1.dataPoints(),
            dpL2 = _L2.dataPoints(),
            dpL3 = _L3.dataPoints();

        data.addAll(_R1.toCsvData(row)); // RM1
        data.addAll(_L1.toCsvData(row)); // LM1

        // M1
        String range = "(H" + row + ":K" + row + ",P" + row + ":S" + row + ")";
        if (dpR1 > 0 || dpL1 > 0) {
            data.add("=SUM" + range);
            data.add("=AVERAGE" + range);
            data.add("=STDEV" + range);
        } else {
            data.add("NA");
            data.add("NA");
            data.add("NA");
        }

        data.addAll(_R2.toCsvData(row)); // RM2
        data.addAll(_L2.toCsvData(row)); // LM2

        // M2
        range = "(AA" + row + ":AD" + row + ",AI" + row + ":AL" + row + ")";
        if (dpR2 > 0 || dpL2 > 0) {
            data.add("=SUM" + range);
            data.add("=AVERAGE" + range);
            data.add("=STDEV" + range);
        } else {
            data.add("NA");
            data.add("NA");
            data.add("NA");
        }

        data.addAll(_R3.toCsvData(row)); // RM3
        data.addAll(_L3.toCsvData(row)); // LM3

        // M3
        range = "(AT" + row + ":AW" + row + ",BB" + row + ":BE" + row + ")";
        if (dpR3 > 0 || dpL3 > 0) {
            data.add("=SUM" + range);
            data.add("=AVERAGE" + range);
            data.add("=STDEV" + range);
        } else {
            data.add("NA");
            data.add("NA");
            data.add("NA");
        }

        // RM1-3
        range = "(H" + row + ":K" + row + ",AA" + row + ":AD" + row + ",AT" + row + ":AW" + row + ")";
        if (dpR1 > 0 || dpR2 > 0 || dpR3 > 0) {
            data.add("=SUM" + range);
            data.add("=AVERAGE" + range);
            data.add("=STDEV" + range);
        } else {
            data.add("NA");
            data.add("NA");
            data.add("NA");
        }

        // LM1-3
        range = "(P" + row + ":S" + row + ",AI" + row + ":AL" + row + ",BB" + row + ":BE" + row + ")";
        if (dpL1 > 0 || dpL2 > 0 || dpL3 > 0) {
            data.add("=SUM" + range);
            data.add("=AVERAGE" + range);
            data.add("=STDEV" + range);
        } else {
            data.add("NA");
            data.add("NA");
            data.add("NA");
        }

        // M1-3 (All)
        range = "(H" + row + ":K" + row + ",P" + row + ":S" + row
                + ",AA" + row + ":AD" + row + ",AI" + row + ":AL" + row
                + ",AT" + row + ":AW" + row + ",BB" + row + ":BE" + row + ")";
        if (dpR1 > 0 || dpR2 > 0 || dpR3 > 0 || dpL1 > 0 || dpL2 > 0 || dpL3 > 0) {
            data.add("=SUM" + range);
            data.add("=AVERAGE" + range);
            data.add("=STDEV" + range);
        } else {
            data.add("NA");
            data.add("NA");
            data.add("NA");
        }

        return data;
    }
}
