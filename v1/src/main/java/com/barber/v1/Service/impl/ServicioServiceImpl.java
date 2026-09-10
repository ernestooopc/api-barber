package com.barber.v1.Service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barber.v1.Model.CategoriaServicio;
import com.barber.v1.Model.Servicio;
import com.barber.v1.Repository.CategoriaServicioRepository;
import com.barber.v1.Repository.ServicioRepository;
import com.barber.v1.Service.ServicioService;


@Service
public class ServicioServiceImpl implements ServicioService{

    private final ServicioRepository servicioRepository;
    private final CategoriaServicioRepository categoriaRepository;


    public ServicioServiceImpl(ServicioRepository servicioRepository, CategoriaServicioRepository categoriaRepository) {
        this.servicioRepository = servicioRepository;
        this.categoriaRepository = categoriaRepository;
    }


    @Override
    public List<Servicio> listarServicios(){
        return servicioRepository.findAll();
    }

    @Override 
    public List<Servicio> listarPorCategoria(Long categoriaId) {
        return servicioRepository.findByCategoriaId(categoriaId);
    }
     @Override
    public Optional<Servicio> findById(Long id) {
        return servicioRepository.findById(id);
    }

     @Override
    public Servicio crearServicio(Servicio servicio) {
    if (servicio.getCategoria() == null || servicio.getCategoria().getId() == null) {
        throw new RuntimeException("Debe enviarse un objeto 'categoria' con un 'id'");
    }

    Long categoriaId = servicio.getCategoria().getId();
    CategoriaServicio categoria = categoriaRepository.findById(categoriaId)
        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    
    servicio.setCategoria(categoria);

    return servicioRepository.save(servicio);
}


    @Override
    @Transactional
    public Servicio actualizarServicio(Long id, Servicio servicioActualizado) {
        return servicioRepository.findById(id)
                .map(servicioExistente -> {
                    servicioExistente.setNombre(servicioActualizado.getNombre());
                    servicioExistente.setDescripcion(servicioActualizado.getDescripcion());
                    servicioExistente.setPrecio(servicioActualizado.getPrecio());
                    servicioExistente.setDuracionMinutos(servicioActualizado.getDuracionMinutos());
                    
                    // Validamos que se envíe la categoría en la actualización
                    if (servicioActualizado.getCategoria() == null || servicioActualizado.getCategoria().getId() == null) {
                        throw new RuntimeException("Debe enviarse una categoría válida para la actualización");
                    }

                    // Obtenemos el ID de la nueva categoría enviada
                    Long categoriaId = servicioActualizado.getCategoria().getId();
                    CategoriaServicio categoria = categoriaRepository.findById(categoriaId)
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + categoriaId));
                    
                    servicioExistente.setCategoria(categoria);
                    return servicioRepository.save(servicioExistente);
                })
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
    }



    @Override
    @Transactional
    public void eliminarServicio(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Servicio no encontrado con ID: " + id);
        }
        servicioRepository.deleteById(id);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return servicioRepository.existsByNombre(nombre);
    }


}
