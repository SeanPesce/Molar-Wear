package mw.molarwear.gui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mw.molarwear.R;
import mw.molarwear.util.AppUtil;

/**
 *
 *
 * @author Sean Pesce
 *
 * @see Dialog
 */

public class BasicDialog extends Dialog {

    final protected BasicDialog _this;
    final protected AppCompatActivity _activity;

    protected LinearLayout _primaryLayout;

    protected LinearLayout _titleBar;
    protected TextView _titleView;
    protected View _customTitle;

    protected ScrollView _messageScrollView;
    protected TextView _messageView;
    protected LinearLayout _body;

    protected ButtonBarLayout _buttonBar;
    protected Button _btPos;
    protected Button _btNeg;
    protected Button _btNeutral;

    protected String _message = "";
    protected int _maxWidth   = -1;
    protected int _maxHeight  = -1;

    protected View.OnClickListener _onClickPosBt;
    protected View.OnClickListener _onClickNegBt;
    protected View.OnClickListener _onClickNeutralBt;
    protected View.OnLayoutChangeListener _onLayoutChange;
    protected DialogInterface.OnShowListener _onShow;
    protected DialogInterface.OnDismissListener _onDismiss;
    protected DialogInterface.OnCancelListener _onCancel;

    final protected List<View.OnClickListener> _onClickPosInternalAdd = new ArrayList<>();
    final protected List<View.OnClickListener> _onClickNegInternalAdd = new ArrayList<>();
    final protected List<View.OnClickListener> _onClickNeutralInternalAdd = new ArrayList<>();
    final protected List<View.OnLayoutChangeListener> _onLayoutChangeInternalAdd = new ArrayList<>();
    final protected List<DialogInterface.OnShowListener> _onShowInternalAdd = new ArrayList<>();
    final protected List<DialogInterface.OnDismissListener> _onDismissInternalAdd = new ArrayList<>();
    final protected List<DialogInterface.OnCancelListener> _onCancelInternalAdd = new ArrayList<>();

    protected boolean _closeOnClickPos = true;
    protected boolean _closeOnClickNeg = true;
    protected boolean _closeOnClickNeutral = true;


    final protected View.OnClickListener _onClickPosInternal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (View.OnClickListener l : _onClickPosInternalAdd) {
                l.onClick(view);
            }
            if (_onClickPosBt != null) {
                _onClickPosBt.onClick(view);
            }
            if (_closeOnClickPos) {
                _this.dismiss();
            }
        }
    };

    final protected View.OnClickListener _onClickNegInternal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (View.OnClickListener l : _onClickNegInternalAdd) {
                l.onClick(view);
            }
            if (_onClickNegBt != null) {
                _onClickNegBt.onClick(view);
            }
            if (_closeOnClickNeg) {
                _this.dismiss();
            }
        }
    };

    final protected View.OnClickListener _onClickNeutralInternal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (View.OnClickListener l : _onClickNeutralInternalAdd) {
                l.onClick(view);
            }
            if (_onClickNeutralBt != null) {
                _onClickNeutralBt.onClick(view);
            }
            if (_closeOnClickNeutral) {
                _this.dismiss();
            }
        }
    };

    private boolean _ignoreNextLayoutChange = false;

    final protected View.OnLayoutChangeListener _onLayoutChangeInternal = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (_ignoreNextLayoutChange) {
                _ignoreNextLayoutChange = false;
            } else {
                updateWindowDimensions(true);
            }

            for (View.OnLayoutChangeListener l : _onLayoutChangeInternalAdd) {
                l.onLayoutChange(v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom);
            }
            if (_onLayoutChange != null) {
                _onLayoutChange.onLayoutChange(v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom);
            }
        }
    };

    final protected DialogInterface.OnShowListener _onShowInternal = new OnShowListener() {
        @Override
        public void onShow(DialogInterface dialog) {
//            Window window = _this.getWindow();
//            if (window != null) {
//                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//            }
            for (DialogInterface.OnShowListener l : _onShowInternalAdd) {
                l.onShow(dialog);
            }
            if (_onShow != null) {
                _onShow.onShow(dialog);
            }
            updateWindowDimensions();
        }
    };

    final protected DialogInterface.OnDismissListener _onDismissInternal = new OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            for (DialogInterface.OnDismissListener l : _onDismissInternalAdd) {
                l.onDismiss(dialog);
            }
            if (_onDismiss != null) {
                _onDismiss.onDismiss(dialog);
            }
        }
    };

    final protected DialogInterface.OnCancelListener _onCancelInternal = new OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            for (DialogInterface.OnCancelListener l : _onCancelInternalAdd) {
                l.onCancel(dialog);
            }
            if (_onCancel != null) {
                _onCancel.onCancel(dialog);
            }
        }
    };


    public BasicDialog(@NonNull AppCompatActivity activity) {
        super(activity);
        _this = this;
        _activity = activity;
        _primaryLayout = (LinearLayout) LayoutInflater.from(_activity).inflate(R.layout.dialog_template, null, false);
        initialize(null, null, 0, null);
    }

    public BasicDialog(@NonNull AppCompatActivity activity, @StyleRes int themeResId, @LayoutRes int bodyLayoutId) {
        super(activity, themeResId);
        _this = this;
        _activity = activity;
        _primaryLayout = (LinearLayout) LayoutInflater.from(_activity).inflate(R.layout.dialog_template, null, false);
        initialize(null, null, bodyLayoutId, null);
    }

    public BasicDialog(@NonNull AppCompatActivity activity, @NonNull ButtonParams btParams) {
        super(activity);
        _this = this;
        _activity = activity;
        _primaryLayout = (LinearLayout) LayoutInflater.from(_activity).inflate(R.layout.dialog_template, null, false);
        initialize(btParams, null, 0, null);
    }

    public BasicDialog(@NonNull AppCompatActivity activity, @LayoutRes int bodyLayoutId) {
        super(activity);
        _this = this;
        _activity = activity;
        _primaryLayout = (LinearLayout) LayoutInflater.from(_activity).inflate(R.layout.dialog_template, null, false);
        initialize(null, null, bodyLayoutId, null);
    }

    public BasicDialog(@NonNull AppCompatActivity activity, @NonNull ButtonParams btParams, @LayoutRes int bodyLayoutId) {
        super(activity);
        _this = this;
        _activity = activity;
        _primaryLayout = (LinearLayout) LayoutInflater.from(_activity).inflate(R.layout.dialog_template, null, false);
        initialize(btParams, null, bodyLayoutId, null);
    }

    public BasicDialog(@NonNull AppCompatActivity activity, @NonNull View bodyLayout) {
        super(activity);
        _this = this;
        _activity = activity;
        _primaryLayout = (LinearLayout) LayoutInflater.from(_activity).inflate(R.layout.dialog_template, null, false);
        initialize(null, null, 0, bodyLayout);
    }

    public BasicDialog(@NonNull AppCompatActivity activity, @NonNull ButtonParams btParams, @NonNull View bodyLayout) {
        super(activity);
        _this = this;
        _activity = activity;
        _primaryLayout = (LinearLayout) LayoutInflater.from(_activity).inflate(R.layout.dialog_template, null, false);
        initialize(btParams, null, 0, bodyLayout);
    }

    public BasicDialog(@NonNull DialogStringData strings) {
        super(strings.activity());
        _this = this;
        _activity = strings.activity();
        _primaryLayout = (LinearLayout) LayoutInflater.from(_activity).inflate(R.layout.dialog_template, null, false);
        initialize(null, strings, 0, null);
    }

    public BasicDialog(@NonNull DialogStringData strings, @LayoutRes int bodyLayoutId) {
        super(strings.activity());
        _this = this;
        _activity = strings.activity();
        _primaryLayout = (LinearLayout) LayoutInflater.from(_activity).inflate(R.layout.dialog_template, null, false);
        initialize(null, strings, bodyLayoutId, null);
    }


    protected void initialize(@Nullable ButtonParams btParams, @Nullable DialogStringData strings, @LayoutRes int bodyLayoutId, @Nullable View bodyLayout) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(_primaryLayout);

        setCancelable(true);
        //setCanceledOnTouchOutside(true);

        _titleBar  = _primaryLayout.findViewById(R.id.dlg_title_bar);
        _titleView = _titleBar.findViewById(R.id.dlg_title);
        _customTitle = null;

        _messageScrollView = _primaryLayout.findViewById(R.id.dlg_msg_scrollview);
        _body = _primaryLayout.findViewById(R.id.dlg_body);
        _messageView = _primaryLayout.findViewById(R.id.dlg_msg);

        _buttonBar = _primaryLayout.findViewById(R.id.dlg_button_bar);
        _btPos     = _buttonBar.findViewById(R.id.bt_positive);
        _btNeg     = _buttonBar.findViewById(R.id.bt_negative);
        _btNeutral = _buttonBar.findViewById(R.id.bt_neutral);

        _btPos.setOnClickListener(_onClickPosInternal);
        _btNeg.setOnClickListener(_onClickNegInternal);
        _btNeutral.setOnClickListener(_onClickNeutralInternal);
        super.setOnShowListener(_onShowInternal);
        super.setOnDismissListener(_onDismissInternal);
        super.setOnCancelListener(_onCancelInternal);

        _messageView.setText(_message);

        if (bodyLayoutId != 0) {
            View bodyLayoutInflated = LayoutInflater.from(_activity).inflate(bodyLayoutId, null, false);
            if (bodyLayoutInflated != null) {
                _body.addView(bodyLayoutInflated);
            }
        }
        if (bodyLayout != null) {
            _body.addView(bodyLayout);
        }

        final View dlgContainer = findViewById(R.id.dlg_main_container);
        if (dlgContainer != null) {
            dlgContainer.addOnLayoutChangeListener(_onLayoutChangeInternal);
        }

        setSize(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        DisplayMetrics screenDim = new DisplayMetrics();
        _activity.getWindowManager().getDefaultDisplay().getMetrics(screenDim);
        //setMaxHeight((int)(screenDim.heightPixels*0.8f));

        if (strings != null) {
            setStrings(strings);
        }
        setButtons(btParams);


        _titleBar.setVisibility((getTitle().isEmpty() || _customTitle != null) ? View.GONE : View.VISIBLE);
        _messageView.setVisibility((_messageView.getText().length() == 0) ? View.GONE : View.VISIBLE);
    }


    public BasicDialog showAndGetInstance() {
        show();
        return this;
    }

    public BasicDialog updateWindowDimensions() {
        return updateWindowDimensions(false);
    }

    public BasicDialog updateWindowDimensions(boolean calledFromLayoutChangeListener) {
        LinearLayout container = findViewById(R.id.dlg_main_container);
        if (container != null) {
            if ((_maxWidth >= 0 && container.getWidth() > _maxWidth) && (_maxHeight >= 0  && container.getHeight() > _maxHeight)) {
                _ignoreNextLayoutChange = calledFromLayoutChangeListener;
                setSize(_maxWidth, _maxHeight);
            } else if (_maxWidth >= 0 && container.getWidth() > _maxWidth) {
                _ignoreNextLayoutChange = calledFromLayoutChangeListener;
                setWidth(_maxWidth);
            } else if (_maxHeight >= 0 && container.getHeight() > _maxHeight) {
                _ignoreNextLayoutChange = calledFromLayoutChangeListener;
                setHeight(_maxHeight);
            }
        }
        return this;
    }

    public BasicDialog updateBodyView() {
        if (_body.getChildCount() == 0) {
            _body.setVisibility(View.GONE);
        } else {
            _body.setVisibility(View.VISIBLE);
        }
        return this;
    }


    //////////// Accessors ////////////

    public String getTitle() { return _titleView.getText().toString(); }
    public String getMessage() { return _messageView.getText().toString(); }
    public int getButtonBarChildCount() { return _buttonBar.getChildCount(); }


    //////////// Mutators ////////////

    public void setTitle(@StringRes int id) {
        setTitle(_activity.getString(id));
    }

    public BasicDialog setTitleAndGetInstance(@StringRes int id) {
        setTitle(_activity.getString(id));
        return this;
    }

    public BasicDialog setTitleAndGetInstance(String title) {
        return setTitle(title);
    }

    public BasicDialog setTitle(String title) {
        if (title == null || title.isEmpty()) {
            _titleView.setText("");
        } else {
            _titleView.setText(title);
        }
        _titleBar.setVisibility((getTitle().isEmpty() || _customTitle != null) ? View.GONE : View.VISIBLE);
        return this;
    }

    public BasicDialog setMessage(@StringRes int id) {
        return setMessage(_activity.getString(id));
    }

    public BasicDialog setMessage(String message) {
        if (message == null || message.isEmpty()) {
            _message = "";
            _messageView.setText("");
        } else {
            _message = message;
            _messageView.setText(message);
        }
        _messageView.setVisibility((_messageView.getText().length() == 0) ? View.GONE : View.VISIBLE);
        return this;
    }

    public BasicDialog addToBody(@LayoutRes int id) {
        View viewInflated = LayoutInflater.from(_activity).inflate(id, null, false);
        if (viewInflated != null) {
            return addToBody(viewInflated);
        }
        return this;
    }

    public BasicDialog addToBody(@NonNull View view) {
        _body.addView(view);
        updateBodyView();
        return this;
    }

    public BasicDialog addToButtonBar(@LayoutRes int id) {
        View viewInflated = LayoutInflater.from(_activity).inflate(id, null, false);
        if (viewInflated != null) {
            return addToButtonBar(viewInflated);
        }
        return this;
    }

    public BasicDialog addToButtonBar(@NonNull View view) {
        _buttonBar.addView(view);
        return this;
    }

    public BasicDialog setCustomTitle(@LayoutRes int id) {
        View viewInflated = LayoutInflater.from(_activity).inflate(id, null, false);
        if (viewInflated != null) {
            return setCustomTitle(viewInflated);
        }
        return this;
    }

    public BasicDialog setCustomTitle(@NonNull View customTitleView) {
        LinearLayout window = findViewById(R.id.dlg_main_container);

        if (_customTitle != null) {
            window.removeView(_customTitle);
            _customTitle = null;
        }
        if (_titleBar.getVisibility() == View.VISIBLE || _titleBar.getVisibility() == View.INVISIBLE) {
            _titleBar.setVisibility(View.GONE);
        }
        _customTitle = customTitleView;
        window.addView(_customTitle, 0);
        return this;
    }

    public BasicDialog setWidth(int width) {
        Window window = getWindow();
        if (window != null) {
            window.setLayout(width, getWindow().getAttributes().height);
            //_scrollView.setLayoutParams(new LinearLayout.LayoutParams((width > 0) ? width-1 : width, _scrollView.getLayoutParams().height));
        }
        return this;
    }

    public BasicDialog setHeight(int height) {
        Window window = getWindow();
        if (window != null) {
            window.setLayout(getWindow().getAttributes().width, height);
        }
        return this;
    }

    public BasicDialog setSize(int width, int height) {
        Window window = getWindow();
        if (window != null) {
            window.setLayout(width, height);
            //_scrollView.setLayoutParams(new LinearLayout.LayoutParams((width > 0) ? width-1 : width, _scrollView.getLayoutParams().height));
        }
        return this;
    }

    public BasicDialog setMaxWidth(int width) {
        _maxWidth = width;
        updateWindowDimensions();
        return this;
    }

    public BasicDialog setMaxHeight(int height) {
        _maxHeight = height;
        updateWindowDimensions();
        return this;
    }

    public BasicDialog setMaxSize(int width, int height) {
        _maxWidth = width;
        _maxHeight = height;
        updateWindowDimensions();
        return this;
    }

    public BasicDialog setMinWidth(@IntRange(from=0) int dp) {
        LinearLayout window = findViewById(R.id.dlg_main_container);
        if (window != null) {
            window.setMinimumWidth(AppUtil.dpToPixels(dp));
        }
        return this;
    }

    public BasicDialog setMinWidthPx(@IntRange(from=0) int px) {
        LinearLayout window = findViewById(R.id.dlg_main_container);
        if (window != null) {
            window.setMinimumWidth(px);
        }
        return this;
    }

    public BasicDialog setMinHeight(@IntRange(from=0) int dp) {
        LinearLayout window = findViewById(R.id.dlg_main_container);
        if (window != null) {
            window.setMinimumHeight(AppUtil.dpToPixels(dp));
        }
        return this;
    }

    public BasicDialog setMinHeightPx(@IntRange(from=0) int px) {
        LinearLayout window = findViewById(R.id.dlg_main_container);
        if (window != null) {
            window.setMinimumHeight(px);
        }
        return this;
    }

    public BasicDialog setMinSize(@IntRange(from=0) int dpWidth, @IntRange(from=0) int dpHeight) {
        LinearLayout window = findViewById(R.id.dlg_main_container);
        if (window != null) {
            window.setMinimumWidth(AppUtil.dpToPixels(dpWidth));
            window.setMinimumHeight(AppUtil.dpToPixels(dpHeight));
        }
        return this;
    }

    public BasicDialog setMinSizePx(@IntRange(from=0) int pxWidth, @IntRange(from=0) int pxHeight) {
        LinearLayout window = findViewById(R.id.dlg_main_container);
        if (window != null) {
            window.setMinimumWidth(pxWidth);
            window.setMinimumHeight(pxHeight);
        }
        return this;
    }

    @Override
    public void setOnShowListener(@Nullable DialogInterface.OnShowListener listener) {
        _onShow = listener;
    }

    public BasicDialog setOnShow(@Nullable DialogInterface.OnShowListener listener) {
        _onShow = listener;
        return this;
    }

    @Override
    public void setOnDismissListener(@Nullable DialogInterface.OnDismissListener listener) {
        _onDismiss = listener;
    }

    public BasicDialog setOnDismiss(@Nullable DialogInterface.OnDismissListener listener) {
        _onDismiss = listener;
        return this;
    }

    @Override
    public void setOnCancelListener(@Nullable DialogInterface.OnCancelListener listener) {
        _onCancel = listener;
    }

    public BasicDialog setOnCancel(@Nullable DialogInterface.OnCancelListener listener) {
        _onCancel = listener;
        return this;
    }

    public BasicDialog setOnLayoutChange(@Nullable View.OnLayoutChangeListener listener) {
        _onLayoutChange = listener;
        return this;
    }

    public BasicDialog usePosBt(boolean use) {
        _btPos.setVisibility(use ? View.VISIBLE : View.GONE);
        return this;
    }

    public BasicDialog useNegBt(boolean use) {
        _btNeg.setVisibility(use ? View.VISIBLE : View.GONE);
        return this;
    }

    public BasicDialog useNeutralBt(boolean use) {
        _btNeutral.setVisibility(use ? View.VISIBLE : View.GONE);
        return this;
    }

    public BasicDialog setCloseOnClickPos(boolean close) {
        _closeOnClickPos = close;
        return this;
    }

    public BasicDialog setCloseOnClickNeg(boolean close) {
        _closeOnClickNeg = close;
        return this;
    }

    public BasicDialog setCloseOnClickNeutral(boolean close) {
        _closeOnClickNeutral = close;
        return this;
    }

    public BasicDialog setPosBt(@StringRes int id) {
        final String txt = _activity.getString(id);
        if (txt != null) {
            setPosBt(txt);
        }
        return this;
    }

    public BasicDialog setPosBt(@Nullable String text) {
        if (text == null) {
            _btPos.setVisibility(View.GONE);
        } else {
            _btPos.setText(text);
        }
        return this;
    }

    public BasicDialog setPosBt(@Nullable View.OnClickListener listener) {
        _onClickPosBt = listener;
        return this;
    }

    public BasicDialog setPosBt(@StringRes int id, @Nullable View.OnClickListener listener) {
        setPosBt(id);
        return setPosBt(listener);
    }

    public BasicDialog setPosBt(@Nullable String text, @Nullable View.OnClickListener listener) {
        setPosBt(text);
        return setPosBt(listener);
    }

    public BasicDialog setNegBt(@StringRes int id) {
        final String txt = _activity.getString(id);
        if (txt != null) {
            setNegBt(txt);
        }
        return this;
    }

    public BasicDialog setNegBt(@Nullable String text) {
        if (text == null) {
            _btNeg.setVisibility(View.GONE);
        } else {
            _btNeg.setText(text);
        }
        return this;
    }

    public BasicDialog setNegBt(@Nullable View.OnClickListener listener) {
        _onClickNegBt = listener;
        return this;
    }

    public BasicDialog setNegBt(@StringRes int id, @Nullable View.OnClickListener listener) {
        setNegBt(id);
        return setNegBt(listener);
    }

    public BasicDialog setNegBt(@Nullable String text, @Nullable View.OnClickListener listener) {
        setNegBt(text);
        return setNegBt(listener);
    }

    public BasicDialog setNeutralBt(@StringRes int id) {
        final String txt = _activity.getString(id);
        if (txt != null) {
            setNeutralBt(txt);
        }
        return this;
    }

    public BasicDialog setNeutralBt(@Nullable String text) {
        if (text == null) {
            _btNeutral.setVisibility(View.GONE);
        } else {
            _btNeutral.setText(text);
        }
        return this;
    }

    public BasicDialog setNeutralBt(@Nullable View.OnClickListener listener) {
        _onClickNeutralBt = listener;
        return this;
    }

    public BasicDialog setNeutralBt(@StringRes int id, @Nullable View.OnClickListener listener) {
        setNeutralBt(id);
        return setNeutralBt(listener);
    }

    public BasicDialog setNeutralBt(@Nullable String text, @Nullable View.OnClickListener listener) {
        setNeutralBt(text);
        return setNeutralBt(listener);
    }

    public BasicDialog setStrings(@NonNull DialogStringData strings) {
        if (strings.title() != null) {
            setTitle(strings.title());
        }
        if (strings.message() != null) {
            setMessage(strings.message());
        }
        if (strings.posBtText() != null) {
            setPosBt(strings.posBtText());
        }
        if (strings.negBtText() != null) {
            setNegBt(strings.negBtText());
        }
        return this;
    }

    public BasicDialog setButtons(@Nullable ButtonParams btParams) {
        if (btParams != null) {
            _btPos.setVisibility(btParams.USE_POSITIVE_BT ? View.VISIBLE : View.GONE);
            _btNeg.setVisibility(btParams.USE_NEGATIVE_BT ? View.VISIBLE : View.GONE);
            _btNeutral.setVisibility(btParams.USE_NEUTRAL_BT ? View.VISIBLE : View.GONE);
            if (btParams.POSITIVE_BT_TXT != null) {
                setPosBt(btParams.POSITIVE_BT_TXT);
            }
            if (btParams.NEGATIVE_BT_TXT != null) {
                setNegBt(btParams.NEGATIVE_BT_TXT);
            }
            if (btParams.NEUTRAL_BT_TXT != null) {
                setNeutralBt(btParams.NEUTRAL_BT_TXT);
            }
            if (btParams.NULLABLE_LISTENERS || btParams.POS_BT_ON_CLICK != null) {
                setPosBt(btParams.POS_BT_ON_CLICK);
            }
            if (btParams.NULLABLE_LISTENERS || btParams.NEG_BT_ON_CLICK != null) {
                setNegBt(btParams.NEG_BT_ON_CLICK);
            }
            if (btParams.NULLABLE_LISTENERS || btParams.NEUTRAL_BT_ON_CLICK != null) {
                setNeutralBt(btParams.NEUTRAL_BT_ON_CLICK);
            }
        }
        return this;
    }



    public static class ButtonParams {
        public boolean USE_POSITIVE_BT = true;
        public boolean USE_NEGATIVE_BT = true;
        public boolean USE_NEUTRAL_BT  = false;
        public String  POSITIVE_BT_TXT = null;
        public String  NEGATIVE_BT_TXT = null;
        public String  NEUTRAL_BT_TXT  = null;
        public View.OnClickListener POS_BT_ON_CLICK     = null;
        public View.OnClickListener NEG_BT_ON_CLICK     = null;
        public View.OnClickListener NEUTRAL_BT_ON_CLICK = null;

        public boolean NULLABLE_LISTENERS = false;

        public ButtonParams() {}
        public ButtonParams(boolean usePosBt, boolean useNegBt, boolean useNeutralBt) {
            USE_POSITIVE_BT = usePosBt;
            USE_NEGATIVE_BT = useNegBt;
            USE_NEUTRAL_BT  = useNeutralBt;
        }

        public ButtonParams(boolean usePosBt, boolean useNegBt, boolean useNeutralBt,
                            View.OnClickListener posListener, View.OnClickListener negListener, View.OnClickListener neutralListener) {
            USE_POSITIVE_BT = usePosBt;
            USE_NEGATIVE_BT = useNegBt;
            USE_NEUTRAL_BT  = useNeutralBt;
            POS_BT_ON_CLICK = posListener;
            NEG_BT_ON_CLICK = negListener;
            NEUTRAL_BT_ON_CLICK = neutralListener;
        }

        public ButtonParams(boolean usePosBt, boolean useNegBt, boolean useNeutralBt,
                            String posBtTxt, String negBtTxt, String neutralBtTxt) {
            USE_POSITIVE_BT = usePosBt;
            USE_NEGATIVE_BT = useNegBt;
            USE_NEUTRAL_BT  = useNeutralBt;
            POSITIVE_BT_TXT = posBtTxt;
            NEGATIVE_BT_TXT = negBtTxt;
            NEUTRAL_BT_TXT  = neutralBtTxt;
        }

        public ButtonParams(boolean usePosBt, boolean useNegBt, boolean useNeutralBt,
                            String posBtTxt, String negBtTxt, String neutralBtTxt,
                            View.OnClickListener posListener, View.OnClickListener negListener, View.OnClickListener neutralListener) {
            USE_POSITIVE_BT = usePosBt;
            USE_NEGATIVE_BT = useNegBt;
            USE_NEUTRAL_BT  = useNeutralBt;
            POSITIVE_BT_TXT = posBtTxt;
            NEGATIVE_BT_TXT = negBtTxt;
            NEUTRAL_BT_TXT  = neutralBtTxt;
            POS_BT_ON_CLICK = posListener;
            NEG_BT_ON_CLICK = negListener;
            NEUTRAL_BT_ON_CLICK = neutralListener;
        }

        public ButtonParams(boolean usePosBt, boolean useNegBt, boolean useNeutralBt,
                            String posBtTxt, String negBtTxt, String neutralBtTxt,
                            View.OnClickListener posListener, View.OnClickListener negListener, View.OnClickListener neutralListener,
                            boolean nullableListeners) {
            USE_POSITIVE_BT = usePosBt;
            USE_NEGATIVE_BT = useNegBt;
            USE_NEUTRAL_BT  = useNeutralBt;
            POSITIVE_BT_TXT = posBtTxt;
            NEGATIVE_BT_TXT = negBtTxt;
            NEUTRAL_BT_TXT  = neutralBtTxt;
            POS_BT_ON_CLICK = posListener;
            NEG_BT_ON_CLICK = negListener;
            NEUTRAL_BT_ON_CLICK = neutralListener;
            NULLABLE_LISTENERS  = nullableListeners;
        }

        public ButtonParams setNullableListeners(boolean nullableListeners) {
            NULLABLE_LISTENERS = nullableListeners;
            return this;
        }
    }
}
