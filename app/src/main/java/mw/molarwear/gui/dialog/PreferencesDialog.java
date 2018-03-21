package mw.molarwear.gui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.LinearLayout;

import mw.molarwear.R;
import mw.molarwear.util.AppUtility;

/**
 * A dialog box that allows the user to configure persistent app preferences.
 *
 * @author Sean Pesce
 *
 * @see    TwoButtonDialog
 * @see    DialogStringData
 */

public class PreferencesDialog extends TwoButtonDialog {

    // GUI
    protected final AppCompatCheckBox _cbAutoSavePref;


    //////////// Constructors ////////////

    public PreferencesDialog(@NonNull Activity activity) {
        super(new DialogStringData(activity,
                                   R.string.dlg_title_app_prefs,
                                   R.string.dlg_msg_app_prefs,
                                   R.string.dlg_bt_save,
                                   R.string.dlg_bt_cancel),
              R.layout.dialog_app_preferences);
        _cbAutoSavePref = (AppCompatCheckBox) this.linearLayout().findViewById(R.id.checkbox_pref_auto_save);
        initialize();
    }


    protected void initialize() {
        _cbAutoSavePref.setChecked(AppUtility.getPrefAutoSave());

        setPositiveButton(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = AppUtility.PREFERENCES.edit();
                editor.putBoolean(_activity.getString(R.string.rt_cfg_pref_key_auto_save), _cbAutoSavePref.isChecked());
                editor.apply();
                AppUtility.loadPreferences(_activity);
            }
        });
    }


    //////////// Accessors ////////////

    public AppCompatCheckBox checkBoxAutoSavePref() { return _cbAutoSavePref;       }
    public      LinearLayout linearLayout()         { return (LinearLayout)_layout; }

}
