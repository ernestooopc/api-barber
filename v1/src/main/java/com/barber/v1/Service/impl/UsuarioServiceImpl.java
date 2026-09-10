package com.barber.v1.Service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barber.v1.Model.Usuario;
import com.barber.v1.Model.Usuario.NivelLealtad;
import com.barber.v1.Repository.UsuarioRepository;
import com.barber.v1.Service.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;


    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public Usuario registrarCliente(Usuario usuario) {
        log.info("Registrando nuevo cliente con correo: {}", usuario.getCorreo());
        
        // Blindaje: Forzamos el rol de cliente para registros públicos
        usuario.setRol(Usuario.Rol.CLIENTE);
        usuario.setActivo(true);
        usuario.setRequiereCambioContrasena(false);
        
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        log.info("Actualizando datos del usuario con ID: {}", id);
        
        return usuarioRepository.findById(id)
            .map(usuarioExistente -> {
                usuarioExistente.setNombre(usuarioActualizado.getNombre());
                usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
                usuarioExistente.setRol(usuarioActualizado.getRol());
                usuarioExistente.setNivelLealtad(usuarioActualizado.getNivelLealtad());
                return usuarioRepository.save(usuarioExistente);
            })
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public void desactivarUsuario(Long id) {
        log.info("Desactivando lógicamente al usuario con ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        // Desactivación lógica en lugar de borrado físico para conservar el historial
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> listUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional
    public void actualizarContrasena(Long id, String nuevaContrasena) {
        log.info("Actualizando contraseña para el usuario ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        usuario.setContrasena(nuevaContrasena);
        // Apagamos el interruptor de cambio obligatorio ya que el usuario estableció su nueva clave
        usuario.setRequiereCambioContrasena(false);
        
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario registrarVisitaYActualizarLealtad(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        int nuevasVisitas = (usuario.getVisitasTotales() == null ? 0 : usuario.getVisitasTotales()) + 1;
        usuario.setVisitasTotales(nuevasVisitas);

        // Reglas de negocio del Enum NivelLealtad
        if (nuevasVisitas >= 25) {
            usuario.setNivelLealtad(NivelLealtad.BLACK);
        } else if (nuevasVisitas >= 10) {
            usuario.setNivelLealtad(NivelLealtad.ORO);
        } else {
            usuario.setNivelLealtad(NivelLealtad.CLASICO);
        }

        log.info("Usuario ID {} ahora tiene {} visitas. Nivel: {}", usuarioId, nuevasVisitas, usuario.getNivelLealtad());
        return usuarioRepository.save(usuario);   
    }
}