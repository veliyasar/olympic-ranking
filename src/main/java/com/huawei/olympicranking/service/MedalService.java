package com.huawei.olympicranking.service;

import com.huawei.olympicranking.data.MedalRequest;
import com.huawei.olympicranking.data.MedalResponse;
import com.huawei.olympicranking.entity.Athlete;
import com.huawei.olympicranking.entity.Medal;
import com.huawei.olympicranking.entity.Sport;
import com.huawei.olympicranking.exception.DuplicateMedalException;
import com.huawei.olympicranking.exception.ResourceNotFoundException;
import com.huawei.olympicranking.repository.AthleteRepository;
import com.huawei.olympicranking.repository.MedalRepository;
import com.huawei.olympicranking.repository.SportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedalService {

    private final MedalRepository medalRepository;
    private final AthleteRepository athleteRepository;
    private final SportRepository sportRepository;

    public MedalService(MedalRepository medalRepository,
                        AthleteRepository athleteRepository,
                        SportRepository sportRepository) {
        this.medalRepository = medalRepository;
        this.athleteRepository = athleteRepository;
        this.sportRepository = sportRepository;
    }

    @Transactional
    public MedalResponse createMedal(MedalRequest request) {
        Athlete athlete = athleteRepository.findById(request.athleteId())
                .orElseThrow(() -> new ResourceNotFoundException("Athlete", request.athleteId()));
        Sport sport = sportRepository.findById(request.sportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport", request.sportId()));

        if (medalRepository.existsByAthleteIdAndSportIdAndMedalType(request.athleteId(), request.sportId(), request.medalType())) {
            throw new DuplicateMedalException(request.athleteId(), request.sportId(), request.medalType().name());
        }

        // medal is credited to the country with the representing athlete
        Medal medal = medalRepository.save(new Medal(athlete, sport, request.medalType(), athlete.getCountry()));
        return new MedalResponse(medal.getId(), athlete.getName(), sport.getName(),
                medal.getMedalType(), athlete.getCountry().getCode());
    }
}
