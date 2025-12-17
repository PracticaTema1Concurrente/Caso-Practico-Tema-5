package com.wakanda.health.dispositivos_monitoreo;

import java.time.LocalDateTime;

public class HealthDevice {
    private String id;
    private String pacienteId;
    private String nombrePaciente;
    private String tipo; // SMARTWATCH, PULSERA, MONITOR_CARDIACO, GLUCOMETRO, OXIMETRO
    private String modelo;
    private boolean activo;
    private int bateria;
    private LocalDateTime ultimaSincronizacion;

    // Signos Vitales
    private Integer frecuenciaCardiaca; // bpm
    private Integer presionSistolica;
    private Integer presionDiastolica;
    private Integer saturacionOxigeno; // %
    private Double temperatura; // °C
    private Integer pasos;
    private Integer calorias;
    private Double nivelGlucosa; // mg/dL

    // Alertas
    private String nivelAlerta; // NORMAL, PRECAUCION, ALERTA, CRITICO
    private String mensajeAlerta;
    private boolean alertaMedica;

    public HealthDevice() {
    }

    public HealthDevice(String id, String pacienteId, String nombrePaciente,
                        String tipo, String modelo) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.nombrePaciente = nombrePaciente;
        this.tipo = tipo;
        this.modelo = modelo;
        this.activo = true;
        this.bateria = 100;
        this.ultimaSincronizacion = LocalDateTime.now();
        this.nivelAlerta = "NORMAL";
        this.alertaMedica = false;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPacienteId() { return pacienteId; }
    public void setPacienteId(String pacienteId) { this.pacienteId = pacienteId; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public int getBateria() { return bateria; }
    public void setBateria(int bateria) { this.bateria = Math.min(100, Math.max(0, bateria)); }

    public LocalDateTime getUltimaSincronizacion() { return ultimaSincronizacion; }
    public void setUltimaSincronizacion(LocalDateTime ultimaSincronizacion) {
        this.ultimaSincronizacion = ultimaSincronizacion;
    }

    public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.ultimaSincronizacion = LocalDateTime.now();
    }

    public Integer getPresionSistolica() { return presionSistolica; }
    public void setPresionSistolica(Integer presionSistolica) { this.presionSistolica = presionSistolica; }

    public Integer getPresionDiastolica() { return presionDiastolica; }
    public void setPresionDiastolica(Integer presionDiastolica) { this.presionDiastolica = presionDiastolica; }

    public Integer getSaturacionOxigeno() { return saturacionOxigeno; }
    public void setSaturacionOxigeno(Integer saturacionOxigeno) { this.saturacionOxigeno = saturacionOxigeno; }

    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

    public Integer getPasos() { return pasos; }
    public void setPasos(Integer pasos) { this.pasos = pasos; }

    public Integer getCalorias() { return calorias; }
    public void setCalorias(Integer calorias) { this.calorias = calorias; }

    public Double getNivelGlucosa() { return nivelGlucosa; }
    public void setNivelGlucosa(Double nivelGlucosa) { this.nivelGlucosa = nivelGlucosa; }

    public String getNivelAlerta() { return nivelAlerta; }
    public void setNivelAlerta(String nivelAlerta) { this.nivelAlerta = nivelAlerta; }

    public String getMensajeAlerta() { return mensajeAlerta; }
    public void setMensajeAlerta(String mensajeAlerta) { this.mensajeAlerta = mensajeAlerta; }

    public boolean isAlertaMedica() { return alertaMedica; }
    public void setAlertaMedica(boolean alertaMedica) { this.alertaMedica = alertaMedica; }
}