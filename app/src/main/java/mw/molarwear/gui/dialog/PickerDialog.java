package mw.molarwear.gui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.LinearLayout;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import mw.molarwear.R;

/**
 * A basic dialog box that allows the user to select an item from a picker.
 *
 * @author Sean Pesce
 *
 * @see    TwoButtonDialog
 * @see    DialogStringData
 */

public class PickerDialog extends TwoButtonDialog {

    public final int DEFAULT_SELECTION = 0;

    // GUI
    protected final MaterialNumberPicker _picker;


    //////////// Constructors ////////////

    public PickerDialog(Activity activity) {
        super(activity, R.layout.dialog_picker_input);
        _picker = (MaterialNumberPicker) this.linearLayout().findViewById(R.id.picker_input_dlg);
        _picker.setValue(DEFAULT_SELECTION);
        initialize();
    }

    public PickerDialog(Activity activity, int selection) {
        super(activity, R.layout.dialog_picker_input);
        _picker = (MaterialNumberPicker) this.linearLayout().findViewById(R.id.picker_input_dlg);
        _picker.setValue(selection);
        initialize();
    }

    public PickerDialog(Activity activity, DialogStringData strings) {
        super(activity, strings, R.layout.dialog_picker_input);
        _picker = (MaterialNumberPicker) this.linearLayout().findViewById(R.id.picker_input_dlg);
        _picker.setValue(DEFAULT_SELECTION);
        initialize();
    }

    public PickerDialog(Activity activity, DialogStringData strings, int selection) {
        super(activity, strings, R.layout.dialog_picker_input);
        _picker = (MaterialNumberPicker) this.linearLayout().findViewById(R.id.picker_input_dlg);
        _picker.setValue(selection);
        initialize();
    }

    public PickerDialog(DialogStringData strings) {
        super(strings, R.layout.dialog_picker_input);
        _picker = (MaterialNumberPicker) this.linearLayout().findViewById(R.id.picker_input_dlg);
        _picker.setValue(DEFAULT_SELECTION);
        initialize();
    }

    public PickerDialog(DialogStringData strings, int selection) {
        super(strings, R.layout.dialog_picker_input);
        _picker = (MaterialNumberPicker) this.linearLayout().findViewById(R.id.picker_input_dlg);
        _picker.setValue(selection);
        initialize();
    }

    public PickerDialog(Activity activity, AlertDialog.Builder builder) {
        super(activity, builder, R.layout.dialog_picker_input);
        _picker = (MaterialNumberPicker) this.linearLayout().findViewById(R.id.picker_input_dlg);
        _picker.setValue(DEFAULT_SELECTION);
        initialize();
    }

    public PickerDialog(Activity activity, AlertDialog.Builder builder, int selection) {
        super(activity, builder, R.layout.dialog_picker_input);
        _picker = (MaterialNumberPicker) this.linearLayout().findViewById(R.id.picker_input_dlg);
        _picker.setValue(selection);
        initialize();
    }

    public PickerDialog(Activity activity, DialogStringData strings, AlertDialog.Builder builder) {
        super(activity, strings, builder, R.layout.dialog_picker_input);
        _picker = (MaterialNumberPicker) this.linearLayout().findViewById(R.id.picker_input_dlg);
        _picker.setValue(DEFAULT_SELECTION);
        initialize();
    }

    public PickerDialog(Activity activity, DialogStringData strings, AlertDialog.Builder builder, int selection) {
        super(activity, strings, builder, R.layout.dialog_picker_input);
        _picker = (MaterialNumberPicker) this.linearLayout().findViewById(R.id.picker_input_dlg);
        _picker.setValue(selection);
        initialize();
    }

    public PickerDialog(DialogStringData strings, AlertDialog.Builder builder) {
        super(strings, builder, R.layout.dialog_picker_input);
        _picker = (MaterialNumberPicker) this.linearLayout().findViewById(R.id.picker_input_dlg);
        _picker.setValue(DEFAULT_SELECTION);
        initialize();
    }

    public PickerDialog(DialogStringData strings, AlertDialog.Builder builder, int selection) {
        super(strings, builder, R.layout.dialog_picker_input);
        _picker = (MaterialNumberPicker) this.linearLayout().findViewById(R.id.picker_input_dlg);
        _picker.setValue(selection);
        initialize();
    }


    private void initialize() {
//        setPositiveButton(new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User clicked "OK" button
//
//            }
//        });
//
//        setNegativeButton(new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User clicked "Cancel" button
//
//            }
//        });
    }


    //////////// Accessors ////////////

    public                  int selection()    { return _picker.getValue();    }
    public MaterialNumberPicker picker()       { return _picker;               }
    public         LinearLayout linearLayout() { return (LinearLayout)_layout; }


    //////////// Mutators ////////////

    public void resetPicker() {
        _picker.setValue(DEFAULT_SELECTION);
        updateDialog();
    }

    public void setSelection(int selection) {
        _picker.setValue(selection);
        updateDialog();
    }

}
