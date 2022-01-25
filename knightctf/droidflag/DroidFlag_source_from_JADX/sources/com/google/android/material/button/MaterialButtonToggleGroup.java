package com.google.android.material.button;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.C0460R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;

public class MaterialButtonToggleGroup extends LinearLayout {
    private static final int DEF_STYLE_RES = C0460R.style.Widget_MaterialComponents_MaterialButtonToggleGroup;
    private static final String LOG_TAG = "MaterialButtonToggleGroup";
    /* access modifiers changed from: private */
    public int checkedId;
    private final CheckedStateTracker checkedStateTracker;
    private Integer[] childOrder;
    private final Comparator<MaterialButton> childOrderComparator;
    private final LinkedHashSet<OnButtonCheckedListener> onButtonCheckedListeners;
    private final List<CornerData> originalCornerData;
    private final PressedStateTracker pressedStateTracker;
    private boolean selectionRequired;
    /* access modifiers changed from: private */
    public boolean singleSelection;
    /* access modifiers changed from: private */
    public boolean skipCheckedStateTracker;

    public interface OnButtonCheckedListener {
        void onButtonChecked(MaterialButtonToggleGroup materialButtonToggleGroup, int i, boolean z);
    }

    public MaterialButtonToggleGroup(Context context) {
        this(context, (AttributeSet) null);
    }

    public MaterialButtonToggleGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0460R.attr.materialButtonToggleGroupStyle);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MaterialButtonToggleGroup(android.content.Context r7, android.util.AttributeSet r8, int r9) {
        /*
            r6 = this;
            int r4 = DEF_STYLE_RES
            android.content.Context r7 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r7, r8, r9, r4)
            r6.<init>(r7, r8, r9)
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            r6.originalCornerData = r7
            com.google.android.material.button.MaterialButtonToggleGroup$CheckedStateTracker r7 = new com.google.android.material.button.MaterialButtonToggleGroup$CheckedStateTracker
            r0 = 0
            r7.<init>()
            r6.checkedStateTracker = r7
            com.google.android.material.button.MaterialButtonToggleGroup$PressedStateTracker r7 = new com.google.android.material.button.MaterialButtonToggleGroup$PressedStateTracker
            r7.<init>()
            r6.pressedStateTracker = r7
            java.util.LinkedHashSet r7 = new java.util.LinkedHashSet
            r7.<init>()
            r6.onButtonCheckedListeners = r7
            com.google.android.material.button.MaterialButtonToggleGroup$1 r7 = new com.google.android.material.button.MaterialButtonToggleGroup$1
            r7.<init>()
            r6.childOrderComparator = r7
            r7 = 0
            r6.skipCheckedStateTracker = r7
            android.content.Context r0 = r6.getContext()
            int[] r2 = com.google.android.material.C0460R.styleable.MaterialButtonToggleGroup
            int[] r5 = new int[r7]
            r1 = r8
            r3 = r9
            android.content.res.TypedArray r8 = com.google.android.material.internal.ThemeEnforcement.obtainStyledAttributes(r0, r1, r2, r3, r4, r5)
            int r9 = com.google.android.material.C0460R.styleable.MaterialButtonToggleGroup_singleSelection
            boolean r9 = r8.getBoolean(r9, r7)
            r6.setSingleSelection((boolean) r9)
            int r9 = com.google.android.material.C0460R.styleable.MaterialButtonToggleGroup_checkedButton
            r0 = -1
            int r9 = r8.getResourceId(r9, r0)
            r6.checkedId = r9
            int r9 = com.google.android.material.C0460R.styleable.MaterialButtonToggleGroup_selectionRequired
            boolean r7 = r8.getBoolean(r9, r7)
            r6.selectionRequired = r7
            r7 = 1
            r6.setChildrenDrawingOrderEnabled(r7)
            r8.recycle()
            androidx.core.view.ViewCompat.setImportantForAccessibility(r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.button.MaterialButtonToggleGroup.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        int i = this.checkedId;
        if (i != -1) {
            checkForced(i);
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        updateChildOrder();
        super.dispatchDraw(canvas);
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (!(view instanceof MaterialButton)) {
            Log.e(LOG_TAG, "Child views must be of type MaterialButton.");
            return;
        }
        super.addView(view, i, layoutParams);
        MaterialButton materialButton = (MaterialButton) view;
        setGeneratedIdIfNeeded(materialButton);
        setupButtonChild(materialButton);
        if (materialButton.isChecked()) {
            updateCheckedStates(materialButton.getId(), true);
            setCheckedId(materialButton.getId());
        }
        ShapeAppearanceModel shapeAppearanceModel = materialButton.getShapeAppearanceModel();
        this.originalCornerData.add(new CornerData(shapeAppearanceModel.getTopLeftCornerSize(), shapeAppearanceModel.getBottomLeftCornerSize(), shapeAppearanceModel.getTopRightCornerSize(), shapeAppearanceModel.getBottomRightCornerSize()));
        ViewCompat.setAccessibilityDelegate(materialButton, new AccessibilityDelegateCompat() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, MaterialButtonToggleGroup.this.getIndexWithinVisibleButtons(view), 1, false, ((MaterialButton) view).isChecked()));
            }
        });
    }

    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (view instanceof MaterialButton) {
            MaterialButton materialButton = (MaterialButton) view;
            materialButton.removeOnCheckedChangeListener(this.checkedStateTracker);
            materialButton.setOnPressedChangeListenerInternal((MaterialButton.OnPressedChangeListener) null);
        }
        int indexOfChild = indexOfChild(view);
        if (indexOfChild >= 0) {
            this.originalCornerData.remove(indexOfChild);
        }
        updateChildShapes();
        adjustChildMarginsAndUpdateLayout();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        updateChildShapes();
        adjustChildMarginsAndUpdateLayout();
        super.onMeasure(i, i2);
    }

    public CharSequence getAccessibilityClassName() {
        return MaterialButtonToggleGroup.class.getName();
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, getVisibleButtonCount(), false, isSingleSelection() ? 1 : 2));
    }

    public void check(int i) {
        if (i != this.checkedId) {
            checkForced(i);
        }
    }

    public void uncheck(int i) {
        setCheckedStateForView(i, false);
        updateCheckedStates(i, false);
        this.checkedId = -1;
        dispatchOnButtonChecked(i, false);
    }

    public void clearChecked() {
        this.skipCheckedStateTracker = true;
        for (int i = 0; i < getChildCount(); i++) {
            MaterialButton childButton = getChildButton(i);
            childButton.setChecked(false);
            dispatchOnButtonChecked(childButton.getId(), false);
        }
        this.skipCheckedStateTracker = false;
        setCheckedId(-1);
    }

    public int getCheckedButtonId() {
        if (this.singleSelection) {
            return this.checkedId;
        }
        return -1;
    }

    public List<Integer> getCheckedButtonIds() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < getChildCount(); i++) {
            MaterialButton childButton = getChildButton(i);
            if (childButton.isChecked()) {
                arrayList.add(Integer.valueOf(childButton.getId()));
            }
        }
        return arrayList;
    }

    public void addOnButtonCheckedListener(OnButtonCheckedListener onButtonCheckedListener) {
        this.onButtonCheckedListeners.add(onButtonCheckedListener);
    }

    public void removeOnButtonCheckedListener(OnButtonCheckedListener onButtonCheckedListener) {
        this.onButtonCheckedListeners.remove(onButtonCheckedListener);
    }

    public void clearOnButtonCheckedListeners() {
        this.onButtonCheckedListeners.clear();
    }

    public boolean isSingleSelection() {
        return this.singleSelection;
    }

    public void setSingleSelection(boolean z) {
        if (this.singleSelection != z) {
            this.singleSelection = z;
            clearChecked();
        }
    }

    public void setSelectionRequired(boolean z) {
        this.selectionRequired = z;
    }

    public boolean isSelectionRequired() {
        return this.selectionRequired;
    }

    public void setSingleSelection(int i) {
        setSingleSelection(getResources().getBoolean(i));
    }

    private void setCheckedStateForView(int i, boolean z) {
        View findViewById = findViewById(i);
        if (findViewById instanceof MaterialButton) {
            this.skipCheckedStateTracker = true;
            ((MaterialButton) findViewById).setChecked(z);
            this.skipCheckedStateTracker = false;
        }
    }

    private void setCheckedId(int i) {
        this.checkedId = i;
        dispatchOnButtonChecked(i, true);
    }

    private void adjustChildMarginsAndUpdateLayout() {
        int firstVisibleChildIndex = getFirstVisibleChildIndex();
        if (firstVisibleChildIndex != -1) {
            for (int i = firstVisibleChildIndex + 1; i < getChildCount(); i++) {
                MaterialButton childButton = getChildButton(i);
                int min = Math.min(childButton.getStrokeWidth(), getChildButton(i - 1).getStrokeWidth());
                LinearLayout.LayoutParams buildLayoutParams = buildLayoutParams(childButton);
                if (getOrientation() == 0) {
                    MarginLayoutParamsCompat.setMarginEnd(buildLayoutParams, 0);
                    MarginLayoutParamsCompat.setMarginStart(buildLayoutParams, -min);
                    buildLayoutParams.topMargin = 0;
                } else {
                    buildLayoutParams.bottomMargin = 0;
                    buildLayoutParams.topMargin = -min;
                    MarginLayoutParamsCompat.setMarginStart(buildLayoutParams, 0);
                }
                childButton.setLayoutParams(buildLayoutParams);
            }
            resetChildMargins(firstVisibleChildIndex);
        }
    }

    private MaterialButton getChildButton(int i) {
        return (MaterialButton) getChildAt(i);
    }

    private void resetChildMargins(int i) {
        if (getChildCount() != 0 && i != -1) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getChildButton(i).getLayoutParams();
            if (getOrientation() == 1) {
                layoutParams.topMargin = 0;
                layoutParams.bottomMargin = 0;
                return;
            }
            MarginLayoutParamsCompat.setMarginEnd(layoutParams, 0);
            MarginLayoutParamsCompat.setMarginStart(layoutParams, 0);
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
        }
    }

    /* access modifiers changed from: package-private */
    public void updateChildShapes() {
        int childCount = getChildCount();
        int firstVisibleChildIndex = getFirstVisibleChildIndex();
        int lastVisibleChildIndex = getLastVisibleChildIndex();
        for (int i = 0; i < childCount; i++) {
            MaterialButton childButton = getChildButton(i);
            if (childButton.getVisibility() != 8) {
                ShapeAppearanceModel.Builder builder = childButton.getShapeAppearanceModel().toBuilder();
                updateBuilderWithCornerData(builder, getNewCornerData(i, firstVisibleChildIndex, lastVisibleChildIndex));
                childButton.setShapeAppearanceModel(builder.build());
            }
        }
    }

    private int getFirstVisibleChildIndex() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (isChildVisible(i)) {
                return i;
            }
        }
        return -1;
    }

    private int getLastVisibleChildIndex() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            if (isChildVisible(childCount)) {
                return childCount;
            }
        }
        return -1;
    }

    private boolean isChildVisible(int i) {
        return getChildAt(i).getVisibility() != 8;
    }

    private int getVisibleButtonCount() {
        int i = 0;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            if ((getChildAt(i2) instanceof MaterialButton) && isChildVisible(i2)) {
                i++;
            }
        }
        return i;
    }

    /* access modifiers changed from: private */
    public int getIndexWithinVisibleButtons(View view) {
        if (!(view instanceof MaterialButton)) {
            return -1;
        }
        int i = 0;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            if (getChildAt(i2) == view) {
                return i;
            }
            if ((getChildAt(i2) instanceof MaterialButton) && isChildVisible(i2)) {
                i++;
            }
        }
        return -1;
    }

    private CornerData getNewCornerData(int i, int i2, int i3) {
        CornerData cornerData = this.originalCornerData.get(i);
        if (i2 == i3) {
            return cornerData;
        }
        boolean z = getOrientation() == 0;
        if (i == i2) {
            return z ? CornerData.start(cornerData, this) : CornerData.top(cornerData);
        }
        if (i == i3) {
            return z ? CornerData.end(cornerData, this) : CornerData.bottom(cornerData);
        }
        return null;
    }

    private static void updateBuilderWithCornerData(ShapeAppearanceModel.Builder builder, CornerData cornerData) {
        if (cornerData == null) {
            builder.setAllCornerSizes(0.0f);
        } else {
            builder.setTopLeftCornerSize(cornerData.topLeft).setBottomLeftCornerSize(cornerData.bottomLeft).setTopRightCornerSize(cornerData.topRight).setBottomRightCornerSize(cornerData.bottomRight);
        }
    }

    /* access modifiers changed from: private */
    public boolean updateCheckedStates(int i, boolean z) {
        List<Integer> checkedButtonIds = getCheckedButtonIds();
        if (!this.selectionRequired || !checkedButtonIds.isEmpty()) {
            if (z && this.singleSelection) {
                checkedButtonIds.remove(Integer.valueOf(i));
                for (Integer intValue : checkedButtonIds) {
                    int intValue2 = intValue.intValue();
                    setCheckedStateForView(intValue2, false);
                    dispatchOnButtonChecked(intValue2, false);
                }
            }
            return true;
        }
        setCheckedStateForView(i, true);
        this.checkedId = i;
        return false;
    }

    /* access modifiers changed from: private */
    public void dispatchOnButtonChecked(int i, boolean z) {
        Iterator it = this.onButtonCheckedListeners.iterator();
        while (it.hasNext()) {
            ((OnButtonCheckedListener) it.next()).onButtonChecked(this, i, z);
        }
    }

    private void checkForced(int i) {
        setCheckedStateForView(i, true);
        updateCheckedStates(i, true);
        setCheckedId(i);
    }

    private void setGeneratedIdIfNeeded(MaterialButton materialButton) {
        if (materialButton.getId() == -1) {
            materialButton.setId(ViewCompat.generateViewId());
        }
    }

    private void setupButtonChild(MaterialButton materialButton) {
        materialButton.setMaxLines(1);
        materialButton.setEllipsize(TextUtils.TruncateAt.END);
        materialButton.setCheckable(true);
        materialButton.addOnCheckedChangeListener(this.checkedStateTracker);
        materialButton.setOnPressedChangeListenerInternal(this.pressedStateTracker);
        materialButton.setShouldDrawSurfaceColorStroke(true);
    }

    private LinearLayout.LayoutParams buildLayoutParams(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            return (LinearLayout.LayoutParams) layoutParams;
        }
        return new LinearLayout.LayoutParams(layoutParams.width, layoutParams.height);
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int i, int i2) {
        Integer[] numArr = this.childOrder;
        if (numArr != null && i2 < numArr.length) {
            return numArr[i2].intValue();
        }
        Log.w(LOG_TAG, "Child order wasn't updated");
        return i2;
    }

    private void updateChildOrder() {
        TreeMap treeMap = new TreeMap(this.childOrderComparator);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            treeMap.put(getChildButton(i), Integer.valueOf(i));
        }
        this.childOrder = (Integer[]) treeMap.values().toArray(new Integer[0]);
    }

    private class CheckedStateTracker implements MaterialButton.OnCheckedChangeListener {
        private CheckedStateTracker() {
        }

        public void onCheckedChanged(MaterialButton materialButton, boolean z) {
            if (!MaterialButtonToggleGroup.this.skipCheckedStateTracker) {
                if (MaterialButtonToggleGroup.this.singleSelection) {
                    int unused = MaterialButtonToggleGroup.this.checkedId = z ? materialButton.getId() : -1;
                }
                if (MaterialButtonToggleGroup.this.updateCheckedStates(materialButton.getId(), z)) {
                    MaterialButtonToggleGroup.this.dispatchOnButtonChecked(materialButton.getId(), materialButton.isChecked());
                }
                MaterialButtonToggleGroup.this.invalidate();
            }
        }
    }

    private class PressedStateTracker implements MaterialButton.OnPressedChangeListener {
        private PressedStateTracker() {
        }

        public void onPressedChanged(MaterialButton materialButton, boolean z) {
            MaterialButtonToggleGroup.this.invalidate();
        }
    }

    private static class CornerData {
        private static final CornerSize noCorner = new AbsoluteCornerSize(0.0f);
        CornerSize bottomLeft;
        CornerSize bottomRight;
        CornerSize topLeft;
        CornerSize topRight;

        CornerData(CornerSize cornerSize, CornerSize cornerSize2, CornerSize cornerSize3, CornerSize cornerSize4) {
            this.topLeft = cornerSize;
            this.topRight = cornerSize3;
            this.bottomRight = cornerSize4;
            this.bottomLeft = cornerSize2;
        }

        public static CornerData start(CornerData cornerData, View view) {
            return ViewUtils.isLayoutRtl(view) ? right(cornerData) : left(cornerData);
        }

        public static CornerData end(CornerData cornerData, View view) {
            return ViewUtils.isLayoutRtl(view) ? left(cornerData) : right(cornerData);
        }

        public static CornerData left(CornerData cornerData) {
            CornerSize cornerSize = cornerData.topLeft;
            CornerSize cornerSize2 = cornerData.bottomLeft;
            CornerSize cornerSize3 = noCorner;
            return new CornerData(cornerSize, cornerSize2, cornerSize3, cornerSize3);
        }

        public static CornerData right(CornerData cornerData) {
            CornerSize cornerSize = noCorner;
            return new CornerData(cornerSize, cornerSize, cornerData.topRight, cornerData.bottomRight);
        }

        public static CornerData top(CornerData cornerData) {
            CornerSize cornerSize = cornerData.topLeft;
            CornerSize cornerSize2 = noCorner;
            return new CornerData(cornerSize, cornerSize2, cornerData.topRight, cornerSize2);
        }

        public static CornerData bottom(CornerData cornerData) {
            CornerSize cornerSize = noCorner;
            return new CornerData(cornerSize, cornerData.bottomLeft, cornerSize, cornerData.bottomRight);
        }
    }
}
