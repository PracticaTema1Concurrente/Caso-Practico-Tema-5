package com.wakanda.traffic.transporte_publico;

import java.time.LocalDateTime;

public class PublicTransport {
    private String id;
    private String linea; // Ej: "L1", "L2", "BUS-45"
    private String tipo; // BUS, METRO, TRAM
    private String paradaActual;
    private String proximaParada;
    private int minutosLlegada;
    private String estado; // EN_RUTA, DETENIDO, DESVIO, CANCELADO
    private int capacidad;
    private int pasajerosActuales;
    private double velocidadPromedio;
    private LocalDateTime ultimaActualizacion;
    private String rutaAlternativa;
    private String motivoDesvio;

    public PublicTransport() {
    }

    public PublicTransport(String id, String linea, String tipo, String paradaActual,
                           String proximaParada, int minutosLlegada, String estado,
                           int capacidad, int pasajerosActuales, double velocidadPromedio) {
        this.id = id;
        this.linea = linea;
        this.tipo = tipo;
        this.paradaActual = paradaActual;
        this.proximaParada = proximaParada;
        this.minutosLlegada = minutosLlegada;
        this.estado = estado;
        this.capacidad = capacidad;
        this.pasajerosActuales = pasajerosActuales;
        this.velocidadPromedio = velocidadPromedio;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLinea() { return linea; }
    public void setLinea(String linea) { this.linea = linea; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getParadaActual() { return paradaActual; }
    public void setParadaActual(String paradaActual) { this.paradaActual = paradaActual; }

    public String getProximaParada() { return proximaParada; }
    public void setProximaParada(String proximaParada) { this.proximaParada = proximaParada; }

    public int getMinutosLlegada() { return minutosLlegada; }
    public void setMinutosLlegada(int minutosLlegada) {
        this.minutosLlegada = minutosLlegada;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) {
        this.estado = estado;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public int getPasajerosActuales() { return pasajerosActuales; }
    public void setPasajerosActuales(int pasajerosActuales) { this.pasajerosActuales = pasajerosActuales; }

    public double getVelocidadPromedio() { return velocidadPromedio; }
    public void setVelocidadPromedio(double velocidadPromedio) { this.velocidadPromedio = velocidadPromedio; }

    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }

    public String getRutaAlternativa() { return rutaAlternativa; }
    public void setRutaAlternativa(String rutaAlternativa) { this.rutaAlternativa = rutaAlternativa; }

    public String getMotivoDesvio() { return motivoDesvio; }
    public void setMotivoDesvio(String motivoDesvio) { this.motivoDesvio = motivoDesvio; }

    public double getPorcentajeOcupacion() {
        return (pasajerosActuales * 100.0) / capacidad;
    }
}