package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.MemberYearlyDues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by adelimon on 4/12/2017.
 */
public interface MemberYearlyDuesRepository extends JpaRepository<MemberYearlyDues,Long> {

    @Query("select myd from MemberYearlyDues myd where myd.year = :year")
    public List<MemberYearlyDues> findForYear(@Param("year") int year);

    @Query("select myd from MemberYearlyDues myd where myd.member.id = :memberId")
    public List<MemberYearlyDues> findForMember(@Param("memberId") long memberId);

}
