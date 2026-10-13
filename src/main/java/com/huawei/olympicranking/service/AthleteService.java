package com.huawei.olympicranking.service;

import com.huawei.olympicranking.data.AthleteRequest;
import com.huawei.olympicranking.data.AthleteResponse;
import com.huawei.olympicranking.entity.Athlete;
import com.huawei.olympicranking.entity.Country;
import com.huawei.olympicranking.exception.ResourceNotFoundException;
import com.huawei.olympicranking.repository.AthleteRepository;
import com.huawei.olympicranking.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AthleteService {

    private final AthleteRepository athleteRepository;
    private final CountryRepository countryRepository;

    public AthleteService(AthleteRepository athleteRepository, CountryRepository countryRepository) {
        this.athleteRepository = athleteRepository;
        this.countryRepository = countryRepository;
    }

    @Transactional
    public AthleteResponse createAthlete(AthleteRequest request) {
        Country country = countryRepository.findById(request.countryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country", request.countryId()));
        Athlete athlete = athleteRepository.save(new Athlete(request.name(), country));
        return new AthleteResponse(athlete.getId(), athlete.getName(), country.getCode());
    }
}
