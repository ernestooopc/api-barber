package com.barber.v1.Service;

import java.util.List;
import java.util.Optional;
import com.barber.v1.Model.Servicio;

public interface ServicioService {

    Servicio crearServicio(Servicio servicio);
    
    Servicio actualizarServicio(Long id, Servicio servicioActualizado);
    
    void eliminarServicio(Long id);
    
    List<Servicio> listarServicios();
    
    List<Servicio> listarPorCategoria(Long categoriaId);
    
    Optional<Servicio> findById(Long id);
    
    boolean existsByNombre(String nombre);
}