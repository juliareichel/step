package com.google.sps.servlets;

import static com.google.sps.servlets.DataStoreKeys.REPLIES_ENTITY;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reply")
public class ReplyServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query replyQuery = new Query(REPLIES_ENTITY).addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery replyResults = datastore.prepare(replyQuery);

    String userReply = request.getParameter("reply-input");
    long timestamp = System.currentTimeMillis();
    // long id = entity.getKey().getId();

    List<Entity> reply_entities = replyResults;
    List<String> replies = new ArrayList<>();
    // Entity replies = new Entity(REPLIES_ENTITY);
    
    for (Entity reply : reply_entities) {
      long replyId = entity.getKey().getId();
      String userReply = (String) entity.getProperty("userReply");
      long timestamp = (long) entity.getProperty("timestamp");

      replies.add(reply);
    }

      // replies.setProperty("replyId", replyId);
      // replies.setProperty("userReply", userReply);
      // replies.setProperty("timestamp", timestamp);

    datastore.put(replies);

    response.setContentType("application/json;");
    String json = new Gson().toJson(replies);
    response.getWriter().println(json);
    // response.sendRedirect("/index.html");
    }
  }
