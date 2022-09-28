package com.project.geoapp.repository;

import com.project.geoapp.model.EarthquakeData;
import com.project.geoapp.model.dto.HightestEarthquakeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface EarthquakeDataRepository extends JpaRepository<EarthquakeData,Integer>, JpaSpecificationExecutor<EarthquakeData> {
     //("select e from EarthquakeData e where e.date = to_date('?1', 'DD-mm-YYYY') and e.time =to_time('?2' ,'hh:mm:ss')");
    List<EarthquakeData> findByDateOrTime(LocalDate date, LocalTime time);

    @Query("select e from EarthquakeData e where \n" +
            "EXTRACT(YEAR FROM date) = ?1 ")
    List<EarthquakeData> getEarthquakeDataPerYear(Integer year);

    @Query(value = "\tSELECT magnitude, earthquakeCount\n" +
            "FROM (\n" +
            "    SELECT DISTINCT magnitude AS magnitude,\n" +
            "    COUNT(ogc_fid) AS earthquakeCount,\n" +
            "    RANK() OVER (ORDER BY magnitude DESC) AS magnitude_rank\n" +
            "    FROM EarthquakeData\n" +
            "    GROUP BY magnitude\n" +
            ") \n" +
            "WHERE magnitude_rank <= 5\n" +
            "ORDER BY magnitude_rank", nativeQuery = true)
    List<HightestEarthquakeData> getTopFiveEarthquakeData();
}
