package com.huawei.olympicranking.controller;

import com.huawei.olympicranking.data.CountryRequest;
import com.huawei.olympicranking.data.CountryResponse;
import com.huawei.olympicranking.service.CountryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
    public ResponseEntity<CountryResponse> createCountry(@Valid @RequestBody CountryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(countryService.createCountry(request));
    }
}
