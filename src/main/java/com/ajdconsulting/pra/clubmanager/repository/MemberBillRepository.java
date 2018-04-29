package com.ajdconsulting.pra.clubmanager.repository;

import com.ajdconsulting.pra.clubmanager.domain.Member;
import com.ajdconsulting.pra.clubmanager.domain.MemberBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberBillRepository extends JpaRepository<MemberBill, Long> {

    @Query("select b from MemberBill b where b.isSent = false and b.year = :year order by b.member.id, b.generatedDate")
    public List<MemberBill> getUnsentBillsByYear(@Param("year") int year);

}
