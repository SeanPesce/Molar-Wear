package mw.molarwear.data.classes.dental.enums;

import mw.molarwear.data.classes.dental.Tooth;

/**
 * This enum is used for referencing each of the 32 teeth that make up a typical human dental structure.
 *
 * <p><ul>
 *     <li>{@link #MOLAR3_U_R}</li>
 *     <li>{@link #MOLAR2_U_R}</li>
 *     <li>{@link #MOLAR1_U_R}</li>
 *     <li>{@link #PREMOLAR2_U_R}</li>
 *     <li>{@link #PREMOLAR1_U_R}</li>
 *     <li>{@link #CUSPID_U_R}</li>
 *     <li>{@link #INCISOR_LATERAL_U_R}</li>
 *     <li>{@link #INCISOR_CENTRAL_U_R}</li>
 *     <li>{@link #INCISOR_CENTRAL_U_L}</li>
 *     <li>{@link #INCISOR_LATERAL_U_L}</li>
 *     <li>{@link #CUSPID_U_L}</li>
 *     <li>{@link #PREMOLAR1_U_L}</li>
 *     <li>{@link #PREMOLAR2_U_L}</li>
 *     <li>{@link #MOLAR1_U_L}</li>
 *     <li>{@link #MOLAR2_U_L}</li>
 *     <li>{@link #MOLAR3_U_L}</li>
 *     <li>{@link #MOLAR3_L_L}</li>
 *     <li>{@link #MOLAR2_L_L}</li>
 *     <li>{@link #MOLAR1_L_L}</li>
 *     <li>{@link #PREMOLAR2_L_L}</li>
 *     <li>{@link #PREMOLAR1_L_L}</li>
 *     <li>{@link #CUSPID_L_L}</li>
 *     <li>{@link #INCISOR_LATERAL_L_L}</li>
 *     <li>{@link #INCISOR_CENTRAL_L_L}</li>
 *     <li>{@link #INCISOR_CENTRAL_L_R}</li>
 *     <li>{@link #INCISOR_LATERAL_L_R}</li>
 *     <li>{@link #CUSPID_L_R}</li>
 *     <li>{@link #PREMOLAR1_L_R}</li>
 *     <li>{@link #PREMOLAR2_L_R}</li>
 *     <li>{@link #MOLAR1_L_R}</li>
 *     <li>{@link #MOLAR2_L_R}</li>
 *     <li>{@link #MOLAR3_L_R}</li>
 * </ul></p>
 *
 * <p>References:</p>
 * <ul>
 *     <li><a href="https://d1mpuhcnuqykh7.cloudfront.net/uploads/2013/08/Teeth-Chart.jpg">Quadrants & indices</a></li>
 *     <li><a href="https://i.pinimg.com/originals/f8/44/98/f844983dac1493acf74f8f8af0f4ae4b.jpg">Tooth names</a></li>
 * </ul>
 *
 * @author Sean Pesce
 *
 * @see    Tooth
 */

public enum ToothMapping {

    ///////////////////////////////////////////////////////////////
    ///////////////////////// STATIC DATA /////////////////////////
    ///////////////////////////////////////////////////////////////

    // U_R = Upper right
    /**
     * Third molar ("wisdom tooth"; upper right)
     */
    MOLAR3_U_R          (1,  true,  false, Tooth.Type.MOLAR    ),
    /**
     * Second molar (upper right)
     */
    MOLAR2_U_R          (2,  true,  false, Tooth.Type.MOLAR    ),
    /**
     * First molar (upper right)
     */
    MOLAR1_U_R          (3,  true,  false, Tooth.Type.MOLAR    ),
    /**
     * Second premolar (upper right)
     */
    PREMOLAR2_U_R       (4,  true,  false, Tooth.Type.PREMOLAR ),
    /**
     * First premolar (upper right)
     */
    PREMOLAR1_U_R       (5,  true,  false, Tooth.Type.PREMOLAR ),
    /**
     * Cuspid (upper right)
     */
    CUSPID_U_R          (6,  true,  false, Tooth.Type.CUSPID   ),
    /**
     * Lateral incisor (upper right)
     */
    INCISOR_LATERAL_U_R (7,  true,  false, Tooth.Type.INCISOR  ),
    /**
     * Central incisor (upper right)
     */
    INCISOR_CENTRAL_U_R (8,  true,  false, Tooth.Type.INCISOR  ),

    // U_L = Upper left
    /**
     * Central incisor (upper left)
     */
    INCISOR_CENTRAL_U_L (9,  true,  true,  Tooth.Type.INCISOR  ),
    /**
     * Lateral incisor (upper left)
     */
    INCISOR_LATERAL_U_L (10, true,  true,  Tooth.Type.INCISOR  ),
    /**
     * Cuspid (upper left)
     */
    CUSPID_U_L          (11, true,  true,  Tooth.Type.CUSPID   ),
    /**
     * First premolar (upper left)
     */
    PREMOLAR1_U_L       (12, true,  true,  Tooth.Type.PREMOLAR ),
    /**
     * Second premolar (upper left)
     */
    PREMOLAR2_U_L       (13, true,  true,  Tooth.Type.PREMOLAR ),
    /**
     * First molar (upper left)
     */
    MOLAR1_U_L          (14, true,  true,  Tooth.Type.MOLAR    ),
    /**
     * Second molar (upper left)
     */
    MOLAR2_U_L          (15, true,  true,  Tooth.Type.MOLAR    ),
    /**
     * Third molar ("wisdom tooth"; upper left)
     */
    MOLAR3_U_L          (16, true,  true,  Tooth.Type.MOLAR    ),

    // L_L = Lower left
    /**
     * Third molar ("wisdom tooth"; lower left)
     */
    MOLAR3_L_L          (17, false, true,  Tooth.Type.MOLAR    ),
    /**
     * Second molar (lower left)
     */
    MOLAR2_L_L          (18, false, true,  Tooth.Type.MOLAR    ),
    /**
     * First molar (lower left)
     */
    MOLAR1_L_L          (19, false, true,  Tooth.Type.MOLAR    ),
    /**
     * Second premolar (lower left)
     */
    PREMOLAR2_L_L       (20, false, true,  Tooth.Type.PREMOLAR ),
    /**
     * First premolar (lower left)
     */
    PREMOLAR1_L_L       (21, false, true,  Tooth.Type.PREMOLAR ),
    /**
     * Cuspid (lower left)
     */
    CUSPID_L_L          (22, false, true,  Tooth.Type.CUSPID   ),
    /**
     * Lateral incisor (lower left)
     */
    INCISOR_LATERAL_L_L (23, false, true,  Tooth.Type.INCISOR  ),
    /**
     * Central incisor (lower left)
     */
    INCISOR_CENTRAL_L_L (24, false, true,  Tooth.Type.INCISOR  ),

    // L_R = Lower right
    /**
     * Central incisor (lower right)
     */
    INCISOR_CENTRAL_L_R (25, false, false, Tooth.Type.INCISOR  ),
    /**
     * Lateral incisor (lower right)
     */
    INCISOR_LATERAL_L_R (26, false, false, Tooth.Type.INCISOR  ),
    /**
     * Cuspid (lower right)
     */
    CUSPID_L_R          (27, false, false, Tooth.Type.CUSPID   ),
    /**
     * First premolar (lower right)
     */
    PREMOLAR1_L_R       (28, false, false, Tooth.Type.PREMOLAR ),
    /**
     * Second premolar (lower right)
     */
    PREMOLAR2_L_R       (29, false, false, Tooth.Type.PREMOLAR ),
    /**
     * First molar (lower right)
     */
    MOLAR1_L_R          (30, false, false, Tooth.Type.MOLAR    ),
    /**
     * Second molar (lower right)
     */
    MOLAR2_L_R          (31, false, false, Tooth.Type.MOLAR    ),
    /**
     * Third molar ("wisdom tooth"; lower right)
     */
    MOLAR3_L_R          (32, false, false, Tooth.Type.MOLAR    );


    private static final String[] NAME_PREFIX =
        {
            /*  0 */ "UNKNOWN",
            /*  1 */ "Third",
            /*  2 */ "Second",
            /*  3 */ "First",
            /*  4 */ "Second",
            /*  5 */ "First",
            /*  6 */ "",
            /*  7 */ "Lateral",
            /*  8 */ "Central",
            /*  9 */ "Central",
            /* 10 */ "Lateral",
            /* 11 */ "",
            /* 12 */ "First",
            /* 13 */ "Second",
            /* 14 */ "First",
            /* 15 */ "Second",
            /* 16 */ "Third",
            /* 17 */ "Third",
            /* 18 */ "Second",
            /* 19 */ "First",
            /* 20 */ "Second",
            /* 21 */ "First",
            /* 22 */ "",
            /* 23 */ "Lateral",
            /* 24 */ "Central",
            /* 25 */ "Central",
            /* 26 */ "Lateral",
            /* 27 */ "",
            /* 28 */ "First",
            /* 29 */ "Second",
            /* 30 */ "First",
            /* 31 */ "Second",
            /* 32 */ "Third"
        };

    public static ToothMapping get(int index) { return ToothMapping.values()[index - 1]; } // Indices start at 1


    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    protected final        int _index;
    protected final    boolean _upper;
    protected final    boolean _left;
    protected final Tooth.Type _type;

    ToothMapping(int index, boolean upper, boolean left, Tooth.Type type) {
        _index = index;
        _upper = upper;
        _left  = left;
        _type  = type;
    }

    public        int index() { return  _index; }
    public Tooth.Type type()  { return  _type;  }
    public    boolean upper() { return  _upper; }
    public    boolean lower() { return !_upper; }
    public    boolean left()  { return  _left;  }
    public    boolean right() { return !_left;  }

    public boolean molar()    { return _type == Tooth.Type.MOLAR;    }
    public boolean premolar() { return _type == Tooth.Type.PREMOLAR; }
    public boolean cuspid()   { return _type == Tooth.Type.CUSPID;   }
    public boolean incisor()  { return _type == Tooth.Type.INCISOR;  }

    public boolean molarOrPremolar() { return _type == Tooth.Type.MOLAR || _type == Tooth.Type.PREMOLAR; }

    public boolean incisorCentral()  { return this.equals(INCISOR_CENTRAL_L_L) || this.equals(INCISOR_CENTRAL_L_R) || this.equals(INCISOR_CENTRAL_U_L) || this.equals(INCISOR_CENTRAL_U_R); }
    public boolean incisorLateral()  { return this.equals(INCISOR_LATERAL_L_L) || this.equals(INCISOR_LATERAL_L_R) || this.equals(INCISOR_LATERAL_U_L) || this.equals(INCISOR_LATERAL_U_R); }

    public  String nameFull()         { return this.nameShort() + " (" + this.positionToString() + ")";             }
    public  String nameShort()        { return ToothMapping.NAME_PREFIX[_index] + " " + _type.toString();           }
    public  String positionToString() { return ((_upper) ? Tooth.Descriptor.UPPER : Tooth.Descriptor.LOWER) + " " + ((_left) ? Tooth.Descriptor.LEFT.toLowerCase() : Tooth.Descriptor.RIGHT.toLowerCase()); }

    public boolean equals(ToothMapping other) { return _index == other.index(); }
    public boolean equals(         int other) { return _index == other;         }
}
