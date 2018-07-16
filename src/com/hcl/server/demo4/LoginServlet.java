package com.hcl.server.demo4;

public class LoginServlet extends Servlet{
    @Override
    public void doPost(Request req, Response rep) {

    }

    @Override
    public void doGet(Request req, Response rep) {

        String name = req.getParameter("uname");
        String pwd =req.getParameter("pwd");
        if(login(name,pwd)){
            rep.println("登录成功");
        }else{
            rep.println("登录失败");
        }
    }
    public boolean login(String name,String pwd){
        return name.equals("霍晨龙") && pwd.equals("123456");
    }
}
