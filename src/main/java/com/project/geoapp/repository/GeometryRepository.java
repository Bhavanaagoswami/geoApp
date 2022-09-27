package com.project.geoapp.repository;

import com.project.geoapp.model.Countries;
import com.project.geoapp.model.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GeometryRepository extends JpaRepository<Geometry,Long>, JpaSpecificationExecutor<Geometry> {

    public Geometry findByCountry(String name);

}
