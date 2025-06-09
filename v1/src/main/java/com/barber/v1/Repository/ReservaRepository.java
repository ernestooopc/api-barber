package com.barber.v1.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barber.v1.Model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuarioId(Long usuarioId);
    List<Reserva> findByEstado(Reserva.Estado estado);
    List<Reserva> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    boolean existsByUsuarioIdAndFechaHora(Long usuarioId, LocalDateTime fechaHora);
}
