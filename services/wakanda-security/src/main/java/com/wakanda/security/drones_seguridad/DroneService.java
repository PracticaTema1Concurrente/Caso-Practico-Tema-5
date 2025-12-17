package com.wakanda.security.drones_seguridad;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DroneService {

    private final Map<String, SecurityDrone> drones = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @PostConstruct
    public void inicializarDrones() {
        System.out.println("🚁 Inicializando Flota de Drones de Seguridad...");

        // Drones de Patrulla General
        crearDrones("PATROL", "DJI-Wakanda-X1", 10, "GENERAL", true);

        // Drones de Respuesta Rápida
        crearDrones("RAPID", "Falcon-Response", 5, "EMERGENCIA", true);

        // Drones de Vigilancia Nocturna
        crearDrones("NIGHT", "NightHawk-Pro", 8, "NOCTURNA", true);

        // Drones de Zonas Remotas
        crearDrones("REMOTE", "LongRange-Scout", 6, "REMOTA", false);

        System.out.println("🚁 Flota de Drones inicializada: " + drones.size() + " unidades operativas");
    }

    private void crearDrones(String serie, String modelo, int cantidad, String zona, boolean termal) {
        for (int i = 1; i <= cantidad; i++) {
            String id = "DRONE-" + serie + "-" + String.format("%02d", i);
            int bateria = 60 + random.nextInt(40); // 60-100%
            drones.put(id, new SecurityDrone(id, modelo, zona, termal, bateria));
        }
    }

    public List<SecurityDrone> obtenerTodosLosDrones() {
        return new ArrayList<>(drones.values());
    }

    public List<SecurityDrone> obtenerDronesDisponibles() {
        return drones.values().stream()
                .filter(d -> Arrays.asList("EN_BASE", "PATRULLANDO").contains(d.getEstado()))
                .filter(d -> d.getBateria() > 30)
                .collect(Collectors.toList());
    }

    public SecurityDrone obtenerDron(String id) {
        SecurityDrone drone = drones.get(id);
        if (drone == null) {
            throw new RuntimeException("Dron no encontrado: " + id);
        }
        return drone;
    }

    public SecurityDrone desplegarDron(String droneId, String mision, String ubicacion) {
        SecurityDrone drone = obtenerDron(droneId);

        if (drone.getBateria() < 30) {
            throw new RuntimeException("Batería insuficiente. Dron en carga.");
        }

        if ("MANTENIMIENTO".equals(drone.getEstado())) {
            throw new RuntimeException("Dron en mantenimiento, no disponible.");
        }

        drone.setEstado("INVESTIGANDO");
        drone.setMisionActual(mision);

        // Simular coordenadas aleatorias
        drone.setLatitud(-15.0 + random.nextDouble() * 0.1);
        drone.setLongitud(28.0 + random.nextDouble() * 0.1);
        drone.setAltitud(50 + random.nextInt(150));

        return drone;
    }

    public SecurityDrone iniciarPatrulla(String droneId, String zona) {
        SecurityDrone drone = obtenerDron(droneId);

        if (drone.getBateria() < 40) {
            throw new RuntimeException("Batería insuficiente para patrulla extendida.");
        }

        drone.setEstado("PATRULLANDO");
        drone.setZonaAsignada(zona);
        drone.setMisionActual("Patrulla en zona " + zona);
        drone.setAltitud(100);

        return drone;
    }

    public SecurityDrone regresarABase(String droneId) {
        SecurityDrone drone = obtenerDron(droneId);

        if (drone.getBateria() < 20) {
            drone.setEstado("CARGANDO");
        } else {
            drone.setEstado("EN_BASE");
        }

        drone.setMisionActual(null);
        drone.setAltitud(0);

        return drone;
    }

    public void simularOperaciones() {
        drones.values().forEach(drone -> {
            switch (drone.getEstado()) {
                case "PATRULLANDO":
                case "INVESTIGANDO":
                    // Consumir batería
                    drone.setBateria(Math.max(0, drone.getBateria() - random.nextInt(3)));

                    // Si batería baja, regresar
                    if (drone.getBateria() < 20) {
                        regresarABase(drone.getId());
                    } else {
                        // Simular movimiento
                        drone.setLatitud(drone.getLatitud() + (random.nextDouble() - 0.5) * 0.01);
                        drone.setLongitud(drone.getLongitud() + (random.nextDouble() - 0.5) * 0.01);
                    }
                    break;

                case "CARGANDO":
                    // Cargar batería
                    drone.setBateria(Math.min(100, drone.getBateria() + 5));

                    if (drone.getBateria() >= 80) {
                        drone.setEstado("EN_BASE");
                    }
                    break;

                case "EN_BASE":
                    // Cargar lentamente
                    if (drone.getBateria() < 100) {
                        drone.setBateria(Math.min(100, drone.getBateria() + 1));
                    }
                    break;
            }
        });
    }

    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();

        long disponibles = drones.values().stream()
                .filter(d -> d.getBateria() > 30 && !"MANTENIMIENTO".equals(d.getEstado()))
                .count();

        long enMision = drones.values().stream()
                .filter(d -> Arrays.asList("PATRULLANDO", "INVESTIGANDO").contains(d.getEstado()))
                .count();

        Map<String, Long> porEstado = drones.values().stream()
                .collect(Collectors.groupingBy(SecurityDrone::getEstado, Collectors.counting()));

        stats.put("total", drones.size());
        stats.put("disponibles", disponibles);
        stats.put("enMision", enMision);
        stats.put("estadoDetallado", porEstado);

        return stats;
    }
}
