package com.kpisoft.user.impl.openam.commands;

import com.kpisoft.user.openam.*;
import java.util.*;
import javax.ws.rs.core.*;

public class CreateRealm extends OpenAmCommand
{
    private String realm;
    private Session session;
    
    public CreateRealm(final String realm, final Session session, final OpenAmConfiguration conf) {
        super(conf.getBaseUrl(), conf.getCreateRealmCommand());
        this.session = session;
        this.realm = realm;
    }
    
    @Override
    public String run() {
        final Properties header = new Properties();
        header.setProperty("iplanetDirectoryPro", this.session.getToken());
        final String data = "{ \"realm\": \"" + this.realm + "\", \"sunOrganizationStatus\": \"" + "Active" + "\"}";
        this.setType(MediaType.APPLICATION_JSON_TYPE);
        this.setAccept(MediaType.APPLICATION_JSON_TYPE);
        final String result = this.post(data, header);
        return result;
    }
}
