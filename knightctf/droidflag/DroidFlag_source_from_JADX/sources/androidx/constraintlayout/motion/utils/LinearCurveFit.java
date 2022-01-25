package androidx.constraintlayout.motion.utils;

public class LinearCurveFit extends CurveFit {
    private static final String TAG = "LinearCurveFit";

    /* renamed from: mT */
    private double[] f21mT;
    private double mTotalLength = Double.NaN;

    /* renamed from: mY */
    private double[][] f22mY;

    public LinearCurveFit(double[] dArr, double[][] dArr2) {
        int length = dArr.length;
        int length2 = dArr2[0].length;
        this.f21mT = dArr;
        this.f22mY = dArr2;
        if (length2 > 2) {
            int i = 0;
            double d = 0.0d;
            double d2 = 0.0d;
            while (i < dArr.length) {
                double d3 = dArr2[i][0];
                double d4 = dArr2[i][0];
                if (i > 0) {
                    Math.hypot(d3 - d, d4 - d2);
                }
                i++;
                d = d3;
                d2 = d4;
            }
            this.mTotalLength = 0.0d;
        }
    }

    private double getLength2D(double d) {
        if (Double.isNaN(this.mTotalLength)) {
            return 0.0d;
        }
        double[] dArr = this.f21mT;
        int length = dArr.length;
        if (d <= dArr[0]) {
            return 0.0d;
        }
        int i = length - 1;
        if (d >= dArr[i]) {
            return this.mTotalLength;
        }
        double d2 = 0.0d;
        double d3 = 0.0d;
        double d4 = 0.0d;
        int i2 = 0;
        while (i2 < i) {
            double[][] dArr2 = this.f22mY;
            double d5 = dArr2[i2][0];
            double d6 = dArr2[i2][1];
            if (i2 > 0) {
                d2 += Math.hypot(d5 - d3, d6 - d4);
            }
            double[] dArr3 = this.f21mT;
            if (d == dArr3[i2]) {
                return d2;
            }
            int i3 = i2 + 1;
            if (d < dArr3[i3]) {
                double d7 = (d - dArr3[i2]) / (dArr3[i3] - dArr3[i2]);
                double[][] dArr4 = this.f22mY;
                double d8 = 1.0d - d7;
                return d2 + Math.hypot(d6 - ((dArr4[i2][1] * d8) + (dArr4[i3][1] * d7)), d5 - ((dArr4[i2][0] * d8) + (dArr4[i3][0] * d7)));
            }
            i2 = i3;
            d3 = d5;
            d4 = d6;
        }
        return 0.0d;
    }

    public void getPos(double d, double[] dArr) {
        double[] dArr2 = this.f21mT;
        int length = dArr2.length;
        int i = 0;
        int length2 = this.f22mY[0].length;
        if (d <= dArr2[0]) {
            for (int i2 = 0; i2 < length2; i2++) {
                dArr[i2] = this.f22mY[0][i2];
            }
            return;
        }
        int i3 = length - 1;
        if (d >= dArr2[i3]) {
            while (i < length2) {
                dArr[i] = this.f22mY[i3][i];
                i++;
            }
            return;
        }
        int i4 = 0;
        while (i4 < i3) {
            if (d == this.f21mT[i4]) {
                for (int i5 = 0; i5 < length2; i5++) {
                    dArr[i5] = this.f22mY[i4][i5];
                }
            }
            double[] dArr3 = this.f21mT;
            int i6 = i4 + 1;
            if (d < dArr3[i6]) {
                double d2 = (d - dArr3[i4]) / (dArr3[i6] - dArr3[i4]);
                while (i < length2) {
                    double[][] dArr4 = this.f22mY;
                    dArr[i] = (dArr4[i4][i] * (1.0d - d2)) + (dArr4[i6][i] * d2);
                    i++;
                }
                return;
            }
            i4 = i6;
        }
    }

    public void getPos(double d, float[] fArr) {
        double[] dArr = this.f21mT;
        int length = dArr.length;
        int i = 0;
        int length2 = this.f22mY[0].length;
        if (d <= dArr[0]) {
            for (int i2 = 0; i2 < length2; i2++) {
                fArr[i2] = (float) this.f22mY[0][i2];
            }
            return;
        }
        int i3 = length - 1;
        if (d >= dArr[i3]) {
            while (i < length2) {
                fArr[i] = (float) this.f22mY[i3][i];
                i++;
            }
            return;
        }
        int i4 = 0;
        while (i4 < i3) {
            if (d == this.f21mT[i4]) {
                for (int i5 = 0; i5 < length2; i5++) {
                    fArr[i5] = (float) this.f22mY[i4][i5];
                }
            }
            double[] dArr2 = this.f21mT;
            int i6 = i4 + 1;
            if (d < dArr2[i6]) {
                double d2 = (d - dArr2[i4]) / (dArr2[i6] - dArr2[i4]);
                while (i < length2) {
                    double[][] dArr3 = this.f22mY;
                    fArr[i] = (float) ((dArr3[i4][i] * (1.0d - d2)) + (dArr3[i6][i] * d2));
                    i++;
                }
                return;
            }
            i4 = i6;
        }
    }

    public double getPos(double d, int i) {
        double[] dArr = this.f21mT;
        int length = dArr.length;
        int i2 = 0;
        if (d <= dArr[0]) {
            return this.f22mY[0][i];
        }
        int i3 = length - 1;
        if (d >= dArr[i3]) {
            return this.f22mY[i3][i];
        }
        while (i2 < i3) {
            double[] dArr2 = this.f21mT;
            if (d == dArr2[i2]) {
                return this.f22mY[i2][i];
            }
            int i4 = i2 + 1;
            if (d < dArr2[i4]) {
                double d2 = (d - dArr2[i2]) / (dArr2[i4] - dArr2[i2]);
                double[][] dArr3 = this.f22mY;
                return (dArr3[i2][i] * (1.0d - d2)) + (dArr3[i4][i] * d2);
            }
            i2 = i4;
        }
        return 0.0d;
    }

    public void getSlope(double d, double[] dArr) {
        double[] dArr2 = this.f21mT;
        int length = dArr2.length;
        int length2 = this.f22mY[0].length;
        if (d <= dArr2[0]) {
            d = dArr2[0];
        } else {
            int i = length - 1;
            if (d >= dArr2[i]) {
                d = dArr2[i];
            }
        }
        int i2 = 0;
        while (i2 < length - 1) {
            double[] dArr3 = this.f21mT;
            int i3 = i2 + 1;
            if (d <= dArr3[i3]) {
                double d2 = dArr3[i3] - dArr3[i2];
                double d3 = dArr3[i2];
                for (int i4 = 0; i4 < length2; i4++) {
                    double[][] dArr4 = this.f22mY;
                    dArr[i4] = (dArr4[i3][i4] - dArr4[i2][i4]) / d2;
                }
                return;
            }
            i2 = i3;
        }
    }

    public double getSlope(double d, int i) {
        double[] dArr = this.f21mT;
        int length = dArr.length;
        int i2 = 0;
        if (d < dArr[0]) {
            d = dArr[0];
        } else {
            int i3 = length - 1;
            if (d >= dArr[i3]) {
                d = dArr[i3];
            }
        }
        while (i2 < length - 1) {
            double[] dArr2 = this.f21mT;
            int i4 = i2 + 1;
            if (d <= dArr2[i4]) {
                double d2 = dArr2[i4] - dArr2[i2];
                double d3 = dArr2[i2];
                double[][] dArr3 = this.f22mY;
                return (dArr3[i4][i] - dArr3[i2][i]) / d2;
            }
            i2 = i4;
        }
        return 0.0d;
    }

    public double[] getTimePoints() {
        return this.f21mT;
    }
}
