package com.project.geoapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.geoapp.model.EarthquackAggregate;
import com.project.geoapp.model.EarthquackData;
import com.project.geoapp.model.dto.EarthquackDateAndTimeDto;
import com.project.geoapp.model.dto.HightestEarthquackData;
import com.project.geoapp.service.EarthquackDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class EarthquackDataController {
    @Autowired
    private EarthquackDataService earthquackDataService;

    @GetMapping("/getEarthquakes/alldates")
    public EarthquackDateAndTimeDto getEarthquacksDateAndTime(){
        return earthquackDataService.getEarthquackDatesAndTimes();
    }

    @GetMapping(value = "/getEarthquake/datetime")
    public EarthquackAggregate getEarthquacksDateAndTime(@RequestParam String date, @RequestParam String time){
        return earthquackDataService.getEarthquackPerDateAndTime(date,time);
    }

    @PatchMapping(value = "/updateEarthquake/{id}", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public EarthquackData updateEarthquacksData(@PathVariable Long id, @RequestBody JsonNode request){
        try {
            return earthquackDataService.updateEarthquackData(request,id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value="/addEarthquake",consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public EarthquackData addEarthquacksData(@RequestBody EarthquackData earthquackData){
        return earthquackDataService.addEarthquackData(earthquackData);
    }

    @DeleteMapping("/deleteEarthquake/{id}")
    public void deleteEarthquacksData(@PathVariable Long id){
        earthquackDataService.deleteEarthquackData(id);
    }

    @GetMapping(value = "/getAllEarthquackData")
    public EarthquackAggregate getAllEarthquakData(){
        return earthquackDataService.getAllEarthquackData();
    }


    @GetMapping(value = "/getEarthquackDataPerYear/{year}")
    public EarthquackAggregate getEarthquackDataPerYear(@PathVariable Integer year){
        return earthquackDataService.getEarthquackDataPerYear(year);
    }

    @GetMapping(value = "/getEarthquackDataPerYearAndCountry")
    public EarthquackAggregate getEarthquackDataPerYearAndCountry(@RequestParam Integer year,@RequestParam String country){
        return earthquackDataService.getEarthquackDataPerYearAndCountry(year, country);
    }

    @GetMapping(value = "/getTopFiveMagnitudeEarthquackData")
    public List<HightestEarthquackData> getTopFiveMagnitudeEarthquackData(){
        return earthquackDataService.getTopFiveMagnitudeEarthquackData();
    }


}
