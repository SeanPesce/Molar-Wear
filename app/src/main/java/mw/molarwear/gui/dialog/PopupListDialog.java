package mw.molarwear.gui.dialog;

import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import mw.molarwear.R;
import mw.molarwear.util.AppUtil;

/**
 *
 * @author Sean Pesce
 *
 * @see    BasicDialog
 */

public class PopupListDialog<T> extends BasicDialog {


    protected final List<T> _items;
    protected final ArrayAdapter<T> _adapter;

    protected ListFragment _listFragment;
    protected String _emptyMessage = "";
    protected boolean _showHeaderDividers = true;
    protected boolean _showFooterDividers = true;
    protected int _highlightIndex = -1;
    @IdRes
    protected int _highlightElementId = R.id.layout_listitem_large;
    @ColorRes
    protected int _highlightColorId = R.color.colorPrimaryLight4;

    protected AdapterView.OnItemSelectedListener  _onItemSelected  = null;
    protected AdapterView.OnItemClickListener     _onItemClick     = null;
    protected AdapterView.OnItemLongClickListener _onItemLongClick = null;

    protected List<AdapterView.OnItemSelectedListener>  _onItemSelectedInternalAdd  = new ArrayList<>();
    protected List<AdapterView.OnItemClickListener>     _onItemClickInternalAdd     = new ArrayList<>();
    protected List<AdapterView.OnItemLongClickListener> _onItemLongClickInternalAdd = new ArrayList<>();

    protected boolean _closeOnItemSelected = true;
    protected boolean _closeOnNothingSelected = false;
    protected boolean _closeOnItemClick = true;
    protected boolean _closeOnItemLongClick = false;
    protected boolean _consumeItemLongClicks = false;
    protected boolean _highlightOnLongClick = true;

    protected final AdapterView.OnItemSelectedListener _onItemSelectedInternal = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setHighlighted(position);
            for (AdapterView.OnItemSelectedListener l : _onItemSelectedInternalAdd) {
                l.onItemSelected(parent, view, position, id);
            }
            if (_onItemSelected != null) {
                _onItemSelected.onItemSelected(parent, view, position, id);
            }
            if (_closeOnItemSelected) {
                dismiss();
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            setHighlighted(-1);
            for (AdapterView.OnItemSelectedListener l : _onItemSelectedInternalAdd) {
                l.onNothingSelected(parent);
            }
            if (_onItemSelected != null) {
                _onItemSelected.onNothingSelected(parent);
            }
            if (_closeOnNothingSelected) {
                dismiss();
            }
        }
    };

    protected final AdapterView.OnItemClickListener _onItemClickInternal = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            setHighlighted(position);
            for (AdapterView.OnItemClickListener l : _onItemClickInternalAdd) {
                l.onItemClick(parent, view, position, id);
            }
            if (_onItemClick != null) {
                _onItemClick.onItemClick(parent, view, position, id);
            }
            if (_closeOnItemClick) {
                dismiss();
            }
        }
    };

    protected final AdapterView.OnItemLongClickListener _onItemLongClickInternal = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            boolean returnVal = _consumeItemLongClicks;
            if (_highlightOnLongClick) {
                setHighlighted(position);
            }
            for (AdapterView.OnItemLongClickListener l : _onItemLongClickInternalAdd) {
                if (l.onItemLongClick(parent, view, position, id)) {
                    returnVal = true;
                }
            }
            if (_onItemLongClick != null) {
                returnVal = _onItemLongClick.onItemLongClick(parent, view, position, id);
            }
            if (_closeOnItemLongClick) {
                dismiss();
            }
            return returnVal;
        }
    };


    //////////// Constructors ////////////

    public PopupListDialog(@NonNull AppCompatActivity activity) {
        super(activity,
              new BasicDialog.ButtonParams(false, false, false),
              R.layout.dialog_popup_list_frag);
        _items = new ArrayList<>();
        _adapter = new ArrayAdapter<>(_activity, android.R.layout.select_dialog_item, _items);
        initialize(-1);
    }

    @SuppressWarnings({"ConstantConditions"})
    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull T item) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = new ArrayList<>();
        if (item != null) {
            _items.add(item);
        }
        _adapter = new ArrayAdapter<>(_activity, android.R.layout.select_dialog_item, _items);
        initialize(-1);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull Collection<T> items) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = (items != null && !items.isEmpty()) ? new ArrayList<>(Arrays.asList((T[])items.toArray())) : new ArrayList<T>();
        _adapter = new ArrayAdapter<>(_activity, android.R.layout.select_dialog_item, _items);
        initialize(-1);
    }

    @SuppressWarnings({"ConstantConditions"})
    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull T[] items) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = (items != null && items.length > 0) ? new ArrayList<>(Arrays.asList(items)) : new ArrayList<T>();
        _adapter = new ArrayAdapter<>(_activity, android.R.layout.select_dialog_item, _items);
        initialize(-1);
    }

    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull ResourceParams resParams) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = new ArrayList<>();
        if (resParams.ITEM_LAYOUT_ID >= 0 && resParams.ITEM_TEXT_VIEW_ID >= 0) {
            _adapter = new ArrayAdapter<>(_activity, resParams.ITEM_LAYOUT_ID, resParams.ITEM_TEXT_VIEW_ID, _items);
        } else if (resParams.ITEM_LAYOUT_ID >= 0) {
            _adapter = new ArrayAdapter<>(_activity, resParams.ITEM_LAYOUT_ID, _items);
        } else if (resParams.ITEM_TEXT_VIEW_ID >= 0) {
            _adapter = new ArrayAdapter<>(_activity, resParams.ITEM_TEXT_VIEW_ID, _items);
        } else {
            _adapter = new ArrayAdapter<>(_activity, android.R.layout.select_dialog_item, _items);
        }
        initialize(resParams.DROP_DOWN_ITEM_LAYOUT_ID >= 0 ? resParams.DROP_DOWN_ITEM_LAYOUT_ID : -1);
    }

    @SuppressWarnings({"ConstantConditions"})
    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull T item, @NonNull ResourceParams resParams) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = new ArrayList<>();
        if (item != null) {
            _items.add(item);
        }
        if (resParams.ITEM_LAYOUT_ID >= 0 && resParams.ITEM_TEXT_VIEW_ID >= 0) {
            _adapter = new ArrayAdapter<>(_activity, resParams.ITEM_LAYOUT_ID, resParams.ITEM_TEXT_VIEW_ID, _items);
        } else if (resParams.ITEM_LAYOUT_ID >= 0) {
            _adapter = new ArrayAdapter<>(_activity, resParams.ITEM_LAYOUT_ID, _items);
        } else if (resParams.ITEM_TEXT_VIEW_ID >= 0) {
            _adapter = new ArrayAdapter<>(_activity, resParams.ITEM_TEXT_VIEW_ID, _items);
        } else {
            _adapter = new ArrayAdapter<>(_activity, android.R.layout.select_dialog_item, _items);
        }
        initialize(resParams.DROP_DOWN_ITEM_LAYOUT_ID >= 0 ? resParams.DROP_DOWN_ITEM_LAYOUT_ID : -1);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull Collection<T> items, @NonNull ResourceParams resParams) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = (items != null && !items.isEmpty()) ? new ArrayList<>(Arrays.asList((T[])items.toArray())) : new ArrayList<T>();
        if (resParams.ITEM_LAYOUT_ID >= 0 && resParams.ITEM_TEXT_VIEW_ID >= 0) {
            _adapter = new ArrayAdapter<>(_activity, resParams.ITEM_LAYOUT_ID, resParams.ITEM_TEXT_VIEW_ID, _items);
        } else if (resParams.ITEM_LAYOUT_ID >= 0) {
            _adapter = new ArrayAdapter<>(_activity, resParams.ITEM_LAYOUT_ID, _items);
        } else if (resParams.ITEM_TEXT_VIEW_ID >= 0) {
            _adapter = new ArrayAdapter<>(_activity, resParams.ITEM_TEXT_VIEW_ID, _items);
        } else {
            _adapter = new ArrayAdapter<>(_activity, android.R.layout.select_dialog_item, _items);
        }
        initialize(resParams.DROP_DOWN_ITEM_LAYOUT_ID >= 0 ? resParams.DROP_DOWN_ITEM_LAYOUT_ID : -1);
    }

    @SuppressWarnings({"ConstantConditions"})
    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull T[] items, @NonNull ResourceParams resParams) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = (items != null && items.length > 0) ? new ArrayList<>(Arrays.asList(items)) : new ArrayList<T>();
        if (resParams.ITEM_LAYOUT_ID >= 0 && resParams.ITEM_TEXT_VIEW_ID >= 0) {
            _adapter = new ArrayAdapter<>(_activity, resParams.ITEM_LAYOUT_ID, resParams.ITEM_TEXT_VIEW_ID, _items);
        } else if (resParams.ITEM_LAYOUT_ID >= 0) {
            _adapter = new ArrayAdapter<>(_activity, resParams.ITEM_LAYOUT_ID, _items);
        } else if (resParams.ITEM_TEXT_VIEW_ID >= 0) {
            _adapter = new ArrayAdapter<>(_activity, resParams.ITEM_TEXT_VIEW_ID, _items);
        } else {
            _adapter = new ArrayAdapter<>(_activity, android.R.layout.select_dialog_item, _items);
        }
        initialize(resParams.DROP_DOWN_ITEM_LAYOUT_ID >= 0 ? resParams.DROP_DOWN_ITEM_LAYOUT_ID : -1);
    }

    public PopupListDialog(@NonNull AppCompatActivity activity, @LayoutRes int itemTextViewId) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = new ArrayList<>();
        _adapter = new ArrayAdapter<>(_activity, itemTextViewId, _items);
        initialize(-1);
    }

    @SuppressWarnings({"ConstantConditions"})
    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull T item, @LayoutRes int itemTextViewId) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = new ArrayList<>();
        if (item != null) {
            _items.add(item);
        }
        _adapter = new ArrayAdapter<>(_activity, itemTextViewId, _items);
        initialize(-1);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull Collection<T> items, @LayoutRes int itemTextViewId) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = (items != null && !items.isEmpty()) ? new ArrayList<>(Arrays.asList((T[])items.toArray())) : new ArrayList<T>();
        _adapter = new ArrayAdapter<>(_activity, itemTextViewId, _items);
        initialize(-1);
    }

    @SuppressWarnings({"ConstantConditions"})
    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull T[] items, @LayoutRes int itemTextViewId) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = (items != null && items.length > 0) ? new ArrayList<>(Arrays.asList(items)) : new ArrayList<T>();
        _adapter = new ArrayAdapter<>(_activity, itemTextViewId, _items);
        initialize(-1);
    }

    public PopupListDialog(@NonNull AppCompatActivity activity, @LayoutRes int itemLayoutId, @IdRes int itemTextViewId) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = new ArrayList<>();
        _adapter = new ArrayAdapter<>(_activity, itemLayoutId, itemTextViewId, _items);
        initialize(-1);
    }

    @SuppressWarnings({"ConstantConditions"})
    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull T item, @LayoutRes int itemLayoutId, @IdRes int itemTextViewId) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = new ArrayList<>();
        if (item != null) {
            _items.add(item);
        }
        _adapter = new ArrayAdapter<>(_activity, itemLayoutId, itemTextViewId, _items);
        initialize(-1);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull Collection<T> items, @LayoutRes int itemLayoutId, @IdRes int itemTextViewId) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = (items != null && !items.isEmpty()) ? new ArrayList<>(Arrays.asList((T[])items.toArray())) : new ArrayList<T>();
        _adapter = new ArrayAdapter<>(_activity, itemLayoutId, itemTextViewId, _items);
        initialize(-1);
    }

    @SuppressWarnings({"ConstantConditions"})
    public PopupListDialog(@NonNull AppCompatActivity activity, @NonNull T[] items, @LayoutRes int itemLayoutId, @IdRes int itemTextViewId) {
        super(activity,
            new BasicDialog.ButtonParams(false, false, false),
            R.layout.dialog_popup_list_frag);
        _items = (items != null && items.length > 0) ? new ArrayList<>(Arrays.asList(items)) : new ArrayList<T>();
        _adapter = new ArrayAdapter<>(_activity, itemLayoutId, itemTextViewId, _items);
        initialize(-1);
    }




    private void initialize(@IntRange(from=-1) final int dropDownItemLayoutId) {

        //  Useful default item layouts:
        // android.R.layout.simple_spinner_item
        // android.R.layout.select_dialog_item
        // android.R.layout.simple_spinner_dropdown_item


        _listFragment = (ListFragment) _activity.getSupportFragmentManager().findFragmentById(R.id.dlg_popup_list_frag);
        _listFragment.setListAdapter(_adapter);
        _adapter.setDropDownViewResource(dropDownItemLayoutId >= 0 ? dropDownItemLayoutId : android.R.layout.simple_spinner_dropdown_item);

        _onShowInternalAdd.add(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    if (_listFragment == null) {
                        _listFragment = (ListFragment) _activity.getSupportFragmentManager().findFragmentById(R.id.dlg_popup_list_frag);
                        _listFragment.setListAdapter(_adapter);
                        _adapter.setDropDownViewResource(dropDownItemLayoutId >= 0 ? dropDownItemLayoutId : android.R.layout.simple_spinner_dropdown_item);
                        _adapter.notifyDataSetChanged();
                    }

                    Window window = getWindow();
                    if ((!_items.isEmpty()) && window != null) {
                        int sizeDiff = (_titleView.getVisibility() != View.GONE) ? _titleView.getHeight() : 0;
                        sizeDiff += (_messageView.getVisibility() != View.GONE) ? _messageView.getHeight() : 0;
                        if (_btPos.getVisibility() != View.GONE) {
                            sizeDiff += _btPos.getHeight();
                        } else if (_btNeg.getVisibility() != View.GONE) {
                            sizeDiff += _btNeg.getHeight();
                        } else if (_btNeutral.getVisibility() != View.GONE) {
                            sizeDiff += _btNeutral.getHeight();
                        }
                        int bodyHeight = window.getAttributes().height - sizeDiff;
                        if (_listFragment.getListView().getHeight() > bodyHeight) {
                            if (Build.VERSION.SDK_INT >= 16) {
                                _listFragment.getListView().setScrollBarFadeDuration(2000);
                            }
                            _listFragment.getListView().setVerticalScrollBarEnabled(true);
                            //_listFragment.getListView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
                        }
                    }

                    // Highlight item
                    if (_highlightIndex >= 0 && _highlightIndex < _items.size() && _listFragment.getListView().getChildAt(_highlightIndex) != null) {
                        final View highlightedItem = _listFragment.getListView().getChildAt(_highlightIndex).findViewById(_highlightElementId);
                        if (highlightedItem != null) {
                            highlightedItem.setBackgroundColor(_activity.getResources().getColor(_highlightColorId));
                        }
                    }

                    _listFragment.getListView().setFadingEdgeLength(AppUtil.dpToPixels(8));
                    _listFragment.getListView().setVerticalFadingEdgeEnabled(true);
                    _listFragment.getListView().setHeaderDividersEnabled(_showHeaderDividers);
                    _listFragment.getListView().setFooterDividersEnabled(_showFooterDividers);
                    _listFragment.getListView().setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, _listFragment.getListView().getLayoutParams().height));
                    _listFragment.getListView().setOnItemSelectedListener(_onItemSelectedInternal);
                    _listFragment.getListView().setOnItemClickListener(_onItemClickInternal);
                    _listFragment.getListView().setOnItemLongClickListener(_onItemLongClickInternal);
                }
            });

        _onDismissInternalAdd.add(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (_listFragment != null) {
                    _activity.getSupportFragmentManager().beginTransaction().remove(_listFragment).commit();
                    _listFragment = null;
                }
            }
        });

        _onCancelInternalAdd.add(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog) {
                if (_listFragment != null) {
                    _activity.getSupportFragmentManager().beginTransaction().remove(_listFragment).commit();
                    _listFragment = null;
                }
            }
        });

        _onItemClickInternalAdd.add(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (_listFragment != null) {
                    _listFragment.setSelection(position);
                }
            }
        });

        _onLayoutChangeInternalAdd.add(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int sizeDiff = (_titleView.getVisibility() != View.GONE) ? _titleView.getHeight() : 0;
                sizeDiff += (_messageView.getVisibility() != View.GONE) ? _messageView.getHeight() : 0;
                sizeDiff = (_btPos.getVisibility() != View.GONE || _btNeg.getVisibility() != View.GONE ||_btNeutral.getVisibility() != View.GONE) ? sizeDiff+AppUtil.dpToPixels(64) : 0;
                sizeDiff += 32; // Padding
                if ((sizeDiff != 0) && _listFragment != null && (_listFragment.getListView() != null) && (_listFragment.getListView().getHeight() > (v.getHeight() - sizeDiff))) {
                    //_listFragment.getListView().setLayoutParams(new ViewGroup.LayoutParams(_listFragment.getListView().getLayoutParams().width, v.getHeight() - sizeDiff));
                    _body.setLayoutParams(new LinearLayout.LayoutParams(_body.getLayoutParams().width, v.getHeight() - sizeDiff));
                }
            }
        });

        notifyDataSetChanged();
    }


    public PopupListDialog<T> notifyDataSetChanged() {
        _adapter.notifyDataSetChanged();
        updateText();
        return this;
    }


    public void updateText() {
        if (_emptyMessage.isEmpty() || !_items.isEmpty()) {
            _messageView.setText(_message);
        } else {
            _messageView.setText(_emptyMessage);
        }
        _messageView.setVisibility((_messageView.getText().length() == 0) ? View.GONE : View.VISIBLE);
    }


    //////////// Accessors ////////////

    public T getItem(@IntRange(from=0) int index) {
        if (index >= 0 && index < _items.size()) {
            return _items.get(index);
        }
        return null;
    }
    public String getEmptyMessage() { return _emptyMessage; }
    public LinearLayout linearLayout() { return (LinearLayout)_body; }
    @Override
    public String getMessage() { return _message; }
    public ListFragment getListFragment () { return _listFragment; }
    public AdapterView.OnItemSelectedListener getOnItemSelectListener() { return _onItemSelected; }
    public AdapterView.OnItemClickListener getOnItemClickListener() { return _onItemClick; }
    public AdapterView.OnItemLongClickListener getOnItemLongClickListener() { return _onItemLongClick; }
    public boolean getHighlightOnLongClick() { return _highlightOnLongClick; }

    public int getSelection() {
        if (_listFragment != null) {
            _listFragment.getSelectedItemPosition();
        }
        return -1;
    }

    public int getHighlighted() {
        return _highlightIndex;
    }


    //////////// Mutators ////////////

    public PopupListDialog<T> setHighlighted(@IntRange(from=-1) int index) {
        return setHighlighted(index, _highlightElementId);
    }

    public PopupListDialog<T> setHighlighted(@IntRange(from=-1) int index, @IdRes int backgroundViewId) {
        if (index < _items.size()) {
            _highlightElementId = backgroundViewId;
            if (_highlightIndex >= 0 && _listFragment != null && _listFragment.getListView() != null && _listFragment.getListView().getChildAt(_highlightIndex) != null) {
                // Un-highlight previous highlighted item
                final View oldHighlightedItem = _listFragment.getListView().getChildAt(_highlightIndex).findViewById(backgroundViewId);
                if (oldHighlightedItem != null) {
                    oldHighlightedItem.setBackgroundColor(_activity.getResources().getColor(R.color.transparent));
                }
            }
            _highlightIndex = (index >= -1) ? index : -1;
            if (_highlightIndex >= 0 && _listFragment != null && _listFragment.getListView() != null && _listFragment.getListView().getChildAt(_highlightIndex) != null) {
                // Highlight new item
                final View highlightedItem = _listFragment.getListView().getChildAt(_highlightIndex).findViewById(backgroundViewId);
                if (highlightedItem != null) {
                    highlightedItem.setBackgroundColor(_activity.getResources().getColor(_highlightColorId));
                }
            }
        }
        return this;
    }

    public PopupListDialog<T> setHighlightOnLongClick(boolean enable) {
        _highlightOnLongClick = enable;
        return this;
    }

    public PopupListDialog<T> setEmptyMessage(@Nullable String emptyMessage) {
        if (emptyMessage != null && !emptyMessage.isEmpty()) {
            _emptyMessage = emptyMessage;
        } else {
            _emptyMessage = "";
        }
        if (_emptyMessage.isEmpty() || !_items.isEmpty()) {
            _messageView.setText(_message);
        } else {
            _messageView.setText(_emptyMessage);
        }
        _messageView.setVisibility((_messageView.getText().length() == 0) ? View.GONE : View.VISIBLE);
        return this;
    }

    @Override
    public BasicDialog setMessage(@StringRes int id) {
        return setMessage(_activity.getString(id));
    }

    @Override
    public BasicDialog setMessage(String message) {
        if (message == null || message.isEmpty()) {
            _message = "";
        } else {
            _message = message;
        }
        if (_items.isEmpty() && !_emptyMessage.isEmpty()) {
            _messageView.setText(_emptyMessage);
        } else {
            _messageView.setText(_message);
        }
        _messageView.setVisibility((_messageView.getText().length() == 0) ? View.GONE : View.VISIBLE);
        return this;
    }

    public PopupListDialog<T> setHeaderDividersEnabled(boolean enabled) {
        _showHeaderDividers = enabled;
        if (_listFragment != null && _listFragment.getListView() != null) {
            _listFragment.getListView().setHeaderDividersEnabled(_showHeaderDividers);
        }
        return this;
    }

    public PopupListDialog<T> setFooterDividersEnabled(boolean enabled) {
        _showFooterDividers = enabled;
        if (_listFragment != null && _listFragment.getListView() != null) {
            _listFragment.getListView().setFooterDividersEnabled(_showFooterDividers);
        }
        return this;
    }

    public PopupListDialog<T> setOnItemSelectedListener(@Nullable AdapterView.OnItemSelectedListener listener) {
        _onItemSelected = listener;
        return this;
    }

    public PopupListDialog<T> setOnItemClickListener(@Nullable AdapterView.OnItemClickListener listener) {
        _onItemClick = listener;
        return this;
    }

    public PopupListDialog<T> setOnItemLongClickListener(@Nullable AdapterView.OnItemLongClickListener listener) {
        _onItemLongClick = listener;
        return this;
    }

    public PopupListDialog<T> setCloseOnItemSelected(boolean close) {
        _closeOnItemSelected = close;
        return this;
    }

    public PopupListDialog<T> setCloseOnNothingSelected(boolean close) {
        _closeOnNothingSelected = close;
        return this;
    }

    public PopupListDialog<T> setCloseOnItemClick(boolean close) {
        _closeOnItemClick = close;
        return this;
    }

    public PopupListDialog<T> setCloseOnItemLongClick(boolean close) {
        _closeOnItemLongClick = close;
        return this;
    }

    public PopupListDialog<T> setConsumeItemLongClicks(boolean close) {
        _consumeItemLongClicks = close;
        return this;
    }


    public static class ResourceParams {

        public int ITEM_LAYOUT_ID = -1;
        public int ITEM_TEXT_VIEW_ID = -1;
        public int DROP_DOWN_ITEM_LAYOUT_ID = -1;

        public ResourceParams() {}

        public ResourceParams(@LayoutRes int itemLayoutId) { this.ITEM_LAYOUT_ID = itemLayoutId; }
        public ResourceParams(@LayoutRes int itemLayoutId, @IdRes int itemTextViewId) {
            this.ITEM_LAYOUT_ID = itemLayoutId;
            this.ITEM_TEXT_VIEW_ID = itemTextViewId;
        }
        public ResourceParams(@LayoutRes int itemLayoutId, @IdRes int itemTextViewId, @LayoutRes int dropDownItemLayoutId) {
            this.ITEM_LAYOUT_ID = itemLayoutId;
            this.ITEM_TEXT_VIEW_ID = itemTextViewId;
            this.DROP_DOWN_ITEM_LAYOUT_ID = dropDownItemLayoutId;
        }

        public ResourceParams setLayoutId(@LayoutRes int itemLayoutId) {
            this.ITEM_LAYOUT_ID = itemLayoutId;
            return this;
        }

        public ResourceParams setTextViewId(int itemTextViewId) {
            this.ITEM_TEXT_VIEW_ID = itemTextViewId;
            return this;
        }

        public ResourceParams setDropDownId(@LayoutRes int dropDownItemLayoutId) {
            this.DROP_DOWN_ITEM_LAYOUT_ID = dropDownItemLayoutId;
            return this;
        }

    }


    //////////// Overrides ////////////

    @Override
    public PopupListDialog<T> showAndGetInstance() {
        show();
        return this;
    }

}
