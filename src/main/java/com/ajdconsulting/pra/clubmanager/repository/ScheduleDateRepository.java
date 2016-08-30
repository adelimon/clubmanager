package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.ScheduleDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the ScheduleDate entity.
 */
public interface ScheduleDateRepository extends JpaRepository<ScheduleDate,Long> {

    @Query("select s from ScheduleDate s order by date")
    public Page<ScheduleDate> findAllOrdered(Pageable pageable);

    @Query("select s from ScheduleDate s where ((s.eventType.type = 'Meeting') or (s.date >= current_date() - 7)) order by date")
    public Page<ScheduleDate> findAllFutureDates(Pageable pageable);
}
