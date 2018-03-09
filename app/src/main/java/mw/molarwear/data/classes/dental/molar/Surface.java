package mw.molarwear.data.classes.dental.molar;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import mw.molarwear.data.classes.dental.molar.enums.Quadrant;
import mw.molarwear.data.classes.dental.molar.enums.Wear;

/**
 * This class contains data about wear on the Occlusal surface of a human molar (Bottom surface of
 *  upper molars, or top surface of lower molars). Wear data is stored in four {@link SurfaceQuadrant}
 *  objects, each representing the condition of the tooth surface in a different area (inner front,
 *  inner back, outer front, outer back).
 *
 * @author Sean Pesce
 *
 * @see    Molar
 * @see    SurfaceQuadrant
 * @see    Quadrant
 * @see    Wear
 */

public class Surface implements Serializable {


    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    // Tooth surface quadrants
    private SurfaceQuadrant _q1, _q2, _q3, _q4;


    //////////// Constructors ////////////

    public Surface() {
        this.initialize(Wear.UNKNOWN, Wear.UNKNOWN, Wear.UNKNOWN, Wear.UNKNOWN);
    }
    public Surface(Wear q1, Wear q2, Wear q3, Wear q4) { this.initialize(q1, q2, q3, q4); }
    public Surface(int q1Wear, int q2Wear, int q3Wear, int q4Wear) { this.initialize(q1Wear, q2Wear, q3Wear, q4Wear); }


    private void initialize(Wear q1, Wear q2, Wear q3, Wear q4) {
        _q1 = new SurfaceQuadrant(Quadrant.Q1, q1);
        _q2 = new SurfaceQuadrant(Quadrant.Q2, q2);
        _q3 = new SurfaceQuadrant(Quadrant.Q3, q3);
        _q4 = new SurfaceQuadrant(Quadrant.Q4, q4);
    }

    private void initialize(int q1Wear, int q2Wear, int q3Wear, int q4Wear) {
        _q1 = new SurfaceQuadrant(Quadrant.Q1, q1Wear);
        _q2 = new SurfaceQuadrant(Quadrant.Q2, q2Wear);
        _q3 = new SurfaceQuadrant(Quadrant.Q3, q3Wear);
        _q4 = new SurfaceQuadrant(Quadrant.Q4, q4Wear);
    }


    //////////// Accessors ////////////

    public SurfaceQuadrant q1() { return _q1; }
    public SurfaceQuadrant q2() { return _q2; }
    public SurfaceQuadrant q3() { return _q3; }
    public SurfaceQuadrant q4() { return _q4; }
    public List<SurfaceQuadrant> quadrants() {
        return Arrays.asList(_q1, _q2, _q3, _q4);
    };

    public Wear q1Wear() { return _q1.wear(); }
    public Wear q2Wear() { return _q2.wear(); }
    public Wear q3Wear() { return _q3.wear(); }
    public Wear q4Wear() { return _q4.wear(); }
    public List<Wear> quadrantScores() {
        return Arrays.asList(_q1.wear(), _q2.wear(), _q3.wear(), _q4.wear());
    };


    //////////// Mutators ////////////

    public void setQ1(Wear q1) { _q1.setWear(q1); }
    public void setQ2(Wear q2) { _q2.setWear(q2); }
    public void setQ3(Wear q3) { _q3.setWear(q3); }
    public void setQ4(Wear q4) { _q4.setWear(q4); }

    public void set(Wear q1, Wear q2, Wear q3, Wear q4) {
        _q1.setWear(q1);
        _q2.setWear(q2);
        _q3.setWear(q3);
        _q4.setWear(q4);
    }

    public void setQ1(int q1Wear) { _q1.setWear(q1Wear); }
    public void setQ2(int q2Wear) { _q2.setWear(q2Wear); }
    public void setQ3(int q3Wear) { _q3.setWear(q3Wear); }
    public void setQ4(int q4Wear) { _q4.setWear(q4Wear); }

    public void set(int q1Wear, int q2Wear, int q3Wear, int q4Wear) {
        _q1.setWear(q1Wear);
        _q2.setWear(q2Wear);
        _q3.setWear(q3Wear);
        _q4.setWear(q4Wear);
    }

    public static Surface fromCsvData(@NonNull String q1Str, @NonNull String q2Str, @NonNull String q3Str, @NonNull String q4Str) {
        return new Surface(
            q1Str.equals("NA") ? Wear.UNKNOWN.score() : Integer.parseInt(q1Str),
            q2Str.equals("NA") ? Wear.UNKNOWN.score() : Integer.parseInt(q2Str),
            q3Str.equals("NA") ? Wear.UNKNOWN.score() : Integer.parseInt(q3Str),
            q4Str.equals("NA") ? Wear.UNKNOWN.score() : Integer.parseInt(q4Str)
        );
    }
}
