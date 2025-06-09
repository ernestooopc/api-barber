package com.barber.v1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barber.v1.Model.Barbero;

public interface BarberoRepository extends JpaRepository<Barbero,Long>{


    boolean existsByDni(String dni);


}
