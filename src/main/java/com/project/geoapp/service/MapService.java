package com.project.geoapp.service;

import net.minidev.json.writer.JsonReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class MapService {


    /**
     * Read JSON with earthquack data and populate MVC model
     *
     * Use:for JSON browsing
     *
     */
    private void createModel()
    {
        /*try
        {
            URL vvv = new URL("http://vvv.chmi.cz/uoco/aqindex_eng.json");
            URLConnection urlConnection = vvv.openConnection();

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8.displayName())))
            {
                JsonReader jsonReader = Json.createReader(reader);
                JsonObject root = (JsonObject) jsonReader.read();

                mapModel.setActualized(root.getString("Actualized"));

                final JsonArray jsonLegend = root.getJsonArray("Legend");

                for (int i = 0; i < jsonLegend.size(); i++)
                {
                    JsonObject jsonAqIndex = jsonLegend.getJsonObject(i);

                    mapModel.putAirQualiryLegendItem(jsonAqIndex.getInt("Ix"), new AirQualiryLegendItem(jsonAqIndex.getInt("Ix"),
                            jsonAqIndex.getString("Color"),
                            jsonAqIndex.getString("ColorText"),
                            jsonAqIndex.getString("Description")));
                }

                final JsonArray jsonStates = root.getJsonArray("States");

                for (int s = 0; s < jsonStates.size(); s++)
                {
                    final JsonObject jsonState = jsonStates.getJsonObject(s);
                    final JsonArray jsonRegions = jsonState.getJsonArray("Regions");

                    for (int i = 0; i < jsonRegions.size(); i++)
                    {
                        final JsonObject jsonRegion = jsonRegions.getJsonObject(i);

                        final JsonArray jsonStations = jsonRegion.getJsonArray("Stations");
                        for (int j = 0; j < jsonStations.size(); j++)
                        {
                            final JsonObject jsonStation = jsonStations.getJsonObject(j);

                            if ((jsonStation.containsKey("Lat")) && (jsonStation.containsKey("Lon"))) // 'Prague center' and 'Prague periphery' not have position and components list
                            {
                                String wgs84Latitude = jsonStation.getString("Lat");
                                String wgs84Longitude = jsonStation.getString("Lon");

                                mapModel.addAirQualityMeasuringStation(new AirQualityMeasuringStation(jsonStation.getString("Code"),
                                        jsonStation.getString("Name"),
                                        jsonStation.getString("Owner"),
                                        jsonStation.getString("Classif"),
                                        Float.valueOf(wgs84Latitude),
                                        Float.valueOf(wgs84Longitude),
                                        jsonStation.getInt("Ix")));
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
    }

    /**
     * Substitute an empty String, when a null value is encountered.
     *
     * @param source String
     * @return Original string, or empty string, if parameter is null
     */
    static String nvl(String source)
    {
        return (source == null) ? "" : source;
    }
}
