package com.barber.v1.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barber.v1.Model.Barbero;
import com.barber.v1.Service.BarberoService;

@RestController
@RequestMapping("/api/barberos")
@CrossOrigin(origins = "http://localhost:4200")
public class BarberoController {

    private final BarberoService barberoService;

    public BarberoController(BarberoService barberoService) {
        this.barberoService = barberoService;
    }

    @PostMapping
    public ResponseEntity<?> registrarBarbero(@RequestBody Barbero barbero) {
        if (barberoService.existsByCorreo(barbero.getCorreo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Correo ya registrado");
        }
        Barbero newBarbero = barberoService.registrarBarbero(barbero);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBarbero);
    }

    @GetMapping
    public ResponseEntity<List<Barbero>> listBarberos() {
        List<Barbero> barberos = barberoService.listBarberos();
        return ResponseEntity.ok(barberos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Barbero> getById(@PathVariable Long id) {
        return barberoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarBarbero(@PathVariable Long id, @RequestBody Barbero barberoActualizado) {
        try {
            Barbero actualizado = barberoService.actualizarBarbero(id, barberoActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarBarbero(@PathVariable Long id) {
        try {
            barberoService.desactivarBarbero(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}