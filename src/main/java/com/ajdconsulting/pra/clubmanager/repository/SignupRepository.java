package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.Signup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Signup entity.
 */
public interface SignupRepository extends JpaRepository<Signup,Long> {
    @Query("select sr from Signup sr where sr.scheduleDate.date >= current_date()")
    public Page<Signup> findFutureSignups(Pageable pageable);
}
