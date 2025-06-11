package com.barber.v1.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    private TipoCorte tipoCorte;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE;

    //Enumerated ayuda que el estado solo puede ser uno de estos 3

    public enum Estado {
        PENDIENTE, CANCELADA, COMPLETADA
    }
    
    @ManyToOne
    @JoinColumn(name = "barbero_id", nullable = false)
    private Barbero barbero;
    
}