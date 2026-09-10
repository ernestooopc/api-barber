package com.barber.v1.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barber.v1.Model.Reserva;
import com.barber.v1.Service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<?> createReserva(@RequestBody Reserva reserva) {
        try {
            Reserva nueva = reservaService.crearReserva(reserva);
            return ResponseEntity.status(201).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable Long id, @RequestBody Reserva reserva) {
        try {
            Reserva updated = reservaService.actualizarReserva(id, reserva);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        try {
            reservaService.eliminarReserva(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Reserva> listReservas() {
        return reservaService.listarReservas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> findById(@PathVariable Long id) {
        Optional<Reserva> reserva = reservaService.findById(id);
        return reserva.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Reserva> findByUsuarioId(@PathVariable Long usuarioId) {
        return reservaService.listarPorUsuarioId(usuarioId);
    }

    @GetMapping("/estado/{estado}")
    public List<Reserva> findByEstado(@PathVariable Reserva.Estado estado) {
        return reservaService.listarPorEstado(estado);
    }

    @GetMapping("/entre-fechas")
    public List<Reserva> findBetweenDates(@RequestParam("desde") String desde, @RequestParam("hasta") String hasta) {
        LocalDateTime fechaDesde = LocalDateTime.parse(desde.trim());
        LocalDateTime fechaHasta = LocalDateTime.parse(hasta.trim());
        return reservaService.listarEntreFechas(fechaDesde, fechaHasta);
    }

    /** PATCH para cambiar sólo el estado */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> changeEstado(
            @PathVariable Long id,
            @RequestParam("nuevoEstado") Reserva.Estado nuevoEstado) {
        try {
            return reservaService.findById(id)
                    .map(r -> {
                        r.setEstado(nuevoEstado);
                        Reserva saved = reservaService.actualizarReserva(id, r);
                        return ResponseEntity.ok(saved);
                    })
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id) {
        try {
            reservaService.cancelarReserva(id);
            return ResponseEntity.ok("Reserva cancelada exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}