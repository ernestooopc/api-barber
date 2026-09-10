package com.barber.v1.Repository;

import com.barber.v1.Model.HorarioDisponible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioDisponibleRepository extends JpaRepository<HorarioDisponible, Long> {

    //QUERY METHODS
    // 1. General: Todos los turnos libres del local hoy (sin importar el barbero)
    List<HorarioDisponible> findByFechaAndReservadoFalse(LocalDate fecha);

    // 2. Cliente: Turnos libres de un barbero específico (Ordenados para mostrar en la app)
    List<HorarioDisponible> findByBarberoIdAndFechaAndReservadoFalseOrderByHoraAsc(Long barberoId, LocalDate fecha);
    
    // 3. Barbero/Admin: Toda la agenda del día (libres y ocupados, ordenados)
    List<HorarioDisponible> findByBarberoIdAndFechaOrderByHoraAsc(Long barberoId, LocalDate fecha);

    // 4. Generación: Valida si ya existe el bloque antes de crearlo (usando ID)
    boolean existsByBarberoIdAndFechaAndHora(Long barberoId, LocalDate fecha, LocalTime hora);
    
    // 5. Reserva: Trae el bloque exacto para ocuparlo (usando ID)
    Optional<HorarioDisponible> findByBarberoIdAndFechaAndHora(Long barberoId, LocalDate fecha, LocalTime hora);
}

