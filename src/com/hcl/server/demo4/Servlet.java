package com.hcl.server.demo4;

public abstract class Servlet {
    public Servlet() {
    }

    public void service(Request req, Response rep){
        this.doGet(req,rep);
        this.doPost(req,rep);
    }

    public abstract void doPost(Request req, Response rep);
    public abstract void doGet(Request req, Response rep);
}
