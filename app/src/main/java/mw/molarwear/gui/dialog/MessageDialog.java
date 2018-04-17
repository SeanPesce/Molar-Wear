package mw.molarwear.gui.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import mw.molarwear.R;
import mw.molarwear.util.AppUtil;

public class MessageDialog extends BasicDialog {

    public MessageDialog(@NonNull AppCompatActivity activity) {
        super(activity);
        initialize(null);
    }

    public MessageDialog(@NonNull DialogStringData stringData) {
        super(stringData);

        initialize(stringData);
    }

    public MessageDialog(@NonNull AppCompatActivity activity, @NonNull String title) {
        super(new DialogStringData(activity, title));
        initialize(null);
    }

    public MessageDialog(@NonNull AppCompatActivity activity, @StringRes int titleId) {
        super(new DialogStringData(activity, titleId));
        initialize(null);
    }

    public MessageDialog(@NonNull AppCompatActivity activity, @NonNull String title, @NonNull String message) {
        super(new DialogStringData(activity, title, message));
        initialize(null);
    }

    public MessageDialog(@NonNull AppCompatActivity activity, @NonNull String title, @StringRes int messageId) {
        super(new DialogStringData(activity, title, messageId));
        initialize(null);
    }

    public MessageDialog(@NonNull AppCompatActivity activity, @StringRes int titleId, @NonNull String message) {
        super(new DialogStringData(activity, titleId, message));
        initialize(null);
    }

    public MessageDialog(@NonNull AppCompatActivity activity, @StringRes int titleId, @StringRes int messageId) {
        super(new DialogStringData(activity, titleId, messageId));
        initialize(null);
    }


    private void initialize(@Nullable DialogStringData stringData) {
        useNeutralBt(false);
        if (stringData == null) {
            usePosBt(true);
            useNegBt(false);
            setPosBt(R.string.dlg_bt_ok);
        } else {
            usePosBt(stringData.posBtText() != null && !stringData.posBtText().isEmpty());
            useNegBt(stringData.negBtText() != null && !stringData.negBtText().isEmpty());
        }

        _onLayoutChangeInternalAdd.add(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int sizeDiff = (_titleView.getVisibility() != View.GONE) ? _titleView.getHeight() : 0;
                sizeDiff += (_body.getVisibility() != View.GONE) ? _body.getHeight() : 0;
                sizeDiff = (_btPos.getVisibility() != View.GONE || _btNeg.getVisibility() != View.GONE ||_btNeutral.getVisibility() != View.GONE) ? sizeDiff+AppUtil.dpToPixels(64) : 0;
                if ((sizeDiff != 0) && (_messageScrollView.getHeight() > (v.getHeight() - sizeDiff))) {
                    _messageScrollView.setLayoutParams(new LinearLayout.LayoutParams(_messageScrollView.getLayoutParams().width, v.getHeight() - sizeDiff));
                }
            }
        });

        //show();
    }
}
