package mw.molarwear.util;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.annotation.BoolRes;
import android.support.annotation.IntRange;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import mw.molarwear.MolWearApp;
import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.data.classes.MolarWearSubject;
import mw.molarwear.data.classes.dental.molar.Molar;
import mw.molarwear.data.classes.dental.molar.enums.Wear;
import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.gui.activity.SplashActivity;
import mw.molarwear.gui.dialog.DialogStringData;
import mw.molarwear.gui.dialog.MessageDialog;
import mw.molarwear.gui.dialog.PreferencesDialog;
import mw.molarwear.gui.dialog.TextInputDialog;
import mw.molarwear.gui.fragment.MolarDataFragment;

/**
 * Utility class for handling global app data.
 *
 * @author Sean Pesce
 */

public class AppUtil {

    private static final String _LOG_FILE = "MolWear.log";

    public static boolean OVERWRITE_LOG_FILE = false;
    public static int DEFAULT_TOAST_DURATION = Toast.LENGTH_LONG;

    // Startup data
    private static final ArrayList<String> _STARTUP_LOG = new ArrayList<>();
    private static             int _STARTUP_LOAD_ERRORS = 0;


    public  static AppCompatActivity CONTEXT = null;
    public  static              View VIEW    = null;

    public  static SharedPreferences PREFERENCES     = null;
    private static           boolean _PREF_AUTO_SAVE = true;


    private AppUtil() {

    }

    private static String _ERR_NO_CONTEXT = "AppUtil.CONTEXT was null";
    private static String _ERR_NO_VIEW    = "AppUtil.VIEW was null";

    private static void _errorNoContext() {
        throw new IllegalStateException(_ERR_NO_CONTEXT);
    }

    private static void _errorNoView() {
        throw new IllegalStateException(_ERR_NO_VIEW);
    }

    private static Resources _res() {
        if (CONTEXT != null) {
            return CONTEXT.getResources();
        } else {
            _errorNoContext();
        }
        return null;
    }

    private static Resources _res(@NonNull Context context) {
        return context.getResources();
    }

    private static boolean BOOLEAN(@BoolRes    int booleanId) { return _res().getBoolean(booleanId); }
    private static     int INT    (@IntegerRes int intId    ) { return _res().getInteger(intId);     }
    private static  String STRING (@StringRes  int stringId ) { return _res().getString (stringId);  }

    private static boolean BOOLEAN(@NonNull Context context, @BoolRes    int booleanId) { return _res(context).getBoolean(booleanId); }
    private static     int INT    (@NonNull Context context, @IntegerRes int intId    ) { return _res(context).getInteger(intId);     }
    private static  String STRING (@NonNull Context context, @StringRes  int stringId ) { return _res(context).getString (stringId);  }

    public static Resources  getResources()    { return _res();          }

    public static boolean    getPrefAutoSave() { return _PREF_AUTO_SAVE; }

    public static void initializeRuntimeSettings() {

        _ERR_NO_CONTEXT = STRING(R.string.err_app_util_context_null);
        _ERR_NO_VIEW    = STRING(R.string.err_app_util_view_null);

        OVERWRITE_LOG_FILE = BOOLEAN(R.bool.rt_cfg_overwrite_log);
        if (OVERWRITE_LOG_FILE) {
            FileUtil.writeText(_LOG_FILE, "", false, true);
        }

        // Default app preferences
        FileUtil.USE_SYSTEM_FILE_CHOOSER = BOOLEAN(R.bool.rt_cfg_default_use_sys_file_picker);
        DEFAULT_TOAST_DURATION = BOOLEAN(R.bool.rt_cfg_default_long_toast_duration) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        _PREF_AUTO_SAVE = BOOLEAN(R.bool.rt_cfg_pref_default_auto_save);
        MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE = BOOLEAN(R.bool.rt_cfg_pref_default_group_name_ignore_case);

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

        FileUtil.FILE_CHOOSER_TITLE_DEFAULT = STRING(R.string.title_choose_file);
        FileUtil.FILE_EXT_SERIALIZED_DATA   = STRING(R.string.file_ext_serialized_data);
        //FileUtil.FILE_EXT_XML               = STRING(R.string.file_ext_xml);
        FileUtil.FILE_EXT_JSON_DATA         = STRING(R.string.file_ext_json);

        FileUtil.PROJECT_FILE_EXTENSIONS.add(FileUtil.FILE_EXT_CSV);
        FileUtil.PROJECT_FILE_EXTENSIONS.add(FileUtil.FILE_EXT_SERIALIZED_DATA);
        FileUtil.PROJECT_FILE_EXTENSIONS.add(FileUtil.FILE_EXT_JSON_DATA);


        SplashActivity.SET_WAIT_TIME(INT(R.integer.rt_cfg_splash_screen_wait_time));

        Molar.ERROR_MSG_INVALID_TYPE = STRING(R.string.err_invalid_type_not_molar);

        MolarWearProject.DEFAULT_TITLE = STRING(R.string.default_project_title);
        MolarWearSubject.DEFAULT_ID    = STRING(R.string.default_subject_id);

        DialogStringData.DEFAULT_TITLE      = STRING(R.string.default_dlg_title);
        DialogStringData.DEFAULT_MESSAGE    = STRING(R.string.default_dlg_msg);
        DialogStringData.DEFAULT_POS_BT_TXT = STRING(R.string.default_dlg_bt_pos_txt);
        DialogStringData.DEFAULT_NEG_BT_TXT = STRING(R.string.default_dlg_bt_neg_txt);

        TextInputDialog.DEFAULT_TEXT_INPUT_HINT = STRING(R.string.default_dlg_txt_input_hint);

        MolarDataFragment.WEAR_PICKER_ITEMS[0] = STRING(R.string.desc_wear_lvl_unk_picker);
    }

    public static void loadPreferences() {
        loadPreferences(CONTEXT);
    }

    public static void loadPreferences(@NonNull AppCompatActivity activity) {
        CONTEXT = activity;
        Log.i("AppUtil", "Loading preferences...");
        PREFERENCES = activity.getSharedPreferences("_PREFS", Context.MODE_PRIVATE);
        final Map<String, ?> prefMap = PREFERENCES.getAll();
        _PREF_AUTO_SAVE = PREFERENCES.getBoolean(STRING(R.string.rt_cfg_pref_key_auto_save), _PREF_AUTO_SAVE);
        FileUtil.USE_SYSTEM_FILE_CHOOSER = PREFERENCES.getBoolean(STRING(R.string.rt_cfg_pref_key_use_sys_file_picker), FileUtil.USE_SYSTEM_FILE_CHOOSER);
        MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE = PREFERENCES.getBoolean(STRING(R.string.rt_cfg_pref_key_group_name_ignore_case), MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE);
        final String defaultSitePrefix = STRING(R.string.rt_cfg_pref_key_default_site_prefix),
                     defaultGroupPrefix = STRING(R.string.rt_cfg_pref_key_default_group_prefix);

        ProjectHandler.clearDefaultIds();
        ProjectHandler.addDefaultGroupId(STRING(R.string.desc_sex_f));
        ProjectHandler.addDefaultGroupId(STRING(R.string.desc_sex_m));
        for (String p : prefMap.keySet()) {
            Log.i("", p);
            if (p.startsWith(defaultSitePrefix)) {
                ProjectHandler.addDefaultSiteId(PREFERENCES.getString(p, null));
            } else if (p.startsWith(defaultGroupPrefix)) {
                ProjectHandler.addDefaultGroupId(PREFERENCES.getString(p, null));
            }
        }
        ProjectHandler.sortDefaultIds();

    }

    public static int dpToPixels(@IntRange(from=0) int dp) {
        if (CONTEXT != null) {
            return dpToPixels(CONTEXT, dp);
        } else {
            _errorNoContext();
            return 0;
        }
    }

    public static int dpToPixels(@NonNull Activity activity, @IntRange(from=0) int dp) {
        return (int)(dp * activity.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static void hideKeyboard(@NonNull View view) {
        hideKeyboard(CONTEXT, view.getWindowToken());
    }

    public static void hideKeyboard(@NonNull IBinder windowToken) {
        hideKeyboard(CONTEXT, windowToken);
    }

    public static void hideKeyboard(@NonNull Fragment fragment) {
        hideKeyboard(fragment.getContext(), fragment.getView().getRootView().getWindowToken());
    }

    public static void hideKeyboard(@NonNull Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        hideKeyboard(activity, view.getWindowToken());
    }

    public static void hideKeyboard(@NonNull Activity activity, @NonNull View view) {
        hideKeyboard(activity, view.getWindowToken());
    }

    public static void hideKeyboard(@NonNull Activity activity, @NonNull IBinder windowToken) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

    public static void hideKeyboard(@NonNull Context context, @NonNull IBinder windowToken) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

    public static void showKeyboard(@NonNull View view) {
        showKeyboard(CONTEXT, view);
    }

    public static void showKeyboard(@NonNull Activity activity, @NonNull View view) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }


    public static void featureNotImplementedYet() {
        new MessageDialog(new DialogStringData(CONTEXT,
                                                R.string.dlg_title_feature_not_implemented,
                                                R.string.dlg_msg_feature_not_implemented)).show();
    }

    public static void printSnackBarMsg(@NonNull String msg) {
        if (VIEW != null) {
            printSnackBarMsg(AppUtil.VIEW, msg);
        } else {
            _errorNoView();
        }
    }

    public static void printSnackBarMsg(@NonNull View view, @NonNull String msg) {
        Log.i("[SNACKBAR]", msg);
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    public static void printSnackBarMsg(@StringRes int msgId) {
        printSnackBarMsg(STRING(msgId));
    }

    public static void printSnackBarMsg(@NonNull View view, @StringRes int msgId) {
        printSnackBarMsg(view, STRING(msgId));
    }

    public static Toast printToast(@NonNull String msg) {
        return printToast(msg, DEFAULT_TOAST_DURATION);
    }

    public static Toast printToast(@StringRes int msgId) {
        return printToast(msgId, DEFAULT_TOAST_DURATION);
    }

    public static Toast printToast(@NonNull String msg, int duration) {
        return printToast(MolWearApp.getContext(), msg, duration);

    }

    public static Toast printToast(@StringRes int msgId, int duration) {
        return printToast(MolWearApp.getContext(), msgId, duration);
    }

    public static Toast printToast(@NonNull Context context, @NonNull String msg) {
        return printToast(context, msg, DEFAULT_TOAST_DURATION);
    }

    public static Toast printToast(@NonNull Context context, @StringRes int msgId) {
        return printToast(context, STRING(context, msgId), DEFAULT_TOAST_DURATION);
    }

    public static Toast printToast(@NonNull Context context, @NonNull String msg, int duration) {
        Log.i("[TOAST]", msg);
        final Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
        return toast;
    }

    public static Toast printToast(@NonNull Context context, @StringRes int msgId, int duration) {
        return printToast(context, STRING(context, msgId), duration);
    }

    public static void sendKeyPress(final int key) {
        final Instrumentation inst = new Instrumentation();
        new Thread(new Runnable() {
            public void run() {
                inst.sendKeyDownUpSync(key);
            }
        }).start();
    }

    public static void openPreferencesDialog() {
        openPreferencesDialog(CONTEXT);
    }

    public static void openPreferencesDialog(@NonNull AppCompatActivity activity) {
        PreferencesDialog dlg = new PreferencesDialog(activity);
        dlg.show();
    }

    public static void clearLoadErrors() {
        _STARTUP_LOAD_ERRORS = 0;
    }

    public static void addLoadError() {
        _STARTUP_LOAD_ERRORS++;
    }

    public static int LOAD_ERROR_COUNT() {
        return _STARTUP_LOAD_ERRORS;
    }

    public static void log(@NonNull String msg) {
        Log.i("[MOLWEAR_LOG]", msg);
//        if (VIEW == null || CONTEXT == null) {
//            _STARTUP_LOG.add(msg);
//        }
        if (MolWearApp.getContext() != null) {
            FileUtil.writeText(_LOG_FILE, msg, true, true);
        }
    }
}
