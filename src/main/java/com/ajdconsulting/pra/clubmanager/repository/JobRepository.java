package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.Job;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Job entity.
 */
public interface JobRepository extends JpaRepository<Job,Long> {

    @Query("select distinct job from Job job left join fetch job.eventTypes")
    List<Job> findAllWithEagerRelationships();

    @Query("select job from Job job left join fetch job.eventTypes where job.id =:id")
    Job findOneWithEagerRelationships(@Param("id") Long id);

}
