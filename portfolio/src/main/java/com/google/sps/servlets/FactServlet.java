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

import static com.google.sps.servlets.DataStoreKeys.FACT_ENTITY;
import com.google.sps.data.FactPost;
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
      String fact = (String) factEntity.getProperty("fact");
      String postTime = (String) factEntity.getProperty("postTime");

      FactPost post = new FactPost(fact, postId, postTime);
      posts.add(post);  
    }

    String json = new Gson().toJson(posts);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String fact = request.getParameter("text-input");

      FactPost newPost = new FactPost(fact);
      
      Entity factEntity = new Entity(FACT_ENTITY);
      if (fact != "") {
        factEntity.setProperty("fact", newPost.getFact());
        factEntity.setProperty("postTime", newPost.getTime());
        factEntity.setProperty("postId", factEntity.getKey().getId());
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(factEntity);
      }
      
      response.sendRedirect("/facts.html");
  }
}

 
