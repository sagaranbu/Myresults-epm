package com.kpisoft.user.impl.openam.commands;

import com.kpisoft.user.openam.*;
import com.sun.jersey.api.representation.*;

public class Logout extends OpenAmCommand
{
    private Session session;
    
    public Logout(final Session session, final OpenAmConfiguration conf) {
        super(conf.getBaseUrl(), conf.getSessionCommandPrefix() + "/" + conf.getLogoutCommand());
        this.session = session;
    }
    
    @Override
    public String run() {
        final Form form = new Form();
        form.add("subjectid", this.session.getToken());
        final String result = this.post(form);
        return result;
    }
}
