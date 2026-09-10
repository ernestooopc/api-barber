package com.barber.v1.Service;

import java.util.List;
import java.util.Optional;
import com.barber.v1.Model.Usuario;

public interface UsuarioService {
    
    Usuario registrarCliente(Usuario usuario);
    
    Usuario actualizarUsuario(Long id, Usuario usuarioActualizado);
    
    void desactivarUsuario(Long id);
    
    List<Usuario> listUsuarios();
    
    Optional<Usuario> findByCorreo(String correo);
    
    boolean existsByCorreo(String correo);
    
    Optional<Usuario> findById(Long id);
    
    void actualizarContrasena(Long id, String nuevaContrasena);
    Usuario registrarVisitaYActualizarLealtad(Long usuarioId);

    
}