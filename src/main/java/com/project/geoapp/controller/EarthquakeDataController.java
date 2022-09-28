package com.project.geoapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.geoapp.model.EarthquakeAggregate;
import com.project.geoapp.model.EarthquakeData;
import com.project.geoapp.model.dto.EarthquakeDateAndTimeDto;
import com.project.geoapp.model.dto.HightestEarthquakeData;
import com.project.geoapp.service.EarthquakeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * User:bgoswami
 * Controller to render all Earthquack DATA API
 */
@RestController
public class EarthquakeDataController {
    @Autowired
    private EarthquakeDataService earthquakeDataService;

    @GetMapping("/getEarthquakes/alldates")
    public EarthquakeDateAndTimeDto getEarthquakesDateAndTime(){
        return earthquakeDataService.getEarthquakeDatesAndTimes();
    }

    @GetMapping(value = "/getEarthquake/datetime")
    public EarthquakeAggregate getEarthquakesDateAndTime(@RequestParam String date, @RequestParam String time){
        return earthquakeDataService.getEarthquakePerDateAndTime(date,time);
    }

    @PatchMapping(value = "/updateEarthquake/{id}", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public EarthquakeData updateEarthquakesData(@PathVariable Long id, @RequestBody JsonNode request){
        try {
            return earthquakeDataService.updateEarthquakeData(request,id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value="/addEarthquake",consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public EarthquakeData addearthquakesData(@RequestBody EarthquakeData earthquakeData){
        return earthquakeDataService.addEarthquakeData(earthquakeData);
    }

    @DeleteMapping("/deleteEarthquake/{id}")
    public void deleteEarthquakesData(@PathVariable Long id){
        earthquakeDataService.deleteEarthquakeData(id);
    }

    @GetMapping(value = "/getAllEarthquakeData")
    public EarthquakeAggregate getAllEarthquakData(){
        return earthquakeDataService.getAllEarthquakeData();
    }


    @GetMapping(value = "/getEarthquakeDataPerYear/{year}")
    public EarthquakeAggregate getEarthquakeDataPerYear(@PathVariable Integer year){
        return earthquakeDataService.getEarthquakeDataPerYear(year);
    }

    @GetMapping(value = "/getEarthquakeDataPerYearAndCountry")
    public EarthquakeAggregate getEarthquakeDataPerYearAndCountry(@RequestParam Integer year, @RequestParam String country){
        return earthquakeDataService.getEarthquakeDataPerYearAndCountry(year, country);
    }

    @GetMapping(value = "/getTopFiveMagnitudeEarthquakeData")
    public List<HightestEarthquakeData> getTopFiveMagnitudeEarthquakeData(){
        return earthquakeDataService.getTopFiveMagnitudeEarthquakeData();
    }

        @GetMapping(value = "/getEarthquakeData/{id}")
    public EarthquakeData getEarthquakeData(@PathVariable Integer id){
        return earthquakeDataService.getEarthquakeData(id);
    }


}
