package com.google.android.material.tabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Pools;
import androidx.core.view.GravityCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.C0460R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.shape.MaterialShapeUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@ViewPager.DecorView
public class TabLayout extends HorizontalScrollView {
    private static final int ANIMATION_DURATION = 300;
    static final int DEFAULT_GAP_TEXT_ICON = 8;
    private static final int DEFAULT_HEIGHT = 48;
    private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72;
    private static final int DEF_STYLE_RES = C0460R.style.Widget_Design_TabLayout;
    static final int FIXED_WRAP_GUTTER_MIN = 16;
    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_FILL = 0;
    public static final int GRAVITY_START = 2;
    public static final int INDICATOR_ANIMATION_MODE_ELASTIC = 1;
    public static final int INDICATOR_ANIMATION_MODE_LINEAR = 0;
    public static final int INDICATOR_GRAVITY_BOTTOM = 0;
    public static final int INDICATOR_GRAVITY_CENTER = 1;
    public static final int INDICATOR_GRAVITY_STRETCH = 3;
    public static final int INDICATOR_GRAVITY_TOP = 2;
    private static final int INVALID_WIDTH = -1;
    private static final String LOG_TAG = "TabLayout";
    public static final int MODE_AUTO = 2;
    public static final int MODE_FIXED = 1;
    public static final int MODE_SCROLLABLE = 0;
    public static final int TAB_LABEL_VISIBILITY_LABELED = 1;
    public static final int TAB_LABEL_VISIBILITY_UNLABELED = 0;
    private static final int TAB_MIN_WIDTH_MARGIN = 56;
    private static final Pools.Pool<Tab> tabPool = new Pools.SynchronizedPool(16);
    private AdapterChangeListener adapterChangeListener;
    private int contentInsetStart;
    private BaseOnTabSelectedListener currentVpSelectedListener;
    boolean inlineLabel;
    int mode;
    private TabLayoutOnPageChangeListener pageChangeListener;
    private PagerAdapter pagerAdapter;
    private DataSetObserver pagerAdapterObserver;
    private final int requestedTabMaxWidth;
    private final int requestedTabMinWidth;
    private ValueAnimator scrollAnimator;
    private final int scrollableTabMinWidth;
    private BaseOnTabSelectedListener selectedListener;
    private final ArrayList<BaseOnTabSelectedListener> selectedListeners;
    private Tab selectedTab;
    private boolean setupViewPagerImplicitly;
    final SlidingTabIndicator slidingTabIndicator;
    final int tabBackgroundResId;
    int tabGravity;
    ColorStateList tabIconTint;
    PorterDuff.Mode tabIconTintMode;
    int tabIndicatorAnimationDuration;
    int tabIndicatorAnimationMode;
    boolean tabIndicatorFullWidth;
    int tabIndicatorGravity;
    /* access modifiers changed from: private */
    public TabIndicatorInterpolator tabIndicatorInterpolator;
    int tabMaxWidth;
    int tabPaddingBottom;
    int tabPaddingEnd;
    int tabPaddingStart;
    int tabPaddingTop;
    ColorStateList tabRippleColorStateList;
    Drawable tabSelectedIndicator;
    /* access modifiers changed from: private */
    public int tabSelectedIndicatorColor;
    int tabTextAppearance;
    ColorStateList tabTextColors;
    float tabTextMultiLineSize;
    float tabTextSize;
    private final Pools.Pool<TabView> tabViewPool;
    private final ArrayList<Tab> tabs;
    boolean unboundedRipple;
    ViewPager viewPager;

    @Deprecated
    public interface BaseOnTabSelectedListener<T extends Tab> {
        void onTabReselected(T t);

        void onTabSelected(T t);

        void onTabUnselected(T t);
    }

    public @interface LabelVisibility {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public interface OnTabSelectedListener extends BaseOnTabSelectedListener<Tab> {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TabGravity {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TabIndicatorAnimationMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TabIndicatorGravity {
    }

    public TabLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public TabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0460R.attr.tabStyle);
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public TabLayout(android.content.Context r11, android.util.AttributeSet r12, int r13) {
        /*
            r10 = this;
            int r4 = DEF_STYLE_RES
            android.content.Context r11 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r11, r12, r13, r4)
            r10.<init>(r11, r12, r13)
            java.util.ArrayList r11 = new java.util.ArrayList
            r11.<init>()
            r10.tabs = r11
            android.graphics.drawable.GradientDrawable r11 = new android.graphics.drawable.GradientDrawable
            r11.<init>()
            r10.tabSelectedIndicator = r11
            r11 = 0
            r10.tabSelectedIndicatorColor = r11
            r0 = 2147483647(0x7fffffff, float:NaN)
            r10.tabMaxWidth = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r10.selectedListeners = r0
            androidx.core.util.Pools$SimplePool r0 = new androidx.core.util.Pools$SimplePool
            r1 = 12
            r0.<init>(r1)
            r10.tabViewPool = r0
            android.content.Context r6 = r10.getContext()
            r10.setHorizontalScrollBarEnabled(r11)
            com.google.android.material.tabs.TabLayout$SlidingTabIndicator r7 = new com.google.android.material.tabs.TabLayout$SlidingTabIndicator
            r7.<init>(r6)
            r10.slidingTabIndicator = r7
            android.widget.FrameLayout$LayoutParams r0 = new android.widget.FrameLayout$LayoutParams
            r1 = -2
            r8 = -1
            r0.<init>(r1, r8)
            super.addView(r7, r11, r0)
            int[] r2 = com.google.android.material.C0460R.styleable.TabLayout
            r9 = 1
            int[] r5 = new int[r9]
            int r0 = com.google.android.material.C0460R.styleable.TabLayout_tabTextAppearance
            r5[r11] = r0
            r0 = r6
            r1 = r12
            r3 = r13
            android.content.res.TypedArray r12 = com.google.android.material.internal.ThemeEnforcement.obtainStyledAttributes(r0, r1, r2, r3, r4, r5)
            android.graphics.drawable.Drawable r13 = r10.getBackground()
            boolean r13 = r13 instanceof android.graphics.drawable.ColorDrawable
            if (r13 == 0) goto L_0x0082
            android.graphics.drawable.Drawable r13 = r10.getBackground()
            android.graphics.drawable.ColorDrawable r13 = (android.graphics.drawable.ColorDrawable) r13
            com.google.android.material.shape.MaterialShapeDrawable r0 = new com.google.android.material.shape.MaterialShapeDrawable
            r0.<init>()
            int r13 = r13.getColor()
            android.content.res.ColorStateList r13 = android.content.res.ColorStateList.valueOf(r13)
            r0.setFillColor(r13)
            r0.initializeElevationOverlay(r6)
            float r13 = androidx.core.view.ViewCompat.getElevation(r10)
            r0.setElevation(r13)
            androidx.core.view.ViewCompat.setBackground(r10, r0)
        L_0x0082:
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabIndicator
            android.graphics.drawable.Drawable r13 = com.google.android.material.resources.MaterialResources.getDrawable(r6, r12, r13)
            r10.setSelectedTabIndicator((android.graphics.drawable.Drawable) r13)
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabIndicatorColor
            int r13 = r12.getColor(r13, r11)
            r10.setSelectedTabIndicatorColor(r13)
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabIndicatorHeight
            int r13 = r12.getDimensionPixelSize(r13, r8)
            r7.setSelectedIndicatorHeight(r13)
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabIndicatorGravity
            int r13 = r12.getInt(r13, r11)
            r10.setSelectedTabIndicatorGravity(r13)
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabIndicatorFullWidth
            boolean r13 = r12.getBoolean(r13, r9)
            r10.setTabIndicatorFullWidth(r13)
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabIndicatorAnimationMode
            int r13 = r12.getInt(r13, r11)
            r10.setTabIndicatorAnimationMode(r13)
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabPadding
            int r13 = r12.getDimensionPixelSize(r13, r11)
            r10.tabPaddingBottom = r13
            r10.tabPaddingEnd = r13
            r10.tabPaddingTop = r13
            r10.tabPaddingStart = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabPaddingStart
            int r0 = r10.tabPaddingStart
            int r13 = r12.getDimensionPixelSize(r13, r0)
            r10.tabPaddingStart = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabPaddingTop
            int r0 = r10.tabPaddingTop
            int r13 = r12.getDimensionPixelSize(r13, r0)
            r10.tabPaddingTop = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabPaddingEnd
            int r0 = r10.tabPaddingEnd
            int r13 = r12.getDimensionPixelSize(r13, r0)
            r10.tabPaddingEnd = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabPaddingBottom
            int r0 = r10.tabPaddingBottom
            int r13 = r12.getDimensionPixelSize(r13, r0)
            r10.tabPaddingBottom = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabTextAppearance
            int r0 = com.google.android.material.C0460R.style.TextAppearance_Design_Tab
            int r13 = r12.getResourceId(r13, r0)
            r10.tabTextAppearance = r13
            int[] r0 = androidx.appcompat.C0010R.styleable.TextAppearance
            android.content.res.TypedArray r13 = r6.obtainStyledAttributes(r13, r0)
            int r0 = androidx.appcompat.C0010R.styleable.TextAppearance_android_textSize     // Catch:{ all -> 0x01bf }
            int r0 = r13.getDimensionPixelSize(r0, r11)     // Catch:{ all -> 0x01bf }
            float r0 = (float) r0     // Catch:{ all -> 0x01bf }
            r10.tabTextSize = r0     // Catch:{ all -> 0x01bf }
            int r0 = androidx.appcompat.C0010R.styleable.TextAppearance_android_textColor     // Catch:{ all -> 0x01bf }
            android.content.res.ColorStateList r0 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r6, (android.content.res.TypedArray) r13, (int) r0)     // Catch:{ all -> 0x01bf }
            r10.tabTextColors = r0     // Catch:{ all -> 0x01bf }
            r13.recycle()
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabTextColor
            boolean r13 = r12.hasValue(r13)
            if (r13 == 0) goto L_0x0122
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabTextColor
            android.content.res.ColorStateList r13 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r6, (android.content.res.TypedArray) r12, (int) r13)
            r10.tabTextColors = r13
        L_0x0122:
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabSelectedTextColor
            boolean r13 = r12.hasValue(r13)
            if (r13 == 0) goto L_0x013c
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabSelectedTextColor
            int r13 = r12.getColor(r13, r11)
            android.content.res.ColorStateList r0 = r10.tabTextColors
            int r0 = r0.getDefaultColor()
            android.content.res.ColorStateList r13 = createColorStateList(r0, r13)
            r10.tabTextColors = r13
        L_0x013c:
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabIconTint
            android.content.res.ColorStateList r13 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r6, (android.content.res.TypedArray) r12, (int) r13)
            r10.tabIconTint = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabIconTintMode
            int r13 = r12.getInt(r13, r8)
            r0 = 0
            android.graphics.PorterDuff$Mode r13 = com.google.android.material.internal.ViewUtils.parseTintMode(r13, r0)
            r10.tabIconTintMode = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabRippleColor
            android.content.res.ColorStateList r13 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r6, (android.content.res.TypedArray) r12, (int) r13)
            r10.tabRippleColorStateList = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabIndicatorAnimationDuration
            r0 = 300(0x12c, float:4.2E-43)
            int r13 = r12.getInt(r13, r0)
            r10.tabIndicatorAnimationDuration = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabMinWidth
            int r13 = r12.getDimensionPixelSize(r13, r8)
            r10.requestedTabMinWidth = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabMaxWidth
            int r13 = r12.getDimensionPixelSize(r13, r8)
            r10.requestedTabMaxWidth = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabBackground
            int r13 = r12.getResourceId(r13, r11)
            r10.tabBackgroundResId = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabContentStart
            int r13 = r12.getDimensionPixelSize(r13, r11)
            r10.contentInsetStart = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabMode
            int r13 = r12.getInt(r13, r9)
            r10.mode = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabGravity
            int r13 = r12.getInt(r13, r11)
            r10.tabGravity = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabInlineLabel
            boolean r13 = r12.getBoolean(r13, r11)
            r10.inlineLabel = r13
            int r13 = com.google.android.material.C0460R.styleable.TabLayout_tabUnboundedRipple
            boolean r11 = r12.getBoolean(r13, r11)
            r10.unboundedRipple = r11
            r12.recycle()
            android.content.res.Resources r11 = r10.getResources()
            int r12 = com.google.android.material.C0460R.dimen.design_tab_text_size_2line
            int r12 = r11.getDimensionPixelSize(r12)
            float r12 = (float) r12
            r10.tabTextMultiLineSize = r12
            int r12 = com.google.android.material.C0460R.dimen.design_tab_scrollable_min_width
            int r11 = r11.getDimensionPixelSize(r12)
            r10.scrollableTabMinWidth = r11
            r10.applyModeAndGravity()
            return
        L_0x01bf:
            r11 = move-exception
            r13.recycle()
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.tabs.TabLayout.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    public void setSelectedTabIndicatorColor(int i) {
        this.tabSelectedIndicatorColor = i;
    }

    @Deprecated
    public void setSelectedTabIndicatorHeight(int i) {
        this.slidingTabIndicator.setSelectedIndicatorHeight(i);
    }

    public void setScrollPosition(int i, float f, boolean z) {
        setScrollPosition(i, f, z, true);
    }

    public void setScrollPosition(int i, float f, boolean z, boolean z2) {
        int round = Math.round(((float) i) + f);
        if (round >= 0 && round < this.slidingTabIndicator.getChildCount()) {
            if (z2) {
                this.slidingTabIndicator.setIndicatorPositionFromTabPosition(i, f);
            }
            ValueAnimator valueAnimator = this.scrollAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.scrollAnimator.cancel();
            }
            scrollTo(calculateScrollXForTab(i, f), 0);
            if (z) {
                setSelectedTabView(round);
            }
        }
    }

    public void addTab(Tab tab) {
        addTab(tab, this.tabs.isEmpty());
    }

    public void addTab(Tab tab, int i) {
        addTab(tab, i, this.tabs.isEmpty());
    }

    public void addTab(Tab tab, boolean z) {
        addTab(tab, this.tabs.size(), z);
    }

    public void addTab(Tab tab, int i, boolean z) {
        if (tab.parent == this) {
            configureTab(tab, i);
            addTabView(tab);
            if (z) {
                tab.select();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
    }

    private void addTabFromItemView(TabItem tabItem) {
        Tab newTab = newTab();
        if (tabItem.text != null) {
            newTab.setText(tabItem.text);
        }
        if (tabItem.icon != null) {
            newTab.setIcon(tabItem.icon);
        }
        if (tabItem.customLayout != 0) {
            newTab.setCustomView(tabItem.customLayout);
        }
        if (!TextUtils.isEmpty(tabItem.getContentDescription())) {
            newTab.setContentDescription(tabItem.getContentDescription());
        }
        addTab(newTab);
    }

    @Deprecated
    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        setOnTabSelectedListener((BaseOnTabSelectedListener) onTabSelectedListener);
    }

    @Deprecated
    public void setOnTabSelectedListener(BaseOnTabSelectedListener baseOnTabSelectedListener) {
        BaseOnTabSelectedListener baseOnTabSelectedListener2 = this.selectedListener;
        if (baseOnTabSelectedListener2 != null) {
            removeOnTabSelectedListener(baseOnTabSelectedListener2);
        }
        this.selectedListener = baseOnTabSelectedListener;
        if (baseOnTabSelectedListener != null) {
            addOnTabSelectedListener(baseOnTabSelectedListener);
        }
    }

    public void addOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        addOnTabSelectedListener((BaseOnTabSelectedListener) onTabSelectedListener);
    }

    @Deprecated
    public void addOnTabSelectedListener(BaseOnTabSelectedListener baseOnTabSelectedListener) {
        if (!this.selectedListeners.contains(baseOnTabSelectedListener)) {
            this.selectedListeners.add(baseOnTabSelectedListener);
        }
    }

    public void removeOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        removeOnTabSelectedListener((BaseOnTabSelectedListener) onTabSelectedListener);
    }

    @Deprecated
    public void removeOnTabSelectedListener(BaseOnTabSelectedListener baseOnTabSelectedListener) {
        this.selectedListeners.remove(baseOnTabSelectedListener);
    }

    public void clearOnTabSelectedListeners() {
        this.selectedListeners.clear();
    }

    public Tab newTab() {
        Tab createTabFromPool = createTabFromPool();
        createTabFromPool.parent = this;
        createTabFromPool.view = createTabView(createTabFromPool);
        if (createTabFromPool.f96id != -1) {
            createTabFromPool.view.setId(createTabFromPool.f96id);
        }
        return createTabFromPool;
    }

    /* access modifiers changed from: protected */
    public Tab createTabFromPool() {
        Tab acquire = tabPool.acquire();
        return acquire == null ? new Tab() : acquire;
    }

    /* access modifiers changed from: protected */
    public boolean releaseFromTabPool(Tab tab) {
        return tabPool.release(tab);
    }

    public int getTabCount() {
        return this.tabs.size();
    }

    public Tab getTabAt(int i) {
        if (i < 0 || i >= getTabCount()) {
            return null;
        }
        return this.tabs.get(i);
    }

    public int getSelectedTabPosition() {
        Tab tab = this.selectedTab;
        if (tab != null) {
            return tab.getPosition();
        }
        return -1;
    }

    public void removeTab(Tab tab) {
        if (tab.parent == this) {
            removeTabAt(tab.getPosition());
            return;
        }
        throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
    }

    public void removeTabAt(int i) {
        Tab tab = this.selectedTab;
        int position = tab != null ? tab.getPosition() : 0;
        removeTabViewAt(i);
        Tab remove = this.tabs.remove(i);
        if (remove != null) {
            remove.reset();
            releaseFromTabPool(remove);
        }
        int size = this.tabs.size();
        for (int i2 = i; i2 < size; i2++) {
            this.tabs.get(i2).setPosition(i2);
        }
        if (position == i) {
            selectTab(this.tabs.isEmpty() ? null : this.tabs.get(Math.max(0, i - 1)));
        }
    }

    public void removeAllTabs() {
        for (int childCount = this.slidingTabIndicator.getChildCount() - 1; childCount >= 0; childCount--) {
            removeTabViewAt(childCount);
        }
        Iterator<Tab> it = this.tabs.iterator();
        while (it.hasNext()) {
            Tab next = it.next();
            it.remove();
            next.reset();
            releaseFromTabPool(next);
        }
        this.selectedTab = null;
    }

    public void setTabMode(int i) {
        if (i != this.mode) {
            this.mode = i;
            applyModeAndGravity();
        }
    }

    public int getTabMode() {
        return this.mode;
    }

    public void setTabGravity(int i) {
        if (this.tabGravity != i) {
            this.tabGravity = i;
            applyModeAndGravity();
        }
    }

    public int getTabGravity() {
        return this.tabGravity;
    }

    public void setSelectedTabIndicatorGravity(int i) {
        if (this.tabIndicatorGravity != i) {
            this.tabIndicatorGravity = i;
            ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
        }
    }

    public int getTabIndicatorGravity() {
        return this.tabIndicatorGravity;
    }

    public void setTabIndicatorAnimationMode(int i) {
        this.tabIndicatorAnimationMode = i;
        if (i == 0) {
            this.tabIndicatorInterpolator = new TabIndicatorInterpolator();
        } else if (i == 1) {
            this.tabIndicatorInterpolator = new ElasticTabIndicatorInterpolator();
        } else {
            throw new IllegalArgumentException(i + " is not a valid TabIndicatorAnimationMode");
        }
    }

    public int getTabIndicatorAnimationMode() {
        return this.tabIndicatorAnimationMode;
    }

    public void setTabIndicatorFullWidth(boolean z) {
        this.tabIndicatorFullWidth = z;
        ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
    }

    public boolean isTabIndicatorFullWidth() {
        return this.tabIndicatorFullWidth;
    }

    public void setInlineLabel(boolean z) {
        if (this.inlineLabel != z) {
            this.inlineLabel = z;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
                View childAt = this.slidingTabIndicator.getChildAt(i);
                if (childAt instanceof TabView) {
                    ((TabView) childAt).updateOrientation();
                }
            }
            applyModeAndGravity();
        }
    }

    public void setInlineLabelResource(int i) {
        setInlineLabel(getResources().getBoolean(i));
    }

    public boolean isInlineLabel() {
        return this.inlineLabel;
    }

    public void setUnboundedRipple(boolean z) {
        if (this.unboundedRipple != z) {
            this.unboundedRipple = z;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
                View childAt = this.slidingTabIndicator.getChildAt(i);
                if (childAt instanceof TabView) {
                    ((TabView) childAt).updateBackgroundDrawable(getContext());
                }
            }
        }
    }

    public void setUnboundedRippleResource(int i) {
        setUnboundedRipple(getResources().getBoolean(i));
    }

    public boolean hasUnboundedRipple() {
        return this.unboundedRipple;
    }

    public void setTabTextColors(ColorStateList colorStateList) {
        if (this.tabTextColors != colorStateList) {
            this.tabTextColors = colorStateList;
            updateAllTabs();
        }
    }

    public ColorStateList getTabTextColors() {
        return this.tabTextColors;
    }

    public void setTabTextColors(int i, int i2) {
        setTabTextColors(createColorStateList(i, i2));
    }

    public void setTabIconTint(ColorStateList colorStateList) {
        if (this.tabIconTint != colorStateList) {
            this.tabIconTint = colorStateList;
            updateAllTabs();
        }
    }

    public void setTabIconTintResource(int i) {
        setTabIconTint(AppCompatResources.getColorStateList(getContext(), i));
    }

    public ColorStateList getTabIconTint() {
        return this.tabIconTint;
    }

    public ColorStateList getTabRippleColor() {
        return this.tabRippleColorStateList;
    }

    public void setTabRippleColor(ColorStateList colorStateList) {
        if (this.tabRippleColorStateList != colorStateList) {
            this.tabRippleColorStateList = colorStateList;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
                View childAt = this.slidingTabIndicator.getChildAt(i);
                if (childAt instanceof TabView) {
                    ((TabView) childAt).updateBackgroundDrawable(getContext());
                }
            }
        }
    }

    public void setTabRippleColorResource(int i) {
        setTabRippleColor(AppCompatResources.getColorStateList(getContext(), i));
    }

    public Drawable getTabSelectedIndicator() {
        return this.tabSelectedIndicator;
    }

    public void setSelectedTabIndicator(Drawable drawable) {
        if (this.tabSelectedIndicator != drawable) {
            if (drawable == null) {
                drawable = new GradientDrawable();
            }
            this.tabSelectedIndicator = drawable;
        }
    }

    public void setSelectedTabIndicator(int i) {
        if (i != 0) {
            setSelectedTabIndicator(AppCompatResources.getDrawable(getContext(), i));
        } else {
            setSelectedTabIndicator((Drawable) null);
        }
    }

    public void setupWithViewPager(ViewPager viewPager2) {
        setupWithViewPager(viewPager2, true);
    }

    public void setupWithViewPager(ViewPager viewPager2, boolean z) {
        setupWithViewPager(viewPager2, z, false);
    }

    private void setupWithViewPager(ViewPager viewPager2, boolean z, boolean z2) {
        ViewPager viewPager3 = this.viewPager;
        if (viewPager3 != null) {
            TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener = this.pageChangeListener;
            if (tabLayoutOnPageChangeListener != null) {
                viewPager3.removeOnPageChangeListener(tabLayoutOnPageChangeListener);
            }
            AdapterChangeListener adapterChangeListener2 = this.adapterChangeListener;
            if (adapterChangeListener2 != null) {
                this.viewPager.removeOnAdapterChangeListener(adapterChangeListener2);
            }
        }
        BaseOnTabSelectedListener baseOnTabSelectedListener = this.currentVpSelectedListener;
        if (baseOnTabSelectedListener != null) {
            removeOnTabSelectedListener(baseOnTabSelectedListener);
            this.currentVpSelectedListener = null;
        }
        if (viewPager2 != null) {
            this.viewPager = viewPager2;
            if (this.pageChangeListener == null) {
                this.pageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            this.pageChangeListener.reset();
            viewPager2.addOnPageChangeListener(this.pageChangeListener);
            ViewPagerOnTabSelectedListener viewPagerOnTabSelectedListener = new ViewPagerOnTabSelectedListener(viewPager2);
            this.currentVpSelectedListener = viewPagerOnTabSelectedListener;
            addOnTabSelectedListener((BaseOnTabSelectedListener) viewPagerOnTabSelectedListener);
            PagerAdapter adapter = viewPager2.getAdapter();
            if (adapter != null) {
                setPagerAdapter(adapter, z);
            }
            if (this.adapterChangeListener == null) {
                this.adapterChangeListener = new AdapterChangeListener();
            }
            this.adapterChangeListener.setAutoRefresh(z);
            viewPager2.addOnAdapterChangeListener(this.adapterChangeListener);
            setScrollPosition(viewPager2.getCurrentItem(), 0.0f, true);
        } else {
            this.viewPager = null;
            setPagerAdapter((PagerAdapter) null, false);
        }
        this.setupViewPagerImplicitly = z2;
    }

    @Deprecated
    public void setTabsFromPagerAdapter(PagerAdapter pagerAdapter2) {
        setPagerAdapter(pagerAdapter2, false);
    }

    public boolean shouldDelayChildPressedState() {
        return getTabScrollRange() > 0;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation(this);
        if (this.viewPager == null) {
            ViewParent parent = getParent();
            if (parent instanceof ViewPager) {
                setupWithViewPager((ViewPager) parent, true, true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.setupViewPagerImplicitly) {
            setupWithViewPager((ViewPager) null);
            this.setupViewPagerImplicitly = false;
        }
    }

    private int getTabScrollRange() {
        return Math.max(0, ((this.slidingTabIndicator.getWidth() - getWidth()) - getPaddingLeft()) - getPaddingRight());
    }

    /* access modifiers changed from: package-private */
    public void setPagerAdapter(PagerAdapter pagerAdapter2, boolean z) {
        DataSetObserver dataSetObserver;
        PagerAdapter pagerAdapter3 = this.pagerAdapter;
        if (!(pagerAdapter3 == null || (dataSetObserver = this.pagerAdapterObserver) == null)) {
            pagerAdapter3.unregisterDataSetObserver(dataSetObserver);
        }
        this.pagerAdapter = pagerAdapter2;
        if (z && pagerAdapter2 != null) {
            if (this.pagerAdapterObserver == null) {
                this.pagerAdapterObserver = new PagerAdapterObserver();
            }
            pagerAdapter2.registerDataSetObserver(this.pagerAdapterObserver);
        }
        populateFromPagerAdapter();
    }

    /* access modifiers changed from: package-private */
    public void populateFromPagerAdapter() {
        int currentItem;
        removeAllTabs();
        PagerAdapter pagerAdapter2 = this.pagerAdapter;
        if (pagerAdapter2 != null) {
            int count = pagerAdapter2.getCount();
            for (int i = 0; i < count; i++) {
                addTab(newTab().setText(this.pagerAdapter.getPageTitle(i)), false);
            }
            ViewPager viewPager2 = this.viewPager;
            if (viewPager2 != null && count > 0 && (currentItem = viewPager2.getCurrentItem()) != getSelectedTabPosition() && currentItem < getTabCount()) {
                selectTab(getTabAt(currentItem));
            }
        }
    }

    private void updateAllTabs() {
        int size = this.tabs.size();
        for (int i = 0; i < size; i++) {
            this.tabs.get(i).updateView();
        }
    }

    private TabView createTabView(Tab tab) {
        Pools.Pool<TabView> pool = this.tabViewPool;
        TabView acquire = pool != null ? pool.acquire() : null;
        if (acquire == null) {
            acquire = new TabView(getContext());
        }
        acquire.setTab(tab);
        acquire.setFocusable(true);
        acquire.setMinimumWidth(getTabMinWidth());
        if (TextUtils.isEmpty(tab.contentDesc)) {
            acquire.setContentDescription(tab.text);
        } else {
            acquire.setContentDescription(tab.contentDesc);
        }
        return acquire;
    }

    private void configureTab(Tab tab, int i) {
        tab.setPosition(i);
        this.tabs.add(i, tab);
        int size = this.tabs.size();
        while (true) {
            i++;
            if (i < size) {
                this.tabs.get(i).setPosition(i);
            } else {
                return;
            }
        }
    }

    private void addTabView(Tab tab) {
        TabView tabView = tab.view;
        tabView.setSelected(false);
        tabView.setActivated(false);
        this.slidingTabIndicator.addView(tabView, tab.getPosition(), createLayoutParamsForTabs());
    }

    public void addView(View view) {
        addViewInternal(view);
    }

    public void addView(View view, int i) {
        addViewInternal(view);
    }

    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        addViewInternal(view);
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        addViewInternal(view);
    }

    private void addViewInternal(View view) {
        if (view instanceof TabItem) {
            addTabFromItemView((TabItem) view);
            return;
        }
        throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
        updateTabViewLayoutParams(layoutParams);
        return layoutParams;
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams layoutParams) {
        if (this.mode == 1 && this.tabGravity == 0) {
            layoutParams.width = 0;
            layoutParams.weight = 1.0f;
            return;
        }
        layoutParams.width = -2;
        layoutParams.weight = 0.0f;
    }

    public void setElevation(float f) {
        super.setElevation(f);
        MaterialShapeUtils.setElevation(this, f);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, getTabCount(), false, 1));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
            View childAt = this.slidingTabIndicator.getChildAt(i);
            if (childAt instanceof TabView) {
                ((TabView) childAt).drawBackground(canvas);
            }
        }
        super.onDraw(canvas);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0073, code lost:
        if (r0 != 2) goto L_0x008d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x007e, code lost:
        if (r7.getMeasuredWidth() != getMeasuredWidth()) goto L_0x0080;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0080, code lost:
        r4 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x008a, code lost:
        if (r7.getMeasuredWidth() < getMeasuredWidth()) goto L_0x0080;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMeasure(int r7, int r8) {
        /*
            r6 = this;
            android.content.Context r0 = r6.getContext()
            int r1 = r6.getDefaultHeight()
            float r0 = com.google.android.material.internal.ViewUtils.dpToPx(r0, r1)
            int r0 = java.lang.Math.round(r0)
            int r1 = android.view.View.MeasureSpec.getMode(r8)
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = 1073741824(0x40000000, float:2.0)
            r4 = 0
            r5 = 1
            if (r1 == r2) goto L_0x002e
            if (r1 == 0) goto L_0x001f
            goto L_0x0041
        L_0x001f:
            int r8 = r6.getPaddingTop()
            int r0 = r0 + r8
            int r8 = r6.getPaddingBottom()
            int r0 = r0 + r8
            int r8 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r3)
            goto L_0x0041
        L_0x002e:
            int r1 = r6.getChildCount()
            if (r1 != r5) goto L_0x0041
            int r1 = android.view.View.MeasureSpec.getSize(r8)
            if (r1 < r0) goto L_0x0041
            android.view.View r1 = r6.getChildAt(r4)
            r1.setMinimumHeight(r0)
        L_0x0041:
            int r0 = android.view.View.MeasureSpec.getSize(r7)
            int r1 = android.view.View.MeasureSpec.getMode(r7)
            if (r1 == 0) goto L_0x005f
            int r1 = r6.requestedTabMaxWidth
            if (r1 <= 0) goto L_0x0050
            goto L_0x005d
        L_0x0050:
            float r0 = (float) r0
            android.content.Context r1 = r6.getContext()
            r2 = 56
            float r1 = com.google.android.material.internal.ViewUtils.dpToPx(r1, r2)
            float r0 = r0 - r1
            int r1 = (int) r0
        L_0x005d:
            r6.tabMaxWidth = r1
        L_0x005f:
            super.onMeasure(r7, r8)
            int r7 = r6.getChildCount()
            if (r7 != r5) goto L_0x00ad
            android.view.View r7 = r6.getChildAt(r4)
            int r0 = r6.mode
            if (r0 == 0) goto L_0x0082
            if (r0 == r5) goto L_0x0076
            r1 = 2
            if (r0 == r1) goto L_0x0082
            goto L_0x008d
        L_0x0076:
            int r0 = r7.getMeasuredWidth()
            int r1 = r6.getMeasuredWidth()
            if (r0 == r1) goto L_0x008d
        L_0x0080:
            r4 = r5
            goto L_0x008d
        L_0x0082:
            int r0 = r7.getMeasuredWidth()
            int r1 = r6.getMeasuredWidth()
            if (r0 >= r1) goto L_0x008d
            goto L_0x0080
        L_0x008d:
            if (r4 == 0) goto L_0x00ad
            int r0 = r6.getPaddingTop()
            int r1 = r6.getPaddingBottom()
            int r0 = r0 + r1
            android.view.ViewGroup$LayoutParams r1 = r7.getLayoutParams()
            int r1 = r1.height
            int r8 = getChildMeasureSpec(r8, r0, r1)
            int r0 = r6.getMeasuredWidth()
            int r0 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r3)
            r7.measure(r0, r8)
        L_0x00ad:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.tabs.TabLayout.onMeasure(int, int):void");
    }

    private void removeTabViewAt(int i) {
        TabView tabView = (TabView) this.slidingTabIndicator.getChildAt(i);
        this.slidingTabIndicator.removeViewAt(i);
        if (tabView != null) {
            tabView.reset();
            this.tabViewPool.release(tabView);
        }
        requestLayout();
    }

    private void animateToTab(int i) {
        if (i != -1) {
            if (getWindowToken() == null || !ViewCompat.isLaidOut(this) || this.slidingTabIndicator.childrenNeedLayout()) {
                setScrollPosition(i, 0.0f, true);
                return;
            }
            int scrollX = getScrollX();
            int calculateScrollXForTab = calculateScrollXForTab(i, 0.0f);
            if (scrollX != calculateScrollXForTab) {
                ensureScrollAnimator();
                this.scrollAnimator.setIntValues(new int[]{scrollX, calculateScrollXForTab});
                this.scrollAnimator.start();
            }
            this.slidingTabIndicator.animateIndicatorToPosition(i, this.tabIndicatorAnimationDuration);
        }
    }

    private void ensureScrollAnimator() {
        if (this.scrollAnimator == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.scrollAnimator = valueAnimator;
            valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.scrollAnimator.setDuration((long) this.tabIndicatorAnimationDuration);
            this.scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    TabLayout.this.scrollTo(((Integer) valueAnimator.getAnimatedValue()).intValue(), 0);
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void setScrollAnimatorListener(Animator.AnimatorListener animatorListener) {
        ensureScrollAnimator();
        this.scrollAnimator.addListener(animatorListener);
    }

    private void setSelectedTabView(int i) {
        int childCount = this.slidingTabIndicator.getChildCount();
        if (i < childCount) {
            int i2 = 0;
            while (i2 < childCount) {
                View childAt = this.slidingTabIndicator.getChildAt(i2);
                boolean z = true;
                childAt.setSelected(i2 == i);
                if (i2 != i) {
                    z = false;
                }
                childAt.setActivated(z);
                i2++;
            }
        }
    }

    public void selectTab(Tab tab) {
        selectTab(tab, true);
    }

    public void selectTab(Tab tab, boolean z) {
        Tab tab2 = this.selectedTab;
        if (tab2 != tab) {
            int position = tab != null ? tab.getPosition() : -1;
            if (z) {
                if ((tab2 == null || tab2.getPosition() == -1) && position != -1) {
                    setScrollPosition(position, 0.0f, true);
                } else {
                    animateToTab(position);
                }
                if (position != -1) {
                    setSelectedTabView(position);
                }
            }
            this.selectedTab = tab;
            if (tab2 != null) {
                dispatchTabUnselected(tab2);
            }
            if (tab != null) {
                dispatchTabSelected(tab);
            }
        } else if (tab2 != null) {
            dispatchTabReselected(tab);
            animateToTab(tab.getPosition());
        }
    }

    private void dispatchTabSelected(Tab tab) {
        for (int size = this.selectedListeners.size() - 1; size >= 0; size--) {
            this.selectedListeners.get(size).onTabSelected(tab);
        }
    }

    private void dispatchTabUnselected(Tab tab) {
        for (int size = this.selectedListeners.size() - 1; size >= 0; size--) {
            this.selectedListeners.get(size).onTabUnselected(tab);
        }
    }

    private void dispatchTabReselected(Tab tab) {
        for (int size = this.selectedListeners.size() - 1; size >= 0; size--) {
            this.selectedListeners.get(size).onTabReselected(tab);
        }
    }

    private int calculateScrollXForTab(int i, float f) {
        int i2 = this.mode;
        int i3 = 0;
        if (i2 != 0 && i2 != 2) {
            return 0;
        }
        View childAt = this.slidingTabIndicator.getChildAt(i);
        int i4 = i + 1;
        View childAt2 = i4 < this.slidingTabIndicator.getChildCount() ? this.slidingTabIndicator.getChildAt(i4) : null;
        int width = childAt != null ? childAt.getWidth() : 0;
        if (childAt2 != null) {
            i3 = childAt2.getWidth();
        }
        int left = (childAt.getLeft() + (width / 2)) - (getWidth() / 2);
        int i5 = (int) (((float) (width + i3)) * 0.5f * f);
        return ViewCompat.getLayoutDirection(this) == 0 ? left + i5 : left - i5;
    }

    private void applyModeAndGravity() {
        int i;
        int i2 = this.mode;
        if (i2 == 0 || i2 == 2) {
            i = Math.max(0, this.contentInsetStart - this.tabPaddingStart);
        } else {
            i = 0;
        }
        ViewCompat.setPaddingRelative(this.slidingTabIndicator, i, 0, 0, 0);
        int i3 = this.mode;
        if (i3 == 0) {
            applyGravityForModeScrollable(this.tabGravity);
        } else if (i3 == 1 || i3 == 2) {
            if (this.tabGravity == 2) {
                Log.w(LOG_TAG, "GRAVITY_START is not supported with the current tab mode, GRAVITY_CENTER will be used instead");
            }
            this.slidingTabIndicator.setGravity(1);
        }
        updateTabViews(true);
    }

    private void applyGravityForModeScrollable(int i) {
        if (i == 0) {
            Log.w(LOG_TAG, "MODE_SCROLLABLE + GRAVITY_FILL is not supported, GRAVITY_START will be used instead");
        } else if (i == 1) {
            this.slidingTabIndicator.setGravity(1);
            return;
        } else if (i != 2) {
            return;
        }
        this.slidingTabIndicator.setGravity(GravityCompat.START);
    }

    /* access modifiers changed from: package-private */
    public void updateTabViews(boolean z) {
        for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
            View childAt = this.slidingTabIndicator.getChildAt(i);
            childAt.setMinimumWidth(getTabMinWidth());
            updateTabViewLayoutParams((LinearLayout.LayoutParams) childAt.getLayoutParams());
            if (z) {
                childAt.requestLayout();
            }
        }
    }

    public static class Tab {
        public static final int INVALID_POSITION = -1;
        /* access modifiers changed from: private */
        public CharSequence contentDesc;
        private View customView;
        private Drawable icon;
        /* access modifiers changed from: private */

        /* renamed from: id */
        public int f96id = -1;
        /* access modifiers changed from: private */
        public int labelVisibilityMode = 1;
        public TabLayout parent;
        private int position = -1;
        private Object tag;
        /* access modifiers changed from: private */
        public CharSequence text;
        public TabView view;

        public Object getTag() {
            return this.tag;
        }

        public Tab setTag(Object obj) {
            this.tag = obj;
            return this;
        }

        public Tab setId(int i) {
            this.f96id = i;
            TabView tabView = this.view;
            if (tabView != null) {
                tabView.setId(i);
            }
            return this;
        }

        public int getId() {
            return this.f96id;
        }

        public View getCustomView() {
            return this.customView;
        }

        public Tab setCustomView(View view2) {
            this.customView = view2;
            updateView();
            return this;
        }

        public Tab setCustomView(int i) {
            return setCustomView(LayoutInflater.from(this.view.getContext()).inflate(i, this.view, false));
        }

        public Drawable getIcon() {
            return this.icon;
        }

        public int getPosition() {
            return this.position;
        }

        /* access modifiers changed from: package-private */
        public void setPosition(int i) {
            this.position = i;
        }

        public CharSequence getText() {
            return this.text;
        }

        public Tab setIcon(Drawable drawable) {
            this.icon = drawable;
            if (this.parent.tabGravity == 1 || this.parent.mode == 2) {
                this.parent.updateTabViews(true);
            }
            updateView();
            if (BadgeUtils.USE_COMPAT_PARENT && this.view.hasBadgeDrawable() && this.view.badgeDrawable.isVisible()) {
                this.view.invalidate();
            }
            return this;
        }

        public Tab setIcon(int i) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return setIcon(AppCompatResources.getDrawable(tabLayout.getContext(), i));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setText(CharSequence charSequence) {
            if (TextUtils.isEmpty(this.contentDesc) && !TextUtils.isEmpty(charSequence)) {
                this.view.setContentDescription(charSequence);
            }
            this.text = charSequence;
            updateView();
            return this;
        }

        public Tab setText(int i) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return setText(tabLayout.getResources().getText(i));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public BadgeDrawable getOrCreateBadge() {
            return this.view.getOrCreateBadge();
        }

        public void removeBadge() {
            this.view.removeBadge();
        }

        public BadgeDrawable getBadge() {
            return this.view.getBadge();
        }

        public Tab setTabLabelVisibility(int i) {
            this.labelVisibilityMode = i;
            if (this.parent.tabGravity == 1 || this.parent.mode == 2) {
                this.parent.updateTabViews(true);
            }
            updateView();
            if (BadgeUtils.USE_COMPAT_PARENT && this.view.hasBadgeDrawable() && this.view.badgeDrawable.isVisible()) {
                this.view.invalidate();
            }
            return this;
        }

        public int getTabLabelVisibility() {
            return this.labelVisibilityMode;
        }

        public void select() {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                tabLayout.selectTab(this);
                return;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public boolean isSelected() {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return tabLayout.getSelectedTabPosition() == this.position;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setContentDescription(int i) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return setContentDescription(tabLayout.getResources().getText(i));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setContentDescription(CharSequence charSequence) {
            this.contentDesc = charSequence;
            updateView();
            return this;
        }

        public CharSequence getContentDescription() {
            TabView tabView = this.view;
            if (tabView == null) {
                return null;
            }
            return tabView.getContentDescription();
        }

        /* access modifiers changed from: package-private */
        public void updateView() {
            TabView tabView = this.view;
            if (tabView != null) {
                tabView.update();
            }
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            this.parent = null;
            this.view = null;
            this.tag = null;
            this.icon = null;
            this.f96id = -1;
            this.text = null;
            this.contentDesc = null;
            this.position = -1;
            this.customView = null;
        }
    }

    public final class TabView extends LinearLayout {
        private View badgeAnchorView;
        /* access modifiers changed from: private */
        public BadgeDrawable badgeDrawable;
        private Drawable baseBackgroundDrawable;
        private ImageView customIconView;
        private TextView customTextView;
        private View customView;
        private int defaultMaxLines = 2;
        private ImageView iconView;
        private Tab tab;
        private TextView textView;

        public TabView(Context context) {
            super(context);
            updateBackgroundDrawable(context);
            ViewCompat.setPaddingRelative(this, TabLayout.this.tabPaddingStart, TabLayout.this.tabPaddingTop, TabLayout.this.tabPaddingEnd, TabLayout.this.tabPaddingBottom);
            setGravity(17);
            setOrientation(TabLayout.this.inlineLabel ^ true ? 1 : 0);
            setClickable(true);
            ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(getContext(), PointerIconCompat.TYPE_HAND));
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v5, resolved type: android.graphics.drawable.GradientDrawable} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v6, resolved type: android.graphics.drawable.GradientDrawable} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v7, resolved type: android.graphics.drawable.GradientDrawable} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v8, resolved type: android.graphics.drawable.GradientDrawable} */
        /* access modifiers changed from: private */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void updateBackgroundDrawable(android.content.Context r7) {
            /*
                r6 = this;
                com.google.android.material.tabs.TabLayout r0 = com.google.android.material.tabs.TabLayout.this
                int r0 = r0.tabBackgroundResId
                r1 = 0
                if (r0 == 0) goto L_0x0023
                com.google.android.material.tabs.TabLayout r0 = com.google.android.material.tabs.TabLayout.this
                int r0 = r0.tabBackgroundResId
                android.graphics.drawable.Drawable r7 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r7, r0)
                r6.baseBackgroundDrawable = r7
                if (r7 == 0) goto L_0x0025
                boolean r7 = r7.isStateful()
                if (r7 == 0) goto L_0x0025
                android.graphics.drawable.Drawable r7 = r6.baseBackgroundDrawable
                int[] r0 = r6.getDrawableState()
                r7.setState(r0)
                goto L_0x0025
            L_0x0023:
                r6.baseBackgroundDrawable = r1
            L_0x0025:
                android.graphics.drawable.GradientDrawable r7 = new android.graphics.drawable.GradientDrawable
                r7.<init>()
                r0 = r7
                android.graphics.drawable.GradientDrawable r0 = (android.graphics.drawable.GradientDrawable) r0
                r2 = 0
                r0.setColor(r2)
                com.google.android.material.tabs.TabLayout r0 = com.google.android.material.tabs.TabLayout.this
                android.content.res.ColorStateList r0 = r0.tabRippleColorStateList
                if (r0 == 0) goto L_0x007f
                android.graphics.drawable.GradientDrawable r0 = new android.graphics.drawable.GradientDrawable
                r0.<init>()
                r3 = 925353388(0x3727c5ac, float:1.0E-5)
                r0.setCornerRadius(r3)
                r3 = -1
                r0.setColor(r3)
                com.google.android.material.tabs.TabLayout r3 = com.google.android.material.tabs.TabLayout.this
                android.content.res.ColorStateList r3 = r3.tabRippleColorStateList
                android.content.res.ColorStateList r3 = com.google.android.material.ripple.RippleUtils.convertToRippleDrawableColor(r3)
                int r4 = android.os.Build.VERSION.SDK_INT
                r5 = 21
                if (r4 < r5) goto L_0x006a
                android.graphics.drawable.RippleDrawable r2 = new android.graphics.drawable.RippleDrawable
                com.google.android.material.tabs.TabLayout r4 = com.google.android.material.tabs.TabLayout.this
                boolean r4 = r4.unboundedRipple
                if (r4 == 0) goto L_0x005d
                r7 = r1
            L_0x005d:
                com.google.android.material.tabs.TabLayout r4 = com.google.android.material.tabs.TabLayout.this
                boolean r4 = r4.unboundedRipple
                if (r4 == 0) goto L_0x0064
                goto L_0x0065
            L_0x0064:
                r1 = r0
            L_0x0065:
                r2.<init>(r3, r7, r1)
                r7 = r2
                goto L_0x007f
            L_0x006a:
                android.graphics.drawable.Drawable r0 = androidx.core.graphics.drawable.DrawableCompat.wrap(r0)
                androidx.core.graphics.drawable.DrawableCompat.setTintList(r0, r3)
                android.graphics.drawable.LayerDrawable r1 = new android.graphics.drawable.LayerDrawable
                r3 = 2
                android.graphics.drawable.Drawable[] r3 = new android.graphics.drawable.Drawable[r3]
                r3[r2] = r7
                r7 = 1
                r3[r7] = r0
                r1.<init>(r3)
                r7 = r1
            L_0x007f:
                androidx.core.view.ViewCompat.setBackground(r6, r7)
                com.google.android.material.tabs.TabLayout r7 = com.google.android.material.tabs.TabLayout.this
                r7.invalidate()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.tabs.TabLayout.TabView.updateBackgroundDrawable(android.content.Context):void");
        }

        /* access modifiers changed from: private */
        public void drawBackground(Canvas canvas) {
            Drawable drawable = this.baseBackgroundDrawable;
            if (drawable != null) {
                drawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
                this.baseBackgroundDrawable.draw(canvas);
            }
        }

        /* access modifiers changed from: protected */
        public void drawableStateChanged() {
            super.drawableStateChanged();
            int[] drawableState = getDrawableState();
            Drawable drawable = this.baseBackgroundDrawable;
            boolean z = false;
            if (drawable != null && drawable.isStateful()) {
                z = false | this.baseBackgroundDrawable.setState(drawableState);
            }
            if (z) {
                invalidate();
                TabLayout.this.invalidate();
            }
        }

        public boolean performClick() {
            boolean performClick = super.performClick();
            if (this.tab == null) {
                return performClick;
            }
            if (!performClick) {
                playSoundEffect(0);
            }
            this.tab.select();
            return true;
        }

        public void setSelected(boolean z) {
            boolean z2 = isSelected() != z;
            super.setSelected(z);
            if (z2 && z && Build.VERSION.SDK_INT < 16) {
                sendAccessibilityEvent(4);
            }
            TextView textView2 = this.textView;
            if (textView2 != null) {
                textView2.setSelected(z);
            }
            ImageView imageView = this.iconView;
            if (imageView != null) {
                imageView.setSelected(z);
            }
            View view = this.customView;
            if (view != null) {
                view.setSelected(z);
            }
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            BadgeDrawable badgeDrawable2 = this.badgeDrawable;
            if (badgeDrawable2 != null && badgeDrawable2.isVisible()) {
                CharSequence contentDescription = getContentDescription();
                accessibilityNodeInfo.setContentDescription(contentDescription + ", " + this.badgeDrawable.getContentDescription());
            }
            AccessibilityNodeInfoCompat wrap = AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo);
            wrap.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, this.tab.getPosition(), 1, false, isSelected()));
            if (isSelected()) {
                wrap.setClickable(false);
                wrap.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
            }
            wrap.setRoleDescription(getResources().getString(C0460R.string.item_view_role_description));
        }

        public void onMeasure(int i, int i2) {
            Layout layout;
            int size = View.MeasureSpec.getSize(i);
            int mode = View.MeasureSpec.getMode(i);
            int tabMaxWidth = TabLayout.this.getTabMaxWidth();
            if (tabMaxWidth > 0 && (mode == 0 || size > tabMaxWidth)) {
                i = View.MeasureSpec.makeMeasureSpec(TabLayout.this.tabMaxWidth, Integer.MIN_VALUE);
            }
            super.onMeasure(i, i2);
            if (this.textView != null) {
                float f = TabLayout.this.tabTextSize;
                int i3 = this.defaultMaxLines;
                ImageView imageView = this.iconView;
                boolean z = true;
                if (imageView == null || imageView.getVisibility() != 0) {
                    TextView textView2 = this.textView;
                    if (textView2 != null && textView2.getLineCount() > 1) {
                        f = TabLayout.this.tabTextMultiLineSize;
                    }
                } else {
                    i3 = 1;
                }
                float textSize = this.textView.getTextSize();
                int lineCount = this.textView.getLineCount();
                int maxLines = TextViewCompat.getMaxLines(this.textView);
                int i4 = (f > textSize ? 1 : (f == textSize ? 0 : -1));
                if (i4 != 0 || (maxLines >= 0 && i3 != maxLines)) {
                    if (TabLayout.this.mode == 1 && i4 > 0 && lineCount == 1 && ((layout = this.textView.getLayout()) == null || approximateLineWidth(layout, 0, f) > ((float) ((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight())))) {
                        z = false;
                    }
                    if (z) {
                        this.textView.setTextSize(0, f);
                        this.textView.setMaxLines(i3);
                        super.onMeasure(i, i2);
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void setTab(Tab tab2) {
            if (tab2 != this.tab) {
                this.tab = tab2;
                update();
            }
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            setTab((Tab) null);
            setSelected(false);
        }

        /* access modifiers changed from: package-private */
        public final void update() {
            Tab tab2 = this.tab;
            Drawable drawable = null;
            View customView2 = tab2 != null ? tab2.getCustomView() : null;
            if (customView2 != null) {
                ViewParent parent = customView2.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(customView2);
                    }
                    addView(customView2);
                }
                this.customView = customView2;
                TextView textView2 = this.textView;
                if (textView2 != null) {
                    textView2.setVisibility(8);
                }
                ImageView imageView = this.iconView;
                if (imageView != null) {
                    imageView.setVisibility(8);
                    this.iconView.setImageDrawable((Drawable) null);
                }
                TextView textView3 = (TextView) customView2.findViewById(16908308);
                this.customTextView = textView3;
                if (textView3 != null) {
                    this.defaultMaxLines = TextViewCompat.getMaxLines(textView3);
                }
                this.customIconView = (ImageView) customView2.findViewById(16908294);
            } else {
                View view = this.customView;
                if (view != null) {
                    removeView(view);
                    this.customView = null;
                }
                this.customTextView = null;
                this.customIconView = null;
            }
            if (this.customView == null) {
                if (this.iconView == null) {
                    inflateAndAddDefaultIconView();
                }
                if (!(tab2 == null || tab2.getIcon() == null)) {
                    drawable = DrawableCompat.wrap(tab2.getIcon()).mutate();
                }
                if (drawable != null) {
                    DrawableCompat.setTintList(drawable, TabLayout.this.tabIconTint);
                    if (TabLayout.this.tabIconTintMode != null) {
                        DrawableCompat.setTintMode(drawable, TabLayout.this.tabIconTintMode);
                    }
                }
                if (this.textView == null) {
                    inflateAndAddDefaultTextView();
                    this.defaultMaxLines = TextViewCompat.getMaxLines(this.textView);
                }
                TextViewCompat.setTextAppearance(this.textView, TabLayout.this.tabTextAppearance);
                if (TabLayout.this.tabTextColors != null) {
                    this.textView.setTextColor(TabLayout.this.tabTextColors);
                }
                updateTextAndIcon(this.textView, this.iconView);
                tryUpdateBadgeAnchor();
                addOnLayoutChangeListener(this.iconView);
                addOnLayoutChangeListener(this.textView);
            } else {
                TextView textView4 = this.customTextView;
                if (!(textView4 == null && this.customIconView == null)) {
                    updateTextAndIcon(textView4, this.customIconView);
                }
            }
            if (tab2 != null && !TextUtils.isEmpty(tab2.contentDesc)) {
                setContentDescription(tab2.contentDesc);
            }
            setSelected(tab2 != null && tab2.isSelected());
        }

        private void inflateAndAddDefaultIconView() {
            ViewGroup viewGroup;
            if (BadgeUtils.USE_COMPAT_PARENT) {
                viewGroup = createPreApi18BadgeAnchorRoot();
                addView(viewGroup, 0);
            } else {
                viewGroup = this;
            }
            ImageView imageView = (ImageView) LayoutInflater.from(getContext()).inflate(C0460R.layout.design_layout_tab_icon, viewGroup, false);
            this.iconView = imageView;
            viewGroup.addView(imageView, 0);
        }

        private void inflateAndAddDefaultTextView() {
            ViewGroup viewGroup;
            if (BadgeUtils.USE_COMPAT_PARENT) {
                viewGroup = createPreApi18BadgeAnchorRoot();
                addView(viewGroup);
            } else {
                viewGroup = this;
            }
            TextView textView2 = (TextView) LayoutInflater.from(getContext()).inflate(C0460R.layout.design_layout_tab_text, viewGroup, false);
            this.textView = textView2;
            viewGroup.addView(textView2);
        }

        private FrameLayout createPreApi18BadgeAnchorRoot() {
            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
            return frameLayout;
        }

        /* access modifiers changed from: private */
        public BadgeDrawable getOrCreateBadge() {
            if (this.badgeDrawable == null) {
                this.badgeDrawable = BadgeDrawable.create(getContext());
            }
            tryUpdateBadgeAnchor();
            BadgeDrawable badgeDrawable2 = this.badgeDrawable;
            if (badgeDrawable2 != null) {
                return badgeDrawable2;
            }
            throw new IllegalStateException("Unable to create badge");
        }

        /* access modifiers changed from: private */
        public BadgeDrawable getBadge() {
            return this.badgeDrawable;
        }

        /* access modifiers changed from: private */
        public void removeBadge() {
            if (this.badgeAnchorView != null) {
                tryRemoveBadgeFromAnchor();
            }
            this.badgeDrawable = null;
        }

        private void addOnLayoutChangeListener(final View view) {
            if (view != null) {
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                        if (view.getVisibility() == 0) {
                            TabView.this.tryUpdateBadgeDrawableBounds(view);
                        }
                    }
                });
            }
        }

        private void tryUpdateBadgeAnchor() {
            Tab tab2;
            Tab tab3;
            if (hasBadgeDrawable()) {
                if (this.customView != null) {
                    tryRemoveBadgeFromAnchor();
                } else if (this.iconView != null && (tab3 = this.tab) != null && tab3.getIcon() != null) {
                    View view = this.badgeAnchorView;
                    ImageView imageView = this.iconView;
                    if (view != imageView) {
                        tryRemoveBadgeFromAnchor();
                        tryAttachBadgeToAnchor(this.iconView);
                        return;
                    }
                    tryUpdateBadgeDrawableBounds(imageView);
                } else if (this.textView == null || (tab2 = this.tab) == null || tab2.getTabLabelVisibility() != 1) {
                    tryRemoveBadgeFromAnchor();
                } else {
                    View view2 = this.badgeAnchorView;
                    TextView textView2 = this.textView;
                    if (view2 != textView2) {
                        tryRemoveBadgeFromAnchor();
                        tryAttachBadgeToAnchor(this.textView);
                        return;
                    }
                    tryUpdateBadgeDrawableBounds(textView2);
                }
            }
        }

        private void tryAttachBadgeToAnchor(View view) {
            if (hasBadgeDrawable() && view != null) {
                clipViewToPaddingForBadge(false);
                BadgeUtils.attachBadgeDrawable(this.badgeDrawable, view, getCustomParentForBadge(view));
                this.badgeAnchorView = view;
            }
        }

        private void tryRemoveBadgeFromAnchor() {
            if (hasBadgeDrawable()) {
                clipViewToPaddingForBadge(true);
                View view = this.badgeAnchorView;
                if (view != null) {
                    BadgeUtils.detachBadgeDrawable(this.badgeDrawable, view);
                    this.badgeAnchorView = null;
                }
            }
        }

        private void clipViewToPaddingForBadge(boolean z) {
            setClipChildren(z);
            setClipToPadding(z);
            ViewGroup viewGroup = (ViewGroup) getParent();
            if (viewGroup != null) {
                viewGroup.setClipChildren(z);
                viewGroup.setClipToPadding(z);
            }
        }

        /* access modifiers changed from: package-private */
        public final void updateOrientation() {
            setOrientation(TabLayout.this.inlineLabel ^ true ? 1 : 0);
            TextView textView2 = this.customTextView;
            if (textView2 == null && this.customIconView == null) {
                updateTextAndIcon(this.textView, this.iconView);
            } else {
                updateTextAndIcon(textView2, this.customIconView);
            }
        }

        private void updateTextAndIcon(TextView textView2, ImageView imageView) {
            Tab tab2 = this.tab;
            CharSequence charSequence = null;
            Drawable mutate = (tab2 == null || tab2.getIcon() == null) ? null : DrawableCompat.wrap(this.tab.getIcon()).mutate();
            Tab tab3 = this.tab;
            CharSequence text = tab3 != null ? tab3.getText() : null;
            if (imageView != null) {
                if (mutate != null) {
                    imageView.setImageDrawable(mutate);
                    imageView.setVisibility(0);
                    setVisibility(0);
                } else {
                    imageView.setVisibility(8);
                    imageView.setImageDrawable((Drawable) null);
                }
            }
            boolean z = !TextUtils.isEmpty(text);
            if (textView2 != null) {
                if (z) {
                    textView2.setText(text);
                    if (this.tab.labelVisibilityMode == 1) {
                        textView2.setVisibility(0);
                    } else {
                        textView2.setVisibility(8);
                    }
                    setVisibility(0);
                } else {
                    textView2.setVisibility(8);
                    textView2.setText((CharSequence) null);
                }
            }
            if (imageView != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                int dpToPx = (!z || imageView.getVisibility() != 0) ? 0 : (int) ViewUtils.dpToPx(getContext(), 8);
                if (TabLayout.this.inlineLabel) {
                    if (dpToPx != MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams)) {
                        MarginLayoutParamsCompat.setMarginEnd(marginLayoutParams, dpToPx);
                        marginLayoutParams.bottomMargin = 0;
                        imageView.setLayoutParams(marginLayoutParams);
                        imageView.requestLayout();
                    }
                } else if (dpToPx != marginLayoutParams.bottomMargin) {
                    marginLayoutParams.bottomMargin = dpToPx;
                    MarginLayoutParamsCompat.setMarginEnd(marginLayoutParams, 0);
                    imageView.setLayoutParams(marginLayoutParams);
                    imageView.requestLayout();
                }
            }
            Tab tab4 = this.tab;
            if (tab4 != null) {
                charSequence = tab4.contentDesc;
            }
            if (!z) {
                text = charSequence;
            }
            TooltipCompat.setTooltipText(this, text);
        }

        /* access modifiers changed from: private */
        public void tryUpdateBadgeDrawableBounds(View view) {
            if (hasBadgeDrawable() && view == this.badgeAnchorView) {
                BadgeUtils.setBadgeDrawableBounds(this.badgeDrawable, view, getCustomParentForBadge(view));
            }
        }

        /* access modifiers changed from: private */
        public boolean hasBadgeDrawable() {
            return this.badgeDrawable != null;
        }

        private FrameLayout getCustomParentForBadge(View view) {
            if ((view == this.iconView || view == this.textView) && BadgeUtils.USE_COMPAT_PARENT) {
                return (FrameLayout) view.getParent();
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public int getContentWidth() {
            View[] viewArr = {this.textView, this.iconView, this.customView};
            int i = 0;
            int i2 = 0;
            boolean z = false;
            for (int i3 = 0; i3 < 3; i3++) {
                View view = viewArr[i3];
                if (view != null && view.getVisibility() == 0) {
                    i2 = z ? Math.min(i2, view.getLeft()) : view.getLeft();
                    i = z ? Math.max(i, view.getRight()) : view.getRight();
                    z = true;
                }
            }
            return i - i2;
        }

        /* access modifiers changed from: package-private */
        public int getContentHeight() {
            View[] viewArr = {this.textView, this.iconView, this.customView};
            int i = 0;
            int i2 = 0;
            boolean z = false;
            for (int i3 = 0; i3 < 3; i3++) {
                View view = viewArr[i3];
                if (view != null && view.getVisibility() == 0) {
                    i2 = z ? Math.min(i2, view.getTop()) : view.getTop();
                    i = z ? Math.max(i, view.getBottom()) : view.getBottom();
                    z = true;
                }
            }
            return i - i2;
        }

        public Tab getTab() {
            return this.tab;
        }

        private float approximateLineWidth(Layout layout, int i, float f) {
            return layout.getLineWidth(i) * (f / layout.getPaint().getTextSize());
        }
    }

    class SlidingTabIndicator extends LinearLayout {
        ValueAnimator indicatorAnimator;
        private int layoutDirection = -1;
        int selectedPosition = -1;
        float selectionOffset;

        SlidingTabIndicator(Context context) {
            super(context);
            setWillNotDraw(false);
        }

        /* access modifiers changed from: package-private */
        public void setSelectedIndicatorHeight(int i) {
            Rect bounds = TabLayout.this.tabSelectedIndicator.getBounds();
            TabLayout.this.tabSelectedIndicator.setBounds(bounds.left, 0, bounds.right, i);
            requestLayout();
        }

        /* access modifiers changed from: package-private */
        public boolean childrenNeedLayout() {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (getChildAt(i).getWidth() <= 0) {
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public void setIndicatorPositionFromTabPosition(int i, float f) {
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }
            this.selectedPosition = i;
            this.selectionOffset = f;
            tweenIndicatorPosition(getChildAt(i), getChildAt(this.selectedPosition + 1), this.selectionOffset);
        }

        /* access modifiers changed from: package-private */
        public float getIndicatorPosition() {
            return ((float) this.selectedPosition) + this.selectionOffset;
        }

        public void onRtlPropertiesChanged(int i) {
            super.onRtlPropertiesChanged(i);
            if (Build.VERSION.SDK_INT < 23 && this.layoutDirection != i) {
                requestLayout();
                this.layoutDirection = i;
            }
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            if (View.MeasureSpec.getMode(i) == 1073741824) {
                boolean z = true;
                if (TabLayout.this.tabGravity == 1 || TabLayout.this.mode == 2) {
                    int childCount = getChildCount();
                    int i3 = 0;
                    for (int i4 = 0; i4 < childCount; i4++) {
                        View childAt = getChildAt(i4);
                        if (childAt.getVisibility() == 0) {
                            i3 = Math.max(i3, childAt.getMeasuredWidth());
                        }
                    }
                    if (i3 > 0) {
                        if (i3 * childCount <= getMeasuredWidth() - (((int) ViewUtils.dpToPx(getContext(), 16)) * 2)) {
                            boolean z2 = false;
                            for (int i5 = 0; i5 < childCount; i5++) {
                                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getChildAt(i5).getLayoutParams();
                                if (layoutParams.width != i3 || layoutParams.weight != 0.0f) {
                                    layoutParams.width = i3;
                                    layoutParams.weight = 0.0f;
                                    z2 = true;
                                }
                            }
                            z = z2;
                        } else {
                            TabLayout.this.tabGravity = 0;
                            TabLayout.this.updateTabViews(false);
                        }
                        if (z) {
                            super.onMeasure(i, i2);
                        }
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator == null || !valueAnimator.isRunning()) {
                jumpIndicatorToSelectedPosition();
            } else {
                updateOrRecreateIndicatorAnimation(false, this.selectedPosition, -1);
            }
        }

        private void jumpIndicatorToSelectedPosition() {
            View childAt = getChildAt(this.selectedPosition);
            TabIndicatorInterpolator access$1200 = TabLayout.this.tabIndicatorInterpolator;
            TabLayout tabLayout = TabLayout.this;
            access$1200.setIndicatorBoundsForTab(tabLayout, childAt, tabLayout.tabSelectedIndicator);
        }

        /* access modifiers changed from: private */
        public void tweenIndicatorPosition(View view, View view2, float f) {
            if (view != null && view.getWidth() > 0) {
                TabIndicatorInterpolator access$1200 = TabLayout.this.tabIndicatorInterpolator;
                TabLayout tabLayout = TabLayout.this;
                access$1200.setIndicatorBoundsForOffset(tabLayout, view, view2, f, tabLayout.tabSelectedIndicator);
            } else {
                TabLayout.this.tabSelectedIndicator.setBounds(-1, TabLayout.this.tabSelectedIndicator.getBounds().top, -1, TabLayout.this.tabSelectedIndicator.getBounds().bottom);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }

        /* access modifiers changed from: package-private */
        public void animateIndicatorToPosition(int i, int i2) {
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }
            updateOrRecreateIndicatorAnimation(true, i, i2);
        }

        private void updateOrRecreateIndicatorAnimation(boolean z, final int i, int i2) {
            final View childAt = getChildAt(this.selectedPosition);
            final View childAt2 = getChildAt(i);
            if (childAt2 == null) {
                jumpIndicatorToSelectedPosition();
                return;
            }
            C06391 r2 = new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    SlidingTabIndicator.this.tweenIndicatorPosition(childAt, childAt2, valueAnimator.getAnimatedFraction());
                }
            };
            if (z) {
                ValueAnimator valueAnimator = new ValueAnimator();
                this.indicatorAnimator = valueAnimator;
                valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                valueAnimator.setDuration((long) i2);
                valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
                valueAnimator.addUpdateListener(r2);
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animator) {
                        SlidingTabIndicator.this.selectedPosition = i;
                    }

                    public void onAnimationEnd(Animator animator) {
                        SlidingTabIndicator.this.selectedPosition = i;
                    }
                });
                valueAnimator.start();
                return;
            }
            this.indicatorAnimator.removeAllUpdateListeners();
            this.indicatorAnimator.addUpdateListener(r2);
        }

        public void draw(Canvas canvas) {
            int height = TabLayout.this.tabSelectedIndicator.getBounds().height();
            if (height < 0) {
                height = TabLayout.this.tabSelectedIndicator.getIntrinsicHeight();
            }
            int i = TabLayout.this.tabIndicatorGravity;
            int i2 = 0;
            if (i == 0) {
                i2 = getHeight() - height;
                height = getHeight();
            } else if (i == 1) {
                i2 = (getHeight() - height) / 2;
                height = (getHeight() + height) / 2;
            } else if (i != 2) {
                height = i != 3 ? 0 : getHeight();
            }
            if (TabLayout.this.tabSelectedIndicator.getBounds().width() > 0) {
                Rect bounds = TabLayout.this.tabSelectedIndicator.getBounds();
                TabLayout.this.tabSelectedIndicator.setBounds(bounds.left, i2, bounds.right, height);
                Drawable drawable = TabLayout.this.tabSelectedIndicator;
                if (TabLayout.this.tabSelectedIndicatorColor != 0) {
                    drawable = DrawableCompat.wrap(drawable);
                    if (Build.VERSION.SDK_INT == 21) {
                        drawable.setColorFilter(TabLayout.this.tabSelectedIndicatorColor, PorterDuff.Mode.SRC_IN);
                    } else {
                        DrawableCompat.setTint(drawable, TabLayout.this.tabSelectedIndicatorColor);
                    }
                }
                drawable.draw(canvas);
            }
            super.draw(canvas);
        }
    }

    private static ColorStateList createColorStateList(int i, int i2) {
        return new ColorStateList(new int[][]{SELECTED_STATE_SET, EMPTY_STATE_SET}, new int[]{i2, i});
    }

    private int getDefaultHeight() {
        int size = this.tabs.size();
        boolean z = false;
        int i = 0;
        while (true) {
            if (i < size) {
                Tab tab = this.tabs.get(i);
                if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty(tab.getText())) {
                    z = true;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        return (!z || this.inlineLabel) ? 48 : 72;
    }

    private int getTabMinWidth() {
        int i = this.requestedTabMinWidth;
        if (i != -1) {
            return i;
        }
        int i2 = this.mode;
        if (i2 == 0 || i2 == 2) {
            return this.scrollableTabMinWidth;
        }
        return 0;
    }

    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return generateDefaultLayoutParams();
    }

    /* access modifiers changed from: package-private */
    public int getTabMaxWidth() {
        return this.tabMaxWidth;
    }

    public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int previousScrollState;
        private int scrollState;
        private final WeakReference<TabLayout> tabLayoutRef;

        public TabLayoutOnPageChangeListener(TabLayout tabLayout) {
            this.tabLayoutRef = new WeakReference<>(tabLayout);
        }

        public void onPageScrollStateChanged(int i) {
            this.previousScrollState = this.scrollState;
            this.scrollState = i;
        }

        public void onPageScrolled(int i, float f, int i2) {
            TabLayout tabLayout = (TabLayout) this.tabLayoutRef.get();
            if (tabLayout != null) {
                int i3 = this.scrollState;
                boolean z = false;
                boolean z2 = i3 != 2 || this.previousScrollState == 1;
                if (!(i3 == 2 && this.previousScrollState == 0)) {
                    z = true;
                }
                tabLayout.setScrollPosition(i, f, z2, z);
            }
        }

        public void onPageSelected(int i) {
            TabLayout tabLayout = (TabLayout) this.tabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != i && i < tabLayout.getTabCount()) {
                int i2 = this.scrollState;
                tabLayout.selectTab(tabLayout.getTabAt(i), i2 == 0 || (i2 == 2 && this.previousScrollState == 0));
            }
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            this.scrollState = 0;
            this.previousScrollState = 0;
        }
    }

    public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
        private final ViewPager viewPager;

        public void onTabReselected(Tab tab) {
        }

        public void onTabUnselected(Tab tab) {
        }

        public ViewPagerOnTabSelectedListener(ViewPager viewPager2) {
            this.viewPager = viewPager2;
        }

        public void onTabSelected(Tab tab) {
            this.viewPager.setCurrentItem(tab.getPosition());
        }
    }

    private class PagerAdapterObserver extends DataSetObserver {
        PagerAdapterObserver() {
        }

        public void onChanged() {
            TabLayout.this.populateFromPagerAdapter();
        }

        public void onInvalidated() {
            TabLayout.this.populateFromPagerAdapter();
        }
    }

    private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
        private boolean autoRefresh;

        AdapterChangeListener() {
        }

        public void onAdapterChanged(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
            if (TabLayout.this.viewPager == viewPager) {
                TabLayout.this.setPagerAdapter(pagerAdapter2, this.autoRefresh);
            }
        }

        /* access modifiers changed from: package-private */
        public void setAutoRefresh(boolean z) {
            this.autoRefresh = z;
        }
    }
}
