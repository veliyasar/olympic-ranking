package com.huawei.olympicranking.controller;

import com.huawei.olympicranking.data.AthleteRequest;
import com.huawei.olympicranking.data.AthleteResponse;
import com.huawei.olympicranking.service.AthleteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/athletes")
public class AthleteController {

    private final AthleteService athleteService;

    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    @PostMapping
    public ResponseEntity<AthleteResponse> createAthlete(@Valid @RequestBody AthleteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(athleteService.createAthlete(request));
    }
}