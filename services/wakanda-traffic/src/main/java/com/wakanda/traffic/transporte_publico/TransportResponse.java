package com.wakanda.traffic.transporte_publico;

import java.util.List;

public class TransportResponse {
    private int totalUnidades;
    private int enRuta;
    private int conRetrasos;
    private int conDesvios;
    private List<PublicTransport> unidades;
    private String mensaje;
    private List<String> alertas;

    public TransportResponse() {
    }

    public TransportResponse(int totalUnidades, int enRuta, int conRetrasos,
                             int conDesvios, List<PublicTransport> unidades,
                             String mensaje, List<String> alertas) {
        this.totalUnidades = totalUnidades;
        this.enRuta = enRuta;
        this.conRetrasos = conRetrasos;
        this.conDesvios = conDesvios;
        this.unidades = unidades;
        this.mensaje = mensaje;
        this.alertas = alertas;
    }

    // Getters y Setters
    public int getTotalUnidades() { return totalUnidades; }
    public void setTotalUnidades(int totalUnidades) { this.totalUnidades = totalUnidades; }

    public int getEnRuta() { return enRuta; }
    public void setEnRuta(int enRuta) { this.enRuta = enRuta; }

    public int getConRetrasos() { return conRetrasos; }
    public void setConRetrasos(int conRetrasos) { this.conRetrasos = conRetrasos; }

    public int getConDesvios() { return conDesvios; }
    public void setConDesvios(int conDesvios) { this.conDesvios = conDesvios; }

    public List<PublicTransport> getUnidades() { return unidades; }
    public void setUnidades(List<PublicTransport> unidades) { this.unidades = unidades; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public List<String> getAlertas() { return alertas; }
    public void setAlertas(List<String> alertas) { this.alertas = alertas; }
}