package com.wakanda.security.camaras_inteligentes;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/security/cameras")
public class CameraController {

    @Autowired
    private CameraService cameraService;

    @GetMapping("/estado")
    @CircuitBreaker(name = "cameras", fallbackMethod = "fallbackEstado")
    public ResponseEntity<Map<String, Object>> obtenerEstado() {
        cameraService.simularDetecciones();
        Map<String, Object> response = new HashMap<>();
        response.put("camaras", cameraService.obtenerTodasLasCamaras());
        response.put("estadisticas", cameraService.obtenerEstadisticas());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/zona/{zona}")
    @CircuitBreaker(name = "cameras", fallbackMethod = "fallbackZona")
    public ResponseEntity<List<SecurityCamera>> obtenerPorZona(@PathVariable String zona) {
        List<SecurityCamera> camaras = cameraService.obtenerPorZona(zona);
        if (camaras.isEmpty()) {
            throw new RuntimeException("No hay cámaras en la zona: " + zona);
        }
        return ResponseEntity.ok(camaras);
    }

    @GetMapping("/alertas")
    @CircuitBreaker(name = "cameras", fallbackMethod = "fallbackAlertas")
    public ResponseEntity<List<SecurityCamera>> obtenerCamarasConAlertas() {
        return ResponseEntity.ok(cameraService.obtenerCamarasConAlertas());
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "cameras", fallbackMethod = "fallbackCamera")
    public ResponseEntity<SecurityCamera> obtenerCamara(@PathVariable String id) {
        return ResponseEntity.ok(cameraService.obtenerCamara(id));
    }

    @PostMapping("/{id}/detectar")
    @CircuitBreaker(name = "cameras", fallbackMethod = "fallbackDetectar")
    public ResponseEntity<Map<String, Object>> activarDeteccion(
            @PathVariable String id,
            @RequestParam String tipoDeteccion) {

        SecurityCamera cam = cameraService.activarDeteccion(id, tipoDeteccion);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "🎥 Detección registrada: " + tipoDeteccion);
        response.put("camara", cam);
        response.put("nivelAlerta", cam.getNivelAlerta());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reiniciar")
    @CircuitBreaker(name = "cameras", fallbackMethod = "fallbackReiniciar")
    public ResponseEntity<Map<String, Object>> reiniciarCamara(@PathVariable String id) {
        SecurityCamera cam = cameraService.reiniciarCamara(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "♻️ Cámara reiniciada exitosamente");
        response.put("camara", cam);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(cameraService.obtenerEstadisticas());
    }

    // ========== FALLBACK METHODS ==========

    public ResponseEntity<Map<String, Object>> fallbackEstado(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Sistema de cámaras temporalmente no disponible");
        response.put("mensaje", "⚠️ Modo de emergencia activado");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<List<SecurityCamera>> fallbackZona(String zona, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<List<SecurityCamera>> fallbackAlertas(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<SecurityCamera> fallbackCamera(String id, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<Map<String, Object>> fallbackDetectar(String id, String tipo, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo procesar la detección");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<Map<String, Object>> fallbackReiniciar(String id, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo reiniciar la cámara");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
