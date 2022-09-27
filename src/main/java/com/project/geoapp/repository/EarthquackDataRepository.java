package com.project.geoapp.repository;

import com.project.geoapp.model.EarthquackData;
import com.project.geoapp.model.dto.HightestEarthquackData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Native;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface EarthquackDataRepository extends JpaRepository<EarthquackData,Integer>, JpaSpecificationExecutor<EarthquackData> {
     //("select e from EarthquackData e where e.date = to_date('?1', 'DD-mm-YYYY') and e.time =to_time('?2' ,'hh:mm:ss')");
    List<EarthquackData> findByDateOrTime(LocalDate date, LocalTime time);

    @Query("select e from EarthquackData e where \n" +
            "EXTRACT(YEAR FROM date) = ?1 ")
    List<EarthquackData> getEarthquackDataPerYear(Integer year);

    @Query(value = "\tSELECT magnitude, earthquackCount\n" +
            "FROM (\n" +
            "    SELECT DISTINCT magnitude AS magnitude,\n" +
            "    COUNT(ogc_fid) AS earthquackCount,\n" +
            "    RANK() OVER (ORDER BY magnitude DESC) AS magnitude_rank\n" +
            "    FROM EarthquackData\n" +
            "    GROUP BY magnitude\n" +
            ") \n" +
            "WHERE magnitude_rank <= 5\n" +
            "ORDER BY magnitude_rank", nativeQuery = true)
    List<HightestEarthquackData> getTopFiveEarthquackData();
}
