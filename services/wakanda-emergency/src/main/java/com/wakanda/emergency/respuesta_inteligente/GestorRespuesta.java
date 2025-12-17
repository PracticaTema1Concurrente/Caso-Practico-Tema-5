package com.wakanda.emergency.respuesta_inteligente;

import org.springframework.stereotype.Service;

@Service
public class GestorRespuesta {

    public String asignarVehiculo(String tipoEmergencia, String zona) {
        // Lógica simulada de optimización de rutas
        return "🚑 UNIDAD ASIGNADA: Enviando vehículo de tipo " + tipoEmergencia +
                " más cercano a la zona " + zona + ". Tiempo estimado: 2 minutos.";
    }
}