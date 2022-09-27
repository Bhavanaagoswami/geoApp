package com.project.geoapp.repository;

import com.project.geoapp.model.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CountriesRepository extends JpaRepository<Countries,Long>, JpaSpecificationExecutor<Countries> {

    public Countries findByName(String name);
}
