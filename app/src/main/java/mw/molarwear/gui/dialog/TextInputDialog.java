package mw.molarwear.gui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.widget.EditText;
import android.widget.LinearLayout;

import mw.molarwear.R;

/**
 * A basic dialog box that allows the user to enter text input.
 *
 * @author Sean Pesce
 *
 * @see    TwoButtonDialog
 * @see    DialogStringData
 */

public class TextInputDialog extends TwoButtonDialog {

    public static String DEFAULT_TEXT_INPUT_HINT = "";

    // GUI
    protected final EditText _textInput;

    protected String _textInputHint = DEFAULT_TEXT_INPUT_HINT;


    //////////// Constructors ////////////

    public TextInputDialog(Activity activity) {
        super(activity, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = DEFAULT_TEXT_INPUT_HINT;
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(Activity activity, String textInputHint) {
        super(activity, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = (textInputHint != null) ? textInputHint : "";
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(Activity activity, @StringRes int textInputHintId) {
        super(activity, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        try { _textInputHint = _activity.getResources().getString(textInputHintId); } catch (Resources.NotFoundException e) { _textInputHint = DEFAULT_TEXT_INPUT_HINT; }
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(Activity activity, DialogStringData strings) {
        super(activity, strings, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = DEFAULT_TEXT_INPUT_HINT;
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(Activity activity, DialogStringData strings, String textInputHint) {
        super(activity, strings, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = (textInputHint != null) ? textInputHint : "";
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(Activity activity, DialogStringData strings, @StringRes int textInputHintId) {
        super(activity, strings, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        try { _textInputHint = _activity.getResources().getString(textInputHintId); } catch (Resources.NotFoundException e) { _textInputHint = DEFAULT_TEXT_INPUT_HINT; }
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(DialogStringData strings) {
        super(strings, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = DEFAULT_TEXT_INPUT_HINT;
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(DialogStringData strings, String textInputHint) {
        super(strings, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = (textInputHint != null) ? textInputHint : "";
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(DialogStringData strings, @StringRes int textInputHintId) {
        super(strings, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        try { _textInputHint = _activity.getResources().getString(textInputHintId); } catch (Resources.NotFoundException e) { _textInputHint = DEFAULT_TEXT_INPUT_HINT; }
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(Activity activity, AlertDialog.Builder builder) {
        super(activity, builder, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = DEFAULT_TEXT_INPUT_HINT;
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(Activity activity, AlertDialog.Builder builder, String textInputHint) {
        super(activity, builder, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = (textInputHint != null) ? textInputHint : "";
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(Activity activity, AlertDialog.Builder builder, @StringRes int textInputHintId) {
        super(activity, builder, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        try { _textInputHint = _activity.getResources().getString(textInputHintId); } catch (Resources.NotFoundException e) { _textInputHint = DEFAULT_TEXT_INPUT_HINT; }
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(Activity activity, DialogStringData strings, AlertDialog.Builder builder) {
        super(activity, strings, builder, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = DEFAULT_TEXT_INPUT_HINT;
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(Activity activity, DialogStringData strings, AlertDialog.Builder builder, String textInputHint) {
        super(activity, strings, builder, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = (textInputHint != null) ? textInputHint : "";
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(Activity activity, DialogStringData strings, AlertDialog.Builder builder, @StringRes int textInputHintId) {
        super(activity, strings, builder, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        try { _textInputHint = _activity.getResources().getString(textInputHintId); } catch (Resources.NotFoundException e) { _textInputHint = DEFAULT_TEXT_INPUT_HINT; }
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(DialogStringData strings, AlertDialog.Builder builder) {
        super(strings, builder, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = DEFAULT_TEXT_INPUT_HINT;
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(DialogStringData strings, AlertDialog.Builder builder, String textInputHint) {
        super(strings, builder, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = (textInputHint != null) ? textInputHint : "";
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(DialogStringData strings, AlertDialog.Builder builder, @StringRes int textInputHintId) {
        super(strings, builder, R.layout.dialog_text_input);
        _textInput = (EditText) this.linearLayout().findViewById(R.id.txt_input_dlg);
        try { _textInputHint = _activity.getResources().getString(textInputHintId); } catch (Resources.NotFoundException e) { _textInputHint = DEFAULT_TEXT_INPUT_HINT; }
        _textInput.setHint(_textInputHint);
        initialize();
    }


    private void initialize() {
        setPositiveButton(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked "OK" button
                eraseText();
            }
        });

        setNegativeButton(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked "Cancel" button
                eraseText();
            }
        });
    }


    //////////// Accessors ////////////

    public       String text()          { return _textInput.getText().toString(); }
    public     EditText textInput()     { return _textInput;                      }
    public       String textInputHint() { return _textInputHint;                  }
    public LinearLayout linearLayout()  { return (LinearLayout)_layout;           }


    //////////// Mutators ////////////

    public void eraseText() {
        _textInput.getText().clear();
    }

    public void setText(String text) {
        if (text != null) {
            _textInput.setText(text);
        } else {
            _textInput.getText().clear();
        }
        updateDialog();
    }

    public void setText(@StringRes int textId) {
        try {
            setText(_activity.getResources().getString(textId));
        } catch (Resources.NotFoundException e) {}
    }

    public void setTextInputHint(String textInputHint) {
        if (textInputHint != null) {
            _textInputHint = textInputHint;
            _textInput.setHint(_textInputHint);
            updateDialog();
        }
    }

    public void setTextInputHint(@StringRes int textInputHintId) {
        try {
            setTextInputHint(_activity.getResources().getString(textInputHintId));
        } catch (Resources.NotFoundException e) {}
    }
}
