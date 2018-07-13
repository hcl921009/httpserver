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

        Map<String,Servlet> servlet =context.getServlet();
        servlet.put("login", new LoginServlet());
        servlet.put("register", new RegisterServlet());
    }

    public static Servlet getServlet(String url){
        //对域名检查并传送到servlet
        if((null==url)||(url=url.trim()).equals("")){
            return null;
        }

        return context.getServlet().get(context.getMapping().get(url));

    }


}
