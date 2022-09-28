package com.project.geoapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.geoapp.model.*;
import com.project.geoapp.model.dto.EarthquakeDateAndTimeDto;
import com.project.geoapp.model.dto.HightestEarthquakeData;
import com.project.geoapp.repository.EarthquakeDataRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EarthquakeDataService {

    private final EarthquakeDataRepository earthquakeDataRepository;
    private final CountriesDataService countriesDataService;

    private final GeometryDataService geometryDataService;
    private final ObjectMapper objectMapper;

    public EarthquakeDataService(EarthquakeDataRepository earthquakeDataRepository, CountriesDataService countriesDataService, GeometryDataService geometryDataService, ObjectMapper objectMapper) {
        this.earthquakeDataRepository = earthquakeDataRepository;
        this.countriesDataService = countriesDataService;
        this.geometryDataService = geometryDataService;
        this.objectMapper = objectMapper;
    }

    public EarthquakeAggregate getAllEarthquakeData() {
        EarthquakeAggregate earthquakeAggregate = new EarthquakeAggregate();
        earthquakeAggregate.setEarthquakeDataList(earthquakeDataRepository.findAll());
        return earthquakeAggregate;
    }

    public EarthquakeDateAndTimeDto getEarthquakeDatesAndTimes() {
        EarthquakeDateAndTimeDto earthquakeDateAndTimeDto = new EarthquakeDateAndTimeDto();
        List<EarthquakeData> earthquakeDataList = earthquakeDataRepository.findAll();
        List<LocalDate> dates = earthquakeDataList.stream().map(EarthquakeData::getDate).collect(Collectors.toList());
        List<LocalTime> times = earthquakeDataList.stream().map(EarthquakeData::getTime).collect(Collectors.toList());
        earthquakeDateAndTimeDto.setDates(dates);
        earthquakeDateAndTimeDto.setTimes(times);
        return earthquakeDateAndTimeDto;
    }

    public EarthquakeAggregate getEarthquakePerDateAndTime(String date, String time) {
        EarthquakeAggregate earthquakeAggregate = new EarthquakeAggregate();
        List<EarthquakeData> earthquakeDataList = earthquakeDataRepository.findByDateOrTime(LocalDate.parse(date, DateTimeFormatter.ISO_DATE), LocalTime.parse(time));
        if (Objects.nonNull(earthquakeDataList))
            earthquakeAggregate.setEarthquakeDataList(earthquakeDataList);
        return earthquakeAggregate;
    }

    public EarthquakeData addEarthquakeData(EarthquakeData earthquakeData) {
        EarthquakeData newEarthquakeData = earthquakeDataRepository.save(earthquakeData);
        return newEarthquakeData;
    }

    public EarthquakeData updateEarthquakeData(JsonNode request, Long id) throws IOException {
        EarthquakeData oldEarthquakeData = earthquakeDataRepository.findById(Integer.parseInt(String.valueOf(id))).orElseThrow(ResourceNotFoundException::new);
        EarthquakeData entityToPatch = this.objectMapper.convertValue(request, EarthquakeData.class);
        request.fieldNames().forEachRemaining(field -> {
            Field f = ReflectionUtils.findField(EarthquakeData.class, field);
            String s = StringUtils.capitalize(field);
            Method[] methods = ReflectionUtils.getDeclaredMethods(EarthquakeData.class);
            Method method = Arrays.stream(methods).filter(m -> m.getName().equals("set" + s)).findAny().get();
            try {
                ReflectionUtils.makeAccessible(method);
                ReflectionUtils.makeAccessible(f);
                method.invoke(oldEarthquakeData, f.get(entityToPatch));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        });
        EarthquakeData newEarthquakeData = earthquakeDataRepository.save(oldEarthquakeData);
        return newEarthquakeData;
    }

    public void deleteEarthquakeData(Long id) {
        Optional<EarthquakeData> deleteData = earthquakeDataRepository.findById(id.intValue());
        earthquakeDataRepository.delete(deleteData.get());
    }

    public EarthquakeAggregate getEarthquakeDataPerYear(Integer year) {
        EarthquakeAggregate earthquakeAggregate = new EarthquakeAggregate();
        List<EarthquakeData> earthquakeDataList = earthquakeDataRepository.getEarthquakeDataPerYear(year);
        earthquakeAggregate.setEarthquakeDataList(earthquakeDataList);
        return earthquakeAggregate;
    }

    public EarthquakeAggregate getEarthquakeDataPerYearAndCountry(Integer year , String country) {
        EarthquakeAggregate earthquakeAggregate = new EarthquakeAggregate();
        List<EarthquakeData> earthquakeDataList = new ArrayList<>();
        CountriesAggregate countriesAggregate = countriesDataService.getCountryDataWithName(country);
        Geometry geometry = null;
        if(Objects.nonNull(countriesAggregate.getCountriesList())
                && Objects.nonNull(countriesAggregate.getCountriesList().get(0))) {
             geometry = geometryDataService.getGeometryDataPerCountry(countriesAggregate.getCountriesList().get(0).getName().toString());
        }
        List<Point> points = geometry.getPoints();
        EarthquakeAggregate earthquakeDataListPerYear = getEarthquakeDataPerYear(year);
            earthquakeDataListPerYear.getEarthquakeDataList().stream().forEach( e->{
                DecimalFormat decimalFormatter = new DecimalFormat("#.##");
                decimalFormatter.setRoundingMode(RoundingMode.UP);
                boolean locationMatchesWithCountry = points.stream().anyMatch(p->p.getLatitude().equals(Double.valueOf(decimalFormatter.format(e.getLatitude())))
                        && p.getLongitude().equals(Double.valueOf(decimalFormatter.format(e.getLongitude()))));
                if(locationMatchesWithCountry){
                    earthquakeDataList.add(e);
                }
            });
        earthquakeAggregate.setEarthquakeDataList(earthquakeDataList);
        return earthquakeAggregate;
    }

    public List<HightestEarthquakeData> getTopFiveMagnitudeEarthquakeData() {
       return earthquakeDataRepository.getTopFiveEarthquakeData();
    }

    public EarthquakeData getEarthquakeData(Integer id) {
        return earthquakeDataRepository.findById(id).orElseThrow(null);
    }
}
