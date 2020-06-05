/* Class representing a user's "comment" (email address)*/

package com.google.sps.data;

import java.util.ArrayList;

public class EmailObject {
  private String email;
  private ArrayList<String> replies;

  public EmailObject(String e) {
    email = e;
    replies = new ArrayList<>();
  }

  public String getEmail (){
    return email;
  }

  public ArrayList<String> getReplies() {
    return replies;
  }

  public void addReply(String reply) {
    replies.add(reply);
  }
}

