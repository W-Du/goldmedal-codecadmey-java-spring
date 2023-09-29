package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import org.apache.commons.text.WordUtils;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.codecademy.goldmedal.repository.*;


@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    // TODO: declare references to your repositories
    private final GoldMedalRepository goldMedalRepository;
    private final CountryRepository countryRepository;

    // TODO: update your constructor to include your repositories
    public GoldMedalController(GoldMedalRepository goldMedalRepository, CountryRepository countryRepository) {
        this.goldMedalRepository = goldMedalRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
            case "year":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryByOrderByYearAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryByOrderByYearDesc(countryName);
                }
                // TODO: list of medals sorted by year in the given order
                break;
            case "season":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryByOrderBySeasonAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryByOrderBySeasonDesc(countryName);
                }
                // TODO: list of medals sorted by season in the given order
                break;
            case "city":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryByOrderByCityAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryByOrderByCityDesc(countryName);
                }
                // TODO: list of medals sorted by city in the given order
                break;
            case "name":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryByOrderByNameAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryByOrderByNameDesc(countryName);
                }
                // TODO: list of medals sorted by athlete's name in the given order
                break;
            case "event":
                if (ascendingOrder) {
                    medalsList = this.goldMedalRepository.findByCountryByOrderByEventAsc(countryName);
                } else {
                    medalsList = this.goldMedalRepository.findByCountryByOrderByEventDesc(countryName);
                }
                // TODO: list of medals sorted by event in the given order
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        Optional<Country> countryOptional = this.countryRepository.findByName(countryName);
        // TODO: get the country; this repository method should return a java.util.Optional

        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }
        Country country = countryOptional.get();
        int goldMedalCount = this.goldMedalRepository.findByCountry(countryName).size();
        // TODO: get the medal count

        List<GoldMedal> summerWins = this.goldMedalRepository.findByCountryAndEventByOrderByYearAsc(countryName, "Summer Olympics");
        // TODO: get the collection of wins at the Summer Olympics, sorted by year in ascending order

        var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;
        int totalSummerEvents = this.goldMedalRepository.findByEvent("Summer Olympics").size();
        // TODO: get the total number of events at the Summer Olympics

        var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

        List<GoldMedal> winterWins = this.goldMedalRepository.findByCountryAndEventByOrderByYearAsc(countryName, "Winter Olympics");
        // TODO: get the collection of wins at the Winter Olympics

        var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
        var totalWinterEvents = this.goldMedalRepository.findByEvent("Winter Olympics").size();
        // TODO: get the total number of events at the Winter Olympics, sorted by year in ascending order

        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

        var numberEventsWonByFemaleAthletes = this.goldMedalRepository.findByGender("female").size();
        // TODO: get the number of wins by female athletes
        var numberEventsWonByMaleAthletes = this.goldMedalRepository.findByGender("male").size();
        // TODO: get the number of wins by male athletes

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        switch (sortBy) {
            case "name":
                if (ascendingOrder){
                    countries = this.countryRepository.findAllByOrderByNameAsc();
                } else {
                    countries = this.countryRepository.findAllByOrderByNameDesc();
                }
                //TODO: list of countries sorted by name in the given order
                break;
            case "gdp":
                if (ascendingOrder){
                    countries = this.countryRepository.findAllByOrderByGdpAsc();
                } else {
                    countries = this.countryRepository.findAllByOrderByGdpDesc();
                }
                // TODO: list of countries sorted by gdp in the given order
                break;
            case "population":
                if (ascendingOrder){
                    countries = this.countryRepository.findAllByOrderByPopulationAsc();
                } else {
                    countries = this.countryRepository.findAllByOrderByPopulationDesc();
                }
                // TODO: list of countries sorted by population in the given order
                break;
            case "medals":
            default:
                countries = this.countryRepository.findAllByOrderByCodeAsc();
                // TODO: list of countries in any order you choose; for sorting by medal count, additional logic below will handle that
                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            var goldMedalCount = this.goldMedalRepository.findByCountry(country.getName()).size();
            // TODO: get count of medals for the given country
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
