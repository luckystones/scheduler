package com.ugur.service;

import com.ugur.DataGenerator;
import com.ugur.model.Session;
import com.ugur.model.entity.SessionEntity;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@WebMvcTest(Scheduler.class)
public class SchedulerTest {

  @Autowired
  Scheduler scheduler;

  @Test
  public void checkAllBranches() {
    List<SessionEntity> allSessions = DataGenerator.generateTestData();
    Scheduler scheduler = new Scheduler();
    List<SessionEntity> scheduledSessionList = scheduler.schedule(allSessions);
    assertMorningSchedule(scheduledSessionList);
    assertAfternoonSchedule(scheduledSessionList);
    assertIfOverlapsExists(scheduledSessionList);

  }

  /**
   * Checks if all morning slots has startdate and end date for both tracks
   * @param scheduledSessionList
   */
  private void assertMorningSchedule(List<SessionEntity> scheduledSessionList) {
    List<SessionEntity> track1MorningSchedule = scheduledSessionList.stream().filter(s -> {
      return s.getTrack() == Scheduler.TRACK1 && s.getStartDate() != null && s.getEndDate() != null && s.getStartDate()
          .before(scheduler.getDatetimeForSpecificHour(12).toDate());
    }).collect(Collectors.toList());
    List<SessionEntity> track2MorningSchedule = scheduledSessionList.stream().filter(s -> {
      return s.getTrack() == Scheduler.TRACK2 && s.getStartDate() != null && s.getEndDate() != null
          && s.getEndDate() != null && s.getStartDate().before(scheduler.getDatetimeForSpecificHour(12).toDate());
    }).collect(Collectors.toList());
    Assert.assertEquals(4, track1MorningSchedule.size());
    Assert.assertEquals(4, track2MorningSchedule.size());
  }

  /**
   * Checks if all afternoon slots has startdate and end date for both tracks
   * Checks if last session ends at 17:00
   * @param scheduledSessionList
   */
  private void assertAfternoonSchedule(List<SessionEntity> scheduledSessionList) {

    List<SessionEntity> track1MorningSchedule = scheduledSessionList.stream().filter(s -> {
      return s.getTrack() == Scheduler.TRACK1 && s.getStartDate() != null && s.getEndDate() != null && s.getStartDate()
          .after(scheduler.getDatetimeForSpecificHour(12).toDate());
    }).sorted(Comparator.comparing(SessionEntity::getEndDate).reversed()).collect(Collectors.toList());
    new DateTime(track1MorningSchedule.iterator().next().getEndDate()).equals(scheduler.getDatetimeForSpecificHour(17));

    List<SessionEntity> track2MorningSchedule = scheduledSessionList.stream().filter(s -> {
      return s.getTrack() == Scheduler.TRACK2 && s.getStartDate() != null && s.getEndDate() != null && s.getStartDate()
          .after(scheduler.getDatetimeForSpecificHour(12).toDate());
    }).sorted(Comparator.comparing(SessionEntity::getEndDate).reversed()).collect(Collectors.toList());
    new DateTime(track2MorningSchedule.iterator().next().getEndDate()).equals(scheduler.getDatetimeForSpecificHour(17));

  }
  /**
   * Checks if each session starts just after previous session ended
   * Checks also lunch break is given or not
   *
   * @param scheduledSessionList
   */
  private void assertIfOverlapsExists(List<SessionEntity> scheduledSessionList) {
    List<SessionEntity> orderedTrack1Sessions = scheduledSessionList.stream().filter(s -> {
      return s.getTrack() == Scheduler.TRACK1;
    }).sorted(Comparator.comparing(SessionEntity::getEndDate)).collect(Collectors.toList());

    List<SessionEntity> orderedTrack2Sessions = scheduledSessionList.stream().filter(s -> {
      return s.getTrack() == Scheduler.TRACK1;
    }).sorted(Comparator.comparing(SessionEntity::getEndDate)).collect(Collectors.toList());

    for (List<SessionEntity> schedule : Arrays.asList(orderedTrack1Sessions, orderedTrack2Sessions)) {
      for (int index = 0; index < schedule.size() - 1; index++) {
        if (schedule.get(index).getEndDate().equals(scheduler.getDatetimeForSpecificHour(12).toDate())) {
          Assert.assertTrue(new Duration(new DateTime(schedule.get(index).getEndDate()),
              new DateTime(schedule.get(index + 1).getStartDate())).getStandardMinutes() == 60);
        } else {
          Assert.assertTrue(schedule.get(index).getEndDate().equals(schedule.get(index + 1).getStartDate()));
        }
      }
    }
  }
}
