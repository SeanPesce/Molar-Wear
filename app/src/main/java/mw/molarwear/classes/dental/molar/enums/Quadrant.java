package mw.molarwear.classes.dental.molar.enums;

/**
 * This enum represents the four quadrants that make up the surface of a human molar
 * (Bottom surface of upper molars, or top surface of lower molars).
 *
 * <ul>
 *     <li>{@link #Q1}</li>
 *     <li>{@link #Q2}</li>
 *     <li>{@link #Q3}</li>
 *     <li>{@link #Q4}</li>
 * </ul>
 *
 * @author Sean Pesce
 * @see    mw.molarwear.classes.dental.molar.SurfaceQuadrant
 * @see    mw.molarwear.classes.dental.molar.Surface
 * @see    mw.molarwear.classes.dental.molar.Molar
 */

public enum Quadrant {

    ///////////////////////////////////////////////////////////////
    ///////////////////////// STATIC DATA /////////////////////////
    ///////////////////////////////////////////////////////////////

    /**
     * Quadrant 1 (Front, inner)
     */
    Q1 (1, true,  true  ), // Front, inner
    /**
     * Quadrant 2 (Front, outer)
     */
    Q2 (2, true,  false ), // Front, outer
    /**
     * Quadrant 3 (Back, inner)
     */
    Q3 (3, false, true  ), // Back,  inner
    /**
     * Quadrant 4 (Back, outer)
     */
    Q4 (4, false, false ); // Back,  outer


    public static Quadrant get(int index) { return Quadrant.values()[index - 1]; } // Indices start at 1


    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    private final     int _index; // 1-4
    private final boolean _front; // Mesial (SurfaceQuadrant is closer to front of mouth); false = distal
    private final boolean _inner; // Lingual (SurfaceQuadrant is on outer side of molar; farther from tongue); false = buccal

    Quadrant(int index, boolean front, boolean inner) {
        _front = front;
        _inner = inner;
        _index = index;
    }

    public     int index()   { return  _index; }

    public boolean front()   { return  _front; }
    public boolean mesial()  { return  _front; }

    public boolean back()    { return !_front; }
    public boolean distal()  { return !_front; }

    public boolean inner()   { return  _inner; }
    public boolean lingual() { return  _inner; }

    public boolean outer()   { return !_inner; }
    public boolean buccal()  { return !_inner; }


    public boolean equals(Quadrant other) { return _index == other.index(); }
    public boolean equals(     int other) { return _index == other;         }
}
