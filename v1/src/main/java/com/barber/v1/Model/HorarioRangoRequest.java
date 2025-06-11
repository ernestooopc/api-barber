package com.barber.v1.Model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioRangoRequest {
    private Long barberoId;
    private String fecha; // formato YYYY-MM-DD
    private String inicio; // formato HH:mm
    private String fin; // formato HH:mm
    private int intervaloMinutos;
}
