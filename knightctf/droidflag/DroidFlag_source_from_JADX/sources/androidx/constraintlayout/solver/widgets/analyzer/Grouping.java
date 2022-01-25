package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.Guideline;
import androidx.constraintlayout.solver.widgets.HelperWidget;
import java.util.ArrayList;

public class Grouping {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_GROUPING = false;

    public static boolean validInGroup(ConstraintWidget.DimensionBehaviour dimensionBehaviour, ConstraintWidget.DimensionBehaviour dimensionBehaviour2, ConstraintWidget.DimensionBehaviour dimensionBehaviour3, ConstraintWidget.DimensionBehaviour dimensionBehaviour4) {
        return (dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (dimensionBehaviour3 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && dimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)) || (dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || (dimensionBehaviour4 == ConstraintWidget.DimensionBehaviour.MATCH_PARENT && dimensionBehaviour2 != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT));
    }

    /* JADX WARNING: Removed duplicated region for block: B:178:0x0353  */
    /* JADX WARNING: Removed duplicated region for block: B:189:0x038d  */
    /* JADX WARNING: Removed duplicated region for block: B:192:0x0391 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean simpleSolvingPass(androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r16, androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measurer r17) {
        /*
            r0 = r16
            java.util.ArrayList r1 = r16.getChildren()
            int r2 = r1.size()
            r3 = 0
            r4 = r3
        L_0x000c:
            if (r4 >= r2) goto L_0x0033
            java.lang.Object r5 = r1.get(r4)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r5
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r6 = r16.getHorizontalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r7 = r16.getVerticalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r8 = r5.getHorizontalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r9 = r5.getVerticalDimensionBehaviour()
            boolean r6 = validInGroup(r6, r7, r8, r9)
            if (r6 != 0) goto L_0x002b
            return r3
        L_0x002b:
            boolean r5 = r5 instanceof androidx.constraintlayout.solver.widgets.Flow
            if (r5 == 0) goto L_0x0030
            return r3
        L_0x0030:
            int r4 = r4 + 1
            goto L_0x000c
        L_0x0033:
            androidx.constraintlayout.solver.Metrics r4 = r0.mMetrics
            if (r4 == 0) goto L_0x0040
            androidx.constraintlayout.solver.Metrics r4 = r0.mMetrics
            long r5 = r4.grouping
            r7 = 1
            long r5 = r5 + r7
            r4.grouping = r5
        L_0x0040:
            r5 = r3
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r11 = 0
        L_0x0047:
            r12 = 1
            if (r5 >= r2) goto L_0x0120
            java.lang.Object r13 = r1.get(r5)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r13 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r13
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r14 = r16.getHorizontalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r15 = r16.getVerticalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r3 = r13.getHorizontalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = r13.getVerticalDimensionBehaviour()
            boolean r3 = validInGroup(r14, r15, r3, r4)
            if (r3 != 0) goto L_0x0070
            androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measure r3 = r0.mMeasure
            int r4 = androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure.Measure.SELF_DIMENSIONS
            r14 = r17
            androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer.measure(r13, r14, r3, r4)
            goto L_0x0072
        L_0x0070:
            r14 = r17
        L_0x0072:
            boolean r3 = r13 instanceof androidx.constraintlayout.solver.widgets.Guideline
            if (r3 == 0) goto L_0x0099
            r4 = r13
            androidx.constraintlayout.solver.widgets.Guideline r4 = (androidx.constraintlayout.solver.widgets.Guideline) r4
            int r15 = r4.getOrientation()
            if (r15 != 0) goto L_0x0089
            if (r8 != 0) goto L_0x0086
            java.util.ArrayList r8 = new java.util.ArrayList
            r8.<init>()
        L_0x0086:
            r8.add(r4)
        L_0x0089:
            int r15 = r4.getOrientation()
            if (r15 != r12) goto L_0x0099
            if (r6 != 0) goto L_0x0096
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
        L_0x0096:
            r6.add(r4)
        L_0x0099:
            boolean r4 = r13 instanceof androidx.constraintlayout.solver.widgets.HelperWidget
            if (r4 == 0) goto L_0x00dc
            boolean r4 = r13 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r4 == 0) goto L_0x00c5
            r4 = r13
            androidx.constraintlayout.solver.widgets.Barrier r4 = (androidx.constraintlayout.solver.widgets.Barrier) r4
            int r15 = r4.getOrientation()
            if (r15 != 0) goto L_0x00b4
            if (r7 != 0) goto L_0x00b1
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
        L_0x00b1:
            r7.add(r4)
        L_0x00b4:
            int r15 = r4.getOrientation()
            if (r15 != r12) goto L_0x00dc
            if (r9 != 0) goto L_0x00c1
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
        L_0x00c1:
            r9.add(r4)
            goto L_0x00dc
        L_0x00c5:
            r4 = r13
            androidx.constraintlayout.solver.widgets.HelperWidget r4 = (androidx.constraintlayout.solver.widgets.HelperWidget) r4
            if (r7 != 0) goto L_0x00cf
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
        L_0x00cf:
            r7.add(r4)
            if (r9 != 0) goto L_0x00d9
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
        L_0x00d9:
            r9.add(r4)
        L_0x00dc:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r13.mLeft
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 != 0) goto L_0x00f8
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r13.mRight
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 != 0) goto L_0x00f8
            if (r3 != 0) goto L_0x00f8
            boolean r4 = r13 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r4 != 0) goto L_0x00f8
            if (r10 != 0) goto L_0x00f5
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
        L_0x00f5:
            r10.add(r13)
        L_0x00f8:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r13.mTop
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 != 0) goto L_0x011b
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r13.mBottom
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 != 0) goto L_0x011b
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r13.mBaseline
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r4.mTarget
            if (r4 != 0) goto L_0x011b
            if (r3 != 0) goto L_0x011b
            boolean r3 = r13 instanceof androidx.constraintlayout.solver.widgets.Barrier
            if (r3 != 0) goto L_0x011b
            if (r11 != 0) goto L_0x0118
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r11 = r3
        L_0x0118:
            r11.add(r13)
        L_0x011b:
            int r5 = r5 + 1
            r3 = 0
            goto L_0x0047
        L_0x0120:
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            if (r6 == 0) goto L_0x013d
            java.util.Iterator r4 = r6.iterator()
        L_0x012b:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x013d
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.Guideline r5 = (androidx.constraintlayout.solver.widgets.Guideline) r5
            r6 = 0
            r13 = 0
            findDependents(r5, r6, r3, r13)
            goto L_0x012b
        L_0x013d:
            r6 = 0
            r13 = 0
            if (r7 == 0) goto L_0x015e
            java.util.Iterator r4 = r7.iterator()
        L_0x0145:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x015e
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.HelperWidget r5 = (androidx.constraintlayout.solver.widgets.HelperWidget) r5
            androidx.constraintlayout.solver.widgets.analyzer.WidgetGroup r7 = findDependents(r5, r6, r3, r13)
            r5.addDependents(r3, r6, r7)
            r7.cleanup(r3)
            r6 = 0
            r13 = 0
            goto L_0x0145
        L_0x015e:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r0.getAnchor(r4)
            java.util.HashSet r5 = r4.getDependents()
            if (r5 == 0) goto L_0x0186
            java.util.HashSet r4 = r4.getDependents()
            java.util.Iterator r4 = r4.iterator()
        L_0x0172:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x0186
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r5
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r5.mOwner
            r6 = 0
            r7 = 0
            findDependents(r5, r6, r3, r7)
            goto L_0x0172
        L_0x0186:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r0.getAnchor(r4)
            java.util.HashSet r5 = r4.getDependents()
            if (r5 == 0) goto L_0x01ae
            java.util.HashSet r4 = r4.getDependents()
            java.util.Iterator r4 = r4.iterator()
        L_0x019a:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x01ae
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r5
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r5.mOwner
            r6 = 0
            r7 = 0
            findDependents(r5, r6, r3, r7)
            goto L_0x019a
        L_0x01ae:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.CENTER
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r0.getAnchor(r4)
            java.util.HashSet r5 = r4.getDependents()
            if (r5 == 0) goto L_0x01d6
            java.util.HashSet r4 = r4.getDependents()
            java.util.Iterator r4 = r4.iterator()
        L_0x01c2:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x01d6
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r5
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r5.mOwner
            r6 = 0
            r7 = 0
            findDependents(r5, r6, r3, r7)
            goto L_0x01c2
        L_0x01d6:
            r6 = 0
            r7 = 0
            if (r10 == 0) goto L_0x01ee
            java.util.Iterator r4 = r10.iterator()
        L_0x01de:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x01ee
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r5
            findDependents(r5, r6, r3, r7)
            goto L_0x01de
        L_0x01ee:
            if (r8 == 0) goto L_0x0204
            java.util.Iterator r4 = r8.iterator()
        L_0x01f4:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x0204
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.Guideline r5 = (androidx.constraintlayout.solver.widgets.Guideline) r5
            findDependents(r5, r12, r3, r7)
            goto L_0x01f4
        L_0x0204:
            if (r9 == 0) goto L_0x0222
            java.util.Iterator r4 = r9.iterator()
        L_0x020a:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x0222
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.HelperWidget r5 = (androidx.constraintlayout.solver.widgets.HelperWidget) r5
            androidx.constraintlayout.solver.widgets.analyzer.WidgetGroup r6 = findDependents(r5, r12, r3, r7)
            r5.addDependents(r3, r12, r6)
            r6.cleanup(r3)
            r7 = 0
            goto L_0x020a
        L_0x0222:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r0.getAnchor(r4)
            java.util.HashSet r5 = r4.getDependents()
            if (r5 == 0) goto L_0x0249
            java.util.HashSet r4 = r4.getDependents()
            java.util.Iterator r4 = r4.iterator()
        L_0x0236:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x0249
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r5
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r5.mOwner
            r6 = 0
            findDependents(r5, r12, r3, r6)
            goto L_0x0236
        L_0x0249:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BASELINE
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r0.getAnchor(r4)
            java.util.HashSet r5 = r4.getDependents()
            if (r5 == 0) goto L_0x0270
            java.util.HashSet r4 = r4.getDependents()
            java.util.Iterator r4 = r4.iterator()
        L_0x025d:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x0270
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r5
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r5.mOwner
            r6 = 0
            findDependents(r5, r12, r3, r6)
            goto L_0x025d
        L_0x0270:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r0.getAnchor(r4)
            java.util.HashSet r5 = r4.getDependents()
            if (r5 == 0) goto L_0x0297
            java.util.HashSet r4 = r4.getDependents()
            java.util.Iterator r4 = r4.iterator()
        L_0x0284:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x0297
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r5
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r5.mOwner
            r6 = 0
            findDependents(r5, r12, r3, r6)
            goto L_0x0284
        L_0x0297:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.CENTER
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r4 = r0.getAnchor(r4)
            java.util.HashSet r5 = r4.getDependents()
            if (r5 == 0) goto L_0x02be
            java.util.HashSet r4 = r4.getDependents()
            java.util.Iterator r4 = r4.iterator()
        L_0x02ab:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x02be
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r5 = (androidx.constraintlayout.solver.widgets.ConstraintAnchor) r5
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = r5.mOwner
            r13 = 0
            findDependents(r5, r12, r3, r13)
            goto L_0x02ab
        L_0x02be:
            r13 = 0
            if (r11 == 0) goto L_0x02d5
            java.util.Iterator r4 = r11.iterator()
        L_0x02c5:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x02d5
            java.lang.Object r5 = r4.next()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r5
            findDependents(r5, r12, r3, r13)
            goto L_0x02c5
        L_0x02d5:
            r4 = 0
        L_0x02d6:
            if (r4 >= r2) goto L_0x0302
            java.lang.Object r5 = r1.get(r4)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r5 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r5
            boolean r6 = r5.oppositeDimensionsTied()
            if (r6 == 0) goto L_0x02ff
            int r6 = r5.horizontalGroup
            androidx.constraintlayout.solver.widgets.analyzer.WidgetGroup r6 = findGroup(r3, r6)
            int r5 = r5.verticalGroup
            androidx.constraintlayout.solver.widgets.analyzer.WidgetGroup r5 = findGroup(r3, r5)
            if (r6 == 0) goto L_0x02ff
            if (r5 == 0) goto L_0x02ff
            r7 = 0
            r6.moveTo(r7, r5)
            r7 = 2
            r5.setOrientation(r7)
            r3.remove(r6)
        L_0x02ff:
            int r4 = r4 + 1
            goto L_0x02d6
        L_0x0302:
            int r1 = r3.size()
            if (r1 > r12) goto L_0x030a
            r1 = 0
            return r1
        L_0x030a:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = r16.getHorizontalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r1 != r2) goto L_0x034a
            java.util.Iterator r1 = r3.iterator()
            r2 = r13
            r6 = 0
        L_0x0318:
            boolean r4 = r1.hasNext()
            if (r4 == 0) goto L_0x033c
            java.lang.Object r4 = r1.next()
            androidx.constraintlayout.solver.widgets.analyzer.WidgetGroup r4 = (androidx.constraintlayout.solver.widgets.analyzer.WidgetGroup) r4
            int r5 = r4.getOrientation()
            if (r5 != r12) goto L_0x032b
            goto L_0x0318
        L_0x032b:
            r5 = 0
            r4.setAuthoritative(r5)
            androidx.constraintlayout.solver.LinearSystem r7 = r16.getSystem()
            int r7 = r4.measureWrap((androidx.constraintlayout.solver.LinearSystem) r7, (int) r5)
            if (r7 <= r6) goto L_0x0318
            r2 = r4
            r6 = r7
            goto L_0x0318
        L_0x033c:
            if (r2 == 0) goto L_0x034a
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r0.setHorizontalDimensionBehaviour(r1)
            r0.setWidth(r6)
            r2.setAuthoritative(r12)
            goto L_0x034b
        L_0x034a:
            r2 = r13
        L_0x034b:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = r16.getVerticalDimensionBehaviour()
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r4 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r1 != r4) goto L_0x038d
            java.util.Iterator r1 = r3.iterator()
            r3 = r13
            r6 = 0
        L_0x0359:
            boolean r4 = r1.hasNext()
            if (r4 == 0) goto L_0x037d
            java.lang.Object r4 = r1.next()
            androidx.constraintlayout.solver.widgets.analyzer.WidgetGroup r4 = (androidx.constraintlayout.solver.widgets.analyzer.WidgetGroup) r4
            int r5 = r4.getOrientation()
            if (r5 != 0) goto L_0x036c
            goto L_0x0359
        L_0x036c:
            r5 = 0
            r4.setAuthoritative(r5)
            androidx.constraintlayout.solver.LinearSystem r7 = r16.getSystem()
            int r7 = r4.measureWrap((androidx.constraintlayout.solver.LinearSystem) r7, (int) r12)
            if (r7 <= r6) goto L_0x0359
            r3 = r4
            r6 = r7
            goto L_0x0359
        L_0x037d:
            r5 = 0
            if (r3 == 0) goto L_0x038e
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r1 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            r0.setVerticalDimensionBehaviour(r1)
            r0.setHeight(r6)
            r3.setAuthoritative(r12)
            r4 = r3
            goto L_0x038f
        L_0x038d:
            r5 = 0
        L_0x038e:
            r4 = r13
        L_0x038f:
            if (r2 != 0) goto L_0x0396
            if (r4 == 0) goto L_0x0394
            goto L_0x0396
        L_0x0394:
            r3 = r5
            goto L_0x0397
        L_0x0396:
            r3 = r12
        L_0x0397:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.Grouping.simpleSolvingPass(androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer, androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure$Measurer):boolean");
    }

    private static WidgetGroup findGroup(ArrayList<WidgetGroup> arrayList, int i) {
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            WidgetGroup widgetGroup = arrayList.get(i2);
            if (i == widgetGroup.f44id) {
                return widgetGroup;
            }
        }
        return null;
    }

    public static WidgetGroup findDependents(ConstraintWidget constraintWidget, int i, ArrayList<WidgetGroup> arrayList, WidgetGroup widgetGroup) {
        int i2;
        int findGroupInDependents;
        if (i == 0) {
            i2 = constraintWidget.horizontalGroup;
        } else {
            i2 = constraintWidget.verticalGroup;
        }
        int i3 = 0;
        if (i2 != -1 && (widgetGroup == null || i2 != widgetGroup.f44id)) {
            int i4 = 0;
            while (true) {
                if (i4 >= arrayList.size()) {
                    break;
                }
                WidgetGroup widgetGroup2 = arrayList.get(i4);
                if (widgetGroup2.getId() == i2) {
                    if (widgetGroup != null) {
                        widgetGroup.moveTo(i, widgetGroup2);
                        arrayList.remove(widgetGroup);
                    }
                    widgetGroup = widgetGroup2;
                } else {
                    i4++;
                }
            }
        } else if (i2 != -1) {
            return widgetGroup;
        }
        if (widgetGroup == null) {
            if ((constraintWidget instanceof HelperWidget) && (findGroupInDependents = ((HelperWidget) constraintWidget).findGroupInDependents(i)) != -1) {
                int i5 = 0;
                while (true) {
                    if (i5 >= arrayList.size()) {
                        break;
                    }
                    WidgetGroup widgetGroup3 = arrayList.get(i5);
                    if (widgetGroup3.getId() == findGroupInDependents) {
                        widgetGroup = widgetGroup3;
                        break;
                    }
                    i5++;
                }
            }
            if (widgetGroup == null) {
                widgetGroup = new WidgetGroup(i);
            }
            arrayList.add(widgetGroup);
        }
        if (widgetGroup.add(constraintWidget)) {
            if (constraintWidget instanceof Guideline) {
                Guideline guideline = (Guideline) constraintWidget;
                ConstraintAnchor anchor = guideline.getAnchor();
                if (guideline.getOrientation() == 0) {
                    i3 = 1;
                }
                anchor.findDependents(i3, arrayList, widgetGroup);
            }
            if (i == 0) {
                constraintWidget.horizontalGroup = widgetGroup.getId();
                constraintWidget.mLeft.findDependents(i, arrayList, widgetGroup);
                constraintWidget.mRight.findDependents(i, arrayList, widgetGroup);
            } else {
                constraintWidget.verticalGroup = widgetGroup.getId();
                constraintWidget.mTop.findDependents(i, arrayList, widgetGroup);
                constraintWidget.mBaseline.findDependents(i, arrayList, widgetGroup);
                constraintWidget.mBottom.findDependents(i, arrayList, widgetGroup);
            }
            constraintWidget.mCenter.findDependents(i, arrayList, widgetGroup);
        }
        return widgetGroup;
    }
}
