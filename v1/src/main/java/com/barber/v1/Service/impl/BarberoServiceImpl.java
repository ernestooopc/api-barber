package com.barber.v1.Service.impl;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.barber.v1.Model.Barbero;
import com.barber.v1.Model.Usuario;
import com.barber.v1.Repository.BarberoRepository;

import com.barber.v1.Service.BarberoService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.transaction.Transactional;
@Service
public class BarberoServiceImpl implements BarberoService {

    private final BarberoRepository barberoRepository;

    public BarberoServiceImpl(BarberoRepository barberoRepository) {
        this.barberoRepository = barberoRepository;
    }

    @Override
    public List<Barbero> listBarberos() {
        return barberoRepository.findAll();
    }

    @Override
    public Optional<Barbero> findById(Long id) {
        return barberoRepository.findById(id);
    }

    @Override
    @Transactional
    public Barbero registrarBarbero(Barbero barbero) { 
    String contrasenaTemporal = com.barber.v1.Security.PasswordUtils.generarContrasenaTemporal(barbero.getNombre());
    
    // 2. Cifrado fuerte
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    barbero.getUsuario().setContrasena(encoder.encode(contrasenaTemporal));
    barbero.getUsuario().setRol(Usuario.Rol.BARBERO);
    barbero.getUsuario().setActivo(true);
    barbero.getUsuario().setRequiereCambioContrasena(true);
    
    Barbero barberoGuardado = barberoRepository.save(barbero);
    
    //envio de correo contraseña pendiente
    
    return barberoGuardado;
}

    @Override
    public Barbero actualizarBarbero(Long id, Barbero barberoUpdate) {
        return barberoRepository.findById(id)
                .map(barberoExistente -> {
                    barberoExistente.setNombre(barberoUpdate.getNombre());
                    barberoExistente.setApellido(barberoUpdate.getApellido());
                    barberoExistente.setCorreo(barberoUpdate.getCorreo());
                    barberoExistente.setTelefono(barberoUpdate.getTelefono());
                    barberoExistente.setExperienciaAnios(barberoUpdate.getExperienciaAnios());
                    barberoExistente.setEspecialidad(barberoUpdate.getEspecialidad());
                    barberoExistente.setFechaIngreso(barberoUpdate.getFechaIngreso());
                    return barberoRepository.save(barberoExistente);
                })
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado con ID: " + id));
    }

    @Override
    public void desactivarBarbero(Long id) {
        barberoRepository.findById(id).ifPresent(barbero -> {
            barbero.getUsuario().setActivo(false);
            barberoRepository.save(barbero);
        });
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return barberoRepository.existsByCorreo(correo);
    }

    @Override
    public Optional<Barbero> findByUsuarioId(Long usuarioId) {
        return barberoRepository.findByUsuarioId(usuarioId);
    }


 
}
