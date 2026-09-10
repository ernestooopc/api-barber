package com.barber.v1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.barber.v1.Model.Usuario;
import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{


    boolean existsByCorreo(String correo);  

    Optional<Usuario> findByCorreo(String correo);

    //Traer todos los usuarios que son Barberos o Admins
    List<Usuario> findByRol(Usuario.Rol rol);
    
    //Traer a los clientes VIP (Nivel BLACK) para enviarles promos
    List<Usuario> findByNivelLealtad(Usuario.NivelLealtad nivelLealtad);
}