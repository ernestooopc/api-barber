package com.barber.v1.Model;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class HorarioRangoRequest {
    private Long barberoId;
    private LocalDate fecha;       
    private LocalTime horaInicio; 
    private LocalTime horaFin;   
    private int intervaloMinutos; 
}