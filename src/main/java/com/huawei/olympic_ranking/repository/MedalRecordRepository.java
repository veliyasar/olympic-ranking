package com.huawei.olympic_ranking.repository;

import com.huawei.olympic_ranking.entity.MedalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedalRecordRepository extends JpaRepository<MedalRecord, Long> {

}
