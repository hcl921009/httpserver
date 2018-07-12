package com.hcl.server.demo2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/*
    封装Request和Response，实现单线程接受并返回信息
 */

public class Server {
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

            //Response响应调用
            Request req = new Request(client.getInputStream());
            //Response响应调用
            Response rep = new Response(client);

            rep.println("<html><head><title>HTTP响应示例</title>");
            rep.println("</head><body>");
            rep.println("欢迎:").println(req.getParameter("uname")).println("回来");
            rep.println("</body></html>");
            rep.pushClient(200);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void stop() {

    }

    public static void main(String[] args) {

        Server server = new Server();
        server.start();
    }

}
