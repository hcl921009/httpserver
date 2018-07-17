package com.hcl.server.demo5;

import com.hcl.util.CloseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;


public class Request {
    private String method;//请求方式只提供GET或POST
    private String url;//请求地址
    private Map<String, List<String>> parameterMapValues;//请求参数的集合

    public static final String CRLF = "\r\n";
    private InputStream is;//输入流给Server提供管道
    private String requestInfo;//请求信息的内容部分

    public Request() {
        method = "";
        url = "";
        parameterMapValues = new HashMap<String, List<String>>();
    }

    //构造函数
    public Request(InputStream is) {//只要流
        this();
        this.is = is;
        try {
            byte[] data = new byte[20480];
            int len = is.read(data);//read(byte[] a)方法将流中的数据一次存入数组，再返回实际读取的有效数据个数
            requestInfo = new String(data, 0, len);//转为字符串
        } catch (IOException e) {
            e.printStackTrace();
        }

        parseRequestInfo();//解析requestInfo中的内容
    }


    /*
        开始分析请求信息
        GET /index.html?name=123&pwd=5456 HTTP/1.1
     */
    private void parseRequestInfo() {
        if (requestInfo == null || (requestInfo.trim()).equals("")) {//判断是否获取到请求信息
            return;
        }

        String paraString = "";//用于存储请求参数

        String firstLine = requestInfo.substring(0,requestInfo.indexOf(CRLF));//取第一行，方便分析
        int index = firstLine.indexOf("/");
        method = firstLine.substring(0, index).trim();
        String urlStr = requestInfo.substring(index,firstLine.indexOf("HTTP/")).trim();
        //判断请求方式从而得到参数
        if(method.equalsIgnoreCase("get")){
            if(urlStr.contains("?")){
                //以'?'为分界用split切割字符串
                String[] urlArray = urlStr.split("\\?");//注意：'?'需要转义，而转义符号'\'本身需要转义，故有两个"\\"
                this.url = urlArray[0];//前半部分即为页面地址
                paraString = urlArray[1];//后半部分为参数（多个）
            }else{
                this.url = urlStr;
            }

        }else if(method.equalsIgnoreCase("post")){
            url = urlStr;
            paraString = requestInfo.substring(requestInfo.lastIndexOf(CRLF));
        }
        //没有参数
        if(paraString.equals("")){
            return;
        }

        parseParams(paraString);//将参数放入Map
    }

    private void parseParams(String paraString){
        StringTokenizer token = new StringTokenizer(paraString," &");
        while (token.hasMoreTokens()){
            String keyValue = token.nextToken();
            String[] keyValues = keyValue.split("=");//如果'='右侧没有值，返回参数的字符串
            if(keyValues.length == 1){
                //参数名=参数值，判断参数值为空字符串（即为用户出错）的情况。
                keyValues = Arrays.copyOf(keyValues,2);//复制数组并规定数组长度为2，则keyValues[1]为null
                keyValues[1] = null;//为了放入Map，字符串数组必须有两个值。
            }

            String key = keyValues[0].trim();
            String value = null==keyValues[1]?null:decode(keyValues[1].trim(),"UTF-8");
            if(!parameterMapValues.containsKey(key)){
                parameterMapValues.put(key, new ArrayList<String>());
            }

            List<String> values = parameterMapValues.get(key);
            values.add(value);

        }
    }

    //解决中文参数无法识别的问题
    private  String decode(String value,String code){
        try {
            return java.net.URLDecoder.decode(value,code);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据页面实际情况获取对应多个值
    public String[] getParameterValues(String name){
        List<String> values = null;
        if((values =parameterMapValues.get(name)) == null){
            return null;//没有参数就直接返回空
        }
        return values.toArray(new String[0]);
    }


    //根据页面实际情况获取对应单个值
    public String getParameter(String name){
        String[] values =getParameterValues(name);
        if(null==values){
            return null;
        }
        return values[0];
    }

    public String getUrl(){

        return url;
    }

    public void close(){
        CloseUtil.closeIO(is);
    }

}
