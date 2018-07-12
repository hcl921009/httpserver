package com.hcl.server.demo3;

import com.hcl.util.CloseUtil;

import java.io.IOException;
import java.net.Socket;

public class Dispatcher implements Runnable {
    private Socket client;
    private Request req;
    private Response rep;
    private int code = 200;

    public Dispatcher (Socket client){//接口
        this.client = client;
        try {
            //Request请求信息分析
            req = new Request(client.getInputStream());
            //Response响应信息构建
            rep = new Response(client);
        } catch (IOException e) {
            //e.printStackTrace();
            code = 500;
            return;
        }
    }

    @Override
    public void run() {
        Servlet servlet = new Servlet();
        servlet.service(req,rep);
        try {
            rep.pushClient(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            rep.pushClient(500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CloseUtil.closeSocket(client);

    }
}
