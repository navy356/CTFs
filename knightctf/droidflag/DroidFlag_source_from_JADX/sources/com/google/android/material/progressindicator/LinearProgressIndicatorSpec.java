package com.google.android.material.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.google.android.material.C0460R;
import com.google.android.material.internal.ThemeEnforcement;

public final class LinearProgressIndicatorSpec extends BaseProgressIndicatorSpec {
    boolean drawHorizontallyInverse;
    public int indeterminateAnimationType;
    public int indicatorDirection;

    public LinearProgressIndicatorSpec(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0460R.attr.linearProgressIndicatorStyle);
    }

    public LinearProgressIndicatorSpec(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, LinearProgressIndicator.DEF_STYLE_RES);
    }

    public LinearProgressIndicatorSpec(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        boolean z = false;
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, C0460R.styleable.LinearProgressIndicator, C0460R.attr.linearProgressIndicatorStyle, LinearProgressIndicator.DEF_STYLE_RES, new int[0]);
        this.indeterminateAnimationType = obtainStyledAttributes.getInt(C0460R.styleable.LinearProgressIndicator_indeterminateAnimationType, 1);
        this.indicatorDirection = obtainStyledAttributes.getInt(C0460R.styleable.LinearProgressIndicator_indicatorDirectionLinear, 0);
        obtainStyledAttributes.recycle();
        validateSpec();
        this.drawHorizontallyInverse = this.indicatorDirection == 1 ? true : z;
    }

    /* access modifiers changed from: package-private */
    public void validateSpec() {
        if (this.indeterminateAnimationType != 0) {
            return;
        }
        if (this.trackCornerRadius > 0) {
            throw new IllegalArgumentException("Rounded corners are not supported in contiguous indeterminate animation.");
        } else if (this.indicatorColors.length < 3) {
            throw new IllegalArgumentException("Contiguous indeterminate animation must be used with 3 or more indicator colors.");
        }
    }
}
