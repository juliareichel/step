package com.google.sps.data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/* Class representing a user's "reply"*/
public class Reply {
  private String reply;
  private String postTime;
  private String username;

  public Reply(String name, String message) {
    username = name;
    reply = message;
    long currentTime = System.currentTimeMillis();
    Date date = new Date(currentTime);
    TimeZone timezone = TimeZone.getTimeZone("EST");
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
    simpleDate.setTimeZone(timezone);
    postTime = simpleDate.format(date);
  }

  public Reply(String name, String message, String time) {
    username = name;
    reply = message;
    postTime = time;
  }

  public String getReply (){
    return reply;
  }

  public String getTime() {
    return postTime;
  }

  public String getUsername() {
    return username;
  }
}