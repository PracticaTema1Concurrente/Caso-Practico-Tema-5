package com.wakanda.health.dispositivos_monitoreo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/health/devices")
public class DeviceMonitoringController {

    @Autowired
    private DeviceMonitoringService deviceService;

    @GetMapping("/estado")
    @CircuitBreaker(name = "dispositivos", fallbackMethod = "fallbackEstado")
    public ResponseEntity<Map<String, Object>> obtenerEstado() {
        deviceService.simularActualizaciones();
        Map<String, Object> response = new HashMap<>();
        response.put("dispositivos", deviceService.obtenerTodosLosDispositivos());
        response.put("estadisticas", deviceService.obtenerEstadisticas());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activos")
    @CircuitBreaker(name = "dispositivos", fallbackMethod = "fallbackActivos")
    public ResponseEntity<List<HealthDevice>> obtenerActivos() {
        return ResponseEntity.ok(deviceService.obtenerDispositivosActivos());
    }

    @GetMapping("/alertas")
    @CircuitBreaker(name = "dispositivos", fallbackMethod = "fallbackAlertas")
    public ResponseEntity<List<HealthDevice>> obtenerConAlertas() {
        return ResponseEntity.ok(deviceService.obtenerDispositivosConAlertas());
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "dispositivos", fallbackMethod = "fallbackDispositivo")
    public ResponseEntity<HealthDevice> obtenerDispositivo(@PathVariable String id) {
        return ResponseEntity.ok(deviceService.obtenerDispositivo(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    @CircuitBreaker(name = "dispositivos", fallbackMethod = "fallbackPaciente")
    public ResponseEntity<List<HealthDevice>> obtenerPorPaciente(@PathVariable String pacienteId) {
        return ResponseEntity.ok(deviceService.obtenerPorPaciente(pacienteId));
    }

    @PutMapping("/{id}/signos")
    @CircuitBreaker(name = "dispositivos", fallbackMethod = "fallbackActualizar")
    public ResponseEntity<Map<String, Object>> actualizarSignosVitales(
            @PathVariable String id,
            @RequestBody Map<String, Object> signos) {

        HealthDevice device = deviceService.actualizarSignosVitales(id, signos);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "✅ Signos vitales actualizados");
        response.put("dispositivo", device);
        response.put("nivelAlerta", device.getNivelAlerta());
        response.put("alertaMedica", device.isAlertaMedica());

        if (device.isAlertaMedica()) {
            response.put("atencion", "⚠️ Se detectaron anomalías. Se recomienda consulta médica.");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/simular-anomalia")
    @CircuitBreaker(name = "dispositivos", fallbackMethod = "fallbackSimular")
    public ResponseEntity<Map<String, Object>> simularAnomalia(
            @PathVariable String id,
            @RequestParam String tipo) {

        Map<String, Object> signosAnormales = new HashMap<>();

        switch (tipo.toUpperCase()) {
            case "TAQUICARDIA":
                signosAnormales.put("frecuenciaCardiaca", 130);
                break;
            case "BRADICARDIA":
                signosAnormales.put("frecuenciaCardiaca", 45);
                break;
            case "HIPERTENSION":
                signosAnormales.put("presionSistolica", 160);
                signosAnormales.put("presionDiastolica", 100);
                break;
            case "HIPOXIA":
                signosAnormales.put("saturacionOxigeno", 88);
                break;
            case "FIEBRE":
                signosAnormales.put("temperatura", 39.5);
                break;
            case "HIPOGLUCEMIA":
                signosAnormales.put("glucosa", 60.0);
                break;
            case "HIPERGLUCEMIA":
                signosAnormales.put("glucosa", 200.0);
                break;
            default:
                throw new RuntimeException("Tipo de anomalía no reconocido: " + tipo);
        }

        HealthDevice device = deviceService.actualizarSignosVitales(id, signosAnormales);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "🚨 Anomalía simulada: " + tipo);
        response.put("dispositivo", device);
        response.put("alertaGenerada", device.getMensajeAlerta());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(deviceService.obtenerEstadisticas());
    }

    // ========== FALLBACK METHODS ==========

    public ResponseEntity<Map<String, Object>> fallbackEstado(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Sistema de monitoreo temporalmente no disponible");
        response.put("mensaje", "⚠️ Verifique conexión con dispositivos");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<List<HealthDevice>> fallbackActivos(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<List<HealthDevice>> fallbackAlertas(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<HealthDevice> fallbackDispositivo(String id, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<List<HealthDevice>> fallbackPaciente(String pacienteId, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<Map<String, Object>> fallbackActualizar(String id, Map<String, Object> signos, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudieron actualizar los signos vitales");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<Map<String, Object>> fallbackSimular(String id, String tipo, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo simular la anomalía");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}