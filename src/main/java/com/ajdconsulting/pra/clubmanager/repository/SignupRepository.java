package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.Signup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Signup entity.
 */
public interface SignupRepository extends JpaRepository<Signup,Long> {
    @Query("select sr from Signup sr where sr.scheduleDate.date >= current_date() order by sr.scheduleDate.date, sr.job.sortOrder")
    public Page<Signup> findFutureSignups(Pageable pageable);

    @Query("select sr from Signup sr where sr.worker.id = :id")
    public List<Signup> findByWorkerId(@Param("id") long id);
}
