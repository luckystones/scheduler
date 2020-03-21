package com.ugur.service;

import com.ugur.dao.ScheduleRepository;
import com.ugur.dao.ScheduleSpecification;
import com.ugur.dao.SearchCriteria;
import com.ugur.mapper.ModelToEntityMapper;
import com.ugur.model.Session;
import com.ugur.model.entity.SessionEntity;
import com.ugur.model.response.ScheduleResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScheduleService {

  public static Pattern duration = Pattern.compile("([0-9]+)");
  ScheduleRepository scheduleRepository;
  InputValidator inputValidator;
  @Qualifier("modelToEntityMapperImpl")
  ModelToEntityMapper mapper;
  Scheduler scheduler;

  @Autowired
  public ScheduleService(ScheduleRepository scheduleRepository, InputValidator inputValidator, Scheduler scheduler,
      ModelToEntityMapper mapper) {
    this.scheduleRepository = scheduleRepository;
    this.inputValidator = inputValidator;
    this.scheduler = scheduler;
    this.mapper = mapper;
  }

  public ScheduleResponse retrieve(String programId) {
    ScheduleSpecification searchSpec = new ScheduleSpecification(new SearchCriteria("programId", ":", programId));
    List<SessionEntity> searchResult = scheduleRepository.findAll(Specification.where(searchSpec));
    return new ScheduleResponse.Builder().programId(programId).data(mapper.mapToModelList(searchResult)).build();
  }

  public ScheduleResponse post(List<Session> sessions) {
    List<String> errorMessages = inputValidator.validate(sessions);

    if (!CollectionUtils.isEmpty(errorMessages)) {
      return new ScheduleResponse.Builder().errors(errorMessages).build();
    }
    parseAndSetDuration(sessions);
    List<SessionEntity> scheduledList = scheduler.schedule(mapper.mapToEntityList(sessions));
    //setting common unique program id for all lightening talks
    String programId = setProgramResourceId(scheduledList);
    List<SessionEntity> savedEntities = (List<SessionEntity>)scheduleRepository.saveAll(scheduledList);
    return new ScheduleResponse.Builder().programId(programId).errors(errorMessages).data(
        mapper.mapToModelList(savedEntities)).build();
  }

  private String setProgramResourceId(List<SessionEntity> sessionEntities) {
    String programId = new ObjectId().toString();
    sessionEntities.forEach(ent -> ent.setProgramId(programId));
    return programId;
  }

  private void parseAndSetDuration(List<Session> data) {
    for (Session session : data) {
      if (session.getDuration() == null) {
        session.setDuration("0");
      } else {
        Matcher matcher = duration.matcher(session.getDuration());
        boolean durationFound = matcher.find();
        if (!durationFound || Integer.parseInt(matcher.group(1)) <= 0) {
          session.setDuration("0");
        } else {
          session.setDuration(matcher.group(1));
        }
      }
    }
  }

}
