package com.google.sps.servlets;

import static com.google.sps.servlets.DataStoreKeys.REPLIES_ENTITY;
import static com.google.sps.servlets.DataStoreKeys.EMAIL_ENTITY;
import com.google.sps.servlets.DataServlet;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyFactory.Builder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.io.*;
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
    System.err.println("*********************");
    System.err.println("*********************");
    System.err.println("*********************");
    System.err.println("*********************");
    System.err.println("*********************");

    String replyData = request.getParameter("reply-input");
    System.err.println("replydata: " + replyData);
    long replyTimestamp = System.currentTimeMillis();
    String id = request.getParameter("postId");
    System.err.println("id: " + id);
    long numericalId = Long.parseLong(id);
    System.err.println("numericalId: " + numericalId);


    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // Key replyKey = datastore.newKeyFactory().addAncestors(PathElement.of(EMAIL_ENTITY, numericalId).setKind(REPLIES_ENTITY).newKey("reply"));
    // Entity replyEntity = Entity.newBuilder(replyKey);

    Entity replyEntity = new Entity(REPLIES_ENTITY, numericalId);
    replyEntity.setProperty("replyData", replyData);
    replyEntity.setProperty("replyTimestamp", replyTimestamp);

    System.err.println("REPLY ENTITY!!!!!!!!!!!!!" + replyEntity.getProperty("replyData"));

    datastore.put(replyEntity);

    response.setContentType("text/html");
    response.sendRedirect("/index.html");
  }





















  // @Override
  // public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
  //   Query replyQuery = new Query(REPLIES_ENTITY).addSort("timestamp", SortDirection.DESCENDING);
  //   DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  //   PreparedQuery replyResults = datastore.prepare(replyQuery);

  //   long timestamp = System.currentTimeMillis();
  //   // long id = entity.getKey().getId();

  //   List<Entity> reply_entities = replyResults;
  //   List<String> replies = new ArrayList<>();
  //   // Entity replies = new Entity(REPLIES_ENTITY);
    
  //   for (Entity reply : reply_entities) {
  //     long replyId = entity.getKey().getId();
  //     String userReply = (String) entity.getProperty("userReply");
  //     long timestamp = (long) entity.getProperty("timestamp");

  //     replies.add(reply);
  //   }
  //   datastore.put(replies);

  //   response.setContentType("application/json;");
  //   String json = new Gson().toJson(replies);
  //   response.getWriter().println(json);
  //   // response.sendRedirect("/index.html");
  //   }
  }
