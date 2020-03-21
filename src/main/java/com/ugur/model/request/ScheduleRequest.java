package com.ugur.model.request;

import com.ugur.model.Session;

import java.util.List;

public class ScheduleRequest {

  List<Session> data;

  public List<Session> getData() {
    return data;
  }
  public void setData(List<Session> data) {
    this.data = data;
  }
}
