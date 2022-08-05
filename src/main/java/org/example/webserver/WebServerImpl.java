package org.example.webserver;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.example.servlets.UsersApiServlet;
import org.example.servlets.UsersServlets;
import org.example.storage.CRUD;
import org.example.storage.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerImpl implements WebServer{

    private static final Logger log = LoggerFactory.getLogger(WebServerImpl.class);

    private Gson gson;
    private final Server server;
    private UserRequest users;
    private static final int WEB_SERVER_PORT = 8080;
    private CRUD crud;

    public WebServerImpl(UserRequest users, Gson gson, CRUD crud) {
        this.users = users;
        this.gson = gson;
        this.crud = crud;
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
        handlerList.addHandler(servletContextHandler);
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
}
