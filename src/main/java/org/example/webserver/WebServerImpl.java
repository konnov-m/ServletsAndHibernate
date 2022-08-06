package org.example.webserver;

import com.google.gson.Gson;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.example.helper.FileSystemHelper;
import org.example.servlets.UsersApiServlet;
import org.example.servlets.UsersServlets;
import org.example.storage.CRUD;
import org.example.storage.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebServerImpl implements WebServer{

    private static final Logger log = LoggerFactory.getLogger(WebServerImpl.class);
    private static final String CONSTRAINT_NAME = "auth";
    private static final String ROLE_NAME_USER = "user";
    private static final String ROLE_NAME_ADMIN = "admin";

    private final String START_PAGE_NAME = "index.html";
    private final String COMMON_RESOURCE_DIR = "static";

    private final Gson gson;
    private final Server server;
    private UserRequest users;
    private static final int WEB_SERVER_PORT = 8080;
    private CRUD crud;
    private LoginService loginService;

    public WebServerImpl(UserRequest users, Gson gson, CRUD crud, LoginService loginService) {
        this.users = users;
        this.gson = gson;
        this.crud = crud;
        this.loginService = loginService;
        server = new Server(WEB_SERVER_PORT);
    }

    @Override
    public void start() throws Exception {
        initcontext();
        server.start();
    }

    public void initcontext(){
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new UsersServlets(users, crud)), "/users");
        servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(users, gson)), "/api/user/*");
        HandlerList handlerList = new HandlerList();

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[] {START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.FullPath(COMMON_RESOURCE_DIR));

        handlerList.addHandler(resourceHandler);
        handlerList.addHandler(applySecurity(servletContextHandler, "/users", "/api/users/*") );
        server.setHandler(handlerList);
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String ...paths) {
        Constraint constraint = new Constraint();
        constraint.setName(CONSTRAINT_NAME);
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{ROLE_NAME_USER, ROLE_NAME_ADMIN});

        List<ConstraintMapping> constraintMappings = new ArrayList<>();
        Arrays.stream(paths).forEachOrdered(path -> {
            ConstraintMapping mapping = new ConstraintMapping();
            mapping.setPathSpec(path);
            mapping.setConstraint(constraint);
            constraintMappings.add(mapping);
        });

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        security.setAuthenticator(new BasicAuthenticator());

        security.setLoginService(loginService);
        security.setConstraintMappings(constraintMappings);
        security.setHandler(new HandlerList(servletContextHandler));

        return security;
    }

}
