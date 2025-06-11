package com.barber.v1.Model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Barbero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private Integer experienciaAnios;

    private String especialidad; 

    private LocalDateTime fechaIngreso = LocalDateTime.now();
}
