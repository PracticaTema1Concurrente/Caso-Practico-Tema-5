package com.wakanda.traffic.semaforos;

import com.wakanda.traffic.sensores.TrafficSensorData;
import org.springframework.stereotype.Service;

@Service
public class SmartTrafficService {

    public String analyzeTrafficData(TrafficSensorData data) {
        // 1. Detección de Accidentes o Bloqueos (Prevención)
        if (data.isBlockageDetected() || (data.getVehicleCount() > 10 && data.getAverageSpeed() < 5)) {
            return "🚨 ALERTA: Posible accidente en " + data.getIntersectionId() +
                    ". Semáforos en ROJO TOTAL. Enviando alerta a Emergencias.";
        }

        // 2. Optimización de Flujo (Semáforos Inteligentes)
        if (data.getVehicleCount() > 50) {
            // Mucho tráfico: Aumentamos el tiempo del verde para descongestionar
            return "🚦 TRÁFICO DENSO en " + data.getIntersectionId() +
                    ". Ajustando semáforo: VERDE EXTENDIDO (90 segundos).";
        } else if (data.getVehicleCount() > 20) {
            // Tráfico moderado
            return "🚦 Tráfico Moderado en " + data.getIntersectionId() +
                    ". Ajustando semáforo: VERDE NORMAL (45 segundos).";
        } else {
            // Poco tráfico: Priorizamos el ahorro de energía o el cruce de peatones
            return "🟢 Tráfico Fluido en " + data.getIntersectionId() +
                    ". Ajustando semáforo: VERDE CORTO (30 segundos) / Prioridad Peatonal.";
        }
    }
}