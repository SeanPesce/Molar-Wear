package mw.molarwear.gui.dialog;

import android.app.Activity;
import android.widget.NumberPicker;

import mw.molarwear.R;
import mw.molarwear.data.classes.dental.molar.enums.Wear;

/**
 * A basic dialog box that allows the user to select the wear score of a surface quadrant of a molar.
 *
 * @author Sean Pesce
 *
 * @see    PickerDialog
 * @see    mw.molarwear.data.classes.dental.molar.Molar
 * @see    mw.molarwear.data.classes.dental.molar.enums.Wear
 */

public class WearPickerDialog extends PickerDialog {

    public  static final int UNKNOWN_SCORE = 9;
    private static final int _DEFAULT_SELECTION = UNKNOWN_SCORE;

    private static final int      _TITLE_ID = R.string.dlg_title_set_wear_lvl;
    private static final int        _MSG_ID = R.string.dlg_msg_set_wear_lvl;
    private static final int _POS_BT_TXT_ID = R.string.dlg_bt_ok;
    private static final int _NEG_BT_TXT_ID = R.string.dlg_bt_cancel;

    public WearPickerDialog(Activity activity) {
        super(activity, new DialogStringData(activity, _TITLE_ID, _MSG_ID, _POS_BT_TXT_ID, _NEG_BT_TXT_ID), _DEFAULT_SELECTION);
        initialize();
    }

    public WearPickerDialog(Activity activity, int selection) {
        super(activity, new DialogStringData(activity, _TITLE_ID, _MSG_ID, _POS_BT_TXT_ID, _NEG_BT_TXT_ID), (selection == Wear.UNKNOWN.score()) ? UNKNOWN_SCORE : selection);
        initialize();
    }

    private void initialize() {
        _picker.setMinValue(0);
        _picker.setMaxValue(UNKNOWN_SCORE);
        _picker.setFormatter(new PickerFormatter());
    }


    //////////// Accessors ////////////

    @Override
    public int selection() {
        return selectionWear();
    }

    public int selectionIndex() {
        return super.selection();
    }

    public int selectionWear() {
        final int sel = super.selection();
        return (sel >= UNKNOWN_SCORE) ? Wear.UNKNOWN.score() : sel;
    }


    //////////// Mutators ////////////

    @Override
    public void setSelection(int selection) {
        super.setSelection((selection < _picker.getMinValue() || selection > _picker.getMaxValue()) ? UNKNOWN_SCORE : selection);
    }



    public class PickerFormatter implements NumberPicker.Formatter {

        public PickerFormatter() {
        }

        @Override
        public String format(int value) {
            if (value == Wear.UNKNOWN.score() || value == UNKNOWN_SCORE) {
                return "?";
            }
            return "" + value;
        }

    }
}
