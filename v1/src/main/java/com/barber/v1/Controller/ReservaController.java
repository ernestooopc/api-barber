package com.barber.v1.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public Reserva createReserva(@RequestBody Reserva reserva) {
        return reservaService.createReserva(reserva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable Long id, @RequestBody Reserva reserva) {
        Reserva updated = reservaService.updateReserva(id, reserva);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        reservaService.deleteReserva(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Reserva> listReservas() {
        return reservaService.listReservas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> findById(@PathVariable Long id) {
        Optional<Reserva> reserva = reservaService.findById(id);
        return reserva.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Reserva> findByUsuarioId(@PathVariable Long usuarioId) {
        return reservaService.findByUserId(usuarioId);
    }

    @GetMapping("/estado/{estado}")
    public List<Reserva> findByEstado(@PathVariable Reserva.Estado estado) {
        return reservaService.findByStatus(estado);
    }

    @GetMapping("/entre-fechas")
    public List<Reserva> findBetweenDates(@RequestParam("desde") String desde, @RequestParam("hasta") String hasta) {
        LocalDateTime fechaDesde = LocalDateTime.parse(desde.trim());
        LocalDateTime fechaHasta = LocalDateTime.parse(hasta.trim());
        return reservaService.findBetweenDates(fechaDesde, fechaHasta);
    }

    @GetMapping("/existe-en-fecha")
    public boolean existsByFechaHora(@RequestParam("fechaHora") String fechaHora) {
        return reservaService.existsReservaAtDate(LocalDateTime.parse(fechaHora));
    }

    /** PATCH para cambiar sólo el estado */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Reserva> changeEstado(
            @PathVariable Long id,
            @RequestParam("nuevoEstado") Reserva.Estado nuevoEstado) {
        return reservaService.findById(id)
                .map(r -> {
                    r.setEstado(nuevoEstado);
                    Reserva saved = reservaService.updateReserva(id, r);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.ok("Reserva cancelada exitosamente.");
    }

}