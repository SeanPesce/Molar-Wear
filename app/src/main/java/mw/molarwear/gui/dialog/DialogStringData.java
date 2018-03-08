package mw.molarwear.gui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;

/**
 * This class is a container for holding basic dialog strings (window title, message, button texts).
 *
 * @author Sean Pesce
 *
 * @see    AlertDialog
 * @see    TwoButtonDialog
 */

public class DialogStringData {

    public static String DEFAULT_TITLE      = "",
                         DEFAULT_MESSAGE    = "",
                         DEFAULT_POS_BT_TXT = "",
                         DEFAULT_NEG_BT_TXT = "";

    private final Activity _activity;

    private String _title, _message, _posBtText, _negBtText;


    //////////// Constructors ////////////

    public DialogStringData(Activity activity) {
        _activity  = activity;
        _title     = DEFAULT_TITLE;
        _message   = DEFAULT_MESSAGE;
        _posBtText = DEFAULT_POS_BT_TXT;
        _negBtText = DEFAULT_NEG_BT_TXT;
    }

    public DialogStringData(Activity activity, String title) {
        _activity  = activity;
        _title     = (title != null) ? title : "";
        _message   = DEFAULT_MESSAGE;
        _posBtText = DEFAULT_POS_BT_TXT;
        _negBtText = DEFAULT_NEG_BT_TXT;
    }

    public DialogStringData(Activity activity, @StringRes int titleId) {
        _activity  = activity;
        try { _title = _activity.getResources().getString(titleId); } catch (Resources.NotFoundException e) { _title = DEFAULT_TITLE; }
        _message   = DEFAULT_MESSAGE;
        _posBtText = DEFAULT_POS_BT_TXT;
        _negBtText = DEFAULT_NEG_BT_TXT;
    }

    public DialogStringData(Activity activity, String title, String message) {
        _activity  = activity;
        _title     = (title   != null) ? title   : "";
        _message   = (message != null) ? message : "";
        _posBtText = DEFAULT_POS_BT_TXT;
        _negBtText = DEFAULT_NEG_BT_TXT;
    }

    public DialogStringData(Activity activity, @StringRes int titleId, String message) {
        _activity  = activity;
        try { _title = _activity.getResources().getString(titleId); } catch (Resources.NotFoundException e) { _title = DEFAULT_TITLE; }
        _message   = (message != null) ? message : "";
        _posBtText = DEFAULT_POS_BT_TXT;
        _negBtText = DEFAULT_NEG_BT_TXT;
    }

    public DialogStringData(Activity activity, String title, @StringRes int messageId) {
        _activity  = activity;
        _title     = (title != null) ? title : "";
        try { _message = _activity.getResources().getString(messageId); } catch (Resources.NotFoundException e) { _message = DEFAULT_MESSAGE; }
        _posBtText = DEFAULT_POS_BT_TXT;
        _negBtText = DEFAULT_NEG_BT_TXT;
    }

    public DialogStringData(Activity activity, @StringRes int titleId, @StringRes int messageId) {
        _activity  = activity;
        try { _title   = _activity.getResources().getString(titleId);   } catch (Resources.NotFoundException e) { _title   = DEFAULT_TITLE;   }
        try { _message = _activity.getResources().getString(messageId); } catch (Resources.NotFoundException e) { _message = DEFAULT_MESSAGE; }
        _posBtText = DEFAULT_POS_BT_TXT;
        _negBtText = DEFAULT_NEG_BT_TXT;
    }

    public DialogStringData(Activity activity, String title, String message, String posBtText, String negBtText) {
        _activity  = activity;
        _title     = (title     != null) ? title     : "";
        _message   = (message   != null) ? message   : "";
        _posBtText = (posBtText != null) ? posBtText : "";
        _negBtText = (negBtText != null) ? negBtText : "";
    }

    public DialogStringData(Activity activity, @StringRes int titleId, @StringRes int messageId, String posBtText, String negBtText) {
        _activity  = activity;
        try { _title   = _activity.getResources().getString(titleId);   } catch (Resources.NotFoundException e) { _title   = DEFAULT_TITLE;   }
        try { _message = _activity.getResources().getString(messageId); } catch (Resources.NotFoundException e) { _message = DEFAULT_MESSAGE; }
        _posBtText = (posBtText != null) ? posBtText : "";
        _negBtText = (negBtText != null) ? negBtText : "";
    }

    public DialogStringData(Activity activity, String title, @StringRes int messageId, String posBtText, String negBtText) {
        _activity  = activity;
        _title     = (title != null)     ? title     : "";
        try { _message = _activity.getResources().getString(messageId); } catch (Resources.NotFoundException e) { _message = DEFAULT_MESSAGE; }
        _posBtText = (posBtText != null) ? posBtText : "";
        _negBtText = (negBtText != null) ? negBtText : "";
    }

    public DialogStringData(Activity activity, @StringRes int titleId, String message, String posBtText, String negBtText) {
        _activity  = activity;
        try { _title   = _activity.getResources().getString(titleId);   } catch (Resources.NotFoundException e) { _title   = DEFAULT_TITLE;   }
        _message   = (message   != null) ? message   : "";
        _posBtText = (posBtText != null) ? posBtText : "";
        _negBtText = (negBtText != null) ? negBtText : "";
    }

    public DialogStringData(Activity activity, @StringRes int titleId, @StringRes int messageId, @StringRes int posBtTextId, String negBtText) {
        _activity  = activity;
        try { _title     = _activity.getResources().getString(titleId);     } catch (Resources.NotFoundException e) { _title     = DEFAULT_TITLE;     }
        try { _message   = _activity.getResources().getString(messageId);   } catch (Resources.NotFoundException e) { _message   = DEFAULT_MESSAGE;   }
        try { _posBtText = _activity.getResources().getString(posBtTextId); } catch (Resources.NotFoundException e) { _posBtText = DEFAULT_POS_BT_TXT; }
        _negBtText = (negBtText != null) ? negBtText : "";
    }

    public DialogStringData(Activity activity, @StringRes int titleId, @StringRes int messageId, String posBtText, @StringRes int negBtTextId) {
        _activity  = activity;
        try { _title     = _activity.getResources().getString(titleId);     } catch (Resources.NotFoundException e) { _title     = DEFAULT_TITLE;     }
        try { _message   = _activity.getResources().getString(messageId);   } catch (Resources.NotFoundException e) { _message   = DEFAULT_MESSAGE;   }
        _posBtText = (posBtText != null) ? posBtText : "";
        try { _negBtText = _activity.getResources().getString(negBtTextId); } catch (Resources.NotFoundException e) { _negBtText = DEFAULT_NEG_BT_TXT; }
    }

    public DialogStringData(Activity activity, String title, String message, @StringRes int posBtTextId, @StringRes int negBtTextId) {
        _activity  = activity;
        _title     = (title   != null) ? title   : "";
        _message   = (message != null) ? message : "";
        try { _posBtText = _activity.getResources().getString(posBtTextId); } catch (Resources.NotFoundException e) { _posBtText = DEFAULT_POS_BT_TXT; }
        try { _negBtText = _activity.getResources().getString(negBtTextId); } catch (Resources.NotFoundException e) { _negBtText = DEFAULT_NEG_BT_TXT; }
    }

    public DialogStringData(Activity activity, @StringRes int titleId, String message, @StringRes int posBtTextId, @StringRes int negBtTextId) {
        _activity  = activity;
        try { _title     = _activity.getResources().getString(titleId);     } catch (Resources.NotFoundException e) { _title     = DEFAULT_TITLE;     }
        _message   = (message != null) ? message : "";
        try { _posBtText = _activity.getResources().getString(posBtTextId); } catch (Resources.NotFoundException e) { _posBtText = DEFAULT_POS_BT_TXT; }
        try { _negBtText = _activity.getResources().getString(negBtTextId); } catch (Resources.NotFoundException e) { _negBtText = DEFAULT_NEG_BT_TXT; }
    }

    public DialogStringData(Activity activity, String title, @StringRes int messageId, @StringRes int posBtTextId, @StringRes int negBtTextId) {
        _activity  = activity;
        _title     = (title   != null) ? title   : "";
        try { _message   = _activity.getResources().getString(messageId);   } catch (Resources.NotFoundException e) { _message   = DEFAULT_MESSAGE;   }
        try { _posBtText = _activity.getResources().getString(posBtTextId); } catch (Resources.NotFoundException e) { _posBtText = DEFAULT_POS_BT_TXT; }
        try { _negBtText = _activity.getResources().getString(negBtTextId); } catch (Resources.NotFoundException e) { _negBtText = DEFAULT_NEG_BT_TXT; }
    }

    public DialogStringData(Activity activity, @StringRes int titleId, @StringRes int messageId, @StringRes int posBtTextId, @StringRes int negBtTextId) {
        _activity  = activity;
        try { _title     = _activity.getResources().getString(titleId);     } catch (Resources.NotFoundException e) { _title     = DEFAULT_TITLE;     }
        try { _message   = _activity.getResources().getString(messageId);   } catch (Resources.NotFoundException e) { _message   = DEFAULT_MESSAGE;   }
        try { _posBtText = _activity.getResources().getString(posBtTextId); } catch (Resources.NotFoundException e) { _posBtText = DEFAULT_POS_BT_TXT; }
        try { _negBtText = _activity.getResources().getString(negBtTextId); } catch (Resources.NotFoundException e) { _negBtText = DEFAULT_NEG_BT_TXT; }
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

    public void setTitle(@StringRes int titleId)         { try { _title     = _activity.getResources().getString(titleId);     } catch (Resources.NotFoundException e) {} }
    public void setMessage(@StringRes int messageId)     { try { _message   = _activity.getResources().getString(messageId);   } catch (Resources.NotFoundException e) {} }
    public void setPosBtText(@StringRes int posBtTextId) { try { _posBtText = _activity.getResources().getString(posBtTextId); } catch (Resources.NotFoundException e) {} }
    public void setNegBtText(@StringRes int negBtTextId) { try { _negBtText = _activity.getResources().getString(negBtTextId); } catch (Resources.NotFoundException e) {} }
}
