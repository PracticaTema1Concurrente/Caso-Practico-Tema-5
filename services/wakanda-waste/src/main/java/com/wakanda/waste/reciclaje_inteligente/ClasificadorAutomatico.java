package com.wakanda.waste.reciclaje_inteligente;

import org.springframework.stereotype.Service;

@Service // 👈 Esto permite que el Controlador lo use
public class ClasificadorAutomatico {

    public String clasificarResiduo(String tipoMaterial) {
        switch (tipoMaterial.toLowerCase()) {
            case "plastico":
            case "latas":
                return "Contenedor Amarillo";
            case "papel":
            case "carton":
                return "Contenedor Azul";
            case "vidrio":
                return "Contenedor Verde";
            default:
                return "Residuo General";
        }
    }

    public String calcularIncentivos(String tipoMaterial, double pesoKg) {
        int puntos = 0;
        String mensaje;
        if (tipoMaterial.equals("plastico") || tipoMaterial.equals("vidrio")) {
            puntos = (int) (pesoKg * 10);
            mensaje = "¡Gracias por separar! Has ganado " + puntos + " puntos.";
        } else {
            mensaje = "Residuo procesado. Sigue reciclando.";
        }
        return mensaje; // 👈 Devolvemos String para verlo en Postman
    }
}