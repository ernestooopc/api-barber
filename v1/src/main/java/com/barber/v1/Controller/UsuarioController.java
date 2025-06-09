package com.barber.v1.Controller;

import com.barber.v1.Model.Usuario;
import com.barber.v1.Security.JwtUtil;
import com.barber.v1.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public UsuarioController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> createUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.existsCorreo(usuario.getCorreo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Correo ya registrado");
        }
        Usuario nuevo = usuarioService.createUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping
    public List<Usuario> listUsuarios() {
        return usuarioService.listUsuarios();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        if (usuarioService.findById(id).isPresent()) {
            usuarioService.updateUsuario(id, usuarioActualizado);
            return ResponseEntity.ok("Usuario actualizado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long id) {
        if (usuarioService.findById(id).isPresent()) {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.ok("Usuario eliminado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @GetMapping("/{id}")
    public Optional<Usuario> getById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    record LoginResponse(String token, String rol, Long id) {}


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        // 1) Recupera el Optional<Usuario>
        Optional<Usuario> optUsuario = usuarioService.findByCorreo(req.correo());

        // 2) Si no existe, devuelve 401
        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 3) Ahora sí obtienes el Usuario real
        Usuario usuario = optUsuario.get();

        // 4) Verifica la contraseña
        if (!usuario.getContrasena().equals(req.contrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 5) Genera el token y responde
        String token = jwtUtil.generateToken(usuario.getCorreo());
         return ResponseEntity.ok(new LoginResponse(token, usuario.getRol().name(), usuario.getId()));
    }

}
