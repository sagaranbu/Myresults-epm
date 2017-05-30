package com.kpisoft.user.impl.openam.commands;

import com.kpisoft.user.openam.*;
import java.util.*;

public class GetUserProfile extends OpenAmCommand
{
    private Session session;
    private String attribute;
    
    public GetUserProfile(final Session session, final OpenAmConfiguration conf) {
        super(conf.getBaseUrl(), conf.getSessionCommandPrefix() + "/" + conf.getSessionProfileCommand());
        this.session = session;
        this.attribute = conf.getUserIdentityAttribute();
        if (this.attribute == null || this.attribute.startsWith("$")) {
            this.attribute = "uid";
        }
    }
    
    @Override
    public String run() {
        final Properties props = new Properties();
        props.setProperty("subjectid", this.session.getToken());
        props.setProperty("attributenames", this.attribute);
        final String data = this.toQueryString(props);
        final String result = this.get(data);
        return result;
    }
}
