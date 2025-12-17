package com.wakanda.traffic.transporte_publico;

import java.time.LocalDateTime;

public class ArrivalPrediction {
    private String lineaId;
    private String parada;
    private int minutosEstimados;
    private LocalDateTime horaLlegadaEstimada;
    private String confianza; // ALTA, MEDIA, BAJA
    private String estado;
    private String desvio;

    public ArrivalPrediction(String lineaId, String parada, int minutosEstimados,
                             String confianza, String estado) {
        this.lineaId = lineaId;
        this.parada = parada;
        this.minutosEstimados = minutosEstimados;
        this.horaLlegadaEstimada = LocalDateTime.now().plusMinutes(minutosEstimados);
        this.confianza = confianza;
        this.estado = estado;
    }

    // Getters y Setters
    public String getLineaId() { return lineaId; }
    public void setLineaId(String lineaId) { this.lineaId = lineaId; }

    public String getParada() { return parada; }
    public void setParada(String parada) { this.parada = parada; }

    public int getMinutosEstimados() { return minutosEstimados; }
    public void setMinutosEstimados(int minutosEstimados) {
        this.minutosEstimados = minutosEstimados;
        this.horaLlegadaEstimada = LocalDateTime.now().plusMinutes(minutosEstimados);
    }

    public LocalDateTime getHoraLlegadaEstimada() { return horaLlegadaEstimada; }
    public void setHoraLlegadaEstimada(LocalDateTime horaLlegadaEstimada) {
        this.horaLlegadaEstimada = horaLlegadaEstimada;
    }

    public String getConfianza() { return confianza; }
    public void setConfianza(String confianza) { this.confianza = confianza; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDesvio() { return desvio; }
    public void setDesvio(String desvio) { this.desvio = desvio; }
}