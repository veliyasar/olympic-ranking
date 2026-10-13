package com.huawei.olympicranking.data;

import com.huawei.olympicranking.entity.MedalType;

public record MedalResponse(Long id, String athlete, String sport, MedalType medalType, String countryCode) {}
