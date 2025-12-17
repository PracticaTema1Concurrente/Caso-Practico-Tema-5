@RestController
@RequestMapping("/traffic")
public class TrafficController {

    @GetMapping("/semaforos")
    @HystrixCommand(fallbackMethod = "fallbackSemaforos") // Si falla, ejecuta el fallback
    public String getEstadoSemaforos() {
        if (Math.random() > 0.5) throw new RuntimeException("Fallo simulación sensor");
        return "Semáforos Inteligentes: FLUJO OPTIMIZADO";
    }

    public String fallbackSemaforos() {
        return "⚠️ ALERTA: Sensores caídos. Modo de tráfico preventivo activado.";
    }
}