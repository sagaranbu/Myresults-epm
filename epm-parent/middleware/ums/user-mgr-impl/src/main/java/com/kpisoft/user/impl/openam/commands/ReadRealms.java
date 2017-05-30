package com.kpisoft.user.impl.openam.commands;

import com.kpisoft.user.openam.*;
import java.util.*;
import javax.ws.rs.core.*;

public class ReadRealms extends OpenAmCommand
{
    private Session session;
    
    public ReadRealms(final String realm, final Session session, final OpenAmConfiguration conf) {
        super(conf.getBaseUrl(), conf.getGetRealmCommand() + "/" + realm);
        this.session = session;
    }
    
    @Override
    public String run() {
        final Properties header = new Properties();
        header.setProperty("iplanetDirectoryPro", this.session.getToken());
        final String data = null;
        this.setType(MediaType.APPLICATION_JSON_TYPE);
        this.setAccept(MediaType.APPLICATION_JSON_TYPE);
        final String result = this.post(data, header);
        return result;
    }
}
