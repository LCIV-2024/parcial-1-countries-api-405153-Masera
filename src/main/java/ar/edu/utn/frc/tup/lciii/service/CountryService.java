package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDto;
import ar.edu.utn.frc.tup.lciii.entities.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

        @Autowired
        private CountryRepository countryRepository;

        @Autowired
        private ModelMapper modelMapper;

        private final RestTemplate restTemplate;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }

        public List<CountryDto> getAllCountriesDto(String name, String code) {
                List<Country> countries = getAllCountries();
                List<CountryDto> countriesDto = new ArrayList<>();

                if(name == null && code == null){
                        return countries.stream().map(this::mapToDTO).collect(Collectors.toList());

                }else {
                        for (Country country : countries) {
                                if(name != null){
                                        if (country.getName().toLowerCase().contains(name.toLowerCase())) {
                                                countriesDto.add(mapToDTO(country));
                                        }
                                }
                                if (code != null) {
                                        if (country.getCode().toLowerCase().contains(code.toLowerCase())) {
                                                countriesDto.add(mapToDTO(country));
                                        }
                                }

                        }
                }
                return countriesDto;
        }

        public List<CountryDto> getAllCountriesByContinent(String continent) {
                List<Country> countries = getAllCountries();
                List<CountryDto> countriesDto = new ArrayList<>();

                for (Country country : countries) {
                        if (country.getRegion().toLowerCase().contains(continent.toLowerCase())) {
                                countriesDto.add(mapToDTO(country));
                        }
                }
                return countriesDto;
        }

        public List<CountryDto> getAllCountriesByLanguage(String language) {
                List<Country> countries = getAllCountries();
                List<CountryDto> countriesDto = new ArrayList<>();

                for (Country country : countries) {
                        if (country.getLanguages() != null) {
                                List<String> values = country.getLanguages().values().stream().toList();
                                if (values.contains(language)) {
                                        countriesDto.add(mapToDTO(country));
                                }
                        }
                }
                return countriesDto;
        }

        public CountryDto getCountryWithMostBorders() {
                List<Country> countries = getAllCountries();
                Country countryMostBorder = null;
                int max = 0;

                for (Country country : countries) {
                        int borders = country.getBorders() != null ? country.getBorders().size() : 0;
                        if (borders > max) {
                                max = borders;
                                countryMostBorder = country;
                        }
                }

                CountryDto countryDto = mapToDTO(countryMostBorder);

                return countryDto;
        }

        public List<CountryDto> saveRandomCountries(int countryToSave) {
                List<Country> countries = getAllCountries();
                List<CountryEntity> countryEntities = new ArrayList<>();

                for (int i = 0; i < countryToSave; i++) {
                        int random = (int) (Math.random() * countries.size());
                        countryEntities.add(modelMapper.map(countries.get(random), CountryEntity.class));
                }

                List<CountryEntity> countryEntitiesSaved = countryRepository.saveAll(countryEntities);
                List<CountryDto> countriesDto = new ArrayList<>();
                for (CountryEntity countryEntity : countryEntitiesSaved) {
                        countriesDto.add(modelMapper.map(countryEntity, CountryDto.class));
                }

                return countriesDto;
        }
        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                return Country.builder()
                        .name((String) nameData.get("common"))
                        .code((String) countryData.get("cca3"))
                        .borders((List<String>) countryData.get("borders"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .languages((Map<String, String>) countryData.get("languages"))
                        .build();
        }

        private CountryDto mapToDTO(Country country) {
                return new CountryDto(country.getCode(), country.getName());
        }
}