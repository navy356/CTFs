package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.Barrier;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import java.util.ArrayList;
import java.util.Iterator;

public class Direct {
    private static final boolean APPLY_MATCH_PARENT = false;
    private static final boolean DEBUG = false;
    private static BasicMeasure.Measure measure = new BasicMeasure.Measure();

    public static void solvingPass(ConstraintWidgetContainer constraintWidgetContainer, BasicMeasure.Measurer measurer) {
        ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = constraintWidgetContainer.getHorizontalDimensionBehaviour();
        ConstraintWidget.DimensionBehaviour verticalDimensionBehaviour = constraintWidgetContainer.getVerticalDimensionBehaviour();
        constraintWidgetContainer.resetFinalResolution();
        ArrayList<ConstraintWidget> children = constraintWidgetContainer.getChildren();
        int size = children.size();
        for (int i = 0; i < size; i++) {
            children.get(i).resetFinalResolution();
        }
        boolean isRtl = constraintWidgetContainer.isRtl();
        if (horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED) {
            constraintWidgetContainer.setFinalHorizontal(0, constraintWidgetContainer.getWidth());
        } else {
            constraintWidgetContainer.setFinalLeft(0);
        }
        boolean z = false;
        boolean z2 = false;
        for (int i2 = 0; i2 < size; i2++) {
            ConstraintWidget constraintWidget = children.get(i2);
            if (constraintWidget instanceof Guideline) {
                Guideline guideline = (Guideline) constraintWidget;
                if (guideline.getOrientation() == 1) {
                    if (guideline.getRelativeBegin() != -1) {
                        guideline.setFinalValue(guideline.getRelativeBegin());
                    } else if (guideline.getRelativeEnd() != -1 && constraintWidgetContainer.isResolvedHorizontally()) {
                        guideline.setFinalValue(constraintWidgetContainer.getWidth() - guideline.getRelativeEnd());
                    } else if (constraintWidgetContainer.isResolvedHorizontally()) {
                        guideline.setFinalValue((int) ((guideline.getRelativePercent() * ((float) constraintWidgetContainer.getWidth())) + 0.5f));
                    }
                    z = true;
                }
            } else if ((constraintWidget instanceof Barrier) && ((Barrier) constraintWidget).getOrientation() == 0) {
                z2 = true;
            }
        }
        if (z) {
            for (int i3 = 0; i3 < size; i3++) {
                ConstraintWidget constraintWidget2 = children.get(i3);
                if (constraintWidget2 instanceof Guideline) {
                    Guideline guideline2 = (Guideline) constraintWidget2;
                    if (guideline2.getOrientation() == 1) {
                        horizontalSolvingPass(guideline2, measurer, isRtl);
                    }
                }
            }
        }
        horizontalSolvingPass(constraintWidgetContainer, measurer, isRtl);
        if (z2) {
            for (int i4 = 0; i4 < size; i4++) {
                ConstraintWidget constraintWidget3 = children.get(i4);
                if (constraintWidget3 instanceof Barrier) {
                    Barrier barrier = (Barrier) constraintWidget3;
                    if (barrier.getOrientation() == 0) {
                        solveBarrier(barrier, measurer, 0, isRtl);
                    }
                }
            }
        }
        if (verticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED) {
            constraintWidgetContainer.setFinalVertical(0, constraintWidgetContainer.getHeight());
        } else {
            constraintWidgetContainer.setFinalTop(0);
        }
        boolean z3 = false;
        boolean z4 = false;
        for (int i5 = 0; i5 < size; i5++) {
            ConstraintWidget constraintWidget4 = children.get(i5);
            if (constraintWidget4 instanceof Guideline) {
                Guideline guideline3 = (Guideline) constraintWidget4;
                if (guideline3.getOrientation() == 0) {
                    if (guideline3.getRelativeBegin() != -1) {
                        guideline3.setFinalValue(guideline3.getRelativeBegin());
                    } else if (guideline3.getRelativeEnd() != -1 && constraintWidgetContainer.isResolvedVertically()) {
                        guideline3.setFinalValue(constraintWidgetContainer.getHeight() - guideline3.getRelativeEnd());
                    } else if (constraintWidgetContainer.isResolvedVertically()) {
                        guideline3.setFinalValue((int) ((guideline3.getRelativePercent() * ((float) constraintWidgetContainer.getHeight())) + 0.5f));
                    }
                    z3 = true;
                }
            } else if ((constraintWidget4 instanceof Barrier) && ((Barrier) constraintWidget4).getOrientation() == 1) {
                z4 = true;
            }
        }
        if (z3) {
            for (int i6 = 0; i6 < size; i6++) {
                ConstraintWidget constraintWidget5 = children.get(i6);
                if (constraintWidget5 instanceof Guideline) {
                    Guideline guideline4 = (Guideline) constraintWidget5;
                    if (guideline4.getOrientation() == 0) {
                        verticalSolvingPass(guideline4, measurer);
                    }
                }
            }
        }
        verticalSolvingPass(constraintWidgetContainer, measurer);
        if (z4) {
            for (int i7 = 0; i7 < size; i7++) {
                ConstraintWidget constraintWidget6 = children.get(i7);
                if (constraintWidget6 instanceof Barrier) {
                    Barrier barrier2 = (Barrier) constraintWidget6;
                    if (barrier2.getOrientation() == 1) {
                        solveBarrier(barrier2, measurer, 1, isRtl);
                    }
                }
            }
        }
        for (int i8 = 0; i8 < size; i8++) {
            ConstraintWidget constraintWidget7 = children.get(i8);
            if (constraintWidget7.isMeasureRequested() && canMeasure(constraintWidget7)) {
                ConstraintWidgetContainer.measure(constraintWidget7, measurer, measure, BasicMeasure.Measure.SELF_DIMENSIONS);
                horizontalSolvingPass(constraintWidget7, measurer, isRtl);
                verticalSolvingPass(constraintWidget7, measurer);
            }
        }
    }

    private static void solveBarrier(Barrier barrier, BasicMeasure.Measurer measurer, int i, boolean z) {
        if (!barrier.allSolved()) {
            return;
        }
        if (i == 0) {
            horizontalSolvingPass(barrier, measurer, z);
        } else {
            verticalSolvingPass(barrier, measurer);
        }
    }

    private static void horizontalSolvingPass(ConstraintWidget constraintWidget, BasicMeasure.Measurer measurer, boolean z) {
        if (!(constraintWidget instanceof ConstraintWidgetContainer) && constraintWidget.isMeasureRequested() && canMeasure(constraintWidget)) {
            ConstraintWidgetContainer.measure(constraintWidget, measurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
        }
        ConstraintAnchor anchor = constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT);
        ConstraintAnchor anchor2 = constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT);
        int finalValue = anchor.getFinalValue();
        int finalValue2 = anchor2.getFinalValue();
        if (anchor.getDependents() != null && anchor.hasFinalValue()) {
            Iterator<ConstraintAnchor> it = anchor.getDependents().iterator();
            while (it.hasNext()) {
                ConstraintAnchor next = it.next();
                ConstraintWidget constraintWidget2 = next.mOwner;
                boolean canMeasure = canMeasure(constraintWidget2);
                if (constraintWidget2.isMeasureRequested() && canMeasure) {
                    ConstraintWidgetContainer.measure(constraintWidget2, measurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                }
                if (constraintWidget2.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || canMeasure) {
                    if (!constraintWidget2.isMeasureRequested()) {
                        if (next == constraintWidget2.mLeft && constraintWidget2.mRight.mTarget == null) {
                            int margin = constraintWidget2.mLeft.getMargin() + finalValue;
                            constraintWidget2.setFinalHorizontal(margin, constraintWidget2.getWidth() + margin);
                            horizontalSolvingPass(constraintWidget2, measurer, z);
                        } else if (next == constraintWidget2.mRight && constraintWidget2.mLeft.mTarget == null) {
                            int margin2 = finalValue - constraintWidget2.mRight.getMargin();
                            constraintWidget2.setFinalHorizontal(margin2 - constraintWidget2.getWidth(), margin2);
                            horizontalSolvingPass(constraintWidget2, measurer, z);
                        } else if (next == constraintWidget2.mLeft && constraintWidget2.mRight.mTarget != null && constraintWidget2.mRight.mTarget.hasFinalValue() && !constraintWidget2.isInHorizontalChain()) {
                            solveHorizontalCenterConstraints(measurer, constraintWidget2, z);
                        }
                    }
                } else if (constraintWidget2.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget2.mMatchConstraintMaxWidth >= 0 && constraintWidget2.mMatchConstraintMinWidth >= 0) {
                    if ((constraintWidget2.getVisibility() == 8 || (constraintWidget2.mMatchConstraintDefaultWidth == 0 && constraintWidget2.getDimensionRatio() == 0.0f)) && !constraintWidget2.isInHorizontalChain() && !constraintWidget2.isInVirtualLayout()) {
                        if (((next == constraintWidget2.mLeft && constraintWidget2.mRight.mTarget != null && constraintWidget2.mRight.mTarget.hasFinalValue()) || (next == constraintWidget2.mRight && constraintWidget2.mLeft.mTarget != null && constraintWidget2.mLeft.mTarget.hasFinalValue())) && !constraintWidget2.isInHorizontalChain()) {
                            solveHorizontalMatchConstraint(constraintWidget, measurer, constraintWidget2, z);
                        }
                    }
                }
            }
        }
        if (!(constraintWidget instanceof Guideline) && anchor2.getDependents() != null && anchor2.hasFinalValue()) {
            Iterator<ConstraintAnchor> it2 = anchor2.getDependents().iterator();
            while (it2.hasNext()) {
                ConstraintAnchor next2 = it2.next();
                ConstraintWidget constraintWidget3 = next2.mOwner;
                boolean canMeasure2 = canMeasure(constraintWidget3);
                if (constraintWidget3.isMeasureRequested() && canMeasure2) {
                    ConstraintWidgetContainer.measure(constraintWidget3, measurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                }
                boolean z2 = (next2 == constraintWidget3.mLeft && constraintWidget3.mRight.mTarget != null && constraintWidget3.mRight.mTarget.hasFinalValue()) || (next2 == constraintWidget3.mRight && constraintWidget3.mLeft.mTarget != null && constraintWidget3.mLeft.mTarget.hasFinalValue());
                if (constraintWidget3.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || canMeasure2) {
                    if (!constraintWidget3.isMeasureRequested()) {
                        if (next2 == constraintWidget3.mLeft && constraintWidget3.mRight.mTarget == null) {
                            int margin3 = constraintWidget3.mLeft.getMargin() + finalValue2;
                            constraintWidget3.setFinalHorizontal(margin3, constraintWidget3.getWidth() + margin3);
                            horizontalSolvingPass(constraintWidget3, measurer, z);
                        } else if (next2 == constraintWidget3.mRight && constraintWidget3.mLeft.mTarget == null) {
                            int margin4 = finalValue2 - constraintWidget3.mRight.getMargin();
                            constraintWidget3.setFinalHorizontal(margin4 - constraintWidget3.getWidth(), margin4);
                            horizontalSolvingPass(constraintWidget3, measurer, z);
                        } else if (z2 && !constraintWidget3.isInHorizontalChain()) {
                            solveHorizontalCenterConstraints(measurer, constraintWidget3, z);
                        }
                    }
                } else if (constraintWidget3.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget3.mMatchConstraintMaxWidth >= 0 && constraintWidget3.mMatchConstraintMinWidth >= 0) {
                    if ((constraintWidget3.getVisibility() == 8 || (constraintWidget3.mMatchConstraintDefaultWidth == 0 && constraintWidget3.getDimensionRatio() == 0.0f)) && !constraintWidget3.isInHorizontalChain() && !constraintWidget3.isInVirtualLayout() && z2 && !constraintWidget3.isInHorizontalChain()) {
                        solveHorizontalMatchConstraint(constraintWidget, measurer, constraintWidget3, z);
                    }
                }
            }
        }
    }

    private static void verticalSolvingPass(ConstraintWidget constraintWidget, BasicMeasure.Measurer measurer) {
        if (!(constraintWidget instanceof ConstraintWidgetContainer) && constraintWidget.isMeasureRequested() && canMeasure(constraintWidget)) {
            ConstraintWidgetContainer.measure(constraintWidget, measurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
        }
        ConstraintAnchor anchor = constraintWidget.getAnchor(ConstraintAnchor.Type.TOP);
        ConstraintAnchor anchor2 = constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM);
        int finalValue = anchor.getFinalValue();
        int finalValue2 = anchor2.getFinalValue();
        if (anchor.getDependents() != null && anchor.hasFinalValue()) {
            Iterator<ConstraintAnchor> it = anchor.getDependents().iterator();
            while (it.hasNext()) {
                ConstraintAnchor next = it.next();
                ConstraintWidget constraintWidget2 = next.mOwner;
                boolean canMeasure = canMeasure(constraintWidget2);
                if (constraintWidget2.isMeasureRequested() && canMeasure) {
                    ConstraintWidgetContainer.measure(constraintWidget2, measurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                }
                if (constraintWidget2.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || canMeasure) {
                    if (!constraintWidget2.isMeasureRequested()) {
                        if (next == constraintWidget2.mTop && constraintWidget2.mBottom.mTarget == null) {
                            int margin = constraintWidget2.mTop.getMargin() + finalValue;
                            constraintWidget2.setFinalVertical(margin, constraintWidget2.getHeight() + margin);
                            verticalSolvingPass(constraintWidget2, measurer);
                        } else if (next == constraintWidget2.mBottom && constraintWidget2.mBottom.mTarget == null) {
                            int margin2 = finalValue - constraintWidget2.mBottom.getMargin();
                            constraintWidget2.setFinalVertical(margin2 - constraintWidget2.getHeight(), margin2);
                            verticalSolvingPass(constraintWidget2, measurer);
                        } else if (next == constraintWidget2.mTop && constraintWidget2.mBottom.mTarget != null && constraintWidget2.mBottom.mTarget.hasFinalValue()) {
                            solveVerticalCenterConstraints(measurer, constraintWidget2);
                        }
                    }
                } else if (constraintWidget2.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget2.mMatchConstraintMaxHeight >= 0 && constraintWidget2.mMatchConstraintMinHeight >= 0) {
                    if ((constraintWidget2.getVisibility() == 8 || (constraintWidget2.mMatchConstraintDefaultHeight == 0 && constraintWidget2.getDimensionRatio() == 0.0f)) && !constraintWidget2.isInVerticalChain() && !constraintWidget2.isInVirtualLayout()) {
                        if (((next == constraintWidget2.mTop && constraintWidget2.mBottom.mTarget != null && constraintWidget2.mBottom.mTarget.hasFinalValue()) || (next == constraintWidget2.mBottom && constraintWidget2.mTop.mTarget != null && constraintWidget2.mTop.mTarget.hasFinalValue())) && !constraintWidget2.isInVerticalChain()) {
                            solveVerticalMatchConstraint(constraintWidget, measurer, constraintWidget2);
                        }
                    }
                }
            }
        }
        if (!(constraintWidget instanceof Guideline)) {
            if (anchor2.getDependents() != null && anchor2.hasFinalValue()) {
                Iterator<ConstraintAnchor> it2 = anchor2.getDependents().iterator();
                while (it2.hasNext()) {
                    ConstraintAnchor next2 = it2.next();
                    ConstraintWidget constraintWidget3 = next2.mOwner;
                    boolean canMeasure2 = canMeasure(constraintWidget3);
                    if (constraintWidget3.isMeasureRequested() && canMeasure2) {
                        ConstraintWidgetContainer.measure(constraintWidget3, measurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                    }
                    boolean z = (next2 == constraintWidget3.mTop && constraintWidget3.mBottom.mTarget != null && constraintWidget3.mBottom.mTarget.hasFinalValue()) || (next2 == constraintWidget3.mBottom && constraintWidget3.mTop.mTarget != null && constraintWidget3.mTop.mTarget.hasFinalValue());
                    if (constraintWidget3.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || canMeasure2) {
                        if (!constraintWidget3.isMeasureRequested()) {
                            if (next2 == constraintWidget3.mTop && constraintWidget3.mBottom.mTarget == null) {
                                int margin3 = constraintWidget3.mTop.getMargin() + finalValue2;
                                constraintWidget3.setFinalVertical(margin3, constraintWidget3.getHeight() + margin3);
                                verticalSolvingPass(constraintWidget3, measurer);
                            } else if (next2 == constraintWidget3.mBottom && constraintWidget3.mTop.mTarget == null) {
                                int margin4 = finalValue2 - constraintWidget3.mBottom.getMargin();
                                constraintWidget3.setFinalVertical(margin4 - constraintWidget3.getHeight(), margin4);
                                verticalSolvingPass(constraintWidget3, measurer);
                            } else if (z && !constraintWidget3.isInVerticalChain()) {
                                solveVerticalCenterConstraints(measurer, constraintWidget3);
                            }
                        }
                    } else if (constraintWidget3.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget3.mMatchConstraintMaxHeight >= 0 && constraintWidget3.mMatchConstraintMinHeight >= 0) {
                        if ((constraintWidget3.getVisibility() == 8 || (constraintWidget3.mMatchConstraintDefaultHeight == 0 && constraintWidget3.getDimensionRatio() == 0.0f)) && !constraintWidget3.isInVerticalChain() && !constraintWidget3.isInVirtualLayout() && z && !constraintWidget3.isInVerticalChain()) {
                            solveVerticalMatchConstraint(constraintWidget, measurer, constraintWidget3);
                        }
                    }
                }
            }
            ConstraintAnchor anchor3 = constraintWidget.getAnchor(ConstraintAnchor.Type.BASELINE);
            if (anchor3.getDependents() != null && anchor3.hasFinalValue()) {
                int finalValue3 = anchor3.getFinalValue();
                Iterator<ConstraintAnchor> it3 = anchor3.getDependents().iterator();
                while (it3.hasNext()) {
                    ConstraintAnchor next3 = it3.next();
                    ConstraintWidget constraintWidget4 = next3.mOwner;
                    boolean canMeasure3 = canMeasure(constraintWidget4);
                    if (constraintWidget4.isMeasureRequested() && canMeasure3) {
                        ConstraintWidgetContainer.measure(constraintWidget4, measurer, new BasicMeasure.Measure(), BasicMeasure.Measure.SELF_DIMENSIONS);
                    }
                    if ((constraintWidget4.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || canMeasure3) && !constraintWidget4.isMeasureRequested() && next3 == constraintWidget4.mBaseline) {
                        constraintWidget4.setFinalBaseline(finalValue3);
                        verticalSolvingPass(constraintWidget4, measurer);
                    }
                }
            }
        }
    }

    private static void solveHorizontalCenterConstraints(BasicMeasure.Measurer measurer, ConstraintWidget constraintWidget, boolean z) {
        float horizontalBiasPercent = constraintWidget.getHorizontalBiasPercent();
        int finalValue = constraintWidget.mLeft.mTarget.getFinalValue();
        int finalValue2 = constraintWidget.mRight.mTarget.getFinalValue();
        int margin = constraintWidget.mLeft.getMargin() + finalValue;
        int margin2 = finalValue2 - constraintWidget.mRight.getMargin();
        if (finalValue == finalValue2) {
            horizontalBiasPercent = 0.5f;
        } else {
            finalValue = margin;
            finalValue2 = margin2;
        }
        int width = constraintWidget.getWidth();
        int i = (finalValue2 - finalValue) - width;
        if (finalValue > finalValue2) {
            i = (finalValue - finalValue2) - width;
        }
        int i2 = ((int) ((horizontalBiasPercent * ((float) i)) + 0.5f)) + finalValue;
        int i3 = i2 + width;
        if (finalValue > finalValue2) {
            i3 = i2 - width;
        }
        constraintWidget.setFinalHorizontal(i2, i3);
        horizontalSolvingPass(constraintWidget, measurer, z);
    }

    private static void solveVerticalCenterConstraints(BasicMeasure.Measurer measurer, ConstraintWidget constraintWidget) {
        float verticalBiasPercent = constraintWidget.getVerticalBiasPercent();
        int finalValue = constraintWidget.mTop.mTarget.getFinalValue();
        int finalValue2 = constraintWidget.mBottom.mTarget.getFinalValue();
        int margin = constraintWidget.mTop.getMargin() + finalValue;
        int margin2 = finalValue2 - constraintWidget.mBottom.getMargin();
        if (finalValue == finalValue2) {
            verticalBiasPercent = 0.5f;
        } else {
            finalValue = margin;
            finalValue2 = margin2;
        }
        int height = constraintWidget.getHeight();
        int i = (finalValue2 - finalValue) - height;
        if (finalValue > finalValue2) {
            i = (finalValue - finalValue2) - height;
        }
        int i2 = (int) ((verticalBiasPercent * ((float) i)) + 0.5f);
        int i3 = finalValue + i2;
        int i4 = i3 + height;
        if (finalValue > finalValue2) {
            i3 = finalValue - i2;
            i4 = i3 - height;
        }
        constraintWidget.setFinalVertical(i3, i4);
        verticalSolvingPass(constraintWidget, measurer);
    }

    private static void solveHorizontalMatchConstraint(ConstraintWidget constraintWidget, BasicMeasure.Measurer measurer, ConstraintWidget constraintWidget2, boolean z) {
        int i;
        float horizontalBiasPercent = constraintWidget2.getHorizontalBiasPercent();
        int finalValue = constraintWidget2.mLeft.mTarget.getFinalValue() + constraintWidget2.mLeft.getMargin();
        int finalValue2 = constraintWidget2.mRight.mTarget.getFinalValue() - constraintWidget2.mRight.getMargin();
        if (finalValue2 >= finalValue) {
            int width = constraintWidget2.getWidth();
            if (constraintWidget2.getVisibility() != 8) {
                if (constraintWidget2.mMatchConstraintDefaultWidth == 2) {
                    if (constraintWidget instanceof ConstraintWidgetContainer) {
                        i = constraintWidget.getWidth();
                    } else {
                        i = constraintWidget.getParent().getWidth();
                    }
                    width = (int) (constraintWidget2.getHorizontalBiasPercent() * 0.5f * ((float) i));
                } else if (constraintWidget2.mMatchConstraintDefaultWidth == 0) {
                    width = finalValue2 - finalValue;
                }
                width = Math.max(constraintWidget2.mMatchConstraintMinWidth, width);
                if (constraintWidget2.mMatchConstraintMaxWidth > 0) {
                    width = Math.min(constraintWidget2.mMatchConstraintMaxWidth, width);
                }
            }
            int i2 = finalValue + ((int) ((horizontalBiasPercent * ((float) ((finalValue2 - finalValue) - width))) + 0.5f));
            constraintWidget2.setFinalHorizontal(i2, width + i2);
            horizontalSolvingPass(constraintWidget2, measurer, z);
        }
    }

    private static void solveVerticalMatchConstraint(ConstraintWidget constraintWidget, BasicMeasure.Measurer measurer, ConstraintWidget constraintWidget2) {
        int i;
        float verticalBiasPercent = constraintWidget2.getVerticalBiasPercent();
        int finalValue = constraintWidget2.mTop.mTarget.getFinalValue() + constraintWidget2.mTop.getMargin();
        int finalValue2 = constraintWidget2.mBottom.mTarget.getFinalValue() - constraintWidget2.mBottom.getMargin();
        if (finalValue2 >= finalValue) {
            int height = constraintWidget2.getHeight();
            if (constraintWidget2.getVisibility() != 8) {
                if (constraintWidget2.mMatchConstraintDefaultHeight == 2) {
                    if (constraintWidget instanceof ConstraintWidgetContainer) {
                        i = constraintWidget.getHeight();
                    } else {
                        i = constraintWidget.getParent().getHeight();
                    }
                    height = (int) (verticalBiasPercent * 0.5f * ((float) i));
                } else if (constraintWidget2.mMatchConstraintDefaultHeight == 0) {
                    height = finalValue2 - finalValue;
                }
                height = Math.max(constraintWidget2.mMatchConstraintMinHeight, height);
                if (constraintWidget2.mMatchConstraintMaxHeight > 0) {
                    height = Math.min(constraintWidget2.mMatchConstraintMaxHeight, height);
                }
            }
            int i2 = finalValue + ((int) ((verticalBiasPercent * ((float) ((finalValue2 - finalValue) - height))) + 0.5f));
            constraintWidget2.setFinalVertical(i2, height + i2);
            verticalSolvingPass(constraintWidget2, measurer);
        }
    }

    private static boolean canMeasure(ConstraintWidget constraintWidget) {
        ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour = constraintWidget.getHorizontalDimensionBehaviour();
        ConstraintWidget.DimensionBehaviour verticalDimensionBehaviour = constraintWidget.getVerticalDimensionBehaviour();
        ConstraintWidgetContainer constraintWidgetContainer = constraintWidget.getParent() != null ? (ConstraintWidgetContainer) constraintWidget.getParent() : null;
        if (constraintWidgetContainer != null) {
            ConstraintWidget.DimensionBehaviour horizontalDimensionBehaviour2 = constraintWidgetContainer.getHorizontalDimensionBehaviour();
            ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        if (constraintWidgetContainer != null) {
            ConstraintWidget.DimensionBehaviour verticalDimensionBehaviour2 = constraintWidgetContainer.getVerticalDimensionBehaviour();
            ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.FIXED;
        }
        boolean z = horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED || horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (horizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultWidth == 0 && constraintWidget.mDimensionRatio == 0.0f && constraintWidget.hasDanglingDimension(0)) || constraintWidget.isResolvedHorizontally();
        boolean z2 = verticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.FIXED || verticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (verticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mMatchConstraintDefaultHeight == 0 && constraintWidget.mDimensionRatio == 0.0f && constraintWidget.hasDanglingDimension(1)) || constraintWidget.isResolvedVertically();
        if (constraintWidget.mDimensionRatio > 0.0f && (z || z2)) {
            return true;
        }
        if (!z || !z2) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0058, code lost:
        r7 = r7.mTarget.getFinalValue() + r4.mListAnchors[r21].getMargin();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x01da, code lost:
        if (r5.mListAnchors[r21].mTarget.mOwner == r2) goto L_0x01de;
     */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0150  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean solveChain(androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r18, androidx.constraintlayout.solver.LinearSystem r19, int r20, int r21, androidx.constraintlayout.solver.widgets.ChainHead r22, boolean r23, boolean r24, boolean r25) {
        /*
            r0 = 0
            if (r25 == 0) goto L_0x0004
            return r0
        L_0x0004:
            if (r20 != 0) goto L_0x000d
            boolean r1 = r18.isResolvedHorizontally()
            if (r1 != 0) goto L_0x0014
            return r0
        L_0x000d:
            boolean r1 = r18.isResolvedVertically()
            if (r1 != 0) goto L_0x0014
            return r0
        L_0x0014:
            boolean r1 = r18.isRtl()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r22.getFirst()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r22.getLast()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r4 = r22.getFirstVisibleWidget()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r22.getLastVisibleWidget()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r6 = r22.getHead()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r7 = r2.mListAnchors
            r7 = r7[r21]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r3 = r3.mListAnchors
            int r8 = r21 + 1
            r3 = r3[r8]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r7.mTarget
            if (r9 == 0) goto L_0x0230
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r3.mTarget
            if (r9 != 0) goto L_0x0040
            goto L_0x0230
        L_0x0040:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r7.mTarget
            boolean r9 = r9.hasFinalValue()
            if (r9 == 0) goto L_0x0230
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r9 = r3.mTarget
            boolean r9 = r9.hasFinalValue()
            if (r9 != 0) goto L_0x0052
            goto L_0x0230
        L_0x0052:
            if (r4 == 0) goto L_0x0230
            if (r5 != 0) goto L_0x0058
            goto L_0x0230
        L_0x0058:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r7 = r7.mTarget
            int r7 = r7.getFinalValue()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r9 = r4.mListAnchors
            r9 = r9[r21]
            int r9 = r9.getMargin()
            int r7 = r7 + r9
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            int r3 = r3.getFinalValue()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r9 = r5.mListAnchors
            r9 = r9[r8]
            int r9 = r9.getMargin()
            int r3 = r3 - r9
            int r9 = r3 - r7
            if (r9 > 0) goto L_0x007b
            return r0
        L_0x007b:
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure r10 = new androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure
            r10.<init>()
            r11 = r0
            r12 = r11
            r14 = r12
            r15 = r14
            r13 = r2
        L_0x0085:
            r16 = 0
            if (r11 != 0) goto L_0x0102
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r13.mListAnchors
            r0 = r0[r21]
            boolean r0 = canMeasure(r13)
            if (r0 != 0) goto L_0x0095
            r0 = 0
            return r0
        L_0x0095:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r0 = r13.mListDimensionBehaviors
            r0 = r0[r20]
            r17 = r2
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r0 != r2) goto L_0x00a1
            r0 = 0
            return r0
        L_0x00a1:
            boolean r0 = r13.isMeasureRequested()
            if (r0 == 0) goto L_0x00b0
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r0 = r18.getMeasurer()
            int r2 = androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measure.SELF_DIMENSIONS
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer.measure(r13, r0, r10, r2)
        L_0x00b0:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r13.mListAnchors
            r0 = r0[r21]
            int r0 = r0.getMargin()
            int r15 = r15 + r0
            if (r20 != 0) goto L_0x00c0
            int r0 = r13.getWidth()
            goto L_0x00c4
        L_0x00c0:
            int r0 = r13.getHeight()
        L_0x00c4:
            int r15 = r15 + r0
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r13.mListAnchors
            r0 = r0[r8]
            int r0 = r0.getMargin()
            int r15 = r15 + r0
            int r14 = r14 + 1
            int r0 = r13.getVisibility()
            r2 = 8
            if (r0 == r2) goto L_0x00da
            int r12 = r12 + 1
        L_0x00da:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r0 = r13.mListAnchors
            r0 = r0[r8]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            if (r0 == 0) goto L_0x00f9
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r0.mOwner
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r0.mListAnchors
            r2 = r2[r21]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            if (r2 == 0) goto L_0x00f9
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r2 = r0.mListAnchors
            r2 = r2[r21]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r2.mOwner
            if (r2 == r13) goto L_0x00f7
            goto L_0x00f9
        L_0x00f7:
            r16 = r0
        L_0x00f9:
            if (r16 == 0) goto L_0x00fe
            r13 = r16
            goto L_0x00ff
        L_0x00fe:
            r11 = 1
        L_0x00ff:
            r2 = r17
            goto L_0x0085
        L_0x0102:
            r17 = r2
            if (r12 != 0) goto L_0x0108
            r0 = 0
            return r0
        L_0x0108:
            r0 = 0
            if (r12 == r14) goto L_0x010c
            return r0
        L_0x010c:
            if (r9 >= r15) goto L_0x010f
            return r0
        L_0x010f:
            int r9 = r9 - r15
            r0 = 2
            if (r23 == 0) goto L_0x0118
            int r2 = r12 + 1
            int r9 = r9 / r2
        L_0x0116:
            r2 = 1
            goto L_0x011f
        L_0x0118:
            if (r24 == 0) goto L_0x0116
            if (r12 <= r0) goto L_0x0116
            int r9 = r9 / r12
            r2 = 1
            int r9 = r9 - r2
        L_0x011f:
            if (r12 != r2) goto L_0x0150
            if (r20 != 0) goto L_0x0128
            float r0 = r6.getHorizontalBiasPercent()
            goto L_0x012c
        L_0x0128:
            float r0 = r6.getVerticalBiasPercent()
        L_0x012c:
            r2 = 1056964608(0x3f000000, float:0.5)
            float r3 = (float) r7
            float r3 = r3 + r2
            float r2 = (float) r9
            float r2 = r2 * r0
            float r3 = r3 + r2
            int r0 = (int) r3
            if (r20 != 0) goto L_0x013f
            int r2 = r4.getWidth()
            int r2 = r2 + r0
            r4.setFinalHorizontal(r0, r2)
            goto L_0x0147
        L_0x013f:
            int r2 = r4.getHeight()
            int r2 = r2 + r0
            r4.setFinalVertical(r0, r2)
        L_0x0147:
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r0 = r18.getMeasurer()
            horizontalSolvingPass(r4, r0, r1)
            r0 = 1
            return r0
        L_0x0150:
            if (r23 == 0) goto L_0x01e6
            int r7 = r7 + r9
            r2 = r17
            r0 = 0
        L_0x0156:
            if (r0 != 0) goto L_0x022f
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r3 = r2.mListAnchors
            r3 = r3[r21]
            int r3 = r2.getVisibility()
            r4 = 8
            if (r3 != r4) goto L_0x017f
            if (r20 != 0) goto L_0x0171
            r2.setFinalHorizontal(r7, r7)
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r3 = r18.getMeasurer()
            horizontalSolvingPass(r2, r3, r1)
            goto L_0x017b
        L_0x0171:
            r2.setFinalVertical(r7, r7)
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r3 = r18.getMeasurer()
            verticalSolvingPass(r2, r3)
        L_0x017b:
            r3 = r19
            r5 = 0
            goto L_0x01bd
        L_0x017f:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r3 = r2.mListAnchors
            r3 = r3[r21]
            int r3 = r3.getMargin()
            int r7 = r7 + r3
            if (r20 != 0) goto L_0x019e
            int r3 = r2.getWidth()
            int r3 = r3 + r7
            r2.setFinalHorizontal(r7, r3)
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r3 = r18.getMeasurer()
            horizontalSolvingPass(r2, r3, r1)
            int r3 = r2.getWidth()
            goto L_0x01b1
        L_0x019e:
            int r3 = r2.getHeight()
            int r3 = r3 + r7
            r2.setFinalVertical(r7, r3)
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r3 = r18.getMeasurer()
            verticalSolvingPass(r2, r3)
            int r3 = r2.getHeight()
        L_0x01b1:
            int r7 = r7 + r3
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r3 = r2.mListAnchors
            r3 = r3[r8]
            int r3 = r3.getMargin()
            int r7 = r7 + r3
            int r7 = r7 + r9
            goto L_0x017b
        L_0x01bd:
            r2.addToSolver(r3, r5)
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r5 = r2.mListAnchors
            r5 = r5[r8]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = r5.mTarget
            if (r5 == 0) goto L_0x01dc
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r5.mOwner
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r6 = r5.mListAnchors
            r6 = r6[r21]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r6 = r6.mTarget
            if (r6 == 0) goto L_0x01dc
            androidx.constraintlayout.solver.widgets.ConstraintAnchor[] r6 = r5.mListAnchors
            r6 = r6[r21]
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r6 = r6.mTarget
            androidx.constraintlayout.solver.widgets.ConstraintWidget r6 = r6.mOwner
            if (r6 == r2) goto L_0x01de
        L_0x01dc:
            r5 = r16
        L_0x01de:
            if (r5 == 0) goto L_0x01e3
            r2 = r5
            goto L_0x0156
        L_0x01e3:
            r0 = 1
            goto L_0x0156
        L_0x01e6:
            if (r24 == 0) goto L_0x022f
            if (r12 != r0) goto L_0x022d
            if (r20 != 0) goto L_0x020c
            int r0 = r4.getWidth()
            int r0 = r0 + r7
            r4.setFinalHorizontal(r7, r0)
            int r0 = r5.getWidth()
            int r0 = r3 - r0
            r5.setFinalHorizontal(r0, r3)
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r0 = r18.getMeasurer()
            horizontalSolvingPass(r4, r0, r1)
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r0 = r18.getMeasurer()
            horizontalSolvingPass(r5, r0, r1)
            goto L_0x022b
        L_0x020c:
            int r0 = r4.getHeight()
            int r0 = r0 + r7
            r4.setFinalVertical(r7, r0)
            int r0 = r5.getHeight()
            int r0 = r3 - r0
            r5.setFinalVertical(r0, r3)
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r0 = r18.getMeasurer()
            verticalSolvingPass(r4, r0)
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer r0 = r18.getMeasurer()
            verticalSolvingPass(r5, r0)
        L_0x022b:
            r0 = 1
            return r0
        L_0x022d:
            r0 = 0
            return r0
        L_0x022f:
            r0 = 1
        L_0x0230:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.Direct.solveChain(androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer, androidx.constraintlayout.solver.LinearSystem, int, int, androidx.constraintlayout.solver.widgets.ChainHead, boolean, boolean, boolean):boolean");
    }
}
