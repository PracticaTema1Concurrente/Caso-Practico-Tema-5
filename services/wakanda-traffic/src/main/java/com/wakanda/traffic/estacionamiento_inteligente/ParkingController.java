package com.wakanda.traffic.estacionamiento_inteligente;

import com.wakanda.traffic.estacionamiento_inteligente.ParkingResponse;
import com.wakanda.traffic.estacionamiento_inteligente.ParkingSpot;
import com.wakanda.traffic.estacionamiento_inteligente.ParkingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/traffic/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @GetMapping("/estado")
    @CircuitBreaker(name = "parking", fallbackMethod = "fallbackEstado")
    public ResponseEntity<ParkingResponse> obtenerEstado() {
        parkingService.simularSensores(); // Simula actualización de sensores
        return ResponseEntity.ok(parkingService.obtenerEstadoGeneral());
    }

    @GetMapping("/zona/{zona}")
    @CircuitBreaker(name = "parking", fallbackMethod = "fallbackZona")
    public ResponseEntity<ParkingResponse> buscarPorZona(@PathVariable String zona) {
        return ResponseEntity.ok(parkingService.buscarPorZona(zona));
    }

    @GetMapping("/tipo/{tipo}")
    @CircuitBreaker(name = "parking", fallbackMethod = "fallbackTipo")
    public ResponseEntity<ParkingResponse> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(parkingService.buscarPorTipo(tipo));
    }

    @GetMapping("/buscar")
    @CircuitBreaker(name = "parking", fallbackMethod = "fallbackBuscar")
    public ResponseEntity<ParkingSpot> buscarMasCercano(
            @RequestParam String zona,
            @RequestParam String tipo) {
        return ResponseEntity.ok(parkingService.buscarMasCercano(zona, tipo));
    }

    @PostMapping("/ocupar/{id}")
    @CircuitBreaker(name = "parking", fallbackMethod = "fallbackOcupar")
    public ResponseEntity<Map<String, Object>> ocuparEspacio(@PathVariable String id) {
        ParkingSpot spot = parkingService.ocuparEspacio(id);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "🅿️ Espacio ocupado exitosamente");
        response.put("espacio", spot);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/liberar/{id}")
    @CircuitBreaker(name = "parking", fallbackMethod = "fallbackLiberar")
    public ResponseEntity<Map<String, Object>> liberarEspacio(@PathVariable String id) {
        ParkingSpot spot = parkingService.liberarEspacio(id);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "✅ Espacio liberado exitosamente");
        response.put("espacio", spot);
        return ResponseEntity.ok(response);
    }

    // ========== FALLBACK METHODS ==========

    public ResponseEntity<ParkingResponse> fallbackEstado(Exception ex) {
        ParkingResponse response = new ParkingResponse();
        response.setMensaje("⚠️ Sistema de sensores temporalmente no disponible. Modo emergencia activado.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<ParkingResponse> fallbackZona(String zona, Exception ex) {
        ParkingResponse response = new ParkingResponse();
        response.setMensaje("⚠️ No se puede consultar zona: " + zona + ". Intente más tarde.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<ParkingResponse> fallbackTipo(String tipo, Exception ex) {
        ParkingResponse response = new ParkingResponse();
        response.setMensaje("⚠️ No se puede consultar tipo: " + tipo + ". Intente más tarde.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<ParkingSpot> fallbackBuscar(String zona, String tipo, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<Map<String, Object>> fallbackOcupar(String id, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo ocupar el espacio. Sistema no disponible.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<Map<String, Object>> fallbackLiberar(String id, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo liberar el espacio. Sistema no disponible.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
