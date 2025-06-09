package com.barber.v1.Model;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

 

    private Boolean activo = true;

    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.USUARIO;   

    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente = TipoCliente.NUEVO;

    public enum Rol {
        USUARIO, ADMIN
    }

    public enum TipoCliente {
        NUEVO, FRECUENTE
    }
}