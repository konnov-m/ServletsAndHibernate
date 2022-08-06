package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Main;
import org.example.models.User;
import org.example.storage.CRUD;
import org.example.storage.UserRequest;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UsersServlets extends HttpServlet {

    CRUD crud;
    private UserRequest users;

    public UsersServlets(UserRequest users, CRUD crud) {
        this.crud = crud;
        this.users = users;
    }

    private static final String USERS_PAGE_TEMPLATE = "users.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        File file = new File(getClass().getClassLoader().getResource(USERS_PAGE_TEMPLATE).getFile());
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            resp.getWriter().println(scanner.nextLine());
        }
        long i = 1L;

        while (crud.read(i) != null){
            resp.getWriter().println(crud.read(i++) + "<br>");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] params = req.getParameterMap().entrySet().stream().map(
                entry -> {
                    String param = String.join(" and ", entry.getValue());
                    return param + " ";
                }
        ).collect(Collectors.joining()).split(" ");

        if(params.length == 3) {
            User user = new User();
            user.setName(params[0]);
            user.setPassword(params[1]);
            user.setLogin(params[2]);
            crud.create(user);
        } else if (params.length == 4) {
            User user = new User();
            user.setName(params[0] + " " + params[1]);
            user.setPassword(params[2]);
            user.setLogin(params[3]);
            crud.create(user);
        }


        resp.sendRedirect("/users");
    }
}
