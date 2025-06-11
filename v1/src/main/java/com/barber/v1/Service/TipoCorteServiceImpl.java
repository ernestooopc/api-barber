package com.barber.v1.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barber.v1.Model.TipoCorte;
import com.barber.v1.Repository.TipoCorteRepository;


@Service
public class TipoCorteServiceImpl implements TipoCorteService{

    private final TipoCorteRepository tipoCorteRepository;

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


    @Override
    public TipoCorte createTipoCorte(TipoCorte tipoCorte) {
        return tipoCorteRepository.save(tipoCorte);
    }

    @Override
    public TipoCorte updateTipoCorteo(Long id, TipoCorte tipoCorteActualizado) {
        return tipoCorteRepository.findById(id)
                .map(tcExistente -> {
                    tcExistente.setNombre(tipoCorteActualizado.getNombre());
                    tcExistente.setDescripcion(tipoCorteActualizado.getDescripcion());
                    tcExistente.setPrecio(tipoCorteActualizado.getPrecio());
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
