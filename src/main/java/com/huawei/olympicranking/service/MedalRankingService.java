package com.huawei.olympicranking.service;

import com.huawei.olympicranking.data.AthleteRankingDto;
import com.huawei.olympicranking.data.CountryRankingDto;
import com.huawei.olympicranking.data.SportRankingDto;
import com.huawei.olympicranking.entity.Medal;
import com.huawei.olympicranking.repository.AthleteRepository;
import com.huawei.olympicranking.repository.CountryRepository;
import com.huawei.olympicranking.repository.MedalRepository;
import com.huawei.olympicranking.repository.SportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

@Service
public class MedalRankingService {

    private static final int GOLD_IDX = 0, SILVER_IDX = 1, BRONZE_IDX = 2;

    private final MedalRepository medalRepository;
    private final SportRepository sportRepository;
    private final AthleteRepository athleteRepository;
    private final CountryRepository countryRepository;

    public MedalRankingService(MedalRepository medalRepository,
                               SportRepository sportRepository,
                               AthleteRepository athleteRepository,
                               CountryRepository countryRepository) {
        this.medalRepository = medalRepository;
        this.sportRepository = sportRepository;
        this.athleteRepository = athleteRepository;
        this.countryRepository = countryRepository;
    }

    @Transactional(readOnly = true)
    public List<CountryRankingDto> rankCountries() {
        Map<Long, long[]> counts = countMedalsBy(m -> m.getCountry().getId());

        // country with no medal gets [0, 0, 0]
        List<CountryRankingDto> sorted = countryRepository.findAll().stream()
                .map(country -> {
                    long[] c = counts.getOrDefault(country.getId(), new long[3]);
                    return new CountryRankingDto(0, country.getName(), c[GOLD_IDX], c[SILVER_IDX], c[BRONZE_IDX]);
                })
                .sorted(Comparator.comparingLong(CountryRankingDto::gold).reversed() // most gold first
                        .thenComparing(Comparator.comparingLong(CountryRankingDto::silver).reversed()) // if tie, most silver
                        .thenComparing(Comparator.comparingLong(CountryRankingDto::bronze).reversed()) // if tie, most bronze
                        .thenComparing(CountryRankingDto::country)) // if tie, alphabetical
                .toList();

        return IntStream.range(0, sorted.size())
                .mapToObj(i -> {
                    CountryRankingDto d = sorted.get(i);
                    return new CountryRankingDto(i + 1, d.country(), d.gold(), d.silver(), d.bronze());
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AthleteRankingDto> rankAthletes() {
        Map<Long, long[]> counts = countMedalsBy(m -> m.getAthlete().getId());

        List<AthleteRankingDto> sorted = athleteRepository.findAllWithCountry().stream()
                .map(athlete -> {
                    long[] c = counts.getOrDefault(athlete.getId(), new long[3]);
                    long total = c[GOLD_IDX] + c[SILVER_IDX] + c[BRONZE_IDX];
                    return new AthleteRankingDto(0, athlete.getName(), athlete.getCountry().getCode(),
                            total, c[GOLD_IDX], c[SILVER_IDX], c[BRONZE_IDX]);
                })
                .sorted(Comparator.comparingLong(AthleteRankingDto::total).reversed() // most medals first
                        .thenComparing(Comparator.comparingLong(AthleteRankingDto::gold).reversed())
                        .thenComparing(Comparator.comparingLong(AthleteRankingDto::silver).reversed())
                        .thenComparing(Comparator.comparingLong(AthleteRankingDto::bronze).reversed())
                        .thenComparing(AthleteRankingDto::athlete))
                .toList();

        return IntStream.range(0, sorted.size())
                .mapToObj(i -> {
                    AthleteRankingDto d = sorted.get(i);
                    return new AthleteRankingDto(i + 1, d.athlete(), d.country(), d.total(), d.gold(), d.silver(), d.bronze());
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SportRankingDto> rankSports() {
        Map<Long, long[]> counts = countMedalsBy(m -> m.getSport().getId());

        List<SportRankingDto> sorted = sportRepository.findAll().stream()
                .map(sport -> new SportRankingDto(0, sport.getName(),
                        Arrays.stream(counts.getOrDefault(sport.getId(), new long[3])).sum()))
                .sorted(Comparator.comparingLong(SportRankingDto::totalMedals).reversed()
                        .thenComparing(SportRankingDto::sport))
                .toList();

        return IntStream.range(0, sorted.size())
                .mapToObj(i -> new SportRankingDto(i + 1, sorted.get(i).sport(), sorted.get(i).totalMedals()))
                .toList();
    }

    // groups all medals by the given key and counts (gold, silver, bronze) per key
    private Map<Long, long[]> countMedalsBy(Function<Medal, Long> keyExtractor) {
        Map<Long, long[]> counts = new HashMap<>();
        for (Medal m : medalRepository.findAll()) {
            long[] countTriplet = counts.computeIfAbsent(keyExtractor.apply(m), k -> new long[3]);
            switch (m.getMedalType()) {
                case GOLD -> countTriplet[GOLD_IDX]++;
                case SILVER -> countTriplet[SILVER_IDX]++;
                case BRONZE -> countTriplet[BRONZE_IDX]++;
            }
        }
        return counts;
    }
}
