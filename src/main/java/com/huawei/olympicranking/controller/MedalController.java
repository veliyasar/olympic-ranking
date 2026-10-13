package com.huawei.olympicranking.controller;

import com.huawei.olympicranking.data.MedalRequest;
import com.huawei.olympicranking.data.MedalResponse;
import com.huawei.olympicranking.service.MedalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medals")
public class MedalController {

    private final MedalService medalService;

    public MedalController(MedalService medalService) { this.medalService = medalService; }

    @PostMapping
    public ResponseEntity<MedalResponse> createMedal(@Valid @RequestBody MedalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medalService.createMedal(request));
    }
}
