package androidx.constraintlayout.solver.widgets;

import androidx.appcompat.widget.ActivityChooserView;
import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.analyzer.ChainRun;
import androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ConstraintWidget {
    public static final int ANCHOR_BASELINE = 4;
    public static final int ANCHOR_BOTTOM = 3;
    public static final int ANCHOR_LEFT = 0;
    public static final int ANCHOR_RIGHT = 1;
    public static final int ANCHOR_TOP = 2;
    private static final boolean AUTOTAG_CENTER = false;
    public static final int BOTH = 2;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    public static float DEFAULT_BIAS = 0.5f;
    static final int DIMENSION_HORIZONTAL = 0;
    static final int DIMENSION_VERTICAL = 1;
    protected static final int DIRECT = 2;
    public static final int GONE = 8;
    public static final int HORIZONTAL = 0;
    public static final int INVISIBLE = 4;
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    public static final int MATCH_CONSTRAINT_RATIO = 3;
    public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    protected static final int SOLVER = 1;
    public static final int UNKNOWN = -1;
    private static final boolean USE_WRAP_DIMENSION_FOR_SPREAD = false;
    public static final int VERTICAL = 1;
    public static final int VISIBLE = 0;
    private static final int WRAP = -2;
    private boolean OPTIMIZE_WRAP;
    private boolean OPTIMIZE_WRAP_ON_RESOLVED;
    private boolean hasBaseline;
    public ChainRun horizontalChainRun;
    public int horizontalGroup;
    public HorizontalWidgetRun horizontalRun;
    private boolean inPlaceholder;
    public boolean[] isTerminalWidget;
    protected ArrayList<ConstraintAnchor> mAnchors;
    public ConstraintAnchor mBaseline;
    int mBaselineDistance;
    public ConstraintAnchor mBottom;
    boolean mBottomHasCentered;
    public ConstraintAnchor mCenter;
    ConstraintAnchor mCenterX;
    ConstraintAnchor mCenterY;
    private float mCircleConstraintAngle;
    private Object mCompanionWidget;
    private int mContainerItemSkip;
    private String mDebugName;
    public float mDimensionRatio;
    protected int mDimensionRatioSide;
    int mDistToBottom;
    int mDistToLeft;
    int mDistToRight;
    int mDistToTop;
    boolean mGroupsToSolver;
    int mHeight;
    float mHorizontalBiasPercent;
    boolean mHorizontalChainFixedPosition;
    int mHorizontalChainStyle;
    ConstraintWidget mHorizontalNextWidget;
    public int mHorizontalResolution;
    boolean mHorizontalWrapVisited;
    private boolean mInVirtuaLayout;
    public boolean mIsHeightWrapContent;
    private boolean[] mIsInBarrier;
    public boolean mIsWidthWrapContent;
    private int mLastHorizontalMeasureSpec;
    private int mLastVerticalMeasureSpec;
    public ConstraintAnchor mLeft;
    boolean mLeftHasCentered;
    public ConstraintAnchor[] mListAnchors;
    public DimensionBehaviour[] mListDimensionBehaviors;
    protected ConstraintWidget[] mListNextMatchConstraintsWidget;
    public int mMatchConstraintDefaultHeight;
    public int mMatchConstraintDefaultWidth;
    public int mMatchConstraintMaxHeight;
    public int mMatchConstraintMaxWidth;
    public int mMatchConstraintMinHeight;
    public int mMatchConstraintMinWidth;
    public float mMatchConstraintPercentHeight;
    public float mMatchConstraintPercentWidth;
    private int[] mMaxDimension;
    private boolean mMeasureRequested;
    protected int mMinHeight;
    protected int mMinWidth;
    protected ConstraintWidget[] mNextChainWidget;
    protected int mOffsetX;
    protected int mOffsetY;
    public ConstraintWidget mParent;
    int mRelX;
    int mRelY;
    float mResolvedDimensionRatio;
    int mResolvedDimensionRatioSide;
    boolean mResolvedHasRatio;
    public int[] mResolvedMatchConstraintDefault;
    public ConstraintAnchor mRight;
    boolean mRightHasCentered;
    public ConstraintAnchor mTop;
    boolean mTopHasCentered;
    private String mType;
    float mVerticalBiasPercent;
    boolean mVerticalChainFixedPosition;
    int mVerticalChainStyle;
    ConstraintWidget mVerticalNextWidget;
    public int mVerticalResolution;
    boolean mVerticalWrapVisited;
    private int mVisibility;
    public float[] mWeight;
    int mWidth;

    /* renamed from: mX */
    protected int f35mX;

    /* renamed from: mY */
    protected int f36mY;
    public boolean measured;
    private boolean resolvedHorizontal;
    private boolean resolvedVertical;
    public WidgetRun[] run;
    public ChainRun verticalChainRun;
    public int verticalGroup;
    public VerticalWidgetRun verticalRun;

    public enum DimensionBehaviour {
        FIXED,
        WRAP_CONTENT,
        MATCH_CONSTRAINT,
        MATCH_PARENT
    }

    public WidgetRun getRun(int i) {
        if (i == 0) {
            return this.horizontalRun;
        }
        if (i == 1) {
            return this.verticalRun;
        }
        return null;
    }

    public void setFinalFrame(int i, int i2, int i3, int i4, int i5, int i6) {
        setFrame(i, i2, i3, i4);
        setBaselineDistance(i5);
        if (i6 == 0) {
            this.resolvedHorizontal = true;
            this.resolvedVertical = false;
        } else if (i6 == 1) {
            this.resolvedHorizontal = false;
            this.resolvedVertical = true;
        } else if (i6 == 2) {
            this.resolvedHorizontal = true;
            this.resolvedVertical = true;
        } else {
            this.resolvedHorizontal = false;
            this.resolvedVertical = false;
        }
    }

    public void setFinalLeft(int i) {
        this.mLeft.setFinalValue(i);
        this.f35mX = i;
    }

    public void setFinalTop(int i) {
        this.mTop.setFinalValue(i);
        this.f36mY = i;
    }

    public void setFinalHorizontal(int i, int i2) {
        this.mLeft.setFinalValue(i);
        this.mRight.setFinalValue(i2);
        this.f35mX = i;
        this.mWidth = i2 - i;
        this.resolvedHorizontal = true;
    }

    public void setFinalVertical(int i, int i2) {
        this.mTop.setFinalValue(i);
        this.mBottom.setFinalValue(i2);
        this.f36mY = i;
        this.mHeight = i2 - i;
        if (this.hasBaseline) {
            this.mBaseline.setFinalValue(i + this.mBaselineDistance);
        }
        this.resolvedVertical = true;
    }

    public void setFinalBaseline(int i) {
        if (this.hasBaseline) {
            int i2 = i - this.mBaselineDistance;
            int i3 = this.mHeight + i2;
            this.f36mY = i2;
            this.mTop.setFinalValue(i2);
            this.mBottom.setFinalValue(i3);
            this.mBaseline.setFinalValue(i);
            this.resolvedVertical = true;
        }
    }

    public boolean isResolvedHorizontally() {
        return this.resolvedHorizontal || (this.mLeft.hasFinalValue() && this.mRight.hasFinalValue());
    }

    public boolean isResolvedVertically() {
        return this.resolvedVertical || (this.mTop.hasFinalValue() && this.mBottom.hasFinalValue());
    }

    public void resetFinalResolution() {
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        int size = this.mAnchors.size();
        for (int i = 0; i < size; i++) {
            this.mAnchors.get(i).resetFinalResolution();
        }
    }

    public void ensureMeasureRequested() {
        this.mMeasureRequested = true;
    }

    public boolean hasDependencies() {
        int size = this.mAnchors.size();
        for (int i = 0; i < size; i++) {
            if (this.mAnchors.get(i).hasDependents()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDanglingDimension(int i) {
        if (i == 0) {
            return (this.mLeft.mTarget != null ? 1 : 0) + (this.mRight.mTarget != null ? 1 : 0) < 2;
        }
        if ((this.mTop.mTarget != null ? 1 : 0) + (this.mBottom.mTarget != null ? 1 : 0) + (this.mBaseline.mTarget != null ? 1 : 0) < 2) {
            return true;
        }
        return false;
    }

    public boolean isInVirtualLayout() {
        return this.mInVirtuaLayout;
    }

    public void setInVirtualLayout(boolean z) {
        this.mInVirtuaLayout = z;
    }

    public int getMaxHeight() {
        return this.mMaxDimension[1];
    }

    public int getMaxWidth() {
        return this.mMaxDimension[0];
    }

    public void setMaxWidth(int i) {
        this.mMaxDimension[0] = i;
    }

    public void setMaxHeight(int i) {
        this.mMaxDimension[1] = i;
    }

    public boolean isSpreadWidth() {
        return this.mMatchConstraintDefaultWidth == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMaxWidth == 0 && this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public boolean isSpreadHeight() {
        return this.mMatchConstraintDefaultHeight == 0 && this.mDimensionRatio == 0.0f && this.mMatchConstraintMinHeight == 0 && this.mMatchConstraintMaxHeight == 0 && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public void setHasBaseline(boolean z) {
        this.hasBaseline = z;
    }

    public boolean getHasBaseline() {
        return this.hasBaseline;
    }

    public boolean isInPlaceholder() {
        return this.inPlaceholder;
    }

    public void setInPlaceholder(boolean z) {
        this.inPlaceholder = z;
    }

    /* access modifiers changed from: protected */
    public void setInBarrier(int i, boolean z) {
        this.mIsInBarrier[i] = z;
    }

    public void setMeasureRequested(boolean z) {
        this.mMeasureRequested = z;
    }

    public boolean isMeasureRequested() {
        return this.mMeasureRequested && this.mVisibility != 8;
    }

    public int getLastHorizontalMeasureSpec() {
        return this.mLastHorizontalMeasureSpec;
    }

    public int getLastVerticalMeasureSpec() {
        return this.mLastVerticalMeasureSpec;
    }

    public void setLastMeasureSpec(int i, int i2) {
        this.mLastHorizontalMeasureSpec = i;
        this.mLastVerticalMeasureSpec = i2;
        setMeasureRequested(false);
    }

    public void reset() {
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mParent = null;
        this.mCircleConstraintAngle = 0.0f;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.f35mX = 0;
        this.f36mY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        this.mMinWidth = 0;
        this.mMinHeight = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
        this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
        this.mCompanionWidget = null;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mType = null;
        this.mHorizontalWrapVisited = false;
        this.mVerticalWrapVisited = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mHorizontalChainFixedPosition = false;
        this.mVerticalChainFixedPosition = false;
        float[] fArr = this.mWeight;
        fArr[0] = -1.0f;
        fArr[1] = -1.0f;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        int[] iArr = this.mMaxDimension;
        iArr[0] = Integer.MAX_VALUE;
        iArr[1] = Integer.MAX_VALUE;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mMatchConstraintMaxWidth = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mMatchConstraintMaxHeight = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMinHeight = 0;
        this.mResolvedHasRatio = false;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mGroupsToSolver = false;
        boolean[] zArr = this.isTerminalWidget;
        zArr[0] = true;
        zArr[1] = true;
        this.mInVirtuaLayout = false;
        boolean[] zArr2 = this.mIsInBarrier;
        zArr2[0] = false;
        zArr2[1] = false;
        this.mMeasureRequested = true;
    }

    public boolean oppositeDimensionDependsOn(int i) {
        char c = i == 0 ? (char) 1 : 0;
        DimensionBehaviour[] dimensionBehaviourArr = this.mListDimensionBehaviors;
        DimensionBehaviour dimensionBehaviour = dimensionBehaviourArr[i];
        DimensionBehaviour dimensionBehaviour2 = dimensionBehaviourArr[c];
        if (dimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && dimensionBehaviour2 == DimensionBehaviour.MATCH_CONSTRAINT) {
            return true;
        }
        return false;
    }

    public boolean oppositeDimensionsTied() {
        return this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT;
    }

    public ConstraintWidget() {
        this.measured = false;
        this.run = new WidgetRun[2];
        this.horizontalRun = null;
        this.verticalRun = null;
        this.isTerminalWidget = new boolean[]{true, true};
        this.mResolvedHasRatio = false;
        this.mMeasureRequested = true;
        this.OPTIMIZE_WRAP = false;
        this.OPTIMIZE_WRAP_ON_RESOLVED = true;
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mMaxDimension = new int[]{ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED};
        this.mCircleConstraintAngle = 0.0f;
        this.hasBaseline = false;
        this.mInVirtuaLayout = false;
        this.mLastHorizontalMeasureSpec = 0;
        this.mLastVerticalMeasureSpec = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor;
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList<>();
        this.mIsInBarrier = new boolean[2];
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.f35mX = 0;
        this.f36mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        addAnchors();
    }

    public ConstraintWidget(String str) {
        this.measured = false;
        this.run = new WidgetRun[2];
        this.horizontalRun = null;
        this.verticalRun = null;
        this.isTerminalWidget = new boolean[]{true, true};
        this.mResolvedHasRatio = false;
        this.mMeasureRequested = true;
        this.OPTIMIZE_WRAP = false;
        this.OPTIMIZE_WRAP_ON_RESOLVED = true;
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mMaxDimension = new int[]{ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED};
        this.mCircleConstraintAngle = 0.0f;
        this.hasBaseline = false;
        this.mInVirtuaLayout = false;
        this.mLastHorizontalMeasureSpec = 0;
        this.mLastVerticalMeasureSpec = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor;
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList<>();
        this.mIsInBarrier = new boolean[2];
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.f35mX = 0;
        this.f36mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        addAnchors();
        setDebugName(str);
    }

    public ConstraintWidget(int i, int i2, int i3, int i4) {
        this.measured = false;
        this.run = new WidgetRun[2];
        this.horizontalRun = null;
        this.verticalRun = null;
        this.isTerminalWidget = new boolean[]{true, true};
        this.mResolvedHasRatio = false;
        this.mMeasureRequested = true;
        this.OPTIMIZE_WRAP = false;
        this.OPTIMIZE_WRAP_ON_RESOLVED = true;
        this.resolvedHorizontal = false;
        this.resolvedVertical = false;
        this.mHorizontalResolution = -1;
        this.mVerticalResolution = -1;
        this.mMatchConstraintDefaultWidth = 0;
        this.mMatchConstraintDefaultHeight = 0;
        this.mResolvedMatchConstraintDefault = new int[2];
        this.mMatchConstraintMinWidth = 0;
        this.mMatchConstraintMaxWidth = 0;
        this.mMatchConstraintPercentWidth = 1.0f;
        this.mMatchConstraintMinHeight = 0;
        this.mMatchConstraintMaxHeight = 0;
        this.mMatchConstraintPercentHeight = 1.0f;
        this.mResolvedDimensionRatioSide = -1;
        this.mResolvedDimensionRatio = 1.0f;
        this.mMaxDimension = new int[]{ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED};
        this.mCircleConstraintAngle = 0.0f;
        this.hasBaseline = false;
        this.mInVirtuaLayout = false;
        this.mLastHorizontalMeasureSpec = 0;
        this.mLastVerticalMeasureSpec = 0;
        this.mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
        this.mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
        this.mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
        this.mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
        this.mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
        this.mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
        this.mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
        ConstraintAnchor constraintAnchor = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
        this.mCenter = constraintAnchor;
        this.mListAnchors = new ConstraintAnchor[]{this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, constraintAnchor};
        this.mAnchors = new ArrayList<>();
        this.mIsInBarrier = new boolean[2];
        this.mListDimensionBehaviors = new DimensionBehaviour[]{DimensionBehaviour.FIXED, DimensionBehaviour.FIXED};
        this.mParent = null;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mDimensionRatio = 0.0f;
        this.mDimensionRatioSide = -1;
        this.f35mX = 0;
        this.f36mY = 0;
        this.mRelX = 0;
        this.mRelY = 0;
        this.mOffsetX = 0;
        this.mOffsetY = 0;
        this.mBaselineDistance = 0;
        float f = DEFAULT_BIAS;
        this.mHorizontalBiasPercent = f;
        this.mVerticalBiasPercent = f;
        this.mContainerItemSkip = 0;
        this.mVisibility = 0;
        this.mDebugName = null;
        this.mType = null;
        this.mGroupsToSolver = false;
        this.mHorizontalChainStyle = 0;
        this.mVerticalChainStyle = 0;
        this.mWeight = new float[]{-1.0f, -1.0f};
        this.mListNextMatchConstraintsWidget = new ConstraintWidget[]{null, null};
        this.mNextChainWidget = new ConstraintWidget[]{null, null};
        this.mHorizontalNextWidget = null;
        this.mVerticalNextWidget = null;
        this.horizontalGroup = -1;
        this.verticalGroup = -1;
        this.f35mX = i;
        this.f36mY = i2;
        this.mWidth = i3;
        this.mHeight = i4;
        addAnchors();
    }

    public ConstraintWidget(String str, int i, int i2, int i3, int i4) {
        this(i, i2, i3, i4);
        setDebugName(str);
    }

    public ConstraintWidget(int i, int i2) {
        this(0, 0, i, i2);
    }

    public void ensureWidgetRuns() {
        if (this.horizontalRun == null) {
            this.horizontalRun = new HorizontalWidgetRun(this);
        }
        if (this.verticalRun == null) {
            this.verticalRun = new VerticalWidgetRun(this);
        }
    }

    public ConstraintWidget(String str, int i, int i2) {
        this(i, i2);
        setDebugName(str);
    }

    public void resetSolverVariables(Cache cache) {
        this.mLeft.resetSolverVariable(cache);
        this.mTop.resetSolverVariable(cache);
        this.mRight.resetSolverVariable(cache);
        this.mBottom.resetSolverVariable(cache);
        this.mBaseline.resetSolverVariable(cache);
        this.mCenter.resetSolverVariable(cache);
        this.mCenterX.resetSolverVariable(cache);
        this.mCenterY.resetSolverVariable(cache);
    }

    private void addAnchors() {
        this.mAnchors.add(this.mLeft);
        this.mAnchors.add(this.mTop);
        this.mAnchors.add(this.mRight);
        this.mAnchors.add(this.mBottom);
        this.mAnchors.add(this.mCenterX);
        this.mAnchors.add(this.mCenterY);
        this.mAnchors.add(this.mCenter);
        this.mAnchors.add(this.mBaseline);
    }

    public boolean isRoot() {
        return this.mParent == null;
    }

    public ConstraintWidget getParent() {
        return this.mParent;
    }

    public void setParent(ConstraintWidget constraintWidget) {
        this.mParent = constraintWidget;
    }

    public void setWidthWrapContent(boolean z) {
        this.mIsWidthWrapContent = z;
    }

    public boolean isWidthWrapContent() {
        return this.mIsWidthWrapContent;
    }

    public void setHeightWrapContent(boolean z) {
        this.mIsHeightWrapContent = z;
    }

    public boolean isHeightWrapContent() {
        return this.mIsHeightWrapContent;
    }

    public void connectCircularConstraint(ConstraintWidget constraintWidget, float f, int i) {
        immediateConnect(ConstraintAnchor.Type.CENTER, constraintWidget, ConstraintAnchor.Type.CENTER, i, 0);
        this.mCircleConstraintAngle = f;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String str) {
        this.mType = str;
    }

    public void setVisibility(int i) {
        this.mVisibility = i;
    }

    public int getVisibility() {
        return this.mVisibility;
    }

    public String getDebugName() {
        return this.mDebugName;
    }

    public void setDebugName(String str) {
        this.mDebugName = str;
    }

    public void setDebugSolverName(LinearSystem linearSystem, String str) {
        this.mDebugName = str;
        SolverVariable createObjectVariable = linearSystem.createObjectVariable(this.mLeft);
        SolverVariable createObjectVariable2 = linearSystem.createObjectVariable(this.mTop);
        SolverVariable createObjectVariable3 = linearSystem.createObjectVariable(this.mRight);
        SolverVariable createObjectVariable4 = linearSystem.createObjectVariable(this.mBottom);
        createObjectVariable.setName(str + ".left");
        createObjectVariable2.setName(str + ".top");
        createObjectVariable3.setName(str + ".right");
        createObjectVariable4.setName(str + ".bottom");
        SolverVariable createObjectVariable5 = linearSystem.createObjectVariable(this.mBaseline);
        createObjectVariable5.setName(str + ".baseline");
    }

    public void createObjectVariables(LinearSystem linearSystem) {
        linearSystem.createObjectVariable(this.mLeft);
        linearSystem.createObjectVariable(this.mTop);
        linearSystem.createObjectVariable(this.mRight);
        linearSystem.createObjectVariable(this.mBottom);
        if (this.mBaselineDistance > 0) {
            linearSystem.createObjectVariable(this.mBaseline);
        }
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        if (this.mType != null) {
            str = "type: " + this.mType + " ";
        } else {
            str = str2;
        }
        sb.append(str);
        if (this.mDebugName != null) {
            str2 = "id: " + this.mDebugName + " ";
        }
        sb.append(str2);
        sb.append("(");
        sb.append(this.f35mX);
        sb.append(", ");
        sb.append(this.f36mY);
        sb.append(") - (");
        sb.append(this.mWidth);
        sb.append(" x ");
        sb.append(this.mHeight);
        sb.append(")");
        return sb.toString();
    }

    public int getX() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer)) {
            return this.f35mX;
        }
        return ((ConstraintWidgetContainer) constraintWidget).mPaddingLeft + this.f35mX;
    }

    public int getY() {
        ConstraintWidget constraintWidget = this.mParent;
        if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer)) {
            return this.f36mY;
        }
        return ((ConstraintWidgetContainer) constraintWidget).mPaddingTop + this.f36mY;
    }

    public int getWidth() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mWidth;
    }

    public int getOptimizerWrapWidth() {
        int i;
        int i2 = this.mWidth;
        if (this.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT) {
            return i2;
        }
        if (this.mMatchConstraintDefaultWidth == 1) {
            i = Math.max(this.mMatchConstraintMinWidth, i2);
        } else {
            i = this.mMatchConstraintMinWidth;
            if (i > 0) {
                this.mWidth = i;
            } else {
                i = 0;
            }
        }
        int i3 = this.mMatchConstraintMaxWidth;
        return (i3 <= 0 || i3 >= i) ? i : i3;
    }

    public int getOptimizerWrapHeight() {
        int i;
        int i2 = this.mHeight;
        if (this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT) {
            return i2;
        }
        if (this.mMatchConstraintDefaultHeight == 1) {
            i = Math.max(this.mMatchConstraintMinHeight, i2);
        } else {
            i = this.mMatchConstraintMinHeight;
            if (i > 0) {
                this.mHeight = i;
            } else {
                i = 0;
            }
        }
        int i3 = this.mMatchConstraintMaxHeight;
        return (i3 <= 0 || i3 >= i) ? i : i3;
    }

    public int getHeight() {
        if (this.mVisibility == 8) {
            return 0;
        }
        return this.mHeight;
    }

    public int getLength(int i) {
        if (i == 0) {
            return getWidth();
        }
        if (i == 1) {
            return getHeight();
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public int getRootX() {
        return this.f35mX + this.mOffsetX;
    }

    /* access modifiers changed from: protected */
    public int getRootY() {
        return this.f36mY + this.mOffsetY;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getLeft() {
        return getX();
    }

    public int getTop() {
        return getY();
    }

    public int getRight() {
        return getX() + this.mWidth;
    }

    public int getBottom() {
        return getY() + this.mHeight;
    }

    public int getHorizontalMargin() {
        ConstraintAnchor constraintAnchor = this.mLeft;
        int i = 0;
        if (constraintAnchor != null) {
            i = 0 + constraintAnchor.mMargin;
        }
        ConstraintAnchor constraintAnchor2 = this.mRight;
        return constraintAnchor2 != null ? i + constraintAnchor2.mMargin : i;
    }

    public int getVerticalMargin() {
        int i = 0;
        if (this.mLeft != null) {
            i = 0 + this.mTop.mMargin;
        }
        return this.mRight != null ? i + this.mBottom.mMargin : i;
    }

    public float getHorizontalBiasPercent() {
        return this.mHorizontalBiasPercent;
    }

    public float getVerticalBiasPercent() {
        return this.mVerticalBiasPercent;
    }

    public float getBiasPercent(int i) {
        if (i == 0) {
            return this.mHorizontalBiasPercent;
        }
        if (i == 1) {
            return this.mVerticalBiasPercent;
        }
        return -1.0f;
    }

    public boolean hasBaseline() {
        return this.hasBaseline;
    }

    public int getBaselineDistance() {
        return this.mBaselineDistance;
    }

    public Object getCompanionWidget() {
        return this.mCompanionWidget;
    }

    public ArrayList<ConstraintAnchor> getAnchors() {
        return this.mAnchors;
    }

    public void setX(int i) {
        this.f35mX = i;
    }

    public void setY(int i) {
        this.f36mY = i;
    }

    public void setOrigin(int i, int i2) {
        this.f35mX = i;
        this.f36mY = i2;
    }

    public void setOffset(int i, int i2) {
        this.mOffsetX = i;
        this.mOffsetY = i2;
    }

    public void setGoneMargin(ConstraintAnchor.Type type, int i) {
        int i2 = C01301.f37x4c44d048[type.ordinal()];
        if (i2 == 1) {
            this.mLeft.mGoneMargin = i;
        } else if (i2 == 2) {
            this.mTop.mGoneMargin = i;
        } else if (i2 == 3) {
            this.mRight.mGoneMargin = i;
        } else if (i2 == 4) {
            this.mBottom.mGoneMargin = i;
        }
    }

    public void setWidth(int i) {
        this.mWidth = i;
        int i2 = this.mMinWidth;
        if (i < i2) {
            this.mWidth = i2;
        }
    }

    public void setHeight(int i) {
        this.mHeight = i;
        int i2 = this.mMinHeight;
        if (i < i2) {
            this.mHeight = i2;
        }
    }

    public void setLength(int i, int i2) {
        if (i2 == 0) {
            setWidth(i);
        } else if (i2 == 1) {
            setHeight(i);
        }
    }

    public void setHorizontalMatchStyle(int i, int i2, int i3, float f) {
        this.mMatchConstraintDefaultWidth = i;
        this.mMatchConstraintMinWidth = i2;
        if (i3 == Integer.MAX_VALUE) {
            i3 = 0;
        }
        this.mMatchConstraintMaxWidth = i3;
        this.mMatchConstraintPercentWidth = f;
        if (f > 0.0f && f < 1.0f && i == 0) {
            this.mMatchConstraintDefaultWidth = 2;
        }
    }

    public void setVerticalMatchStyle(int i, int i2, int i3, float f) {
        this.mMatchConstraintDefaultHeight = i;
        this.mMatchConstraintMinHeight = i2;
        if (i3 == Integer.MAX_VALUE) {
            i3 = 0;
        }
        this.mMatchConstraintMaxHeight = i3;
        this.mMatchConstraintPercentHeight = f;
        if (f > 0.0f && f < 1.0f && i == 0) {
            this.mMatchConstraintDefaultHeight = 2;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setDimensionRatio(java.lang.String r9) {
        /*
            r8 = this;
            r0 = 0
            if (r9 == 0) goto L_0x008e
            int r1 = r9.length()
            if (r1 != 0) goto L_0x000b
            goto L_0x008e
        L_0x000b:
            r1 = -1
            int r2 = r9.length()
            r3 = 44
            int r3 = r9.indexOf(r3)
            r4 = 0
            r5 = 1
            if (r3 <= 0) goto L_0x0037
            int r6 = r2 + -1
            if (r3 >= r6) goto L_0x0037
            java.lang.String r6 = r9.substring(r4, r3)
            java.lang.String r7 = "W"
            boolean r7 = r6.equalsIgnoreCase(r7)
            if (r7 == 0) goto L_0x002c
            r1 = r4
            goto L_0x0035
        L_0x002c:
            java.lang.String r4 = "H"
            boolean r4 = r6.equalsIgnoreCase(r4)
            if (r4 == 0) goto L_0x0035
            r1 = r5
        L_0x0035:
            int r4 = r3 + 1
        L_0x0037:
            r3 = 58
            int r3 = r9.indexOf(r3)
            if (r3 < 0) goto L_0x0075
            int r2 = r2 - r5
            if (r3 >= r2) goto L_0x0075
            java.lang.String r2 = r9.substring(r4, r3)
            int r3 = r3 + r5
            java.lang.String r9 = r9.substring(r3)
            int r3 = r2.length()
            if (r3 <= 0) goto L_0x0084
            int r3 = r9.length()
            if (r3 <= 0) goto L_0x0084
            float r2 = java.lang.Float.parseFloat(r2)     // Catch:{ NumberFormatException -> 0x0084 }
            float r9 = java.lang.Float.parseFloat(r9)     // Catch:{ NumberFormatException -> 0x0084 }
            int r3 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r3 <= 0) goto L_0x0084
            int r3 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r3 <= 0) goto L_0x0084
            if (r1 != r5) goto L_0x006f
            float r9 = r9 / r2
            float r9 = java.lang.Math.abs(r9)     // Catch:{ NumberFormatException -> 0x0084 }
            goto L_0x0085
        L_0x006f:
            float r2 = r2 / r9
            float r9 = java.lang.Math.abs(r2)     // Catch:{ NumberFormatException -> 0x0084 }
            goto L_0x0085
        L_0x0075:
            java.lang.String r9 = r9.substring(r4)
            int r2 = r9.length()
            if (r2 <= 0) goto L_0x0084
            float r9 = java.lang.Float.parseFloat(r9)     // Catch:{ NumberFormatException -> 0x0084 }
            goto L_0x0085
        L_0x0084:
            r9 = r0
        L_0x0085:
            int r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r0 <= 0) goto L_0x008d
            r8.mDimensionRatio = r9
            r8.mDimensionRatioSide = r1
        L_0x008d:
            return
        L_0x008e:
            r8.mDimensionRatio = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.setDimensionRatio(java.lang.String):void");
    }

    public void setDimensionRatio(float f, int i) {
        this.mDimensionRatio = f;
        this.mDimensionRatioSide = i;
    }

    public float getDimensionRatio() {
        return this.mDimensionRatio;
    }

    public int getDimensionRatioSide() {
        return this.mDimensionRatioSide;
    }

    public void setHorizontalBiasPercent(float f) {
        this.mHorizontalBiasPercent = f;
    }

    public void setVerticalBiasPercent(float f) {
        this.mVerticalBiasPercent = f;
    }

    public void setMinWidth(int i) {
        if (i < 0) {
            this.mMinWidth = 0;
        } else {
            this.mMinWidth = i;
        }
    }

    public void setMinHeight(int i) {
        if (i < 0) {
            this.mMinHeight = 0;
        } else {
            this.mMinHeight = i;
        }
    }

    public void setDimension(int i, int i2) {
        this.mWidth = i;
        int i3 = this.mMinWidth;
        if (i < i3) {
            this.mWidth = i3;
        }
        this.mHeight = i2;
        int i4 = this.mMinHeight;
        if (i2 < i4) {
            this.mHeight = i4;
        }
    }

    public void setFrame(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7 = i3 - i;
        int i8 = i4 - i2;
        this.f35mX = i;
        this.f36mY = i2;
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && i7 < (i6 = this.mWidth)) {
            i7 = i6;
        }
        if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && i8 < (i5 = this.mHeight)) {
            i8 = i5;
        }
        this.mWidth = i7;
        this.mHeight = i8;
        int i9 = this.mMinHeight;
        if (i8 < i9) {
            this.mHeight = i9;
        }
        int i10 = this.mMinWidth;
        if (i7 < i10) {
            this.mWidth = i10;
        }
    }

    public void setFrame(int i, int i2, int i3) {
        if (i3 == 0) {
            setHorizontalDimension(i, i2);
        } else if (i3 == 1) {
            setVerticalDimension(i, i2);
        }
    }

    public void setHorizontalDimension(int i, int i2) {
        this.f35mX = i;
        int i3 = i2 - i;
        this.mWidth = i3;
        int i4 = this.mMinWidth;
        if (i3 < i4) {
            this.mWidth = i4;
        }
    }

    public void setVerticalDimension(int i, int i2) {
        this.f36mY = i;
        int i3 = i2 - i;
        this.mHeight = i3;
        int i4 = this.mMinHeight;
        if (i3 < i4) {
            this.mHeight = i4;
        }
    }

    /* access modifiers changed from: package-private */
    public int getRelativePositioning(int i) {
        if (i == 0) {
            return this.mRelX;
        }
        if (i == 1) {
            return this.mRelY;
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public void setRelativePositioning(int i, int i2) {
        if (i2 == 0) {
            this.mRelX = i;
        } else if (i2 == 1) {
            this.mRelY = i;
        }
    }

    public void setBaselineDistance(int i) {
        this.mBaselineDistance = i;
        this.hasBaseline = i > 0;
    }

    public void setCompanionWidget(Object obj) {
        this.mCompanionWidget = obj;
    }

    public void setContainerItemSkip(int i) {
        if (i >= 0) {
            this.mContainerItemSkip = i;
        } else {
            this.mContainerItemSkip = 0;
        }
    }

    public int getContainerItemSkip() {
        return this.mContainerItemSkip;
    }

    public void setHorizontalWeight(float f) {
        this.mWeight[0] = f;
    }

    public void setVerticalWeight(float f) {
        this.mWeight[1] = f;
    }

    public void setHorizontalChainStyle(int i) {
        this.mHorizontalChainStyle = i;
    }

    public int getHorizontalChainStyle() {
        return this.mHorizontalChainStyle;
    }

    public void setVerticalChainStyle(int i) {
        this.mVerticalChainStyle = i;
    }

    public int getVerticalChainStyle() {
        return this.mVerticalChainStyle;
    }

    public boolean allowedInBarrier() {
        return this.mVisibility != 8;
    }

    public void immediateConnect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int i, int i2) {
        getAnchor(type).connect(constraintWidget.getAnchor(type2), i, i2, true);
    }

    public void connect(ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i) {
        if (constraintAnchor.getOwner() == this) {
            connect(constraintAnchor.getType(), constraintAnchor2.getOwner(), constraintAnchor2.getType(), i);
        }
    }

    public void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2) {
        connect(type, constraintWidget, type2, 0);
    }

    public void connect(ConstraintAnchor.Type type, ConstraintWidget constraintWidget, ConstraintAnchor.Type type2, int i) {
        boolean z;
        if (type == ConstraintAnchor.Type.CENTER) {
            if (type2 == ConstraintAnchor.Type.CENTER) {
                ConstraintAnchor anchor = getAnchor(ConstraintAnchor.Type.LEFT);
                ConstraintAnchor anchor2 = getAnchor(ConstraintAnchor.Type.RIGHT);
                ConstraintAnchor anchor3 = getAnchor(ConstraintAnchor.Type.TOP);
                ConstraintAnchor anchor4 = getAnchor(ConstraintAnchor.Type.BOTTOM);
                boolean z2 = true;
                if ((anchor == null || !anchor.isConnected()) && (anchor2 == null || !anchor2.isConnected())) {
                    connect(ConstraintAnchor.Type.LEFT, constraintWidget, ConstraintAnchor.Type.LEFT, 0);
                    connect(ConstraintAnchor.Type.RIGHT, constraintWidget, ConstraintAnchor.Type.RIGHT, 0);
                    z = true;
                } else {
                    z = false;
                }
                if ((anchor3 == null || !anchor3.isConnected()) && (anchor4 == null || !anchor4.isConnected())) {
                    connect(ConstraintAnchor.Type.TOP, constraintWidget, ConstraintAnchor.Type.TOP, 0);
                    connect(ConstraintAnchor.Type.BOTTOM, constraintWidget, ConstraintAnchor.Type.BOTTOM, 0);
                } else {
                    z2 = false;
                }
                if (z && z2) {
                    getAnchor(ConstraintAnchor.Type.CENTER).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.CENTER), 0);
                } else if (z) {
                    getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_X), 0);
                } else if (z2) {
                    getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_Y), 0);
                }
            } else if (type2 == ConstraintAnchor.Type.LEFT || type2 == ConstraintAnchor.Type.RIGHT) {
                connect(ConstraintAnchor.Type.LEFT, constraintWidget, type2, 0);
                connect(ConstraintAnchor.Type.RIGHT, constraintWidget, type2, 0);
                getAnchor(ConstraintAnchor.Type.CENTER).connect(constraintWidget.getAnchor(type2), 0);
            } else if (type2 == ConstraintAnchor.Type.TOP || type2 == ConstraintAnchor.Type.BOTTOM) {
                connect(ConstraintAnchor.Type.TOP, constraintWidget, type2, 0);
                connect(ConstraintAnchor.Type.BOTTOM, constraintWidget, type2, 0);
                getAnchor(ConstraintAnchor.Type.CENTER).connect(constraintWidget.getAnchor(type2), 0);
            }
        } else if (type == ConstraintAnchor.Type.CENTER_X && (type2 == ConstraintAnchor.Type.LEFT || type2 == ConstraintAnchor.Type.RIGHT)) {
            ConstraintAnchor anchor5 = getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor anchor6 = constraintWidget.getAnchor(type2);
            ConstraintAnchor anchor7 = getAnchor(ConstraintAnchor.Type.RIGHT);
            anchor5.connect(anchor6, 0);
            anchor7.connect(anchor6, 0);
            getAnchor(ConstraintAnchor.Type.CENTER_X).connect(anchor6, 0);
        } else if (type == ConstraintAnchor.Type.CENTER_Y && (type2 == ConstraintAnchor.Type.TOP || type2 == ConstraintAnchor.Type.BOTTOM)) {
            ConstraintAnchor anchor8 = constraintWidget.getAnchor(type2);
            getAnchor(ConstraintAnchor.Type.TOP).connect(anchor8, 0);
            getAnchor(ConstraintAnchor.Type.BOTTOM).connect(anchor8, 0);
            getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(anchor8, 0);
        } else if (type == ConstraintAnchor.Type.CENTER_X && type2 == ConstraintAnchor.Type.CENTER_X) {
            getAnchor(ConstraintAnchor.Type.LEFT).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT), 0);
            getAnchor(ConstraintAnchor.Type.RIGHT).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT), 0);
            getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintWidget.getAnchor(type2), 0);
        } else if (type == ConstraintAnchor.Type.CENTER_Y && type2 == ConstraintAnchor.Type.CENTER_Y) {
            getAnchor(ConstraintAnchor.Type.TOP).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.TOP), 0);
            getAnchor(ConstraintAnchor.Type.BOTTOM).connect(constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM), 0);
            getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintWidget.getAnchor(type2), 0);
        } else {
            ConstraintAnchor anchor9 = getAnchor(type);
            ConstraintAnchor anchor10 = constraintWidget.getAnchor(type2);
            if (anchor9.isValidConnection(anchor10)) {
                if (type == ConstraintAnchor.Type.BASELINE) {
                    ConstraintAnchor anchor11 = getAnchor(ConstraintAnchor.Type.TOP);
                    ConstraintAnchor anchor12 = getAnchor(ConstraintAnchor.Type.BOTTOM);
                    if (anchor11 != null) {
                        anchor11.reset();
                    }
                    if (anchor12 != null) {
                        anchor12.reset();
                    }
                    i = 0;
                } else if (type == ConstraintAnchor.Type.TOP || type == ConstraintAnchor.Type.BOTTOM) {
                    ConstraintAnchor anchor13 = getAnchor(ConstraintAnchor.Type.BASELINE);
                    if (anchor13 != null) {
                        anchor13.reset();
                    }
                    ConstraintAnchor anchor14 = getAnchor(ConstraintAnchor.Type.CENTER);
                    if (anchor14.getTarget() != anchor10) {
                        anchor14.reset();
                    }
                    ConstraintAnchor opposite = getAnchor(type).getOpposite();
                    ConstraintAnchor anchor15 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
                    if (anchor15.isConnected()) {
                        opposite.reset();
                        anchor15.reset();
                    }
                } else if (type == ConstraintAnchor.Type.LEFT || type == ConstraintAnchor.Type.RIGHT) {
                    ConstraintAnchor anchor16 = getAnchor(ConstraintAnchor.Type.CENTER);
                    if (anchor16.getTarget() != anchor10) {
                        anchor16.reset();
                    }
                    ConstraintAnchor opposite2 = getAnchor(type).getOpposite();
                    ConstraintAnchor anchor17 = getAnchor(ConstraintAnchor.Type.CENTER_X);
                    if (anchor17.isConnected()) {
                        opposite2.reset();
                        anchor17.reset();
                    }
                }
                anchor9.connect(anchor10, i);
            }
        }
    }

    public void resetAllConstraints() {
        resetAnchors();
        setVerticalBiasPercent(DEFAULT_BIAS);
        setHorizontalBiasPercent(DEFAULT_BIAS);
    }

    public void resetAnchor(ConstraintAnchor constraintAnchor) {
        if (getParent() == null || !(getParent() instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            ConstraintAnchor anchor = getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor anchor2 = getAnchor(ConstraintAnchor.Type.RIGHT);
            ConstraintAnchor anchor3 = getAnchor(ConstraintAnchor.Type.TOP);
            ConstraintAnchor anchor4 = getAnchor(ConstraintAnchor.Type.BOTTOM);
            ConstraintAnchor anchor5 = getAnchor(ConstraintAnchor.Type.CENTER);
            ConstraintAnchor anchor6 = getAnchor(ConstraintAnchor.Type.CENTER_X);
            ConstraintAnchor anchor7 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
            if (constraintAnchor == anchor5) {
                if (anchor.isConnected() && anchor2.isConnected() && anchor.getTarget() == anchor2.getTarget()) {
                    anchor.reset();
                    anchor2.reset();
                }
                if (anchor3.isConnected() && anchor4.isConnected() && anchor3.getTarget() == anchor4.getTarget()) {
                    anchor3.reset();
                    anchor4.reset();
                }
                this.mHorizontalBiasPercent = 0.5f;
                this.mVerticalBiasPercent = 0.5f;
            } else if (constraintAnchor == anchor6) {
                if (anchor.isConnected() && anchor2.isConnected() && anchor.getTarget().getOwner() == anchor2.getTarget().getOwner()) {
                    anchor.reset();
                    anchor2.reset();
                }
                this.mHorizontalBiasPercent = 0.5f;
            } else if (constraintAnchor == anchor7) {
                if (anchor3.isConnected() && anchor4.isConnected() && anchor3.getTarget().getOwner() == anchor4.getTarget().getOwner()) {
                    anchor3.reset();
                    anchor4.reset();
                }
                this.mVerticalBiasPercent = 0.5f;
            } else if (constraintAnchor == anchor || constraintAnchor == anchor2) {
                if (anchor.isConnected() && anchor.getTarget() == anchor2.getTarget()) {
                    anchor5.reset();
                }
            } else if ((constraintAnchor == anchor3 || constraintAnchor == anchor4) && anchor3.isConnected() && anchor3.getTarget() == anchor4.getTarget()) {
                anchor5.reset();
            }
            constraintAnchor.reset();
        }
    }

    public void resetAnchors() {
        ConstraintWidget parent = getParent();
        if (parent == null || !(parent instanceof ConstraintWidgetContainer) || !((ConstraintWidgetContainer) getParent()).handlesInternalConstraints()) {
            int size = this.mAnchors.size();
            for (int i = 0; i < size; i++) {
                this.mAnchors.get(i).reset();
            }
        }
    }

    public ConstraintAnchor getAnchor(ConstraintAnchor.Type type) {
        switch (C01301.f37x4c44d048[type.ordinal()]) {
            case 1:
                return this.mLeft;
            case 2:
                return this.mTop;
            case 3:
                return this.mRight;
            case 4:
                return this.mBottom;
            case 5:
                return this.mBaseline;
            case 6:
                return this.mCenter;
            case 7:
                return this.mCenterX;
            case 8:
                return this.mCenterY;
            case 9:
                return null;
            default:
                throw new AssertionError(type.name());
        }
    }

    public DimensionBehaviour getHorizontalDimensionBehaviour() {
        return this.mListDimensionBehaviors[0];
    }

    public DimensionBehaviour getVerticalDimensionBehaviour() {
        return this.mListDimensionBehaviors[1];
    }

    public DimensionBehaviour getDimensionBehaviour(int i) {
        if (i == 0) {
            return getHorizontalDimensionBehaviour();
        }
        if (i == 1) {
            return getVerticalDimensionBehaviour();
        }
        return null;
    }

    public void setHorizontalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[0] = dimensionBehaviour;
    }

    public void setVerticalDimensionBehaviour(DimensionBehaviour dimensionBehaviour) {
        this.mListDimensionBehaviors[1] = dimensionBehaviour;
    }

    public boolean isInHorizontalChain() {
        if (this.mLeft.mTarget == null || this.mLeft.mTarget.mTarget != this.mLeft) {
            return this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight;
        }
        return true;
    }

    public ConstraintWidget getPreviousChainMember(int i) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i == 0) {
            if (this.mLeft.mTarget == null || this.mLeft.mTarget.mTarget != (constraintAnchor2 = this.mLeft)) {
                return null;
            }
            return constraintAnchor2.mTarget.mOwner;
        } else if (i == 1 && this.mTop.mTarget != null && this.mTop.mTarget.mTarget == (constraintAnchor = this.mTop)) {
            return constraintAnchor.mTarget.mOwner;
        } else {
            return null;
        }
    }

    public ConstraintWidget getNextChainMember(int i) {
        ConstraintAnchor constraintAnchor;
        ConstraintAnchor constraintAnchor2;
        if (i == 0) {
            if (this.mRight.mTarget == null || this.mRight.mTarget.mTarget != (constraintAnchor2 = this.mRight)) {
                return null;
            }
            return constraintAnchor2.mTarget.mOwner;
        } else if (i == 1 && this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == (constraintAnchor = this.mBottom)) {
            return constraintAnchor.mTarget.mOwner;
        } else {
            return null;
        }
    }

    public ConstraintWidget getHorizontalChainControlWidget() {
        ConstraintAnchor constraintAnchor;
        ConstraintWidget constraintWidget;
        ConstraintAnchor constraintAnchor2;
        if (!isInHorizontalChain()) {
            return null;
        }
        ConstraintWidget constraintWidget2 = this;
        ConstraintWidget constraintWidget3 = null;
        while (constraintWidget3 == null && constraintWidget2 != null) {
            ConstraintAnchor anchor = constraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT);
            if (anchor == null) {
                constraintAnchor = null;
            } else {
                constraintAnchor = anchor.getTarget();
            }
            if (constraintAnchor == null) {
                constraintWidget = null;
            } else {
                constraintWidget = constraintAnchor.getOwner();
            }
            if (constraintWidget == getParent()) {
                return constraintWidget2;
            }
            if (constraintWidget == null) {
                constraintAnchor2 = null;
            } else {
                constraintAnchor2 = constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
            }
            if (constraintAnchor2 == null || constraintAnchor2.getOwner() == constraintWidget2) {
                constraintWidget2 = constraintWidget;
            } else {
                constraintWidget3 = constraintWidget2;
            }
        }
        return constraintWidget3;
    }

    public boolean isInVerticalChain() {
        if (this.mTop.mTarget == null || this.mTop.mTarget.mTarget != this.mTop) {
            return this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom;
        }
        return true;
    }

    public ConstraintWidget getVerticalChainControlWidget() {
        ConstraintAnchor constraintAnchor;
        ConstraintWidget constraintWidget;
        ConstraintAnchor constraintAnchor2;
        if (!isInVerticalChain()) {
            return null;
        }
        ConstraintWidget constraintWidget2 = this;
        ConstraintWidget constraintWidget3 = null;
        while (constraintWidget3 == null && constraintWidget2 != null) {
            ConstraintAnchor anchor = constraintWidget2.getAnchor(ConstraintAnchor.Type.TOP);
            if (anchor == null) {
                constraintAnchor = null;
            } else {
                constraintAnchor = anchor.getTarget();
            }
            if (constraintAnchor == null) {
                constraintWidget = null;
            } else {
                constraintWidget = constraintAnchor.getOwner();
            }
            if (constraintWidget == getParent()) {
                return constraintWidget2;
            }
            if (constraintWidget == null) {
                constraintAnchor2 = null;
            } else {
                constraintAnchor2 = constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
            }
            if (constraintAnchor2 == null || constraintAnchor2.getOwner() == constraintWidget2) {
                constraintWidget2 = constraintWidget;
            } else {
                constraintWidget3 = constraintWidget2;
            }
        }
        return constraintWidget3;
    }

    private boolean isChainHead(int i) {
        int i2 = i * 2;
        if (this.mListAnchors[i2].mTarget != null) {
            ConstraintAnchor constraintAnchor = this.mListAnchors[i2].mTarget.mTarget;
            ConstraintAnchor[] constraintAnchorArr = this.mListAnchors;
            if (constraintAnchor != constraintAnchorArr[i2]) {
                int i3 = i2 + 1;
                return constraintAnchorArr[i3].mTarget != null && this.mListAnchors[i3].mTarget.mTarget == this.mListAnchors[i3];
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v1, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v1, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v2, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v2, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v3, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v11, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v4, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v5, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v6, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v7, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v6, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v8, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v9, resolved type: int} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x030e  */
    /* JADX WARNING: Removed duplicated region for block: B:186:0x0318  */
    /* JADX WARNING: Removed duplicated region for block: B:189:0x031d  */
    /* JADX WARNING: Removed duplicated region for block: B:198:0x0336  */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x0339  */
    /* JADX WARNING: Removed duplicated region for block: B:201:0x033d  */
    /* JADX WARNING: Removed duplicated region for block: B:202:0x0340  */
    /* JADX WARNING: Removed duplicated region for block: B:214:0x0372  */
    /* JADX WARNING: Removed duplicated region for block: B:223:0x03a8  */
    /* JADX WARNING: Removed duplicated region for block: B:238:0x045b  */
    /* JADX WARNING: Removed duplicated region for block: B:255:0x04c5  */
    /* JADX WARNING: Removed duplicated region for block: B:259:0x04d9  */
    /* JADX WARNING: Removed duplicated region for block: B:260:0x04db  */
    /* JADX WARNING: Removed duplicated region for block: B:264:0x04e2  */
    /* JADX WARNING: Removed duplicated region for block: B:297:0x056f  */
    /* JADX WARNING: Removed duplicated region for block: B:298:0x0572  */
    /* JADX WARNING: Removed duplicated region for block: B:300:0x05b2  */
    /* JADX WARNING: Removed duplicated region for block: B:302:0x05b8  */
    /* JADX WARNING: Removed duplicated region for block: B:306:0x05e3  */
    /* JADX WARNING: Removed duplicated region for block: B:309:0x05ed  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addToSolver(androidx.constraintlayout.solver.LinearSystem r51, boolean r52) {
        /*
            r50 = this;
            r15 = r50
            r14 = r51
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mLeft
            androidx.constraintlayout.solver.SolverVariable r13 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mRight
            androidx.constraintlayout.solver.SolverVariable r12 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mTop
            androidx.constraintlayout.solver.SolverVariable r11 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBottom
            androidx.constraintlayout.solver.SolverVariable r10 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBaseline
            androidx.constraintlayout.solver.SolverVariable r9 = r14.createObjectVariable(r0)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            r8 = 1
            r7 = 0
            if (r0 == 0) goto L_0x0047
            if (r0 == 0) goto L_0x0034
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r0.mListDimensionBehaviors
            r0 = r0[r7]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r0 != r1) goto L_0x0034
            r0 = r8
            goto L_0x0035
        L_0x0034:
            r0 = r7
        L_0x0035:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r15.mParent
            if (r1 == 0) goto L_0x0043
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r1.mListDimensionBehaviors
            r1 = r1[r8]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r1 != r2) goto L_0x0043
            r1 = r8
            goto L_0x0044
        L_0x0043:
            r1 = r7
        L_0x0044:
            r5 = r0
            r6 = r1
            goto L_0x0049
        L_0x0047:
            r5 = r7
            r6 = r5
        L_0x0049:
            int r0 = r15.mVisibility
            r4 = 8
            if (r0 != r4) goto L_0x0060
            boolean r0 = r50.hasDependencies()
            if (r0 != 0) goto L_0x0060
            boolean[] r0 = r15.mIsInBarrier
            boolean r1 = r0[r7]
            if (r1 != 0) goto L_0x0060
            boolean r0 = r0[r8]
            if (r0 != 0) goto L_0x0060
            return
        L_0x0060:
            boolean r0 = r15.resolvedHorizontal
            r3 = 5
            if (r0 != 0) goto L_0x0069
            boolean r1 = r15.resolvedVertical
            if (r1 == 0) goto L_0x00e6
        L_0x0069:
            if (r0 == 0) goto L_0x0098
            int r0 = r15.f35mX
            r14.addEquality(r13, r0)
            int r0 = r15.f35mX
            int r1 = r15.mWidth
            int r0 = r0 + r1
            r14.addEquality(r12, r0)
            if (r5 == 0) goto L_0x0098
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x0098
            boolean r1 = r15.OPTIMIZE_WRAP_ON_RESOLVED
            if (r1 == 0) goto L_0x008f
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r0 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r0
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r15.mLeft
            r0.addVerticalWrapMinVariable(r1)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r15.mRight
            r0.addHorizontalWrapMaxVariable(r1)
            goto L_0x0098
        L_0x008f:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r14.addGreaterThan(r0, r12, r7, r3)
        L_0x0098:
            boolean r0 = r15.resolvedVertical
            if (r0 == 0) goto L_0x00d9
            int r0 = r15.f36mY
            r14.addEquality(r11, r0)
            int r0 = r15.f36mY
            int r1 = r15.mHeight
            int r0 = r0 + r1
            r14.addEquality(r10, r0)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBaseline
            boolean r0 = r0.hasDependents()
            if (r0 == 0) goto L_0x00b9
            int r0 = r15.f36mY
            int r1 = r15.mBaselineDistance
            int r0 = r0 + r1
            r14.addEquality(r9, r0)
        L_0x00b9:
            if (r6 == 0) goto L_0x00d9
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x00d9
            boolean r1 = r15.OPTIMIZE_WRAP_ON_RESOLVED
            if (r1 == 0) goto L_0x00d0
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r0 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r0
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r15.mTop
            r0.addVerticalWrapMinVariable(r1)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r15.mBottom
            r0.addVerticalWrapMaxVariable(r1)
            goto L_0x00d9
        L_0x00d0:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r14.addGreaterThan(r0, r10, r7, r3)
        L_0x00d9:
            boolean r0 = r15.resolvedHorizontal
            if (r0 == 0) goto L_0x00e6
            boolean r0 = r15.resolvedVertical
            if (r0 == 0) goto L_0x00e6
            r15.resolvedHorizontal = r7
            r15.resolvedVertical = r7
            return
        L_0x00e6:
            androidx.constraintlayout.solver.Metrics r0 = androidx.constraintlayout.solver.LinearSystem.sMetrics
            r1 = 1
            if (r0 == 0) goto L_0x00f3
            androidx.constraintlayout.solver.Metrics r0 = androidx.constraintlayout.solver.LinearSystem.sMetrics
            long r3 = r0.widgets
            long r3 = r3 + r1
            r0.widgets = r3
        L_0x00f3:
            if (r52 == 0) goto L_0x0192
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            if (r0 == 0) goto L_0x0192
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r3 = r15.verticalRun
            if (r3 == 0) goto L_0x0192
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x0192
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x0192
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x0192
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x0192
            androidx.constraintlayout.solver.Metrics r0 = androidx.constraintlayout.solver.LinearSystem.sMetrics
            if (r0 == 0) goto L_0x0126
            androidx.constraintlayout.solver.Metrics r0 = androidx.constraintlayout.solver.LinearSystem.sMetrics
            long r3 = r0.graphSolved
            long r3 = r3 + r1
            r0.graphSolved = r3
        L_0x0126:
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            int r0 = r0.value
            r14.addEquality(r13, r0)
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r14.addEquality(r12, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            int r0 = r0.value
            r14.addEquality(r11, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r14.addEquality(r10, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.baseline
            int r0 = r0.value
            r14.addEquality(r9, r0)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x018d
            if (r5 == 0) goto L_0x0172
            boolean[] r0 = r15.isTerminalWidget
            boolean r0 = r0[r7]
            if (r0 == 0) goto L_0x0172
            boolean r0 = r50.isInHorizontalChain()
            if (r0 != 0) goto L_0x0172
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r1 = 8
            r14.addGreaterThan(r0, r12, r7, r1)
        L_0x0172:
            if (r6 == 0) goto L_0x018d
            boolean[] r0 = r15.isTerminalWidget
            boolean r0 = r0[r8]
            if (r0 == 0) goto L_0x018d
            boolean r0 = r50.isInVerticalChain()
            if (r0 != 0) goto L_0x018d
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r1 = 8
            r14.addGreaterThan(r0, r10, r7, r1)
        L_0x018d:
            r15.resolvedHorizontal = r7
            r15.resolvedVertical = r7
            return
        L_0x0192:
            androidx.constraintlayout.solver.Metrics r0 = androidx.constraintlayout.solver.LinearSystem.sMetrics
            if (r0 == 0) goto L_0x019d
            androidx.constraintlayout.solver.Metrics r0 = androidx.constraintlayout.solver.LinearSystem.sMetrics
            long r3 = r0.linearSolved
            long r3 = r3 + r1
            r0.linearSolved = r3
        L_0x019d:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x0212
            boolean r0 = r15.isChainHead(r7)
            if (r0 == 0) goto L_0x01b0
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r0 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r0
            r0.addChain(r15, r7)
            r0 = r8
            goto L_0x01b4
        L_0x01b0:
            boolean r0 = r50.isInHorizontalChain()
        L_0x01b4:
            boolean r1 = r15.isChainHead(r8)
            if (r1 == 0) goto L_0x01c3
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r1 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r1
            r1.addChain(r15, r8)
            r1 = r8
            goto L_0x01c7
        L_0x01c3:
            boolean r1 = r50.isInVerticalChain()
        L_0x01c7:
            if (r0 != 0) goto L_0x01e8
            if (r5 == 0) goto L_0x01e8
            int r2 = r15.mVisibility
            r3 = 8
            if (r2 == r3) goto L_0x01e8
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r15.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x01e8
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r15.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x01e8
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mRight
            androidx.constraintlayout.solver.SolverVariable r2 = r14.createObjectVariable(r2)
            r14.addGreaterThan(r2, r12, r7, r8)
        L_0x01e8:
            if (r1 != 0) goto L_0x020d
            if (r6 == 0) goto L_0x020d
            int r2 = r15.mVisibility
            r3 = 8
            if (r2 == r3) goto L_0x020d
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r15.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x020d
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r15.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 != 0) goto L_0x020d
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r15.mBaseline
            if (r2 != 0) goto L_0x020d
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mBottom
            androidx.constraintlayout.solver.SolverVariable r2 = r14.createObjectVariable(r2)
            r14.addGreaterThan(r2, r10, r7, r8)
        L_0x020d:
            r29 = r0
            r28 = r1
            goto L_0x0216
        L_0x0212:
            r28 = r7
            r29 = r28
        L_0x0216:
            int r0 = r15.mWidth
            int r1 = r15.mMinWidth
            if (r0 >= r1) goto L_0x021d
            r0 = r1
        L_0x021d:
            int r1 = r15.mHeight
            int r2 = r15.mMinHeight
            if (r1 >= r2) goto L_0x0224
            r1 = r2
        L_0x0224:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r15.mListDimensionBehaviors
            r2 = r2[r7]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r2 == r3) goto L_0x022e
            r2 = r8
            goto L_0x022f
        L_0x022e:
            r2 = r7
        L_0x022f:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r3 = r15.mListDimensionBehaviors
            r3 = r3[r8]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r3 == r4) goto L_0x0239
            r3 = r8
            goto L_0x023a
        L_0x0239:
            r3 = r7
        L_0x023a:
            int r4 = r15.mDimensionRatioSide
            r15.mResolvedDimensionRatioSide = r4
            float r4 = r15.mDimensionRatio
            r15.mResolvedDimensionRatio = r4
            int r8 = r15.mMatchConstraintDefaultWidth
            int r7 = r15.mMatchConstraintDefaultHeight
            r20 = 0
            int r4 = (r4 > r20 ? 1 : (r4 == r20 ? 0 : -1))
            r20 = 4
            r21 = r0
            if (r4 <= 0) goto L_0x02f7
            int r4 = r15.mVisibility
            r0 = 8
            if (r4 == r0) goto L_0x02f7
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r4 = 0
            r0 = r0[r4]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r23 = r1
            r1 = 3
            if (r0 != r4) goto L_0x0265
            if (r8 != 0) goto L_0x0265
            r8 = r1
        L_0x0265:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r4 = 1
            r0 = r0[r4]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r4) goto L_0x0271
            if (r7 != 0) goto L_0x0271
            r7 = r1
        L_0x0271:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r4 = 0
            r0 = r0[r4]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r4) goto L_0x028c
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r4 = 1
            r0 = r0[r4]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r4) goto L_0x028c
            if (r8 != r1) goto L_0x028c
            if (r7 != r1) goto L_0x028c
            r15.setupDimensionRatio(r5, r6, r2, r3)
            goto L_0x02ed
        L_0x028c:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r2 = 0
            r0 = r0[r2]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r3) goto L_0x02b4
            if (r8 != r1) goto L_0x02b4
            r15.mResolvedDimensionRatioSide = r2
            float r0 = r15.mResolvedDimensionRatio
            int r1 = r15.mHeight
            float r1 = (float) r1
            float r0 = r0 * r1
            int r0 = (int) r0
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r15.mListDimensionBehaviors
            r2 = 1
            r1 = r1[r2]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r31 = r7
            if (r1 == r3) goto L_0x02ae
            r32 = r20
            goto L_0x02ff
        L_0x02ae:
            r32 = r8
            r30 = r23
            r8 = r2
            goto L_0x0302
        L_0x02b4:
            r2 = 1
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r0 = r0[r2]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r3) goto L_0x02ed
            if (r7 != r1) goto L_0x02ed
            r15.mResolvedDimensionRatioSide = r2
            int r0 = r15.mDimensionRatioSide
            r1 = -1
            if (r0 != r1) goto L_0x02cd
            r0 = 1065353216(0x3f800000, float:1.0)
            float r1 = r15.mResolvedDimensionRatio
            float r0 = r0 / r1
            r15.mResolvedDimensionRatio = r0
        L_0x02cd:
            float r0 = r15.mResolvedDimensionRatio
            int r1 = r15.mWidth
            float r1 = (float) r1
            float r0 = r0 * r1
            int r1 = (int) r0
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r2 = 0
            r0 = r0[r2]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            r30 = r1
            if (r0 == r2) goto L_0x02e6
            r32 = r8
            r31 = r20
            r0 = r21
            goto L_0x0301
        L_0x02e6:
            r31 = r7
            r32 = r8
            r0 = r21
            goto L_0x02f5
        L_0x02ed:
            r31 = r7
            r32 = r8
            r0 = r21
            r30 = r23
        L_0x02f5:
            r8 = 1
            goto L_0x0302
        L_0x02f7:
            r23 = r1
            r31 = r7
            r32 = r8
            r0 = r21
        L_0x02ff:
            r30 = r23
        L_0x0301:
            r8 = 0
        L_0x0302:
            int[] r1 = r15.mResolvedMatchConstraintDefault
            r2 = 0
            r1[r2] = r32
            r2 = 1
            r1[r2] = r31
            r15.mResolvedHasRatio = r8
            if (r8 == 0) goto L_0x0318
            int r1 = r15.mResolvedDimensionRatioSide
            r2 = -1
            if (r1 == 0) goto L_0x0315
            if (r1 != r2) goto L_0x0319
        L_0x0315:
            r20 = 1
            goto L_0x031b
        L_0x0318:
            r2 = -1
        L_0x0319:
            r20 = 0
        L_0x031b:
            if (r8 == 0) goto L_0x0327
            int r1 = r15.mResolvedDimensionRatioSide
            r3 = 1
            if (r1 == r3) goto L_0x0324
            if (r1 != r2) goto L_0x0327
        L_0x0324:
            r33 = 1
            goto L_0x0329
        L_0x0327:
            r33 = 0
        L_0x0329:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r15.mListDimensionBehaviors
            r2 = 0
            r1 = r1[r2]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r1 != r2) goto L_0x0339
            boolean r1 = r15 instanceof androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer
            if (r1 == 0) goto L_0x0339
            r21 = 1
            goto L_0x033b
        L_0x0339:
            r21 = 0
        L_0x033b:
            if (r21 == 0) goto L_0x0340
            r22 = 0
            goto L_0x0342
        L_0x0340:
            r22 = r0
        L_0x0342:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mCenter
            boolean r0 = r0.isConnected()
            r1 = 1
            r34 = r0 ^ 1
            boolean[] r0 = r15.mIsInBarrier
            r2 = 0
            boolean r23 = r0[r2]
            boolean r35 = r0[r1]
            int r0 = r15.mHorizontalResolution
            r7 = 2
            r36 = 0
            if (r0 == r7) goto L_0x0449
            boolean r0 = r15.resolvedHorizontal
            if (r0 != 0) goto L_0x0449
            if (r52 == 0) goto L_0x03a8
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            if (r0 == 0) goto L_0x03a8
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x03a8
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 != 0) goto L_0x0372
            goto L_0x03a8
        L_0x0372:
            if (r52 == 0) goto L_0x0449
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            int r0 = r0.value
            r14.addEquality(r13, r0)
            androidx.constraintlayout.solver.widgets.analyzer.HorizontalWidgetRun r0 = r15.horizontalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r14.addEquality(r12, r0)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x0449
            if (r5 == 0) goto L_0x0449
            boolean[] r0 = r15.isTerminalWidget
            r1 = 0
            boolean r0 = r0[r1]
            if (r0 == 0) goto L_0x0449
            boolean r0 = r50.isInHorizontalChain()
            if (r0 != 0) goto L_0x0449
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r4 = 8
            r14.addGreaterThan(r0, r12, r1, r4)
            goto L_0x0449
        L_0x03a8:
            r4 = 8
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x03b7
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mRight
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r16 = r0
            goto L_0x03b9
        L_0x03b7:
            r16 = r36
        L_0x03b9:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x03c6
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mLeft
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r27 = r0
            goto L_0x03c8
        L_0x03c6:
            r27 = r36
        L_0x03c8:
            boolean[] r0 = r15.isTerminalWidget
            r19 = 0
            boolean r37 = r0[r19]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r38 = r0[r19]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r15.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r15.mRight
            int r2 = r15.f35mX
            r40 = r2
            int r2 = r15.mMinWidth
            int[] r4 = r15.mMaxDimension
            r42 = r4[r19]
            float r4 = r15.mHorizontalBiasPercent
            r18 = 1
            r0 = r0[r18]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r7) goto L_0x03ed
            r44 = r18
            goto L_0x03ef
        L_0x03ed:
            r44 = r19
        L_0x03ef:
            int r0 = r15.mMatchConstraintMinWidth
            r24 = r0
            int r0 = r15.mMatchConstraintMaxWidth
            r25 = r0
            float r0 = r15.mMatchConstraintPercentWidth
            r26 = r0
            r0 = r50
            r45 = r1
            r1 = r51
            r39 = r40
            r40 = r2
            r2 = 1
            r17 = r3
            r7 = 5
            r3 = r5
            r41 = r4
            r4 = r6
            r46 = r5
            r5 = r37
            r37 = r6
            r6 = r27
            r7 = r16
            r43 = r8
            r8 = r38
            r47 = r9
            r9 = r21
            r48 = r10
            r10 = r17
            r49 = r11
            r11 = r45
            r38 = r12
            r12 = r39
            r39 = r13
            r13 = r22
            r14 = r40
            r15 = r42
            r16 = r41
            r17 = r20
            r18 = r44
            r19 = r29
            r20 = r28
            r21 = r23
            r22 = r32
            r23 = r31
            r27 = r34
            r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27)
            goto L_0x0459
        L_0x0449:
            r46 = r5
            r37 = r6
            r43 = r8
            r47 = r9
            r48 = r10
            r49 = r11
            r38 = r12
            r39 = r13
        L_0x0459:
            if (r52 == 0) goto L_0x04c5
            r15 = r50
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            if (r0 == 0) goto L_0x04b8
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x04b8
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            boolean r0 = r0.resolved
            if (r0 == 0) goto L_0x04b8
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.start
            int r0 = r0.value
            r14 = r51
            r13 = r49
            r14.addEquality(r13, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.end
            int r0 = r0.value
            r12 = r48
            r14.addEquality(r12, r0)
            androidx.constraintlayout.solver.widgets.analyzer.VerticalWidgetRun r0 = r15.verticalRun
            androidx.constraintlayout.solver.widgets.analyzer.DependencyNode r0 = r0.baseline
            int r0 = r0.value
            r1 = r47
            r14.addEquality(r1, r0)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x04b2
            if (r28 != 0) goto L_0x04b2
            if (r37 == 0) goto L_0x04b2
            boolean[] r2 = r15.isTerminalWidget
            r11 = 1
            boolean r2 = r2[r11]
            if (r2 == 0) goto L_0x04ae
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r2 = 8
            r10 = 0
            r14.addGreaterThan(r0, r12, r10, r2)
            goto L_0x04b6
        L_0x04ae:
            r2 = 8
            r10 = 0
            goto L_0x04b6
        L_0x04b2:
            r2 = 8
            r10 = 0
            r11 = 1
        L_0x04b6:
            r8 = r10
            goto L_0x04d4
        L_0x04b8:
            r14 = r51
            r1 = r47
            r12 = r48
            r13 = r49
            r2 = 8
            r10 = 0
            r11 = 1
            goto L_0x04d3
        L_0x04c5:
            r2 = 8
            r10 = 0
            r11 = 1
            r15 = r50
            r14 = r51
            r1 = r47
            r12 = r48
            r13 = r49
        L_0x04d3:
            r8 = r11
        L_0x04d4:
            int r0 = r15.mVerticalResolution
            r3 = 2
            if (r0 != r3) goto L_0x04db
            r7 = r10
            goto L_0x04dc
        L_0x04db:
            r7 = r8
        L_0x04dc:
            if (r7 == 0) goto L_0x05b2
            boolean r0 = r15.resolvedVertical
            if (r0 != 0) goto L_0x05b2
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r0 = r0[r11]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r0 != r3) goto L_0x04f0
            boolean r0 = r15 instanceof androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer
            if (r0 == 0) goto L_0x04f0
            r9 = r11
            goto L_0x04f1
        L_0x04f0:
            r9 = r10
        L_0x04f1:
            if (r9 == 0) goto L_0x04f5
            r30 = r10
        L_0x04f5:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x0501
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mBottom
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r7 = r0
            goto L_0x0503
        L_0x0501:
            r7 = r36
        L_0x0503:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r15.mParent
            if (r0 == 0) goto L_0x050f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTop
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r6 = r0
            goto L_0x0511
        L_0x050f:
            r6 = r36
        L_0x0511:
            int r0 = r15.mBaselineDistance
            if (r0 > 0) goto L_0x0519
            int r0 = r15.mVisibility
            if (r0 != r2) goto L_0x054f
        L_0x0519:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            if (r0 == 0) goto L_0x0540
            int r0 = r50.getBaselineDistance()
            r14.addEquality(r1, r13, r0, r2)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r14.addEquality(r1, r0, r10, r2)
            if (r37 == 0) goto L_0x053d
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r15.mBottom
            androidx.constraintlayout.solver.SolverVariable r0 = r14.createObjectVariable(r0)
            r1 = 5
            r14.addGreaterThan(r7, r0, r10, r1)
        L_0x053d:
            r27 = r10
            goto L_0x0551
        L_0x0540:
            int r0 = r15.mVisibility
            if (r0 != r2) goto L_0x0548
            r14.addEquality(r1, r13, r10, r2)
            goto L_0x054f
        L_0x0548:
            int r0 = r50.getBaselineDistance()
            r14.addEquality(r1, r13, r0, r2)
        L_0x054f:
            r27 = r34
        L_0x0551:
            boolean[] r0 = r15.isTerminalWidget
            boolean r5 = r0[r11]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r15.mListDimensionBehaviors
            r8 = r0[r11]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r15.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r15.mBottom
            int r1 = r15.f36mY
            int r2 = r15.mMinHeight
            int[] r10 = r15.mMaxDimension
            r16 = r10[r11]
            float r10 = r15.mVerticalBiasPercent
            r17 = 0
            r0 = r0[r17]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r11 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r11) goto L_0x0572
            r18 = 1
            goto L_0x0574
        L_0x0572:
            r18 = r17
        L_0x0574:
            int r0 = r15.mMatchConstraintMinHeight
            r24 = r0
            int r0 = r15.mMatchConstraintMaxHeight
            r25 = r0
            float r0 = r15.mMatchConstraintPercentHeight
            r26 = r0
            r0 = r50
            r19 = r1
            r1 = r51
            r20 = r2
            r2 = 0
            r11 = r3
            r3 = r37
            r21 = r4
            r4 = r46
            r17 = r10
            r10 = r21
            r34 = r12
            r12 = r19
            r36 = r13
            r13 = r30
            r14 = r20
            r15 = r16
            r16 = r17
            r17 = r33
            r19 = r28
            r20 = r29
            r21 = r35
            r22 = r31
            r23 = r32
            r0.applyConstraints(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27)
            goto L_0x05b6
        L_0x05b2:
            r34 = r12
            r36 = r13
        L_0x05b6:
            if (r43 == 0) goto L_0x05e3
            r6 = 8
            r7 = r50
            int r0 = r7.mResolvedDimensionRatioSide
            r1 = 1
            if (r0 != r1) goto L_0x05d1
            float r5 = r7.mResolvedDimensionRatio
            r0 = r51
            r1 = r34
            r2 = r36
            r3 = r38
            r4 = r39
            r0.addRatio(r1, r2, r3, r4, r5, r6)
            goto L_0x05e5
        L_0x05d1:
            float r5 = r7.mResolvedDimensionRatio
            r6 = 8
            r0 = r51
            r1 = r38
            r2 = r39
            r3 = r34
            r4 = r36
            r0.addRatio(r1, r2, r3, r4, r5, r6)
            goto L_0x05e5
        L_0x05e3:
            r7 = r50
        L_0x05e5:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.mCenter
            boolean r0 = r0.isConnected()
            if (r0 == 0) goto L_0x060d
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r7.mCenter
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.getTarget()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r0.getOwner()
            float r1 = r7.mCircleConstraintAngle
            r2 = 1119092736(0x42b40000, float:90.0)
            float r1 = r1 + r2
            double r1 = (double) r1
            double r1 = java.lang.Math.toRadians(r1)
            float r1 = (float) r1
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r7.mCenter
            int r2 = r2.getMargin()
            r3 = r51
            r3.addCenterPoint(r7, r0, r1, r2)
        L_0x060d:
            r0 = 0
            r7.resolvedHorizontal = r0
            r7.resolvedVertical = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.addToSolver(androidx.constraintlayout.solver.LinearSystem, boolean):void");
    }

    /* access modifiers changed from: package-private */
    public boolean addFirst() {
        return (this instanceof VirtualLayout) || (this instanceof Guideline);
    }

    public void setupDimensionRatio(boolean z, boolean z2, boolean z3, boolean z4) {
        if (this.mResolvedDimensionRatioSide == -1) {
            if (z3 && !z4) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (!z3 && z4) {
                this.mResolvedDimensionRatioSide = 1;
                if (this.mDimensionRatioSide == -1) {
                    this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                }
            }
        }
        if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
            this.mResolvedDimensionRatioSide = 1;
        } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
            this.mResolvedDimensionRatioSide = 0;
        }
        if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected())) {
            if (this.mTop.isConnected() && this.mBottom.isConnected()) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
        if (this.mResolvedDimensionRatioSide == -1) {
            int i = this.mMatchConstraintMinWidth;
            if (i > 0 && this.mMatchConstraintMinHeight == 0) {
                this.mResolvedDimensionRatioSide = 0;
            } else if (i == 0 && this.mMatchConstraintMinHeight > 0) {
                this.mResolvedDimensionRatio = 1.0f / this.mResolvedDimensionRatio;
                this.mResolvedDimensionRatioSide = 1;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:104:0x01dd  */
    /* JADX WARNING: Removed duplicated region for block: B:212:0x039b  */
    /* JADX WARNING: Removed duplicated region for block: B:220:0x03dd  */
    /* JADX WARNING: Removed duplicated region for block: B:225:0x03f5 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:226:0x03f6  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:299:0x04bf A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x008c  */
    /* JADX WARNING: Removed duplicated region for block: B:316:0x04ec  */
    /* JADX WARNING: Removed duplicated region for block: B:318:0x04f9 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:347:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:351:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00b2  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00df  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyConstraints(androidx.constraintlayout.solver.LinearSystem r32, boolean r33, boolean r34, boolean r35, boolean r36, androidx.constraintlayout.solver.SolverVariable r37, androidx.constraintlayout.solver.SolverVariable r38, androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour r39, boolean r40, androidx.constraintlayout.solver.widgets.ConstraintAnchor r41, androidx.constraintlayout.solver.widgets.ConstraintAnchor r42, int r43, int r44, int r45, int r46, float r47, boolean r48, boolean r49, boolean r50, boolean r51, boolean r52, int r53, int r54, int r55, int r56, float r57, boolean r58) {
        /*
            r31 = this;
            r0 = r31
            r10 = r32
            r11 = r37
            r12 = r38
            r13 = r41
            r14 = r42
            r15 = r45
            r1 = r46
            r2 = r54
            r3 = r55
            r4 = r56
            androidx.constraintlayout.solver.SolverVariable r9 = r10.createObjectVariable(r13)
            androidx.constraintlayout.solver.SolverVariable r8 = r10.createObjectVariable(r14)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r41.getTarget()
            androidx.constraintlayout.solver.SolverVariable r7 = r10.createObjectVariable(r5)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r42.getTarget()
            androidx.constraintlayout.solver.SolverVariable r6 = r10.createObjectVariable(r5)
            androidx.constraintlayout.solver.Metrics r5 = androidx.constraintlayout.solver.LinearSystem.getMetrics()
            if (r5 == 0) goto L_0x0040
            androidx.constraintlayout.solver.Metrics r5 = androidx.constraintlayout.solver.LinearSystem.getMetrics()
            long r11 = r5.nonresolvedWidgets
            r16 = 1
            long r11 = r11 + r16
            r5.nonresolvedWidgets = r11
        L_0x0040:
            boolean r11 = r41.isConnected()
            boolean r12 = r42.isConnected()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r0.mCenter
            boolean r16 = r5.isConnected()
            if (r12 == 0) goto L_0x0053
            int r5 = r11 + 1
            goto L_0x0054
        L_0x0053:
            r5 = r11
        L_0x0054:
            if (r16 == 0) goto L_0x0058
            int r5 = r5 + 1
        L_0x0058:
            if (r48 == 0) goto L_0x005d
            r18 = 3
            goto L_0x005f
        L_0x005d:
            r18 = r53
        L_0x005f:
            int[] r17 = androidx.constraintlayout.solver.widgets.ConstraintWidget.C01301.f38xdde91696
            int r19 = r39.ordinal()
            r2 = r17[r19]
            r14 = 1
            if (r2 == r14) goto L_0x0073
            r14 = 2
            if (r2 == r14) goto L_0x0073
            r14 = 3
            if (r2 == r14) goto L_0x0073
            r14 = 4
            if (r2 == r14) goto L_0x0078
        L_0x0073:
            r2 = r18
        L_0x0075:
            r18 = 0
            goto L_0x007e
        L_0x0078:
            r2 = r18
            if (r2 == r14) goto L_0x0075
            r18 = 1
        L_0x007e:
            int r14 = r0.mVisibility
            r13 = 8
            if (r14 != r13) goto L_0x0088
            r14 = 0
            r18 = 0
            goto L_0x008a
        L_0x0088:
            r14 = r44
        L_0x008a:
            if (r58 == 0) goto L_0x00ad
            if (r11 != 0) goto L_0x0098
            if (r12 != 0) goto L_0x0098
            if (r16 != 0) goto L_0x0098
            r13 = r43
            r10.addEquality(r9, r13)
            goto L_0x00a8
        L_0x0098:
            if (r11 == 0) goto L_0x00a8
            if (r12 != 0) goto L_0x00a8
            int r13 = r41.getMargin()
            r21 = r6
            r6 = 8
            r10.addEquality(r9, r7, r13, r6)
            goto L_0x00b0
        L_0x00a8:
            r21 = r6
            r6 = 8
            goto L_0x00b0
        L_0x00ad:
            r21 = r6
            r6 = r13
        L_0x00b0:
            if (r18 != 0) goto L_0x00df
            if (r40 == 0) goto L_0x00c9
            r6 = 0
            r13 = 3
            r10.addEquality(r8, r9, r6, r13)
            r13 = 8
            if (r15 <= 0) goto L_0x00c0
            r10.addGreaterThan(r8, r9, r15, r13)
        L_0x00c0:
            r14 = 2147483647(0x7fffffff, float:NaN)
            if (r1 >= r14) goto L_0x00ce
            r10.addLowerThan(r8, r9, r1, r13)
            goto L_0x00ce
        L_0x00c9:
            r13 = r6
            r6 = 0
            r10.addEquality(r8, r9, r14, r13)
        L_0x00ce:
            r22 = r3
            r1 = r4
            r19 = r6
            r14 = r7
            r15 = r8
        L_0x00d5:
            r23 = r18
            r13 = r21
            r18 = r36
        L_0x00db:
            r21 = r5
            goto L_0x01db
        L_0x00df:
            r1 = 2
            r6 = 0
            if (r5 == r1) goto L_0x0107
            if (r48 != 0) goto L_0x0107
            r1 = 1
            if (r2 == r1) goto L_0x00ea
            if (r2 != 0) goto L_0x0107
        L_0x00ea:
            int r1 = java.lang.Math.max(r3, r14)
            if (r4 <= 0) goto L_0x00f4
            int r1 = java.lang.Math.min(r4, r1)
        L_0x00f4:
            r13 = 8
            r10.addEquality(r8, r9, r1, r13)
            r18 = r36
            r22 = r3
            r1 = r4
            r19 = r6
            r23 = r19
            r14 = r7
            r15 = r8
            r13 = r21
            goto L_0x00db
        L_0x0107:
            r1 = -2
            if (r3 != r1) goto L_0x010c
            r13 = r14
            goto L_0x010d
        L_0x010c:
            r13 = r3
        L_0x010d:
            if (r4 != r1) goto L_0x0111
            r1 = r14
            goto L_0x0112
        L_0x0111:
            r1 = r4
        L_0x0112:
            if (r14 <= 0) goto L_0x0118
            r3 = 1
            if (r2 == r3) goto L_0x0118
            r14 = r6
        L_0x0118:
            if (r13 <= 0) goto L_0x0123
            r3 = 8
            r10.addGreaterThan(r8, r9, r13, r3)
            int r14 = java.lang.Math.max(r14, r13)
        L_0x0123:
            if (r1 <= 0) goto L_0x013c
            if (r34 == 0) goto L_0x012c
            r3 = 1
            if (r2 != r3) goto L_0x012c
            r3 = r6
            goto L_0x012d
        L_0x012c:
            r3 = 1
        L_0x012d:
            if (r3 == 0) goto L_0x0135
            r3 = 8
            r10.addLowerThan(r8, r9, r1, r3)
            goto L_0x0137
        L_0x0135:
            r3 = 8
        L_0x0137:
            int r14 = java.lang.Math.min(r14, r1)
            goto L_0x013e
        L_0x013c:
            r3 = 8
        L_0x013e:
            r4 = 1
            if (r2 != r4) goto L_0x0160
            if (r34 == 0) goto L_0x0147
            r10.addEquality(r8, r9, r14, r3)
            goto L_0x0158
        L_0x0147:
            if (r50 == 0) goto L_0x0151
            r4 = 5
            r10.addEquality(r8, r9, r14, r4)
            r10.addLowerThan(r8, r9, r14, r3)
            goto L_0x0158
        L_0x0151:
            r4 = 5
            r10.addEquality(r8, r9, r14, r4)
            r10.addLowerThan(r8, r9, r14, r3)
        L_0x0158:
            r19 = r6
            r14 = r7
            r15 = r8
            r22 = r13
            goto L_0x00d5
        L_0x0160:
            r14 = 2
            if (r2 != r14) goto L_0x01cb
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = r41.getType()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            if (r3 == r4) goto L_0x018d
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = r41.getType()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            if (r3 != r4) goto L_0x0174
            goto L_0x018d
        L_0x0174:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r0.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r3.getAnchor(r4)
            androidx.constraintlayout.solver.SolverVariable r3 = r10.createObjectVariable(r3)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r4 = r0.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r6 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r4.getAnchor(r6)
            androidx.constraintlayout.solver.SolverVariable r4 = r10.createObjectVariable(r4)
            goto L_0x01a5
        L_0x018d:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r0.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r3.getAnchor(r4)
            androidx.constraintlayout.solver.SolverVariable r3 = r10.createObjectVariable(r3)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r4 = r0.mParent
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r6 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r4.getAnchor(r6)
            androidx.constraintlayout.solver.SolverVariable r4 = r10.createObjectVariable(r4)
        L_0x01a5:
            r18 = r3
            r6 = r4
            androidx.constraintlayout.solver.ArrayRow r3 = r32.createRow()
            r4 = r8
            r14 = r5
            r5 = r9
            r40 = r13
            r13 = r21
            r19 = 0
            r21 = r14
            r14 = r7
            r7 = r18
            r15 = r8
            r8 = r57
            androidx.constraintlayout.solver.ArrayRow r3 = r3.createRowDimensionRatio(r4, r5, r6, r7, r8)
            r10.addConstraint(r3)
            r18 = r36
            r22 = r40
            r23 = r19
            goto L_0x01db
        L_0x01cb:
            r19 = r6
            r14 = r7
            r15 = r8
            r40 = r13
            r13 = r21
            r21 = r5
            r22 = r40
            r23 = r18
            r18 = 1
        L_0x01db:
            if (r58 == 0) goto L_0x04ec
            if (r50 == 0) goto L_0x01ec
            r1 = r37
            r4 = r38
            r2 = r15
            r3 = r19
            r5 = r21
            r6 = 2
            r15 = r9
            goto L_0x04f7
        L_0x01ec:
            if (r11 != 0) goto L_0x01f3
            if (r12 != 0) goto L_0x01f3
            if (r16 != 0) goto L_0x01f3
            goto L_0x01f7
        L_0x01f3:
            if (r11 == 0) goto L_0x01fc
            if (r12 != 0) goto L_0x01fc
        L_0x01f7:
            r2 = r15
            r3 = r19
            goto L_0x04bb
        L_0x01fc:
            if (r11 != 0) goto L_0x0238
            if (r12 == 0) goto L_0x0238
            int r1 = r42.getMargin()
            int r1 = -r1
            r2 = 8
            r10.addEquality(r15, r13, r1, r2)
            if (r34 == 0) goto L_0x01f7
            boolean r1 = r0.OPTIMIZE_WRAP
            if (r1 == 0) goto L_0x022e
            boolean r1 = r9.isFinalValue
            if (r1 == 0) goto L_0x022e
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r0.mParent
            if (r1 == 0) goto L_0x022e
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r1 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r1
            if (r33 == 0) goto L_0x0225
            r7 = r41
            r8 = r19
            r1.addHorizontalWrapMinVariable(r7)
            goto L_0x04b9
        L_0x0225:
            r7 = r41
            r8 = r19
            r1.addVerticalWrapMinVariable(r7)
            goto L_0x04b9
        L_0x022e:
            r8 = r19
            r6 = r37
            r1 = 5
            r10.addGreaterThan(r9, r6, r8, r1)
            goto L_0x04b9
        L_0x0238:
            r6 = r37
            r7 = r41
            r8 = r19
            if (r11 == 0) goto L_0x04b9
            if (r12 == 0) goto L_0x04b9
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r7.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r11 = r3.mOwner
            r12 = r42
            r3 = 2
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r12.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r4.mOwner
            androidx.constraintlayout.solver.widgets.ConstraintWidget r4 = r31.getParent()
            r16 = 6
            if (r23 == 0) goto L_0x033f
            if (r2 != 0) goto L_0x02a0
            if (r1 != 0) goto L_0x027e
            if (r22 != 0) goto L_0x027e
            boolean r1 = r14.isFinalValue
            if (r1 == 0) goto L_0x0275
            boolean r1 = r13.isFinalValue
            if (r1 == 0) goto L_0x0275
            int r1 = r41.getMargin()
            r2 = 8
            r10.addEquality(r9, r14, r1, r2)
            int r1 = r42.getMargin()
            int r1 = -r1
            r10.addEquality(r15, r13, r1, r2)
            return
        L_0x0275:
            r1 = r8
            r19 = r1
            r3 = 1
            r21 = 8
            r24 = 8
            goto L_0x0286
        L_0x027e:
            r3 = r8
            r1 = 1
            r19 = 1
            r21 = 5
            r24 = 5
        L_0x0286:
            boolean r8 = r11 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r8 != 0) goto L_0x0294
            boolean r8 = r5 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r8 == 0) goto L_0x028f
            goto L_0x0294
        L_0x028f:
            r8 = r38
            r25 = r21
            goto L_0x029a
        L_0x0294:
            r8 = r38
            r25 = r21
            r24 = 4
        L_0x029a:
            r21 = r3
            r3 = r16
            goto L_0x038c
        L_0x02a0:
            r8 = 1
            if (r2 != r8) goto L_0x02b2
            r8 = r38
            r3 = r16
            r1 = 1
            r19 = 1
            r21 = 0
            r24 = 4
        L_0x02ae:
            r25 = 8
            goto L_0x038c
        L_0x02b2:
            r8 = 3
            if (r2 != r8) goto L_0x0337
            int r8 = r0.mResolvedDimensionRatioSide
            r3 = -1
            if (r8 != r3) goto L_0x02d1
            if (r51 == 0) goto L_0x02c5
            r8 = r38
            r1 = 1
            if (r34 == 0) goto L_0x02c3
            r3 = 5
            goto L_0x02ca
        L_0x02c3:
            r3 = 4
            goto L_0x02ca
        L_0x02c5:
            r8 = r38
            r1 = 1
            r3 = 8
        L_0x02ca:
            r19 = 1
            r21 = 1
            r24 = 5
            goto L_0x02ae
        L_0x02d1:
            if (r48 == 0) goto L_0x02f6
            r3 = r54
            r8 = 2
            if (r3 == r8) goto L_0x02de
            r1 = 1
            if (r3 != r1) goto L_0x02dc
            goto L_0x02de
        L_0x02dc:
            r1 = 0
            goto L_0x02df
        L_0x02de:
            r1 = 1
        L_0x02df:
            if (r1 != 0) goto L_0x02e5
            r1 = 8
            r3 = 5
            goto L_0x02e7
        L_0x02e5:
            r1 = 5
            r3 = 4
        L_0x02e7:
            r8 = r38
            r25 = r1
            r24 = r3
            r3 = r16
            r1 = 1
            r19 = 1
            r21 = 1
            goto L_0x038c
        L_0x02f6:
            if (r1 <= 0) goto L_0x0305
            r8 = r38
            r3 = r16
            r1 = 1
            r19 = 1
            r21 = 1
            r24 = 5
            goto L_0x038a
        L_0x0305:
            if (r1 != 0) goto L_0x032d
            if (r22 != 0) goto L_0x032d
            if (r51 != 0) goto L_0x0318
            r8 = r38
            r3 = r16
            r1 = 1
            r19 = 1
            r21 = 1
            r24 = 8
            goto L_0x038a
        L_0x0318:
            if (r11 == r4) goto L_0x031e
            if (r5 == r4) goto L_0x031e
            r1 = 4
            goto L_0x031f
        L_0x031e:
            r1 = 5
        L_0x031f:
            r8 = r38
            r25 = r1
            r3 = r16
            r1 = 1
            r19 = 1
            r21 = 1
            r24 = 4
            goto L_0x038c
        L_0x032d:
            r8 = r38
            r3 = r16
            r1 = 1
            r19 = 1
            r21 = 1
            goto L_0x0388
        L_0x0337:
            r8 = r38
            r3 = r16
            r1 = 0
            r19 = 0
            goto L_0x0386
        L_0x033f:
            boolean r1 = r14.isFinalValue
            if (r1 == 0) goto L_0x037f
            boolean r1 = r13.isFinalValue
            if (r1 == 0) goto L_0x037f
            int r1 = r41.getMargin()
            int r2 = r42.getMargin()
            r3 = 8
            r48 = r32
            r49 = r9
            r50 = r14
            r51 = r1
            r52 = r47
            r53 = r13
            r54 = r15
            r55 = r2
            r56 = r3
            r48.addCentering(r49, r50, r51, r52, r53, r54, r55, r56)
            if (r34 == 0) goto L_0x037e
            if (r18 == 0) goto L_0x037e
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r12.mTarget
            if (r1 == 0) goto L_0x0375
            int r1 = r42.getMargin()
            r8 = r38
            goto L_0x0378
        L_0x0375:
            r8 = r38
            r1 = 0
        L_0x0378:
            if (r13 == r8) goto L_0x037e
            r2 = 5
            r10.addGreaterThan(r8, r15, r1, r2)
        L_0x037e:
            return
        L_0x037f:
            r8 = r38
            r3 = r16
            r1 = 1
            r19 = 1
        L_0x0386:
            r21 = 0
        L_0x0388:
            r24 = 4
        L_0x038a:
            r25 = 5
        L_0x038c:
            if (r19 == 0) goto L_0x0397
            if (r14 != r13) goto L_0x0397
            if (r11 == r4) goto L_0x0397
            r19 = 0
            r26 = 0
            goto L_0x0399
        L_0x0397:
            r26 = 1
        L_0x0399:
            if (r1 == 0) goto L_0x03dd
            if (r23 != 0) goto L_0x03ae
            if (r49 != 0) goto L_0x03ae
            if (r51 != 0) goto L_0x03ae
            if (r14 != r6) goto L_0x03ae
            if (r13 != r8) goto L_0x03ae
            r25 = 0
            r26 = 8
            r27 = 0
            r28 = 8
            goto L_0x03b6
        L_0x03ae:
            r28 = r25
            r27 = r26
            r25 = r34
            r26 = r3
        L_0x03b6:
            int r29 = r41.getMargin()
            int r30 = r42.getMargin()
            r1 = r32
            r3 = r2
            r2 = r9
            r12 = r3
            r3 = r14
            r39 = r12
            r12 = r4
            r4 = r29
            r29 = r12
            r12 = r5
            r5 = r47
            r6 = r13
            r7 = r15
            r8 = r30
            r20 = r15
            r15 = r9
            r9 = r26
            r1.addCentering(r2, r3, r4, r5, r6, r7, r8, r9)
            r26 = r27
            goto L_0x03e9
        L_0x03dd:
            r39 = r2
            r29 = r4
            r12 = r5
            r20 = r15
            r15 = r9
            r28 = r25
            r25 = r34
        L_0x03e9:
            int r1 = r0.mVisibility
            r2 = 8
            if (r1 != r2) goto L_0x03f6
            boolean r1 = r42.hasDependents()
            if (r1 != 0) goto L_0x03f6
            return
        L_0x03f6:
            if (r19 == 0) goto L_0x041f
            if (r25 == 0) goto L_0x0409
            if (r14 == r13) goto L_0x0409
            if (r23 != 0) goto L_0x0409
            boolean r1 = r11 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r1 != 0) goto L_0x0406
            boolean r1 = r12 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r1 == 0) goto L_0x0409
        L_0x0406:
            r1 = r16
            goto L_0x040b
        L_0x0409:
            r1 = r28
        L_0x040b:
            int r2 = r41.getMargin()
            r10.addGreaterThan(r15, r14, r2, r1)
            int r2 = r42.getMargin()
            int r2 = -r2
            r3 = r20
            r10.addLowerThan(r3, r13, r2, r1)
            r28 = r1
            goto L_0x0421
        L_0x041f:
            r3 = r20
        L_0x0421:
            if (r25 == 0) goto L_0x0433
            if (r52 == 0) goto L_0x0433
            boolean r1 = r11 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r1 != 0) goto L_0x0433
            boolean r1 = r12 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r1 != 0) goto L_0x0433
            r1 = r16
            r2 = r1
            r26 = 1
            goto L_0x0437
        L_0x0433:
            r1 = r24
            r2 = r28
        L_0x0437:
            if (r26 == 0) goto L_0x0487
            if (r21 == 0) goto L_0x0467
            if (r51 == 0) goto L_0x043f
            if (r35 == 0) goto L_0x0467
        L_0x043f:
            r4 = r29
            if (r11 == r4) goto L_0x0448
            if (r12 != r4) goto L_0x0446
            goto L_0x0448
        L_0x0446:
            r16 = r1
        L_0x0448:
            boolean r5 = r11 instanceof androidx.constraintlayout.solver.widgets.Guideline
            if (r5 != 0) goto L_0x0450
            boolean r5 = r12 instanceof androidx.constraintlayout.solver.widgets.Guideline
            if (r5 == 0) goto L_0x0452
        L_0x0450:
            r16 = 5
        L_0x0452:
            boolean r5 = r11 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r5 != 0) goto L_0x045a
            boolean r5 = r12 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r5 == 0) goto L_0x045c
        L_0x045a:
            r16 = 5
        L_0x045c:
            if (r51 == 0) goto L_0x0460
            r5 = 5
            goto L_0x0462
        L_0x0460:
            r5 = r16
        L_0x0462:
            int r1 = java.lang.Math.max(r5, r1)
            goto L_0x0469
        L_0x0467:
            r4 = r29
        L_0x0469:
            if (r25 == 0) goto L_0x0478
            int r1 = java.lang.Math.min(r2, r1)
            if (r48 == 0) goto L_0x0478
            if (r51 != 0) goto L_0x0478
            if (r11 == r4) goto L_0x0477
            if (r12 != r4) goto L_0x0478
        L_0x0477:
            r1 = 4
        L_0x0478:
            int r2 = r41.getMargin()
            r10.addEquality(r15, r14, r2, r1)
            int r2 = r42.getMargin()
            int r2 = -r2
            r10.addEquality(r3, r13, r2, r1)
        L_0x0487:
            if (r25 == 0) goto L_0x0499
            r1 = r37
            if (r1 != r14) goto L_0x0492
            int r2 = r41.getMargin()
            goto L_0x0493
        L_0x0492:
            r2 = 0
        L_0x0493:
            if (r14 == r1) goto L_0x0499
            r4 = 5
            r10.addGreaterThan(r15, r1, r2, r4)
        L_0x0499:
            if (r25 == 0) goto L_0x04b6
            if (r23 == 0) goto L_0x04b6
            r2 = r3
            if (r45 != 0) goto L_0x04b7
            if (r22 != 0) goto L_0x04b7
            if (r23 == 0) goto L_0x04b0
            r1 = r39
            r3 = 3
            if (r1 != r3) goto L_0x04b0
            r1 = 8
            r3 = 0
            r10.addGreaterThan(r2, r15, r3, r1)
            goto L_0x04bd
        L_0x04b0:
            r3 = 0
            r1 = 5
            r10.addGreaterThan(r2, r15, r3, r1)
            goto L_0x04bd
        L_0x04b6:
            r2 = r3
        L_0x04b7:
            r3 = 0
            goto L_0x04bd
        L_0x04b9:
            r3 = r8
            r2 = r15
        L_0x04bb:
            r25 = r34
        L_0x04bd:
            if (r25 == 0) goto L_0x04eb
            if (r18 == 0) goto L_0x04eb
            r1 = r42
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r1.mTarget
            if (r4 == 0) goto L_0x04cb
            int r3 = r42.getMargin()
        L_0x04cb:
            r4 = r38
            if (r13 == r4) goto L_0x04eb
            boolean r5 = r0.OPTIMIZE_WRAP
            if (r5 == 0) goto L_0x04e7
            boolean r5 = r2.isFinalValue
            if (r5 == 0) goto L_0x04e7
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r0.mParent
            if (r5 == 0) goto L_0x04e7
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r5 = (androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer) r5
            if (r33 == 0) goto L_0x04e3
            r5.addHorizontalWrapMaxVariable(r1)
            goto L_0x04e6
        L_0x04e3:
            r5.addVerticalWrapMaxVariable(r1)
        L_0x04e6:
            return
        L_0x04e7:
            r1 = 5
            r10.addGreaterThan(r4, r2, r3, r1)
        L_0x04eb:
            return
        L_0x04ec:
            r1 = r37
            r4 = r38
            r2 = r15
            r3 = r19
            r15 = r9
            r5 = r21
            r6 = 2
        L_0x04f7:
            if (r5 >= r6) goto L_0x0540
            if (r34 == 0) goto L_0x0540
            if (r18 == 0) goto L_0x0540
            r5 = 8
            r10.addGreaterThan(r15, r1, r3, r5)
            if (r33 != 0) goto L_0x050d
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r0.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mTarget
            if (r1 != 0) goto L_0x050b
            goto L_0x050d
        L_0x050b:
            r13 = r3
            goto L_0x050e
        L_0x050d:
            r13 = 1
        L_0x050e:
            if (r33 != 0) goto L_0x0538
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r0.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mTarget
            if (r1 == 0) goto L_0x0538
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r0.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r1.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = r1.mOwner
            float r5 = r1.mDimensionRatio
            r6 = 0
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 == 0) goto L_0x0536
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r5 = r1.mListDimensionBehaviors
            r5 = r5[r3]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r6 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r5 != r6) goto L_0x0536
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r1 = r1.mListDimensionBehaviors
            r5 = 1
            r1 = r1[r5]
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r6 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r1 != r6) goto L_0x0536
            r14 = r5
            goto L_0x0539
        L_0x0536:
            r14 = r3
            goto L_0x0539
        L_0x0538:
            r14 = r13
        L_0x0539:
            if (r14 == 0) goto L_0x0540
            r1 = 8
            r10.addGreaterThan(r4, r2, r3, r1)
        L_0x0540:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.applyConstraints(androidx.constraintlayout.solver.LinearSystem, boolean, boolean, boolean, boolean, androidx.constraintlayout.solver.SolverVariable, androidx.constraintlayout.solver.SolverVariable, androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour, boolean, androidx.constraintlayout.solver.widgets.ConstraintAnchor, androidx.constraintlayout.solver.widgets.ConstraintAnchor, int, int, int, int, float, boolean, boolean, boolean, boolean, boolean, int, int, int, int, float, boolean):void");
    }

    /* renamed from: androidx.constraintlayout.solver.widgets.ConstraintWidget$1 */
    static /* synthetic */ class C01301 {

        /* renamed from: $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type */
        static final /* synthetic */ int[] f37x4c44d048;

        /* renamed from: $SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour */
        static final /* synthetic */ int[] f38xdde91696;

        /* JADX WARNING: Can't wrap try/catch for region: R(29:0|(2:1|2)|3|(2:5|6)|7|9|10|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Can't wrap try/catch for region: R(31:0|1|2|3|(2:5|6)|7|9|10|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Can't wrap try/catch for region: R(32:0|1|2|3|5|6|7|9|10|11|13|14|15|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0044 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x004e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0058 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x006d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0078 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0083 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x008f */
        static {
            /*
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f38xdde91696 = r0
                r1 = 1
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = f38xdde91696     // Catch:{ NoSuchFieldError -> 0x001d }
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = f38xdde91696     // Catch:{ NoSuchFieldError -> 0x0028 }
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_PARENT     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                r3 = 4
                int[] r4 = f38xdde91696     // Catch:{ NoSuchFieldError -> 0x0033 }
                androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type[] r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.values()
                int r4 = r4.length
                int[] r4 = new int[r4]
                f37x4c44d048 = r4
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r5 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT     // Catch:{ NoSuchFieldError -> 0x0044 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0044 }
                r4[r5] = r1     // Catch:{ NoSuchFieldError -> 0x0044 }
            L_0x0044:
                int[] r1 = f37x4c44d048     // Catch:{ NoSuchFieldError -> 0x004e }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP     // Catch:{ NoSuchFieldError -> 0x004e }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x004e }
                r1[r4] = r0     // Catch:{ NoSuchFieldError -> 0x004e }
            L_0x004e:
                int[] r0 = f37x4c44d048     // Catch:{ NoSuchFieldError -> 0x0058 }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT     // Catch:{ NoSuchFieldError -> 0x0058 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0058 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0058 }
            L_0x0058:
                int[] r0 = f37x4c44d048     // Catch:{ NoSuchFieldError -> 0x0062 }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM     // Catch:{ NoSuchFieldError -> 0x0062 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0062 }
                r0[r1] = r3     // Catch:{ NoSuchFieldError -> 0x0062 }
            L_0x0062:
                int[] r0 = f37x4c44d048     // Catch:{ NoSuchFieldError -> 0x006d }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BASELINE     // Catch:{ NoSuchFieldError -> 0x006d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006d }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006d }
            L_0x006d:
                int[] r0 = f37x4c44d048     // Catch:{ NoSuchFieldError -> 0x0078 }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.CENTER     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                int[] r0 = f37x4c44d048     // Catch:{ NoSuchFieldError -> 0x0083 }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.CENTER_X     // Catch:{ NoSuchFieldError -> 0x0083 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0083 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0083 }
            L_0x0083:
                int[] r0 = f37x4c44d048     // Catch:{ NoSuchFieldError -> 0x008f }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.CENTER_Y     // Catch:{ NoSuchFieldError -> 0x008f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x008f }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x008f }
            L_0x008f:
                int[] r0 = f37x4c44d048     // Catch:{ NoSuchFieldError -> 0x009b }
                androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r1 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.NONE     // Catch:{ NoSuchFieldError -> 0x009b }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x009b }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x009b }
            L_0x009b:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidget.C01301.<clinit>():void");
        }
    }

    public void updateFromSolver(LinearSystem linearSystem, boolean z) {
        VerticalWidgetRun verticalWidgetRun;
        HorizontalWidgetRun horizontalWidgetRun;
        int objectVariableValue = linearSystem.getObjectVariableValue(this.mLeft);
        int objectVariableValue2 = linearSystem.getObjectVariableValue(this.mTop);
        int objectVariableValue3 = linearSystem.getObjectVariableValue(this.mRight);
        int objectVariableValue4 = linearSystem.getObjectVariableValue(this.mBottom);
        if (z && (horizontalWidgetRun = this.horizontalRun) != null && horizontalWidgetRun.start.resolved && this.horizontalRun.end.resolved) {
            objectVariableValue = this.horizontalRun.start.value;
            objectVariableValue3 = this.horizontalRun.end.value;
        }
        if (z && (verticalWidgetRun = this.verticalRun) != null && verticalWidgetRun.start.resolved && this.verticalRun.end.resolved) {
            objectVariableValue2 = this.verticalRun.start.value;
            objectVariableValue4 = this.verticalRun.end.value;
        }
        int i = objectVariableValue4 - objectVariableValue2;
        if (objectVariableValue3 - objectVariableValue < 0 || i < 0 || objectVariableValue == Integer.MIN_VALUE || objectVariableValue == Integer.MAX_VALUE || objectVariableValue2 == Integer.MIN_VALUE || objectVariableValue2 == Integer.MAX_VALUE || objectVariableValue3 == Integer.MIN_VALUE || objectVariableValue3 == Integer.MAX_VALUE || objectVariableValue4 == Integer.MIN_VALUE || objectVariableValue4 == Integer.MAX_VALUE) {
            objectVariableValue4 = 0;
            objectVariableValue = 0;
            objectVariableValue2 = 0;
            objectVariableValue3 = 0;
        }
        setFrame(objectVariableValue, objectVariableValue2, objectVariableValue3, objectVariableValue4);
    }

    public void copy(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        this.mHorizontalResolution = constraintWidget.mHorizontalResolution;
        this.mVerticalResolution = constraintWidget.mVerticalResolution;
        this.mMatchConstraintDefaultWidth = constraintWidget.mMatchConstraintDefaultWidth;
        this.mMatchConstraintDefaultHeight = constraintWidget.mMatchConstraintDefaultHeight;
        int[] iArr = this.mResolvedMatchConstraintDefault;
        int[] iArr2 = constraintWidget.mResolvedMatchConstraintDefault;
        iArr[0] = iArr2[0];
        iArr[1] = iArr2[1];
        this.mMatchConstraintMinWidth = constraintWidget.mMatchConstraintMinWidth;
        this.mMatchConstraintMaxWidth = constraintWidget.mMatchConstraintMaxWidth;
        this.mMatchConstraintMinHeight = constraintWidget.mMatchConstraintMinHeight;
        this.mMatchConstraintMaxHeight = constraintWidget.mMatchConstraintMaxHeight;
        this.mMatchConstraintPercentHeight = constraintWidget.mMatchConstraintPercentHeight;
        this.mIsWidthWrapContent = constraintWidget.mIsWidthWrapContent;
        this.mIsHeightWrapContent = constraintWidget.mIsHeightWrapContent;
        this.mResolvedDimensionRatioSide = constraintWidget.mResolvedDimensionRatioSide;
        this.mResolvedDimensionRatio = constraintWidget.mResolvedDimensionRatio;
        int[] iArr3 = constraintWidget.mMaxDimension;
        this.mMaxDimension = Arrays.copyOf(iArr3, iArr3.length);
        this.mCircleConstraintAngle = constraintWidget.mCircleConstraintAngle;
        this.hasBaseline = constraintWidget.hasBaseline;
        this.inPlaceholder = constraintWidget.inPlaceholder;
        this.mLeft.reset();
        this.mTop.reset();
        this.mRight.reset();
        this.mBottom.reset();
        this.mBaseline.reset();
        this.mCenterX.reset();
        this.mCenterY.reset();
        this.mCenter.reset();
        this.mListDimensionBehaviors = (DimensionBehaviour[]) Arrays.copyOf(this.mListDimensionBehaviors, 2);
        ConstraintWidget constraintWidget2 = null;
        this.mParent = this.mParent == null ? null : hashMap.get(constraintWidget.mParent);
        this.mWidth = constraintWidget.mWidth;
        this.mHeight = constraintWidget.mHeight;
        this.mDimensionRatio = constraintWidget.mDimensionRatio;
        this.mDimensionRatioSide = constraintWidget.mDimensionRatioSide;
        this.f35mX = constraintWidget.f35mX;
        this.f36mY = constraintWidget.f36mY;
        this.mRelX = constraintWidget.mRelX;
        this.mRelY = constraintWidget.mRelY;
        this.mOffsetX = constraintWidget.mOffsetX;
        this.mOffsetY = constraintWidget.mOffsetY;
        this.mBaselineDistance = constraintWidget.mBaselineDistance;
        this.mMinWidth = constraintWidget.mMinWidth;
        this.mMinHeight = constraintWidget.mMinHeight;
        this.mHorizontalBiasPercent = constraintWidget.mHorizontalBiasPercent;
        this.mVerticalBiasPercent = constraintWidget.mVerticalBiasPercent;
        this.mCompanionWidget = constraintWidget.mCompanionWidget;
        this.mContainerItemSkip = constraintWidget.mContainerItemSkip;
        this.mVisibility = constraintWidget.mVisibility;
        this.mDebugName = constraintWidget.mDebugName;
        this.mType = constraintWidget.mType;
        this.mDistToTop = constraintWidget.mDistToTop;
        this.mDistToLeft = constraintWidget.mDistToLeft;
        this.mDistToRight = constraintWidget.mDistToRight;
        this.mDistToBottom = constraintWidget.mDistToBottom;
        this.mLeftHasCentered = constraintWidget.mLeftHasCentered;
        this.mRightHasCentered = constraintWidget.mRightHasCentered;
        this.mTopHasCentered = constraintWidget.mTopHasCentered;
        this.mBottomHasCentered = constraintWidget.mBottomHasCentered;
        this.mHorizontalWrapVisited = constraintWidget.mHorizontalWrapVisited;
        this.mVerticalWrapVisited = constraintWidget.mVerticalWrapVisited;
        this.mHorizontalChainStyle = constraintWidget.mHorizontalChainStyle;
        this.mVerticalChainStyle = constraintWidget.mVerticalChainStyle;
        this.mHorizontalChainFixedPosition = constraintWidget.mHorizontalChainFixedPosition;
        this.mVerticalChainFixedPosition = constraintWidget.mVerticalChainFixedPosition;
        float[] fArr = this.mWeight;
        float[] fArr2 = constraintWidget.mWeight;
        fArr[0] = fArr2[0];
        fArr[1] = fArr2[1];
        ConstraintWidget[] constraintWidgetArr = this.mListNextMatchConstraintsWidget;
        ConstraintWidget[] constraintWidgetArr2 = constraintWidget.mListNextMatchConstraintsWidget;
        constraintWidgetArr[0] = constraintWidgetArr2[0];
        constraintWidgetArr[1] = constraintWidgetArr2[1];
        ConstraintWidget[] constraintWidgetArr3 = this.mNextChainWidget;
        ConstraintWidget[] constraintWidgetArr4 = constraintWidget.mNextChainWidget;
        constraintWidgetArr3[0] = constraintWidgetArr4[0];
        constraintWidgetArr3[1] = constraintWidgetArr4[1];
        ConstraintWidget constraintWidget3 = constraintWidget.mHorizontalNextWidget;
        this.mHorizontalNextWidget = constraintWidget3 == null ? null : hashMap.get(constraintWidget3);
        ConstraintWidget constraintWidget4 = constraintWidget.mVerticalNextWidget;
        if (constraintWidget4 != null) {
            constraintWidget2 = hashMap.get(constraintWidget4);
        }
        this.mVerticalNextWidget = constraintWidget2;
    }

    public void updateFromRuns(boolean z, boolean z2) {
        int i;
        int i2;
        boolean isResolved = z & this.horizontalRun.isResolved();
        boolean isResolved2 = z2 & this.verticalRun.isResolved();
        int i3 = this.horizontalRun.start.value;
        int i4 = this.verticalRun.start.value;
        int i5 = this.horizontalRun.end.value;
        int i6 = this.verticalRun.end.value;
        int i7 = i6 - i4;
        if (i5 - i3 < 0 || i7 < 0 || i3 == Integer.MIN_VALUE || i3 == Integer.MAX_VALUE || i4 == Integer.MIN_VALUE || i4 == Integer.MAX_VALUE || i5 == Integer.MIN_VALUE || i5 == Integer.MAX_VALUE || i6 == Integer.MIN_VALUE || i6 == Integer.MAX_VALUE) {
            i3 = 0;
            i4 = 0;
            i5 = 0;
            i6 = 0;
        }
        int i8 = i5 - i3;
        int i9 = i6 - i4;
        if (isResolved) {
            this.f35mX = i3;
        }
        if (isResolved2) {
            this.f36mY = i4;
        }
        if (this.mVisibility == 8) {
            this.mWidth = 0;
            this.mHeight = 0;
            return;
        }
        if (isResolved) {
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.FIXED && i8 < (i2 = this.mWidth)) {
                i8 = i2;
            }
            this.mWidth = i8;
            int i10 = this.mMinWidth;
            if (i8 < i10) {
                this.mWidth = i10;
            }
        }
        if (isResolved2) {
            if (this.mListDimensionBehaviors[1] == DimensionBehaviour.FIXED && i9 < (i = this.mHeight)) {
                i9 = i;
            }
            this.mHeight = i9;
            int i11 = this.mMinHeight;
            if (i9 < i11) {
                this.mHeight = i11;
            }
        }
    }

    public void addChildrenToSolverByDependency(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, HashSet<ConstraintWidget> hashSet, int i, boolean z) {
        if (z) {
            if (hashSet.contains(this)) {
                Optimizer.checkMatchParent(constraintWidgetContainer, linearSystem, this);
                hashSet.remove(this);
                addToSolver(linearSystem, constraintWidgetContainer.optimizeFor(64));
            } else {
                return;
            }
        }
        if (i == 0) {
            HashSet<ConstraintAnchor> dependents = this.mLeft.getDependents();
            if (dependents != null) {
                Iterator<ConstraintAnchor> it = dependents.iterator();
                while (it.hasNext()) {
                    it.next().mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, i, true);
                }
            }
            HashSet<ConstraintAnchor> dependents2 = this.mRight.getDependents();
            if (dependents2 != null) {
                Iterator<ConstraintAnchor> it2 = dependents2.iterator();
                while (it2.hasNext()) {
                    it2.next().mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, i, true);
                }
                return;
            }
            return;
        }
        HashSet<ConstraintAnchor> dependents3 = this.mTop.getDependents();
        if (dependents3 != null) {
            Iterator<ConstraintAnchor> it3 = dependents3.iterator();
            while (it3.hasNext()) {
                it3.next().mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, i, true);
            }
        }
        HashSet<ConstraintAnchor> dependents4 = this.mBottom.getDependents();
        if (dependents4 != null) {
            Iterator<ConstraintAnchor> it4 = dependents4.iterator();
            while (it4.hasNext()) {
                it4.next().mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, i, true);
            }
        }
        HashSet<ConstraintAnchor> dependents5 = this.mBaseline.getDependents();
        if (dependents5 != null) {
            Iterator<ConstraintAnchor> it5 = dependents5.iterator();
            while (it5.hasNext()) {
                it5.next().mOwner.addChildrenToSolverByDependency(constraintWidgetContainer, linearSystem, hashSet, i, true);
            }
        }
    }
}
