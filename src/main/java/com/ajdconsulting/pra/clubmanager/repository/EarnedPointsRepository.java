package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the EarnedPoints entity.
 */
public interface EarnedPointsRepository extends JpaRepository<EarnedPoints,Long> {

    @Query("select e from EarnedPoints e where e.member.email = :userId order by e.date")
    public Page<EarnedPoints> findForUser(Pageable pageable, @Param("userId") String userId);

    @Query("select e from EarnedPoints e where e.verified = false and e.member.status <> 9 order by e.date, e.verified, e.description")
    public Page<EarnedPoints> findAllOrdered(Pageable pageable);

    @Query("select e from EarnedPoints e where e.verified = false and e.member.status <> 9 order by e.member.lastName")
    public Page<EarnedPoints> findUnverified(Pageable pageable);
}
