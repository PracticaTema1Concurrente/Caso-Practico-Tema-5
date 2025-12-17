package com.wakanda.emergency.respuesta_inteligente;

public class VehiculoEmergencia {
    private String id;
    private String tipo; // Ambulancia, Bomberos, Policía
    private String ubicacionActual;
    private boolean disponible;

    public VehiculoEmergencia(String id, String tipo, String ubicacionActual) {
        this.id = id;
        this.tipo = tipo;
        this.ubicacionActual = ubicacionActual;
        this.disponible = true;
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getTipo() { return tipo; }
    public String getUbicacionActual() { return ubicacionActual; }
    public void setUbicacionActual(String ubicacionActual) { this.ubicacionActual = ubicacionActual; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}