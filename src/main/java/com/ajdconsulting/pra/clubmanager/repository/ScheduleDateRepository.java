package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.ScheduleDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ScheduleDate entity.
 */
public interface ScheduleDateRepository extends JpaRepository<ScheduleDate,Long> {

    @Query("select s from ScheduleDate s order by date")
    public Page<ScheduleDate> findAllOrdered(Pageable pageable);

    @Query("select s from ScheduleDate s where s.date >= current_date() order by date")
    public Page<ScheduleDate> findAllFutureDates(Pageable pageable);
}
