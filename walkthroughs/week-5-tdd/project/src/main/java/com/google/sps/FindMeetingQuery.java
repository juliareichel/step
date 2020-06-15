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

package com.google.sps;

import com.google.sps.TimeRange;
import static com.google.sps.TimeRange.START_OF_DAY;
import static com.google.sps.TimeRange.END_OF_DAY;
// import com.google.sps.servlets.TimeRange;
// import com.google.sps.servlets.Event;
// import com.google.sps.servlets.Events.java;
// import com.google.sps.servlets.MeetingRequest.java;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.io.*;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    ArrayList<Event> additionalEvents = new ArrayList<>();
    ArrayList<Event> scheduledEvents = new ArrayList<>();
    ArrayList<TimeRange> possibleTimes = new ArrayList<>();

    //When the requested duration is longer than 24 hours
    long duration = request.getDuration();
    if (duration > 1440){
      System.out.println("DURATION IS LONGER THAN DAY!");
      return Collections.emptyList();
    }
    
    //If there are no scheduled events yet
    if (events.isEmpty()){
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    //Ignores people not attending the existing event
    Iterator<Event> iter = events.iterator();
    Collection<String> requestAttendees = request.getAttendees();
    for (String requestAttendee: requestAttendees) {
      Collection<String> EventAttendees = iter.next().getAttendees();
      for (String EventAttendee: EventAttendees) {
         if (!requestAttendee.equals(EventAttendee)){
           return Arrays.asList(TimeRange.WHOLE_DAY);
         }
      }
    }

    //Event splits the rest of the day into two parts
    Iterator<Event> iter2 = events.iterator();

    if (events.size() == 1){
      TimeRange eventTime = iter2.next().getWhen();
      System.out.println("EventTime: " + eventTime);
      int startTime = eventTime.start();
      int endTime = eventTime.end();
      System.out.println("Time of splitting event: " + eventTime);
      if (startTime != TimeRange.START_OF_DAY){
        TimeRange firstHalf = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, startTime, false);
        TimeRange secondHalf = TimeRange.fromStartEnd(endTime, TimeRange.END_OF_DAY, true);
        possibleTimes.add(firstHalf);
        possibleTimes.add(secondHalf);
      }
      else {
        TimeRange firstHalf = TimeRange.fromStartEnd(endTime, TimeRange.END_OF_DAY, true);
        possibleTimes.add(firstHalf);
      }
      return possibleTimes;
    }

    /* *********************************************** 

    else if (events.size() == 2){
      while (iter.hasNext()){
        Event tempEvent = iter.next();
        String title = tempEvent.getTitle();
       
        System.out.print("TempEvent is: " + title);
        System.out.print("CurEvent is: " + curEvent);
        Event curEvent = tempEvent;
      }
      return possibleTimes;
    }
    return possibleTimes;
 ********************************* */
    // else {
    //   int earliestStart=0, latestEnd=0;
    //   int counter=0;
    //   TimeRange previousTime = TimeRange.fromStartEnd(0, 0, true);
    //   while (iter.hasNext()){
    //     counter++;
    //     Event tempEvent = iter.next();
    //     TimeRange curTime = tempEvent.getWhen();
    //     if (previousTime.overlaps(curTime)){
    //       earliestStart = previousTime.start();
    //       latestEnd = previousTime.end();
    //       int startTemp = curTime.start();
 
    //       int endTemp = curTime.end();
    //       if (startTemp <= earliestStart) {
    //         earliestStart = startTemp;
    //         System.out.println("EARLIEST START: " + earliestStart);
    //       }
    //       if (endTemp > latestEnd) {
    //         latestEnd = endTemp;
    //         System.out.println("LATEST END: " + latestEnd);
    //       }
    //       TimeRange tempTime = curTime;
    //       System.out.println("This is the " + counter + " th pass");
    //     }
    //   }
    //       if (earliestStart != TimeRange.START_OF_DAY){
    //         TimeRange firstAvailableSlot = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, earliestStart, false);
    //         System.out.println("FIRST SLOT: " + firstAvailableSlot);
    //         possibleTimes.add(firstAvailableSlot);
    //         if (latestEnd != TimeRange.END_OF_DAY) {
    //           TimeRange secondAvailableSlot = TimeRange.fromStartEnd(latestEnd, TimeRange.END_OF_DAY, true);
    //           System.out.println("SECOND SLOT: " + secondAvailableSlot);
    //           possibleTimes.add(secondAvailableSlot);
    //         }      
    //       }
    //   return possibleTimes;
    // }
    /* ************************************************ */
     else if (events.size() == 2){
       ArrayList<Event> tempList = new ArrayList<>(events);
       int earliestStart, latestEnd;
       for (int i=0; i<tempList.size(); i++){
         TimeRange firstEventTime = tempList.get(i).getWhen();
         i++;
         TimeRange secondEventTime = tempList.get(i).getWhen();

         if (firstEventTime.overlaps(secondEventTime)){
          earliestStart = firstEventTime.start();
          latestEnd = firstEventTime.end();
          int startTemp = secondEventTime.start();
          int endTemp = secondEventTime.end();
          if (startTemp < earliestStart) {
            earliestStart = startTemp;
          }
          if (endTemp > latestEnd) {
            latestEnd = endTemp;
          }
      
          if (earliestStart != TimeRange.START_OF_DAY){
            TimeRange firstAvailableSlot = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, earliestStart, false);
            possibleTimes.add(firstAvailableSlot);
            if (latestEnd != TimeRange.END_OF_DAY) {
              TimeRange secondAvailableSlot = TimeRange.fromStartEnd(latestEnd, TimeRange.END_OF_DAY, true);
              possibleTimes.add(secondAvailableSlot);
            }      
          }
          return possibleTimes;
        }
       }
     }

      //   TimeRange firstEventTime = tempEvent.getWhen();
      //   TimeRange secondEventTime = tempList.get(1).getWhen();
      //   if (firstEventTime.overlaps(secondEventTime)){
      //     earliestStart = firstEventTime.start();
      //     latestEnd = firstEventTime.end();
      //     int startTemp = secondEventTime.start();
      //     int endTemp = secondEventTime.end();
      //     if (startTemp < earliestStart) {
      //       earliestStart = startTemp;
      //     }
      //     if (endTemp > latestEnd) {
      //       latestEnd = endTemp;
      //     }
      
      //     if (earliestStart != TimeRange.START_OF_DAY){
      //       TimeRange firstAvailableSlot = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, earliestStart, false);
      //       possibleTimes.add(firstAvailableSlot);
      //       if (latestEnd != TimeRange.END_OF_DAY) {
      //         TimeRange secondAvailableSlot = TimeRange.fromStartEnd(latestEnd, TimeRange.END_OF_DAY, true);
      //         possibleTimes.add(secondAvailableSlot);
      //       }      
      //     }
      //   return possibleTimes;
      // }
      return possibleTimes;
    }
}
