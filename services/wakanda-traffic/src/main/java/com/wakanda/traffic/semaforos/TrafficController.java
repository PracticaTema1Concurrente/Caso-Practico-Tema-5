package com.wakanda.traffic.semaforos;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/traffic")
public class TrafficController {

    // ESTE ENDPOINT ES PARA EL PUNTO 1: Mostrar el estado de los semáforos
    @GetMapping("/semaforos")
    @CircuitBreaker(name = "semaforos", fallbackMethod = "fallbackSemaforos")
    public String getEstadoSemaforos() {
        // Simulamos fallo aleatorio para probar la resiliencia
        if (Math.random() > 0.8) {
            throw new RuntimeException("¡Fallo en la red de semáforos!");
        }
        return "🟢 Semáforos Inteligentes: SISTEMA ONLINE (Gestión de Tiempos Activa)";
    }

    // Método de emergencia si falla el sistema
    public String fallbackSemaforos(Throwable t) {
        return "⚠️ ALERTA: Sistema central caído. Semáforos en modo preventivo (Parpadeo Ámbar).";
    }
}