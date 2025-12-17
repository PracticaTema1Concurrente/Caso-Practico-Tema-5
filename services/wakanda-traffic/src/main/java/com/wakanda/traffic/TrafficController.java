package com.wakanda.traffic;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/traffic")
public class TrafficController {

    @Autowired
    private SmartTrafficService smartService;

    @GetMapping("/semaforos")
    @CircuitBreaker(name = "semaforos", fallbackMethod = "fallbackSemaforos")
    public String getEstadoSemaforos() {
        if (Math.random() > 0.5) {
            throw new RuntimeException("Fallo simulación sensor");
        }
        return "🚦 Semáforos Inteligentes: FLUJO OPTIMIZADO";
    }

    public String fallbackSemaforos() {
        return "⚠️ ALERTA: Sensores caídos. Modo de tráfico preventivo activado.";
    }

    @GetMapping("/info")
    public String getInfo() {
        return "🚦 Sistema de Tráfico Inteligente de Wakanda\n" +
                "- Semáforos inteligentes\n" +
                "- Estacionamiento inteligente\n" +
                "- Monitoreo en tiempo real";
    }

    // --- NUEVO: Endpoint para recibir datos de SENSORES ---
    // Recibe un JSON con los datos del tráfico y devuelve la decisión inteligente
    @PostMapping("/sensor")
    public String recibirDatosSensor(@RequestBody TrafficSensorData sensorData) {
        return smartService.analyzeTrafficData(sensorData);
    }
}