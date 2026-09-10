package com.barber.v1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.barber.v1.Model.Barbero;
import org.springframework.stereotype.Repository;
@Repository
public interface BarberoRepository extends JpaRepository<Barbero,Long>{


    boolean existsByCorreo(String correo);
    Optional<Barbero> findByUsuarioId(Long usuarioId);

}
