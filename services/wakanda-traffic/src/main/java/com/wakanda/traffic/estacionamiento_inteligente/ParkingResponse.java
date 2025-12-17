package com.wakanda.traffic.estacionamiento_inteligente;

import java.util.List;

public class ParkingResponse {
    private int totalEspacios;
    private int disponibles;
    private int ocupados;
    private double porcentajeOcupacion;
    private List<ParkingSpot> espaciosDisponibles;
    private String mensaje;

    public ParkingResponse() {
    }

    public ParkingResponse(int totalEspacios, int disponibles, int ocupados,
                           double porcentajeOcupacion, List<ParkingSpot> espaciosDisponibles, String mensaje) {
        this.totalEspacios = totalEspacios;
        this.disponibles = disponibles;
        this.ocupados = ocupados;
        this.porcentajeOcupacion = porcentajeOcupacion;
        this.espaciosDisponibles = espaciosDisponibles;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public int getTotalEspacios() {
        return totalEspacios;
    }

    public void setTotalEspacios(int totalEspacios) {
        this.totalEspacios = totalEspacios;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    public int getOcupados() {
        return ocupados;
    }

    public void setOcupados(int ocupados) {
        this.ocupados = ocupados;
    }

    public double getPorcentajeOcupacion() {
        return porcentajeOcupacion;
    }

    public void setPorcentajeOcupacion(double porcentajeOcupacion) {
        this.porcentajeOcupacion = porcentajeOcupacion;
    }

    public List<ParkingSpot> getEspaciosDisponibles() {
        return espaciosDisponibles;
    }

    public void setEspaciosDisponibles(List<ParkingSpot> espaciosDisponibles) {
        this.espaciosDisponibles = espaciosDisponibles;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
