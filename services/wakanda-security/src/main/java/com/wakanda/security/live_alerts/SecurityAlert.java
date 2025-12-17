package com.wakanda.security.live_alerts;

import java.time.LocalDateTime;
import java.util.UUID;

public class SecurityAlert {
    private String id;
    private String tipo; // ROBO, INCENDIO, ACCIDENTE, AGLOMERACION, EMERGENCIA, DESASTRE
    private String gravedad; // BAJA, MEDIA, ALTA, CRITICA
    private String ubicacion;
    private String zona;
    private String descripcion;
    private LocalDateTime timestamp;
    private String estado; // ACTIVA, EN_PROCESO, RESUELTA, FALSA_ALARMA
    private String fuenteDeteccion; // CAMARA, DRON, SENSOR, MANUAL
    private String idFuente;
    private boolean notificacionEnviada;
    private int ciudadanosAfectados;

    public SecurityAlert() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.estado = "ACTIVA";
        this.notificacionEnviada = false;
    }

    public SecurityAlert(String tipo, String gravedad, String ubicacion, String zona,
                         String descripcion, String fuenteDeteccion, String idFuente) {
        this();
        this.tipo = tipo;
        this.gravedad = gravedad;
        this.ubicacion = ubicacion;
        this.zona = zona;
        this.descripcion = descripcion;
        this.fuenteDeteccion = fuenteDeteccion;
        this.idFuente = idFuente;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getGravedad() { return gravedad; }
    public void setGravedad(String gravedad) { this.gravedad = gravedad; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFuenteDeteccion() { return fuenteDeteccion; }
    public void setFuenteDeteccion(String fuenteDeteccion) { this.fuenteDeteccion = fuenteDeteccion; }

    public String getIdFuente() { return idFuente; }
    public void setIdFuente(String idFuente) { this.idFuente = idFuente; }

    public boolean isNotificacionEnviada() { return notificacionEnviada; }
    public void setNotificacionEnviada(boolean notificacionEnviada) { this.notificacionEnviada = notificacionEnviada; }

    public int getCiudadanosAfectados() { return ciudadanosAfectados; }
    public void setCiudadanosAfectados(int ciudadanosAfectados) { this.ciudadanosAfectados = ciudadanosAfectados; }
}