package com.wakanda.security.camaras_inteligentes;

import java.time.LocalDateTime;

public class SecurityCamera {
    private String id;
    private String ubicacion;
    private String zona;
    private String estado; // ACTIVA, INACTIVA, MANTENIMIENTO, ERROR
    private boolean deteccionIA;
    private LocalDateTime ultimaDeteccion;
    private String tipoDeteccion; // NORMAL, ROBO, AGLOMERACION, ACCIDENTE, INCENDIO
    private int nivelAlerta; // 0-10
    private double angulo;
    private boolean grabando;
    private int capacidadAlmacenamiento; // en GB
    private int almacenamientoUsado;

    public SecurityCamera() {
    }

    public SecurityCamera(String id, String ubicacion, String zona, String estado,
                          boolean deteccionIA, int capacidadAlmacenamiento) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.zona = zona;
        this.estado = estado;
        this.deteccionIA = deteccionIA;
        this.capacidadAlmacenamiento = capacidadAlmacenamiento;
        this.grabando = true;
        this.tipoDeteccion = "NORMAL";
        this.nivelAlerta = 0;
        this.ultimaDeteccion = LocalDateTime.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isDeteccionIA() { return deteccionIA; }
    public void setDeteccionIA(boolean deteccionIA) { this.deteccionIA = deteccionIA; }

    public LocalDateTime getUltimaDeteccion() { return ultimaDeteccion; }
    public void setUltimaDeteccion(LocalDateTime ultimaDeteccion) { this.ultimaDeteccion = ultimaDeteccion; }

    public String getTipoDeteccion() { return tipoDeteccion; }
    public void setTipoDeteccion(String tipoDeteccion) {
        this.tipoDeteccion = tipoDeteccion;
        this.ultimaDeteccion = LocalDateTime.now();
    }

    public int getNivelAlerta() { return nivelAlerta; }
    public void setNivelAlerta(int nivelAlerta) { this.nivelAlerta = Math.min(10, Math.max(0, nivelAlerta)); }

    public double getAngulo() { return angulo; }
    public void setAngulo(double angulo) { this.angulo = angulo; }

    public boolean isGrabando() { return grabando; }
    public void setGrabando(boolean grabando) { this.grabando = grabando; }

    public int getCapacidadAlmacenamiento() { return capacidadAlmacenamiento; }
    public void setCapacidadAlmacenamiento(int capacidadAlmacenamiento) { this.capacidadAlmacenamiento = capacidadAlmacenamiento; }

    public int getAlmacenamientoUsado() { return almacenamientoUsado; }
    public void setAlmacenamientoUsado(int almacenamientoUsado) { this.almacenamientoUsado = almacenamientoUsado; }

    public double getPorcentajeAlmacenamiento() {
        return (almacenamientoUsado * 100.0) / capacidadAlmacenamiento;
    }
}
