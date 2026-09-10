package com.barber.v1.Service;

import java.util.List;
import java.util.Optional;
import com.barber.v1.Model.Barbero;

public interface BarberoService {

    Barbero registrarBarbero(Barbero barbero);
    
    Barbero actualizarBarbero(Long id, Barbero barbero);
    
    // Cambiamos el "delete" físico por una desactivación
    void desactivarBarbero(Long id);
    
    List<Barbero> listBarberos();
    
    Optional<Barbero> findById(Long id);
    
    boolean existsByCorreo(String correo);

    Optional<Barbero> findByUsuarioId(Long usuarioId);

}