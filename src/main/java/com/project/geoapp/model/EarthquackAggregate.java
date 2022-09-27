package com.project.geoapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class EarthquackAggregate {

    private List<EarthquackData> earthquackDataList;
}
