package com.barber.v1.Service;

import com.barber.v1.Model.HorarioDisponible;
import com.barber.v1.Model.Barbero;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface HorarioDisponibleService {

    List<HorarioDisponible> obtenerHorariosDisponibles(LocalDate fecha);

    List<HorarioDisponible> obtenerHorariosPorBarberoYFecha(Barbero barbero, LocalDate fecha);

    HorarioDisponible crearHorario(Barbero barbero, LocalDate fecha, LocalTime hora);

    boolean existeHorario(Barbero barbero, LocalDate fecha, LocalTime hora);
}
