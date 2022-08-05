package org.example.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.User;
import org.example.storage.UserRequest;

import java.io.IOException;

public class UsersApiServlet extends HttpServlet {
    private Gson gson;
    private final int ID_PATH_PARAM_POSITION = 1;
    private UserRequest users;

    public UsersApiServlet(UserRequest users, Gson gson) {
        this.users = users;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = users.findById(extractIdFromRequest(req)).orElse(null);

        resp.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = resp.getOutputStream();
        out.print(gson.toJson(user));
    }

    private long extractIdFromRequest(HttpServletRequest req) {
        String[] path = req.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION]: String.valueOf(-1);
        return Long.parseLong(id);
    }
}
