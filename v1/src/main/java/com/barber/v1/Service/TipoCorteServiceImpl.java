package com.barber.v1.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barber.v1.Model.Categoria;
import com.barber.v1.Model.TipoCorte;
import com.barber.v1.Repository.CategoriaRepository;
import com.barber.v1.Repository.TipoCorteRepository;


@Service
public class TipoCorteServiceImpl implements TipoCorteService{

    private final TipoCorteRepository tipoCorteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;


    @Autowired
    public TipoCorteServiceImpl(TipoCorteRepository tipoCorteRepository){
        this.tipoCorteRepository = tipoCorteRepository;
    }


    @Override
    public List<TipoCorte> listTipoCortes(){
        return tipoCorteRepository.findAll();
    }

     @Override
    public Optional<TipoCorte> findById(Long id) {
        return tipoCorteRepository.findById(id);
    }


    public TipoCorte createTipoCorte(TipoCorte tipoCorte) {
    if (tipoCorte.getCategoria() == null || tipoCorte.getCategoria().getId() == null) {
        throw new RuntimeException("Debe enviarse un objeto 'categoria' con un 'id'");
    }

    Long categoriaId = tipoCorte.getCategoria().getId();
    Categoria categoria = categoriaRepository.findById(categoriaId)
        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    
    tipoCorte.setCategoria(categoria);

    return tipoCorteRepository.save(tipoCorte);
}


    @Override
    public TipoCorte updateTipoCorteo(Long id, TipoCorte tipoCorteActualizado) {
        return tipoCorteRepository.findById(id)
                .map(tcExistente -> {
                    tcExistente.setNombre(tipoCorteActualizado.getNombre());
                    tcExistente.setDescripcion(tipoCorteActualizado.getDescripcion());
                    tcExistente.setPrecio(tipoCorteActualizado.getPrecio());
                    tcExistente.setDuracion(tipoCorteActualizado.getDuracion());
                    Long categoriaId = tcExistente.getCategoria().getId();
                    Categoria categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
                    tcExistente.setCategoria(categoria);
                    return tipoCorteRepository.save(tcExistente);
                })
                .orElseThrow(() -> new RuntimeException("Tipo de corte no encontrado con id: " + id));
    }



    @Override
    public void deleteTipoCorte(Long id) {
        tipoCorteRepository.deleteById(id);
    }


    @Override
    public boolean existsByNombre(String nombre) {
        return tipoCorteRepository.existsByNombre(nombre);
    }


}
