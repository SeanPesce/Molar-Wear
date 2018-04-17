package mw.molarwear.gui.dialog;

import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mw.molarwear.R;
import mw.molarwear.util.AppUtil;

/**
 * A dialog box with radio buttons for user input.
 *
 * @author Sean Pesce
 *
 * @see    BasicDialog
 * @see    RadioGroup
 * @see    AppCompatRadioButton
 */

public class RadioGroupDialog<T> extends BasicDialog {

    private final RadioGroup _radioGroup;
    private final List<AppCompatRadioButton> _radioButtons;
    private final List<T> _items;

    private String _rgLabel = "";
    private RadioGroup.OnCheckedChangeListener _checkChangedListener = null;

    public RadioGroupDialog(@NonNull AppCompatActivity activity) {
        super(activity, R.layout.dialog_radio_input);
        _radioGroup = _body.findViewById(R.id.dlg_radio_group);
        _radioButtons = new ArrayList<>();
        _items = new ArrayList<>();
        initialize(-1);
    }

    @SuppressWarnings("ConstantConditions")
    public RadioGroupDialog(@NonNull AppCompatActivity activity, @NonNull List<T> items) {
        super(activity, R.layout.dialog_radio_input);
        _radioGroup = _body.findViewById(R.id.dlg_radio_group);
        _radioButtons = new ArrayList<>();
        _items = (items != null) ? items : new ArrayList<T>();
        initialize(-1);
    }

    @SuppressWarnings("ConstantConditions")
    public RadioGroupDialog(@NonNull DialogStringData strings, @NonNull List<T> items) {
        super(strings, R.layout.dialog_radio_input);
        _radioGroup = _body.findViewById(R.id.dlg_radio_group);
        _radioButtons = new ArrayList<>();
        _items = (items != null) ? items : new ArrayList<T>();
        initialize(-1);
    }

    @SuppressWarnings("ConstantConditions")
    public RadioGroupDialog(@NonNull AppCompatActivity activity, @NonNull List<T> items, @IntRange(from=0) int selection) {
        super(activity, R.layout.dialog_radio_input);
        _radioGroup = _body.findViewById(R.id.dlg_radio_group);
        _radioButtons = new ArrayList<>();
        _items = (items != null) ? items : new ArrayList<T>();
        if (selection >= items.size() || selection < 0) {
            selection = -1;
        }
        initialize(selection);
    }

    @SuppressWarnings("ConstantConditions")
    public RadioGroupDialog(@NonNull DialogStringData strings, @NonNull List<T> items, @IntRange(from=0) int selection) {
        super(strings, R.layout.dialog_radio_input);
        _radioGroup = _body.findViewById(R.id.dlg_radio_group);
        _radioButtons = new ArrayList<>();
        _items = (items != null) ? items : new ArrayList<T>();
        if (selection >= items.size() || selection < 0) {
            selection = -1;
        }
        initialize(selection);
    }

    @SuppressWarnings("ConstantConditions")
    public RadioGroupDialog(@NonNull AppCompatActivity activity, @NonNull List<T> items, @NonNull String radioGroupLabel) {
        super(activity, R.layout.dialog_radio_input);
        _radioGroup = _body.findViewById(R.id.dlg_radio_group);
        _radioButtons = new ArrayList<>();
        _items = (items != null) ? items : new ArrayList<T>();
        _rgLabel = radioGroupLabel;
        initialize(-1);
    }

    @SuppressWarnings("ConstantConditions")
    public RadioGroupDialog(@NonNull DialogStringData strings, @NonNull List<T> items, @NonNull String radioGroupLabel) {
        super(strings, R.layout.dialog_radio_input);
        _radioGroup = _body.findViewById(R.id.dlg_radio_group);
        _radioButtons = new ArrayList<>();
        _items = (items != null) ? items : new ArrayList<T>();
        _rgLabel = radioGroupLabel;
        initialize(-1);
    }

    @SuppressWarnings("ConstantConditions")
    public RadioGroupDialog(@NonNull AppCompatActivity activity, @NonNull List<T> items, @NonNull String radioGroupLabel, @IntRange(from=0) int selection) {
        super(activity, R.layout.dialog_radio_input);
        _radioGroup = _body.findViewById(R.id.dlg_radio_group);
        _radioButtons = new ArrayList<>();
        _items = (items != null) ? items : new ArrayList<T>();
        _rgLabel = radioGroupLabel;
        if (selection >= items.size() || selection < 0) {
            selection = -1;
        }
        initialize(selection);
    }

    @SuppressWarnings("ConstantConditions")
    public RadioGroupDialog(@NonNull DialogStringData strings, @NonNull List<T> items, @NonNull String radioGroupLabel, @IntRange(from=0) int selection) {
        super(strings, R.layout.dialog_radio_input);
        _radioGroup = _body.findViewById(R.id.dlg_radio_group);
        _radioButtons = new ArrayList<>();
        _items = (items != null) ? items : new ArrayList<T>();
        _rgLabel = radioGroupLabel;
        if (selection >= items.size() || selection < 0) {
            selection = -1;
        }
        initialize(selection);
    }


    protected void initialize(int selection) {

        for (T i : _items) {
            AppCompatRadioButton rb = itemToRadioButton(i);
            Random rand = new Random();
            while (((int)rb.getId()) <= 0 || _radioGroup.findViewById(rb.getId()) != null) {
                // Generate random ID for older Android versions
                rb.setId(rand.nextInt((Integer.MAX_VALUE - 5) + 1) + 5);
            }
            _radioGroup.addView(rb);
            _radioButtons.add(rb);
        }

        if (selection >= 0 && selection < _radioButtons.size()) {
            AppCompatRadioButton rb = _radioButtons.get(selection);
            rb.requestFocus();
            _radioGroup.check(_radioButtons.get(selection).getId());
        }

        setRadioGroupLabelText(_rgLabel);
    }


    @SuppressWarnings("ConstantConditions")
    public AppCompatRadioButton itemToRadioButton(@NonNull T item) {
        // This method should be overridden for custom implementations
        AppCompatRadioButton rb = new AppCompatRadioButton(_activity);
        rb.setPadding(rb.getPaddingLeft(), AppUtil.dpToPixels(4), rb.getPaddingRight(), AppUtil.dpToPixels(4));
        rb.setText((item != null) ? item.toString() : "");
        return rb;
    }


    //////////// Accessors ////////////

    public int getCheckedRadioButtonId() {
        return _radioGroup.getCheckedRadioButtonId();
    }

    public AppCompatRadioButton getCheckedRadioButton() {
        return getRadioButtonById(_radioGroup.getCheckedRadioButtonId());
    }

    public int getCheckedRadioButtonIndex() {
        return _radioButtons.indexOf(getCheckedRadioButton());
    }

    public T getItem(@IntRange(from=0) int index) {
        if (index < _items.size() && index >= 0) {
            return _items.get(index);
        }
        return null;
    }

    public int getItemCount() {
        return _items.size();
    }

    public AppCompatRadioButton getRadioButtonByIndex(@IntRange(from=0) int index) {
        if (index < _radioButtons.size() && index >= 0) {
            return _radioButtons.get(index);
        }
        return null;
    }

    public AppCompatRadioButton getRadioButtonById(@IdRes int id) {
        AppCompatRadioButton rb = null;
        try {
            rb = _radioGroup.findViewById(id);
        } catch (ClassCastException e) {
            return null;
        }
        return rb;
    }

    public int getRadioButtonCount() {
        return _radioButtons.size();
    }

    public View getRadioGroupChildByIndex(@IntRange(from=0) int index) {
        if (index < _radioGroup.getChildCount() && index >= 0) {
            return _radioGroup.getChildAt(index);
        }
        return null;
    }

    public int getRadioGroupChildCount() {
        return _radioGroup.getChildCount();
    }

    public String getRadioGroupLabelText() {
        return _rgLabel;
    }

    public RadioGroup.OnCheckedChangeListener getCheckChangedListener() {
        return _checkChangedListener;
    }


    //////////// Mutators ////////////

    public void clearCheck() {
        _radioGroup.clearCheck();
    }

    public void check(@IdRes int id) {
        _radioGroup.check(id);
    }

    public void setCheckChangedListener(RadioGroup.OnCheckedChangeListener listener) {
        _checkChangedListener = listener;
        _radioGroup.setOnCheckedChangeListener(listener);
    }

    public void setRadioGroupLabelText(@StringRes int id) {
        TextView tv = _body.findViewById(R.id.dlg_radio_group_lbl);
        if (tv != null) {
            tv.setText(id);
        }
    }

    public void setRadioGroupLabelText(@NonNull String text) {
        TextView tv = _body.findViewById(R.id.dlg_radio_group_lbl);
        if (tv != null) {
            tv.setText(text);
            if (tv.getText().length() == 0) {
                tv.setVisibility(View.GONE);
            } else {
                tv.setVisibility(View.VISIBLE);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void addItem(@NonNull T item) {
        if (item != null) {
            _items.add(item);
            AppCompatRadioButton rb = itemToRadioButton(item);
            _radioGroup.addView(rb);
            _radioButtons.add(rb);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void addItem(@NonNull T item, @NonNull AppCompatRadioButton radioButton) {
        if (item != null) {
            if (radioButton != null) {
                _items.add(item);
                _radioGroup.addView(radioButton);
                _radioButtons.add(radioButton);
            } else {
                addItem(item);
            }
        }
    }

    public void removeItemByIndex(@IntRange(from=0) int index) {
        if (index < _radioButtons.size() && index >= 0) {
            int id = _radioButtons.get(index).getId();
            View v = _radioGroup.findViewById(id);
            if (v != null) {
                _radioGroup.removeView(v);
            }
            _radioButtons.remove(index);
            _items.remove(index);
        }
    }

    public void removeItemById(@IdRes int id) {
        View v = _radioGroup.findViewById(id);
        if (v != null) {
            for (int i = 0; i < _radioButtons.size(); i++) {
                AppCompatRadioButton rb = _radioButtons.get(i);
                if (rb.getId() == v.getId()) {
                    _radioGroup.removeView(v);
                    _radioButtons.remove(i);
                    _items.remove(i);
                }
            }
        }
    }

}
