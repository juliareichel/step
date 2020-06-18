/*A query that finds available meeting times given a list of attendees and times*/

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
 
public final class FindMeetingQuery {

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()){
      return Collections.emptyList();
    }
    
    if (events.isEmpty()){
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    ArrayList<TimeRange> possibleTimes = filterUnavailableSlots(events, request);
    return possibleTimes;
  }

  /**
   *function used to see whether a person is available to meet
   *retainAll is used to filter the set to keep only the elements contained in 
     the specified collection 
   @param event the unique event
   @param attendees the set of attendees whose availability is being checked
   @return boolean T/F if person is busy
  **/
 
  public boolean isPersonBusy(Event event, Set<String> attendees) {
    Set<String> eventAttendees = new HashSet<>(event.getAttendees());
    eventAttendees.retainAll(attendees);
    if(!eventAttendees.isEmpty()){
      return true;
    }
    return false;
  }

  /**
   *function used to filter out all unavailable and overlapping times 
   @param events the collection of all today's events
   @param request the new meeting request
   @return ArrayList of TimeRanges that are available to book a meeting
  **/
 
  public ArrayList<TimeRange> filterUnavailableSlots(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> unavailableTimes = new ArrayList<>();
    ArrayList<TimeRange> optionalUnavailableTimes = new ArrayList<>();

    for (Event event: events) {
      Set<String> eventAttendees = event.getAttendees();
      Set<String> optionalAttendees = new HashSet<>(request.getOptionalAttendees());
      Set<String> requiredAttendees = new HashSet<>(request.getAttendees());
      Set<String> justRequiredAttendees = new HashSet<>(eventAttendees);
      justRequiredAttendees.retainAll(requiredAttendees);
      Set<String> justOptionalAttendees = new HashSet<> (eventAttendees);
      justOptionalAttendees.retainAll(optionalAttendees);

      if (!justRequiredAttendees.isEmpty() && isPersonBusy(event, justRequiredAttendees)) {
          unavailableTimes.add(event.getWhen());
      }

      if (!justOptionalAttendees.isEmpty() && isPersonBusy(event, justOptionalAttendees)) {
        optionalUnavailableTimes.add(event.getWhen());
      }
    }

    if (unavailableTimes.isEmpty() && !optionalUnavailableTimes.isEmpty()) {
      return findAvailableSlots(findOverlaps(optionalUnavailableTimes),request);
    }
    else if(!unavailableTimes.isEmpty() && optionalUnavailableTimes.isEmpty()) {
      return findAvailableSlots(findOverlaps(unavailableTimes),request);
    } else {
      ArrayList<TimeRange> allUnavailableTimes = new ArrayList<>();
      allUnavailableTimes.addAll(unavailableTimes);
      allUnavailableTimes.addAll(optionalUnavailableTimes);
      ArrayList<TimeRange> allAvailableTimes = findAvailableSlots(findOverlaps(allUnavailableTimes),request);
      if (allAvailableTimes.isEmpty()) {
        return findAvailableSlots(unavailableTimes,request);
      }
      return allAvailableTimes;
    }
  }

  /**
   *function used to get organize unavailable overlapping events
   @param overlappingUnavailableTimes ArrayList of TimeRanges that are unavailable including 
   overlaps
   @return organized ArrayList of TimeRanges that are unavailable
  **/
  
  public ArrayList<TimeRange> findOverlaps(ArrayList<TimeRange> overlappingUnavailableTimes) {
    Collections.sort(overlappingUnavailableTimes, TimeRange.ORDER_BY_START);
    ArrayList<TimeRange> notOverlappingTimes = new ArrayList<>();
 
    for (int i=0; i<overlappingUnavailableTimes.size(); i++) {  
      //if it doesn't overlap with last TimeRange it can be added 
      int lastTimeIndex = notOverlappingTimes.size()-1;
      if (notOverlappingTimes.isEmpty() || 
          !overlappingUnavailableTimes.get(i).overlaps(notOverlappingTimes.get(lastTimeIndex)))
      {
        notOverlappingTimes.add(overlappingUnavailableTimes.get(i));
      } else {
        TimeRange lastNonOverlapping = notOverlappingTimes.get(notOverlappingTimes.size()-1);
        int start = Math.min(lastNonOverlapping.start(), overlappingUnavailableTimes.get(i).start());
        int end = Math.max(lastNonOverlapping.end(), overlappingUnavailableTimes.get(i).end());
 
        TimeRange allUnavailable = TimeRange.fromStartEnd(start, end, false);
        notOverlappingTimes.add(allUnavailable);
        }
    }
    return notOverlappingTimes;
  }

  /**
   *function used to find available time slots
   @param unavailableTimes organized ArrayList of TimeRanges that are unavailable 
   @return organized ArrayList of TimeRanges that are available
  **/
 
  public ArrayList<TimeRange> findAvailableSlots(ArrayList<TimeRange> unavailableTimes, MeetingRequest request) {
    long duration = request.getDuration();
    int earliestStart = TimeRange.START_OF_DAY;
    int latestEnd = TimeRange.END_OF_DAY;
    ArrayList<TimeRange> availableTimes = new ArrayList<>();

    if (!unavailableTimes.isEmpty()) {
      TimeRange first = unavailableTimes.get(0);
      TimeRange availableSlot = TimeRange.fromStartEnd(earliestStart, first.start(), false);
      int timeInterval = availableSlot.duration();
      if (timeInterval >= duration) {
        availableTimes.add(availableSlot);
      }
    } else {
      ArrayList<TimeRange> fullDay = new ArrayList<>();
      fullDay.add(TimeRange.WHOLE_DAY);
      return fullDay;
    }
 
    for (int i=0; i<unavailableTimes.size(); i++) {
      TimeRange currEvent = unavailableTimes.get(i);
      int nextEventStart;
      if (i==unavailableTimes.size()-1) {
        nextEventStart = latestEnd + 1;
      } else {
        nextEventStart = unavailableTimes.get(i+1).start();
      }
      int timeIntervalTwo = nextEventStart - currEvent.end();
      if (timeIntervalTwo >= duration) {
        TimeRange availableSlotTwo = TimeRange.fromStartEnd(currEvent.end(), nextEventStart, false);
        availableTimes.add(availableSlotTwo);
      }
    }
    return availableTimes;
  }
}

