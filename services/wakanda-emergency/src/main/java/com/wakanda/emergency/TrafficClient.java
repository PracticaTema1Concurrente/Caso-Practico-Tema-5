@FeignClient(name = "wakanda-traffic") // Nombre del servicio en Eureka
public interface TrafficClient {
    @GetMapping("/traffic/semaforos")
    String obtenerEstadoTrafico();
}