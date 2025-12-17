package com.wakanda.health.telemedicina;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/health/telemedicina")
public class TelemedicineController {

    @Autowired
    private TelemedicineService telemedicineService;

    @GetMapping("/medicos")
    @CircuitBreaker(name = "telemedicina", fallbackMethod = "fallbackMedicos")
    public ResponseEntity<List<Doctor>> obtenerMedicos() {
        return ResponseEntity.ok(telemedicineService.obtenerTodosLosMedicos());
    }

    @GetMapping("/medicos/disponibles")
    @CircuitBreaker(name = "telemedicina", fallbackMethod = "fallbackDisponibles")
    public ResponseEntity<List<Doctor>> obtenerMedicosDisponibles() {
        return ResponseEntity.ok(telemedicineService.obtenerMedicosDisponibles());
    }

    @GetMapping("/medicos/especialidad/{especialidad}")
    @CircuitBreaker(name = "telemedicina", fallbackMethod = "fallbackEspecialidad")
    public ResponseEntity<List<Doctor>> obtenerPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(telemedicineService.obtenerPorEspecialidad(especialidad));
    }

    @GetMapping("/medicos/{id}")
    @CircuitBreaker(name = "telemedicina", fallbackMethod = "fallbackMedico")
    public ResponseEntity<Doctor> obtenerMedico(@PathVariable String id) {
        return ResponseEntity.ok(telemedicineService.obtenerMedico(id));
    }

    @PostMapping("/consultas/agendar")
    @CircuitBreaker(name = "consultas", fallbackMethod = "fallbackAgendar")
    public ResponseEntity<Map<String, Object>> agendarConsulta(
            @RequestParam String pacienteId,
            @RequestParam String nombrePaciente,
            @RequestParam String medicoId,
            @RequestParam String tipoConsulta,
            @RequestParam String motivoConsulta,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora) {

        MedicalConsultation consulta = telemedicineService.agendarConsulta(
                pacienteId, nombrePaciente, medicoId, tipoConsulta, motivoConsulta, fechaHora
        );

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "📅 Consulta agendada exitosamente");
        response.put("consulta", consulta);
        response.put("instrucciones", Map.of(
                "VIDEO", "Conéctese 5 minutos antes usando el enlace que recibirá por email",
                "CHAT", "Acceda al chat desde su perfil en la app",
                "TELEFONO", "El médico le llamará a la hora programada"
        ).get(tipoConsulta));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/consultas/{id}/iniciar")
    @CircuitBreaker(name = "consultas", fallbackMethod = "fallbackIniciar")
    public ResponseEntity<Map<String, Object>> iniciarConsulta(@PathVariable String id) {
        MedicalConsultation consulta = telemedicineService.iniciarConsulta(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "🩺 Consulta iniciada");
        response.put("consulta", consulta);
        response.put("sala", "SALA-" + id.substring(0, 8));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/consultas/{id}/finalizar")
    @CircuitBreaker(name = "consultas", fallbackMethod = "fallbackFinalizar")
    public ResponseEntity<Map<String, Object>> finalizarConsulta(
            @PathVariable String id,
            @RequestParam String diagnostico,
            @RequestParam String tratamiento,
            @RequestParam(required = false) List<String> recetas) {

        MedicalConsultation consulta = telemedicineService.finalizarConsulta(
                id, diagnostico, tratamiento, recetas
        );

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "✅ Consulta finalizada");
        response.put("consulta", consulta);
        response.put("duracion", consulta.getDuracionMinutos() + " minutos");

        if (recetas != null && !recetas.isEmpty()) {
            response.put("recetas", "📋 " + recetas.size() + " receta(s) generada(s)");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/consultas/{id}")
    @CircuitBreaker(name = "consultas", fallbackMethod = "fallbackConsulta")
    public ResponseEntity<MedicalConsultation> obtenerConsulta(@PathVariable String id) {
        return ResponseEntity.ok(telemedicineService.obtenerConsulta(id));
    }

    @GetMapping("/consultas/paciente/{pacienteId}")
    @CircuitBreaker(name = "consultas", fallbackMethod = "fallbackConsultasPaciente")
    public ResponseEntity<List<MedicalConsultation>> obtenerConsultasPaciente(@PathVariable String pacienteId) {
        return ResponseEntity.ok(telemedicineService.obtenerConsultasPaciente(pacienteId));
    }

    @GetMapping("/consultas/medico/{medicoId}")
    @CircuitBreaker(name = "consultas", fallbackMethod = "fallbackConsultasMedico")
    public ResponseEntity<List<MedicalConsultation>> obtenerConsultasMedico(@PathVariable String medicoId) {
        return ResponseEntity.ok(telemedicineService.obtenerConsultasMedico(medicoId));
    }

    @GetMapping("/consultas/hoy")
    @CircuitBreaker(name = "consultas", fallbackMethod = "fallbackConsultasHoy")
    public ResponseEntity<List<MedicalConsultation>> obtenerConsultasHoy() {
        return ResponseEntity.ok(telemedicineService.obtenerConsultasHoy());
    }

    @DeleteMapping("/consultas/{id}/cancelar")
    @CircuitBreaker(name = "consultas", fallbackMethod = "fallbackCancelar")
    public ResponseEntity<Map<String, Object>> cancelarConsulta(
            @PathVariable String id,
            @RequestParam String motivo) {

        MedicalConsultation consulta = telemedicineService.cancelarConsulta(id, motivo);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "❌ Consulta cancelada");
        response.put("consulta", consulta);
        response.put("motivo", motivo);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(telemedicineService.obtenerEstadisticas());
    }

    // ========== FALLBACK METHODS ==========

    public ResponseEntity<List<Doctor>> fallbackMedicos(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<List<Doctor>> fallbackDisponibles(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<List<Doctor>> fallbackEspecialidad(String especialidad, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<Doctor> fallbackMedico(String id, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<Map<String, Object>> fallbackAgendar(String pacienteId, String nombrePaciente,
                                                               String medicoId, String tipoConsulta,
                                                               String motivoConsulta, LocalDateTime fechaHora,
                                                               Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo agendar la consulta. Intente más tarde.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<Map<String, Object>> fallbackIniciar(String id, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo iniciar la consulta");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<Map<String, Object>> fallbackFinalizar(String id, String diagnostico,
                                                                 String tratamiento, List<String> recetas,
                                                                 Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo finalizar la consulta");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    public ResponseEntity<MedicalConsultation> fallbackConsulta(String id, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<List<MedicalConsultation>> fallbackConsultasPaciente(String pacienteId, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<List<MedicalConsultation>> fallbackConsultasMedico(String medicoId, Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<List<MedicalConsultation>> fallbackConsultasHoy(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    public ResponseEntity<Map<String, Object>> fallbackCancelar(String id, String motivo, Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "No se pudo cancelar la consulta");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}