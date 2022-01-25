// 
// Decompiled by Procyon v0.5.36
// 

package handout;

import java.io.IOException;
import java.io.Serializable;

public class Eval_util implements Serializable
{
    private static final long serialVersionUID = -8347155815694777921L;
    public String val;
    
    public int get_val() {
        return this.val.length();
    }
    
    public Object execute() throws IOException, InterruptedException {
        final String[] cmd = { "/bin/sh", "-c", this.val };
        final Runtime r = Runtime.getRuntime();
        final Process p = r.exec(cmd);
        return null;
    }
}