package com.ugur.mapper;

import com.ugur.model.Session;
import com.ugur.model.entity.SessionEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component("modelToEntityMapperImpl")
@Mapper(componentModel = "jsr330")
public abstract class ModelToEntityMapper extends AbstractMapper<Session, SessionEntity> {

  public abstract Session mapToModel(SessionEntity object);

  public abstract SessionEntity mapToModel(Session object);


}
