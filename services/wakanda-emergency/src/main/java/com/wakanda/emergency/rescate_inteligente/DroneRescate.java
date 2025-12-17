package com.wakanda.emergency.rescate_inteligente;

public class DroneRescate {
    private String idDrone;
    private int bateria;

    public DroneRescate(String idDrone, int bateria) {
        this.idDrone = idDrone;
        this.bateria = bateria;
    }

    public String escanearZona(String zona) {
        if (bateria < 20) return "⚠️ Batería baja. El dron " + idDrone + " debe regresar.";

        return "🚁 DRON " + idDrone + " (Bat: " + bateria + "%): Escaneando zona " + zona +
                " con sensores térmicos... 2 supervivientes localizados.";
    }

    // Getters necesarios para recibir JSON si fuera necesario
    public String getIdDrone() { return idDrone; }
    public int getBateria() { return bateria; }
}