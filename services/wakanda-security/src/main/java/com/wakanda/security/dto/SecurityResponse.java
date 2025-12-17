package com.wakanda.security.dto;

import com.wakanda.security.live_alerts.SecurityAlert;

import java.util.List;

public class SecurityResponse {
    private int totalCamaras;
    private int camarasActivas;
    private int totalDrones;
    private int dronesDisponibles;
    private int alertasActivas;
    private String nivelSeguridad; // BAJO, MEDIO, ALTO, CRITICO
    private List<SecurityAlert> alertasRecientes;
    private String mensaje;

    public SecurityResponse() {
    }

    // Getters y Setters
    public int getTotalCamaras() { return totalCamaras; }
    public void setTotalCamaras(int totalCamaras) { this.totalCamaras = totalCamaras; }

    public int getCamarasActivas() { return camarasActivas; }
    public void setCamarasActivas(int camarasActivas) { this.camarasActivas = camarasActivas; }

    public int getTotalDrones() { return totalDrones; }
    public void setTotalDrones(int totalDrones) { this.totalDrones = totalDrones; }

    public int getDronesDisponibles() { return dronesDisponibles; }
    public void setDronesDisponibles(int dronesDisponibles) { this.dronesDisponibles = dronesDisponibles; }

    public int getAlertasActivas() { return alertasActivas; }
    public void setAlertasActivas(int alertasActivas) { this.alertasActivas = alertasActivas; }

    public String getNivelSeguridad() { return nivelSeguridad; }
    public void setNivelSeguridad(String nivelSeguridad) { this.nivelSeguridad = nivelSeguridad; }

    public List<SecurityAlert> getAlertasRecientes() { return alertasRecientes; }
    public void setAlertasRecientes(List<SecurityAlert> alertasRecientes) { this.alertasRecientes = alertasRecientes; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}