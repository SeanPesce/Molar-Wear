package mw.molarwear.data.classes.dental.molar;

import java.io.Serializable;

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
