package com.example.IntegrationAPI.Base3.Repository;

import com.example.IntegrationAPI.Base3.model.Conge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CongeRepository extends JpaRepository<Conge, Long> {


    List<Conge> findByEmpCode(String empCode);

    Optional<Conge> findFirstByEmpCode(String empCode);


}