// 
// Decompiled by Procyon v0.5.36
// 

package com.example.demo;

import java.io.IOException;
import java.io.Serializable;

public class Sticky_note implements Serializable
{
    private static final long serialVersionUID = 8997955967313857188L;
    private int id;
    private String notes;
    public Eval_util data;
    
    public Sticky_note(final String note, final int id) {
        this.notes = note;
        this.id = id;
    }
    
    public String get_notes() {
        return this.notes;
    }
    
    public int inv() throws IOException, InterruptedException {
        if (this.data != null) {
            this.data.execute();
        }
        return 1;
    }
}