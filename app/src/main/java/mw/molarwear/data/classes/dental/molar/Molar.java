package mw.molarwear.data.classes.dental.molar;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import mw.molarwear.data.classes.dental.Tooth;
import mw.molarwear.data.classes.dental.enums.ToothMapping;
import mw.molarwear.data.classes.dental.molar.enums.Wear;

/**
 * This class is used for recording and editing dental data pertaining to human molar wear.
 *
 * @author Sean Pesce
 *
 * @see    Tooth
 * @see    Surface
 * @see    Wear
 */

public class Molar extends Tooth implements Serializable {

    ///////////////////////////////////////////////////////////////
    ///////////////////////// STATIC DATA /////////////////////////
    ///////////////////////////////////////////////////////////////

    public static String ERROR_MSG_INVALID_TYPE = "Invalid type (must be Tooth.Type.MOLAR)";


    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    // Tooth surface consisting of four quadrants
    private Surface _surface;
    private  String _notes = "";


    //////////// Constructors ////////////

    public Molar(int index) {
        super(index);
        this.initialize(new Surface(), "");
    }

    public Molar(ToothMapping tooth) {
        super(tooth);
        this.initialize(new Surface(), "");
    }

    public Molar(int index, @NonNull String notes) {
        super(index);
        this.initialize(new Surface(), notes);
    }

    public Molar(ToothMapping tooth, @NonNull String notes) {
        super(tooth);
        this.initialize(new Surface(), notes);
    }

    public Molar(int index, @NonNull Surface surface) {
        super(index);
        this.initialize(surface, "");
    }

    public Molar(ToothMapping tooth, @NonNull Surface surface) {
        super(tooth);
        this.initialize(surface, "");
    }

    public Molar(int index, @NonNull String notes, @NonNull Surface surface) {
        super(index);
        this.initialize(surface, notes);
    }

    public Molar(ToothMapping tooth, @NonNull String notes, @NonNull Surface surface) {
        super(tooth);
        this.initialize(surface, notes);
    }


    private void initialize(Surface surface, String notes) {
        _surface = surface;
        _notes   = notes;
        if (!this.molar()) {
            throw new IllegalArgumentException(Molar.ERROR_MSG_INVALID_TYPE);
        }
    }


    //////////// Accessors ////////////

    public Surface surface() { return _surface; }
    public  String notes()   { return _notes;   }


    //////////// Mutators ////////////

    public void setSurface(@NonNull Surface surface) {
        _surface = surface;
    }
    public void   setNotes(@NonNull String notes   ) { _notes = notes; }


    //////////// Utility ////////////

    public int dataPoints() {
        return ((_surface.q1().wearScore() != Wear.UNKNOWN.score()) ? 1 : 0)
             + ((_surface.q2().wearScore() != Wear.UNKNOWN.score()) ? 1 : 0)
             + ((_surface.q3().wearScore() != Wear.UNKNOWN.score()) ? 1 : 0)
             + ((_surface.q4().wearScore() != Wear.UNKNOWN.score()) ? 1 : 0);
    }

    public ArrayList<String> toCsvData(int row) {
        ArrayList<String> data = new ArrayList<>();

        data.add((_surface.q1().wearScore() != Wear.UNKNOWN.score()) ? (""+_surface.q1().wearScore()) : "NA");
        data.add((_surface.q2().wearScore() != Wear.UNKNOWN.score()) ? (""+_surface.q2().wearScore()) : "NA");
        data.add((_surface.q3().wearScore() != Wear.UNKNOWN.score()) ? (""+_surface.q3().wearScore()) : "NA");
        data.add((_surface.q4().wearScore() != Wear.UNKNOWN.score()) ? (""+_surface.q4().wearScore()) : "NA");
        final String range;
        if (_tooth.index() == ToothMapping.MOLAR1_L_R.index()) {
            range = "(H" + row + ":K" + row + ")";
        } else if (_tooth.index() == ToothMapping.MOLAR1_L_L.index()) {
            range = "(P" + row + ":S" + row + ")";
        } else if (_tooth.index() == ToothMapping.MOLAR2_L_R.index()) {
            range = "(AA" + row + ":AD" + row + ")";
        } else if (_tooth.index() == ToothMapping.MOLAR2_L_L.index()) {
            range = "(AI" + row + ":AL" + row + ")";
        } else if (_tooth.index() == ToothMapping.MOLAR3_L_R.index()) {
            range = "(AT" + row + ":AW" + row + ")";
        } else { // if (_tooth.index() == ToothMapping.MOLAR3_L_L.index())
            range = "(BB" + row + ":BE" + row + ")";
        }
        if (dataPoints() > 0) {
            data.add("=SUM" + range);
            data.add("=AVERAGE" + range);
            data.add("=STDEV" + range);
        } else {
            data.add("NA");
            data.add("NA");
            data.add("NA");
        }
        data.add(_notes);

        return data;
    }
}
