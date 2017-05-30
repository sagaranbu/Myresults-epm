package com.kpisoft.user.impl.openam.commands;

import org.apache.log4j.*;
import java.util.concurrent.atomic.*;
import javax.ws.rs.core.*;
import com.sun.jersey.api.client.*;
import java.util.*;

public abstract class OpenAmCommand
{
    public static final Logger logger;
    private static AtomicInteger reqCounter;
    private String baseUrl;
    private String command;
    private MediaType type;
    private MediaType accept;
    private int reqNum;
    private String url;
    protected Client client;
    
    public OpenAmCommand(final String baseUrl, final String command) {
        this.baseUrl = "";
        this.command = "";
        this.type = null;
        this.accept = null;
        this.reqNum = 0;
        this.url = "";
        this.client = null;
        this.baseUrl = baseUrl;
        this.command = command;
        this.client = Client.create();
        this.url = baseUrl + "/" + command;
        this.reqNum = OpenAmCommand.reqCounter.incrementAndGet();
    }
    
    public abstract String run();
    
    public String get(final String data) {
        if (data != null) {
            this.url = this.url + "?" + data;
        }
        final WebResource.Builder bldr = this.getBuilder(this.url, null);
        this.debug(HttpRequestMethod.GET, this.url, data);
        final ClientResponse response = (ClientResponse)bldr.get((Class)ClientResponse.class);
        return this.getStringResponse(response);
    }
    
    public String post(final Object data) {
        return this.post(data, null);
    }
    
    public String post(final Object data, final Properties headerProps) {
        final WebResource.Builder bldr = this.getBuilder(this.url, headerProps);
        ClientResponse response = null;
        if (data != null) {
            this.debug(HttpRequestMethod.POST, this.url, data);
            response = (ClientResponse)bldr.post((Class)ClientResponse.class, data);
        }
        else {
            this.debug(HttpRequestMethod.GET, this.url, null);
            response = (ClientResponse)bldr.get((Class)ClientResponse.class);
        }
        return this.getStringResponse(response);
    }
    
    public String delete(final Properties headerProps) {
        final WebResource.Builder bldr = this.getBuilder(this.url, headerProps);
        this.debug(HttpRequestMethod.DELETE, this.url, null);
        final ClientResponse response = (ClientResponse)bldr.delete((Class)ClientResponse.class);
        return this.getStringResponse(response);
    }
    
    public String put(final Object data, final Properties headerProps) {
        final WebResource.Builder bldr = this.getBuilder(this.url, headerProps);
        ClientResponse response = null;
        if (data != null) {
            this.debug(HttpRequestMethod.PUT, this.url, null);
            response = (ClientResponse)bldr.put((Class)ClientResponse.class, data);
        }
        else {
            this.debug(HttpRequestMethod.GET, this.url, null);
            response = (ClientResponse)bldr.get((Class)ClientResponse.class);
        }
        return this.getStringResponse(response);
    }
    
    public String getBaseUrl() {
        return this.baseUrl;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public void setType(final MediaType type) {
        this.type = type;
    }
    
    public void setAccept(final MediaType accept) {
        this.accept = accept;
    }
    
    private WebResource.Builder getBuilder(final String url, final Properties headerProps) {
        final WebResource webResource = this.client.resource(url);
        final WebResource.Builder bldr = webResource.getRequestBuilder();
        if (this.type != null) {
            bldr.type(this.type);
        }
        if (this.accept != null) {
            bldr.accept(new MediaType[] { this.accept });
        }
        if (headerProps != null) {
            for (final Object key : headerProps.keySet()) {
                final String val = headerProps.getProperty(key.toString());
                bldr.header((String) key, (Object)val);
            }
        }
        return bldr;
    }
    
    public String toQueryString(final Properties props) {
        final StringBuilder bldr = new StringBuilder();
        for (final Object key : props.keySet()) {
            final String val = props.getProperty(key.toString());
            bldr.append(key).append("=").append(val).append("&");
        }
        String str = bldr.toString();
        str = str.substring(0, str.length() - 1);
        return str.trim();
    }
    
    private void debug(final HttpRequestMethod method, final String url, final Object data) {
        final StringBuilder buf = new StringBuilder();
        buf.append(method).append(" (").append(this.reqNum).append(") ");
        buf.append(url);
        if (data != null) {
            buf.append(data);
        }
        OpenAmCommand.logger.debug((Object)buf.toString());
    }
    
    private String getStringResponse(final ClientResponse response) {
        String output = (String)response.getEntity((Class)String.class);
        output = ((output == null) ? "" : output.trim());
        final StringBuilder buf = new StringBuilder();
        buf.append("Response: ").append("(").append(this.reqNum).append(") ");
        buf.append("Status: ").append(response.getStatus()).append(", ");
        buf.append("Output: ").append(output);
        if (response.getStatus() < 200 || response.getStatus() >= 300) {
            OpenAmCommand.logger.error((Object)buf.toString());
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() + ". " + output);
        }
        OpenAmCommand.logger.debug((Object)buf.toString());
        return output;
    }
    
    static {
        logger = Logger.getLogger((Class)OpenAmCommand.class);
        OpenAmCommand.reqCounter = new AtomicInteger(0);
    }
    
    private enum HttpRequestMethod
    {
        GET, 
        POST, 
        PUT, 
        DELETE;
    }
}
