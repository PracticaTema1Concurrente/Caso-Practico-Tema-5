package com.wakanda.waste.plataformas_gestion;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppGestionResiduos {

    // Simulamos una "base de datos" en memoria
    public List<String> obtenerPuntosRecoleccion() {
        List<String> puntos = new ArrayList<>();
        puntos.add("Parque Central - Punto Verde");
        puntos.add("Calle Mayor 45 - Compostaje Comunitario");
        return puntos;
    }

    public String solicitarRecogidaEspecial(String tipoResiduo) {
        if (tipoResiduo.equalsIgnoreCase("organico")) {
            return "✅ Solicitud recibida: Un equipo pasará a recoger tus residuos orgánicos.";
        } else {
            return "ℹ️ Para residuo '" + tipoResiduo + "', por favor acude a un punto limpio.";
        }
    }
}