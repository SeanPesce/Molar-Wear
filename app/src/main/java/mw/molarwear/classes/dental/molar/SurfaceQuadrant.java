package mw.molarwear.classes.dental.molar;

import mw.molarwear.classes.dental.molar.enums.Quadrant;
import mw.molarwear.classes.dental.molar.enums.Wear;

/**
 * This class contains data about wear on one of four {@link Quadrant} that make up the {@link Surface}
 * of a human {@link Molar} (Bottom surface of upper molars, or top surface of lower molars).
 *
 * @author Sean Pesce
 * @see    Molar
 * @see    Surface
 * @see    Quadrant
 * @see    Wear
 */

public class SurfaceQuadrant {

    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    private final Quadrant _quadrant;
    private           Wear _wear;


    //////////// Constructors ////////////

    public SurfaceQuadrant(Quadrant quadrant) {
        _quadrant = quadrant;
        _wear     = Wear.UNKNOWN;
    }

    public SurfaceQuadrant(int quadrant) {
        _quadrant = Quadrant.get(quadrant);
        _wear     = Wear.UNKNOWN;
    }

    public SurfaceQuadrant(Quadrant quadrant, Wear wear) {
        _quadrant = quadrant;
        _wear     = wear;
    }

    public SurfaceQuadrant(int quadrant, Wear wear) {
        _quadrant = Quadrant.get(quadrant);
        _wear     = wear;
    }

    public SurfaceQuadrant(Quadrant quadrant, int wear) {
        _quadrant = quadrant;
        _wear     = Wear.get(wear);
    }

    public SurfaceQuadrant(int quadrant, int wear) {
        _quadrant = Quadrant.get(quadrant);
        _wear     = Wear.get(wear);
    }


    //////////// Accessors ////////////

    public int    index()           { return _quadrant.index();   } // 1-4
    public Wear   wear()            { return _wear;               }
    public int    wearScore()       { return _wear.score();       } // 0-8 (or -1 for unknown)
    public String wearDescription() { return _wear.description(); }

    // SurfaceQuadrant position
    public boolean front()   { return _quadrant.front();   }
    public boolean mesial()  { return _quadrant.mesial();  }
    public boolean back()    { return _quadrant.back();    }
    public boolean distal()  { return _quadrant.distal();  }
    public boolean inner()   { return _quadrant.inner();   }
    public boolean lingual() { return _quadrant.lingual(); }
    public boolean outer()   { return _quadrant.outer();   }
    public boolean buccal()  { return _quadrant.buccal();  }


    //////////// Mutators ////////////

    public void setWear(Wear wear) { _wear = wear;           }
    public void setWear(int  wear) { _wear = Wear.get(wear); }
}
