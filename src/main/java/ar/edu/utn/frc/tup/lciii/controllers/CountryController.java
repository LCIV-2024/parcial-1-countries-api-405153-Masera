package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountriesToSaveDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDto;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<List<CountryDto>> getAllCountries(@RequestParam(required = false) String name,
                                            @RequestParam(required = false) String code) {
        List<CountryDto> countryDtos = countryService.getAllCountriesDto(name, code);
        if (countryDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(countryDtos);
    }

    @GetMapping("/{continent}/continent")
    public ResponseEntity<List<CountryDto>> getAllCountriesByContinent(@PathVariable String continent) {
        List<CountryDto> countryDtos = countryService.getAllCountriesByContinent(continent);
        if (countryDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(countryDtos);
    }

    @GetMapping("/{language}/language")
    public ResponseEntity<List<CountryDto>> getCountriesByLanguage(@PathVariable String language) {
        List<CountryDto> countryDtos = countryService.getAllCountriesByLanguage(language);
        if (countryDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(countryDtos);
    }

    @GetMapping("/most-borders")
    public ResponseEntity<CountryDto> getCountryWithMostBorders() {
        return ResponseEntity.ok(countryService.getCountryWithMostBorders());
    }

    @PostMapping
    public ResponseEntity<List<CountryDto>> saveRandomCountries(@RequestBody CountriesToSaveDto countriesToSave) {
        return ResponseEntity.ok(countryService.saveRandomCountries(countriesToSave.getAmountOfCountryToSave()));
    }
}