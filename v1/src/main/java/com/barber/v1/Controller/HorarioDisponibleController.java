package com.barber.v1.Controller;

import com.barber.v1.Model.HorarioDisponible;
import com.barber.v1.Service.HorarioDisponibleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.barber.v1.Model.HorarioRangoRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/api/horarios-disponibles")
@CrossOrigin(origins = "http://localhost:4200")
public class HorarioDisponibleController {

        private final HorarioDisponibleService horarioDisponibleService;

        public HorarioDisponibleController(HorarioDisponibleService horarioDisponibleService) {
                this.horarioDisponibleService = horarioDisponibleService;
        }


        // Obtener horarios de un barbero en una fecha, excluyendo reservas activas
        @GetMapping("/barbero/{barberoId}")
        public List<HorarioDisponible> getPorBarberoYFecha(
            @PathVariable Long barberoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return horarioDisponibleService.obtenerHorariosPorBarberoYFecha(barberoId, fecha);
    }

        @PostMapping
        public HorarioDisponible crearHorario(@RequestBody HorarioDisponible horarioDisponible) {
                Long barberoId = horarioDisponible.getBarbero() != null ? horarioDisponible.getBarbero().getId() : null;
                return horarioDisponibleService.crearHorario(
                        barberoId,
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
                horarioDisponibleService.generarBloquesDeHorario(request);
        return ResponseEntity.ok("Horarios generados correctamente por rango.");
        }

        @GetMapping("/verificar")
                public ResponseEntity<Boolean> verificarDisponibilidad(
                        @RequestParam Long barberoId,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                        @RequestParam String hora) {
                LocalTime horaParsed = LocalTime.parse(hora);
                boolean existe = horarioDisponibleService.existeHorario(barberoId, fecha, horaParsed);
                return ResponseEntity.ok(existe);
        }

}
