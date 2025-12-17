@RestController
@RequestMapping("/emergency")
public class EmergencyController {

    @Autowired
    private TrafficClient trafficClient; // Inyección del cliente Feign

    @GetMapping("/alerta")
    public String generarAlerta() {
        // Llamada interna al microservicio de tráfico
        String reporteTrafico = trafficClient.obtenerEstadoTrafico();
        return "🚑 AMBULANCIA DESPACHADA. Estado rutas: " + reporteTrafico;
    }
}