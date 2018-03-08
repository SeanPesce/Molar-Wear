package mw.molarwear.data.classes.dental;

import java.io.Serializable;

import mw.molarwear.data.classes.dental.enums.ToothMapping;

/**
 * This class is used for recording and editing data pertaining to human teeth.
 *
 * @author Sean Pesce
 *
 * @see    ToothMapping
 * @see    mw.molarwear.data.classes.dental.molar.Molar
 */

public class Tooth implements Serializable {

    ///////////////////////////////////////////////////////////////
    ///////////////////////// STATIC DATA /////////////////////////
    ///////////////////////////////////////////////////////////////

    /**
     * Static subclass of Tooth for accessing descriptor strings.
     */
    public static class Descriptor {
        public static String UNKNOWN = "Unknown";

        public static String UPPER   = "Upper";
        public static String LOWER   = "Lower";
        public static String INNER   = "Inner";
        public static String LINGUAL = "Lingual";
        public static String OUTER   = "Outer";
        public static String BUCCAL  = "Buccal";
        public static String FRONT   = "Front";
        public static String MESIAL  = "Mesial";
        public static String BACK    = "Back";
        public static String DISTAL  = "Distal";
        public static String LEFT    = "Left";
        public static String RIGHT   = "Right";
        public static String CENTRAL = "Central";
        public static String LATERAL = "Lateral";
        public static String FIRST   = "First";
        public static String SECOND  = "Second";
        public static String THIRD   = "Third";

        public static String MOLAR    = "Molar";
        public static String PREMOLAR = "Premolar";
        public static String CUSPID   = "Cuspid";
        public static String INCISOR  = "Incisor";
    }

    public enum Type {
        MOLAR    (Descriptor.MOLAR),
        PREMOLAR (Descriptor.PREMOLAR),
        CUSPID   (Descriptor.CUSPID),
        INCISOR  (Descriptor.INCISOR);

        private final String _name;

        Type(String name) { _name = name; }

        public String toString() { return _name; }
    }


    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    protected final ToothMapping _tooth;


    //////////// Constructors ////////////

    public Tooth(         int index) { _tooth = ToothMapping.get(index); }
    public Tooth(ToothMapping tooth) { _tooth = tooth;                   }


    //////////// Accessors ////////////

    public ToothMapping mapping()         { return _tooth;                   }
    public          int index()           { return _tooth.index();           }
    public   Tooth.Type type()            { return _tooth.type();            }
    public      boolean upper()           { return _tooth.upper();           }
    public      boolean lower()           { return _tooth.lower();           }
    public      boolean left()            { return _tooth.left();            }
    public      boolean right()           { return _tooth.right();           }

    public      boolean molar()           { return _tooth.molar();           }
    public      boolean premolar()        { return _tooth.premolar();        }
    public      boolean cuspid()          { return _tooth.cuspid();          }
    public      boolean incisor()         { return _tooth.incisor();         }

    public      boolean molarOrPremolar() { return _tooth.molarOrPremolar(); }
    public      boolean incisorCentral()  { return _tooth.incisorCentral();  }
    public      boolean incisorLateral()  { return _tooth.incisorLateral();  }
}
