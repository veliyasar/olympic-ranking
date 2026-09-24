package com.huawei.olympic_ranking.controller;

import com.huawei.olympic_ranking.data.CountryRankingDto;
import com.huawei.olympic_ranking.entity.Athlete;
import com.huawei.olympic_ranking.entity.Country;
import com.huawei.olympic_ranking.entity.MedalRecord;
import com.huawei.olympic_ranking.entity.Sport;
import com.huawei.olympic_ranking.service.MedalRankingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MedalRankingController {

    private final MedalRankingService medalRankingService;

    public MedalRankingController(MedalRankingService medalRankingService) {;
        this.medalRankingService = medalRankingService;
    }

    @PostMapping("/createCountry")
    public ResponseEntity<Country> createCountry(@RequestBody Country country) {
        Country savedCountry = medalRankingService.createCountry(country);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCountry);
    }

    @PostMapping("/sports")
    public ResponseEntity<Sport> createSport(@RequestBody Sport sport) {
        Sport savedSport = medalRankingService.createSport(sport);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSport);
    }

    @PostMapping("/athletes")
    public ResponseEntity<Athlete> createAthlete(@RequestBody Athlete athlete) {
        Athlete savedAthlete = medalRankingService.createAthlete(athlete);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAthlete);
    }

    @PostMapping("/medals")
    public ResponseEntity<MedalRecord> createMedal(@RequestBody MedalRecord medalRecord) {
        MedalRecord savedRecord = medalRankingService.createMedal(medalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecord);
    }

    @GetMapping("/country-rankings")
    public ResponseEntity<List<CountryRankingDto>> getCountryRanking() {
        List<CountryRankingDto> rankings = medalRankingService.rankCountries();
        return ResponseEntity.ok(rankings);
    }
}
