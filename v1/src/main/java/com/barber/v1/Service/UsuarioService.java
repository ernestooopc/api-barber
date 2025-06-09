package com.barber.v1.Service;

import java.util.List;
import java.util.Optional;

import com.barber.v1.Model.Usuario;

public interface UsuarioService {
    Usuario createUsuario(Usuario usuario);
    Usuario updateUsuario(Long id, Usuario usuarioActualizado);
    void deleteUsuario(Long id);
    List<Usuario> listUsuarios();
    Optional<Usuario> findByCorreo(String correo);
    boolean existsCorreo(String correo);
    Optional<Usuario>findById(Long id);
}
