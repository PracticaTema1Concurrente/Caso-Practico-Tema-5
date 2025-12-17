package com.wakanda.emergency.alertas_geolocalizadas;

import org.springframework.stereotype.Service;

@Service
public class ServicioAlertas {

    public String enviarAlertaMasiva(String tipoDesastre, String coordenadas) {
        // Simula el envío de SMS/Push a móviles en esa zona
        return "📲 ALERTA ENVIADA: Se ha notificado a todos los ciudadanos en un radio de 5km de " +
                coordenadas + " sobre: " + tipoDesastre.toUpperCase() + ". Protocolo de evacuación activado.";
    }
}