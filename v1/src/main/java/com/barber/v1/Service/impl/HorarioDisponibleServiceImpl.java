package com.barber.v1.Service.impl;

import com.barber.v1.Model.Barbero;
import com.barber.v1.Model.HorarioDisponible;
import com.barber.v1.Model.HorarioRangoRequest;
import com.barber.v1.Model.Reserva;
import com.barber.v1.Repository.BarberoRepository;
import com.barber.v1.Repository.HorarioDisponibleRepository;
import com.barber.v1.Repository.ReservaRepository;
import com.barber.v1.Service.HorarioDisponibleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HorarioDisponibleServiceImpl implements HorarioDisponibleService {

    private final ReservaRepository reservaRepository;
    private final HorarioDisponibleRepository horarioDisponibleRepository;
    private final BarberoRepository barberoRepository;

    public HorarioDisponibleServiceImpl(ReservaRepository reservaRepository, HorarioDisponibleRepository horarioDisponibleRepository, BarberoRepository barberoRepository) {
        this.reservaRepository = reservaRepository;
        this.horarioDisponibleRepository = horarioDisponibleRepository;
        this.barberoRepository = barberoRepository;
    }

    @Override
    public List<HorarioDisponible> obtenerHorariosDisponibles(LocalDate fecha) {
        return horarioDisponibleRepository.findByFechaAndReservadoFalse(fecha);
    }

    @Override
    public List<HorarioDisponible> obtenerHorariosPorBarberoYFecha(Long barberoId, LocalDate fecha) {
        // Usamos el método de tu repositorio que recibe barberoId y fecha
        List<HorarioDisponible> todos = horarioDisponibleRepository.findByBarberoIdAndFechaOrderByHoraAsc(barberoId, fecha);

        List<Reserva> reservasActivas = reservaRepository
                .findByBarberoAndFecha(barberoId, fecha).stream()
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
    @Transactional
    public HorarioDisponible crearHorario(Long barberoId, LocalDate fecha, LocalTime hora) {
        Barbero barbero = barberoRepository.findById(barberoId)
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado con ID: " + barberoId));

        if (existeHorario(barberoId, fecha, hora)) {
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
    public boolean existeHorario(Long barberoId, LocalDate fecha, LocalTime hora) {
        // Usamos directamente el método por ID de tu repositorio
        return horarioDisponibleRepository.existsByBarberoIdAndFechaAndHora(barberoId, fecha, hora);
    }

    @Override
    @Transactional
    public void generarBloquesDeHorario(HorarioRangoRequest request) {
        Barbero barbero = barberoRepository.findById(request.getBarberoId())
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado con ID: " + request.getBarberoId()));

        LocalTime horaActual = request.getHoraInicio();
        List<HorarioDisponible> nuevosHorarios = new ArrayList<>();

        while (horaActual.isBefore(request.getHoraFin())) {
            boolean existe = horarioDisponibleRepository.existsByBarberoIdAndFechaAndHora(
                request.getBarberoId(), request.getFecha(), horaActual
            );

            if (!existe) {
                HorarioDisponible horario = HorarioDisponible.builder()
                        .barbero(barbero)
                        .fecha(request.getFecha())
                        .hora(horaActual)
                        .reservado(false)
                        .build();
                nuevosHorarios.add(horario);
            }

            horaActual = horaActual.plusMinutes(request.getIntervaloMinutos());
        }

        if (!nuevosHorarios.isEmpty()) {
            horarioDisponibleRepository.saveAll(nuevosHorarios);
        }
    }
}