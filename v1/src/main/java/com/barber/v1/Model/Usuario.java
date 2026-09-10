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
    
    
    @Column(nullable=false)
    private String apellido;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    private Boolean activo = true;

    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(nullable = false)
    private Integer visitasTotales = 0;

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.CLIENTE;   

    @Enumerated(EnumType.STRING)
    private NivelLealtad nivelLealtad = NivelLealtad.CLASICO;




    private Boolean requiereCambioContrasena = false;
    public enum Rol {
        CLIENTE, ADMIN, BARBERO
    }

    public enum NivelLealtad {
        CLASICO, // Nivel inicial (0 a 9 visitas)
        ORO,     // Nivel intermedio (10 a 24 visitas)
        BLACK    // Nivel máximo (25+ visitas)
    }
}