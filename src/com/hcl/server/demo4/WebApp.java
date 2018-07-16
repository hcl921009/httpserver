package com.hcl.server.demo4;

import java.util.*;

public class WebApp {
    private static ServletContext context;
    static{
        context = new ServletContext();

        //包含网站所有的页面映射
        Map<String, String> mapping = context.getMapping();
        mapping.put("/login","login");
        mapping.put("/log","login");
        mapping.put("/reg","register");

        Map<String,String> servlet =context.getServlet();
        servlet.put("login", "com.hcl.server.demo4.LoginServlet");
        servlet.put("register", "com.hcl.server.demo4.RegisterServlet");
    }

    public static Servlet getServlet(String url) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //对域名检查并传送到servlet
        if((null==url)||(url=url.trim()).equals("")){
            return null;
        }

        String name = context.getServlet().get(context.getMapping().get(url));//建立mapping与Servlet的映射
        return (Servlet)Class.forName(name).newInstance();//需确保空构造存在

    }


}
