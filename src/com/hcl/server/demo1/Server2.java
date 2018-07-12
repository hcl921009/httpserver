package com.hcl.server.demo1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
/*
    封装Response，实现快速构造返回头信息和返回页面
 */

public class Server2 {
    private ServerSocket server;//避免成为局部变量
    public static final String CRLF = "\r\n";
    public static final String BLANK = " ";

    private void start() {
        try {
            server = new ServerSocket(8888);//参数为域名的端口
            this.receive();//执行此对象的receive方法

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void receive() {
        try {
            Socket client = server.accept();//accept方法返回一个Socket对象
            String msg = null;//用于存放未经处理的全部请求信息

            byte[] data = new byte[20480];
            int len = client.getInputStream().read(data);
            String requestInfo = new String(data, 0, len).trim();//trim方法去掉多余空格
            System.out.println(requestInfo);

            //Response响应调用

            Response re = new Response(client);
            re.print("<html>\n" + "<head>\n" + "测试请求\n" + "</head>\n" +
                     "<body>\n" + "成功！" + "</form>\n" + "</body>\n" + "</html>");
            re.pushClient(200);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void stop() {

    }

    public static void main(String[] args) {

        Server2 server = new Server2();
        server.start();
    }

}
