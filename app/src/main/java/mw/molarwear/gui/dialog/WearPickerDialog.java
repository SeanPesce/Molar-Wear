package mw.molarwear.gui.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
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

    public  static String UNKNOWN_SCORE_DESC = "Missing/No data";

    public  static final int MIN_SELECTION_INDEX = 0;
    public  static final int MAX_SELECTION_INDEX = 9;
    public  static final int UNKNOWN_SCORE_INDEX = MIN_SELECTION_INDEX;
    private static final int _DEFAULT_SELECTION_INDEX = UNKNOWN_SCORE_INDEX;

    private static final int      _TITLE_ID = R.string.dlg_title_set_wear_lvl;
    private static final int        _MSG_ID = R.string.dlg_msg_set_wear_lvl;
    private static final int _POS_BT_TXT_ID = R.string.dlg_bt_ok;
    private static final int _NEG_BT_TXT_ID = R.string.dlg_bt_cancel;

    public WearPickerDialog(Activity activity) {
        super(activity, new DialogStringData(activity, _TITLE_ID, _MSG_ID, _POS_BT_TXT_ID, _NEG_BT_TXT_ID), _DEFAULT_SELECTION_INDEX);
        initialize();
    }

    public WearPickerDialog(Activity activity, int score) {
        super(activity, new DialogStringData(activity, _TITLE_ID, _MSG_ID, _POS_BT_TXT_ID, _NEG_BT_TXT_ID), scoreToSelectionIndex(score));
        initialize();
    }

    @Override
    protected void initialize() {
        super.initialize();
        _picker.setMinValue(MIN_SELECTION_INDEX);
        _picker.setMaxValue(MAX_SELECTION_INDEX);
        _picker.setFormatter(new PickerFormatter());
        final View title = _activity.getLayoutInflater().inflate(R.layout.dialog_wear_picker_title, null);
        _builder.setCustomTitle(title);
        final ImageView btWearHelp = title.findViewById(R.id.bt_wear_descriptions);
        btWearHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Wear.showWearDescriptionDialog(_activity);
            }
        });
    }


    //////////// Accessors ////////////

    @Override
    public int selection() {
        return selectionScore();
    }

    public int selectionIndex() {
        return super.selection();
    }

    public int selectionScore() {
        return selectionIndexToScore(super.selection());
    }


    //////////// Mutators ////////////

    @Override
    public void setSelection(int score) {
        super.setSelection(scoreToSelectionIndex(score));
    }


    public static int scoreToSelectionIndex(int score) {
        return (score < 0 || score > 8) ? UNKNOWN_SCORE_INDEX : (score+1);
    }

    public static int selectionIndexToScore(int selection) {
        return (selection <= MIN_SELECTION_INDEX || selection > MAX_SELECTION_INDEX) ? Wear.UNKNOWN.score() : (selection - 1);
    }



    public class PickerFormatter implements NumberPicker.Formatter {

        public PickerFormatter() {
        }

        @Override
        public String format(int value) {
            final int score = selectionIndexToScore(value);
            if (score == Wear.UNKNOWN.score()) {
                return WearPickerDialog.UNKNOWN_SCORE_DESC;
            }
            return "" + score;
        }

    }
}
