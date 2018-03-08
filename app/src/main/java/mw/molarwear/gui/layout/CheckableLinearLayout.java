package mw.molarwear.gui.layout;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Checkable;

import mw.molarwear.R;

/**
 * A selectable extension of {@link LinearLayout} that changes the background color when selected.
 *
 * @author <a href="https://developer.android.com/guide/components/fragments.html">Android SDK Sample</a>
 * @author Sean Pesce
 *
 * @see LinearLayout
 * @see Checkable
 */

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    private boolean _checked;

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        _checked = checked;
        setBackgroundColor(checked ? ContextCompat.getColor(getContext(), R.color.colorPrimaryLight4)
                                   : Color.TRANSPARENT);
    }

    @Override
    public boolean isChecked() {
        return _checked;
    }

    @Override
    public void toggle() {
        setChecked(!_checked);
    }
}
