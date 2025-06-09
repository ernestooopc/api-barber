package com.barber.v1.Controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.barber.v1.Model.TipoCorte;
import com.barber.v1.Service.TipoCorteService;


@RestController
@RequestMapping("/api/tipocortes")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoCorteController {
private final TipoCorteService tipoCorteService;

    @Autowired
    public TipoCorteController(TipoCorteService tipoCorteService) {
        this.tipoCorteService = tipoCorteService;
    }

    // Crear nuevo tipo de corte
    @PostMapping
    public ResponseEntity<TipoCorte> create(@RequestBody TipoCorte tipoCorte) {
        if (tipoCorteService.existsByNombre(tipoCorte.getNombre())) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(tipoCorteService.createTipoCorte(tipoCorte));
    }

    // Obtener todos los tipos de corte
    @GetMapping
    public List<TipoCorte> listAll() {
        return tipoCorteService.listTipoCortes();
    }

    // Buscar tipo de corte por ID
    @GetMapping("/{id}")
    public ResponseEntity<TipoCorte> findById(@PathVariable Long id) {
        return tipoCorteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar tipo de corte
    @PutMapping("/{id}")
    public ResponseEntity<TipoCorte> update(@PathVariable Long id, @RequestBody TipoCorte tipoCorteActualizado) {
        try {
            return ResponseEntity.ok(tipoCorteService.updateTipoCorteo(id, tipoCorteActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar tipo de corte
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoCorteService.deleteTipoCorte(id);
        return ResponseEntity.ok().build();
    }
}
