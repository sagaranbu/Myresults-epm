package com.kpisoft.user.impl.openam.commands;

import com.kpisoft.user.openam.*;
import com.sun.jersey.api.representation.*;

public class Login extends OpenAmCommand
{
    private String userName;
    private String password;
    private Session session;
    
    public Login(final String user, final String pwd, final OpenAmConfiguration conf) {
        super(conf.getBaseUrl(), conf.getSessionCommandPrefix() + "/" + conf.getLoginCommand());
        this.userName = user;
        this.password = pwd;
    }
    
    @Override
    public String run() {
        final Form form = new Form();
        form.add("username", this.userName);
        form.add("password", this.password);
        final String token = this.post(form);
        this.session = new Session(token);
        return token;
    }
    
    public Session getSession() {
        return this.session;
    }
}
