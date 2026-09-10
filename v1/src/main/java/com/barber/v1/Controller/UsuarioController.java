package com.barber.v1.Controller;

import com.barber.v1.Model.Usuario;
import com.barber.v1.Security.JwtUtil;
import com.barber.v1.Service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// DTOs para petición y respuesta de login
record LoginRequest(String correo, String contrasena) {
}

record LoginResponse(String token, String rol) {
}

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")

public class UsuarioController {

    private final UsuarioService usuarioService;

    private final JwtUtil jwtUtil; // tu utilidad para generar JWT

    public UsuarioController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> registrarCliente(@RequestBody Usuario usuario) {
        if (usuarioService.existsByCorreo(usuario.getCorreo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Correo ya registrado");
        }
        Usuario nuevo = usuarioService.registrarCliente(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping
    public List<Usuario> listUsuarios() {
        return usuarioService.listUsuarios();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        if (usuarioService.findById(id).isPresent()) {
            usuarioService.actualizarUsuario(id, usuarioActualizado);
            return ResponseEntity.ok("Usuario actualizado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> desactivarUsuario(@PathVariable Long id) {
        if (usuarioService.findById(id).isPresent()) {
            usuarioService.desactivarUsuario(id);
            return ResponseEntity.ok("Usuario desactivado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @GetMapping("/{id}")
    public Optional<Usuario> getById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    record LoginResponse(String token, String rol, Long id, String nivelLealtad) {}

    @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<Usuario> optUsuario = usuarioService.findByCorreo(req.correo());

        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }

        Usuario usuario = optUsuario.get();

        // Cambiado de isActivo() a getActivo() para evitar el error de compilación
        if (usuario.getActivo() != null && !usuario.getActivo()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El usuario se encuentra inactivo");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(req.contrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }

        String token = jwtUtil.generateToken(usuario.getCorreo());
        String nivel = usuario.getNivelLealtad() != null ? usuario.getNivelLealtad().name() : "CLASICO";

        return ResponseEntity.ok(new LoginResponse(token, usuario.getRol().name(), usuario.getId(), nivel));
    }
}
