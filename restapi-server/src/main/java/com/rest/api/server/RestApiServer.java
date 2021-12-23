package com.rest.api.server;


import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class RestApiServer {

    private static final int PORT = 8443;
    private static final String ROOT_CONTEXT = "/";
    private static final String SERVER_WEB_CONTENT = "restapi-server/src/main/resources/web";

    public static void main(String[] args) throws Exception {

        HttpConfiguration httpsConfiguration = new HttpConfiguration();
        httpsConfiguration.setSecureScheme(HttpScheme.HTTPS.asString());
        httpsConfiguration.setSecurePort(PORT);
        httpsConfiguration.addCustomizer(new SecureRequestCustomizer());
        httpsConfiguration.setSendServerVersion(false);
        httpsConfiguration.setSendDateHeader(false);

        HttpConnectionFactory httpsConnectionFactory = new HttpConnectionFactory(httpsConfiguration);

        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setTrustAll(true);

        SslConnectionFactory sslConnectionFactory =
                new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString());

        Server server = new Server();

        ServerConnector httpsConnector = new ServerConnector(server, sslConnectionFactory, httpsConnectionFactory);
        httpsConnector.setPort(httpsConfiguration.getSecurePort());

        server.addConnector(httpsConnector);

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletContextHandler.setContextPath(ROOT_CONTEXT);
        servletContextHandler.setBaseResource(Resource.newResource(SERVER_WEB_CONTENT));
        servletContextHandler.addServlet(DefaultServlet.class, ROOT_CONTEXT);

        server.setHandler(servletContextHandler);

        server.start();
        server.join();

    }

}
