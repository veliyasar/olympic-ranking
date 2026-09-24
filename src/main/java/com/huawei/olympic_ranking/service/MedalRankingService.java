package com.huawei.olympic_ranking.service;

import com.huawei.olympic_ranking.data.CountryRankingDto;
import com.huawei.olympic_ranking.entity.*;
import com.huawei.olympic_ranking.repository.AthleteRepository;
import com.huawei.olympic_ranking.repository.CountryRepository;
import com.huawei.olympic_ranking.repository.MedalRecordRepository;
import com.huawei.olympic_ranking.repository.SportRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class MedalRankingService {

    private final MedalRecordRepository medalRecordRepository;
    private final SportRepository sportRepository;
    private final AthleteRepository athleteRepository;
    private final CountryRepository countryRepository;

    public MedalRankingService(MedalRecordRepository medalRecordRepository,
                               SportRepository sportRepository,
                               AthleteRepository athleteRepository,
                               CountryRepository countryRepository) {
        this.medalRecordRepository = medalRecordRepository;
        this.sportRepository = sportRepository;
        this.athleteRepository = athleteRepository;
        this.countryRepository = countryRepository;
    }

    public Country createCountry(Country country) {
        return countryRepository.save(country);
    }

    public Sport createSport(Sport sport) {
        return sportRepository.save(sport);
    }

    public Athlete createAthlete(Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    public MedalRecord createMedal(MedalRecord medalRecord) {
        return medalRecordRepository.save(medalRecord);
    }

    public List<CountryRankingDto> rankCountries() {
        // count medals per country
        Map<Long, long[]> counts = new HashMap<>();
        for (MedalRecord m : medalRecordRepository.findAll()) {
            long[] countTriplet = counts.computeIfAbsent(m.getCountry().getId(), c -> new long[3]);
            switch (m.getMedalType()) {
                case GOLD -> countTriplet[0]++;
                case SILVER -> countTriplet[1]++;
                case BRONZE -> countTriplet[2]++;
            }
        }

        // Iterate over all countries, build DTOs and sort
        // Country with no medal gets [0, 0, 0]
        List<CountryRankingDto> unranked = countryRepository.findAll().stream()
                .map(country -> {
                    long[] c = counts.getOrDefault(country.getId(), new long[3]);
                    return new CountryRankingDto(0, country.getCountryName(), c[0], c[1], c[2]);
                })
                .sorted(Comparator.comparingLong(CountryRankingDto::gold).reversed() // most gold first
                        .thenComparing(Comparator.comparingLong(CountryRankingDto::silver).reversed()) // if tie, most silver
                        .thenComparing(Comparator.comparingLong(CountryRankingDto::bronze).reversed()) // if tie, most bronze
                        .thenComparing(CountryRankingDto::country)) // if tie, alphabetical
                .toList();

        // assign ranks
        return IntStream.range(0, unranked.size())
                .mapToObj(i -> {
                    CountryRankingDto d = unranked.get(i);
                    return new CountryRankingDto(i + 1, d.country(), d.gold(), d.silver(), d.bronze());
                })
                .toList();
    }
}
