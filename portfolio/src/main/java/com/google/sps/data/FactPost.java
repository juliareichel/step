package com.google.sps.data;
import java.util.ArrayList;

/* Class representing a user's "post" (fun fact)*/
public class FactPost {
  private String fact;
  private long postId; 
  private String postTime;
  private String username;

  public FactPost(String name, String message) {
    username = name;
    fact = message;
    postTime = String.valueOf(System.currentTimeMillis());
  }

  public FactPost(String name, String message, long id, String time) {
    username = name;
    fact = message;
    postId = id;
    postTime = time;
  }

  public String getFact (){
    return fact;
  }

  public long getId() {
    return postId;
  }

  public String getTime() {
    return postTime;
  }

  public String getUsername() {
    return username;
  }
}