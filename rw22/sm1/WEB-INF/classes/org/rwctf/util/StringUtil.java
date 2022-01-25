package org.rwctf.util;

import java.util.UUID;

public class StringUtil
{
    public static boolean isEmpty(final String str) {
        return str == null || str.isEmpty();
    }
    
    public static String randomStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    public static String replace(final String s, final String oldSub, final String newSub) {
        if (s != null && oldSub != null && newSub != null) {
            final StringBuffer sb = new StringBuffer();
            final int length = oldSub.length();
            int x = 0;
            for (int y = s.indexOf(oldSub); x <= y; x = y + length, y = s.indexOf(oldSub, x)) {
                sb.append(s.substring(x, y));
                sb.append(newSub);
            }
            sb.append(s.substring(x));
            return sb.toString();
        }
        return null;
    }
    
    public static String replace(String s, final String[] oldSubs, final String[] newSubs) {
        if (s == null || oldSubs == null || newSubs == null) {
            return null;
        }
        if (oldSubs.length != newSubs.length) {
            return s;
        }
        for (int i = 0; i < oldSubs.length; ++i) {
            s = replace(s, oldSubs[i], newSubs[i]);
        }
        return s;
    }
}