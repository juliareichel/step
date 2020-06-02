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

@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String email = getEmail(request);
      long timestamp = System.currentTimeMillis();

      Entity emailEntity = new Entity("Email");
      if (email != "") {
        emailEntity.setProperty("email", email);
        emailEntity.setProperty("timestamp", timestamp);
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
    Query query = new Query("Email").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    List<Entity> email_entities = results.asList(FetchOptions.Builder.withLimit(3));

    List<String> emails = new ArrayList<>();
    for (Entity entity : email_entities) {
      long id = entity.getKey().getId();
      String email = (String) entity.getProperty("email");
      long timestamp = (long) entity.getProperty("timestamp");

      if (!emails.contains(email)){
        emails.add(email);  
      }
    }

    response.setContentType("application/json;");
    String json = new Gson().toJson(emails);
    response.getWriter().println(json);
  }
}