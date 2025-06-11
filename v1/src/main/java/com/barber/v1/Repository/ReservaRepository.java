package com.barber.v1.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barber.v1.Model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuarioId(Long usuarioId);
    List<Reserva> findByEstado(Reserva.Estado estado);
    List<Reserva> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    boolean existsByUsuarioIdAndFechaHora(Long usuarioId, LocalDateTime fechaHora);
    @Query("SELECT r FROM Reserva r WHERE r.barbero.id = :barberoId AND DATE(r.fechaHora) = :fecha")
    List<Reserva> findByBarberoAndFecha(@Param("barberoId") Long barberoId, @Param("fecha") LocalDate fecha);

}
