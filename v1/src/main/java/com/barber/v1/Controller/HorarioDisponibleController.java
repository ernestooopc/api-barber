package com.barber.v1.Controller;

import com.barber.v1.Model.Barbero;
import com.barber.v1.Model.HorarioDisponible;
import com.barber.v1.Model.Reserva;
import com.barber.v1.Service.BarberoService;
import com.barber.v1.Service.HorarioDisponibleService;
import com.barber.v1.Repository.BarberoRepository;
import com.barber.v1.Repository.HorarioDisponibleRepository;
import com.barber.v1.Repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.barber.v1.Model.HorarioRangoRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/horarios-disponibles")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class HorarioDisponibleController {

    private final HorarioDisponibleService horarioDisponibleService;
    private final BarberoService barberoService;
    private final ReservaRepository reservaRepository;
    private final BarberoRepository barberoRepository;
    private final HorarioDisponibleRepository horarioDisponibleRepository;

    // Obtener horarios de un barbero en una fecha, excluyendo reservas activas
    @GetMapping("/barbero/{barberoId}")
    public List<HorarioDisponible> getPorBarberoYFecha(
            @PathVariable Long barberoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        Barbero barbero = barberoService.obtenerPorId(barberoId);
        List<HorarioDisponible> todos = horarioDisponibleService.obtenerHorariosPorBarberoYFecha(barbero, fecha);
        List<Reserva> reservasActivas = reservaRepository
                .findByBarberoAndFecha(barbero.getId(), fecha)
                .stream()
                .filter(r -> !r.getEstado().name().equalsIgnoreCase("CANCELADA"))
                .collect(Collectors.toList());
        Set<LocalTime> horasOcupadas = reservasActivas.stream()
                .map(r -> r.getFechaHora().toLocalTime())
                .collect(Collectors.toSet());
        return todos.stream()
                .filter(h -> !horasOcupadas.contains(h.getHora()))
                .collect(Collectors.toList());
    }

    @PostMapping
    public HorarioDisponible crearHorario(@RequestBody HorarioDisponible horarioDisponible) {
        return horarioDisponibleService.crearHorario(
                horarioDisponible.getBarbero(),
                horarioDisponible.getFecha(),
                horarioDisponible.getHora()
        );
    }

    @GetMapping
    public List<HorarioDisponible> getDisponiblesPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return horarioDisponibleService.obtenerHorariosDisponibles(fecha);
    }

    @PostMapping("/rango")
    public ResponseEntity<?> generarHorariosPorRango(@RequestBody HorarioRangoRequest request) {
        Barbero barbero = barberoRepository.findById(request.getBarberoId())
            .orElseThrow(() -> new RuntimeException("Barbero no encontrado"));

        LocalDate fecha = LocalDate.parse(request.getFecha());
        LocalTime inicio = LocalTime.parse(request.getInicio());
        LocalTime fin = LocalTime.parse(request.getFin());
        int intervalo = request.getIntervaloMinutos();

        List<HorarioDisponible> creados = new ArrayList<>();

        for (LocalTime hora = inicio; hora.isBefore(fin); hora = hora.plusMinutes(intervalo)) {
            boolean yaExiste = horarioDisponibleRepository
                .findByBarberoAndFechaAndHora(barbero, fecha, hora)
                .isPresent();

            if (!yaExiste) {
                HorarioDisponible nuevo = HorarioDisponible.builder()
                        .barbero(barbero)
                        .fecha(fecha)
                        .hora(hora)
                        .reservado(false)
                        .build();

                creados.add(horarioDisponibleRepository.save(nuevo));
            }
        }

        return ResponseEntity.ok("Horarios creados: " + creados.size());
    }

}
