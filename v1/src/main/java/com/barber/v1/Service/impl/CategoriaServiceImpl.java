package com.barber.v1.Service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.barber.v1.Model.CategoriaServicio;
import com.barber.v1.Repository.CategoriaServicioRepository;
import com.barber.v1.Service.CategoriaService;

@Service
public class CategoriaServiceImpl  implements CategoriaService{


    private final CategoriaServicioRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaServicioRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<CategoriaServicio> listarCategorias() {
        return categoriaRepository.findAll();
    }

     @Override
    public Optional<CategoriaServicio> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    @Override
    public CategoriaServicio crearCategoria(CategoriaServicio categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public CategoriaServicio actualizarCategoria(Long id, CategoriaServicio categoria) {
        CategoriaServicio existente = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        existente.setNombre(categoria.getNombre());

        return categoriaRepository.save(existente);
    }

    @Override
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }


    @Override 
    public boolean existePorNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }
}
