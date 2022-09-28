package com.project.geoapp.controller;

import com.project.geoapp.model.CountriesAggregate;
import com.project.geoapp.service.CountriesDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User:bgoswami
 * Controller to render all countries API
 */
@RestController
public class CountriesDataController {
    @Autowired
    private CountriesDataService countriesDataService;

    @GetMapping(value = "/getCountries")
    public CountriesAggregate getCountriesList(@RequestParam (required = false) String name){
        return countriesDataService.getAllCountriesData(name);
    }

    @GetMapping(value = "/getCountryWithName")
    public CountriesAggregate getCountry(@RequestParam String name){
        return countriesDataService.getCountryDataWithName(name);
    }

    @GetMapping("/getCountries/names")
    public List<String> getCountriesNameList(){
        return countriesDataService.getListOfCountriesName();
    }

}
