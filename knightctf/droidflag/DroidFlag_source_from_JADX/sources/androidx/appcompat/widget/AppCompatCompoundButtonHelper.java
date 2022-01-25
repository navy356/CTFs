package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;

class AppCompatCompoundButtonHelper {
    private ColorStateList mButtonTintList = null;
    private PorterDuff.Mode mButtonTintMode = null;
    private boolean mHasButtonTint = false;
    private boolean mHasButtonTintMode = false;
    private boolean mSkipNextApply;
    private final CompoundButton mView;

    AppCompatCompoundButtonHelper(CompoundButton compoundButton) {
        this.mView = compoundButton;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0041 A[SYNTHETIC, Splitter:B:12:0x0041] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0066 A[Catch:{ all -> 0x008e }] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0079 A[Catch:{ all -> 0x008e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadFromAttributes(android.util.AttributeSet r11, int r12) {
        /*
            r10 = this;
            android.widget.CompoundButton r0 = r10.mView
            android.content.Context r0 = r0.getContext()
            int[] r1 = androidx.appcompat.C0010R.styleable.CompoundButton
            r2 = 0
            androidx.appcompat.widget.TintTypedArray r0 = androidx.appcompat.widget.TintTypedArray.obtainStyledAttributes(r0, r11, r1, r12, r2)
            android.widget.CompoundButton r3 = r10.mView
            android.content.Context r4 = r3.getContext()
            int[] r5 = androidx.appcompat.C0010R.styleable.CompoundButton
            android.content.res.TypedArray r7 = r0.getWrappedTypeArray()
            r9 = 0
            r6 = r11
            r8 = r12
            androidx.core.view.ViewCompat.saveAttributeDataForStyleable(r3, r4, r5, r6, r7, r8, r9)
            int r11 = androidx.appcompat.C0010R.styleable.CompoundButton_buttonCompat     // Catch:{ all -> 0x008e }
            boolean r11 = r0.hasValue(r11)     // Catch:{ all -> 0x008e }
            if (r11 == 0) goto L_0x003e
            int r11 = androidx.appcompat.C0010R.styleable.CompoundButton_buttonCompat     // Catch:{ all -> 0x008e }
            int r11 = r0.getResourceId(r11, r2)     // Catch:{ all -> 0x008e }
            if (r11 == 0) goto L_0x003e
            android.widget.CompoundButton r12 = r10.mView     // Catch:{ NotFoundException -> 0x003e }
            android.content.Context r1 = r12.getContext()     // Catch:{ NotFoundException -> 0x003e }
            android.graphics.drawable.Drawable r11 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r1, r11)     // Catch:{ NotFoundException -> 0x003e }
            r12.setButtonDrawable(r11)     // Catch:{ NotFoundException -> 0x003e }
            r11 = 1
            goto L_0x003f
        L_0x003e:
            r11 = r2
        L_0x003f:
            if (r11 != 0) goto L_0x005e
            int r11 = androidx.appcompat.C0010R.styleable.CompoundButton_android_button     // Catch:{ all -> 0x008e }
            boolean r11 = r0.hasValue(r11)     // Catch:{ all -> 0x008e }
            if (r11 == 0) goto L_0x005e
            int r11 = androidx.appcompat.C0010R.styleable.CompoundButton_android_button     // Catch:{ all -> 0x008e }
            int r11 = r0.getResourceId(r11, r2)     // Catch:{ all -> 0x008e }
            if (r11 == 0) goto L_0x005e
            android.widget.CompoundButton r12 = r10.mView     // Catch:{ all -> 0x008e }
            android.content.Context r1 = r12.getContext()     // Catch:{ all -> 0x008e }
            android.graphics.drawable.Drawable r11 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r1, r11)     // Catch:{ all -> 0x008e }
            r12.setButtonDrawable(r11)     // Catch:{ all -> 0x008e }
        L_0x005e:
            int r11 = androidx.appcompat.C0010R.styleable.CompoundButton_buttonTint     // Catch:{ all -> 0x008e }
            boolean r11 = r0.hasValue(r11)     // Catch:{ all -> 0x008e }
            if (r11 == 0) goto L_0x0071
            android.widget.CompoundButton r11 = r10.mView     // Catch:{ all -> 0x008e }
            int r12 = androidx.appcompat.C0010R.styleable.CompoundButton_buttonTint     // Catch:{ all -> 0x008e }
            android.content.res.ColorStateList r12 = r0.getColorStateList(r12)     // Catch:{ all -> 0x008e }
            androidx.core.widget.CompoundButtonCompat.setButtonTintList(r11, r12)     // Catch:{ all -> 0x008e }
        L_0x0071:
            int r11 = androidx.appcompat.C0010R.styleable.CompoundButton_buttonTintMode     // Catch:{ all -> 0x008e }
            boolean r11 = r0.hasValue(r11)     // Catch:{ all -> 0x008e }
            if (r11 == 0) goto L_0x008a
            android.widget.CompoundButton r11 = r10.mView     // Catch:{ all -> 0x008e }
            int r12 = androidx.appcompat.C0010R.styleable.CompoundButton_buttonTintMode     // Catch:{ all -> 0x008e }
            r1 = -1
            int r12 = r0.getInt(r12, r1)     // Catch:{ all -> 0x008e }
            r1 = 0
            android.graphics.PorterDuff$Mode r12 = androidx.appcompat.widget.DrawableUtils.parseTintMode(r12, r1)     // Catch:{ all -> 0x008e }
            androidx.core.widget.CompoundButtonCompat.setButtonTintMode(r11, r12)     // Catch:{ all -> 0x008e }
        L_0x008a:
            r0.recycle()
            return
        L_0x008e:
            r11 = move-exception
            r0.recycle()
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatCompoundButtonHelper.loadFromAttributes(android.util.AttributeSet, int):void");
    }

    /* access modifiers changed from: package-private */
    public void setSupportButtonTintList(ColorStateList colorStateList) {
        this.mButtonTintList = colorStateList;
        this.mHasButtonTint = true;
        applyButtonTint();
    }

    /* access modifiers changed from: package-private */
    public ColorStateList getSupportButtonTintList() {
        return this.mButtonTintList;
    }

    /* access modifiers changed from: package-private */
    public void setSupportButtonTintMode(PorterDuff.Mode mode) {
        this.mButtonTintMode = mode;
        this.mHasButtonTintMode = true;
        applyButtonTint();
    }

    /* access modifiers changed from: package-private */
    public PorterDuff.Mode getSupportButtonTintMode() {
        return this.mButtonTintMode;
    }

    /* access modifiers changed from: package-private */
    public void onSetButtonDrawable() {
        if (this.mSkipNextApply) {
            this.mSkipNextApply = false;
            return;
        }
        this.mSkipNextApply = true;
        applyButtonTint();
    }

    /* access modifiers changed from: package-private */
    public void applyButtonTint() {
        Drawable buttonDrawable = CompoundButtonCompat.getButtonDrawable(this.mView);
        if (buttonDrawable == null) {
            return;
        }
        if (this.mHasButtonTint || this.mHasButtonTintMode) {
            Drawable mutate = DrawableCompat.wrap(buttonDrawable).mutate();
            if (this.mHasButtonTint) {
                DrawableCompat.setTintList(mutate, this.mButtonTintList);
            }
            if (this.mHasButtonTintMode) {
                DrawableCompat.setTintMode(mutate, this.mButtonTintMode);
            }
            if (mutate.isStateful()) {
                mutate.setState(this.mView.getDrawableState());
            }
            this.mView.setButtonDrawable(mutate);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r0 = androidx.core.widget.CompoundButtonCompat.getButtonDrawable(r2.mView);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getCompoundPaddingLeft(int r3) {
        /*
            r2 = this;
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 17
            if (r0 >= r1) goto L_0x0013
            android.widget.CompoundButton r0 = r2.mView
            android.graphics.drawable.Drawable r0 = androidx.core.widget.CompoundButtonCompat.getButtonDrawable(r0)
            if (r0 == 0) goto L_0x0013
            int r0 = r0.getIntrinsicWidth()
            int r3 = r3 + r0
        L_0x0013:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatCompoundButtonHelper.getCompoundPaddingLeft(int):int");
    }
}
