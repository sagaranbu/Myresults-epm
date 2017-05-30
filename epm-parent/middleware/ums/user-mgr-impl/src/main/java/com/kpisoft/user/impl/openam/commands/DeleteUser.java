package com.kpisoft.user.impl.openam.commands;

import com.kpisoft.user.openam.*;
import java.util.*;
import javax.ws.rs.core.*;

public class DeleteUser extends OpenAmCommand
{
    private Session session;
    
    public DeleteUser(final String user, final String realm, final Session session, final OpenAmConfiguration conf) {
        super(conf.getBaseUrl(), conf.getUserCommandPrefix() + realm + "/" + conf.getDeleteUserCommand() + "/" + user);
        this.session = session;
    }
    
    @Override
    public String run() {
        final Properties header = new Properties();
        header.setProperty("iplanetDirectoryPro", this.session.getToken());
        this.setType(MediaType.APPLICATION_JSON_TYPE);
        this.setAccept(MediaType.APPLICATION_JSON_TYPE);
        final String result = this.delete(header);
        return result;
    }
}
