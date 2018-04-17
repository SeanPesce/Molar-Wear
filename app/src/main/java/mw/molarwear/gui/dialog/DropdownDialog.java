package mw.molarwear.gui.dialog;

import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import mw.molarwear.R;

/**
 * A dialog box with radio buttons for user input.
 *
 * @author Sean Pesce
 *
 * @see    BasicDialog
 * @see    android.support.v7.widget.AppCompatSpinner
 */

public class DropdownDialog<T> extends BasicDialog {

    private final AppCompatSpinner _spinner;
    private final List<T> _items;
    private final List<String> _itemNames;
    private final ArrayAdapter<String> _adapter;

    private String _label = "";
    private String _emptyMsg = "";
    private AdapterView.OnItemSelectedListener _selectListener = null;

    public DropdownDialog(@NonNull AppCompatActivity activity) {
        super(activity, R.layout.dialog_dropdown_input);
        _spinner = _body.findViewById(R.id.dlg_dropdown);
        _items = new ArrayList<T>();
        _itemNames = new ArrayList<>();
        _adapter = new ArrayAdapter<>(_activity, R.layout.support_simple_spinner_dropdown_item, _itemNames);
        initialize(-1);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public DropdownDialog(@NonNull AppCompatActivity activity, @NonNull Collection<T> items) {
        super(activity, R.layout.dialog_dropdown_input);
        _spinner = _body.findViewById(R.id.dlg_dropdown);
        _items = (items != null && !items.isEmpty()) ? new ArrayList<>(Arrays.asList((T[])items.toArray())) : new ArrayList<T>();
        _itemNames = new ArrayList<>();
        _adapter = new ArrayAdapter<>(_activity, R.layout.support_simple_spinner_dropdown_item, _itemNames);
        initialize(-1);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public DropdownDialog(@NonNull DialogStringData strings, @NonNull Collection<T> items) {
        super(strings, R.layout.dialog_dropdown_input);
        _spinner = _body.findViewById(R.id.dlg_dropdown);
        _items = (items != null && !items.isEmpty()) ? new ArrayList<T>(Arrays.asList((T[])items.toArray())) : new ArrayList<T>();
        _itemNames = new ArrayList<>();
        _adapter = new ArrayAdapter<>(_activity, R.layout.support_simple_spinner_dropdown_item, _itemNames);
        initialize(-1);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public DropdownDialog(@NonNull AppCompatActivity activity, @NonNull Collection<T> items, @IntRange(from=0) int selection) {
        super(activity, R.layout.dialog_dropdown_input);
        _spinner = _body.findViewById(R.id.dlg_dropdown);
        _items = (items != null && !items.isEmpty()) ? new ArrayList<>(Arrays.asList((T[])items.toArray())) : new ArrayList<T>();
        _itemNames = new ArrayList<>();
        _adapter = new ArrayAdapter<>(_activity, R.layout.support_simple_spinner_dropdown_item, _itemNames);
        if (selection >= _items.size() || selection < 0) {
            selection = -1;
        }
        initialize(selection);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public DropdownDialog(@NonNull DialogStringData strings, @NonNull Collection<T> items, @IntRange(from=0) int selection) {
        super(strings, R.layout.dialog_dropdown_input);
        _spinner = _body.findViewById(R.id.dlg_dropdown);
        _items = (items != null && !items.isEmpty()) ? new ArrayList<>(Arrays.asList((T[])items.toArray())) : new ArrayList<T>();
        _itemNames = new ArrayList<>();
        _adapter = new ArrayAdapter<>(_activity, R.layout.support_simple_spinner_dropdown_item, _itemNames);
        if (selection >= _items.size() || selection < 0) {
            selection = -1;
        }
        initialize(selection);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public DropdownDialog(@NonNull AppCompatActivity activity, @NonNull Collection<T> items, @NonNull String labelText) {
        super(activity, R.layout.dialog_dropdown_input);
        _spinner = _body.findViewById(R.id.dlg_dropdown);
        _items = (items != null && !items.isEmpty()) ? new ArrayList<>(Arrays.asList((T[])items.toArray())) : new ArrayList<T>();
        _itemNames = new ArrayList<>();
        _adapter = new ArrayAdapter<>(_activity, R.layout.support_simple_spinner_dropdown_item, _itemNames);
        _label = labelText;
        initialize(-1);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public DropdownDialog(@NonNull DialogStringData strings, @NonNull Collection<T> items, @NonNull String labelText) {
        super(strings, R.layout.dialog_dropdown_input);
        _spinner = _body.findViewById(R.id.dlg_dropdown);
        _items = (items != null && !items.isEmpty()) ? new ArrayList<>(Arrays.asList((T[])items.toArray())) : new ArrayList<T>();
        _itemNames = new ArrayList<>();
        _adapter = new ArrayAdapter<>(_activity, R.layout.support_simple_spinner_dropdown_item, _itemNames);
        _label = labelText;
        initialize(-1);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public DropdownDialog(@NonNull AppCompatActivity activity, @NonNull Collection<T> items, @NonNull String labelText, @IntRange(from=0) int selection) {
        super(activity, R.layout.dialog_dropdown_input);
        _spinner = _body.findViewById(R.id.dlg_dropdown);
        _items = (items != null && !items.isEmpty()) ? new ArrayList<>(Arrays.asList((T[])items.toArray())) : new ArrayList<T>();
        _itemNames = new ArrayList<>();
        _adapter = new ArrayAdapter<>(_activity, R.layout.support_simple_spinner_dropdown_item, _itemNames);
        _label = labelText;
        if (selection >= _items.size() || selection < 0) {
            selection = -1;
        }
        initialize(selection);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public DropdownDialog(@NonNull DialogStringData strings, @NonNull Collection<T> items, @NonNull String labelText, @IntRange(from=0) int selection) {
        super(strings, R.layout.dialog_dropdown_input);
        _spinner = _body.findViewById(R.id.dlg_dropdown);
        _items = (items != null && !items.isEmpty()) ? new ArrayList<>(Arrays.asList((T[])items.toArray())) : new ArrayList<T>();
        _itemNames = new ArrayList<>();
        _adapter = new ArrayAdapter<>(_activity, R.layout.support_simple_spinner_dropdown_item, _itemNames);
        _label = labelText;
        if (selection >= _items.size() || selection < 0) {
            selection = -1;
        }
        initialize(selection);
    }


    protected void initialize(int selection) {

        for (T i : _items) {
            _itemNames.add(itemToString(i));
        }

        _spinner.setAdapter(_adapter);

        if (selection >= 0 && selection < _spinner.getCount()) {
            _spinner.setSelection(selection);
        }

        setLabelText(_label);

        notifyDataSetChanged();
    }


    @SuppressWarnings("ConstantConditions")
    public String itemToString(@NonNull T item) {
        // This method should be overridden for custom implementations
        return (item != null) ? item.toString() : "";
    }


    //////////// Accessors ////////////

    public long getSelectedItemId() {
        return _spinner.getSelectedItemId();
    }

    public View getSelectedItem() {
        return (View)_spinner.getSelectedItem();
    }

    public int getSelectedItemIndex() {
        return _spinner.getSelectedItemPosition();
    }

    public String getSelectedItemStringVal() {
        return _itemNames.get(_spinner.getSelectedItemPosition());
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

    public View getItemById(@IdRes int id) {
        View v = null;
        try {
            v = _spinner.findViewById(id);
        } catch (ClassCastException e) {
            return null;
        }
        return v;
    }

    public String getLabelText() {
        return _label;
    }

    public String getEmptyMsg() {
        return _emptyMsg;
    }

    public AdapterView.OnItemSelectedListener getSelectItemListener() {
        return _selectListener;
    }


    //////////// Mutators ////////////

    public void resetSelection() {
        if (_spinner.getCount() > 0) {
            _spinner.setSelection(0);
        }
    }

    public void select(@IntRange(from=0) int index) {
        if (index < _spinner.getCount() && index >= 0) {
            _spinner.setSelection(index);
        }
    }

    public void select(@NonNull T item) {
        select(_items.indexOf(item));
    }

    public void setCheckChangedListener(AdapterView.OnItemSelectedListener listener) {
        _selectListener = listener;
        _spinner.setOnItemSelectedListener(listener);
    }

    public void setLabelText(@StringRes int id) {
        setLabelText(_activity.getString(id));
    }

    public void setLabelText(@NonNull String text) {
        _label = text;
        updateGuiElements();
    }

    public void setEmptyMsg(@StringRes int id) {
        setEmptyMsg(_activity.getString(id));
    }

    public void setEmptyMsg(@NonNull String msg) {
        _emptyMsg = msg;
        updateGuiElements();
    }

    @SuppressWarnings("ConstantConditions")
    public void addItem(@NonNull T item) {
        if (item != null) {
            _items.add(item);
            _itemNames.add(itemToString(item));
            notifyDataSetChanged();
        }
        updateGuiElements();
    }

    public void removeItem(@IntRange(from=0) int index) {
        if (index < _spinner.getCount() && index >= 0) {
            _items.remove(index);
            _itemNames.remove(index);
            notifyDataSetChanged();
        }
        updateGuiElements();
    }

    public void notifyDataSetChanged() {
        _adapter.notifyDataSetChanged();
    }

    public void updateGuiElements() {
        TextView tv = _body.findViewById(R.id.dlg_dropdown_lbl);
        if (_spinner.getCount() > 0) {
            _spinner.setVisibility(View.VISIBLE);
            if (tv != null) {
                tv.setText(_label);
            }
        } else {
            _spinner.setVisibility(View.GONE);
            if (tv != null) {
                tv.setText(_emptyMsg);
            }
        }

        if (tv != null) {
            if (tv.getText().length() == 0) {
                tv.setVisibility(View.GONE);
            } else {
                tv.setVisibility(View.VISIBLE);
            }
        }
    }

}
