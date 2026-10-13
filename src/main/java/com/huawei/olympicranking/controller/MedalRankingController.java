package com.huawei.olympicranking.controller;

import com.huawei.olympicranking.data.*;
import com.huawei.olympicranking.service.MedalRankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rankings")
public class MedalRankingController {

    private final MedalRankingService medalRankingService;

    public MedalRankingController(MedalRankingService medalRankingService) {
        this.medalRankingService = medalRankingService;
    }

    @GetMapping("/countries")
    public ResponseEntity<List<CountryRankingDto>> getCountryRanking() {
        return ResponseEntity.ok(medalRankingService.rankCountries());
    }

    @GetMapping("/athletes")
    public ResponseEntity<List<AthleteRankingDto>> getAthleteRanking() {
        return ResponseEntity.ok(medalRankingService.rankAthletes());
    }

    @GetMapping("/sports")
    public ResponseEntity<List<SportRankingDto>> getSportRanking() {
        return ResponseEntity.ok(medalRankingService.rankSports());
    }
}
