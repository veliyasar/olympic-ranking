package com.huawei.olympicranking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "athlete")
@Getter
@Setter
public class Athlete {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "athlete_name")
    private String athleteName;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
}
