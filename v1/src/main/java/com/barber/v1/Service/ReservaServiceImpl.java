package com.barber.v1.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barber.v1.Model.Reserva;
import com.barber.v1.Repository.ReservaRepository;
@Service
public class ReservaServiceImpl implements ReservaService{


    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaServiceImpl(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    public Reserva createReserva(Reserva reserva) {
        Reserva saved = reservaRepository.save(reserva);
        return reservaRepository.findById(saved.getId()).get();
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


}
