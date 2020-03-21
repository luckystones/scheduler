package com.ugur.service;

import com.ugur.DataGenerator;
import com.ugur.mapper.ModelToEntityMapper;
import com.ugur.mapper.ModelToEntityMapperImpl;
import com.ugur.model.Session;
import com.ugur.model.entity.SessionEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(InputValidator.class)
public class InputValidatorTest {

  @Autowired
  InputValidator inputValidator;

  @Test
  public void validateTest() {
    ModelToEntityMapper mapper = new ModelToEntityMapperImpl();
    List<Session> dataSet = mapper.mapToModelList(DataGenerator.generateTestData());
    dataSet.add(new Session(1L, null, "45"));
    dataSet.add(new Session(2L, null, "60"));
    List<String> errors = inputValidator.validate(dataSet);
    Assert.assertFalse(errors.isEmpty());
    Assert.assertEquals("2 record has empty title", errors.get(0));
  }

}
