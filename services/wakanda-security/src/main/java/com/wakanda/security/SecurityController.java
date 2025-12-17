package com.wakanda.security;

import com.wakanda.security.dto.SecurityResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @GetMapping("/estado")
    @CircuitBreaker(name = "security", fallbackMethod = "fallbackEstado")
    public ResponseEntity<SecurityResponse> obtenerEstadoGeneral() {
        securityService.actualizarSistemas();
        return ResponseEntity.ok(securityService.obtenerEstadoGeneral());
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> obtenerInformacion() {
        Map<String, Object> info = new HashMap<>();
        info.put("servicio", "Sistema de Seguridad y Vigilancia de Wakanda");
        info.put("version", "1.0.0");
        info.put("modulos", Map.of(
                "camaras", "Cámaras de Seguridad Inteligentes con IA",
                "drones", "Flota de Drones de Patrulla",
                "alertas", "Sistema de Alertas en Tiempo Real"
        ));
        info.put("endpoints", Map.of(
                "cameras", "/security/cameras",
                "drones", "/security/drones",
                "alerts", "/security/alerts"
        ));
        return ResponseEntity.ok(info);
    }

    @GetMapping("/guia")
    public ResponseEntity<Map<String, Object>> obtenerGuiaCompleta() {
        Map<String, Object> guia = new HashMap<>();

        // Información General
        guia.put("titulo", "🔒 GUÍA COMPLETA - SISTEMA DE SEGURIDAD DE WAKANDA");
        guia.put("version", "1.0.0");
        guia.put("puerto", "8083");
        guia.put("baseUrl", "http://localhost:8083");

        // Descripción
        guia.put("descripcion", Map.of(
                "resumen", "Sistema inteligente de seguridad y vigilancia con IA",
                "componentes", Arrays.asList(
                        "220 Cámaras con detección inteligente",
                        "29 Drones de patrulla autónoma",
                        "Sistema de alertas en tiempo real",
                        "Notificaciones automáticas a ciudadanos"
                )
        ));

        // MÓDULO 1: CÁMARAS
        guia.put("camaras", crearSeccionCamaras());

        // MÓDULO 2: DRONES
        guia.put("drones", crearSeccionDrones());

        // MÓDULO 3: ALERTAS
        guia.put("alertas", crearSeccionAlertas());

        // FLUJOS DE TRABAJO
        guia.put("escenarios", crearEscenarios());

        // RECURSOS
        guia.put("recursos", Map.of(
                "zonas", Arrays.asList("CENTRO", "TECH", "RESIDENCIAL", "CRITICO"),
                "totalCamaras", 220,
                "totalDrones", 29,
                "monitoreo", Arrays.asList(
                        "GET /actuator/health",
                        "GET /actuator/metrics",
                        "GET /actuator/circuitbreakers"
                )
        ));

        return ResponseEntity.ok(guia);
    }

    private Map<String, Object> crearSeccionCamaras() {
        Map<String, Object> camaras = new HashMap<>();

        camaras.put("descripcion", "Sistema de 220 cámaras con IA distribuidas en 4 zonas");
        camaras.put("zonas", Map.of(
                "CENTRO", "47 cámaras - Plaza Central, Av. Vibranium, Mercado",
                "TECH", "43 cámaras - Campus Wakanda, Parque Innovación",
                "RESIDENCIAL", "50 cámaras - Barrio Dorado, Zona Norte",
                "CRITICO", "80 cámaras - Aeropuerto, Estación, Hospital"
        ));

        camaras.put("endpoints", Arrays.asList(
                Map.of(
                        "metodo", "GET",
                        "url", "/security/cameras/estado",
                        "descripcion", "Ver todas las cámaras y estadísticas",
                        "ejemplo", "GET http://localhost:8083/security/cameras/estado"
                ),
                Map.of(
                        "metodo", "GET",
                        "url", "/security/cameras/zona/{zona}",
                        "descripcion", "Filtrar cámaras por zona",
                        "ejemplo", "GET http://localhost:8083/security/cameras/zona/CENTRO",
                        "zonas", Arrays.asList("CENTRO", "TECH", "RESIDENCIAL", "CRITICO")
                ),
                Map.of(
                        "metodo", "GET",
                        "url", "/security/cameras/alertas",
                        "descripcion", "Solo cámaras con alertas activas (nivel > 3)",
                        "ejemplo", "GET http://localhost:8083/security/cameras/alertas"
                ),
                Map.of(
                        "metodo", "GET",
                        "url", "/security/cameras/{id}",
                        "descripcion", "Obtener cámara específica",
                        "ejemplo", "GET http://localhost:8083/security/cameras/CAM-CENTRO-001"
                ),
                Map.of(
                        "metodo", "POST",
                        "url", "/security/cameras/{id}/detectar",
                        "descripcion", "Activar detección de IA",
                        "parametros", Map.of("tipoDeteccion", Arrays.asList("NORMAL", "AGLOMERACION", "SOSPECHOSO", "ROBO", "ACCIDENTE", "INCENDIO")),
                        "ejemplo", "POST http://localhost:8083/security/cameras/CAM-CENTRO-001/detectar?tipoDeteccion=ROBO"
                ),
                Map.of(
                        "metodo", "POST",
                        "url", "/security/cameras/{id}/reiniciar",
                        "descripcion", "Reiniciar cámara (limpia alertas)",
                        "ejemplo", "POST http://localhost:8083/security/cameras/CAM-TECH-050/reiniciar"
                ),
                Map.of(
                        "metodo", "GET",
                        "url", "/security/cameras/estadisticas",
                        "descripcion", "Estadísticas generales de cámaras",
                        "ejemplo", "GET http://localhost:8083/security/cameras/estadisticas"
                )
        ));

        return camaras;
    }

    private Map<String, Object> crearSeccionDrones() {
        Map<String, Object> drones = new HashMap<>();

        drones.put("descripcion", "Flota de 29 drones autónomos para patrulla y respuesta rápida");
        drones.put("tipos", Map.of(
                "PATROL", "10 drones - Patrulla general (DJI-Wakanda-X1)",
                "RAPID", "5 drones - Respuesta rápida (Falcon-Response)",
                "NIGHT", "8 drones - Vigilancia nocturna (NightHawk-Pro)",
                "REMOTE", "6 drones - Zonas remotas (LongRange-Scout)"
        ));

        drones.put("endpoints", Arrays.asList(
                Map.of(
                        "metodo", "GET",
                        "url", "/security/drones/estado",
                        "descripcion", "Ver todos los drones con batería y ubicación",
                        "ejemplo", "GET http://localhost:8083/security/drones/estado"
                ),
                Map.of(
                        "metodo", "GET",
                        "url", "/security/drones/disponibles",
                        "descripcion", "Solo drones con batería >30% listos para misión",
                        "ejemplo", "GET http://localhost:8083/security/drones/disponibles"
                ),
                Map.of(
                        "metodo", "GET",
                        "url", "/security/drones/{id}",
                        "descripcion", "Obtener dron específico",
                        "ejemplo", "GET http://localhost:8083/security/drones/DRONE-PATROL-01"
                ),
                Map.of(
                        "metodo", "POST",
                        "url", "/security/drones/{id}/desplegar",
                        "descripcion", "Desplegar dron a misión específica",
                        "parametros", Map.of(
                                "mision", "Descripción de la misión",
                                "ubicacion", "Lugar de despliegue"
                        ),
                        "ejemplo", "POST http://localhost:8083/security/drones/DRONE-RAPID-01/desplegar?mision=Investigar%20Robo&ubicacion=Plaza%20Central"
                ),
                Map.of(
                        "metodo", "POST",
                        "url", "/security/drones/{id}/patrullar",
                        "descripcion", "Iniciar patrulla en zona asignada",
                        "parametros", Map.of("zona", Arrays.asList("GENERAL", "EMERGENCIA", "NOCTURNA", "REMOTA")),
                        "ejemplo", "POST http://localhost:8083/security/drones/DRONE-PATROL-05/patrullar?zona=CENTRO"
                ),
                Map.of(
                        "metodo", "POST",
                        "url", "/security/drones/{id}/regresar",
                        "descripcion", "Regresar dron a base (carga o standby)",
                        "ejemplo", "POST http://localhost:8083/security/drones/DRONE-NIGHT-03/regresar"
                ),
                Map.of(
                        "metodo", "GET",
                        "url", "/security/drones/estadisticas",
                        "descripcion", "Estadísticas de la flota",
                        "ejemplo", "GET http://localhost:8083/security/drones/estadisticas"
                )
        ));

        return drones;
    }

    private Map<String, Object> crearSeccionAlertas() {
        Map<String, Object> alertas = new HashMap<>();

        alertas.put("descripcion", "Sistema de alertas en tiempo real con notificaciones automáticas");

        alertas.put("endpoints", Arrays.asList(
                Map.of(
                        "metodo", "GET",
                        "url", "/security/alerts/activas",
                        "descripcion", "Solo alertas con estado ACTIVA",
                        "ejemplo", "GET http://localhost:8083/security/alerts/activas"
                ),
                Map.of(
                        "metodo", "GET",
                        "url", "/security/alerts/todas",
                        "descripcion", "Historial completo de alertas",
                        "ejemplo", "GET http://localhost:8083/security/alerts/todas"
                ),
                Map.of(
                        "metodo", "GET",
                        "url", "/security/alerts/gravedad/{gravedad}",
                        "descripcion", "Filtrar por nivel de gravedad",
                        "gravedades", Arrays.asList("BAJA", "MEDIA", "ALTA", "CRITICA"),
                        "ejemplo", "GET http://localhost:8083/security/alerts/gravedad/CRITICA"
                ),
                Map.of(
                        "metodo", "GET",
                        "url", "/security/alerts/zona/{zona}",
                        "descripcion", "Filtrar alertas por zona",
                        "ejemplo", "GET http://localhost:8083/security/alerts/zona/CENTRO"
                ),
                Map.of(
                        "metodo", "POST",
                        "url", "/security/alerts/crear",
                        "descripcion", "Crear nueva alerta (notifica automáticamente)",
                        "parametros", Map.of(
                                "tipo", Arrays.asList("ROBO", "INCENDIO", "ACCIDENTE", "AGLOMERACION", "EMERGENCIA", "DESASTRE"),
                                "gravedad", Arrays.asList("BAJA", "MEDIA", "ALTA", "CRITICA"),
                                "ubicacion", "Lugar exacto del incidente",
                                "zona", "Zona de la ciudad",
                                "descripcion", "Detalles del incidente"
                        ),
                        "ejemplo", "POST http://localhost:8083/security/alerts/crear?tipo=ROBO&gravedad=ALTA&ubicacion=Plaza%20Real&zona=CENTRO&descripcion=Robo%20en%20tienda"
                ),
                Map.of(
                        "metodo", "PUT",
                        "url", "/security/alerts/{id}/estado",
                        "descripcion", "Actualizar estado de una alerta",
                        "estados", Arrays.asList("ACTIVA", "EN_PROCESO", "RESUELTA", "FALSA_ALARMA"),
                        "ejemplo", "PUT http://localhost:8083/security/alerts/abc123/estado?estado=RESUELTA"
                ),
                Map.of(
                        "metodo", "GET",
                        "url", "/security/alerts/notificaciones",
                        "descripcion", "Ver notificaciones enviadas a ciudadanos",
                        "ejemplo", "GET http://localhost:8083/security/alerts/notificaciones"
                ),
                Map.of(
                        "metodo", "GET",
                        "url", "/security/alerts/estadisticas",
                        "descripcion", "Estadísticas de alertas por tipo y gravedad",
                        "ejemplo", "GET http://localhost:8083/security/alerts/estadisticas"
                ),
                Map.of(
                        "metodo", "DELETE",
                        "url", "/security/alerts/limpiar",
                        "descripcion", "Eliminar alertas resueltas con más de 24h",
                        "ejemplo", "DELETE http://localhost:8083/security/alerts/limpiar"
                )
        ));

        return alertas;
    }

    private Map<String, Object> crearEscenarios() {
        return Map.of(
                "escenario1", Map.of(
                        "nombre", "🚨 Detección de Robo",
                        "descripcion", "Flujo completo desde detección hasta resolución",
                        "pasos", Arrays.asList(
                                "1. GET /security/estado - Ver estado inicial",
                                "2. POST /security/cameras/CAM-CENTRO-020/detectar?tipoDeteccion=ROBO",
                                "3. GET /security/alerts/activas - Ver alerta generada",
                                "4. POST /security/drones/DRONE-RAPID-01/desplegar?mision=Investigar%20Robo&ubicacion=Plaza%20Central",
                                "5. PUT /security/alerts/{id}/estado?estado=EN_PROCESO",
                                "6. PUT /security/alerts/{id}/estado?estado=RESUELTA"
                        )
                ),
                "escenario2", Map.of(
                        "nombre", "🔥 Emergencia de Incendio",
                        "descripcion", "Activación de protocolo de emergencia completo",
                        "pasos", Arrays.asList(
                                "1. POST /security/alerts/crear?tipo=INCENDIO&gravedad=CRITICA&ubicacion=Edificio%20Tech&zona=TECH&descripcion=Incendio%20piso%207",
                                "2. POST /security/emergencia?tipo=INCENDIO&ubicacion=Edificio%20Tech&descripcion=Personas%20atrapadas",
                                "3. POST /security/drones/DRONE-RAPID-01/desplegar?mision=Emergencia%20Incendio&ubicacion=Edificio%20Tech",
                                "4. POST /security/drones/DRONE-RAPID-02/desplegar?mision=Evacuacion&ubicacion=Edificio%20Tech",
                                "5. GET /security/alerts/notificaciones - Ver ciudadanos notificados",
                                "6. PUT /security/alerts/{id}/estado?estado=RESUELTA"
                        )
                ),
                "escenario3", Map.of(
                        "nombre", "🌙 Patrulla Nocturna",
                        "descripcion", "Configurar vigilancia nocturna en múltiples zonas",
                        "pasos", Arrays.asList(
                                "1. GET /security/drones/disponibles",
                                "2. POST /security/drones/DRONE-NIGHT-01/patrullar?zona=NOCTURNA",
                                "3. POST /security/drones/DRONE-NIGHT-02/patrullar?zona=NOCTURNA",
                                "4. POST /security/drones/DRONE-NIGHT-03/patrullar?zona=NOCTURNA",
                                "5. GET /security/drones/estado - Monitorear patrullas",
                                "6. POST /security/drones/DRONE-NIGHT-01/regresar - Al finalizar turno"
                        )
                ),
                "escenario4", Map.of(
                        "nombre", "📊 Monitoreo de Zona Específica",
                        "descripcion", "Vigilancia intensiva de zona de alta densidad",
                        "pasos", Arrays.asList(
                                "1. GET /security/cameras/zona/CENTRO - Ver cámaras disponibles",
                                "2. GET /security/cameras/alertas - Revisar detecciones activas",
                                "3. POST /security/drones/DRONE-PATROL-05/patrullar?zona=CENTRO",
                                "4. GET /security/alerts/zona/CENTRO - Ver alertas de la zona",
                                "5. GET /security/estado - Dashboard general"
                        )
                )
        );
    }

    @PostMapping("/emergencia")
    @CircuitBreaker(name = "security", fallbackMethod = "fallbackEmergencia")
    public ResponseEntity<Map<String, Object>> activarEmergencia(
            @RequestParam String tipo,
            @RequestParam String ubicacion,
            @RequestParam String descripcion) {

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "🚨 PROTOCOLO DE EMERGENCIA ACTIVADO");
        response.put("tipo", tipo);
        response.put("ubicacion", ubicacion);
        response.put("acciones", Map.of(
                "alertas", "Notificaciones enviadas a ciudadanos",
                "drones", "Drones desplegados a la zona",
                "camaras", "Cámaras enfocadas en el área",
                "emergencias", "Servicios de emergencia notificados"
        ));

        return ResponseEntity.ok(response);
    }

    // ========== FALLBACK METHODS ==========

    public ResponseEntity<SecurityResponse> fallbackEstado(Exception ex) {
        SecurityResponse response = new SecurityResponse();
        response.setMensaje("⚠️ Sistema de seguridad en modo de respaldo");
        response.setNivelSeguridad("DESCONOCIDO");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<Map<String, Object>> fallbackEmergencia(String tipo, String ubicacion,
                                                                  String descripcion, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo procesar la emergencia automáticamente");
        response.put("mensaje", "⚠️ Active protocolo manual de emergencia");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
