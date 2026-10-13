package com.huawei.olympicranking.service;

import com.huawei.olympicranking.data.CountryRequest;
import com.huawei.olympicranking.data.CountryResponse;
import com.huawei.olympicranking.entity.Country;
import com.huawei.olympicranking.exception.DuplicateResourceException;
import com.huawei.olympicranking.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Transactional
    public CountryResponse createCountry(CountryRequest request) {
        String code = request.code().toUpperCase();
        if (countryRepository.existsByCode(code)) {
            throw new DuplicateResourceException("Country already exists with code: " + code);
        }
        Country country = countryRepository.save(new Country(code, request.name()));
        return new CountryResponse(country.getId(), country.getCode(), country.getName());
    }
}
