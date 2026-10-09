package com.huawei.olympicranking.repository;

import com.huawei.olympicranking.entity.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {

}
