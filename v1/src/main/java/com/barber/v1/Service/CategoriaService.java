package com.barber.v1.Service;

import com.barber.v1.Model.CategoriaServicio;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    
    List<CategoriaServicio> listarCategorias();
    
    Optional<CategoriaServicio> obtenerCategoriaPorId(Long id);
    
    CategoriaServicio crearCategoria(CategoriaServicio categoria);
    
    CategoriaServicio actualizarCategoria(Long id, CategoriaServicio categoria);
    
    void eliminarCategoria(Long id);

    boolean existePorNombre(String nombre);
}