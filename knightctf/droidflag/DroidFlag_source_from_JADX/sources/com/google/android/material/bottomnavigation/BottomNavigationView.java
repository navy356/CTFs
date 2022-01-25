package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.C0460R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class BottomNavigationView extends FrameLayout {
    private static final int DEF_STYLE_RES = C0460R.style.Widget_Design_BottomNavigationView;
    private static final int MENU_PRESENTER_ID = 1;
    private ColorStateList itemRippleColor;
    private final MenuBuilder menu;
    private MenuInflater menuInflater;
    final BottomNavigationMenuView menuView;
    private final BottomNavigationPresenter presenter;
    /* access modifiers changed from: private */
    public OnNavigationItemReselectedListener reselectedListener;
    /* access modifiers changed from: private */
    public OnNavigationItemSelectedListener selectedListener;

    public interface OnNavigationItemReselectedListener {
        void onNavigationItemReselected(MenuItem menuItem);
    }

    public interface OnNavigationItemSelectedListener {
        boolean onNavigationItemSelected(MenuItem menuItem);
    }

    public int getMaxItemCount() {
        return 5;
    }

    public BottomNavigationView(Context context) {
        this(context, (AttributeSet) null);
    }

    public BottomNavigationView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0460R.attr.bottomNavigationStyle);
    }

    public BottomNavigationView(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, i, DEF_STYLE_RES), attributeSet, i);
        BottomNavigationPresenter bottomNavigationPresenter = new BottomNavigationPresenter();
        this.presenter = bottomNavigationPresenter;
        Context context2 = getContext();
        BottomNavigationMenu bottomNavigationMenu = new BottomNavigationMenu(context2);
        this.menu = bottomNavigationMenu;
        BottomNavigationMenuView bottomNavigationMenuView = new BottomNavigationMenuView(context2);
        this.menuView = bottomNavigationMenuView;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        bottomNavigationMenuView.setLayoutParams(layoutParams);
        bottomNavigationPresenter.setBottomNavigationMenuView(bottomNavigationMenuView);
        bottomNavigationPresenter.setId(1);
        bottomNavigationMenuView.setPresenter(bottomNavigationPresenter);
        bottomNavigationMenu.addMenuPresenter(bottomNavigationPresenter);
        bottomNavigationPresenter.initForMenu(getContext(), bottomNavigationMenu);
        TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(context2, attributeSet, C0460R.styleable.BottomNavigationView, i, C0460R.style.Widget_Design_BottomNavigationView, C0460R.styleable.BottomNavigationView_itemTextAppearanceInactive, C0460R.styleable.BottomNavigationView_itemTextAppearanceActive);
        if (obtainTintedStyledAttributes.hasValue(C0460R.styleable.BottomNavigationView_itemIconTint)) {
            bottomNavigationMenuView.setIconTintList(obtainTintedStyledAttributes.getColorStateList(C0460R.styleable.BottomNavigationView_itemIconTint));
        } else {
            bottomNavigationMenuView.setIconTintList(bottomNavigationMenuView.createDefaultColorStateList(16842808));
        }
        setItemIconSize(obtainTintedStyledAttributes.getDimensionPixelSize(C0460R.styleable.BottomNavigationView_itemIconSize, getResources().getDimensionPixelSize(C0460R.dimen.design_bottom_navigation_icon_size)));
        if (obtainTintedStyledAttributes.hasValue(C0460R.styleable.BottomNavigationView_itemTextAppearanceInactive)) {
            setItemTextAppearanceInactive(obtainTintedStyledAttributes.getResourceId(C0460R.styleable.BottomNavigationView_itemTextAppearanceInactive, 0));
        }
        if (obtainTintedStyledAttributes.hasValue(C0460R.styleable.BottomNavigationView_itemTextAppearanceActive)) {
            setItemTextAppearanceActive(obtainTintedStyledAttributes.getResourceId(C0460R.styleable.BottomNavigationView_itemTextAppearanceActive, 0));
        }
        if (obtainTintedStyledAttributes.hasValue(C0460R.styleable.BottomNavigationView_itemTextColor)) {
            setItemTextColor(obtainTintedStyledAttributes.getColorStateList(C0460R.styleable.BottomNavigationView_itemTextColor));
        }
        if (getBackground() == null || (getBackground() instanceof ColorDrawable)) {
            ViewCompat.setBackground(this, createMaterialShapeDrawableBackground(context2));
        }
        if (obtainTintedStyledAttributes.hasValue(C0460R.styleable.BottomNavigationView_elevation)) {
            setElevation((float) obtainTintedStyledAttributes.getDimensionPixelSize(C0460R.styleable.BottomNavigationView_elevation, 0));
        }
        DrawableCompat.setTintList(getBackground().mutate(), MaterialResources.getColorStateList(context2, obtainTintedStyledAttributes, C0460R.styleable.BottomNavigationView_backgroundTint));
        setLabelVisibilityMode(obtainTintedStyledAttributes.getInteger(C0460R.styleable.BottomNavigationView_labelVisibilityMode, -1));
        setItemHorizontalTranslationEnabled(obtainTintedStyledAttributes.getBoolean(C0460R.styleable.BottomNavigationView_itemHorizontalTranslationEnabled, true));
        int resourceId = obtainTintedStyledAttributes.getResourceId(C0460R.styleable.BottomNavigationView_itemBackground, 0);
        if (resourceId != 0) {
            bottomNavigationMenuView.setItemBackgroundRes(resourceId);
        } else {
            setItemRippleColor(MaterialResources.getColorStateList(context2, obtainTintedStyledAttributes, C0460R.styleable.BottomNavigationView_itemRippleColor));
        }
        if (obtainTintedStyledAttributes.hasValue(C0460R.styleable.BottomNavigationView_menu)) {
            inflateMenu(obtainTintedStyledAttributes.getResourceId(C0460R.styleable.BottomNavigationView_menu, 0));
        }
        obtainTintedStyledAttributes.recycle();
        addView(bottomNavigationMenuView, layoutParams);
        if (shouldDrawCompatibilityTopDivider()) {
            addCompatibilityTopDivider(context2);
        }
        bottomNavigationMenu.setCallback(new MenuBuilder.Callback() {
            public void onMenuModeChange(MenuBuilder menuBuilder) {
            }

            public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                if (BottomNavigationView.this.reselectedListener != null && menuItem.getItemId() == BottomNavigationView.this.getSelectedItemId()) {
                    BottomNavigationView.this.reselectedListener.onNavigationItemReselected(menuItem);
                    return true;
                } else if (BottomNavigationView.this.selectedListener == null || BottomNavigationView.this.selectedListener.onNavigationItemSelected(menuItem)) {
                    return false;
                } else {
                    return true;
                }
            }
        });
        applyWindowInsets();
    }

    private void applyWindowInsets() {
        ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
                relativePadding.bottom += windowInsetsCompat.getSystemWindowInsetBottom();
                boolean z = true;
                if (ViewCompat.getLayoutDirection(view) != 1) {
                    z = false;
                }
                int systemWindowInsetLeft = windowInsetsCompat.getSystemWindowInsetLeft();
                int systemWindowInsetRight = windowInsetsCompat.getSystemWindowInsetRight();
                relativePadding.start += z ? systemWindowInsetRight : systemWindowInsetLeft;
                int i = relativePadding.end;
                if (!z) {
                    systemWindowInsetLeft = systemWindowInsetRight;
                }
                relativePadding.end = i + systemWindowInsetLeft;
                relativePadding.applyToView(view);
                return windowInsetsCompat;
            }
        });
    }

    private MaterialShapeDrawable createMaterialShapeDrawableBackground(Context context) {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            materialShapeDrawable.setFillColor(ColorStateList.valueOf(((ColorDrawable) background).getColor()));
        }
        materialShapeDrawable.initializeElevationOverlay(context);
        return materialShapeDrawable;
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

    public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.selectedListener = onNavigationItemSelectedListener;
    }

    public void setOnNavigationItemReselectedListener(OnNavigationItemReselectedListener onNavigationItemReselectedListener) {
        this.reselectedListener = onNavigationItemReselectedListener;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public void inflateMenu(int i) {
        this.presenter.setUpdateSuspended(true);
        getMenuInflater().inflate(i, this.menu);
        this.presenter.setUpdateSuspended(false);
        this.presenter.updateMenuView(true);
    }

    public ColorStateList getItemIconTintList() {
        return this.menuView.getIconTintList();
    }

    public void setItemIconTintList(ColorStateList colorStateList) {
        this.menuView.setIconTintList(colorStateList);
    }

    public void setItemIconSize(int i) {
        this.menuView.setItemIconSize(i);
    }

    public void setItemIconSizeRes(int i) {
        setItemIconSize(getResources().getDimensionPixelSize(i));
    }

    public int getItemIconSize() {
        return this.menuView.getItemIconSize();
    }

    public ColorStateList getItemTextColor() {
        return this.menuView.getItemTextColor();
    }

    public void setItemTextColor(ColorStateList colorStateList) {
        this.menuView.setItemTextColor(colorStateList);
    }

    @Deprecated
    public int getItemBackgroundResource() {
        return this.menuView.getItemBackgroundRes();
    }

    public void setItemBackgroundResource(int i) {
        this.menuView.setItemBackgroundRes(i);
        this.itemRippleColor = null;
    }

    public Drawable getItemBackground() {
        return this.menuView.getItemBackground();
    }

    public void setItemBackground(Drawable drawable) {
        this.menuView.setItemBackground(drawable);
        this.itemRippleColor = null;
    }

    public ColorStateList getItemRippleColor() {
        return this.itemRippleColor;
    }

    public void setItemRippleColor(ColorStateList colorStateList) {
        if (this.itemRippleColor != colorStateList) {
            this.itemRippleColor = colorStateList;
            if (colorStateList == null) {
                this.menuView.setItemBackground((Drawable) null);
                return;
            }
            ColorStateList convertToRippleDrawableColor = RippleUtils.convertToRippleDrawableColor(colorStateList);
            if (Build.VERSION.SDK_INT >= 21) {
                this.menuView.setItemBackground(new RippleDrawable(convertToRippleDrawableColor, (Drawable) null, (Drawable) null));
                return;
            }
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius(1.0E-5f);
            Drawable wrap = DrawableCompat.wrap(gradientDrawable);
            DrawableCompat.setTintList(wrap, convertToRippleDrawableColor);
            this.menuView.setItemBackground(wrap);
        } else if (colorStateList == null && this.menuView.getItemBackground() != null) {
            this.menuView.setItemBackground((Drawable) null);
        }
    }

    public int getSelectedItemId() {
        return this.menuView.getSelectedItemId();
    }

    public void setSelectedItemId(int i) {
        MenuItem findItem = this.menu.findItem(i);
        if (findItem != null && !this.menu.performItemAction(findItem, this.presenter, 0)) {
            findItem.setChecked(true);
        }
    }

    public void setLabelVisibilityMode(int i) {
        if (this.menuView.getLabelVisibilityMode() != i) {
            this.menuView.setLabelVisibilityMode(i);
            this.presenter.updateMenuView(false);
        }
    }

    public int getLabelVisibilityMode() {
        return this.menuView.getLabelVisibilityMode();
    }

    public void setItemTextAppearanceInactive(int i) {
        this.menuView.setItemTextAppearanceInactive(i);
    }

    public int getItemTextAppearanceInactive() {
        return this.menuView.getItemTextAppearanceInactive();
    }

    public void setItemTextAppearanceActive(int i) {
        this.menuView.setItemTextAppearanceActive(i);
    }

    public int getItemTextAppearanceActive() {
        return this.menuView.getItemTextAppearanceActive();
    }

    public void setItemHorizontalTranslationEnabled(boolean z) {
        if (this.menuView.isItemHorizontalTranslationEnabled() != z) {
            this.menuView.setItemHorizontalTranslationEnabled(z);
            this.presenter.updateMenuView(false);
        }
    }

    public boolean isItemHorizontalTranslationEnabled() {
        return this.menuView.isItemHorizontalTranslationEnabled();
    }

    public void setItemOnTouchListener(int i, View.OnTouchListener onTouchListener) {
        this.menuView.setItemOnTouchListener(i, onTouchListener);
    }

    public BadgeDrawable getBadge(int i) {
        return this.menuView.getBadge(i);
    }

    public BadgeDrawable getOrCreateBadge(int i) {
        return this.menuView.getOrCreateBadge(i);
    }

    public void removeBadge(int i) {
        this.menuView.removeBadge(i);
    }

    private boolean shouldDrawCompatibilityTopDivider() {
        return Build.VERSION.SDK_INT < 21 && !(getBackground() instanceof MaterialShapeDrawable);
    }

    private void addCompatibilityTopDivider(Context context) {
        View view = new View(context);
        view.setBackgroundColor(ContextCompat.getColor(context, C0460R.C0461color.design_bottom_navigation_shadow_color));
        view.setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(C0460R.dimen.design_bottom_navigation_shadow_height)));
        addView(view);
    }

    private MenuInflater getMenuInflater() {
        if (this.menuInflater == null) {
            this.menuInflater = new SupportMenuInflater(getContext());
        }
        return this.menuInflater;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuPresenterState = new Bundle();
        this.menu.savePresenterStates(savedState.menuPresenterState);
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
        this.menu.restorePresenterStates(savedState.menuPresenterState);
    }

    static class SavedState extends AbsSavedState {
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
        Bundle menuPresenterState;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            readFromParcel(parcel, classLoader == null ? getClass().getClassLoader() : classLoader);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeBundle(this.menuPresenterState);
        }

        private void readFromParcel(Parcel parcel, ClassLoader classLoader) {
            this.menuPresenterState = parcel.readBundle(classLoader);
        }
    }
}
