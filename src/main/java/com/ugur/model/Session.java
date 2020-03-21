package com.ugur.model;

public class Session extends Model {
  Long id;
  String programId;
  String title;
  String startDate;
  String endDate;
  String track;
  String duration;

  public Session() {}

  public Session(Long id, String title, String duration) {
    this.id = id;
    this.title = title;
    this.duration = duration;
  }

  public Long getId() { return id; }

  public void setId(Long id) { this.id = id; }

  public String getProgramId() { return programId; }
  public void setProgramId(String programId) {
    this.programId = programId;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getDuration() { return duration; }
  public void setDuration(String duration) {
    this.duration = duration;
  }
  public String getTrack() { return track; }
  public void setTrack(String track) { this.track = track; }
  public String getStartDate() {
    return startDate;
  }
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }
  public String getEndDate() {
    return endDate;
  }
  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

}
