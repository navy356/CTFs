package org.rwctf.util;

import javax.servlet.http.HttpServletRequest;

public class ParamUtil
{
    private static final String[] SPECIAL_CHARS;
    private static final String[] REPLACE_CHARS;
    
    public static String getParameter(final HttpServletRequest request, final String name) {
        final String val = request.getParameter(name);
        if (StringUtil.isEmpty(val)) {
            return "";
        }
        return StringUtil.replace(val.trim(), ParamUtil.SPECIAL_CHARS, ParamUtil.REPLACE_CHARS);
    }
    
    static {
        SPECIAL_CHARS = new String[] { "&", "<", "'", ">", "\"", "(", ")" };
        REPLACE_CHARS = new String[] { "&amp;", "&lt;", "&#39;", "&gt;", "&quot;", "&#40;", "&#41;" };
    }
}