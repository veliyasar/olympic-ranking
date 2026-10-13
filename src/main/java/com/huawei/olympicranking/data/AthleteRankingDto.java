package com.huawei.olympicranking.data;

public record AthleteRankingDto(int rank, String athlete, String country, long total, long gold, long silver,
                                long bronze) {
}
