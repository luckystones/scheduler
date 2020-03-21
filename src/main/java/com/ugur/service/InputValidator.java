package com.ugur.service;

import com.ugur.model.Session;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class InputValidator {

  public List<String> validate(List<Session> data) {

    List<Session> invalidDurationOrTitle = data.stream().filter(schedule -> StringUtils.isEmpty(schedule.getTitle()))
        .collect(Collectors.toList());

    if (!CollectionUtils.isEmpty(invalidDurationOrTitle)) {
      return Arrays.asList(String.format("%s record has empty title", invalidDurationOrTitle.size()));
    }
    return new ArrayList<>();
  }

}
