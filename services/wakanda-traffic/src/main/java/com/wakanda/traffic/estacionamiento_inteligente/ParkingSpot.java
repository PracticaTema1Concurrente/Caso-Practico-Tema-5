package com.wakanda.traffic.estacionamiento_inteligente;

import java.time.LocalDateTime;

public class ParkingSpot {
    private String id;
    private String zona;
    private String ubicacion;
    private boolean ocupado;
    private LocalDateTime ultimaActualizacion;
    private String tipoVehiculo; // COCHE, MOTO, ELECTRICO, DISCAPACITADO
    private Double tarifa;

    public ParkingSpot() {
    }

    public ParkingSpot(String id, String zona, String ubicacion, boolean ocupado, String tipoVehiculo, Double tarifa) {
        this.id = id;
        this.zona = zona;
        this.ubicacion = ubicacion;
        this.ocupado = ocupado;
        this.tipoVehiculo = tipoVehiculo;
        this.tarifa = tarifa;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public Double getTarifa() {
        return tarifa;
    }

    public void setTarifa(Double tarifa) {
        this.tarifa = tarifa;
    }
}
