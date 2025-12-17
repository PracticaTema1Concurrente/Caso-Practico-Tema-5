package com.wakanda.traffic;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/traffic")
public class TrafficController {

    @Autowired
    private SmartTrafficService smartService;

    // --- Endpoint existente (Simulación simple) ---
    @GetMapping("/semaforos")
    @CircuitBreaker(name = "semaforos", fallbackMethod = "fallbackSemaforos")
    public String getEstadoSemaforos() {
        if (Math.random() > 0.8) { // Bajé un poco la probabilidad de fallo para que no moleste tanto
            throw new RuntimeException("¡Fallo en la red de semáforos!");
        }
        return "🟢 Semáforos Inteligentes: SISTEMA ONLINE";
    }

    public String fallbackSemaforos(Throwable t) {
        return "⚠️ ALERTA: Sistema central caído. Semáforos en modo preventivo (Parpadeo Ámbar).";
    }

    // --- NUEVO: Endpoint para recibir datos de SENSORES ---
    // Recibe un JSON con los datos del tráfico y devuelve la decisión inteligente
    @PostMapping("/sensor")
    public String recibirDatosSensor(@RequestBody TrafficSensorData sensorData) {
        return smartService.analyzeTrafficData(sensorData);
    }
}