package com.ugur;

import com.ugur.model.entity.SessionEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataGenerator {

  public static List<SessionEntity> generateTestData() {
    SessionEntity s1 = new SessionEntity("s1", 45);
    SessionEntity s2 = new SessionEntity("s2", 30);
    SessionEntity s3 = new SessionEntity("s3", 45);
    SessionEntity s4 = new SessionEntity("s4", 60);
    SessionEntity s5 = new SessionEntity("s5", 60);
    SessionEntity s6 = new SessionEntity("s6", 30);
    SessionEntity s7 = new SessionEntity("s7", 30);
    SessionEntity s8 = new SessionEntity("s8", 30);
    SessionEntity s9 = new SessionEntity("s9", 45);
    SessionEntity s10 = new SessionEntity("s10", 60);
    SessionEntity s11 = new SessionEntity("s11", 30);
    SessionEntity s12 = new SessionEntity("s12", 45);
    SessionEntity s13 = new SessionEntity("s13", 30);
    SessionEntity s14 = new SessionEntity("s14", 30);
    SessionEntity s15 = new SessionEntity("s15", 30);
    SessionEntity s16 = new SessionEntity("s16", 60);
    SessionEntity s17 = new SessionEntity("s17", 30);
    SessionEntity s18 = new SessionEntity("s18", 0);
    List<SessionEntity> allSessions = new ArrayList<>(
        Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17));
    Collections.shuffle(allSessions);
    return allSessions;
  }
}
