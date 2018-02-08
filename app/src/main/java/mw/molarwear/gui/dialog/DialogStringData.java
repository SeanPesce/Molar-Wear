package mw.molarwear.gui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;

import mw.molarwear.R;

/**
 * This class is a container for holding basic dialog strings (window title, message, button texts).
 *
 * @author Sean Pesce
 *
 * @see    AlertDialog
 * @see    TwoButtonDialog
 */

public class DialogStringData {

    private final Activity _activity;

    private String _title, _message, _posBtText, _negBtText;


    //////////// Constructors ////////////

    public DialogStringData(Activity activity) {
        _activity  = activity;
        _title     = getDefaultTitle(_activity);
        _message   = getDefaultMessage(_activity);
        _posBtText = getDefaultPosBtText(_activity);
        _negBtText = getDefaultNegBtText(_activity);
    }

    public DialogStringData(Activity activity, String title) {
        _activity  = activity;
        _title     = (title != null) ? title : "";
        _message   = getDefaultMessage(_activity);
        _posBtText = getDefaultPosBtText(_activity);
        _negBtText = getDefaultNegBtText(_activity);
    }

    public DialogStringData(Activity activity, int titleId) {
        _activity  = activity;
        try { _title = _activity.getResources().getString(titleId); } catch (Resources.NotFoundException e) { _title = getDefaultTitle(_activity); }
        _message   = getDefaultMessage(_activity);
        _posBtText = getDefaultPosBtText(_activity);
        _negBtText = getDefaultNegBtText(_activity);
    }

    public DialogStringData(Activity activity, String title, String message) {
        _activity  = activity;
        _title     = (title   != null) ? title   : "";
        _message   = (message != null) ? message : "";
        _posBtText = getDefaultPosBtText(_activity);
        _negBtText = getDefaultNegBtText(_activity);
    }

    public DialogStringData(Activity activity, int titleId, String message) {
        _activity  = activity;
        try { _title = _activity.getResources().getString(titleId); } catch (Resources.NotFoundException e) { _title = getDefaultTitle(_activity); }
        _message   = (message != null) ? message : "";
        _posBtText = getDefaultPosBtText(_activity);
        _negBtText = getDefaultNegBtText(_activity);
    }

    public DialogStringData(Activity activity, String title, int messageId) {
        _activity  = activity;
        _title     = (title != null) ? title : "";
        try { _message = _activity.getResources().getString(messageId); } catch (Resources.NotFoundException e) { _message = getDefaultMessage(_activity); }
        _posBtText = getDefaultPosBtText(_activity);
        _negBtText = getDefaultNegBtText(_activity);
    }

    public DialogStringData(Activity activity, int titleId, int messageId) {
        _activity  = activity;
        try { _title   = _activity.getResources().getString(titleId);   } catch (Resources.NotFoundException e) { _title   = getDefaultTitle(_activity);   }
        try { _message = _activity.getResources().getString(messageId); } catch (Resources.NotFoundException e) { _message = getDefaultMessage(_activity); }
        _posBtText = getDefaultPosBtText(_activity);
        _negBtText = getDefaultNegBtText(_activity);
    }

    public DialogStringData(Activity activity, String title, String message, String posBtText, String negBtText) {
        _activity  = activity;
        _title     = (title     != null) ? title     : "";
        _message   = (message   != null) ? message   : "";
        _posBtText = (posBtText != null) ? posBtText : "";
        _negBtText = (negBtText != null) ? negBtText : "";
    }

    public DialogStringData(Activity activity, int titleId, int messageId, String posBtText, String negBtText) {
        _activity  = activity;
        try { _title   = _activity.getResources().getString(titleId);   } catch (Resources.NotFoundException e) { _title   = getDefaultTitle(_activity);   }
        try { _message = _activity.getResources().getString(messageId); } catch (Resources.NotFoundException e) { _message = getDefaultMessage(_activity); }
        _posBtText = (posBtText != null) ? posBtText : "";
        _negBtText = (negBtText != null) ? negBtText : "";
    }

    public DialogStringData(Activity activity, String title, int messageId, String posBtText, String negBtText) {
        _activity  = activity;
        _title     = (title != null)     ? title     : "";
        try { _message = _activity.getResources().getString(messageId); } catch (Resources.NotFoundException e) { _message = getDefaultMessage(_activity); }
        _posBtText = (posBtText != null) ? posBtText : "";
        _negBtText = (negBtText != null) ? negBtText : "";
    }

    public DialogStringData(Activity activity, int titleId, String message, String posBtText, String negBtText) {
        _activity  = activity;
        try { _title   = _activity.getResources().getString(titleId);   } catch (Resources.NotFoundException e) { _title   = getDefaultTitle(_activity);   }
        _message   = (message   != null) ? message   : "";
        _posBtText = (posBtText != null) ? posBtText : "";
        _negBtText = (negBtText != null) ? negBtText : "";
    }

    public DialogStringData(Activity activity, int titleId, int messageId, int posBtTextId, String negBtText) {
        _activity  = activity;
        try { _title     = _activity.getResources().getString(titleId);     } catch (Resources.NotFoundException e) { _title     = getDefaultTitle(_activity);     }
        try { _message   = _activity.getResources().getString(messageId);   } catch (Resources.NotFoundException e) { _message   = getDefaultMessage(_activity);   }
        try { _posBtText = _activity.getResources().getString(posBtTextId); } catch (Resources.NotFoundException e) { _posBtText = getDefaultPosBtText(_activity); }
        _negBtText = (negBtText != null) ? negBtText : "";
    }

    public DialogStringData(Activity activity, int titleId, int messageId, String posBtText, int negBtTextId) {
        _activity  = activity;
        try { _title     = _activity.getResources().getString(titleId);     } catch (Resources.NotFoundException e) { _title     = getDefaultTitle(_activity);     }
        try { _message   = _activity.getResources().getString(messageId);   } catch (Resources.NotFoundException e) { _message   = getDefaultMessage(_activity);   }
        _posBtText = (posBtText != null) ? posBtText : "";
        try { _negBtText = _activity.getResources().getString(negBtTextId); } catch (Resources.NotFoundException e) { _negBtText = getDefaultNegBtText(_activity); }
    }

    public DialogStringData(Activity activity, String title, String message, int posBtTextId, int negBtTextId) {
        _activity  = activity;
        _title     = (title   != null) ? title   : "";
        _message   = (message != null) ? message : "";
        try { _posBtText = _activity.getResources().getString(posBtTextId); } catch (Resources.NotFoundException e) { _posBtText = getDefaultPosBtText(_activity); }
        try { _negBtText = _activity.getResources().getString(negBtTextId); } catch (Resources.NotFoundException e) { _negBtText = getDefaultNegBtText(_activity); }
    }

    public DialogStringData(Activity activity, int titleId, String message, int posBtTextId, int negBtTextId) {
        _activity  = activity;
        try { _title     = _activity.getResources().getString(titleId);     } catch (Resources.NotFoundException e) { _title     = getDefaultTitle(_activity);     }
        _message   = (message != null) ? message : "";
        try { _posBtText = _activity.getResources().getString(posBtTextId); } catch (Resources.NotFoundException e) { _posBtText = getDefaultPosBtText(_activity); }
        try { _negBtText = _activity.getResources().getString(negBtTextId); } catch (Resources.NotFoundException e) { _negBtText = getDefaultNegBtText(_activity); }
    }

    public DialogStringData(Activity activity, String title, int messageId, int posBtTextId, int negBtTextId) {
        _activity  = activity;
        _title     = (title   != null) ? title   : "";
        try { _message   = _activity.getResources().getString(messageId);   } catch (Resources.NotFoundException e) { _message   = getDefaultMessage(_activity);   }
        try { _posBtText = _activity.getResources().getString(posBtTextId); } catch (Resources.NotFoundException e) { _posBtText = getDefaultPosBtText(_activity); }
        try { _negBtText = _activity.getResources().getString(negBtTextId); } catch (Resources.NotFoundException e) { _negBtText = getDefaultNegBtText(_activity); }
    }

    public DialogStringData(Activity activity, int titleId, int messageId, int posBtTextId, int negBtTextId) {
        _activity  = activity;
        try { _title     = _activity.getResources().getString(titleId);     } catch (Resources.NotFoundException e) { _title     = getDefaultTitle(_activity);     }
        try { _message   = _activity.getResources().getString(messageId);   } catch (Resources.NotFoundException e) { _message   = getDefaultMessage(_activity);   }
        try { _posBtText = _activity.getResources().getString(posBtTextId); } catch (Resources.NotFoundException e) { _posBtText = getDefaultPosBtText(_activity); }
        try { _negBtText = _activity.getResources().getString(negBtTextId); } catch (Resources.NotFoundException e) { _negBtText = getDefaultNegBtText(_activity); }
    }


    //////////// Accessors ////////////

    public Activity activity()  { return _activity;  }
    public Context  context()   { return _activity;  }
    public String   title()     { return _title;     }
    public String   message()   { return _message;   }
    public String   posBtText() { return _posBtText; }
    public String   negBtText() { return _negBtText; }



    //////////// Mutators ////////////

    public void setTitle(String title)         { _title     = (title != null)     ? title     : _title;     }
    public void setMessage(String message)     { _message   = (message != null)   ? message   : _message;   }
    public void setPosBtText(String posBtText) { _posBtText = (posBtText != null) ? posBtText : _posBtText; }
    public void setNegBtText(String negBtText) { _negBtText = (negBtText != null) ? negBtText : _negBtText; }

    public void setTitle(int titleId)         { try { _title     = _activity.getResources().getString(titleId);     } catch (Resources.NotFoundException e) {} }
    public void setMessage(int messageId)     { try { _message   = _activity.getResources().getString(messageId);   } catch (Resources.NotFoundException e) {} }
    public void setPosBtText(int posBtTextId) { try { _posBtText = _activity.getResources().getString(posBtTextId); } catch (Resources.NotFoundException e) {} }
    public void setNegBtText(int negBtTextId) { try { _negBtText = _activity.getResources().getString(negBtTextId); } catch (Resources.NotFoundException e) {} }



    //////////// Static Accessors ////////////
    private static String getDefaultTitle(Activity activity)     { return activity.getResources().getString(R.string.default_dlg_title);      }
    private static String getDefaultMessage(Activity activity)   { return activity.getResources().getString(R.string.default_dlg_msg);        }
    private static String getDefaultPosBtText(Activity activity) { return activity.getResources().getString(R.string.default_dlg_bt_pos_txt); }
    private static String getDefaultNegBtText(Activity activity) { return activity.getResources().getString(R.string.default_dlg_bt_neg_txt); }
}
