package com.huawei.olympicranking.service;

import com.huawei.olympicranking.data.MedalRequest;
import com.huawei.olympicranking.data.MedalResponse;
import com.huawei.olympicranking.entity.*;
import com.huawei.olympicranking.exception.DuplicateMedalException;
import com.huawei.olympicranking.exception.ResourceNotFoundException;
import com.huawei.olympicranking.repository.AthleteRepository;
import com.huawei.olympicranking.repository.CountryRepository;
import com.huawei.olympicranking.repository.MedalRepository;
import com.huawei.olympicranking.repository.SportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedalServiceTest {

    @Mock
    private MedalRepository medalRepository;
    @Mock
    private SportRepository sportRepository;
    @Mock
    private AthleteRepository athleteRepository;
    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private MedalService medalService;

    private final Country turkiye = new Country("TUR", "Turkiye");
    private final Athlete gazoz = new Athlete("Mete Gazoz", turkiye);
    private final Sport archery = new Sport("Archery");

    @Test
    void shouldSaveMedal_WhenValidInput() {
        when(athleteRepository.findById(1L)).thenReturn(Optional.of(gazoz));
        when(sportRepository.findById(2L)).thenReturn(Optional.of(archery));
        when(medalRepository.existsByAthleteIdAndSportIdAndMedalType(1L, 2L, MedalType.GOLD)).thenReturn(false);
        when(medalRepository.save(any(Medal.class))).thenAnswer(inv -> inv.getArgument(0));

        MedalResponse response = medalService.createMedal(new MedalRequest(1L, 2L, MedalType.GOLD));

        assertThat(response.athlete()).isEqualTo("Mete Gazoz");
        assertThat(response.sport()).isEqualTo("Archery");
        assertThat(response.medalType()).isEqualTo(MedalType.GOLD);
        assertThat(response.countryCode()).isEqualTo("TUR");
        verify(medalRepository).save(any(Medal.class));
    }

    @Test
    void shouldThrowException_WhenDuplicateMedal() {
        when(athleteRepository.findById(1L)).thenReturn(Optional.of(gazoz));
        when(sportRepository.findById(2L)).thenReturn(Optional.of(archery));
        when(medalRepository.existsByAthleteIdAndSportIdAndMedalType(1L, 2L, MedalType.GOLD)).thenReturn(true);

        assertThatThrownBy(() -> medalService.createMedal(new MedalRequest(1L, 2L, MedalType.GOLD)))
                .isInstanceOf(DuplicateMedalException.class);
        verify(medalRepository, never()).save(any());
    }

    @Test
    void shouldThrowException_WhenAthleteNotFound() {
        when(athleteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medalService.createMedal(new MedalRequest(99L, 2L, MedalType.GOLD)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Athlete not found with id: 99");
        verify(medalRepository, never()).save(any());
    }
}
