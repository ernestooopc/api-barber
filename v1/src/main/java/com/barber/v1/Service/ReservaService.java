package com.barber.v1.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.barber.v1.Model.Reserva;

public interface ReservaService {

    Reserva crearReserva(Reserva reserva);
    
    Reserva actualizarReserva(Long id, Reserva reservaActualizada);
    
    void eliminarReserva(Long id);
    
    List<Reserva> listarReservas();
    
    Optional<Reserva> findById(Long id);
    
    List<Reserva> listarPorUsuarioId(Long usuarioId);
    
    List<Reserva> listarPorEstado(Reserva.Estado estado);
    
    List<Reserva> listarEntreFechas(LocalDateTime inicio, LocalDateTime fin);
    
    // Validar duplicados por fecha, hora y barbero
    boolean existeReservaEnFechaYBarbero(Long barberoId, LocalDateTime fechaHora);
    
    void cancelarReserva(Long reservaId);
}