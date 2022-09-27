package com.project.geoapp.service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.geoapp.model.Geometry;
import com.project.geoapp.model.Point;
import com.project.geoapp.repository.GeometryRepository;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.JSONParser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;


@Slf4j
@Service
public class GeometryDataService {
    private final GeometryRepository geometryRepository;

    public GeometryDataService(GeometryRepository geometryRepository) {
        this.geometryRepository = geometryRepository;
    }
    public void createGrographyPointsData() throws FileNotFoundException {
        JSONParser parser = new JSONParser();
        ObjectMapper mapper = new ObjectMapper();
        File myObj = new File(this.getClass().getResource("/static/Countries.geojson").getFile());
        try {
            BufferedReader br = new BufferedReader(new FileReader(myObj));
            // convert the json string back to object
            Map countriesMap = mapper.readValue(br, Map.class);
            if (Objects.nonNull(countriesMap.containsKey("features"))) {
                List list = (List) countriesMap.get("features");
                list.forEach(s -> {
                    LinkedHashMap lm = (LinkedHashMap) s;
                    Geometry g = new Geometry();
                    if (!Objects.isNull((lm.containsKey("properties")))) {

                        LinkedHashMap o = (LinkedHashMap) lm.get("properties");
                        String name = (String) o.get("NAME");
                        g.setCountry(name);
                    }
                    if (Objects.nonNull((lm.containsKey("geometry")))) {
                        LinkedHashMap o = (LinkedHashMap) lm.get("geometry");
                        String type = (String) o.get("type");
                        switch (type) {
                            case "MultiPolygon":
                                List<Point> pointList = getPointsOfMultiPolygon(o);
                                g.setPoints(pointList);
                                break;
                            case "Polygon":
                                List<Point> pointList2 = getPointsOfPolygon(o);
                                g.setPoints(pointList2);
                                break;
                            case "default":
                                g.setPoints(null);
                        }
                    }
                    geometryRepository.save(g);
                });

            }
        } catch (StreamReadException ex) {
            throw new RuntimeException(ex);
        } catch (DatabindException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static List<Point> getPointsOfPolygon(LinkedHashMap o) {
        List coList = (List) o.get("coordinates");
        List<Point> pointList = new ArrayList<>();
        for (Object c : coList) {
            if (c instanceof ArrayList) {
                ((ArrayList<?>) c).forEach(c1 -> {
                    if (c1 instanceof ArrayList) {
                        ArrayList a = (ArrayList) c1;
                        if(a.get(0) instanceof Double) {
                            setPoint(a, "Polygon", pointList);
                        }else{
                            ((ArrayList<?>) c1).forEach(c2 -> {
                                ArrayList point = (ArrayList) c2;
                                if (point instanceof ArrayList) {
                                    if (point.get(0) instanceof Double) {
                                        setPoint(point, "Polygon", pointList);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
        return pointList;
    }

    private static void setPoint(ArrayList a, String Polygon, List<Point> pointList) {
        Point p = new Point();
        DecimalFormat decimalFormatter = new DecimalFormat("#.##");
        decimalFormatter.setRoundingMode(RoundingMode.UP);
        p.setLatitude(Double.valueOf(decimalFormatter.format((double) a.get(1))));
        p.setLongitude(Double.valueOf(decimalFormatter.format((double) a.get(0))));
        System.out.println("Polygon" + p);
        pointList.add(p);
    }

    private static List<Point> getPointsOfMultiPolygon(LinkedHashMap o) {
        List coList = (List) o.get("coordinates");
        List<Point> pointList = new ArrayList<>();
        for (Object c : coList) {
            if (c instanceof ArrayList) {
                ((ArrayList<?>) c).forEach(c1 -> {
                    if (c1 instanceof ArrayList) {
                        ((ArrayList<?>) c1).forEach(c2 -> {
                            if (c2 instanceof ArrayList) {
                                ArrayList a = (ArrayList) c2;
                                setPoint(a, "Multipolygon", pointList);
                            }
                        });
                    }
                });
            }

        }
        return pointList;
    }

    public Geometry getGeometryDataPerCountry( String country){
        Geometry geometry = geometryRepository.findByCountry(country);
        return geometry;
    }

}
