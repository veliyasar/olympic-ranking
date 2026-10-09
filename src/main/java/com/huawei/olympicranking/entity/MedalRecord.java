package com.huawei.olympicranking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medal_record")
@Getter
@Setter
public class MedalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "athlete_id")
    private Athlete athlete;

    @Enumerated(EnumType.STRING)
    @Column(name = "medal_type")
    private MedalType medalType;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
}
