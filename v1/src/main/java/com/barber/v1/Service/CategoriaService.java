package com.barber.v1.Service;
import com.barber.v1.Model.Categoria;
import java.util.List;
import java.util.Optional;
public interface CategoriaService {
    List<Categoria> listarCategorias();
    Optional<Categoria> obtenerCategoriaPorId(Long id);
    Categoria crearCategoria(Categoria categoria);
    Categoria actualizarCategoria(Long id, Categoria categoria);
    void eliminarCategoria(Long id);
}
