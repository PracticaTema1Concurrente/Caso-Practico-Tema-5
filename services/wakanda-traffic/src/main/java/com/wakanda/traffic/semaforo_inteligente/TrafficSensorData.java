package com.wakanda.traffic.semaforo_inteligente;

public class TrafficSensorData {
    private String intersectionId; // ID del cruce (ej: "Avenida-Stark-1")
    private int vehicleCount;      // Cantidad de vehículos detectados
    private double averageSpeed;   // Velocidad media (km/h)
    private boolean blockageDetected; // ¿Hay algo bloqueando la vía?

    // Constructor vacío (necesario para Spring)
    public TrafficSensorData() {}

    // Constructor completo
    public TrafficSensorData(String intersectionId, int vehicleCount, double averageSpeed, boolean blockageDetected) {
        this.intersectionId = intersectionId;
        this.vehicleCount = vehicleCount;
        this.averageSpeed = averageSpeed;
        this.blockageDetected = blockageDetected;
    }

    // Getters y Setters
    public String getIntersectionId() { return intersectionId; }
    public void setIntersectionId(String intersectionId) { this.intersectionId = intersectionId; }

    public int getVehicleCount() { return vehicleCount; }
    public void setVehicleCount(int vehicleCount) { this.vehicleCount = vehicleCount; }

    public double getAverageSpeed() { return averageSpeed; }
    public void setAverageSpeed(double averageSpeed) { this.averageSpeed = averageSpeed; }

    public boolean isBlockageDetected() { return blockageDetected; }
    public void setBlockageDetected(boolean blockageDetected) { this.blockageDetected = blockageDetected; }
}