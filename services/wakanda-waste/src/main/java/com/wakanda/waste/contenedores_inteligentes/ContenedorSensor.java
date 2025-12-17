package com.wakanda.waste.contenedores_inteligentes;

public class ContenedorSensor {
    private int id;
    private double capacidadTotalKg;
    private double nivelActualKg;

    // Constructor vacío necesario para recibir JSON en Spring
    public ContenedorSensor() {}

    public ContenedorSensor(int id, double capacidadTotalKg) {
        this.id = id;
        this.capacidadTotalKg = capacidadTotalKg;
        this.nivelActualKg = 0;
    }

    // Getters y Setters (Necesarios para que Spring lea los datos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getCapacidadTotalKg() { return capacidadTotalKg; }
    public double getNivelActualKg() { return nivelActualKg; }
    public void setNivelActualKg(double nivelActualKg) { this.nivelActualKg = nivelActualKg; }

    // Tu lógica original adaptada para devolver texto
    public String verificarEstado() {
        if (nivelActualKg >= capacidadTotalKg) {
            return "[ALERTA SENSOR] El contenedor " + id + " está LLENO (" + nivelActualKg + "kg). Enviando camión...";
        } else {
            return "Estado Normal: " + nivelActualKg + "/" + capacidadTotalKg + "kg.";
        }
    }
}