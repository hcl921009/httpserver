package com.hcl.server.demo1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
/*
    简单实例，用于接受请求并能够返回GET方式请求的头信息，POST请求不能处理空行
 */

public class Server {
    private ServerSocket server;//避免成为局部变量

    private void start(){
        try {
            server = new ServerSocket(8888);//参数为域名的端口
            this.receive();//执行此对象的receive方法

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void receive(){
        try {
            Socket client = server.accept();//accept方法返回一个Socket对象
            String msg;//用于存放未经处理的全部请求信息

            StringBuilder sb = new StringBuilder();//可变字符串装处理好的信息（但是线程不安全）
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));//建立通信管道
            while((msg = br.readLine()).length()>0){
                sb.append(msg);
                sb.append("\r\n");//readLine方法每次按行接受信息，故\r\n加回车
                if(msg == null){//意味着POST请求方式最后的空行接受后被抛弃
                    br.close();
                    break;

                }
            }
            String requestInfo = sb.toString().trim();//trim方法去掉多余空格
            System.out.println(requestInfo);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void close(){

    }

    public static void main(String[] args){

        Server server = new Server();
        server.start();
    }

}
