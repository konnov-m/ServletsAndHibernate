package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.storage.CRUD;
import org.example.storage.UserRequest;
import org.example.storage.UserRequestFromDB;
import org.example.webserver.WebServer;
import org.example.webserver.WebServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CRUD crud = new CRUD();
        UserRequest users = new UserRequestFromDB(crud);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        WebServer webServer = new WebServerImpl(users, gson, crud);

        try {
            webServer.start();
            webServer.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}