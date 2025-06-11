package com.barber.v1.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barber.v1.Model.HorarioDisponible;
import com.barber.v1.Model.Reserva;
import com.barber.v1.Repository.HorarioDisponibleRepository;
import com.barber.v1.Repository.ReservaRepository;

import jakarta.transaction.Transactional;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private HorarioDisponibleRepository horarioDisponibleRepository;

    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaServiceImpl(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    public Reserva createReserva(Reserva reserva) {
        LocalDate fecha = reserva.getFechaHora().toLocalDate();
        LocalTime hora = reserva.getFechaHora().toLocalTime();

        HorarioDisponible horario = horarioDisponibleRepository
                .findByBarberoAndFechaAndHora(reserva.getBarbero(), fecha, hora)
                .orElseThrow(() -> new RuntimeException("El horario no está disponible"));

        if (horario.isReservado()) {
            throw new RuntimeException("Este horario ya fue reservado.");
        }

        horario.setReservado(true);
        horarioDisponibleRepository.save(horario);
        

        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva updateReserva(Long id, Reserva updatedReserva) {
        return reservaRepository.findById(id)
                .map(existing -> {
                    existing.setFechaHora(updatedReserva.getFechaHora());
                    existing.setEstado(updatedReserva.getEstado());
                    existing.setUsuario(updatedReserva.getUsuario());
                    existing.setTipoCorte(updatedReserva.getTipoCorte());
                    return reservaRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }

    @Override
    public void deleteReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    @Override
    public List<Reserva> listReservas() {
        return reservaRepository.findAll();
    }

    @Override
    public Optional<Reserva> findById(Long id) {
        return reservaRepository.findById(id);
    }

    @Override
    public List<Reserva> findByUserId(Long userId) {
        return reservaRepository.findByUsuarioId(userId);
    }

    @Override
    public List<Reserva> findByStatus(Reserva.Estado status) {
        return reservaRepository.findByEstado(status);
    }

    @Override
    public List<Reserva> findBetweenDates(LocalDateTime from, LocalDateTime to) {
        return reservaRepository.findAll().stream()
                .filter(r -> !r.getFechaHora().isBefore(from) && !r.getFechaHora().isAfter(to))
                .toList();
    }

    @Override
    public boolean existsReservaAtDate(LocalDateTime dateTime) {
        return reservaRepository.findAll().stream()
                .anyMatch(r -> r.getFechaHora().equals(dateTime));
    }

    @Transactional
public void cancelarReserva(Long reservaId) {
    Reserva reserva = reservaRepository.findById(reservaId)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

    LocalDate fecha = reserva.getFechaHora().toLocalDate();
    LocalTime hora = reserva.getFechaHora().toLocalTime();

    HorarioDisponible horario = horarioDisponibleRepository
            .findByBarberoAndFechaAndHora(reserva.getBarbero(), fecha, hora)
            .orElseThrow(() -> new IllegalStateException("HORARIO_OCUPADO"));


    // Liberar el horario
    horario.setReservado(false);
    horarioDisponibleRepository.save(horario);

    // Marcar reserva como cancelada
    reserva.setEstado(Reserva.Estado.CANCELADA);
    reservaRepository.save(reserva);
}


}
