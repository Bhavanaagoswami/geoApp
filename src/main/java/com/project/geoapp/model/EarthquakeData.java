package com.project.geoapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="earthquakedata")
public class EarthquakeData implements Serializable {
    @Id
    @GeneratedValue(generator ="sequence-generator")
    @GenericGenerator(name="sequence-generator",strategy ="org.hibernate.id.enhanced.SequenceStyleGenerator",
                     parameters = { @org.hibernate.annotations.Parameter(name="sequence_name",value="ogc_fid"),
                                    @org.hibernate.annotations.Parameter(name="initial_value",value="1001"),
                                    @org.hibernate.annotations.Parameter(name="increment_size",value="1")})
    @Column(name="ogc_fid" , unique = true)
    private Integer ogc_fid;
    private String wkb_geometry;
    private LocalDate date;
    private LocalTime time;
    private double latitude;
    private double longitude;
    private String type;
    private double depth;
    private String depth_error;

    @Column(name = "depth_seismic_stations")
    private String depth_seismic_stations;
    private double magnitude;

    @Column(name = "magnitude_type")
    private String magnitude_type;

    @Column(name = "magnitude_error")
    private String magnitude_error;
    @Column(name = "magnitude_seismic_stations")
    private String magnitude_seismic_stations;
    @Column(name = "azimuthal_gap")
    private String azimuthal_gap;

    private String horizontal_distance;
    @Column(name = "horizontal_error")
    private String horizontal_error;

    @Column(name = "root_mean_square")
    private String root_mean_square;
    private String id;
    private String source;
    @Column(name = "location_source")
    private String location_source;

    @Column(name = "magnitude_source")
    private String magnitude_source;
    private String status;
}
