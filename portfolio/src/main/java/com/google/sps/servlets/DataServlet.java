// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import static com.google.sps.servlets.DataStoreKeys.EMAIL_ENTITY;
import static com.google.sps.servlets.DataStoreKeys.REPLIES_ENTITY;
import com.google.sps.data.Post;
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
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final int DEFAULT_QUANTITY = 5;

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String email = getEmail(request);
      long timestamp = System.currentTimeMillis();
      // long postId = getId(request);
      // Entity specificReply = .....
      
      Entity emailEntity = new Entity(EMAIL_ENTITY);
      if (email != "") {
        emailEntity.setProperty("email", email);
        emailEntity.setProperty("timestamp", timestamp);
        // emailEntity.setProperty("reply", specificReply);
        emailEntity.setProperty("postId", emailEntity.getKey().getId());
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(emailEntity);
      }
      
      response.setContentType("text/html");
      response.sendRedirect("/index.html");
  }

  private String getEmail(HttpServletRequest request) {
    String value = request.getParameter("text-input");
    if (value.contains(".com") || value.contains(".net") ||
      value.contains(".org") || value.contains(".edu")) {
        return value;
    }
    return "";
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query emailQuery = new Query(EMAIL_ENTITY).addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery preparedEmailQuery = datastore.prepare(emailQuery);

    int requestedQuantity = getUserInput(request);

    List<Entity> email_entities = preparedEmailQuery.asList(FetchOptions.Builder.withLimit(requestedQuantity));
    List<Post> posts = new ArrayList<>();

    for (Entity emailEntity : email_entities) {
      long postId = emailEntity.getKey().getId();
      String email = (String) emailEntity.getProperty("email");
      long timestamp = (long) emailEntity.getProperty("timestamp");
      // ArrayList<String> replies = emailEntity.getPropery("replies");

      Post newPost = new Post(email, postId);

      Query replyQuery = new Query(REPLIES_ENTITY).addSort("timestamp", SortDirection.DESCENDING);
      PreparedQuery preparedReplyQuery = datastore.prepare(replyQuery); 

      for (Entity replyEntity : preparedReplyQuery.asIterable()) {
        if (replyEntity.getParent().getId() == postId) {
          String replyData = (String) replyEntity.getProperty("replyData");
          newPost.addReply(replyData);
        }
      }

      if (!posts.contains(newPost)){
        posts.add(newPost);  
      }
    }

    response.setContentType("application/json;");
    String json = new Gson().toJson(posts);
    response.getWriter().println(json);
  }

  private int getUserInput(HttpServletRequest request) {
    String userInputString = request.getParameter("quantity");
    int userRequestedQuantity;

    if (userInputString == null) {
	    return DEFAULT_QUANTITY;
    }

    try {
    	userRequestedQuantity = Integer.parseInt(userInputString);
    }
    catch (NumberFormatException e){
	     System.err.println("Could not convert to int: " + userInputString);
     	 return DEFAULT_QUANTITY;
    }

    if (userRequestedQuantity < 1 || userRequestedQuantity > 10) {
      System.err.println("User request out of range: " + userInputString);
      return DEFAULT_QUANTITY;
    }

    return userRequestedQuantity;
  }
}
