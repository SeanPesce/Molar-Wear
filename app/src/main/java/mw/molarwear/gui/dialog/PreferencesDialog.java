package mw.molarwear.gui.dialog;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import mw.molarwear.R;
import mw.molarwear.data.classes.MolarWearProject;
import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.util.AppUtil;
import mw.molarwear.util.FileUtil;

/**
 * A dialog box that allows the user to configure persistent app preferences.
 *
 * @author Sean Pesce
 *
 * @see    BasicDialog
 * @see    DialogStringData
 */

public class PreferencesDialog extends BasicDialog {

    // GUI
    protected final AppCompatCheckBox _cbAutoSavePref;
    protected final AppCompatCheckBox _cbUseSysFilePickerPref;
    protected final AppCompatCheckBox _cbCaseSensitiveGroupIdsPref;


    //////////// Constructors ////////////

    public PreferencesDialog(@NonNull AppCompatActivity activity) {
        super(new DialogStringData(activity,
                                   R.string.dlg_title_app_prefs,
                                   R.string.dlg_msg_app_prefs,
                                   R.string.dlg_bt_save,
                                   R.string.dlg_bt_cancel),
              R.layout.dialog_app_preferences);
        _cbAutoSavePref = this.linearLayout().findViewById(R.id.checkbox_pref_auto_save);
        _cbUseSysFilePickerPref = this.linearLayout().findViewById(R.id.checkbox_pref_use_sys_file_picker);
        _cbCaseSensitiveGroupIdsPref = this.linearLayout().findViewById(R.id.checkbox_pref_case_sensitive_group_ids);
        initialize();
    }


    protected void initialize() {
        _cbAutoSavePref.setChecked(AppUtil.getPrefAutoSave());
        _cbUseSysFilePickerPref.setChecked(FileUtil.USE_SYSTEM_FILE_CHOOSER);
        _cbCaseSensitiveGroupIdsPref.setChecked(!MolarWearProject.DEFAULT_GROUP_ID_IGNORE_CASE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        _activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        setMinWidthPx((int)(displayMetrics.widthPixels * 0.8f));

        setPosBt(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences.Editor editor = AppUtil.PREFERENCES.edit();
                editor.putBoolean(_activity.getString(R.string.rt_cfg_pref_key_auto_save), _cbAutoSavePref.isChecked());
                editor.putBoolean(_activity.getString(R.string.rt_cfg_pref_key_use_sys_file_picker), _cbUseSysFilePickerPref.isChecked());
                editor.putBoolean(_activity.getString(R.string.rt_cfg_pref_key_group_name_ignore_case), !_cbCaseSensitiveGroupIdsPref.isChecked());
                List<String> defaultSiteIds  = ProjectHandler.getDefaultSiteIds(),
                             defaultGroupIds = ProjectHandler.getDefaultGroupIDs();
                for (int i = 0; i < defaultSiteIds.size(); i++) {
                    editor.putString(_activity.getString(R.string.rt_cfg_pref_key_default_site_prefix)+i, defaultSiteIds.get(i));
                }
                for (int i = 0; i < defaultGroupIds.size(); i++) {
                    editor.putString(_activity.getString(R.string.rt_cfg_pref_key_default_group_prefix)+i, defaultGroupIds.get(i));
                }
                editor.apply();
                AppUtil.loadPreferences(_activity);
            }
        });
    }


    //////////// Accessors ////////////

    public AppCompatCheckBox checkBoxAutoSavePref() { return _cbAutoSavePref;       }
    public      LinearLayout linearLayout()         { return (LinearLayout)_body; }

}
