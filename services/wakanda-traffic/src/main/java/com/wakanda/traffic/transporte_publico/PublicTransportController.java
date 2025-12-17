package com.wakanda.traffic.transporte_publico;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/traffic/transporte")
public class PublicTransportController {

    @Autowired
    private PublicTransportService transportService;

    @GetMapping("/estado")
    @CircuitBreaker(name = "transporte", fallbackMethod = "fallbackEstado")
    public ResponseEntity<TransportResponse> obtenerEstado() {
        transportService.simularMovimiento(); // Actualizar posiciones
        return ResponseEntity.ok(transportService.obtenerEstadoGeneral());
    }

    @GetMapping("/linea/{linea}")
    @CircuitBreaker(name = "transporte", fallbackMethod = "fallbackLinea")
    public ResponseEntity<TransportResponse> consultarLinea(@PathVariable String linea) {
        return ResponseEntity.ok(transportService.obtenerPorLinea(linea));
    }

    @GetMapping("/tipo/{tipo}")
    @CircuitBreaker(name = "transporte", fallbackMethod = "fallbackTipo")
    public ResponseEntity<TransportResponse> consultarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(transportService.obtenerPorTipo(tipo));
    }

    @GetMapping("/prediccion/{parada}")
    @CircuitBreaker(name = "transporte", fallbackMethod = "fallbackPrediccion")
    public ResponseEntity<List<ArrivalPrediction>> predecirLlegadas(@PathVariable String parada) {
        List<ArrivalPrediction> predicciones = transportService.predecirLlegadas(parada);
        return ResponseEntity.ok(predicciones);
    }

    @PostMapping("/desvio/{unidadId}")
    @CircuitBreaker(name = "transporte", fallbackMethod = "fallbackDesvio")
    public ResponseEntity<Map<String, Object>> activarDesvio(
            @PathVariable String unidadId,
            @RequestParam String motivo,
            @RequestParam String rutaAlternativa) {

        PublicTransport unidad = transportService.activarDesvio(unidadId, motivo, rutaAlternativa);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "⚠️ Desvío activado para " + unidadId);
        response.put("unidad", unidad);
        response.put("motivo", motivo);
        response.put("rutaAlternativa", rutaAlternativa);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/paradas/{linea}")
    public ResponseEntity<Map<String, Object>> obtenerParadas(@PathVariable String linea) {
        List<String> paradas = transportService.obtenerParadasDeLinea(linea);

        Map<String, Object> response = new HashMap<>();
        response.put("linea", linea);
        response.put("paradas", paradas);
        response.put("totalParadas", paradas.size());

        return ResponseEntity.ok(response);
    }

    // ========== FALLBACK METHODS ==========

    public ResponseEntity<TransportResponse> fallbackEstado(Exception ex) {
        TransportResponse response = new TransportResponse();
        response.setMensaje("⚠️ Sistema de monitoreo temporalmente no disponible");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<TransportResponse> fallbackLinea(String linea, Exception ex) {
        TransportResponse response = new TransportResponse();
        response.setMensaje("⚠️ No se puede consultar línea: " + linea);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<TransportResponse> fallbackTipo(String tipo, Exception ex) {
        TransportResponse response = new TransportResponse();
        response.setMensaje("⚠️ No se puede consultar tipo: " + tipo);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<List<ArrivalPrediction>> fallbackPrediccion(String parada, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<Map<String, Object>> fallbackDesvio(String unidadId, String motivo,
                                                              String rutaAlternativa, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo activar el desvío. Sistema no disponible.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}