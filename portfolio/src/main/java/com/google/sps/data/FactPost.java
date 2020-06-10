package com.google.sps.data;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/* Class representing a user's "post" (fun fact)*/
public class FactPost {
  private String fact;
  private long postId; 
  private String postTime;
  private String username;

  public FactPost(String name, String message) {
    username = name;
    fact = message;
    long currentTime = System.currentTimeMillis();
    Date date = new Date(currentTime);
    TimeZone timezone = TimeZone.getTimeZone("EST");
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
    simpleDate.setTimeZone(timezone);
    postTime = simpleDate.format(date);
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