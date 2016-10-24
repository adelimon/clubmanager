package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.EarnedPoints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the EarnedPoints entity.
 */
public interface EarnedPointsRepository extends JpaRepository<EarnedPoints,Long> {

    @Query("select e from EarnedPoints e where e.member.email = :userId order by e.date")
    public Page<EarnedPoints> findForUser(Pageable pageable, @Param("userId") String userId);

    @Query("select e from EarnedPoints e where e.member.id = :memberId order by e.date")
    public Page<EarnedPoints> findForMemberId(Pageable pageable, @Param("memberId") Long memberId);

    //@Query("select e from EarnedPoints e where e.")
    //public Page<EarnedPoints> findWorkLeaderVerifications(Pageable pageable, @Param("userId") String userId);

    @Query("select e from EarnedPoints e where e.verified = false and e.member.status <> 9 and e.date < now() " +
        "order by e.member.lastName")
    public Page<EarnedPoints> findAllOrdered(Pageable pageable);

    @Query("select e from EarnedPoints e where e.verified = false and e.member.status <> 9 order by e.member.lastName")
    public Page<EarnedPoints> findUnverified(Pageable pageable);

    @Query("select e from EarnedPoints e where e.member.id = :id")
    public List<EarnedPoints> findByMemberId(@Param("id") long id);
}
