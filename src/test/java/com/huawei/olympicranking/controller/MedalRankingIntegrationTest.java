package com.huawei.olympicranking.controller;

import com.huawei.olympicranking.entity.*;
import com.huawei.olympicranking.repository.AthleteRepository;
import com.huawei.olympicranking.repository.CountryRepository;
import com.huawei.olympicranking.repository.MedalRepository;
import com.huawei.olympicranking.repository.SportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // each test rolls back its data
class MedalRankingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private SportRepository sportRepository;
    @Autowired
    private AthleteRepository athleteRepository;
    @Autowired
    private MedalRepository medalRepository;

    private Athlete gazoz;
    private Sport archery;

    // sample data
    @BeforeEach
    void setUp() {
        Country tur = countryRepository.save(new Country("TUR", "Turkiye"));
        Country usa = countryRepository.save(new Country("USA", "United States"));
        Country jpn = countryRepository.save(new Country("JPN", "Japan"));

        archery = sportRepository.save(new Sport("Archery"));
        Sport swimming = sportRepository.save(new Sport("Swimming"));
        Sport judo = sportRepository.save(new Sport("Judo"));

        gazoz = athleteRepository.save(new Athlete("Mete Gazoz", tur));
        Athlete lyles = athleteRepository.save(new Athlete("Noah Lyles", usa));
        athleteRepository.save(new Athlete("Abby Steuter", usa));
        Athlete abe = athleteRepository.save(new Athlete("Hifumi Abe", jpn));

        medalRepository.save(new Medal(gazoz, archery, MedalType.GOLD, tur));
        medalRepository.save(new Medal(gazoz, archery, MedalType.SILVER, tur));
        medalRepository.save(new Medal(lyles, swimming, MedalType.GOLD, usa));
        medalRepository.save(new Medal(lyles, swimming, MedalType.BRONZE, usa));
        medalRepository.save(new Medal(abe, judo, MedalType.GOLD, jpn));
    }

    @Test
    void shouldReturnCountriesRankedByGoldFirst() throws Exception {
        mockMvc.perform(get("/api/v1/rankings/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rank").value(1))
                .andExpect(jsonPath("$[0].country").value("Turkiye"))
                .andExpect(jsonPath("$[1].country").value("United States"))
                .andExpect(jsonPath("$[2].country").value("Japan"));
    }

    @Test
    void shouldReturnAthletesRankedByTotalFirst() throws Exception {
        // Gazoz and Lyles both have 2 medals and 1 gold -> Gazoz wins on silver
        mockMvc.perform(get("/api/v1/rankings/athletes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].athlete").value("Mete Gazoz"))
                .andExpect(jsonPath("$[1].athlete").value("Noah Lyles"))
                .andExpect(jsonPath("$[2].athlete").value("Hifumi Abe"))
                .andExpect(jsonPath("$[3].athlete").value("Abby Steuter"))
                .andExpect(jsonPath("$[3].total").value(0));
    }

    @Test
    void shouldReturnSportsRankedByTotalThenName() throws Exception {
        mockMvc.perform(get("/api/v1/rankings/sports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sport").value("Archery"))
                .andExpect(jsonPath("$[0].totalMedals").value(2))
                .andExpect(jsonPath("$[1].sport").value("Swimming"))
                .andExpect(jsonPath("$[2].sport").value("Judo"));
    }

    @Test
    void shouldReturn409_WhenDuplicateMedal() throws Exception {
        String body = """
                {"athleteId": %d, "sportId": %d, "medalType": "GOLD"}
                """.formatted(gazoz.getId(), archery.getId());

        mockMvc.perform(post("/api/v1/medals")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"));
    }

    @Test
    void shouldReturn404_WhenAthleteDoesNotExist() throws Exception {
        String body = """
                {"athleteId": 999999, "sportId": %d, "medalType": "GOLD"}
                """.formatted(archery.getId());

        mockMvc.perform(post("/api/v1/medals")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Athlete not found with id: 999999"))
                .andExpect(jsonPath("$.path").value("/api/v1/medals"));
    }

    @Test
    void shouldReturn400_WhenRequiredFieldsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/medals")
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
