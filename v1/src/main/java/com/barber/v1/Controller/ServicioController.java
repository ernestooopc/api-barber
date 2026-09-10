package com.barber.v1.Controller;

import java.util.List;

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

import com.barber.v1.Model.Servicio;
import com.barber.v1.Service.ServicioService;


@RestController
@RequestMapping("/api/tipocortes")
@CrossOrigin(origins = "http://localhost:4200")
public class ServicioController {


    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    // Crear nuevo tipo de corte
    @PostMapping
    public ResponseEntity<Servicio> crearServicio(@RequestBody Servicio tipoCorte) {
        if (servicioService.existsByNombre(tipoCorte.getNombre())) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(servicioService.crearServicio(tipoCorte));
    }

    // Obtener todos los tipos de corte
    @GetMapping
    public List<Servicio> listAll() {
        return servicioService.listarServicios();
    }

    // Buscar tipo de corte por ID
    @GetMapping("/{id}")
    public ResponseEntity<Servicio> findById(@PathVariable Long id) {
        return servicioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar tipo de corte
    @PutMapping("/{id}")
    public ResponseEntity<Servicio> update(@PathVariable Long id, @RequestBody Servicio servicioServiceActualizado) {
        try {
            return ResponseEntity.ok(servicioService.actualizarServicio(id, servicioServiceActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar tipo de corte
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
        return ResponseEntity.ok().build();
    }
}
