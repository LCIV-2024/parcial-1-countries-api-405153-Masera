package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountriesToSaveDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDto;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountryController.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    void getAllCountries() throws Exception {
        CountryDto country1 = new CountryDto("ARG", "Argentina");
        CountryDto country2 = new CountryDto("BRA", "Brazil");
        CountryDto country3 = new CountryDto("NZL", "New Zealand");
        List<CountryDto> countryDtos = new ArrayList<>();
        countryDtos.add(country1);
        countryDtos.add(country2);
        when(countryService.getAllCountriesDto(null, null)).thenReturn(countryDtos);

        mockMvc.perform(get("/api/countries"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[0].code").value("ARG"))
                .andExpect(jsonPath("$[1].name").value("Brazil"))
                .andExpect(jsonPath("$[1].code").value("BRA"));
    }

    @Test
    void getAllCountriesByContinent() throws Exception{

        String continent = "Americas";
        List<CountryDto> countryDtos = new ArrayList<>();
        CountryDto country1 = new CountryDto("ARG", "Argentina");
        CountryDto country2 = new CountryDto("BRA", "Brazil");
        countryDtos.add(country1);
        countryDtos.add(country2);

        when(countryService.getAllCountriesByContinent("Americas")).thenReturn(countryDtos);
        mockMvc.perform(get("/api/countries/{continent}/continent", continent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[0].code").value("ARG"))
                .andExpect(jsonPath("$[1].name").value("Brazil"))
                .andExpect(jsonPath("$[1].code").value("BRA"));

    }

    @Test
    void getCountriesByLanguage() throws Exception {
        String language = "Spanish";
        List<CountryDto> countryDtos = new ArrayList<>();
        CountryDto country1 = new CountryDto("ARG", "Argentina");
        CountryDto country2 = new CountryDto("ESP", "Spain");
        countryDtos.add(country1);
        countryDtos.add(country2);

        when(countryService.getAllCountriesByLanguage(language)).thenReturn(countryDtos);

        mockMvc.perform(get("/api/countries/{language}/language", language))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[0].code").value("ARG"))
                .andExpect(jsonPath("$[1].name").value("Spain"))
                .andExpect(jsonPath("$[1].code").value("ESP"));
    }

    @Test
    void getCountryWithMostBorders() throws Exception{
        String language = "Spanish";
        List<CountryDto> countryDtos = new ArrayList<>();
        CountryDto countryDto = new CountryDto("CHN", "China");

        when(countryService.getCountryWithMostBorders()).thenReturn(countryDto);

        mockMvc.perform(get("/api/countries/most-borders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("China"))
                .andExpect(jsonPath("$.code").value("CHN"));

    }

    @Test
    void saveRandomCountries() throws Exception{
        int cantidad = 2;
        List<CountryDto> countryDtos = new ArrayList<>();
        CountryDto country1 = new CountryDto("ARG", "Argentina");
        CountryDto country2 = new CountryDto("BRA", "Brazil");
        countryDtos.add(country1);
        countryDtos.add(country2);

        CountriesToSaveDto countriesToSaveDto = new CountriesToSaveDto(cantidad);

        when(countryService.saveRandomCountries(cantidad)).thenReturn(countryDtos);

        // Act & Assert
        mockMvc.perform(post("/api/countries")
                        .contentType("application/json")
                        .content("{\"amountOfCountryToSave\": " + cantidad + "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[0].code").value("ARG"))
                .andExpect(jsonPath("$[1].name").value("Brazil"))
                .andExpect(jsonPath("$[1].code").value("BRA"));
    }
}