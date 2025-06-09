package com.barber.v1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barber.v1.Model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{


    boolean existsByCorreo(String correo);  
    Usuario findByCorreo(String correo);

    
}
