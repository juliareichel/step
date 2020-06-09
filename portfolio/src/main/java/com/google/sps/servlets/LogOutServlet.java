package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    String URLRedirect = "/index.html";

    if (userService.isUserLoggedIn()){
      String userEmail = userService.getCurrentUser().getEmail();
      String logOutURL = userService.createLogoutURL(URLRedirect);
      response.getWriter().println("<p>Logout <a href=\"" + logOutURL + "\">here</a>.</p>");
    }
    else {
      response.sendRedirect(URLRedirect);
    }
  }
}