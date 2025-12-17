package com.wakanda.health;

import com.wakanda.health.dispositivos_monitoreo.DeviceMonitoringService;
import com.wakanda.health.telemedicina.TelemedicineService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private DeviceMonitoringService deviceService;

    @Autowired
    private TelemedicineService telemedicineService;

    @GetMapping("/estado")
    @CircuitBreaker(name = "health", fallbackMethod = "fallbackEstado")
    public ResponseEntity<Map<String, Object>> obtenerEstadoGeneral() {
        deviceService.simularActualizaciones();

        Map<String, Object> response = new HashMap<>();
        response.put("dispositivos", deviceService.obtenerEstadisticas());
        response.put("telemedicina", telemedicineService.obtenerEstadisticas());
        response.put("mensaje", "🏥 Sistema de Salud operativo");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> obtenerInformacion() {
        Map<String, Object> info = new HashMap<>();
        info.put("servicio", "Sistema de Salud y Bienestar de Wakanda");
        info.put("version", "1.0.0");
        info.put("modulos", Map.of(
                "dispositivos", "Monitoreo de Salud con Dispositivos IoT",
                "telemedicina", "Consultas Médicas a Distancia"
        ));
        info.put("endpoints", Map.of(
                "devices", "/health/devices",
                "telemedicina", "/health/telemedicina"
        ));
        return ResponseEntity.ok(info);
    }

    @GetMapping("/guia")
    public ResponseEntity<Map<String, Object>> obtenerGuiaCompleta() {
        Map<String, Object> guia = new HashMap<>();

        guia.put("titulo", "🏥 GUÍA COMPLETA - SISTEMA DE SALUD DE WAKANDA");
        guia.put("version", "1.0.0");
        guia.put("puerto", "8084");
        guia.put("baseUrl", "http://localhost:8084");

        guia.put("descripcion", Map.of(
                "resumen", "Plataforma integral de salud con monitoreo IoT y telemedicina",
                "componentes", Arrays.asList(
                        "800 Dispositivos de monitoreo conectados",
                        "12 Médicos especializados disponibles",
                        "Telemedicina por video, chat y teléfono",
                        "Alertas automáticas de salud en tiempo real"
                )
        ));

        guia.put("dispositivos", crearSeccionDispositivos());
        guia.put("telemedicina", crearSeccionTelemedicina());
        guia.put("escenarios", crearEscenarios());

        guia.put("recursos", Map.of(
                "tiposDispositivos", Arrays.asList("SMARTWATCH", "PULSERA", "MONITOR_CARDIACO", "GLUCOMETRO", "OXIMETRO"),
                "especialidades", Arrays.asList("Medicina General", "Cardiología", "Pediatría", "Dermatología", "Psicología", "Endocrinología", "Traumatología"),
                "tiposConsulta", Arrays.asList("VIDEO", "CHAT", "TELEFONO"),
                "totalDispositivos", 800,
                "totalMedicos", 12
        ));

        return ResponseEntity.ok(guia);
    }

    private Map<String, Object> crearSeccionDispositivos() {
        return Map.of(
                "descripcion", "800 dispositivos IoT monitoreando salud en tiempo real",
                "endpoints", Arrays.asList(
                        Map.of(
                                "metodo", "GET",
                                "url", "/health/devices/estado",
                                "descripcion", "Estado de todos los dispositivos",
                                "ejemplo", "GET http://localhost:8084/health/devices/estado"
                        ),
                        Map.of(
                                "metodo", "GET",
                                "url", "/health/devices/alertas",
                                "descripcion", "Dispositivos con alertas médicas activas",
                                "ejemplo", "GET http://localhost:8084/health/devices/alertas"
                        ),
                        Map.of(
                                "metodo", "GET",
                                "url", "/health/devices/{id}",
                                "descripcion", "Obtener dispositivo específico",
                                "ejemplo", "GET http://localhost:8084/health/devices/SMARTWATCH-0001"
                        ),
                        Map.of(
                                "metodo", "PUT",
                                "url", "/health/devices/{id}/signos",
                                "descripcion", "Actualizar signos vitales",
                                "ejemplo", "PUT http://localhost:8084/health/devices/SMARTWATCH-0001/signos",
                                "body", Map.of(
                                        "frecuenciaCardiaca", 75,
                                        "presionSistolica", 120,
                                        "presionDiastolica", 80,
                                        "saturacionOxigeno", 98,
                                        "temperatura", 36.6
                                )
                        ),
                        Map.of(
                                "metodo", "POST",
                                "url", "/health/devices/{id}/simular-anomalia",
                                "descripcion", "Simular anomalía médica",
                                "tipos", Arrays.asList("TAQUICARDIA", "BRADICARDIA", "HIPERTENSION", "HIPOXIA", "FIEBRE", "HIPOGLUCEMIA", "HIPERGLUCEMIA"),
                                "ejemplo", "POST http://localhost:8084/health/devices/SMARTWATCH-0001/simular-anomalia?tipo=TAQUICARDIA"
                        )
                )
        );
    }

    private Map<String, Object> crearSeccionTelemedicina() {
        return Map.of(
                "descripcion", "12 médicos especializados disponibles para consultas remotas",
                "endpoints", Arrays.asList(
                        Map.of(
                                "metodo", "GET",
                                "url", "/health/telemedicina/medicos/disponibles",
                                "descripcion", "Ver médicos disponibles",
                                "ejemplo", "GET http://localhost:8084/health/telemedicina/medicos/disponibles"
                        ),
                        Map.of(
                                "metodo", "GET",
                                "url", "/health/telemedicina/medicos/especialidad/{especialidad}",
                                "descripcion", "Buscar por especialidad",
                                "ejemplo", "GET http://localhost:8084/health/telemedicina/medicos/especialidad/Cardiología"
                        ),
                        Map.of(
                                "metodo", "POST",
                                "url", "/health/telemedicina/consultas/agendar",
                                "descripcion", "Agendar nueva consulta",
                                "parametros", Map.of(
                                        "pacienteId", "ID del paciente",
                                        "nombrePaciente", "Nombre completo",
                                        "medicoId", "ID del médico",
                                        "tipoConsulta", "VIDEO, CHAT o TELEFONO",
                                        "motivoConsulta", "Descripción del motivo",
                                        "fechaHora", "2024-12-20T10:00:00"
                                ),
                                "ejemplo", "POST http://localhost:8084/health/telemedicina/consultas/agendar?pacienteId=PAC-00001&nombrePaciente=Juan%20Pérez&medicoId=MED-001&tipoConsulta=VIDEO&motivoConsulta=Consulta%20general&fechaHora=2024-12-20T10:00:00"
                        ),
                        Map.of(
                                "metodo", "POST",
                                "url", "/health/telemedicina/consultas/{id}/iniciar",
                                "descripcion", "Iniciar consulta agendada",
                                "ejemplo", "POST http://localhost:8084/health/telemedicina/consultas/{consultaId}/iniciar"
                        ),
                        Map.of(
                                "metodo", "POST",
                                "url", "/health/telemedicina/consultas/{id}/finalizar",
                                "descripcion", "Finalizar consulta con diagnóstico",
                                "ejemplo", "POST http://localhost:8084/health/telemedicina/consultas/{consultaId}/finalizar?diagnostico=Gripe%20común&tratamiento=Reposo%20y%20antivirales"
                        ),
                        Map.of(
                                "metodo", "GET",
                                "url", "/health/telemedicina/consultas/hoy",
                                "descripcion", "Ver consultas programadas hoy",
                                "ejemplo", "GET http://localhost:8084/health/telemedicina/consultas/hoy"
                        )
                )
        );
    }

    private Map<String, Object> crearEscenarios() {
        return Map.of(
                "escenario1", Map.of(
                        "nombre", "💓 Monitoreo de Signos Vitales",
                        "pasos", Arrays.asList(
                                "1. GET /health/devices/activos",
                                "2. GET /health/devices/SMARTWATCH-0001",
                                "3. PUT /health/devices/SMARTWATCH-0001/signos (actualizar)",
                                "4. GET /health/devices/alertas (verificar alertas)"
                        )
                ),
                "escenario2", Map.of(
                        "nombre", "🚨 Detección de Anomalía Cardíaca",
                        "pasos", Arrays.asList(
                                "1. POST /health/devices/MONITOR_CARDIACO-0001/simular-anomalia?tipo=TAQUICARDIA",
                                "2. GET /health/devices/alertas",
                                "3. GET /health/telemedicina/medicos/especialidad/Cardiología",
                                "4. POST /health/telemedicina/consultas/agendar (urgente)",
                                "5. POST /health/telemedicina/consultas/{id}/iniciar"
                        )
                ),
                "escenario3", Map.of(
                        "nombre", "🩺 Consulta de Telemedicina Completa",
                        "pasos", Arrays.asList(
                                "1. GET /health/telemedicina/medicos/disponibles",
                                "2. POST /health/telemedicina/consultas/agendar",
                                "3. GET /health/telemedicina/consultas/hoy",
                                "4. POST /health/telemedicina/consultas/{id}/iniciar",
                                "5. POST /health/telemedicina/consultas/{id}/finalizar"
                        )
                )
        );
    }

    // ========== FALLBACK METHODS ==========

    public ResponseEntity<Map<String, Object>> fallbackEstado(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Sistema de salud temporalmente no disponible");
        response.put("mensaje", "⚠️ Modo de respaldo activado");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}