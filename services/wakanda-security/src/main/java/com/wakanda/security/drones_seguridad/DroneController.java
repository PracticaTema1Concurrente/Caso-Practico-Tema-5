package com.wakanda.security.drones_seguridad;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/security/drones")
public class DroneController {

    @Autowired
    private DroneService droneService;

    @GetMapping("/estado")
    @CircuitBreaker(name = "drones", fallbackMethod = "fallbackEstado")
    public ResponseEntity<Map<String, Object>> obtenerEstado() {
        droneService.simularOperaciones();
        Map<String, Object> response = new HashMap<>();
        response.put("drones", droneService.obtenerTodosLosDrones());
        response.put("estadisticas", droneService.obtenerEstadisticas());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/disponibles")
    @CircuitBreaker(name = "drones", fallbackMethod = "fallbackDisponibles")
    public ResponseEntity<List<SecurityDrone>> obtenerDisponibles() {
        return ResponseEntity.ok(droneService.obtenerDronesDisponibles());
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "drones", fallbackMethod = "fallbackDrone")
    public ResponseEntity<SecurityDrone> obtenerDron(@PathVariable String id) {
        return ResponseEntity.ok(droneService.obtenerDron(id));
    }

    @PostMapping("/{id}/desplegar")
    @CircuitBreaker(name = "drones", fallbackMethod = "fallbackDesplegar")
    public ResponseEntity<Map<String, Object>> desplegarDron(
            @PathVariable String id,
            @RequestParam String mision,
            @RequestParam String ubicacion) {

        SecurityDrone drone = droneService.desplegarDron(id, mision, ubicacion);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "🚁 Dron desplegado para: " + mision);
        response.put("drone", drone);
        response.put("ubicacion", ubicacion);
        response.put("tiempoVuelo", drone.getTiempoVueloRestante() + " minutos");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/patrullar")
    @CircuitBreaker(name = "drones", fallbackMethod = "fallbackPatrullar")
    public ResponseEntity<Map<String, Object>> iniciarPatrulla(
            @PathVariable String id,
            @RequestParam String zona) {

        SecurityDrone drone = droneService.iniciarPatrulla(id, zona);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "🚁 Patrulla iniciada en zona: " + zona);
        response.put("drone", drone);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/regresar")
    @CircuitBreaker(name = "drones", fallbackMethod = "fallbackRegresar")
    public ResponseEntity<Map<String, Object>> regresarABase(@PathVariable String id) {
        SecurityDrone drone = droneService.regresarABase(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "🏠 Dron regresando a base");
        response.put("drone", drone);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(droneService.obtenerEstadisticas());
    }

    // ========== FALLBACK METHODS ==========

    public ResponseEntity<Map<String, Object>> fallbackEstado(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Sistema de drones temporalmente no disponible");
        response.put("mensaje", "⚠️ Operaciones en modo manual");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<List<SecurityDrone>> fallbackDisponibles(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<SecurityDrone> fallbackDrone(String id, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<Map<String, Object>> fallbackDesplegar(String id, String mision,
                                                                 String ubicacion, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo desplegar el dron. Intente más tarde.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<Map<String, Object>> fallbackPatrullar(String id, String zona, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo iniciar la patrulla");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<Map<String, Object>> fallbackRegresar(String id, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo ejecutar el comando de regreso");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}