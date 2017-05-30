package com.kpisoft.user.impl.openam.commands;

import com.kpisoft.user.openam.*;
import java.util.*;

public class ValidateSession extends OpenAmCommand
{
    private Session session;
    
    public ValidateSession(final Session session, final OpenAmConfiguration conf) {
        super(conf.getBaseUrl(), conf.getSessionCommandPrefix() + "/" + conf.getSessionValidateCommand());
        this.session = session;
    }
    
    @Override
    public String run() {
        boolean isValidSession = false;
        final Properties props = new Properties();
        props.setProperty("tokenid", this.session.getToken());
        final String data = this.toQueryString(props);
        final String result = this.get(data);
        if (result.equalsIgnoreCase("boolean=true")) {
            isValidSession = true;
        }
        return String.valueOf(isValidSession);
    }
}
