package org.knightsquad.droidflag;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public EditText inputFlagET;
    private TextView kctfTitle;
    private Button validateButton;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0745R.layout.activity_main);
        TextView textView = (TextView) findViewById(C0745R.C0748id.kctf2022Title);
        this.kctfTitle = textView;
        textView.setText("|| KnightCTF 2022 || Organized by Knight Squad ||");
        this.kctfTitle.setSelected(true);
        this.inputFlagET = (EditText) findViewById(C0745R.C0748id.inputFlag);
        Button button = (Button) findViewById(C0745R.C0748id.checkButton);
        this.validateButton = button;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String obj = MainActivity.this.inputFlagET.getText().toString();
                if (obj.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a flag", 0).show();
                } else if (obj.length() <= 10) {
                    Toast.makeText(MainActivity.this, KnightCTF.flag, 0).show();
                } else {
                    StringHandler stringHandler = new StringHandler();
                    if ((stringHandler.getS1(MainActivity.this) + "{" + stringHandler.getS3(MainActivity.this) + "_" + stringHandler.getS2(MainActivity.this) + "_" + stringHandler.getS4(MainActivity.this) + "}").equals(obj)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, C0745R.style.AlertDialog);
                        builder.setTitle((CharSequence) MainActivity.this.getResources().getString(C0745R.string.f116s9));
                        builder.setMessage((CharSequence) MainActivity.this.getResources().getString(C0745R.string.s10));
                        builder.show();
                        return;
                    }
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this, C0745R.style.AlertDialog);
                    builder2.setTitle((CharSequence) MainActivity.this.getResources().getString(C0745R.string.s11));
                    builder2.setMessage((CharSequence) MainActivity.this.getResources().getString(C0745R.string.s12));
                    builder2.show();
                }
            }
        });
    }
}
