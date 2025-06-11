package com.barber.v1.Repository;

import com.barber.v1.Model.HorarioDisponible;
import com.barber.v1.Model.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioDisponibleRepository extends JpaRepository<HorarioDisponible, Long> {

    List<HorarioDisponible> findByFechaAndReservadoFalse(LocalDate fecha);

    List<HorarioDisponible> findByBarberoAndFechaAndReservadoFalse(Barbero barbero, LocalDate fecha);
    List<HorarioDisponible> findByBarberoAndFecha(Barbero barbero, LocalDate fecha);

    boolean existsByBarberoAndFechaAndHora(Barbero barbero, LocalDate fecha, java.time.LocalTime hora);
    Optional<HorarioDisponible> findByBarberoAndFechaAndHora(Barbero barbero, LocalDate fecha, LocalTime hora);

}
