package com.wakanda.security.live_alerts;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/security/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping("/activas")
    @CircuitBreaker(name = "alerts", fallbackMethod = "fallbackActivas")
    public ResponseEntity<List<SecurityAlert>> obtenerAlertasActivas() {
        return ResponseEntity.ok(alertService.obtenerAlertasActivas());
    }

    @GetMapping("/todas")
    @CircuitBreaker(name = "alerts", fallbackMethod = "fallbackTodas")
    public ResponseEntity<List<SecurityAlert>> obtenerTodasLasAlertas() {
        return ResponseEntity.ok(alertService.obtenerTodasLasAlertas());
    }

    @GetMapping("/gravedad/{gravedad}")
    @CircuitBreaker(name = "alerts", fallbackMethod = "fallbackGravedad")
    public ResponseEntity<List<SecurityAlert>> obtenerPorGravedad(@PathVariable String gravedad) {
        return ResponseEntity.ok(alertService.obtenerPorGravedad(gravedad));
    }

    @GetMapping("/zona/{zona}")
    @CircuitBreaker(name = "alerts", fallbackMethod = "fallbackZona")
    public ResponseEntity<List<SecurityAlert>> obtenerPorZona(@PathVariable String zona) {
        return ResponseEntity.ok(alertService.obtenerPorZona(zona));
    }

    @PostMapping("/crear")
    @CircuitBreaker(name = "alerts", fallbackMethod = "fallbackCrear")
    public ResponseEntity<Map<String, Object>> crearAlerta(
            @RequestParam String tipo,
            @RequestParam String gravedad,
            @RequestParam String ubicacion,
            @RequestParam String zona,
            @RequestParam String descripcion) {

        SecurityAlert alert = alertService.crearAlerta(tipo, gravedad, ubicacion, zona, descripcion);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "🚨 Alerta creada y notificaciones enviadas");
        response.put("alerta", alert);
        response.put("ciudadanosNotificados", alert.getCiudadanosAfectados());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/estado")
    @CircuitBreaker(name = "alerts", fallbackMethod = "fallbackActualizar")
    public ResponseEntity<Map<String, Object>> actualizarEstado(
            @PathVariable String id,
            @RequestParam String estado) {

        SecurityAlert alert = alertService.actualizarEstado(id, estado);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "✅ Estado de alerta actualizado");
        response.put("alerta", alert);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/notificaciones")
    public ResponseEntity<Map<String, Object>> obtenerNotificaciones() {
        Map<String, Object> response = new HashMap<>();
        response.put("notificaciones", alertService.obtenerNotificaciones());
        response.put("total", alertService.obtenerNotificaciones().size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(alertService.obtenerEstadisticas());
    }

    @DeleteMapping("/limpiar")
    public ResponseEntity<Map<String, String>> limpiarAlertasAntiguas() {
        alertService.limpiarAlertasAntiguas();
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "🧹 Alertas antiguas eliminadas");
        return ResponseEntity.ok(response);
    }

    // ========== FALLBACK METHODS ==========

    public ResponseEntity<List<SecurityAlert>> fallbackActivas(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<List<SecurityAlert>> fallbackTodas(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<List<SecurityAlert>> fallbackGravedad(String gravedad, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<List<SecurityAlert>> fallbackZona(String zona, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<Map<String, Object>> fallbackCrear(String tipo, String gravedad,
                                                             String ubicacion, String zona,
                                                             String descripcion, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo crear la alerta. Sistema de notificaciones caído.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<Map<String, Object>> fallbackActualizar(String id, String estado, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo actualizar el estado de la alerta");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}