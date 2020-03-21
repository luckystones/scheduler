package com.ugur.dao;

import com.ugur.model.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public interface ScheduleRepository
    extends JpaRepository<SessionEntity, Long>, JpaSpecificationExecutor<SessionEntity> {
}
