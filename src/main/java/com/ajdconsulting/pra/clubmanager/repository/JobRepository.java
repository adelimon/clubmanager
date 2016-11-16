package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.Job;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Job entity.
 */
public interface JobRepository extends JpaRepository<Job,Long> {

    @Query("select j from Job j where reserved = false and online=true")
    public Page<Job> findAvailableJobs(Pageable pageable);

    @Query("select j from Job j where cash_value > 0 and online=true")
    public Page<Job> findPaidJobs(Pageable pageable);

    @Query("select j from Job j where j.online=true and j.eventType.id = :eventTypeId and j.id not in "+
        "(select s.job.id from Signup s where s.scheduleDate.id = :scheduleDateId) order by j.title")
    public Page<Job> findOpenJobsForDate(Pageable pageable,
        @Param("eventTypeId") Long eventTypeId,
        @Param("scheduleDateId") Long scheduleDateId);

    @Query("select j from Job j where j.online=true and j.eventType.id = :eventTypeId  and j.reserved = false and j.id not in "+
        "(select s.job.id from Signup s where s.scheduleDate.id = :scheduleDateId) order by j.title")
    public Page<Job> findOpenJobsForDateNoReserved(Pageable pageable,
                                         @Param("eventTypeId") Long eventTypeId,
                                         @Param("scheduleDateId") Long scheduleDateId);

    @Query("select j from Job j where j.online=true and j.eventType.id = :eventTypeId")
    public Page<Job> findOpenJobsByType(Pageable pageable,
        @Param("eventTypeId") Long eventTypeId);
}
