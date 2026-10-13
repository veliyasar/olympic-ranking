package com.huawei.olympicranking.data;

import com.huawei.olympicranking.entity.MedalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MedalRequest(
        @NotNull @Positive Long athleteId,
        @NotNull @Positive Long sportId,
        @NotNull MedalType medalType) {}
