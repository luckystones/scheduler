package com.ugur.model.entity;

import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
public class SessionEntity {
  @GeneratedValue
  @Id
  Long id;
  String programId;
  String title;
  Date startDate;
  Date endDate;
  String track;
  Integer duration;

  public SessionEntity() {}

  public SessionEntity(String title, Integer duration) {
    this.title = title;
    this.duration = duration;
  }

  public SessionEntity(String title, Integer duration, Date startDate, Date endDate, String track) {
    this.title = title;
    this.duration = duration;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Long getId() { return id; }

  public void setId(Long id) { this.id = id; }

  public String getProgramId() { return programId; }

  public void setProgramId(String programId) { this.programId = programId; }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) { this.endDate = endDate; }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public String getTrack() { return track; }

  public void setTrack(String track) { this.track = track; }

  @Override
  public String toString() {
    return "SessionEntity{" + "title='" + title + ", duration=" + duration + '}';
  }
}
