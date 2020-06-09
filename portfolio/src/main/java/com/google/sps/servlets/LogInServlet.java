package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LogInServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    String urlRedirect = "/facts.html";

    if (userService.isUserLoggedIn()){
      String userEmail = userService.getCurrentUser().getEmail();
      response.sendRedirect(urlRedirect);
    }
    else {
      String loginUrl = userService.createLoginURL(urlRedirect);
      response.getWriter().println("<p>Login<a href=\"" + loginUrl + "\"> here</a>.</p>");
    }
  }
}