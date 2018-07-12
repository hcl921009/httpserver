package com.hcl.server.demo1;

import com.hcl.util.CloseUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class Response {
    public static final String CRLF = "\r\n";//回车符
    public static final String BLANK = " ";//空格符

    private BufferedWriter bw;//管道
    private StringBuilder content;//正文内容
    private StringBuilder headInfo;//头信息

    private int len = 0;//正文的长度

    public Response(){
        content = new StringBuilder();
        headInfo = new StringBuilder();
        len = 0;
    }

    public Response(Socket client){
        this ();
        try {
            bw = new BufferedWriter((new OutputStreamWriter(client.getOutputStream())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response(OutputStreamWriter os){
        this();
        this.bw = bw;
    }

    public Response print(String info){
        content.append(info);
        len += info.getBytes().length;
        return this;
    }

    public Response println(String info){
        content.append(info).append(CRLF);
        len += (info+CRLF).getBytes().length;
        return this;
    }

    private void creatHeadInfo(int code){
        headInfo.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);
        switch (code){
            case 200:
                headInfo.append("OK");
                break;
            case 404:
                headInfo.append("NOT FOUND");
                break;
            case 505:
                headInfo.append("SEVER ERROR");
                break;
        }
        headInfo.append(CRLF);
        //2)  响应头(com.hcl.server.demo1.Response Head)
        headInfo.append("com.hcl.server.demo1.Server:bjsxt com.hcl.server.demo1.Server/0.0.1").append(CRLF);
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Content-type:text/html;charset=GBK").append(CRLF);//响应类型和编码格式
        //正文长度 ：字节长度
        headInfo.append("Content-Length:").append(len).append(CRLF);
        headInfo.append(CRLF); //分隔符
    }
    public void pushClient(int code) throws IOException {
        if (headInfo == null) {
            code =500;
        }
        creatHeadInfo(code);

        bw.append(headInfo.toString());
        bw.append(content.toString());
        bw.flush();
    }

    public void close(){
        CloseUtil.closeIO(bw);
    }



}
