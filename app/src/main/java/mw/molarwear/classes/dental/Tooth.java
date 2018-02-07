package mw.molarwear.classes.dental;

import mw.molarwear.classes.dental.enums.ToothMapping;

/**
 * This class is used for recording and editing data pertaining to human teeth.
 *
 * @author Sean Pesce
 * @see    ToothMapping
 * @see    mw.molarwear.classes.dental.molar.Molar
 */

public class Tooth {

    ///////////////////////////////////////////////////////////////
    ///////////////////////// STATIC DATA /////////////////////////
    ///////////////////////////////////////////////////////////////

    public enum Type {
        MOLAR    ("Molar"),
        PREMOLAR ("Premolar"),
        CUSPID   ("Cuspid"),
        INCISOR  ("Incisor");

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
