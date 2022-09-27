package com.project.geoapp.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Countries {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ogc_fid", nullable = false)
    private Integer ogc_fid;
    private byte[] wkb_geometry;
    private String name;
    private String name_long;
    private String formal_en;
    private String sov_a3;
    private String postal;
    private String abbrev;
    private String continent;
    private String region_un;
    private String subregion;
    private String region_wb;
    private Integer pop_est;
    private Integer pop_year;
    private Integer pop_rank;
    private double gdp_md_est;
    private Integer lastcensus;
    private Integer gdp_year;
    private String economy;
    private String income_grp;

}
