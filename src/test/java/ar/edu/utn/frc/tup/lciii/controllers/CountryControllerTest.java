package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDto;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(CountryController.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    void getAllCountries() {
        CountryDto country1 = new CountryDto("Argentina", "ARG");
        CountryDto country2 = new CountryDto("Brasil", "BRA");
        List<CountryDto> countryDtos = new ArrayList<>();
        countryDtos.add(country1);
        countryDtos.add(country2);
        when(countryService.getAllCountriesDto(null, null)).thenReturn(countryDtos);

        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[0].code").value("ARG"))
                .andExpect(jsonPath("$[1].name").value("Brasil"))
                .andExpect(jsonPath("$[1].code").value("BRA"));
    }

    @Test
    void getAllCountriesByContinent() {
        
    }

    @Test
    void getCountriesByLanguage() {
    }

    @Test
    void getCountryWithMostBorders() {
    }

    @Test
    void saveRandomCountries() {
    }
}