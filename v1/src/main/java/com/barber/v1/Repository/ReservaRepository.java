package com.barber.v1.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.barber.v1.Model.Reserva;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioId(Long usuarioId);
    List<Reserva> findByEstado(Reserva.Estado estado);

    //Lo utilizo para obtener las citas que caen dentro de un rango de tiempo específico. lenguaje JPQL
    List<Reserva> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    //valido si mi cliente ya agendó una cita a esa misma hora, así evito duplicados.
    boolean existsByUsuarioIdAndFechaHora(Long usuarioId, LocalDateTime fechaHora);

    // Me permite sacar la agenda de citas completa de un barbero en un día exacto.
    @Query("SELECT r FROM Reserva r WHERE r.barbero.id = :barberoId AND DATE(r.fechaHora) = :fecha")
    List<Reserva> findByBarberoAndFecha(@Param("barberoId") Long barberoId, @Param("fecha") LocalDate fecha);

    // Lo aplico para detectar citas pasadas que se quedaron estancadas en un estado en específico.
    List<Reserva> findByEstadoAndFechaHoraBefore(Reserva.Estado estado, LocalDateTime fechaHora); 
    
    // Hace lo mismo que el anterior, pero me permite buscar múltiples estados a la vez.
    List<Reserva> findByEstadoInAndFechaHoraBefore(
    List<Reserva.Estado> estados, 
    LocalDateTime fechaHora

    );
}
