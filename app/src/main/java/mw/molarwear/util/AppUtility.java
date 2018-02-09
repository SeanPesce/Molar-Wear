package mw.molarwear.util;

import android.content.Context;
import android.content.res.Resources;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.data.classes.dental.molar.Molar;
import mw.molarwear.gui.activity.SplashActivity;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.TextInputDialog;

/**
 * Utility class for handling global app data.
 *
 * @author Sean Pesce
 */

public class AppUtility {

    // Note: All methods and member data are static; this instance just allows for aliasing
    private static final AppUtility _INSTANCE = new AppUtility();

    public  static Context CONTEXT = null;


    private AppUtility() {

    }

    private static String _ERR_NO_CONTEXT = "AppUtility.CONTEXT was null";

    private static final void _errorNoContext() {
        throw new IllegalStateException(_ERR_NO_CONTEXT);
    }

    private static final Resources _res() {
        if (CONTEXT != null) {
            return CONTEXT.getResources();
        } else {
            _errorNoContext();
        }
        return null;
    }

    private static final boolean BOOLEAN(int booleanId) { return _res().getBoolean(booleanId); }
    private static final     int INT    (int intId    ) { return _res().getInteger(intId);     }
    private static final  String STRING (int stringId ) { return _res().getString (stringId);  }

    public  static final AppUtility APP() { return _INSTANCE; }
    public  static final AppUtility get() { return _INSTANCE; }


    public  static final void initializeRuntimeSettings() {

        _ERR_NO_CONTEXT = STRING(R.string.err_app_util_context_null);

        FileUtility.FILE_EXT_SERIALIZED_DATA = STRING(R.string.file_ext_serialized_data);
        FileUtility.FILE_EXT_JSON_DATA       = STRING(R.string.file_ext_json_data);

        SplashActivity.set_WAIT_TIME(INT(R.integer.rt_cfg_splash_screen_wait_time));

        Molar.ERROR_MSG_INVALID_TYPE = STRING(R.string.err_invalid_type_not_molar);

        MolarWearProject.DEFAULT_TITLE = STRING(R.string.default_project_title);

        DialogStringData.DEFAULT_TITLE      = STRING(R.string.default_dlg_title);
        DialogStringData.DEFAULT_MESSAGE    = STRING(R.string.default_dlg_msg);
        DialogStringData.DEFAULT_POS_BT_TXT = STRING(R.string.default_dlg_bt_pos_txt);
        DialogStringData.DEFAULT_NEG_BT_TXT = STRING(R.string.default_dlg_bt_neg_txt);

        TextInputDialog.DEFAULT_TEXT_INPUT_HINT = STRING(R.string.default_dlg_txt_input_hint);
    }
}
