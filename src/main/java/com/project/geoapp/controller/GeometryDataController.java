package com.project.geoapp.controller;


import com.project.geoapp.service.GeometryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
public class GeometryDataController {

    @Autowired
    private GeometryDataService geometryDataService;
    @GetMapping(value = "/createPointsData")
    public void setPointsData() throws FileNotFoundException {
         geometryDataService.createGrographyPointsData();
    }
}
