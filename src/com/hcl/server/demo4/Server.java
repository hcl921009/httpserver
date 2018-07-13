package com.hcl.server.demo4;

import com.hcl.util.CloseUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/*
    完善分发器，建立对应返回页面
 */

public class Server {
    private ServerSocket server;//避免成为局部变量
    private boolean isShotDown = false;

    //启动方法
    private void start() {

        start(8888);
    }

    //指定端口的启动方法
    private void start(int port) {
        try {
            server = new ServerSocket(port);//参数为域名的端口
            this.receive();//执行此对象的receive方法

        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }

    }

    private void receive() {
        try {
            while (!isShotDown){
                Socket client = server.accept();//accept方法返回一个Socket对象
                new Thread(new Dispatcher(client)).start();//启动线程
            }

        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }

    }

    private void stop() {
        isShotDown = true;
        CloseUtil.closeSocket(server);

    }

    public static void main(String[] args) {

        Server server = new Server();
        server.start();
    }

}
