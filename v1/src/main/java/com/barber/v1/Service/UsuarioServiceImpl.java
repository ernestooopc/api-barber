package com.barber.v1.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barber.v1.Model.Usuario;
import com.barber.v1.Repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario createUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }


    @Override
    public Usuario updateUsuario(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
            .map(usuarioExistente -> {
                usuarioExistente.setNombre(usuarioActualizado.getNombre());
                usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
                usuarioExistente.setRol(usuarioActualizado.getRol());
                usuarioExistente.setTipoCliente(usuarioActualizado.getTipoCliente());
                return usuarioRepository.save(usuarioExistente);
            })
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }


    @Override
    public List<Usuario> listUsuarios() {
        return usuarioRepository.findAll();
    }
   

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return Optional.ofNullable(usuarioRepository.findByCorreo(correo));
    }

    @Override
    public boolean existsCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    @Override
    public Optional<Usuario>findById(Long id){
        return usuarioRepository.findById(id);
    }


   
}
