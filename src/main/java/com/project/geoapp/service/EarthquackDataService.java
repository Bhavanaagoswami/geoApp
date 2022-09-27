package com.project.geoapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.geoapp.model.*;
import com.project.geoapp.model.dto.EarthquackDateAndTimeDto;
import com.project.geoapp.model.dto.HightestEarthquackData;
import com.project.geoapp.repository.EarthquackDataRepository;
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
public class EarthquackDataService {

    private final EarthquackDataRepository earthquackDataRepository;
    private final CountriesDataService countriesDataService;

    private final GeometryDataService geometryDataService;
    private final ObjectMapper objectMapper;

    public EarthquackDataService(EarthquackDataRepository earthquackDataRepository, CountriesDataService countriesDataService, GeometryDataService geometryDataService, ObjectMapper objectMapper) {
        this.earthquackDataRepository = earthquackDataRepository;
        this.countriesDataService = countriesDataService;
        this.geometryDataService = geometryDataService;
        this.objectMapper = objectMapper;
    }

    public EarthquackAggregate getAllEarthquackData() {
        EarthquackAggregate earthquackAggregate = new EarthquackAggregate();
        earthquackAggregate.setEarthquackDataList(earthquackDataRepository.findAll());
        return earthquackAggregate;
    }

    public EarthquackDateAndTimeDto getEarthquackDatesAndTimes() {
        EarthquackDateAndTimeDto earthquackDateAndTimeDto = new EarthquackDateAndTimeDto();
        List<EarthquackData> earthquackDataList = earthquackDataRepository.findAll();
        List<LocalDate> dates = earthquackDataList.stream().map(EarthquackData::getDate).collect(Collectors.toList());
        List<LocalTime> times = earthquackDataList.stream().map(EarthquackData::getTime).collect(Collectors.toList());
        earthquackDateAndTimeDto.setDates(dates);
        earthquackDateAndTimeDto.setTimes(times);
        return earthquackDateAndTimeDto;
    }

    public EarthquackAggregate getEarthquackPerDateAndTime(String date, String time) {
        EarthquackAggregate earthquackAggregate = new EarthquackAggregate();
        List<EarthquackData> earthquackDataList = earthquackDataRepository.findByDateOrTime(LocalDate.parse(date, DateTimeFormatter.ISO_DATE), LocalTime.parse(time));
        if (Objects.nonNull(earthquackDataList))
            earthquackAggregate.setEarthquackDataList(earthquackDataList);
        return earthquackAggregate;
    }

    public EarthquackData addEarthquackData(EarthquackData earthquackData) {
        EarthquackData newEarthquackData = earthquackDataRepository.save(earthquackData);
        return newEarthquackData;
    }

    public EarthquackData updateEarthquackData(JsonNode request, Long id) throws IOException {
        EarthquackData oldEarthquackData = earthquackDataRepository.findById(Integer.parseInt(String.valueOf(id))).orElseThrow(ResourceNotFoundException::new);
        EarthquackData entityToPatch = this.objectMapper.convertValue(request, EarthquackData.class);
        request.fieldNames().forEachRemaining(field -> {
            Field f = ReflectionUtils.findField(EarthquackData.class, field);
            String s = StringUtils.capitalize(field);
            Method[] methods = ReflectionUtils.getDeclaredMethods(EarthquackData.class);
            Method method = Arrays.stream(methods).filter(m -> m.getName().equals("set" + s)).findAny().get();
            try {
                ReflectionUtils.makeAccessible(method);
                ReflectionUtils.makeAccessible(f);
                method.invoke(oldEarthquackData, f.get(entityToPatch));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        });
        EarthquackData newEarthquackData = earthquackDataRepository.save(oldEarthquackData);
        return newEarthquackData;
    }

    public void deleteEarthquackData(Long id) {
        Optional<EarthquackData> deleteData = earthquackDataRepository.findById(id.intValue());
        earthquackDataRepository.delete(deleteData.get());
    }

    public EarthquackAggregate getEarthquackDataPerYear(Integer year) {
        EarthquackAggregate earthquackAggregate = new EarthquackAggregate();
        List<EarthquackData> earthquackDataList = earthquackDataRepository.getEarthquackDataPerYear(year);
        earthquackAggregate.setEarthquackDataList(earthquackDataList);
        return earthquackAggregate;
    }

    public EarthquackAggregate getEarthquackDataPerYearAndCountry(Integer year , String country) {
        EarthquackAggregate earthquackAggregate = new EarthquackAggregate();
        List<EarthquackData> earthquackDataList = new ArrayList<>();
        CountriesAggregate countriesAggregate = countriesDataService.getCountryDataWithName(country);
        Geometry geometry = null;
        if(Objects.nonNull(countriesAggregate.getCountriesList())
                && Objects.nonNull(countriesAggregate.getCountriesList().get(0))) {
             geometry = geometryDataService.getGeometryDataPerCountry(countriesAggregate.getCountriesList().get(0).getName().toString());
        }
        List<Point> points = geometry.getPoints();
        EarthquackAggregate earthquackDataListPerYear = getEarthquackDataPerYear(year);
        earthquackDataListPerYear.getEarthquackDataList().stream().forEach( e->{
            DecimalFormat decimalFormatter = new DecimalFormat("#.##");
            decimalFormatter.setRoundingMode(RoundingMode.UP);
            boolean locationMatchesWithCountry = points.stream().anyMatch(p->p.getLatitude().equals(Double.valueOf(decimalFormatter.format(e.getLatitude())))
                    && p.getLongitude().equals(Double.valueOf(decimalFormatter.format(e.getLongitude()))));
            if(locationMatchesWithCountry){
                earthquackDataList.add(e);
            }
        });
        earthquackAggregate.setEarthquackDataList(earthquackDataList);
        return earthquackAggregate;
    }

    public List<HightestEarthquackData> getTopFiveMagnitudeEarthquackData() {
       return earthquackDataRepository.getTopFiveEarthquackData();
    }
}
