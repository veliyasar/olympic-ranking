package com.huawei.olympicranking.service;

import com.huawei.olympicranking.data.SportRequest;
import com.huawei.olympicranking.data.SportResponse;
import com.huawei.olympicranking.entity.Sport;
import com.huawei.olympicranking.exception.DuplicateResourceException;
import com.huawei.olympicranking.repository.SportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SportService {

    private final SportRepository sportRepository;

    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    @Transactional
    public SportResponse createSport(SportRequest request) {
        if (sportRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Sport already exists with name: " + request.name());
        }
        Sport sport = sportRepository.save(new Sport(request.name()));
        return new SportResponse(sport.getId(), sport.getName());
    }
}
