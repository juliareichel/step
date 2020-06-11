package com.google.sps.data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/* Class representing a user's "reply"*/
public class Reply {
  private String reply;
  private String postTime;
  private String username;

  public Reply(String username, String reply) {
    this.username = username;
    this.reply = reply;
    long currentTime = System.currentTimeMillis();
    Date date = new Date(currentTime);
    TimeZone timezone = TimeZone.getTimeZone("EST");
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
    simpleDate.setTimeZone(timezone);
    this.postTime = simpleDate.format(date);
  }

  public Reply(String username, String reply, String postTime) {
    this.username = username;
    this.reply = reply;
    this.postTime = postTime;
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