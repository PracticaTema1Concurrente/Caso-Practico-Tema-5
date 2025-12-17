
package com.wakanda.health.telemedicina;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TelemedicineService {

    private final Map<String, Doctor> doctors = new ConcurrentHashMap<>();
    private final Map<String, MedicalConsultation> consultations = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @PostConstruct
    public void inicializarSistema() {
        System.out.println("👨‍⚕️ Inicializando Sistema de Telemedicina...");

        inicializarMedicos();

        System.out.println("👨‍⚕️ Sistema de Telemedicina inicializado: " + doctors.size() + " médicos disponibles");
    }

    private void inicializarMedicos() {
        // Medicina General
        agregarDoctor("MED-001", "Dr. T'Challa Okoye", "Medicina General", "Hospital Central", 15);
        agregarDoctor("MED-002", "Dra. Nakia Udaku", "Medicina General", "Hospital Central", 12);
        agregarDoctor("MED-003", "Dr. Shuri Wakanda", "Medicina General", "Clínica Norte", 8);

        // Cardiología
        agregarDoctor("CARD-001", "Dr. M'Baku Heart", "Cardiología", "Hospital Central", 20);
        agregarDoctor("CARD-002", "Dra. Ramonda Pulse", "Cardiología", "Hospital Sur", 18);

        // Pediatría
        agregarDoctor("PED-001", "Dra. Okoye Kids", "Pediatría", "Hospital Infantil", 10);
        agregarDoctor("PED-002", "Dr. W'Kabi Child", "Pediatría", "Clínica Familiar", 14);

        // Dermatología
        agregarDoctor("DERM-001", "Dra. Ayo Skin", "Dermatología", "Centro Dermatológico", 9);

        // Psicología
        agregarDoctor("PSI-001", "Dr. N'Jobu Mind", "Psicología", "Centro Bienestar", 16);
        agregarDoctor("PSI-002", "Dra. Zuri Wellness", "Psicología", "Hospital Central", 11);

        // Endocrinología
        agregarDoctor("ENDO-001", "Dr. Erik Diabetes", "Endocrinología", "Hospital Sur", 13);

        // Traumatología
        agregarDoctor("TRAUMA-001", "Dr. Killmonger Bones", "Traumatología", "Hospital Central", 17);
    }

    private void agregarDoctor(String id, String nombre, String especialidad, String hospital, int experiencia) {
        Doctor doctor = new Doctor(id, nombre, especialidad, hospital, experiencia);
        doctor.setLicencia("LIC-WK-" + id);
        doctors.put(id, doctor);
    }

    public List<Doctor> obtenerTodosLosMedicos() {
        return new ArrayList<>(doctors.values());
    }

    public List<Doctor> obtenerMedicosDisponibles() {
        return doctors.values().stream()
                .filter(Doctor::isDisponible)
                .sorted(Comparator.comparingDouble(Doctor::getCalificacion).reversed())
                .collect(Collectors.toList());
    }

    public List<Doctor> obtenerPorEspecialidad(String especialidad) {
        return doctors.values().stream()
                .filter(d -> d.getEspecialidad().equalsIgnoreCase(especialidad))
                .filter(Doctor::isDisponible)
                .collect(Collectors.toList());
    }

    public Doctor obtenerMedico(String id) {
        Doctor doctor = doctors.get(id);
        if (doctor == null) {
            throw new RuntimeException("Médico no encontrado: " + id);
        }
        return doctor;
    }

    public MedicalConsultation agendarConsulta(String pacienteId, String nombrePaciente,
                                               String medicoId, String tipoConsulta,
                                               String motivoConsulta, LocalDateTime fechaHora) {
        Doctor doctor = obtenerMedico(medicoId);

        if (!doctor.isDisponible()) {
            throw new RuntimeException("Médico no disponible: " + doctor.getNombre());
        }

        MedicalConsultation consulta = new MedicalConsultation(
                pacienteId, nombrePaciente, medicoId, doctor.getNombre(),
                doctor.getEspecialidad(), fechaHora, tipoConsulta, motivoConsulta
        );

        consultations.put(consulta.getId(), consulta);
        doctor.setConsultasHoy(doctor.getConsultasHoy() + 1);

        System.out.println("📅 Consulta agendada: " + consulta.getId() + " - " + nombrePaciente
                + " con " + doctor.getNombre());

        return consulta;
    }

    public MedicalConsultation iniciarConsulta(String consultaId) {
        MedicalConsultation consulta = obtenerConsulta(consultaId);

        if (!"PROGRAMADA".equals(consulta.getEstado())) {
            throw new RuntimeException("La consulta no está en estado PROGRAMADA");
        }

        consulta.setEstado("EN_CURSO");
        return consulta;
    }

    public MedicalConsultation finalizarConsulta(String consultaId, String diagnostico,
                                                 String tratamiento, List<String> recetas) {
        MedicalConsultation consulta = obtenerConsulta(consultaId);

        if (!"EN_CURSO".equals(consulta.getEstado())) {
            throw new RuntimeException("La consulta no está en curso");
        }

        consulta.setEstado("COMPLETADA");
        consulta.setDiagnostico(diagnostico);
        consulta.setTratamiento(tratamiento);
        consulta.setRecetas(recetas != null ? recetas : new ArrayList<>());
        consulta.setDuracionMinutos((int)(Math.random() * 30 + 15)); // 15-45 min

        // Actualizar estadísticas del médico
        Doctor doctor = doctors.get(consulta.getMedicoId());
        if (doctor != null) {
            doctor.setConsultasTotales(doctor.getConsultasTotales() + 1);
        }

        return consulta;
    }

    public MedicalConsultation obtenerConsulta(String id) {
        MedicalConsultation consulta = consultations.get(id);
        if (consulta == null) {
            throw new RuntimeException("Consulta no encontrada: " + id);
        }
        return consulta;
    }

    public List<MedicalConsultation> obtenerConsultasPaciente(String pacienteId) {
        return consultations.values().stream()
                .filter(c -> c.getPacienteId().equals(pacienteId))
                .sorted(Comparator.comparing(MedicalConsultation::getFechaHora).reversed())
                .collect(Collectors.toList());
    }

    public List<MedicalConsultation> obtenerConsultasMedico(String medicoId) {
        return consultations.values().stream()
                .filter(c -> c.getMedicoId().equals(medicoId))
                .sorted(Comparator.comparing(MedicalConsultation::getFechaHora).reversed())
                .collect(Collectors.toList());
    }

    public List<MedicalConsultation> obtenerConsultasHoy() {
        LocalDateTime hoy = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime manana = hoy.plusDays(1);

        return consultations.values().stream()
                .filter(c -> c.getFechaHora().isAfter(hoy) && c.getFechaHora().isBefore(manana))
                .sorted(Comparator.comparing(MedicalConsultation::getFechaHora))
                .collect(Collectors.toList());
    }

    public MedicalConsultation cancelarConsulta(String consultaId, String motivo) {
        MedicalConsultation consulta = obtenerConsulta(consultaId);
        consulta.setEstado("CANCELADA");
        consulta.setNotasAdicionales("Cancelada: " + motivo);

        // Liberar al médico
        Doctor doctor = doctors.get(consulta.getMedicoId());
        if (doctor != null && doctor.getConsultasHoy() > 0) {
            doctor.setConsultasHoy(doctor.getConsultasHoy() - 1);
        }

        return consulta;
    }

    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();

        long disponibles = doctors.values().stream().filter(Doctor::isDisponible).count();
        long consultasHoy = consultations.values().stream()
                .filter(c -> c.getFechaHora().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .count();

        Map<String, Long> porEspecialidad = doctors.values().stream()
                .collect(Collectors.groupingBy(Doctor::getEspecialidad, Collectors.counting()));

        Map<String, Long> consultasPorEstado = consultations.values().stream()
                .collect(Collectors.groupingBy(MedicalConsultation::getEstado, Collectors.counting()));

        stats.put("totalMedicos", doctors.size());
        stats.put("medicosDisponibles", disponibles);
        stats.put("consultasHoy", consultasHoy);
        stats.put("totalConsultas", consultations.size());
        stats.put("especialidades", porEspecialidad);
        stats.put("consultasPorEstado", consultasPorEstado);

        return stats;
    }
}