package mw.molarwear.classes.dental.molar;

import java.util.Arrays;
import java.util.List;

import mw.molarwear.classes.dental.Tooth;
import mw.molarwear.classes.dental.enums.ToothMapping;
import mw.molarwear.classes.dental.molar.enums.Wear;
import mw.molarwear.classes.dental.molar.enums.Quadrant;

/**
 * This class is used for recording and editing dental data pertaining to human molar wear.
 *
 * @author Sean Pesce
 * @see    Tooth
 * @see    Surface
 * @see    Wear
 */

public class Molar extends Tooth {

    ///////////////////////////////////////////////////////////////
    ///////////////////////// STATIC DATA /////////////////////////
    ///////////////////////////////////////////////////////////////

    private static final String ERROR_MSG_INVALID_TYPE = "Invalid type (must be Tooth.Type.MOLAR)";


    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    // Tooth surface consisting of four quadrants
    private Surface _surface;


    //////////// Constructors ////////////

    public Molar(int index) {
        super(index);
        this.initialize(new Surface());
    }

    public Molar(ToothMapping tooth) {
        super(tooth);
        this.initialize(new Surface());
    }

    public Molar(int index, Surface surface) {
        super(index);
        this.initialize(surface);
    }

    public Molar(ToothMapping tooth, Surface surface) {
        super(tooth);
        this.initialize(surface);
    }


    private void initialize(Surface surface) {
        _surface = surface;
        if (!this.molar()) {
            throw new IllegalArgumentException(Molar.ERROR_MSG_INVALID_TYPE);
        }
    }


    //////////// Accessors ////////////

    public Surface surface() { return _surface; }


    //////////// Mutators ////////////

    public void setSurface(Surface surface) {
        _surface = surface;
    }
}
