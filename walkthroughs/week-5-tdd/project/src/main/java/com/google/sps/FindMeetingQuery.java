 package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
 
public final class FindMeetingQuery {
  
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> possibleTimes = new ArrayList<>();
    ArrayList<TimeRange> unavailableTimes = new ArrayList<>();
    long duration = request.getDuration();
 
    if (duration > TimeRange.WHOLE_DAY.duration()){
      return Collections.emptyList();
    }
    
    Set<String> requestAttendees = new HashSet<String>();
    requestAttendees.addAll(request.getAttendees());
    if (events.isEmpty()){
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    unavailableTimes = findUnavailableSlots(events, requestAttendees, unavailableTimes, request);
    possibleTimes = findAvailableSlots(unavailableTimes, duration);
    return possibleTimes;
  }
 
  public boolean personBusy(Event event, Set<String> requestAttendees){
    Set<String> eventAttendees = new HashSet<>(event.getAttendees());
    eventAttendees.retainAll(requestAttendees);
    if(!eventAttendees.isEmpty()){
      return true;
    }
    return false;
  }
 
  public ArrayList<TimeRange> findUnavailableSlots(Collection<Event> events, Set<String> requestAttendees, ArrayList<TimeRange> unavailableTimes, MeetingRequest request){
    
    Set<String> optionalAttendees = new HashSet<>(request.getOptionalAttendees());
    if (!optionalAttendees.isEmpty() && requestAttendees.isEmpty()){
      requestAttendees = optionalAttendees;
    }

    for (Event event: events){
      if(personBusy(event, requestAttendees)){
        unavailableTimes.add(event.getWhen());
      } 
    }
    Collections.sort(unavailableTimes, TimeRange.ORDER_BY_START);
    ArrayList<TimeRange> notOverlappingTimes = new ArrayList<>();
 
    for (int i=0; i<unavailableTimes.size(); i++){  
      if (notOverlappingTimes.isEmpty() || !unavailableTimes.get(i).overlaps(notOverlappingTimes.get(notOverlappingTimes.size()-1))){
        notOverlappingTimes.add(unavailableTimes.get(i));
      } else {
        TimeRange lastNonOverlapping = notOverlappingTimes.get(notOverlappingTimes.size()-1);
        int start = Math.min(lastNonOverlapping.start(), unavailableTimes.get(i).start());
        int end = Math.max(lastNonOverlapping.end(), unavailableTimes.get(i).end());
 
        TimeRange allUnavailable = TimeRange.fromStartEnd(start, end, false);
        notOverlappingTimes.remove(lastNonOverlapping);
        notOverlappingTimes.add(allUnavailable);
        }
    }
    return notOverlappingTimes;
  }
 
  public ArrayList<TimeRange> findAvailableSlots(ArrayList<TimeRange> unavailableTimes, long duration){

    int earliestStart = TimeRange.START_OF_DAY;
    int latestEnd = TimeRange.END_OF_DAY;
    ArrayList<TimeRange> availableTimes = new ArrayList<>();

    if (!unavailableTimes.isEmpty()){
      TimeRange first = unavailableTimes.get(0);
      TimeRange availableSlot = TimeRange.fromStartEnd(earliestStart, first.start(), false);
      int timeInterval = availableSlot.duration();
      if (timeInterval >= duration) {
        availableTimes.add(availableSlot);
      }
    } else{
      ArrayList<TimeRange> fullDay = new ArrayList<>();
      fullDay.add(TimeRange.WHOLE_DAY);
      return fullDay;
    }
 
    for (int i=0; i<unavailableTimes.size(); i++){
      TimeRange currEvent = unavailableTimes.get(i);
      int nextEventStart;
      if (i==unavailableTimes.size()-1){
        nextEventStart = latestEnd + 1;
      } else {
        nextEventStart = unavailableTimes.get(i+1).start();
      }
      int timeIntervalTwo = nextEventStart - currEvent.end();
      if (timeIntervalTwo >= duration){
        TimeRange availableSlotTwo = TimeRange.fromStartEnd(currEvent.end(), nextEventStart, false);
        availableTimes.add(availableSlotTwo);
      }
    }
    return availableTimes;
  }
}

