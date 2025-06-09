package com.barber.v1.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barber.v1.Model.Asignacion;
import com.barber.v1.Service.AsignacionService;

@RestController
@RequestMapping("/api/asignaciones")
@CrossOrigin(origins = "http://localhost:4200")
public class AsignacionController {

    private final AsignacionService asignacionService;

    @Autowired
    public AsignacionController(AsignacionService asignacionService) {
        this.asignacionService = asignacionService;
    }

    // Crear asignación
    @PostMapping
    public ResponseEntity<Asignacion> createAsignacion(@RequestBody Asignacion asignacion) {
        return ResponseEntity.ok(asignacionService.createAsignacion(asignacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Asignacion> actualizarAsignacion(
        @PathVariable Long id,
        @RequestBody Asignacion asignacionActualizada) {
    try {
        Asignacion actualizada = asignacionService.updateAsignacion(id, asignacionActualizada);
        return ResponseEntity.ok(actualizada);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
    }


    // Listar todas las asignaciones
    @GetMapping
    public ResponseEntity<List<Asignacion>> listAsignaciones() {
        return ResponseEntity.ok(asignacionService.listAsignaciones());
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Asignacion> findById(@PathVariable Long id) {
        Optional<Asignacion> asignacion = asignacionService.findById(id);
        return asignacion.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsignacion(@PathVariable Long id) {
        asignacionService.deleteAsignacion(id);
        return ResponseEntity.ok().build();
    }

    // Verificar si existe una asignación por reserva y barbero
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByReservaAndBarbero(
            @RequestParam Long reservaId,
            @RequestParam Long barberoId) {
        boolean exists = asignacionService.existsAsignacion(reservaId, barberoId);
        return ResponseEntity.ok(exists);
    }
}