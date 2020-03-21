package com.ugur.model.response;

import com.ugur.model.Session;

import java.util.List;

public class ScheduleResponse {
  String programId;
  List<Session> data;
  List<String> errors;

  public String getProgramId() { return programId; }
  private void setProgramId(String programId) {this.programId = programId;}
  public List<Session> getData() {
    return data;
  }
  public void setData(List<Session> data) {
    this.data = data;
  }
  public List<String> getErrors() {
    return errors;
  }
  public void setErrors(List<String> errors) {
    this.errors = errors;
  }
  public static class Builder {
    private ScheduleResponse scheduleResponse;

    public Builder() {
      this.scheduleResponse = new ScheduleResponse();
    }

    public Builder data(List<Session> data) {
      this.scheduleResponse.setData(data);
      return this;
    }

    public Builder errors(List<String> data) {
      this.scheduleResponse.setErrors(data);
      return this;
    }

    public Builder programId(String programId) {
      this.scheduleResponse.setProgramId(programId);
      return this;
    }
    public ScheduleResponse build() {
      return this.scheduleResponse;
    }
  }
}
