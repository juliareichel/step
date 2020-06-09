package com.google.sps.data;
import java.util.ArrayList;

/* Class representing a user's "post" (fun fact)*/
public class FactPost {
  private String fact;
  private long postId; 
  private String postTime;

  public FactPost(String message) {
    fact = message;
    postTime = String.valueOf(System.currentTimeMillis());
  }

  public FactPost(String message, long id, String time) {
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
}