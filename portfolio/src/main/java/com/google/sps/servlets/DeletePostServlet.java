package com.google.sps.servlets;

import static com.google.sps.servlets.DataStoreKeys.FACT_ENTITY;
import com.google.sps.data.FactPost;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/delete-post")
public class DeletePostServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query(FACT_ENTITY);
    PreparedQuery results = datastore.prepare(query);

    List<FactPost> fact_entities = new ArrayList<>();
    for (Entity entityKey : results.asIterable()){
      datastore.delete(entityKey.getKey());
    } 
  }
}
