package com.barber.v1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barber.v1.Model.TipoCorte;

public interface TipoCorteRepository extends JpaRepository<TipoCorte,Long>{

        boolean existsByNombre(String nombre);

}
