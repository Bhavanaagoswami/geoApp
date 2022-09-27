package com.project.geoapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.geoapp.model.Countries;
import com.project.geoapp.model.CountriesAggregate;
import com.project.geoapp.repository.CountriesRepository;
import com.project.geoapp.repository.EarthquackDataRepository;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.webmvc.json.DomainObjectReader;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CountriesDataService {
    private final CountriesRepository countriesRepository;

    private final EarthquackDataRepository earthquackDataRepository;

    private final DomainObjectReader domainObjectReader;
    private final ObjectMapper objectMapper;

    public CountriesDataService(CountriesRepository countriesRepository,
                                EarthquackDataRepository earthquackDataRepository,
                                ObjectMapper objectMapper,
                                PersistentEntities persistentEntities,
                                Associations associationLinks) {
        this.countriesRepository = countriesRepository;
        this.earthquackDataRepository = earthquackDataRepository;
        this.domainObjectReader = new DomainObjectReader(persistentEntities, associationLinks);
        this.objectMapper = objectMapper;
    }

    public CountriesAggregate getAllCountriesData(String name) {
        CountriesAggregate countriesAggregate = new CountriesAggregate();
        if (Objects.nonNull(name)) {
            countriesAggregate = getCountryDataWithName(name.trim());
        } else {
            countriesAggregate.setCountriesList(countriesRepository.findAll());
        }
        return countriesAggregate;
    }

    public CountriesAggregate getCountryDataWithName(String name) {
        CountriesAggregate countriesAggregate = new CountriesAggregate();
        Countries countries = countriesRepository.findByName(name);
        byte[] buf = {71, 69, 69, 75, 83};

        // Create byteArrayInputStream
        ByteArrayInputStream byteArrayInputStr
                = new ByteArrayInputStream(countries.getWkb_geometry());

        int b = 0;
        while ((b = byteArrayInputStr.read()) != -1) {
            // Convert byte to character
            char ch = (char) b;

            // Print the character
            System.out.println("Char : " + ch);
        }
        countriesAggregate.setCountriesList(List.of(countries));
        return countriesAggregate;
    }

    public List<String> getListOfCountriesName() {
        CountriesAggregate countriesAggregate = new CountriesAggregate();
        countriesAggregate.setCountriesList((List<Countries>) countriesRepository.findAll());
        List<String> names = countriesAggregate.getCountriesList().stream().map(Countries::getName).collect(Collectors.toList());
        return names;
    }


}
