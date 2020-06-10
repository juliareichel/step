package com.google.sps.servlets;
 
import static com.google.sps.servlets.DataStoreKeys.REPLIES_ENTITY;
import com.google.sps.servlets.DataServlet;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.sps.data.Reply;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyFactory.Builder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebServlet("/reply")
public class ReplyServlet extends HttpServlet {
 
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    String username = userService.getCurrentUser().getEmail();

    String replyData = request.getParameter("reply-input");
    Reply newReply = new Reply(username, replyData);

    String id = request.getParameter("postId");
    long numericalId = Long.parseLong(id);
 
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity replyEntity = new Entity(REPLIES_ENTITY, numericalId);
    replyEntity.setProperty("username", newReply.getUsername());
    replyEntity.setProperty("replyData", newReply.getReply());
    replyEntity.setProperty("replyTime", newReply.getTime());
 
    datastore.put(replyEntity);
 
    response.setContentType("application/json;");
    response.sendRedirect("/facts.html");
  }
}
 
 


