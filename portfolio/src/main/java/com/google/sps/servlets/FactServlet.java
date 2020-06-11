package com.google.sps.servlets;

import static com.google.sps.servlets.DataStoreKeys.FACT_ENTITY;
import static com.google.sps.servlets.DataStoreKeys.REPLIES_ENTITY;
import com.google.sps.data.FactPost;
import com.google.sps.data.Reply;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Long; 
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/share-fact")
public class FactServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Query factQuery = new Query(FACT_ENTITY).addSort("postTime", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery preparedFactQuery = datastore.prepare(factQuery);
    List<FactPost> posts = new ArrayList<>();

    for (Entity factEntity : preparedFactQuery.asIterable()) {
      long postId = factEntity.getKey().getId();
      String username = (String) factEntity.getProperty("username");
      String fact = (String) factEntity.getProperty("fact");
      String postTime = (String) factEntity.getProperty("postTime");

      FactPost post = new FactPost(username, fact, postId, postTime);

      Query replyQuery = new Query(REPLIES_ENTITY);
      replyQuery.addSort("replyTime", SortDirection.DESCENDING);
      PreparedQuery preparedReplyQuery = datastore.prepare(replyQuery); 
 
      for (Entity replyEntity : preparedReplyQuery.asIterable()) {
        String replyUsername = (String) replyEntity.getProperty("username");
        String replyData = (String) replyEntity.getProperty("replyData");
        String replyTime = (String) replyEntity.getProperty("replyTime");
        Long parentId = (Long) replyEntity.getProperty("parentId");

        Reply reply = new Reply(replyUsername, replyData, replyTime);

        if (postId == parentId) {
          post.additionalReply(reply);
        }
      }
      posts.add(post);  
    }

    String json = new Gson().toJson(posts);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()){
      String username = userService.getCurrentUser().getEmail();
      String fact = request.getParameter("text-input");

      FactPost newPost = new FactPost(username, fact);
      
      Entity factEntity = new Entity(FACT_ENTITY);
      if (fact != "") {
        factEntity.setProperty("username", newPost.getUsername());
        factEntity.setProperty("fact", newPost.getFact());
        factEntity.setProperty("postTime", newPost.getTime());
        factEntity.setProperty("postId", factEntity.getKey().getId());
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(factEntity);
      }
      response.sendRedirect("/facts.html");
    }
    else {
      response.sendRedirect("/index.html");
    }
  }
} 

 
