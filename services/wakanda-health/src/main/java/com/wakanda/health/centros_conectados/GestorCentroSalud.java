package com.wakanda.health.centros_conectados;

import org.springframework.stereotype.Service;

@Service
public class GestorCentroSalud {

    // Simula la "Optimización de citas y tratamientos"
    public String gestionarCita(ExpedientePaciente paciente) {
        if (paciente.isEsUrgente()) {
            paciente.setTratamientoActual("Ingreso Inmediato en Box de Vitales");
            return "🚨 PRIORIDAD ALTA: El paciente " + paciente.getNombre() + " pasa directamente a consulta. " +
                    "Sala asignada: URG-01. Protocolo de atención rápida activado.";
        } else {
            paciente.setTratamientoActual("Cita programada con especialista");
            return "✅ Cita Optimizada: " + paciente.getNombre() + ", su espera estimada es de 15 minutos. " +
                    "Puede esperar en la sala digital o monitorear su turno desde la App.";
        }
    }

    // Simula el "Acceso a datos de pacientes"
    public String consultarHistorial(String id) {
        // Aquí conectaríamos con una base de datos real
        return "📄 Historial Digital (" + id + "): Vacunación completa, alergia a la penicilina. " +
                "Última visita: Hace 3 meses (Chequeo general).";
    }
}