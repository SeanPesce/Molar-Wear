package mw.molarwear.data.classes;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import mw.molarwear.data.analysis.AnalysisData;
import mw.molarwear.data.classes.dental.enums.ToothMapping;
import mw.molarwear.data.classes.dental.molar.Molar;
import mw.molarwear.data.classes.dental.molar.Surface;
import mw.molarwear.util.AnalysisUtil;

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

    protected String _id;      // ID of subject
    protected String _siteId;  // ID of site
    protected String _groupId; // ID of group (there can be multiple groups at 1 site)
    protected    int _age;
    protected    SEX _sex;
    protected String _notes;
    protected  Molar _R1, _R2, _R3, _L1, _L2, _L3;


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
    public int compareTo(@NonNull MolarWearSubject other) {
        int firstCompRes = _id.compareTo(other.id());
        return (firstCompRes != 0) ? firstCompRes : _siteId.compareTo(other.siteId());
    }


    //////////// Accessors ////////////

    public String id()      { return _id;      }
    public String siteId()  { return _siteId;  }
    public String groupId() { return _groupId; }
    public    int age()     { return _age;     }
    public    SEX sex()     { return _sex;     }
    public String notes()   { return _notes;   }
    public  Molar R1()      { return _R1;      }
    public  Molar R2()      { return _R2;      }
    public  Molar R3()      { return _R3;      }
    public  Molar L1()      { return _L1;      }
    public  Molar L2()      { return _L2;      }
    public  Molar L3()      { return _L3;      }

    public int dataPoints()   { return dataPointsL() + dataPointsR();                          }
    public int dataPointsL()  { return _L1.dataPoints() + _L2.dataPoints() + _L3.dataPoints(); }
    public int dataPointsR()  { return _R1.dataPoints() + _R2.dataPoints() + _R3.dataPoints(); }
    public int dataPointsM1() { return _L1.dataPoints() + _R1.dataPoints();                    }
    public int dataPointsM2() { return _L2.dataPoints() + _R2.dataPoints();                    }
    public int dataPointsM3() { return _L3.dataPoints() + _R3.dataPoints();                    }


    public Character preferredSideForAnalysis() {
        int L = dataPointsL(),
            R = dataPointsR();
        return (L==0 && R==0) ? null : ((R >= L) ? 'R' : 'L');
    }

    public Integer[] wearScoreData() {
        Integer[] w = _L1.wearScoreData();
        w = AnalysisUtil.concatenate(w, _L2.wearScoreData());
        w = AnalysisUtil.concatenate(w, _L3.wearScoreData());
        w = AnalysisUtil.concatenate(w, _R1.wearScoreData());
        w = AnalysisUtil.concatenate(w, _R2.wearScoreData());
        w = AnalysisUtil.concatenate(w, _R3.wearScoreData());
        return w;
    }

    public Integer[] wearScoreDataL() {
        Integer[] w = _L1.wearScoreData();
        w = AnalysisUtil.concatenate(w, _L2.wearScoreData());
        w = AnalysisUtil.concatenate(w, _L3.wearScoreData());
        return w;
    }

    public Integer[] wearScoreDataR() {
        Integer[] w = _R1.wearScoreData();
        w = AnalysisUtil.concatenate(w, _R2.wearScoreData());
        w = AnalysisUtil.concatenate(w, _R3.wearScoreData());
        return w;
    }

    public Integer[] wearScoreDataM1() {
        Integer[] w = _L1.wearScoreData();
        w = AnalysisUtil.concatenate(w, _R1.wearScoreData());
        return w;
    }

    public Integer[] wearScoreDataM2() {
        Integer[] w = _L2.wearScoreData();
        w = AnalysisUtil.concatenate(w, _R2.wearScoreData());
        return w;
    }

    public Integer[] wearScoreDataM3() {
        Integer[] w = _L3.wearScoreData();
        w = AnalysisUtil.concatenate(w, _R3.wearScoreData());
        return w;
    }

    @SuppressWarnings("UnnecessaryUnboxing")
    public Integer[] wearScoreDataPreferred() {
        Character pref = preferredSideForAnalysis();
        return (pref == null) ? null : ((pref.charValue() == 'L') ? wearScoreDataL() : wearScoreDataR());
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


    //////////// Analysis ////////////

    public Integer wearScoreSum() {
        return (dataPoints() == 0) ? null : AnalysisUtil.sum(wearScoreData());
    }

    public Integer wearScoreSumL() {
        return (dataPointsL() == 0) ? null : AnalysisUtil.sum(wearScoreDataL());
    }

    public Integer wearScoreSumR() {
        return (dataPointsR() == 0) ? null : AnalysisUtil.sum(wearScoreDataR());
    }

    public Integer wearScoreSumM1() {
        return (dataPointsM1() == 0) ? null : AnalysisUtil.sum(wearScoreDataM1());
    }

    public Integer wearScoreSumM2() {
        return (dataPointsM2() == 0) ? null : AnalysisUtil.sum(wearScoreDataM2());
    }

    public Integer wearScoreSumM3() {
        return (dataPointsM3() == 0) ? null : AnalysisUtil.sum(wearScoreDataM3());
    }

    @SuppressWarnings("UnnecessaryUnboxing")
    public Integer wearScoreSumPreferred() {
        Character pref = preferredSideForAnalysis();
        return (pref == null) ? null : ((pref.charValue() == 'L') ? wearScoreSumL() : wearScoreSumR());
    }

    public Double wearScoreMean() {
        return (dataPoints() == 0) ? null : AnalysisUtil.mean(wearScoreData());
    }

    public Double wearScoreMeanL() {
        return (dataPointsL() == 0) ? null : AnalysisUtil.mean(wearScoreDataL());
    }

    public Double wearScoreMeanR() {
        return (dataPointsR() == 0) ? null : AnalysisUtil.mean(wearScoreDataR());
    }

    public Double wearScoreMeanM1() {
        return (dataPointsM1() == 0) ? null : AnalysisUtil.mean(wearScoreDataM1());
    }

    public Double wearScoreMeanM2() {
        return (dataPointsM2() == 0) ? null : AnalysisUtil.mean(wearScoreDataM2());
    }

    public Double wearScoreMeanM3() {
        return (dataPointsM3() == 0) ? null : AnalysisUtil.mean(wearScoreDataM3());
    }

    public Double wearScoreMeanPreferred() {
        return (dataPoints() == 0) ? null : AnalysisUtil.mean(wearScoreDataPreferred());
    }

    public Double wearScoreStandardDeviation() {
        return (dataPoints() == 0) ? null : AnalysisUtil.standardDeviation(wearScoreData());
    }

    public Double wearScoreStandardDeviationL() {
        return (dataPointsL() == 0) ? null : AnalysisUtil.standardDeviation(wearScoreDataL());
    }

    public Double wearScoreStandardDeviationR() {
        return (dataPointsR() == 0) ? null : AnalysisUtil.standardDeviation(wearScoreDataR());
    }

    public Double wearScoreStandardDeviationM1() {
        return (dataPointsM1() == 0) ? null : AnalysisUtil.standardDeviation(wearScoreDataM1());
    }

    public Double wearScoreStandardDeviationM2() {
        return (dataPointsM2() == 0) ? null : AnalysisUtil.standardDeviation(wearScoreDataM2());
    }

    public Double wearScoreStandardDeviationM3() {
        return (dataPointsM3() == 0) ? null : AnalysisUtil.standardDeviation(wearScoreDataM3());
    }

    public Double wearScoreStandardDeviationPreferred() {
        return (dataPoints() == 0) ? null : AnalysisUtil.standardDeviation(wearScoreDataPreferred());
    }

    public Double[] wearScoreMeanAndSd() {
        return (dataPoints() == 0) ? null : AnalysisUtil.meanAndStandardDeviation(wearScoreData());
    }

    public Double[] wearScoreMeanAndSdL() {
        return (dataPointsL() == 0) ? null : AnalysisUtil.meanAndStandardDeviation(wearScoreDataL());
    }

    public Double[] wearScoreMeanAndSdR() {
        return (dataPointsR() == 0) ? null : AnalysisUtil.meanAndStandardDeviation(wearScoreDataR());
    }

    public Double[] wearScoreMeanAndSdM1() {
        return (dataPointsM1() == 0) ? null : AnalysisUtil.meanAndStandardDeviation(wearScoreDataM1());
    }

    public Double[] wearScoreMeanAndSdM2() {
        return (dataPointsM2() == 0) ? null : AnalysisUtil.meanAndStandardDeviation(wearScoreDataM2());
    }

    public Double[] wearScoreMeanAndSdM3() {
        return (dataPointsM3() == 0) ? null : AnalysisUtil.meanAndStandardDeviation(wearScoreDataM3());
    }

    public Double[] wearScoreMeanAndSdPreferred() {
        return (dataPoints() == 0) ? null : AnalysisUtil.meanAndStandardDeviation(wearScoreDataPreferred());
    }

    public AnalysisData<Integer> analyzeWear() {
        return (dataPoints() == 0) ? null : AnalysisUtil.analyze(wearScoreData());
    }

    public AnalysisData<Integer> analyzeWearL() {
        return (dataPointsL() == 0) ? null : AnalysisUtil.analyze(wearScoreDataL());
    }

    public AnalysisData<Integer> analyzeWearR() {
        return (dataPointsR() == 0) ? null : AnalysisUtil.analyze(wearScoreDataR());
    }

    public AnalysisData<Integer> analyzeWearM1() {
        return (dataPointsM1() == 0) ? null : AnalysisUtil.analyze(wearScoreDataM1());
    }

    public AnalysisData<Integer> analyzeWearM2() {
        return (dataPointsM2() == 0) ? null : AnalysisUtil.analyze(wearScoreDataM2());
    }

    public AnalysisData<Integer> analyzeWearM3() {
        return (dataPointsM3() == 0) ? null : AnalysisUtil.analyze(wearScoreDataM3());
    }

    @SuppressWarnings("UnnecessaryUnboxing")
    public AnalysisData<Integer> analyzeWearPreferred() {
        Character pref = preferredSideForAnalysis();
        return (pref == null) ? null : ((pref.charValue() == 'L') ? analyzeWearL() : analyzeWearR());
    }

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
