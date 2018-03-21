package mw.molarwear.util;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.annotation.BoolRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.data.classes.dental.molar.Molar;
import mw.molarwear.data.classes.dental.molar.enums.Wear;
import mw.molarwear.gui.activity.SplashActivity;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.PreferencesDialog;
import mw.molarwear.gui.dialog.TextInputDialog;
import mw.molarwear.gui.dialog.TwoButtonDialog;
import mw.molarwear.gui.dialog.WearPickerDialog;

/**
 * Utility class for handling global app data.
 *
 * @author Sean Pesce
 */

public class AppUtility {

    private static final String _LOG_FILE = "MolWear.log";

    public static boolean OVERWRITE_LOG_FILE = false;
    public static int DEFAULT_TOAST_DURATION = Toast.LENGTH_LONG;

    // Startup data
    private static final ArrayList<String> _STARTUP_LOG = new ArrayList<>();
    private static             int _STARTUP_LOAD_ERRORS = 0;

    // Note: All methods and member data are static; this instance just allows for aliasing
    private static final          AppUtility _INSTANCE      = new AppUtility();

    public  static Activity CONTEXT = null;
    public  static     View VIEW    = null;

    public  static SharedPreferences PREFERENCES     = null;
    private static           boolean _PREF_AUTO_SAVE = true;


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

    private static final Resources _res(@NonNull Context context) {
        return context.getResources();
    }

    private static final boolean BOOLEAN(@BoolRes    int booleanId) { return _res().getBoolean(booleanId); }
    private static final     int INT    (@IntegerRes int intId    ) { return _res().getInteger(intId);     }
    private static final  String STRING (@StringRes  int stringId ) { return _res().getString (stringId);  }

    private static final boolean BOOLEAN(@NonNull Context context, @BoolRes    int booleanId) { return _res(context).getBoolean(booleanId); }
    private static final     int INT    (@NonNull Context context, @IntegerRes int intId    ) { return _res(context).getInteger(intId);     }
    private static final  String STRING (@NonNull Context context, @StringRes  int stringId ) { return _res(context).getString (stringId);  }

    public static final AppUtility APP() { return _INSTANCE; }
    public static final AppUtility get() { return _INSTANCE; }

    public static final Resources  getResources()    { return _res();          }

    public static final boolean    getPrefAutoSave() { return _PREF_AUTO_SAVE; }

    public static final void initializeRuntimeSettings() {

        _ERR_NO_CONTEXT = STRING(R.string.err_app_util_context_null);
        _ERR_NO_VIEW    = STRING(R.string.err_app_util_view_null);

        OVERWRITE_LOG_FILE = BOOLEAN(R.bool.rt_cfg_overwrite_log);
        if (OVERWRITE_LOG_FILE) {
            FileUtility.writeText(_LOG_FILE, "", false, true);
        }

        // Default app preferences
        DEFAULT_TOAST_DURATION = BOOLEAN(R.bool.rt_cfg_default_long_toast_duration) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        _PREF_AUTO_SAVE = BOOLEAN(R.bool.rt_cfg_pref_default_auto_save);

        // Molar wear score descriptions
        Wear.UNKNOWN.setDescription(STRING(R.string.desc_wear_lvl_unk));
        Wear.ZERO.setDescription(STRING(R.string.desc_wear_lvl_0));
        Wear.ONE.setDescription(STRING(R.string.desc_wear_lvl_1));
        Wear.TWO.setDescription(STRING(R.string.desc_wear_lvl_2));
        Wear.THREE.setDescription(STRING(R.string.desc_wear_lvl_3));
        Wear.FOUR.setDescription(STRING(R.string.desc_wear_lvl_4));
        Wear.FIVE.setDescription(STRING(R.string.desc_wear_lvl_5));
        Wear.SIX.setDescription(STRING(R.string.desc_wear_lvl_6));
        Wear.SEVEN.setDescription(STRING(R.string.desc_wear_lvl_7));
        Wear.EIGHT.setDescription(STRING(R.string.desc_wear_lvl_8));

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

        WearPickerDialog.UNKNOWN_SCORE_DESC = STRING(R.string.desc_wear_lvl_unk_picker);
    }

    public static final void loadPreferences() {
        loadPreferences(CONTEXT);
    }

    public static final void loadPreferences(@NonNull Activity activity) {
        CONTEXT = activity;
        PREFERENCES = activity.getSharedPreferences("_PREFS", Context.MODE_PRIVATE);
        _PREF_AUTO_SAVE = PREFERENCES.getBoolean(STRING(R.string.rt_cfg_pref_key_auto_save), _PREF_AUTO_SAVE);
    }

    public static final void hideKeyboard(@NonNull View view) {
        hideKeyboard(CONTEXT, view.getWindowToken());
    }

    public static final void hideKeyboard(@NonNull IBinder windowToken) {
        hideKeyboard(CONTEXT, windowToken);
    }

    public static final void hideKeyboard(@NonNull Fragment fragment) {
        hideKeyboard(fragment.getContext(), fragment.getView().getRootView().getWindowToken());
    }

    public static final void hideKeyboard(@NonNull Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        hideKeyboard(activity, view.getWindowToken());
    }

    public static final void hideKeyboard(@NonNull Activity activity, @NonNull View view) {
        hideKeyboard(activity, view.getWindowToken());
    }

    public static final void hideKeyboard(@NonNull Activity activity, @NonNull IBinder windowToken) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

    public static final void hideKeyboard(@NonNull Context context, @NonNull IBinder windowToken) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

    public static final void showKeyboard(@NonNull View view) {
        showKeyboard(CONTEXT, view);
    }

    public static final void showKeyboard(@NonNull Activity activity, @NonNull View view) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }


    public static final void featureNotImplementedYet() {
        TwoButtonDialog dialog = new TwoButtonDialog(new DialogStringData(CONTEXT,
                                                        R.string.dlg_title_feature_not_implemented,
                                                        R.string.dlg_msg_feature_not_implemented));
        dialog.show();
    }

    public static final void printSnackBarMsg(@NonNull String msg) {
        if (VIEW != null) {
            printSnackBarMsg(AppUtility.VIEW, msg);
        } else {
            _errorNoView();
        }
    }

    public static final void printSnackBarMsg(@NonNull View view, @NonNull String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    public static final void printSnackBarMsg(@StringRes int msgId) {
        printSnackBarMsg(STRING(msgId));
    }

    public static final void printSnackBarMsg(@NonNull View view, @StringRes int msgId) {
        printSnackBarMsg(view, STRING(msgId));
    }

    public static final Toast printToast(@NonNull String msg) {
        return printToast(msg, DEFAULT_TOAST_DURATION);
    }

    public static final Toast printToast(@StringRes int msgId) {
        return printToast(msgId, DEFAULT_TOAST_DURATION);
    }

    public static final Toast printToast(@NonNull String msg, @BaseTransientBottomBar.Duration int duration) {
        if (CONTEXT != null) {
            return printToast(CONTEXT, msg, duration);
        } else {
            _errorNoContext();
        }
        return null;
    }

    public static final Toast printToast(@StringRes int msgId, @BaseTransientBottomBar.Duration int duration) {
        if (CONTEXT != null) {
            return printToast(CONTEXT, msgId, duration);
        } else {
            _errorNoContext();
        }
        return null;
    }

    public static final Toast printToast(@NonNull Context context, @NonNull String msg) {
        return printToast(context, msg, DEFAULT_TOAST_DURATION);
    }

    public static final Toast printToast(@NonNull Context context, @StringRes int msgId) {
        return printToast(context, STRING(context, msgId), DEFAULT_TOAST_DURATION);
    }

    public static final Toast printToast(@NonNull Context context, @NonNull String msg, @BaseTransientBottomBar.Duration int duration) {
        final Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
        return toast;
    }

    public static final Toast printToast(@NonNull Context context, @StringRes int msgId, @BaseTransientBottomBar.Duration int duration) {
        return printToast(context, STRING(context, msgId), duration);
    }

    public static final void sendKeyPress(final int key) {
        final Instrumentation inst = new Instrumentation();
        new Thread(new Runnable() {
            public void run() {
                inst.sendKeyDownUpSync(key);
            }
        }).start();
    }

    public static final void openPreferencesDialog() {
        openPreferencesDialog(CONTEXT);
    }

    public static final void openPreferencesDialog(@NonNull Activity activity) {
        PreferencesDialog dlg = new PreferencesDialog(activity);
        dlg.show();
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
}
