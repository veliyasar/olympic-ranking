package com.huawei.olympicranking.data;

import jakarta.validation.constraints.NotBlank;

public record SportRequest(@NotBlank String name) {}
