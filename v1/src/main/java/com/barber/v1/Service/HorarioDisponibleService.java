package com.barber.v1.Service;

import com.barber.v1.Model.HorarioDisponible;
import com.barber.v1.Model.HorarioRangoRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface HorarioDisponibleService {

    List<HorarioDisponible> obtenerHorariosDisponibles(LocalDate fecha);

    List<HorarioDisponible> obtenerHorariosPorBarberoYFecha(Long barberoId, LocalDate fecha);

    HorarioDisponible crearHorario(Long barberoId, LocalDate fecha, LocalTime hora);

    boolean existeHorario(Long barberoId, LocalDate fecha, LocalTime hora);

    void generarBloquesDeHorario(HorarioRangoRequest request);
}