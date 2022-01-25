package com.google.android.material.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.appcompat.C0010R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.TintTypedArray;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.C0460R;
import com.google.android.material.internal.ContextUtils;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.internal.NavigationMenuPresenter;
import com.google.android.material.internal.ScrimInsetsFrameLayout;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;

public class NavigationView extends ScrimInsetsFrameLayout {
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final int DEF_STYLE_RES = C0460R.style.Widget_Design_NavigationView;
    private static final int[] DISABLED_STATE_SET = {-16842910};
    private static final int PRESENTER_NAVIGATION_VIEW_ID = 1;
    OnNavigationItemSelectedListener listener;
    private final int maxWidth;
    private final NavigationMenu menu;
    private MenuInflater menuInflater;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    /* access modifiers changed from: private */
    public final NavigationMenuPresenter presenter;
    /* access modifiers changed from: private */
    public final int[] tmpLocation;

    public interface OnNavigationItemSelectedListener {
        boolean onNavigationItemSelected(MenuItem menuItem);
    }

    public NavigationView(Context context) {
        this(context, (AttributeSet) null);
    }

    public NavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0460R.attr.navigationViewStyle);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public NavigationView(android.content.Context r11, android.util.AttributeSet r12, int r13) {
        /*
            r10 = this;
            int r6 = DEF_STYLE_RES
            android.content.Context r11 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r11, r12, r13, r6)
            r10.<init>(r11, r12, r13)
            com.google.android.material.internal.NavigationMenuPresenter r11 = new com.google.android.material.internal.NavigationMenuPresenter
            r11.<init>()
            r10.presenter = r11
            r0 = 2
            int[] r0 = new int[r0]
            r10.tmpLocation = r0
            android.content.Context r7 = r10.getContext()
            com.google.android.material.internal.NavigationMenu r8 = new com.google.android.material.internal.NavigationMenu
            r8.<init>(r7)
            r10.menu = r8
            int[] r2 = com.google.android.material.C0460R.styleable.NavigationView
            r9 = 0
            int[] r5 = new int[r9]
            r0 = r7
            r1 = r12
            r3 = r13
            r4 = r6
            androidx.appcompat.widget.TintTypedArray r0 = com.google.android.material.internal.ThemeEnforcement.obtainTintedStyledAttributes(r0, r1, r2, r3, r4, r5)
            int r1 = com.google.android.material.C0460R.styleable.NavigationView_android_background
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x003e
            int r1 = com.google.android.material.C0460R.styleable.NavigationView_android_background
            android.graphics.drawable.Drawable r1 = r0.getDrawable(r1)
            androidx.core.view.ViewCompat.setBackground(r10, r1)
        L_0x003e:
            android.graphics.drawable.Drawable r1 = r10.getBackground()
            if (r1 == 0) goto L_0x004c
            android.graphics.drawable.Drawable r1 = r10.getBackground()
            boolean r1 = r1 instanceof android.graphics.drawable.ColorDrawable
            if (r1 == 0) goto L_0x0074
        L_0x004c:
            com.google.android.material.shape.ShapeAppearanceModel$Builder r12 = com.google.android.material.shape.ShapeAppearanceModel.builder((android.content.Context) r7, (android.util.AttributeSet) r12, (int) r13, (int) r6)
            com.google.android.material.shape.ShapeAppearanceModel r12 = r12.build()
            android.graphics.drawable.Drawable r13 = r10.getBackground()
            com.google.android.material.shape.MaterialShapeDrawable r1 = new com.google.android.material.shape.MaterialShapeDrawable
            r1.<init>((com.google.android.material.shape.ShapeAppearanceModel) r12)
            boolean r12 = r13 instanceof android.graphics.drawable.ColorDrawable
            if (r12 == 0) goto L_0x006e
            android.graphics.drawable.ColorDrawable r13 = (android.graphics.drawable.ColorDrawable) r13
            int r12 = r13.getColor()
            android.content.res.ColorStateList r12 = android.content.res.ColorStateList.valueOf(r12)
            r1.setFillColor(r12)
        L_0x006e:
            r1.initializeElevationOverlay(r7)
            androidx.core.view.ViewCompat.setBackground(r10, r1)
        L_0x0074:
            int r12 = com.google.android.material.C0460R.styleable.NavigationView_elevation
            boolean r12 = r0.hasValue(r12)
            if (r12 == 0) goto L_0x0086
            int r12 = com.google.android.material.C0460R.styleable.NavigationView_elevation
            int r12 = r0.getDimensionPixelSize(r12, r9)
            float r12 = (float) r12
            r10.setElevation(r12)
        L_0x0086:
            int r12 = com.google.android.material.C0460R.styleable.NavigationView_android_fitsSystemWindows
            boolean r12 = r0.getBoolean(r12, r9)
            r10.setFitsSystemWindows(r12)
            int r12 = com.google.android.material.C0460R.styleable.NavigationView_android_maxWidth
            int r12 = r0.getDimensionPixelSize(r12, r9)
            r10.maxWidth = r12
            int r12 = com.google.android.material.C0460R.styleable.NavigationView_itemIconTint
            boolean r12 = r0.hasValue(r12)
            if (r12 == 0) goto L_0x00a6
            int r12 = com.google.android.material.C0460R.styleable.NavigationView_itemIconTint
            android.content.res.ColorStateList r12 = r0.getColorStateList(r12)
            goto L_0x00ad
        L_0x00a6:
            r12 = 16842808(0x1010038, float:2.3693715E-38)
            android.content.res.ColorStateList r12 = r10.createDefaultColorStateList(r12)
        L_0x00ad:
            int r13 = com.google.android.material.C0460R.styleable.NavigationView_itemTextAppearance
            boolean r13 = r0.hasValue(r13)
            r1 = 1
            if (r13 == 0) goto L_0x00be
            int r13 = com.google.android.material.C0460R.styleable.NavigationView_itemTextAppearance
            int r13 = r0.getResourceId(r13, r9)
            r2 = r1
            goto L_0x00c0
        L_0x00be:
            r13 = r9
            r2 = r13
        L_0x00c0:
            int r3 = com.google.android.material.C0460R.styleable.NavigationView_itemIconSize
            boolean r3 = r0.hasValue(r3)
            if (r3 == 0) goto L_0x00d1
            int r3 = com.google.android.material.C0460R.styleable.NavigationView_itemIconSize
            int r3 = r0.getDimensionPixelSize(r3, r9)
            r10.setItemIconSize(r3)
        L_0x00d1:
            r3 = 0
            int r4 = com.google.android.material.C0460R.styleable.NavigationView_itemTextColor
            boolean r4 = r0.hasValue(r4)
            if (r4 == 0) goto L_0x00e0
            int r3 = com.google.android.material.C0460R.styleable.NavigationView_itemTextColor
            android.content.res.ColorStateList r3 = r0.getColorStateList(r3)
        L_0x00e0:
            if (r2 != 0) goto L_0x00eb
            if (r3 != 0) goto L_0x00eb
            r3 = 16842806(0x1010036, float:2.369371E-38)
            android.content.res.ColorStateList r3 = r10.createDefaultColorStateList(r3)
        L_0x00eb:
            int r4 = com.google.android.material.C0460R.styleable.NavigationView_itemBackground
            android.graphics.drawable.Drawable r4 = r0.getDrawable(r4)
            if (r4 != 0) goto L_0x00fd
            boolean r5 = r10.hasShapeAppearance(r0)
            if (r5 == 0) goto L_0x00fd
            android.graphics.drawable.Drawable r4 = r10.createDefaultItemBackground(r0)
        L_0x00fd:
            int r5 = com.google.android.material.C0460R.styleable.NavigationView_itemHorizontalPadding
            boolean r5 = r0.hasValue(r5)
            if (r5 == 0) goto L_0x010e
            int r5 = com.google.android.material.C0460R.styleable.NavigationView_itemHorizontalPadding
            int r5 = r0.getDimensionPixelSize(r5, r9)
            r11.setItemHorizontalPadding(r5)
        L_0x010e:
            int r5 = com.google.android.material.C0460R.styleable.NavigationView_itemIconPadding
            int r5 = r0.getDimensionPixelSize(r5, r9)
            int r6 = com.google.android.material.C0460R.styleable.NavigationView_itemMaxLines
            int r6 = r0.getInt(r6, r1)
            r10.setItemMaxLines(r6)
            com.google.android.material.navigation.NavigationView$1 r6 = new com.google.android.material.navigation.NavigationView$1
            r6.<init>()
            r8.setCallback(r6)
            r11.setId(r1)
            r11.initForMenu(r7, r8)
            r11.setItemIconTintList(r12)
            int r12 = r10.getOverScrollMode()
            r11.setOverScrollMode(r12)
            if (r2 == 0) goto L_0x013a
            r11.setItemTextAppearance(r13)
        L_0x013a:
            r11.setItemTextColor(r3)
            r11.setItemBackground(r4)
            r11.setItemIconPadding(r5)
            r8.addMenuPresenter(r11)
            androidx.appcompat.view.menu.MenuView r11 = r11.getMenuView(r10)
            android.view.View r11 = (android.view.View) r11
            r10.addView(r11)
            int r11 = com.google.android.material.C0460R.styleable.NavigationView_menu
            boolean r11 = r0.hasValue(r11)
            if (r11 == 0) goto L_0x0160
            int r11 = com.google.android.material.C0460R.styleable.NavigationView_menu
            int r11 = r0.getResourceId(r11, r9)
            r10.inflateMenu(r11)
        L_0x0160:
            int r11 = com.google.android.material.C0460R.styleable.NavigationView_headerLayout
            boolean r11 = r0.hasValue(r11)
            if (r11 == 0) goto L_0x0171
            int r11 = com.google.android.material.C0460R.styleable.NavigationView_headerLayout
            int r11 = r0.getResourceId(r11, r9)
            r10.inflateHeaderView(r11)
        L_0x0171:
            r0.recycle()
            r10.setupInsetScrimsListener()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.navigation.NavigationView.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    public void setOverScrollMode(int i) {
        super.setOverScrollMode(i);
        NavigationMenuPresenter navigationMenuPresenter = this.presenter;
        if (navigationMenuPresenter != null) {
            navigationMenuPresenter.setOverScrollMode(i);
        }
    }

    private boolean hasShapeAppearance(TintTypedArray tintTypedArray) {
        return tintTypedArray.hasValue(C0460R.styleable.NavigationView_itemShapeAppearance) || tintTypedArray.hasValue(C0460R.styleable.NavigationView_itemShapeAppearanceOverlay);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation(this);
    }

    public void setElevation(float f) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.setElevation(f);
        }
        MaterialShapeUtils.setElevation(this, f);
    }

    private final Drawable createDefaultItemBackground(TintTypedArray tintTypedArray) {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(ShapeAppearanceModel.builder(getContext(), tintTypedArray.getResourceId(C0460R.styleable.NavigationView_itemShapeAppearance, 0), tintTypedArray.getResourceId(C0460R.styleable.NavigationView_itemShapeAppearanceOverlay, 0)).build());
        materialShapeDrawable.setFillColor(MaterialResources.getColorStateList(getContext(), tintTypedArray, C0460R.styleable.NavigationView_itemShapeFillColor));
        return new InsetDrawable(materialShapeDrawable, tintTypedArray.getDimensionPixelSize(C0460R.styleable.NavigationView_itemShapeInsetStart, 0), tintTypedArray.getDimensionPixelSize(C0460R.styleable.NavigationView_itemShapeInsetTop, 0), tintTypedArray.getDimensionPixelSize(C0460R.styleable.NavigationView_itemShapeInsetEnd, 0), tintTypedArray.getDimensionPixelSize(C0460R.styleable.NavigationView_itemShapeInsetBottom, 0));
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuState = new Bundle();
        this.menu.savePresenterStates(savedState.menuState);
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.menu.restorePresenterStates(savedState.menuState);
    }

    public void setNavigationItemSelectedListener(OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.listener = onNavigationItemSelectedListener;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        if (mode == Integer.MIN_VALUE) {
            i = View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(i), this.maxWidth), BasicMeasure.EXACTLY);
        } else if (mode == 0) {
            i = View.MeasureSpec.makeMeasureSpec(this.maxWidth, BasicMeasure.EXACTLY);
        }
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onInsetsChanged(WindowInsetsCompat windowInsetsCompat) {
        this.presenter.dispatchApplyWindowInsets(windowInsetsCompat);
    }

    public void inflateMenu(int i) {
        this.presenter.setUpdateSuspended(true);
        getMenuInflater().inflate(i, this.menu);
        this.presenter.setUpdateSuspended(false);
        this.presenter.updateMenuView(false);
    }

    public Menu getMenu() {
        return this.menu;
    }

    public View inflateHeaderView(int i) {
        return this.presenter.inflateHeaderView(i);
    }

    public void addHeaderView(View view) {
        this.presenter.addHeaderView(view);
    }

    public void removeHeaderView(View view) {
        this.presenter.removeHeaderView(view);
    }

    public int getHeaderCount() {
        return this.presenter.getHeaderCount();
    }

    public View getHeaderView(int i) {
        return this.presenter.getHeaderView(i);
    }

    public ColorStateList getItemIconTintList() {
        return this.presenter.getItemTintList();
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.presenter.setItemIconTintList(colorStateList);
    }

    public ColorStateList getItemTextColor() {
        return this.presenter.getItemTextColor();
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.presenter.setItemTextColor(colorStateList);
    }

    public Drawable getItemBackground() {
        return this.presenter.getItemBackground();
    }

    public void setItemBackgroundResource(int i) {
        setItemBackground(ContextCompat.getDrawable(getContext(), i));
    }

    public void setItemBackground(Drawable drawable) {
        this.presenter.setItemBackground(drawable);
    }

    public int getItemHorizontalPadding() {
        return this.presenter.getItemHorizontalPadding();
    }

    public void setItemHorizontalPadding(int i) {
        this.presenter.setItemHorizontalPadding(i);
    }

    public void setItemHorizontalPaddingResource(int i) {
        this.presenter.setItemHorizontalPadding(getResources().getDimensionPixelSize(i));
    }

    public int getItemIconPadding() {
        return this.presenter.getItemIconPadding();
    }

    public void setItemIconPadding(int i) {
        this.presenter.setItemIconPadding(i);
    }

    public void setItemIconPaddingResource(int i) {
        this.presenter.setItemIconPadding(getResources().getDimensionPixelSize(i));
    }

    public void setCheckedItem(int i) {
        MenuItem findItem = this.menu.findItem(i);
        if (findItem != null) {
            this.presenter.setCheckedItem((MenuItemImpl) findItem);
        }
    }

    public void setCheckedItem(MenuItem menuItem) {
        MenuItem findItem = this.menu.findItem(menuItem.getItemId());
        if (findItem != null) {
            this.presenter.setCheckedItem((MenuItemImpl) findItem);
            return;
        }
        throw new IllegalArgumentException("Called setCheckedItem(MenuItem) with an item that is not in the current menu.");
    }

    public MenuItem getCheckedItem() {
        return this.presenter.getCheckedItem();
    }

    public void setItemTextAppearance(int i) {
        this.presenter.setItemTextAppearance(i);
    }

    public void setItemIconSize(int i) {
        this.presenter.setItemIconSize(i);
    }

    public void setItemMaxLines(int i) {
        this.presenter.setItemMaxLines(i);
    }

    public int getItemMaxLines() {
        return this.presenter.getItemMaxLines();
    }

    private MenuInflater getMenuInflater() {
        if (this.menuInflater == null) {
            this.menuInflater = new SupportMenuInflater(getContext());
        }
        return this.menuInflater;
    }

    private ColorStateList createDefaultColorStateList(int i) {
        TypedValue typedValue = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(i, typedValue, true)) {
            return null;
        }
        ColorStateList colorStateList = AppCompatResources.getColorStateList(getContext(), typedValue.resourceId);
        if (!getContext().getTheme().resolveAttribute(C0010R.attr.colorPrimary, typedValue, true)) {
            return null;
        }
        int i2 = typedValue.data;
        int defaultColor = colorStateList.getDefaultColor();
        int[] iArr = DISABLED_STATE_SET;
        return new ColorStateList(new int[][]{iArr, CHECKED_STATE_SET, EMPTY_STATE_SET}, new int[]{colorStateList.getColorForState(iArr, defaultColor), i2, defaultColor});
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT < 16) {
            getViewTreeObserver().removeGlobalOnLayoutListener(this.onGlobalLayoutListener);
        } else {
            getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
        }
    }

    private void setupInsetScrimsListener() {
        this.onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                NavigationView navigationView = NavigationView.this;
                navigationView.getLocationOnScreen(navigationView.tmpLocation);
                boolean z = true;
                boolean z2 = NavigationView.this.tmpLocation[1] == 0;
                NavigationView.this.presenter.setBehindStatusBar(z2);
                NavigationView.this.setDrawTopInsetForeground(z2);
                Activity activity = ContextUtils.getActivity(NavigationView.this.getContext());
                if (activity != null && Build.VERSION.SDK_INT >= 21) {
                    boolean z3 = activity.findViewById(16908290).getHeight() == NavigationView.this.getHeight();
                    boolean z4 = Color.alpha(activity.getWindow().getNavigationBarColor()) != 0;
                    NavigationView navigationView2 = NavigationView.this;
                    if (!z3 || !z4) {
                        z = false;
                    }
                    navigationView2.setDrawBottomInsetForeground(z);
                }
            }
        };
        getViewTreeObserver().addOnGlobalLayoutListener(this.onGlobalLayoutListener);
    }

    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, (ClassLoader) null);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public Bundle menuState;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.menuState = parcel.readBundle(classLoader);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeBundle(this.menuState);
        }
    }
}
