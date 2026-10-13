package com.huawei.olympicranking.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AthleteRequest(
        @NotBlank String name,
        @NotNull @Positive Long countryId) {
}
