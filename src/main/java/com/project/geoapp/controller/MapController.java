package com.project.geoapp.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class MapController {


    /**
     * Method responds to HTTP GET requests
     *
     * @return View path
     */
    @GetMapping
    public String map()
    {
       // createModel();

        return "/WEB-INF/jsp/mapView.jsp";
    }
}
