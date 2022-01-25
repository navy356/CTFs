package handout;
import java.io.*;

import com.example.demo.Sticky_note;

public class Main {
    public static void main(String[] args) {
        Eval_util data = new Eval_util();
        data.val = args[1];
        Sticky_note st = new Sticky_note(args[2],1);
        try {
            FileOutputStream out = new FileOutputStream("/dev/stdout");
            ObjectOutputStream oout = new ObjectOutputStream(out);

            oout.writeObject(st);
            oout.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
