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
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String userReply = request.getParameter("reply-input");
    long replyTimestamp = System.currentTimeMillis();

    // Key replyKey = datastore.newKeyFactory().addAncestors(PathElement.of(EMAIL_ENTITY).setKind(REPLIES_ENTITY).newKey("reply"));
    // Entity replyEntity = Entity.newBuilder(replyKey);
    Entity replyEntity = new Entity(REPLIES_ENTITY, emailEntity.getKey());
    replyEntity.setProperty("userReply", userReply);
    replyEntity.setProperty("replyTimestamp", replyTimestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(replyEntity);

    response.setContentType("text/html");
    response.sendRedirect("/index.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query replyQuery = new Query(REPLIES_ENTITY).addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery replyResults = datastore.prepare(replyQuery);

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
    datastore.put(replies);

    response.setContentType("application/json;");
    String json = new Gson().toJson(replies);
    response.getWriter().println(json);
    // response.sendRedirect("/index.html");
    }
  }
