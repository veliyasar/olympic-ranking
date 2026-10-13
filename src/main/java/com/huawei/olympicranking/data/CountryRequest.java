package com.huawei.olympicranking.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CountryRequest(
        @NotBlank @Size(min = 3, max = 3) String code,
        @NotBlank String name) {}
