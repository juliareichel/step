package com.google.sps.data;
import java.util.ArrayList;
import com.google.sps.data.Reply;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/* Class representing a user's "post" (fun fact)*/
public class FactPost {
  private String fact;
  private long postId; 
  private String postTime;
  private String username;
  private ArrayList<Reply> replies;

  public FactPost(String username, String fact) {
    this.username = username;
    this.fact = fact;
    long currentTime = System.currentTimeMillis();
    Date date = new Date(currentTime);
    TimeZone timezone = TimeZone.getTimeZone("EST");
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
    simpleDate.setTimeZone(timezone);
    this.postTime = simpleDate.format(date);
    this.replies = new ArrayList<>();
  }

  public FactPost(String username, String fact, long postId, String postTime) {
    this.username = username;
    this.fact = fact;
    this.postId = postId;
    this.postTime = postTime;
    this.replies = new ArrayList<>();
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

  public ArrayList<Reply> getReplies(){
    return replies;
  }

  public void addReply(Reply newReply){
    replies.add(newReply);
  }
  
}