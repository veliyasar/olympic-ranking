package com.huawei.olympicranking.repository;

import com.huawei.olympicranking.entity.Medal;
import com.huawei.olympicranking.entity.MedalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedalRepository extends JpaRepository<Medal, Long> {

    boolean existsByAthleteIdAndSportIdAndMedalType(Long athleteId, Long sportId, MedalType medalType);
}
