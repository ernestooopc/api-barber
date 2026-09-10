package com.barber.v1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barber.v1.Model.Servicio;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ServicioRepository extends JpaRepository<Servicio,Long>{

        boolean existsByNombre(String nombre);
        List<Servicio> findByCategoriaId(Long categoriaId);

}
