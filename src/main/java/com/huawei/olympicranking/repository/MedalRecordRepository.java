package com.huawei.olympicranking.repository;

import com.huawei.olympicranking.entity.MedalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedalRecordRepository extends JpaRepository<MedalRecord, Long> {

}
