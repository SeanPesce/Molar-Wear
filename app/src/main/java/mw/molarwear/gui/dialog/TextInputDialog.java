package mw.molarwear.gui.dialog;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import mw.molarwear.R;
import mw.molarwear.util.AppUtil;

/**
 * A basic dialog box that allows the user to enter text input.
 *
 * @author Sean Pesce
 *
 * @see    BasicDialog
 * @see    DialogStringData
 */

public class TextInputDialog extends BasicDialog {

    public static String DEFAULT_TEXT_INPUT_HINT = "";

    // GUI
    protected final EditText _textInput;

    protected String _textInputHint = DEFAULT_TEXT_INPUT_HINT;


    //////////// Constructors ////////////

    public TextInputDialog(AppCompatActivity activity) {
        super(activity, R.layout.dialog_text_input);
        _textInput = this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = DEFAULT_TEXT_INPUT_HINT;
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(AppCompatActivity activity, String textInputHint) {
        super(activity, R.layout.dialog_text_input);
        _textInput = this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = (textInputHint != null) ? textInputHint : "";
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(AppCompatActivity activity, @StringRes int textInputHintId) {
        super(activity, R.layout.dialog_text_input);
        _textInput = this.linearLayout().findViewById(R.id.txt_input_dlg);
        try { _textInputHint = _activity.getResources().getString(textInputHintId); } catch (Resources.NotFoundException e) { _textInputHint = DEFAULT_TEXT_INPUT_HINT; }
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(DialogStringData strings) {
        super(strings, R.layout.dialog_text_input);
        _textInput = this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = DEFAULT_TEXT_INPUT_HINT;
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(DialogStringData strings, String textInputHint) {
        super(strings, R.layout.dialog_text_input);
        _textInput = this.linearLayout().findViewById(R.id.txt_input_dlg);
        _textInputHint = (textInputHint != null) ? textInputHint : "";
        _textInput.setHint(_textInputHint);
        initialize();
    }

    public TextInputDialog(DialogStringData strings, @StringRes int textInputHintId) {
        super(strings, R.layout.dialog_text_input);
        _textInput = this.linearLayout().findViewById(R.id.txt_input_dlg);
        try { _textInputHint = _activity.getResources().getString(textInputHintId); } catch (Resources.NotFoundException e) { _textInputHint = DEFAULT_TEXT_INPUT_HINT; }
        _textInput.setHint(_textInputHint);
        initialize();
    }


    private void initialize() {

        _closeOnClickPos = false;

        setMinWidth(260);

        _onClickPosInternalAdd.add(new View.OnClickListener() {
            public void onClick(View view) {
                // User clicked "OK" button
                //AppUtil.hideKeyboard(_activity, _body);
                hide();
            }
        });

//        _onClickNegInternalAdd.add(new View.OnClickListener() {
//            public void onClick(View view) {
//                // User clicked "Cancel" button
//                AppUtil.hideKeyboard(_activity, _body);
//            }
//        });

        _textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _textInput.setError(null);
            }
        });
        _textInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //AppUtil.hideKeyboard(_activity, v);
                    _btPos.requestFocus();
                    AppUtil.sendKeyPress(KeyEvent.KEYCODE_ENTER);
                    return true;
                } else {
                    return false;
                }
            }
        });
        _onDismissInternalAdd.add(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                AppUtil.hideKeyboard(_activity, _body);
            }
        });
        _onCancelInternalAdd.add(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog) {
                AppUtil.hideKeyboard(_activity, _body);
                dismiss();
            }
        });
    }


    //////////// Accessors ////////////

    public       String text()          { return _textInput.getText().toString(); }
    public     EditText textInput()     { return _textInput;                      }
    public       String textInputHint() { return _textInputHint;                  }
    public LinearLayout linearLayout()  { return _body;                           }


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
    }

    public void setText(@StringRes int textId) {
        try {
            setText(_activity.getResources().getString(textId));
        } catch (Resources.NotFoundException e) {
            Log.w(this.getClass().getName(), _activity.getResources().getString(R.string.err_resource_not_found));
        }
    }

    public void setTextInputHint(String textInputHint) {
        if (textInputHint != null) {
            _textInputHint = textInputHint;
            _textInput.setHint(_textInputHint);
        }
    }

    public void setTextInputHint(@StringRes int textInputHintId) {
        try {
            setTextInputHint(_activity.getResources().getString(textInputHintId));
        } catch (Resources.NotFoundException e) {
            Log.w(this.getClass().getName(), _activity.getResources().getString(R.string.err_resource_not_found));
        }
    }

    @Override
    public void show() {
        if (_onShow == null) {
            _onShow = new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    AppUtil.showKeyboard(_activity, _textInput);
                }
            };
        }
        super.show();
        _textInput.requestFocus();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(100);
                    AppUtil.showKeyboard(_activity, _textInput);
                } catch (InterruptedException e) {
                    AppUtil.showKeyboard(_activity, _textInput);
                }
            }
        }).start();
    }
}
