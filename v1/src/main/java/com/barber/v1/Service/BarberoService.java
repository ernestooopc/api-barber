package com.barber.v1.Service;

import java.util.List;
import java.util.Optional;

import com.barber.v1.Model.Barbero;

public interface BarberoService {

    Barbero createBarbero(Barbero barbero);
    Barbero updateBarbero(Long id, Barbero barbero);
    void deleteBarbero(Long id);
    List<Barbero> listBarberos();
    Optional<Barbero> findById(Long id);

    boolean existsDni(String dni);


}
