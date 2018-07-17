package com.hcl.server.demo5;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.List;
import java.util.Map;

public class WebApp {
    private static ServletContext context;
    static{
        try {
            //获取解析工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //获取解析器
            SAXParser sax = factory.newSAXParser();
            //指定xml+处理器
            WebHandler web = new WebHandler();
            sax.parse(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("com/hcl/server/demo5/web.xml"), web);


            //将list 转成Map
            context =new ServletContext();
            Map<String,String> servlet =context.getServlet();

            //servlet-name  servlet-class
            for(Entity entity:web.getEntityList()){
                servlet.put(entity.getName(), entity.getClz());

            }

            //url-pattern servlet-name
            Map<String,String> mapping =context.getMapping();
            for(Mapping mapp:web.getMappingList()){
                List<String> urls =mapp.getUrlPattern();
                for(String url:urls ){
                    mapping.put(url, mapp.getName());
                }
            }

        } catch (Exception e) {

        }

    }

    public static Servlet getServlet(String url) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //对域名检查并传送到servlet
        if((null==url)||(url=url.trim()).equals("")){
            return null;
        }
        String name = context.getServlet().get(context.getMapping().get(url));//建立mapping与Servlet的映射
        return (Servlet)Class.forName(name).newInstance();//需确保空构造存在,否则会报错java.lang.InstantiationException
    }


}
