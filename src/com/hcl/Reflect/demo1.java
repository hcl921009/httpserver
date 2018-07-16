package com.hcl.Reflect;

import com.hcl.server.demo4.Servlet;

public class demo1 {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Servlet clz = (Servlet)Class.forName("com.hcl.server.demo4.LoginServlet").newInstance();
        System.out.println(clz);
    }
}
