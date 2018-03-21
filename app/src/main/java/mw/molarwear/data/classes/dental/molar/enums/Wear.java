package mw.molarwear.data.classes.dental.molar.enums;

import android.app.Activity;

import mw.molarwear.R;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.TwoButtonDialog;

/**
 * This enum represents varying levels (or "scores") of wear for describing the physical condition of
 * the surface of human molars (Bottom surface of upper molars, or top surface of lower molars). Scores
 * range from zero to eight, with higher values indicating greater amounts of surface wear.
 *
 * * <ul>
 *     <li>{@link #ZERO}</li>
 *     <li>{@link #ONE}</li>
 *     <li>{@link #TWO}</li>
 *     <li>{@link #THREE}</li>
 *     <li>{@link #FOUR}</li>
 *     <li>{@link #FIVE}</li>
 *     <li>{@link #SIX}</li>
 *     <li>{@link #SEVEN}</li>
 *     <li>{@link #EIGHT}</li>
 *     <li>{@link #UNKNOWN}</li>
 * </ul>
 *
 * @author Sean Pesce
 *
 * @see    mw.molarwear.data.classes.dental.molar.SurfaceQuadrant
 * @see    mw.molarwear.data.classes.dental.molar.Molar
 */

public enum Wear {

    ///////////////////////////////////////////////////////////////
    ///////////////////////// STATIC DATA /////////////////////////
    ///////////////////////////////////////////////////////////////

    /**
     * Condition of least wear; cusp may be worn/wear facets present but no dentine exposed.
     */
    ZERO    ( 0),
    /**
     * Surface is worn flat, and a pinprick up to a small "dot" of dentine is visible.
     */
    ONE     ( 1),
    /**
     * Surface is worn flat, and dentine exposure covers ¼ of quadrant or less.
     */
    TWO     ( 2),
    /**
     * Greater dentine exposure with more than ¼ of quadrant exposed. Much enamel is still present
     *  within the quadrant (if each quadrant can be visualized with three "sides," the enamel fully
     *  surrounds the dentine exposed)
     */
    THREE   ( 3),
    /**
     * Use this score as an intermediary, when the dentine engages one of two sides (less than ⅓) to
     *  coalesce with a neighbouring quadrant
     */
    FOUR    ( 4),
    /**
     * Enamel is found on only two "sides" of the quadrant; usually suggests it has coalesced with a
     *  neighboring quadrant).
     */
    FIVE    ( 5),
    /**
     * Enamel is only found on one "side" of the quadrant (typically outer rim). Enamel is thick to
     *  medium.
     */
    SIX     ( 6),
    /**
     * Enamel is only found on one "side" of quadrant (typically outer rim); however, enamel is thin.
     *  Sections may be worn through.
     */
    SEVEN   ( 7),
    /**
     * Condition of most wear; full dentine exposure. No enamel present.
     */
    EIGHT   ( 8),
    /**
     * No data.
     */
    UNKNOWN (-1);


    private static int INDEX_OF_UNKNOWN;

    private static final String[] DESCRIPTION =
        {
            /* 0 */ "Condition of least wear; cusp may be worn/wear facets present but no dentine exposed.",
            /* 1 */ "Surface is worn flat, and a pinprick up to a small \"dot\" of dentine is visible.",
            /* 2 */ "Surface is worn flat, and dentine exposure covers ¼ of quadrant or less.",
            /* 3 */ "Greater dentine exposure with more than ¼ of quadrant exposed. Much enamel is still " +
                    "present within the quadrant (if each quadrant can be visualized with three \"sides,\"" +
                    " the enamel fully surrounds the dentine exposed).",
            /* 4 */ "Use this score as an intermediary, when the dentine engages one of two sides (less" +
                    " than ⅓) to coalesce with a neighbouring quadrant.",
            /* 5 */ "Enamel is found on only two \"sides\" of the quadrant; usually suggests it has" +
                    " coalesced with a neighboring quadrant).",
            /* 6 */ "Enamel is only found on one \"side\" of the quadrant (typically outer rim). Enamel" +
                    " is thick to medium.",
            /* 7 */ "Enamel is only found on one \"side\" of quadrant (typically outer rim); however, enamel" +
                    " is thin. Sections may be worn through.",
            /* 8 */ "Condition of most wear; full dentine exposure. No enamel present."
        };

    public static Wear   get(int score) { return (score >= 0 && score < Wear.values().length && score != Wear.INDEX_OF_UNKNOWN) ? Wear.values()[score] : UNKNOWN; }
    public static String getDescription(int score) { return (score >= 0 && score < Wear.values().length && score != Wear.INDEX_OF_UNKNOWN) ? Wear.DESCRIPTION[score] : UNKNOWN.description(); }
    public static int    INDEX_OF_UNKNOWN() { return Wear.INDEX_OF_UNKNOWN; }
    public static void   showWearDescriptionDialog(Activity activity) {
        StringBuilder dlgMsg = new StringBuilder("\n\"" + activity.getString(R.string.desc_wear_lvl_unk_picker)
            + "\"  -  " + activity.getString(R.string.desc_wear_lvl_unk) + "\n");
        for (int i = 0; i < (Wear.values().length-1); i++) {
            dlgMsg.append("\n").append(i).append("  -  ").append(Wear.get(i).description()).append("\n");
        }
        final TwoButtonDialog dlg = new TwoButtonDialog(new DialogStringData(activity,
            R.string.dlg_title_wear_lvl_desc,
            dlgMsg.toString()));
        dlg.show();
    }

    // Initialize static data
    static {
        for (int i = 0; i < Wear.values().length; i++) {
            Wear w = Wear.values()[i];
            if (w.score() >= 0) {
                w._description = Wear.DESCRIPTION[w._score];
            } else {
                Wear.INDEX_OF_UNKNOWN = i;
                w._description = "No available data, possibly due to surface damage, missing teeth, etc.";
            }
        }
    }


    ///////////////////////////////////////////////////////////////
    //////////////////////// INSTANCE DATA ////////////////////////
    ///////////////////////////////////////////////////////////////

    private final    int _score;
    private       String _description;
    private      boolean _initialized = false;

    Wear(int score) {
        _score = score;
    }

    //////////// Accessors ////////////
    public    int  score()        { return _score;       }
    public String  description()  { return _description; }
    public boolean inititalized() { return _initialized; }

    //////////// Mutators ////////////
    public void setDescription(String description) {
        // Can only be called once
        if (!_initialized) {
            _description = description;
            _initialized = true;
        }
    }

    //////////// Miscellaneous ////////////
    public boolean equals(Wear other) { return _score == other.score(); }
    public boolean equals(int  other) { return _score == other;         }
}
