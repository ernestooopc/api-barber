package com.barber.v1.Service;

import java.util.List;
import java.util.Optional;

import com.barber.v1.Model.TipoCorte;

public interface TipoCorteService {

    TipoCorte createTipoCorte(TipoCorte tipoCorte);
    TipoCorte updateTipoCorteo(Long id, TipoCorte tipoCorteActualizado);
    void deleteTipoCorte(Long id);
    List<TipoCorte> listTipoCortes();
    Optional<TipoCorte> findById(Long id);
    boolean existsByNombre(String nombre);
}
