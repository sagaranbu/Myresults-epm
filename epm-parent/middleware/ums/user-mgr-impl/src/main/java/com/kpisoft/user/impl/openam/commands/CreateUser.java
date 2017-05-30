package com.kpisoft.user.impl.openam.commands;

import com.kpisoft.user.openam.*;
import java.util.*;
import javax.ws.rs.core.*;

public class CreateUser extends OpenAmCommand
{
    private String user;
    private String password;
    private Session session;
    
    public CreateUser(final String user, final String password, final String realm, final Session session, final OpenAmConfiguration conf) {
        super(conf.getBaseUrl(), conf.getUserCommandPrefix() + realm + "/" + conf.getCreateUserCommand());
        this.user = user;
        this.password = password;
        this.session = session;
    }
    
    @Override
    public String run() {
        final Properties header = new Properties();
        header.setProperty("iplanetDirectoryPro", this.session.getToken());
        final String data = "{ \"name\": \"" + this.user + "\", \"userpassword\": \"" + this.password + "\" , \"mail\": \"" + this.user + "\"}";
        this.setType(MediaType.APPLICATION_JSON_TYPE);
        this.setAccept(MediaType.APPLICATION_JSON_TYPE);
        final String result = this.post(data, header);
        return result;
    }
}
