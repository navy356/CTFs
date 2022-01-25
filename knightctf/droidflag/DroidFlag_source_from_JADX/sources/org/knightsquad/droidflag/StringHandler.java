package org.knightsquad.droidflag;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

public class StringHandler extends AppCompatActivity {
    public String getS1(Context context) {
        return context.getResources().getString(C0745R.string.f112s5);
    }

    public String getS2(Context context) {
        return new StringBuilder(context.getResources().getString(C0745R.string.f113s6)).reverse().toString();
    }

    public String getS3(Context context) {
        return new StringBuilder(context.getResources().getString(C0745R.string.f114s7)).reverse().toString();
    }

    public String getS4(Context context) {
        return new StringBuilder(context.getResources().getString(C0745R.string.f115s8)).reverse().toString();
    }
}
