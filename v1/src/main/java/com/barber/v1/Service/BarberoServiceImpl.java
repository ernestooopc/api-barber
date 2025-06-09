package com.barber.v1.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barber.v1.Model.Barbero;
import com.barber.v1.Repository.BarberoRepository;

@Service
public class BarberoServiceImpl implements BarberoService{

    private final BarberoRepository barberoRepository;


    @Autowired
    public BarberoServiceImpl(BarberoRepository barberoRepository){
        this.barberoRepository = barberoRepository;
    }

    @Override
    public List<Barbero> listBarberos(){
        return barberoRepository.findAll();
    }

    @Override
    public Optional<Barbero>findById(Long id){
        return barberoRepository.findById(id);
    }

    @Override
    public Barbero createBarbero(Barbero barbero) {
        return barberoRepository.save(barbero);
    }

    @Override
    public Barbero updateBarbero(Long id, Barbero barberoUpdate) {
        return barberoRepository.findById(id)
            .map(barberoExistente -> {
                barberoExistente.setNombre(barberoUpdate.getNombre());
                barberoExistente.setDni(barberoUpdate.getDni());
                barberoExistente.setExperienciaLaboral(barberoUpdate.getExperienciaLaboral());
                return barberoRepository.save(barberoExistente);
            })
            .orElseThrow(() -> new RuntimeException("Barbero no encontrado con ID: " + id));
    }



    @Override
    public void deleteBarbero(Long id) {
        barberoRepository.deleteById(id);
    }


    @Override
    public boolean existsDni(String dni) {
        return barberoRepository.existsByDni(dni);
    }

}
