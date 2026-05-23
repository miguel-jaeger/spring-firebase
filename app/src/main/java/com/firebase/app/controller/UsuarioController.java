package com.firebase.app.controller;

import com.firebase.app.model.Usuario;
import com.firebase.app.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 1. OBTENER UN USUARIO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable String id) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
            if (usuario != null) {
                return ResponseEntity.ok(usuario); 
            } else {
                return ResponseEntity.notFound().build(); 
            }
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build(); 
        }
    }

    // 2. LISTAR TODOS LOS USUARIOS
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarTodos();
            return ResponseEntity.ok(usuarios);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 3. CREAR UN NUEVO USUARIO
    @PostMapping
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        try {
            String idGenerado = usuarioService.insertar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuario creado exitosamente con ID: " + idGenerado);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().body("Error al insertar el usuario: " + e.getMessage());
        }
    }

    // 4. ACTUALIZAR UN USUARIO EXISTENTE
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable String id, @RequestBody Usuario usuario) {
        try {
            // Validamos primero si el usuario existe antes de intentar actualizarlo
            if (usuarioService.obtenerUsuarioPorId(id) == null) {
                return ResponseEntity.notFound().build();
            }
            String tiempoActualizacion = usuarioService.actualizar(id, usuario);
            return ResponseEntity.ok("Usuario actualizado en la fecha/hora: " + tiempoActualizacion);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().body("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    // 5. ELIMINAR UN USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable String id) {
        try {
            if (usuarioService.obtenerUsuarioPorId(id) == null) {
                return ResponseEntity.notFound().build();
            }
            usuarioService.eliminar(id);
            return ResponseEntity.ok("Usuario con ID " + id + " eliminado correctamente.");
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().body("Error al eliminar el usuario: " + e.getMessage());
        }
    }
}