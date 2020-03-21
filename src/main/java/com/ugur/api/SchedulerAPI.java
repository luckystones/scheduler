package com.ugur.api;

import com.ugur.model.request.ScheduleRequest;
import com.ugur.model.response.ScheduleResponse;
import com.ugur.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api")
public class SchedulerAPI {

  private final ScheduleService scheduleService;

  @Autowired
  public SchedulerAPI(ScheduleService scheduleService) {
    this.scheduleService = scheduleService;
  }

  @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET)
  public ScheduleResponse retrieve(@PathVariable("id") String id) {
    return scheduleService.retrieve(id);
  }

  @RequestMapping(value = "/schedule", method = RequestMethod.POST)
  public ScheduleResponse schedule(@Valid @RequestBody ScheduleRequest scheduleRequest) {
    return scheduleService.post(scheduleRequest.getData());
  }

}
