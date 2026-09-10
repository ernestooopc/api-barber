package com.barber.v1.Model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Servicio servicio;

    @OneToOne
    @JoinColumn(name = "horario_disponible_id", nullable = false)
    private HorarioDisponible horarioDisponible;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE;

    // Campo indispensable para que BoletaService y los Repositorios funcionen sin errores
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;


    //Enumerated ayuda que el estado solo puede ser uno de estos 3

    public enum Estado {
        PENDIENTE, CANCELADA, COMPLETADA, PAGADO
    }
    
    @ManyToOne
    @JoinColumn(name = "barbero_id", nullable = false)
    private Barbero barbero;
    
}