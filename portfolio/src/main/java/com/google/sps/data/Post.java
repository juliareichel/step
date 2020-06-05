/* Class representing a user's "comment" (email address)*/

package com.google.sps.data;

import java.util.ArrayList;

public class Post {
  private String email;
  private ArrayList<String> replies;
  private long postId; 

  public Post(String e, long id) {
    email = e;
    postId = id;
    replies = new ArrayList<>();
  }

  public String getEmail (){
    return email;
  }

  public ArrayList<String> getReplies() {
    return replies;
  }

  public long getId() {
    return postId;
  }

  public void addReply(String reply) {
    replies.add(reply);
  }
}

