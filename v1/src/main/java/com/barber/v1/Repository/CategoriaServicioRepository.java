package com.barber.v1.Repository;
import com.barber.v1.Model.CategoriaServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaServicioRepository extends JpaRepository<CategoriaServicio, Long>{


    boolean existsByNombre(String nombre);

}
