package com.ugur.service;

import com.ugur.model.entity.SessionEntity;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.paukov.combinatorics3.Generator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Scheduler {
  public static Integer TOTAL_TIME_IN_MORNING = 3 * 60;
  public static Integer TOTAL_DURATION_IN_AFTERNOON = 4 * 60;
  public static String TRACK1 = "TRACK1";
  public static String TRACK2 = "TRACK2";

  public List<SessionEntity> schedule(List<SessionEntity> sessionList) {
    Map<String, List<SessionEntity>> morningSchedule = scheduleMorning(sessionList);
    setTime(getDatetimeForSpecificHour(9), sessionList, morningSchedule);

    Map<String, List<SessionEntity>> afternoonSchedule = scheduleAfternoon(sessionList);
    setTime(getDatetimeForSpecificHour(13), sessionList, afternoonSchedule);
    scheduleLightningTalkAndNetworking(sessionList);
    return sessionList;
  }
  private void scheduleLightningTalkAndNetworking(List<SessionEntity> sessionList) {
    if (trackHasTimeToEnd(sessionList, TRACK1)) {
      addLightningAndNetworkingEvent(sessionList, TRACK1);
    }
    if (trackHasTimeToEnd(sessionList, TRACK2)) {
      addLightningAndNetworkingEvent(sessionList, TRACK2);
    }
  }
  private void addLightningAndNetworkingEvent(List<SessionEntity> sessionList, String track) {
    DateTime lastSessionEndTime = new DateTime(getLastSession(sessionList, track).get().getEndDate());
    //Adding 0 minutes talks as lightning talk after last session in the list
    for (SessionEntity s : sessionList) {
      if (s.getDuration() == 0 && s.getStartDate() == null) {
        s.setTrack(track);
        s.setStartDate(lastSessionEndTime.toDate());
        lastSessionEndTime = new DateTime(lastSessionEndTime).plusMinutes(5);
        s.setEndDate(new DateTime(lastSessionEndTime).toDate());
      }
    }
    if (trackHasTimeToEnd(sessionList, track)) {
      //Adding 0 minutes talks as lightning talk after last session in the list
      sessionList.add(new SessionEntity("Networking Event",
          (int)new Duration(lastSessionEndTime, getDatetimeForSpecificHour(17)).getStandardMinutes(),
          lastSessionEndTime.toDate(), getDatetimeForSpecificHour(17).toDate(), track));
    }
  }

  private boolean trackHasTimeToEnd(List<SessionEntity> sessionList, String track) {
    Optional<SessionEntity> lastSession = getLastSession(sessionList, track);
    if (lastSession.isPresent()) {
      return new Duration(new DateTime(lastSession.get().getEndDate()), getDatetimeForSpecificHour(17))
          .getStandardMinutes() > 0;
    }
    return false;
  }

  private Optional<SessionEntity> getLastSession(List<SessionEntity> sessionList, String track) {
    return sessionList.stream().filter(s -> {return s.getTrack() == track;}).sorted(
        Comparator.comparing(SessionEntity::getEndDate).reversed()).findFirst();
  }

  public static DateTime getDatetimeForSpecificHour(int hour) {
    LocalDate today = LocalDate.now();
    return new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), hour, 0, 0);
  }

  private void setTime(DateTime startTime, List<SessionEntity> sessionEntities,
      Map<String, List<SessionEntity>> scheduledTrackSlots) {
    for (Map.Entry<String, List<SessionEntity>> track : scheduledTrackSlots.entrySet()) {
      DateTime time = startTime;
      for (SessionEntity sessionEntity : sessionEntities) {
        for (SessionEntity scheduledSession : track.getValue()) {
          if (sessionEntity.getTitle().equals(scheduledSession.getTitle())) {
            sessionEntity.setTrack(track.getKey());
            sessionEntity.setStartDate(time.toDate());
            sessionEntity.setEndDate(time.plusMinutes(scheduledSession.getDuration()).toDate());
            time = time.plusMinutes(scheduledSession.getDuration());
          }
        }
      }
    }
  }

  private Map<String, List<SessionEntity>> scheduleMorning(final List<SessionEntity> sessionList) {
    Map<String, List<SessionEntity>> morningSessions = new HashMap<>();

    Optional<List<SessionEntity>> firstMorningSessions = Generator.combination(sessionList).simple(4).stream().filter(
        cList -> {
          int total = cList.stream().map(SessionEntity::getDuration).reduce(0, (x, y) -> x + y);
          return total == TOTAL_TIME_IN_MORNING;
        }).findFirst();

    if (firstMorningSessions.isPresent()) {
      morningSessions.put(TRACK1, firstMorningSessions.get());

      Optional<List<SessionEntity>> secondMorningSessions = Generator.combination(sessionList).simple(4).stream()
          .filter(cList -> {
            int total = cList.stream().map(SessionEntity::getDuration).reduce(0, (x, y) -> x + y);
            return total == TOTAL_TIME_IN_MORNING;
          }).filter(cList2 -> {
            boolean contains = false;
            for (SessionEntity s : cList2) {
              if (firstMorningSessions.get().contains(s)) {
                return false;
              }
            }
            return true;
          }).findFirst();

      if (secondMorningSessions.isPresent()) {
        morningSessions.put(TRACK2, secondMorningSessions.get());
      }
    }
    return morningSessions;

  }

  public Map<String, List<SessionEntity>> scheduleAfternoon(List<SessionEntity> sessionEntities) {
    List<SessionEntity> notScheduled = sessionEntities.stream().filter(
        session -> {return session.getStartDate() == null && session.getDuration() != 0;}).collect(Collectors.toList());

    Map<String, List<SessionEntity>> afternoonSchedule = new HashMap<>();
    List<SessionEntity> track1AfternoonSchedule = binPacking(notScheduled, TOTAL_DURATION_IN_AFTERNOON);
    notScheduled.removeAll(track1AfternoonSchedule);
    List<SessionEntity> track2AfternoonSchedule = binPacking(notScheduled, TOTAL_DURATION_IN_AFTERNOON);

    afternoonSchedule.put(TRACK1, track1AfternoonSchedule);
    afternoonSchedule.put(TRACK2, track2AfternoonSchedule);

    return afternoonSchedule;
  }

  public static List<SessionEntity> binPacking(final List<SessionEntity> sessionList, int target) {
    int checked = 0, remaining = target;
    List<SessionEntity> scheduledSession = new ArrayList<>();

    for (SessionEntity session : sessionList) {
      if (session.getDuration() > remaining) {
        return scheduledSession;
      } else {
        scheduledSession.add(session);
        remaining -= session.getDuration();
      }
      if (remaining == 0)
        return scheduledSession;
    }
    return scheduledSession;
  }
}
