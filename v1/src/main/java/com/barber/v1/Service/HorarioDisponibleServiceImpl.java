package com.barber.v1.Service;

import com.barber.v1.Model.Barbero;
import com.barber.v1.Model.HorarioDisponible;
import com.barber.v1.Model.Reserva;
import com.barber.v1.Repository.HorarioDisponibleRepository;
import com.barber.v1.Repository.ReservaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioDisponibleServiceImpl implements HorarioDisponibleService {

    private final ReservaRepository reservaRepository;
    private final HorarioDisponibleRepository horarioDisponibleRepository;

    @Override
    public List<HorarioDisponible> obtenerHorariosDisponibles(LocalDate fecha) {
        return horarioDisponibleRepository.findByFechaAndReservadoFalse(fecha);
    }

    @Override
public List<HorarioDisponible> obtenerHorariosPorBarberoYFecha(Barbero barbero, LocalDate fecha) {
    List<HorarioDisponible> todos = horarioDisponibleRepository.findByBarberoAndFecha(barbero, fecha);

    List<Reserva> reservasActivas = reservaRepository
        .findByBarberoAndFecha(barbero.getId(), fecha).stream()
        .filter(r -> !r.getEstado().equals(Reserva.Estado.CANCELADA))
        .toList();

    Set<LocalTime> horasOcupadas = reservasActivas.stream()
        .map(r -> r.getFechaHora().toLocalTime())
        .collect(Collectors.toSet());

    return todos.stream()
        .filter(h -> !horasOcupadas.contains(h.getHora()))
        .collect(Collectors.toList());
}


    @Override
    public HorarioDisponible crearHorario(Barbero barbero, LocalDate fecha, LocalTime hora) {
        if (existeHorario(barbero, fecha, hora)) {
            throw new RuntimeException("El horario ya existe para este barbero");
        }

        HorarioDisponible nuevo = HorarioDisponible.builder()
                .barbero(barbero)
                .fecha(fecha)
                .hora(hora)
                .reservado(false)
                .build();

        return horarioDisponibleRepository.save(nuevo);
    }

    @Override
    public boolean existeHorario(Barbero barbero, LocalDate fecha, LocalTime hora) {
        return horarioDisponibleRepository.existsByBarberoAndFechaAndHora(barbero, fecha, hora);
    }
}
