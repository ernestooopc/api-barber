package com.barber.v1.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.barber.v1.Model.Reserva;

public interface ReservaService {


    Reserva createReserva(Reserva reserva);
    Reserva updateReserva(Long id, Reserva updatedReserva);
    void deleteReserva(Long id);
    List<Reserva> listReservas();
    Optional<Reserva> findById(Long id);
    List<Reserva> findByUserId(Long userId);
    List<Reserva> findByStatus(Reserva.Estado status);
    List<Reserva> findBetweenDates(LocalDateTime from, LocalDateTime to);
    boolean existsReservaAtDate(LocalDateTime dateTime);
    void cancelarReserva(Long reservaId);

}
