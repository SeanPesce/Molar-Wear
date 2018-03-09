package mw.molarwear.util;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.BoolRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.ArrayList;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.data.classes.dental.molar.Molar;
import mw.molarwear.gui.activity.MolWearMainActivity;
import mw.molarwear.gui.activity.SplashActivity;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.TextInputDialog;
import mw.molarwear.gui.dialog.TwoButtonDialog;

/**
 * Utility class for handling global app data.
 *
 * @author Sean Pesce
 */

public class AppUtility {

    private static final String _LOG_FILE = "MolWear.log";

    public static boolean OVERWRITE_LOG_FILE = false;

    // Startup data
    private static final ArrayList<String> _STARTUP_LOG = new ArrayList<>();
    private static             int _STARTUP_LOAD_ERRORS = 0;

    // Note: All methods and member data are static; this instance just allows for aliasing
    private static final          AppUtility _INSTANCE      = new AppUtility();

    private static       MolWearMainActivity _MAIN_ACTIVITY = null;

    public  static Activity CONTEXT = null;
    public  static     View VIEW    = null;


    private AppUtility() {

    }

    private static String _ERR_NO_CONTEXT = "AppUtility.CONTEXT was null";
    private static String _ERR_NO_VIEW    = "AppUtility.VIEW was null";

    private static final void _errorNoContext() {
        throw new IllegalStateException(_ERR_NO_CONTEXT);
    }

    private static final void _errorNoView() {
        throw new IllegalStateException(_ERR_NO_VIEW);
    }

    private static final Resources _res() {
        if (CONTEXT != null) {
            return CONTEXT.getResources();
        } else {
            _errorNoContext();
        }
        return null;
    }

    private static final boolean BOOLEAN(@BoolRes    int booleanId) { return _res().getBoolean(booleanId); }
    private static final     int INT    (@IntegerRes int intId    ) { return _res().getInteger(intId);     }
    private static final  String STRING (@StringRes  int stringId ) { return _res().getString (stringId);  }

    public static final AppUtility APP() { return _INSTANCE; }
    public static final AppUtility get() { return _INSTANCE; }

    public static final Resources  getResources() { return _res(); }

    public static final void initializeRuntimeSettings() {
        _ERR_NO_CONTEXT = STRING(R.string.err_app_util_context_null);
        _ERR_NO_VIEW    = STRING(R.string.err_app_util_view_null);

        OVERWRITE_LOG_FILE = BOOLEAN(R.bool.rt_cfg_overwrite_log);
        if (OVERWRITE_LOG_FILE) {
            FileUtility.writeText(_LOG_FILE, "", false, true);
        }

        FileUtility.FILE_EXT_SERIALIZED_DATA = STRING(R.string.file_ext_serialized_data);
        FileUtility.FILE_EXT_JSON_DATA       = STRING(R.string.file_ext_json_data);

        SplashActivity.SET_WAIT_TIME(INT(R.integer.rt_cfg_splash_screen_wait_time));

        Molar.ERROR_MSG_INVALID_TYPE = STRING(R.string.err_invalid_type_not_molar);

        MolarWearProject.DEFAULT_TITLE = STRING(R.string.default_project_title);
        MolarWearSubject.DEFAULT_ID    = STRING(R.string.default_subject_id);

        DialogStringData.DEFAULT_TITLE      = STRING(R.string.default_dlg_title);
        DialogStringData.DEFAULT_MESSAGE    = STRING(R.string.default_dlg_msg);
        DialogStringData.DEFAULT_POS_BT_TXT = STRING(R.string.default_dlg_bt_pos_txt);
        DialogStringData.DEFAULT_NEG_BT_TXT = STRING(R.string.default_dlg_bt_neg_txt);

        TextInputDialog.DEFAULT_TEXT_INPUT_HINT = STRING(R.string.default_dlg_txt_input_hint);
    }


    public static final void featureNotImplementedYet() {
        TwoButtonDialog dialog = new TwoButtonDialog(new DialogStringData(CONTEXT,
                                                        R.string.dlg_title_feature_not_implemented,
                                                        R.string.dlg_msg_feature_not_implemented));
        dialog.show();
    }

    public static final void printSnackBarMsg(String msg) {
        if (VIEW != null) {
            Snackbar.make(AppUtility.VIEW, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            _errorNoView();
        }
    }

    public static final void printSnackBarMsg(@StringRes int msgId) {
        printSnackBarMsg(STRING(msgId));
    }

    public static final void clearLoadErrors() {
        _STARTUP_LOAD_ERRORS = 0;
    }

    public static final void addLoadError() {
        _STARTUP_LOAD_ERRORS++;
    }

    public static final int LOAD_ERROR_COUNT() {
        return _STARTUP_LOAD_ERRORS;
    }

    public static final void log(@NonNull String msg) {
        if (VIEW == null || CONTEXT == null) {
            _STARTUP_LOG.add(msg);
        }
        if (CONTEXT != null) {
            FileUtility.writeText(_LOG_FILE, msg, true, true);
        }
    }

    public static MolWearMainActivity MAIN_ACTIVITY() { return _MAIN_ACTIVITY; }
    public static final void SET_MAIN_ACTIVITY(@NonNull MolWearMainActivity mainActivity) { _MAIN_ACTIVITY = mainActivity; }
}
