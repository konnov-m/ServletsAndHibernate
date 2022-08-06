package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.example.helper.FileSystemHelper;
import org.example.storage.CRUD;
import org.example.storage.UserRequest;
import org.example.storage.UserRequestFromDB;
import org.example.webserver.WebServer;
import org.example.webserver.WebServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileSystem;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String LOGIN_SERVICE_CONFIG_NAME = "realm.properties";

    private static final String REALM_NAME = "MyRealm";
    public static void main(String[] args) {
        CRUD crud = new CRUD();
        UserRequest users = new UserRequestFromDB(crud);
        String loginServiceConfigPath = FileSystemHelper.FullPath(LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, loginServiceConfigPath);
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        WebServer webServer = new WebServerImpl(users, gson, crud, loginService);

        try {
            webServer.start();
            webServer.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}