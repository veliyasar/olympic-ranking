package com.huawei.olympicranking.controller;

import com.huawei.olympicranking.data.SportRequest;
import com.huawei.olympicranking.data.SportResponse;
import com.huawei.olympicranking.service.SportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sports")
public class SportController {
    private final SportService sportService;

    public SportController(SportService sportService) {
        this.sportService = sportService;
    }


    @PostMapping
    public ResponseEntity<SportResponse> createSport(@Valid @RequestBody SportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sportService.createSport(request));
    }
}
