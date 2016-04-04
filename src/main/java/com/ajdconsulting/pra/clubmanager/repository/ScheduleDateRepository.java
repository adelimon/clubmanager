package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.ScheduleDate;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ScheduleDate entity.
 */
public interface ScheduleDateRepository extends JpaRepository<ScheduleDate,Long> {

    @Query("select distinct scheduleDate from ScheduleDate scheduleDate left join fetch scheduleDate.eventTypes")
    List<ScheduleDate> findAllWithEagerRelationships();

    @Query("select scheduleDate from ScheduleDate scheduleDate left join fetch scheduleDate.eventTypes where scheduleDate.id =:id")
    ScheduleDate findOneWithEagerRelationships(@Param("id") Long id);

}
