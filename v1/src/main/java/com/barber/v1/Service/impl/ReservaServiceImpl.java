package com.barber.v1.Service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.barber.v1.Model.HorarioDisponible;
import com.barber.v1.Model.Reserva;
import com.barber.v1.Repository.HorarioDisponibleRepository;
import com.barber.v1.Repository.ReservaRepository;
import com.barber.v1.Service.ReservaService;

import jakarta.transaction.Transactional;

@Service
public class ReservaServiceImpl implements ReservaService {

   
    private final HorarioDisponibleRepository horarioDisponibleRepository;
    private final ReservaRepository reservaRepository;


    public ReservaServiceImpl(ReservaRepository reservaRepository, HorarioDisponibleRepository horarioDisponibleRepository) {
        this.reservaRepository = reservaRepository;
        this.horarioDisponibleRepository = horarioDisponibleRepository;
    }

    @Override
    public Reserva crearReserva(Reserva reserva) {
        LocalDate fecha = reserva.getFechaHora().toLocalDate();
        LocalTime hora = reserva.getFechaHora().toLocalTime();
        Long barberoId = reserva.getBarbero().getId();

        HorarioDisponible horario = horarioDisponibleRepository
                .findByBarberoIdAndFechaAndHora(barberoId, fecha, hora)
                .orElseThrow(() -> new RuntimeException("El horario no está disponible"));

        if (horario.isReservado()) {
            throw new RuntimeException("Este horario ya fue reservado.");
        }

        horario.setReservado(true);
        horarioDisponibleRepository.save(horario);

        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva actualizarReserva(Long id, Reserva updatedReserva) {
        return reservaRepository.findById(id)
                .map(existing -> {
                    existing.setFechaHora(updatedReserva.getFechaHora());
                    existing.setEstado(updatedReserva.getEstado());
                    existing.setUsuario(updatedReserva.getUsuario());
                    existing.setServicio(updatedReserva.getServicio());
                    return reservaRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }

    @Override
    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    @Override
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    @Override
    public Optional<Reserva> findById(Long id) {
        return reservaRepository.findById(id);
    }

    @Override
    public List<Reserva> listarPorUsuarioId(Long userId) {
        return reservaRepository.findByUsuarioId(userId);
    }

    @Override
    public List<Reserva> listarPorEstado(Reserva.Estado status) {
        return reservaRepository.findByEstado(status);
    }

    @Override
    public List<Reserva> listarEntreFechas(LocalDateTime from, LocalDateTime to) {
        return reservaRepository.findAll().stream()
                .filter(r -> !r.getFechaHora().isBefore(from) && !r.getFechaHora().isAfter(to))
                .toList();
    }

    @Override
    public boolean existeReservaEnFechaYBarbero(Long barberoId, LocalDateTime dateTime) {
        return reservaRepository.findAll().stream()
                .anyMatch(r -> r.getFechaHora().equals(dateTime) && r.getBarbero().getId().equals(barberoId));
    }


    @Override 
    @Transactional
    public void cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // 1) Validar plazo de 24 horas
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime cita = reserva.getFechaHora();
        long horasRestantes = Duration.between(ahora, cita).toHours();
        if (horasRestantes < 24) {
            throw new RuntimeException("Solo puedes cancelar hasta 24 horas antes de la cita");
        }

        // 2) Liberar el horario
        LocalDate fecha = cita.toLocalDate();
        LocalTime hora = cita.toLocalTime();
        Long barberoId = reserva.getBarbero().getId();

        HorarioDisponible horario = horarioDisponibleRepository
                .findByBarberoIdAndFechaAndHora(barberoId, fecha, hora)
                .orElseThrow(() -> new IllegalStateException("HORARIO_OCUPADO"));

        horario.setReservado(false);
        horarioDisponibleRepository.save(horario);

        // 3) Marcar reserva como cancelada
        reserva.setEstado(Reserva.Estado.CANCELADA);
        reservaRepository.save(reserva);
    }

}
