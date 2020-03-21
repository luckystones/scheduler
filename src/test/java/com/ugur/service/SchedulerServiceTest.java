package com.ugur.service;

import com.ugur.DataGenerator;
import com.ugur.dao.ScheduleRepository;
import com.ugur.dao.ScheduleSpecification;
import com.ugur.mapper.ModelToEntityMapper;
import com.ugur.model.entity.SessionEntity;
import com.ugur.model.response.ScheduleResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@WebMvcTest(ScheduleService.class)
public class SchedulerServiceTest {
  @MockBean
  ScheduleRepository scheduleRepository;
  @MockBean
  InputValidator inputValidator;
  @MockBean
  ModelToEntityMapper modelToEntityMapper;
  @MockBean
  Scheduler scheduler;
  @InjectMocks
  @Autowired
  ScheduleService scheduleService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void retrieve() {
    Mockito.when(scheduleRepository.findAll(any(ScheduleSpecification.class))).thenReturn(
        DataGenerator.generateTestData());
    String programId = "1234";
    ScheduleResponse retrieveResponse = scheduleService.retrieve(programId);
    Assert.assertEquals(programId, retrieveResponse.getProgramId());
  }

  @Test
  public void post_invalidInput() {
    String errorMessage = "Error here";
    Mockito.when(inputValidator.validate(any(List.class))).thenReturn(Arrays.asList(errorMessage));
    ScheduleResponse scheduleResponse = scheduleService.post(new ArrayList<>());
    Assert.assertEquals(errorMessage, scheduleResponse.getErrors().get(0));
  }

  @Test
  public void post_allSuccessCase() {
    List<SessionEntity> testData = DataGenerator.generateTestData();
    Mockito.when(inputValidator.validate(any(List.class))).thenReturn(new ArrayList());
    Mockito.when(modelToEntityMapper.mapToEntityList(any(List.class))).thenReturn(testData);
    Mockito.when(scheduler.schedule(any(List.class))).thenAnswer(i -> i.getArguments()[0]);
    Mockito.when(scheduleRepository.saveAll(any(List.class))).thenAnswer(i -> i.getArguments()[0]);
    ScheduleResponse scheduleResponse = scheduleService.post(new ArrayList<>());
    Assert.assertTrue(scheduleResponse.getErrors().size() == 0);
    long numberOfEmptyIdRecords = scheduleResponse.getData().stream().filter(s -> s.getProgramId() == null).count();
    Assert.assertEquals(0, numberOfEmptyIdRecords);
  }
}











