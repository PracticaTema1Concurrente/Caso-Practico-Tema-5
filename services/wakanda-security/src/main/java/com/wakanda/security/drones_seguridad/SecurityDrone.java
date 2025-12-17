package com.wakanda.security.drones_seguridad;

import java.time.LocalDateTime;

public class SecurityDrone {
    private String id;
    private String modelo;
    private String estado; // PATRULLANDO, EN_BASE, INVESTIGANDO, CARGANDO, MANTENIMIENTO
    private String zonaAsignada;
    private double latitud;
    private double longitud;
    private double altitud;
    private int bateria; // 0-100
    private boolean camaraActiva;
    private boolean sensorTermico;
    private LocalDateTime ultimaActividad;
    private String misionActual;
    private int tiempoVueloRestante; // minutos

    public SecurityDrone() {
    }

    public SecurityDrone(String id, String modelo, String zonaAsignada,
                         boolean sensorTermico, int bateria) {
        this.id = id;
        this.modelo = modelo;
        this.zonaAsignada = zonaAsignada;
        this.sensorTermico = sensorTermico;
        this.bateria = bateria;
        this.estado = bateria > 30 ? "EN_BASE" : "CARGANDO";
        this.camaraActiva = true;
        this.ultimaActividad = LocalDateTime.now();
        this.tiempoVueloRestante = (int)(bateria * 0.3); // ~30 min con 100%
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) {
        this.estado = estado;
        this.ultimaActividad = LocalDateTime.now();
    }

    public String getZonaAsignada() { return zonaAsignada; }
    public void setZonaAsignada(String zonaAsignada) { this.zonaAsignada = zonaAsignada; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    public double getAltitud() { return altitud; }
    public void setAltitud(double altitud) { this.altitud = altitud; }

    public int getBateria() { return bateria; }
    public void setBateria(int bateria) {
        this.bateria = Math.min(100, Math.max(0, bateria));
        this.tiempoVueloRestante = (int)(this.bateria * 0.3);
    }

    public boolean isCamaraActiva() { return camaraActiva; }
    public void setCamaraActiva(boolean camaraActiva) { this.camaraActiva = camaraActiva; }

    public boolean isSensorTermico() { return sensorTermico; }
    public void setSensorTermico(boolean sensorTermico) { this.sensorTermico = sensorTermico; }

    public LocalDateTime getUltimaActividad() { return ultimaActividad; }
    public void setUltimaActividad(LocalDateTime ultimaActividad) { this.ultimaActividad = ultimaActividad; }

    public String getMisionActual() { return misionActual; }
    public void setMisionActual(String misionActual) { this.misionActual = misionActual; }

    public int getTiempoVueloRestante() { return tiempoVueloRestante; }
    public void setTiempoVueloRestante(int tiempoVueloRestante) { this.tiempoVueloRestante = tiempoVueloRestante; }
}
