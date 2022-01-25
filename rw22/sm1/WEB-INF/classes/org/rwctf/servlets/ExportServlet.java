package org.rwctf.servlets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.rwctf.util.StringUtil;
import org.rwctf.util.ParamUtil;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.io.File;
import javax.servlet.http.HttpServlet;

public class ExportServlet extends HttpServlet
{
    private File exportDir;
    
    public void init() throws ServletException {
        this.exportDir = new File(this.getServletContext().getRealPath("/export/"));
        if (!this.exportDir.exists()) {
            this.exportDir.mkdirs();
        }
    }
    
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String dir = ParamUtil.getParameter(req, "dir");
        String fileName = ParamUtil.getParameter(req, "filename");
        final String content = ParamUtil.getParameter(req, "content");
        if (StringUtil.isEmpty(content)) {
            this.outputMsg(resp, "Empty content");
            return;
        }
        if (StringUtil.isEmpty(fileName) || fileName.indexOf(46) < 0) {
            fileName = StringUtil.randomStr();
        }
        else {
            final String fileExt = fileName.substring(fileName.lastIndexOf(46) + 1);
            fileName = StringUtil.randomStr() + "." + fileExt;
        }
        File saveFile;
        if (StringUtil.isEmpty(dir)) {
            saveFile = new File(this.exportDir, fileName);
        }
        else {
            saveFile = new File(this.getServletContext().getRealPath("/"), dir + File.separator + fileName);
        }
        final String data = "DIRTY DATA AT THE BEGINNING " + content + " DIRTY DATA AT THE END";
        this.writeBytesToFile(saveFile, data.getBytes(StandardCharsets.UTF_8));
        this.outputMsg(resp, saveFile.getAbsolutePath());
    }
    
    private void outputMsg(final HttpServletResponse resp, final String msg) throws IOException {
        resp.getWriter().write(msg);
    }
    
    private void writeBytesToFile(final File dest, final byte[] bytes) throws IOException {
        if (!dest.getCanonicalPath().startsWith(this.getServletContext().getRealPath("/"))) {
            throw new IOException("Illegal file path");
        }
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dest);
            fos.write(bytes);
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (Exception ex) {}
            }
        }
    }
}