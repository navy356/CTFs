// 
// Decompiled by Procyon v0.5.36
// 

package com.example.demo;

import java.util.Iterator;
import java.io.ObjectInput;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.IOException;
import javax.servlet.http.Cookie;
import java.util.Base64;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home
{
    public int i;
    
    public Home() {
        this.i = 0;
    }
    
    @GetMapping({ "/" })
    public ModelAndView index() {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }
    
    @GetMapping({ "/add_notes" })
    public ModelAndView add_notes() {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addnote");
        return modelAndView;
    }
    
    @PostMapping({ "/add" })
    public String setCookie(final HttpServletResponse response, final HttpServletRequest req) throws IOException {
        final Sticky_note one = new Sticky_note(req.getParameter("note"), this.i);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(one);
        objectOutputStream.close();
        final String cookie_data = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        final Cookie cookie = new Cookie(invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, this.i), cookie_data);
        ++this.i;
        response.addCookie(cookie);
        response.sendRedirect("/");
        return "note added";
    }
    
    @GetMapping({ "/render" })
    public String readAllCookies(final HttpServletRequest request) throws IOException, ClassNotFoundException, InterruptedException {
        String listString = "";
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            List<String> values = new ArrayList<String>();
            final List<String> note_string = new ArrayList<String>();
            values = Arrays.stream(cookies).map(c -> c.getValue()).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
            for (int j = 0; j < values.size(); ++j) {
                if (values.get(j).indexOf("rO0") != -1) {
                    final byte[] decodedBytes = Base64.getDecoder().decode(values.get(j));
                    final ByteArrayInputStream ois = new ByteArrayInputStream(decodedBytes);
                    final ObjectInput in = new ObjectInputStream(ois);
                    final Sticky_note result = (Sticky_note)in.readObject();
                    result.inv();
                    note_string.add(result.get_notes());
                }
            }
            for (final String s : note_string) {
                listString = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, listString, s);
            }
        }
        return listString;
    }
}