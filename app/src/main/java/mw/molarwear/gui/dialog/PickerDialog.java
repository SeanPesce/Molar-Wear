package mw.molarwear.gui.dialog;

import android.support.v7.app.AppCompatActivity;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import mw.molarwear.R;

/**
 * A basic dialog box that allows the user to select an item from a picker.
 *
 * @author Sean Pesce
 *
 * @see    BasicDialog
 * @see    DialogStringData
 */

public class PickerDialog extends BasicDialog {

    public final int DEFAULT_SELECTION = 0;

    // GUI
    protected final MaterialNumberPicker _picker;


    //////////// Constructors ////////////

    public PickerDialog(AppCompatActivity activity) {
        super(activity, R.layout.dialog_picker_input);
        _picker = _body.findViewById(R.id.picker_input_dlg);
        _picker.setValue(DEFAULT_SELECTION);
        initialize();
    }

    public PickerDialog(AppCompatActivity activity, int selection) {
        super(activity, R.layout.dialog_picker_input);
        _picker = _body.findViewById(R.id.picker_input_dlg);
        _picker.setValue(selection);
        initialize();
    }

    public PickerDialog(DialogStringData strings) {
        super(strings, R.layout.dialog_picker_input);
        _picker = _body.findViewById(R.id.picker_input_dlg);
        _picker.setValue(DEFAULT_SELECTION);
        initialize();
    }

    public PickerDialog(DialogStringData strings, int selection) {
        super(strings, R.layout.dialog_picker_input);
        _picker = _body.findViewById(R.id.picker_input_dlg);
        _picker.setValue(selection);
        initialize();
    }


    protected void initialize() {

    }


    //////////// Accessors ////////////

    public                  int selection()    { return _picker.getValue();    }
    public MaterialNumberPicker picker()       { return _picker;               }


    //////////// Mutators ////////////

    public void resetPicker() {
        _picker.setValue(DEFAULT_SELECTION);
    }

    public void setSelection(int selection) {
        _picker.setValue(selection);
    }

}
