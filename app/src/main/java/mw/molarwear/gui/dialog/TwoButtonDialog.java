package mw.molarwear.gui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import mw.molarwear.R;

/**
 * This class is a basic dialog box with two buttons, a title, and a message by default. It can be
 *  used as is, but is meant to be extended (for an example, see {@link TextInputDialog}).
 *
 * @author Sean Pesce
 *
 * @see    AlertDialog
 * @see    AlertDialog.Builder
 * @see    DialogStringData
 * @see    TextInputDialog
 */

public class TwoButtonDialog {

    // GUI
    protected final            Activity _activity;
    protected final AlertDialog.Builder _builder;
    protected final                View _layout;

    protected               AlertDialog _dialog;


    protected String _title     = DialogStringData.DEFAULT_TITLE,
                     _message   = DialogStringData.DEFAULT_MESSAGE,
                     _posBtText = DialogStringData.DEFAULT_POS_BT_TXT,
                     _negBtText = DialogStringData.DEFAULT_NEG_BT_TXT;

    protected DialogInterface.OnClickListener _posBtClickListener = null, _negBtClickListener = null;
    protected DialogInterface.OnShowListener  _onShowListener     = null;

    //////////// Constructors ////////////

    public TwoButtonDialog(Activity activity) {
        _activity = activity;
        _builder  = new AlertDialog.Builder(_activity);
        _layout   = null;
        setStrings(new DialogStringData(_activity));
    }

    public TwoButtonDialog(Activity activity, DialogStringData strings) {
        _activity = activity;
        _builder  = new AlertDialog.Builder(_activity);
        _layout   = null;
        setStrings(strings);
    }

    public TwoButtonDialog(DialogStringData strings) {
        _activity = strings.activity();
        _builder  = new AlertDialog.Builder(_activity);
        _layout   = null;
        setStrings(strings);
    }

    public TwoButtonDialog(Activity activity, AlertDialog.Builder builder) {
        _activity = activity;
        _builder  = builder;
        _layout   = null;
        setStrings(new DialogStringData(_activity));
    }

    public TwoButtonDialog(Activity activity, View layout) {
        _activity = activity;
        _builder  = new AlertDialog.Builder(_activity);
        _layout   = layout;
        setStrings(new DialogStringData(_activity));
    }

    public TwoButtonDialog(Activity activity, int layoutId) {
        _activity = activity;
        _builder  = new AlertDialog.Builder(_activity);
        _layout   = _activity.getLayoutInflater().inflate(layoutId, null);
        setStrings(new DialogStringData(_activity));
    }

    public TwoButtonDialog(Activity activity, DialogStringData strings, AlertDialog.Builder builder) {
        _activity = activity;
        _builder  = builder;
        _layout   = null;
        setStrings(strings);
    }

    public TwoButtonDialog(DialogStringData strings, AlertDialog.Builder builder) {
        _activity = strings.activity();
        _builder  = builder;
        _layout   = null;
        setStrings(strings);
    }

    public TwoButtonDialog(Activity activity, DialogStringData strings, View layout) {
        _activity = activity;
        _builder  = new AlertDialog.Builder(_activity);
        _layout   = layout;
        setStrings(strings);
    }

    public TwoButtonDialog(Activity activity, DialogStringData strings, int layoutId) {
        _activity = activity;
        _builder  = new AlertDialog.Builder(_activity);
        _layout   = _activity.getLayoutInflater().inflate(layoutId, null);
        setStrings(strings);
    }

    public TwoButtonDialog(DialogStringData strings, View layout) {
        _activity = strings.activity();
        _builder  = new AlertDialog.Builder(_activity);
        _layout   = layout;
        setStrings(strings);
    }

    public TwoButtonDialog(DialogStringData strings, int layoutId) {
        _activity = strings.activity();
        _builder  = new AlertDialog.Builder(_activity);
        _layout   = _activity.getLayoutInflater().inflate(layoutId, null);
        setStrings(strings);
    }

    public TwoButtonDialog(Activity activity, DialogStringData strings, AlertDialog.Builder builder, View layout) {
        _activity = activity;
        _builder  = builder;
        _layout   = layout;
        setStrings(strings);
    }

    public TwoButtonDialog(Activity activity, DialogStringData strings, AlertDialog.Builder builder, int layoutId) {
        _activity = activity;
        _builder  = builder;
        _layout   = _activity.getLayoutInflater().inflate(layoutId, null);
        setStrings(strings);
    }

    public TwoButtonDialog(DialogStringData strings, AlertDialog.Builder builder, View layout) {
        _activity = strings.activity();
        _builder  = builder;
        _layout   = layout;
        setStrings(strings);
    }

    public TwoButtonDialog(DialogStringData strings, AlertDialog.Builder builder, int layoutId) {
        _activity = strings.activity();
        _builder  = builder;
        _layout   = _activity.getLayoutInflater().inflate(layoutId, null);
        setStrings(strings);
    }

    public TwoButtonDialog(Activity activity, AlertDialog.Builder builder, View layout) {
        _activity = activity;
        _builder  = builder;
        _layout   = layout;
        setStrings(new DialogStringData(_activity));
    }

    public TwoButtonDialog(Activity activity, AlertDialog.Builder builder, int layoutId) {
        _activity = activity;
        _builder  = builder;
        _layout   = _activity.getLayoutInflater().inflate(layoutId, null);
        setStrings(new DialogStringData(_activity));
    }



    protected void updateBuilder() {
        _builder.setTitle(_title).setMessage(_message);
        _builder.setPositiveButton(_posBtText, _posBtClickListener);
        _builder.setNegativeButton(_negBtText, _negBtClickListener);
        updateDialog();
    }

    protected void updateDialog() {
        _dialog = _builder.create();
        _dialog.setOnShowListener(_onShowListener);
    }


    public void show() {
        if (_layout != null) {
            if (_layout.getParent() != null) {
                ((ViewGroup) _layout.getParent()).removeView(_layout);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                _builder.setView(_layout);
            } else {
                throw new UnsupportedOperationException(_activity.getResources().getString(R.string.err_unsupported_os_new_proj_dlg));
            }
        }
        _builder.show();
    }


    //////////// Accessors ////////////

    public Activity activity()  { return _activity;  }
    public Context  context()   { return _activity;  }
    public String   title()     { return _title;     }
    public String   message()   { return _message;   }
    public String   posBtText() { return _posBtText; }
    public String   negBtText() { return _negBtText; }
    public View     layout()    { return _layout;    }
    public AlertDialog dialog() { return _dialog;    }
    public AlertDialog.Builder builder() { return _builder; }
    public DialogStringData    strings() { return new DialogStringData(_activity, _title, _message, _posBtText, _negBtText); }
    public DialogInterface.OnClickListener posBtClickListener() { return _posBtClickListener; }
    public DialogInterface.OnClickListener negBtClickListener() { return _negBtClickListener; }
    public DialogInterface.OnShowListener  onShowListener()     { return _onShowListener;     }


    //////////// Mutators ////////////

    public void setTitle(String title) {
        if (title != null) {
            _title = title;
            _builder.setTitle(_title);
            _dialog = _builder.create();
        }
    }

    public void setMessage(String message) {
        if (message != null) {
            _message = message;
            _builder.setMessage(_message);
            _dialog = _builder.create();
        }
    }

    public void setPositiveButton(String posBtText) {
        if (posBtText != null) {
            _posBtText = posBtText;
            _builder.setPositiveButton(_posBtText, _posBtClickListener);
            _dialog = _builder.create();
        }
    }

    public void setNegativeButton(String negBtText) {
        if (negBtText != null) {
            _negBtText = negBtText;
            _builder.setNegativeButton(_negBtText, _negBtClickListener);
            _dialog = _builder.create();
        }
    }

    public void setPositiveButton(DialogInterface.OnClickListener posBtClickListener) {
        _posBtClickListener = posBtClickListener;
        _builder.setPositiveButton(_posBtText, _posBtClickListener);
        _dialog = _builder.create();
    }

    public void setNegativeButton(DialogInterface.OnClickListener negBtClickListener) {
        _negBtClickListener = negBtClickListener;
        _builder.setNegativeButton(_negBtText, _negBtClickListener);
        _dialog = _builder.create();
    }

    public void setPositiveButton(String posBtText, DialogInterface.OnClickListener posBtClickListener) {
        if (posBtText != null) {
            _posBtText = posBtText;
        }
        _posBtClickListener = posBtClickListener;
        _builder.setPositiveButton(_posBtText, _posBtClickListener);
        _dialog = _builder.create();
    }

    public void setNegativeButton(String negBtText, DialogInterface.OnClickListener negBtClickListener) {
        if (negBtText != null) {
            _negBtText = negBtText;
        }
        _negBtClickListener = negBtClickListener;
        _builder.setNegativeButton(_negBtText, _negBtClickListener);
        _dialog = _builder.create();
    }

    public void setTitle(int titleId) {
        try {
            setTitle(_activity.getResources().getString(titleId));
        } catch (Resources.NotFoundException e) {}
    }

    public void setMessage(int messageId) {
        try {
            setMessage(_activity.getResources().getString(messageId));
        } catch (Resources.NotFoundException e) {}
    }

    public void setPositiveButton(int posBtTextId) {
        try {
            setPositiveButton(_activity.getResources().getString(posBtTextId));
        } catch (Resources.NotFoundException e) {}
    }

    public void setNegativeButton(int negBtTextId) {
        try {
            setNegativeButton(_activity.getResources().getString(negBtTextId));
        } catch (Resources.NotFoundException e) {}
    }

    public void setPositiveButton(int posBtTextId, DialogInterface.OnClickListener posBtClickListener) {
        try {
            setPositiveButton(_activity.getResources().getString(posBtTextId), posBtClickListener);
        } catch (Resources.NotFoundException e) {}
    }

    public void setNegativeButton(int negBtTextId, DialogInterface.OnClickListener negBtClickListener) {
        try {
            setNegativeButton(_activity.getResources().getString(negBtTextId), negBtClickListener);
        } catch (Resources.NotFoundException e) {}
    }

    public void setOnShowListener(DialogInterface.OnShowListener onShowListener) {
        _onShowListener = onShowListener;
        _dialog.setOnShowListener(_onShowListener);
    }

    public void setStrings(DialogStringData strings) {
        if (strings != null) {
            boolean update = false;
            if (strings.title() != null) {
                _title = strings.title();
                _builder.setTitle(_title);
            }
            if (strings.message() != null) {
                _message = strings.message();
                _builder.setMessage(_message);
            }
            if (strings.posBtText() != null) {
                _posBtText = strings.posBtText();
                _builder.setPositiveButton(_posBtText, _posBtClickListener);
            }
            if (strings.negBtText() != null) {
                _negBtText = strings.negBtText();
                _builder.setNegativeButton(_negBtText, _negBtClickListener);
            }
            updateDialog();
        }
    }
}
