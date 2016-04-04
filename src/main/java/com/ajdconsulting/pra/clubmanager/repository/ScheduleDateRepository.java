package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.ScheduleDate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ScheduleDate entity.
 */
public interface ScheduleDateRepository extends JpaRepository<ScheduleDate,Long> {

}
