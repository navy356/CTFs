package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyGraph;
import androidx.constraintlayout.solver.widgets.analyzer.Direct;
import androidx.constraintlayout.solver.widgets.analyzer.Grouping;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class ConstraintWidgetContainer extends WidgetContainer {
    private static final boolean DEBUG = false;
    static final boolean DEBUG_GRAPH = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final int MAX_ITERATIONS = 8;
    static int mycounter;
    private WeakReference<ConstraintAnchor> horizontalWrapMax = null;
    private WeakReference<ConstraintAnchor> horizontalWrapMin = null;
    BasicMeasure mBasicMeasureSolver = new BasicMeasure(this);
    int mDebugSolverPassCount = 0;
    public DependencyGraph mDependencyGraph = new DependencyGraph(this);
    public boolean mGroupsWrapOptimized = false;
    private boolean mHeightMeasuredTooSmall = false;
    ChainHead[] mHorizontalChainsArray = new ChainHead[4];
    public int mHorizontalChainsSize = 0;
    public boolean mHorizontalWrapOptimized = false;
    private boolean mIsRtl = false;
    public BasicMeasure.Measure mMeasure = new BasicMeasure.Measure();
    protected BasicMeasure.Measurer mMeasurer = null;
    public Metrics mMetrics;
    private int mOptimizationLevel = 257;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    public boolean mSkipSolver = false;
    protected LinearSystem mSystem = new LinearSystem();
    ChainHead[] mVerticalChainsArray = new ChainHead[4];
    public int mVerticalChainsSize = 0;
    public boolean mVerticalWrapOptimized = false;
    private boolean mWidthMeasuredTooSmall = false;
    public int mWrapFixedHeight = 0;
    public int mWrapFixedWidth = 0;
    private WeakReference<ConstraintAnchor> verticalWrapMax = null;
    private WeakReference<ConstraintAnchor> verticalWrapMin = null;

    public String getType() {
        return "ConstraintLayout";
    }

    public boolean handlesInternalConstraints() {
        return false;
    }

    public void invalidateGraph() {
        this.mDependencyGraph.invalidateGraph();
    }

    public void invalidateMeasures() {
        this.mDependencyGraph.invalidateMeasures();
    }

    public boolean directMeasure(boolean z) {
        return this.mDependencyGraph.directMeasure(z);
    }

    public boolean directMeasureSetup(boolean z) {
        return this.mDependencyGraph.directMeasureSetup(z);
    }

    public boolean directMeasureWithOrientation(boolean z, int i) {
        return this.mDependencyGraph.directMeasureWithOrientation(z, i);
    }

    public void defineTerminalWidgets() {
        this.mDependencyGraph.defineTerminalWidgets(getHorizontalDimensionBehaviour(), getVerticalDimensionBehaviour());
    }

    public long measure(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        int i10 = i8;
        this.mPaddingLeft = i10;
        int i11 = i9;
        this.mPaddingTop = i11;
        return this.mBasicMeasureSolver.solverMeasure(this, i, i10, i11, i2, i3, i4, i5, i6, i7);
    }

    public void updateHierarchy() {
        this.mBasicMeasureSolver.updateHierarchy(this);
    }

    public void setMeasurer(BasicMeasure.Measurer measurer) {
        this.mMeasurer = measurer;
        this.mDependencyGraph.setMeasurer(measurer);
    }

    public BasicMeasure.Measurer getMeasurer() {
        return this.mMeasurer;
    }

    public void fillMetrics(Metrics metrics) {
        this.mMetrics = metrics;
        this.mSystem.fillMetrics(metrics);
    }

    public ConstraintWidgetContainer() {
    }

    public ConstraintWidgetContainer(int i, int i2, int i3, int i4) {
        super(i, i2, i3, i4);
    }

    public ConstraintWidgetContainer(int i, int i2) {
        super(i, i2);
    }

    public ConstraintWidgetContainer(String str, int i, int i2) {
        super(i, i2);
        setDebugName(str);
    }

    public void setOptimizationLevel(int i) {
        this.mOptimizationLevel = i;
        LinearSystem.USE_DEPENDENCY_ORDERING = optimizeFor(512);
    }

    public int getOptimizationLevel() {
        return this.mOptimizationLevel;
    }

    public boolean optimizeFor(int i) {
        return (this.mOptimizationLevel & i) == i;
    }

    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        this.mSkipSolver = false;
        super.reset();
    }

    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }

    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }

    /* access modifiers changed from: package-private */
    public void addVerticalWrapMinVariable(ConstraintAnchor constraintAnchor) {
        WeakReference<ConstraintAnchor> weakReference = this.verticalWrapMin;
        if (weakReference == null || weakReference.get() == null || constraintAnchor.getFinalValue() > ((ConstraintAnchor) this.verticalWrapMin.get()).getFinalValue()) {
            this.verticalWrapMin = new WeakReference<>(constraintAnchor);
        }
    }

    public void addHorizontalWrapMinVariable(ConstraintAnchor constraintAnchor) {
        WeakReference<ConstraintAnchor> weakReference = this.horizontalWrapMin;
        if (weakReference == null || weakReference.get() == null || constraintAnchor.getFinalValue() > ((ConstraintAnchor) this.horizontalWrapMin.get()).getFinalValue()) {
            this.horizontalWrapMin = new WeakReference<>(constraintAnchor);
        }
    }

    /* access modifiers changed from: package-private */
    public void addVerticalWrapMaxVariable(ConstraintAnchor constraintAnchor) {
        WeakReference<ConstraintAnchor> weakReference = this.verticalWrapMax;
        if (weakReference == null || weakReference.get() == null || constraintAnchor.getFinalValue() > ((ConstraintAnchor) this.verticalWrapMax.get()).getFinalValue()) {
            this.verticalWrapMax = new WeakReference<>(constraintAnchor);
        }
    }

    public void addHorizontalWrapMaxVariable(ConstraintAnchor constraintAnchor) {
        WeakReference<ConstraintAnchor> weakReference = this.horizontalWrapMax;
        if (weakReference == null || weakReference.get() == null || constraintAnchor.getFinalValue() > ((ConstraintAnchor) this.horizontalWrapMax.get()).getFinalValue()) {
            this.horizontalWrapMax = new WeakReference<>(constraintAnchor);
        }
    }

    private void addMinWrap(ConstraintAnchor constraintAnchor, SolverVariable solverVariable) {
        this.mSystem.addGreaterThan(this.mSystem.createObjectVariable(constraintAnchor), solverVariable, 0, 5);
    }

    private void addMaxWrap(ConstraintAnchor constraintAnchor, SolverVariable solverVariable) {
        this.mSystem.addGreaterThan(solverVariable, this.mSystem.createObjectVariable(constraintAnchor), 0, 5);
    }

    public boolean addChildrenToSolver(LinearSystem linearSystem) {
        boolean optimizeFor = optimizeFor(64);
        addToSolver(linearSystem, optimizeFor);
        int size = this.mChildren.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
            constraintWidget.setInBarrier(0, false);
            constraintWidget.setInBarrier(1, false);
            if (constraintWidget instanceof Barrier) {
                z = true;
            }
        }
        if (z) {
            for (int i2 = 0; i2 < size; i2++) {
                ConstraintWidget constraintWidget2 = (ConstraintWidget) this.mChildren.get(i2);
                if (constraintWidget2 instanceof Barrier) {
                    ((Barrier) constraintWidget2).markWidgets();
                }
            }
        }
        for (int i3 = 0; i3 < size; i3++) {
            ConstraintWidget constraintWidget3 = (ConstraintWidget) this.mChildren.get(i3);
            if (constraintWidget3.addFirst()) {
                constraintWidget3.addToSolver(linearSystem, optimizeFor);
            }
        }
        if (LinearSystem.USE_DEPENDENCY_ORDERING) {
            HashSet hashSet = new HashSet();
            for (int i4 = 0; i4 < size; i4++) {
                ConstraintWidget constraintWidget4 = (ConstraintWidget) this.mChildren.get(i4);
                if (!constraintWidget4.addFirst()) {
                    hashSet.add(constraintWidget4);
                }
            }
            addChildrenToSolverByDependency(this, linearSystem, hashSet, getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT ? 0 : 1, false);
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                ConstraintWidget constraintWidget5 = (ConstraintWidget) it.next();
                Optimizer.checkMatchParent(this, linearSystem, constraintWidget5);
                constraintWidget5.addToSolver(linearSystem, optimizeFor);
            }
        } else {
            for (int i5 = 0; i5 < size; i5++) {
                ConstraintWidget constraintWidget6 = (ConstraintWidget) this.mChildren.get(i5);
                if (constraintWidget6 instanceof ConstraintWidgetContainer) {
                    ConstraintWidget.DimensionBehaviour dimensionBehaviour = constraintWidget6.mListDimensionBehaviors[0];
                    ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget6.mListDimensionBehaviors[1];
                    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget6.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    }
                    if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget6.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                    }
                    constraintWidget6.addToSolver(linearSystem, optimizeFor);
                    if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget6.setHorizontalDimensionBehaviour(dimensionBehaviour);
                    }
                    if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget6.setVerticalDimensionBehaviour(dimensionBehaviour2);
                    }
                } else {
                    Optimizer.checkMatchParent(this, linearSystem, constraintWidget6);
                    if (!constraintWidget6.addFirst()) {
                        constraintWidget6.addToSolver(linearSystem, optimizeFor);
                    }
                }
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, (ArrayList<ConstraintWidget>) null, 0);
        }
        if (this.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, (ArrayList<ConstraintWidget>) null, 1);
        }
        return true;
    }

    public void updateChildrenFromSolver(LinearSystem linearSystem, boolean[] zArr) {
        zArr[2] = false;
        boolean optimizeFor = optimizeFor(64);
        updateFromSolver(linearSystem, optimizeFor);
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ((ConstraintWidget) this.mChildren.get(i)).updateFromSolver(linearSystem, optimizeFor);
        }
    }

    public void updateFromRuns(boolean z, boolean z2) {
        super.updateFromRuns(z, z2);
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ((ConstraintWidget) this.mChildren.get(i)).updateFromRuns(z, z2);
        }
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.mPaddingLeft = i;
        this.mPaddingTop = i2;
        this.mPaddingRight = i3;
        this.mPaddingBottom = i4;
    }

    public void setRtl(boolean z) {
        this.mIsRtl = z;
    }

    public boolean isRtl() {
        return this.mIsRtl;
    }

    public static boolean measure(ConstraintWidget constraintWidget, BasicMeasure.Measurer measurer, BasicMeasure.Measure measure, int i) {
        int i2;
        int i3;
        if (measurer == null) {
            return false;
        }
        measure.horizontalBehavior = constraintWidget.getHorizontalDimensionBehaviour();
        measure.verticalBehavior = constraintWidget.getVerticalDimensionBehaviour();
        measure.horizontalDimension = constraintWidget.getWidth();
        measure.verticalDimension = constraintWidget.getHeight();
        measure.measuredNeedsSolverPass = false;
        measure.measureStrategy = i;
        boolean z = measure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean z2 = measure.verticalBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        boolean z3 = z && constraintWidget.mDimensionRatio > 0.0f;
        boolean z4 = z2 && constraintWidget.mDimensionRatio > 0.0f;
        if (z && constraintWidget.hasDanglingDimension(0) && constraintWidget.mMatchConstraintDefaultWidth == 0 && !z3) {
            measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (z2 && constraintWidget.mMatchConstraintDefaultHeight == 0) {
                measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            }
            z = false;
        }
        if (z2 && constraintWidget.hasDanglingDimension(1) && constraintWidget.mMatchConstraintDefaultHeight == 0 && !z4) {
            measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
            if (z && constraintWidget.mMatchConstraintDefaultWidth == 0) {
                measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            }
            z2 = false;
        }
        if (constraintWidget.isResolvedHorizontally()) {
            measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            z = false;
        }
        if (constraintWidget.isResolvedVertically()) {
            measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            z2 = false;
        }
        if (z3) {
            if (constraintWidget.mResolvedMatchConstraintDefault[0] == 4) {
                measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            } else if (!z2) {
                if (measure.verticalBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    i3 = measure.verticalDimension;
                } else {
                    measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    measurer.measure(constraintWidget, measure);
                    i3 = measure.measuredHeight;
                }
                measure.horizontalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
                if (constraintWidget.mDimensionRatioSide == 0 || constraintWidget.mDimensionRatioSide == -1) {
                    measure.horizontalDimension = (int) (constraintWidget.getDimensionRatio() * ((float) i3));
                } else {
                    measure.horizontalDimension = (int) (constraintWidget.getDimensionRatio() / ((float) i3));
                }
            }
        }
        if (z4) {
            if (constraintWidget.mResolvedMatchConstraintDefault[1] == 4) {
                measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
            } else if (!z) {
                if (measure.horizontalBehavior == ConstraintWidget.DimensionBehaviour.FIXED) {
                    i2 = measure.horizontalDimension;
                } else {
                    measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    measurer.measure(constraintWidget, measure);
                    i2 = measure.measuredWidth;
                }
                measure.verticalBehavior = ConstraintWidget.DimensionBehaviour.FIXED;
                if (constraintWidget.mDimensionRatioSide == 0 || constraintWidget.mDimensionRatioSide == -1) {
                    measure.verticalDimension = (int) (((float) i2) / constraintWidget.getDimensionRatio());
                } else {
                    measure.verticalDimension = (int) (((float) i2) * constraintWidget.getDimensionRatio());
                }
            }
        }
        measurer.measure(constraintWidget, measure);
        constraintWidget.setWidth(measure.measuredWidth);
        constraintWidget.setHeight(measure.measuredHeight);
        constraintWidget.setHasBaseline(measure.measuredHasBaseline);
        constraintWidget.setBaselineDistance(measure.measuredBaseline);
        measure.measureStrategy = BasicMeasure.Measure.SELF_DIMENSIONS;
        return measure.measuredNeedsSolverPass;
    }

    /* JADX WARNING: type inference failed for: r6v5, types: [boolean] */
    /* JADX WARNING: type inference failed for: r6v7 */
    /* JADX WARNING: type inference failed for: r6v8 */
    public void layout() {
        int i;
        int i2;
        boolean z;
        boolean z2;
        ? r6;
        int i3 = 0;
        this.f35mX = 0;
        this.f36mY = 0;
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        int size = this.mChildren.size();
        int max = Math.max(0, getWidth());
        int max2 = Math.max(0, getHeight());
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = this.mListDimensionBehaviors[1];
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = this.mListDimensionBehaviors[0];
        Metrics metrics = this.mMetrics;
        if (metrics != null) {
            metrics.layouts++;
        }
        if (Optimizer.enabled(this.mOptimizationLevel, 1)) {
            Direct.solvingPass(this, getMeasurer());
            for (int i4 = 0; i4 < size; i4++) {
                ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i4);
                if (constraintWidget.isMeasureRequested() && !(constraintWidget instanceof Guideline) && !(constraintWidget instanceof Barrier) && !(constraintWidget instanceof VirtualLayout) && !constraintWidget.isInVirtualLayout()) {
                    if (!(constraintWidget.getDimensionBehaviour(0) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultWidth != 1 && constraintWidget.getDimensionBehaviour(1) == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultHeight != 1)) {
                        measure(constraintWidget, this.mMeasurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                    }
                }
            }
        }
        if (size <= 2 || (!(dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) || !Optimizer.enabled(this.mOptimizationLevel, 1024) || !Grouping.simpleSolvingPass(this, getMeasurer()))) {
            i = max2;
            i2 = max;
            z = false;
        } else {
            if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                if (max >= getWidth() || max <= 0) {
                    max = getWidth();
                } else {
                    setWidth(max);
                    this.mWidthMeasuredTooSmall = true;
                }
            }
            if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                if (max2 >= getHeight() || max2 <= 0) {
                    max2 = getHeight();
                } else {
                    setHeight(max2);
                    this.mHeightMeasuredTooSmall = true;
                }
            }
            i = max2;
            i2 = max;
            z = true;
        }
        boolean z3 = optimizeFor(64) || optimizeFor(128);
        this.mSystem.graphOptimizer = false;
        this.mSystem.newgraphOptimizer = false;
        if (this.mOptimizationLevel != 0 && z3) {
            this.mSystem.newgraphOptimizer = true;
        }
        ArrayList arrayList = this.mChildren;
        boolean z4 = getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        resetChains();
        for (int i5 = 0; i5 < size; i5++) {
            ConstraintWidget constraintWidget2 = (ConstraintWidget) this.mChildren.get(i5);
            if (constraintWidget2 instanceof WidgetContainer) {
                ((WidgetContainer) constraintWidget2).layout();
            }
        }
        boolean optimizeFor = optimizeFor(64);
        boolean z5 = z;
        int i6 = 0;
        boolean z6 = true;
        while (z6) {
            int i7 = i6 + 1;
            try {
                this.mSystem.reset();
                resetChains();
                createObjectVariables(this.mSystem);
                for (int i8 = i3; i8 < size; i8++) {
                    ((ConstraintWidget) this.mChildren.get(i8)).createObjectVariables(this.mSystem);
                }
                z6 = addChildrenToSolver(this.mSystem);
                WeakReference<ConstraintAnchor> weakReference = this.verticalWrapMin;
                if (!(weakReference == null || weakReference.get() == null)) {
                    addMinWrap((ConstraintAnchor) this.verticalWrapMin.get(), this.mSystem.createObjectVariable(this.mTop));
                    this.verticalWrapMin = null;
                }
                WeakReference<ConstraintAnchor> weakReference2 = this.verticalWrapMax;
                if (!(weakReference2 == null || weakReference2.get() == null)) {
                    addMaxWrap((ConstraintAnchor) this.verticalWrapMax.get(), this.mSystem.createObjectVariable(this.mBottom));
                    this.verticalWrapMax = null;
                }
                WeakReference<ConstraintAnchor> weakReference3 = this.horizontalWrapMin;
                if (!(weakReference3 == null || weakReference3.get() == null)) {
                    addMinWrap((ConstraintAnchor) this.horizontalWrapMin.get(), this.mSystem.createObjectVariable(this.mLeft));
                    this.horizontalWrapMin = null;
                }
                WeakReference<ConstraintAnchor> weakReference4 = this.horizontalWrapMax;
                if (!(weakReference4 == null || weakReference4.get() == null)) {
                    addMaxWrap((ConstraintAnchor) this.horizontalWrapMax.get(), this.mSystem.createObjectVariable(this.mRight));
                    this.horizontalWrapMax = null;
                }
                if (z6) {
                    this.mSystem.minimize();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("EXCEPTION : " + e);
            }
            if (z6) {
                updateChildrenFromSolver(this.mSystem, Optimizer.flags);
            } else {
                updateFromSolver(this.mSystem, optimizeFor);
                for (int i9 = 0; i9 < size; i9++) {
                    ((ConstraintWidget) this.mChildren.get(i9)).updateFromSolver(this.mSystem, optimizeFor);
                }
            }
            if (!z4 || i7 >= 8 || !Optimizer.flags[2]) {
                z2 = false;
            } else {
                int i10 = 0;
                int i11 = 0;
                for (int i12 = 0; i12 < size; i12++) {
                    ConstraintWidget constraintWidget3 = (ConstraintWidget) this.mChildren.get(i12);
                    i10 = Math.max(i10, constraintWidget3.f35mX + constraintWidget3.getWidth());
                    i11 = Math.max(i11, constraintWidget3.f36mY + constraintWidget3.getHeight());
                }
                int max3 = Math.max(this.mMinWidth, i10);
                int max4 = Math.max(this.mMinHeight, i11);
                if (dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || getWidth() >= max3) {
                    z2 = false;
                } else {
                    setWidth(max3);
                    this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    z2 = true;
                    z5 = true;
                }
                if (dimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && getHeight() < max4) {
                    setHeight(max4);
                    this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    z2 = true;
                    z5 = true;
                }
            }
            int max5 = Math.max(this.mMinWidth, getWidth());
            if (max5 > getWidth()) {
                setWidth(max5);
                this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                z2 = true;
                z5 = true;
            }
            int max6 = Math.max(this.mMinHeight, getHeight());
            if (max6 > getHeight()) {
                setHeight(max6);
                r6 = 1;
                this.mListDimensionBehaviors[1] = ConstraintWidget.DimensionBehaviour.FIXED;
                z2 = true;
                z5 = true;
            } else {
                r6 = 1;
            }
            if (!z5) {
                if (this.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && i2 > 0 && getWidth() > i2) {
                    this.mWidthMeasuredTooSmall = r6;
                    this.mListDimensionBehaviors[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                    setWidth(i2);
                    z2 = r6;
                    z5 = z2;
                }
                if (this.mListDimensionBehaviors[r6] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && i > 0 && getHeight() > i) {
                    this.mHeightMeasuredTooSmall = r6;
                    this.mListDimensionBehaviors[r6] = ConstraintWidget.DimensionBehaviour.FIXED;
                    setHeight(i);
                    z5 = true;
                    z6 = true;
                    i6 = i7;
                    i3 = 0;
                }
            }
            z6 = z2;
            i6 = i7;
            i3 = 0;
        }
        this.mChildren = arrayList;
        if (z5) {
            this.mListDimensionBehaviors[0] = dimensionBehaviour2;
            this.mListDimensionBehaviors[1] = dimensionBehaviour;
        }
        resetSolverVariables(this.mSystem.getCache());
    }

    public ArrayList<Guideline> getVerticalGuidelines() {
        ArrayList<Guideline> arrayList = new ArrayList<>();
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                Guideline guideline = (Guideline) constraintWidget;
                if (guideline.getOrientation() == 1) {
                    arrayList.add(guideline);
                }
            }
        }
        return arrayList;
    }

    public ArrayList<Guideline> getHorizontalGuidelines() {
        ArrayList<Guideline> arrayList = new ArrayList<>();
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                Guideline guideline = (Guideline) constraintWidget;
                if (guideline.getOrientation() == 0) {
                    arrayList.add(guideline);
                }
            }
        }
        return arrayList;
    }

    public LinearSystem getSystem() {
        return this.mSystem;
    }

    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }

    /* access modifiers changed from: package-private */
    public void addChain(ConstraintWidget constraintWidget, int i) {
        if (i == 0) {
            addHorizontalChain(constraintWidget);
        } else if (i == 1) {
            addVerticalChain(constraintWidget);
        }
    }

    private void addHorizontalChain(ConstraintWidget constraintWidget) {
        int i = this.mHorizontalChainsSize + 1;
        ChainHead[] chainHeadArr = this.mHorizontalChainsArray;
        if (i >= chainHeadArr.length) {
            this.mHorizontalChainsArray = (ChainHead[]) Arrays.copyOf(chainHeadArr, chainHeadArr.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(constraintWidget, 0, isRtl());
        this.mHorizontalChainsSize++;
    }

    private void addVerticalChain(ConstraintWidget constraintWidget) {
        int i = this.mVerticalChainsSize + 1;
        ChainHead[] chainHeadArr = this.mVerticalChainsArray;
        if (i >= chainHeadArr.length) {
            this.mVerticalChainsArray = (ChainHead[]) Arrays.copyOf(chainHeadArr, chainHeadArr.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(constraintWidget, 1, isRtl());
        this.mVerticalChainsSize++;
    }
}
