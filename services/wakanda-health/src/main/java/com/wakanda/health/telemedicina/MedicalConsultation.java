package com.wakanda.health.telemedicina;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MedicalConsultation {
    private String id;
    private String pacienteId;
    private String nombrePaciente;
    private String medicoId;
    private String nombreMedico;
    private String especialidad;
    private LocalDateTime fechaHora;
    private String estado; // PROGRAMADA, EN_CURSO, COMPLETADA, CANCELADA
    private String tipoConsulta; // VIDEO, CHAT, TELEFONO
    private String motivoConsulta;
    private String diagnostico;
    private List<String> sintomas;
    private String tratamiento;
    private List<String> recetas;
    private String prioridad; // BAJA, MEDIA, ALTA, URGENTE
    private Integer duracionMinutos;
    private String notasAdicionales;

    public MedicalConsultation() {
        this.id = UUID.randomUUID().toString();
        this.sintomas = new ArrayList<>();
        this.recetas = new ArrayList<>();
    }

    public MedicalConsultation(String pacienteId, String nombrePaciente,
                               String medicoId, String nombreMedico, String especialidad,
                               LocalDateTime fechaHora, String tipoConsulta, String motivoConsulta) {
        this();
        this.pacienteId = pacienteId;
        this.nombrePaciente = nombrePaciente;
        this.medicoId = medicoId;
        this.nombreMedico = nombreMedico;
        this.especialidad = especialidad;
        this.fechaHora = fechaHora;
        this.tipoConsulta = tipoConsulta;
        this.motivoConsulta = motivoConsulta;
        this.estado = "PROGRAMADA";
        this.prioridad = "MEDIA";
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPacienteId() { return pacienteId; }
    public void setPacienteId(String pacienteId) { this.pacienteId = pacienteId; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getMedicoId() { return medicoId; }
    public void setMedicoId(String medicoId) { this.medicoId = medicoId; }

    public String getNombreMedico() { return nombreMedico; }
    public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTipoConsulta() { return tipoConsulta; }
    public void setTipoConsulta(String tipoConsulta) { this.tipoConsulta = tipoConsulta; }

    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public List<String> getSintomas() { return sintomas; }
    public void setSintomas(List<String> sintomas) { this.sintomas = sintomas; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public List<String> getRecetas() { return recetas; }
    public void setRecetas(List<String> recetas) { this.recetas = recetas; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public String getNotasAdicionales() { return notasAdicionales; }
    public void setNotasAdicionales(String notasAdicionales) { this.notasAdicionales = notasAdicionales; }
}