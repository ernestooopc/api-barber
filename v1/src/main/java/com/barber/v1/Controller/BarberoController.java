package com.barber.v1.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public BarberoController(BarberoService barberoService){
        this.barberoService = barberoService;
    }


    @PostMapping
    public ResponseEntity<?>createBarbero(@RequestBody Barbero barbero){
        if(barberoService.existsDni(barbero.getDni())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("DNI Ya Registrado");
        }
        Barbero newBarbero = barberoService.createBarbero(barbero);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBarbero);
        
    }



    @GetMapping
    public List<Barbero> listBarberos(){
        return barberoService.listBarberos();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Barbero> updateBarbero(@PathVariable Long id,@RequestBody Barbero barberoActualizado) {
        Barbero actualizado = barberoService.updateBarbero(id, barberoActualizado);
        return ResponseEntity.ok(actualizado);
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarbero(@PathVariable Long id) {
        barberoService.deleteBarbero(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public Optional<Barbero> getById(@PathVariable Long id) {
        return barberoService.findById(id);
    }



}
