package com.google.android.material.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.google.android.material.C0460R;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;

public final class CircularProgressIndicatorSpec extends BaseProgressIndicatorSpec {
    public int indicatorDirection;
    public int indicatorInset;
    public int indicatorSize;

    public CircularProgressIndicatorSpec(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0460R.attr.circularProgressIndicatorStyle);
    }

    public CircularProgressIndicatorSpec(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, CircularProgressIndicator.DEF_STYLE_RES);
    }

    public CircularProgressIndicatorSpec(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C0460R.dimen.mtrl_progress_circular_size_medium);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(C0460R.dimen.mtrl_progress_circular_inset_medium);
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, C0460R.styleable.CircularProgressIndicator, i, i2, new int[0]);
        this.indicatorSize = MaterialResources.getDimensionPixelSize(context, obtainStyledAttributes, C0460R.styleable.CircularProgressIndicator_indicatorSize, dimensionPixelSize);
        this.indicatorInset = MaterialResources.getDimensionPixelSize(context, obtainStyledAttributes, C0460R.styleable.CircularProgressIndicator_indicatorInset, dimensionPixelSize2);
        this.indicatorDirection = obtainStyledAttributes.getInt(C0460R.styleable.CircularProgressIndicator_indicatorDirectionCircular, 0);
        obtainStyledAttributes.recycle();
        validateSpec();
    }

    /* access modifiers changed from: package-private */
    public void validateSpec() {
        if (this.indicatorSize < this.trackThickness * 2) {
            throw new IllegalArgumentException("The indicatorSize (" + this.indicatorSize + " px) cannot be less than twice of the trackThickness (" + this.trackThickness + " px).");
        }
    }
}
