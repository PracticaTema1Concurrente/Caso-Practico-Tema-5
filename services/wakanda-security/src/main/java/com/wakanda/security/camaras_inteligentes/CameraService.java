package com.wakanda.security.camaras_inteligentes;

import com.wakanda.security.live_alerts.AlertService;
import com.wakanda.security.live_alerts.SecurityAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CameraService {

    @Autowired
    private AlertService alertService;

    private final Map<String, SecurityCamera> cameras = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @PostConstruct
    public void inicializarCamaras() {
        System.out.println("📹 Inicializando Sistema de Cámaras de Seguridad...");

        // Zona Centro - Alta densidad
        crearCamaras("CENTRO", "Plaza Central", 20, true, 500);
        crearCamaras("CENTRO", "Av. Vibranium", 15, true, 500);
        crearCamaras("CENTRO", "Mercado Principal", 12, true, 300);

        // Zona Tecnológica
        crearCamaras("TECH", "Campus Wakanda", 25, true, 1000);
        crearCamaras("TECH", "Parque Innovación", 18, true, 800);

        // Zona Residencial
        crearCamaras("RESIDENCIAL", "Barrio Dorado", 30, true, 400);
        crearCamaras("RESIDENCIAL", "Zona Norte", 20, true, 400);

        // Zonas Críticas
        crearCamaras("CRITICO", "Aeropuerto", 40, true, 2000);
        crearCamaras("CRITICO", "Estación Central", 25, true, 1000);
        crearCamaras("CRITICO", "Hospital Central", 15, true, 500);

        System.out.println("📹 Sistema de Cámaras inicializado: " + cameras.size() + " cámaras activas");
    }

    private void crearCamaras(String zona, String ubicacion, int cantidad, boolean ia, int almacenamiento) {
        for (int i = 1; i <= cantidad; i++) {
            String id = "CAM-" + zona + "-" + String.format("%03d", i);
            String estado = random.nextDouble() > 0.05 ? "ACTIVA" : "MANTENIMIENTO";
            cameras.put(id, new SecurityCamera(id, ubicacion, zona, estado, ia, almacenamiento));
        }
    }

    public List<SecurityCamera> obtenerTodasLasCamaras() {
        return new ArrayList<>(cameras.values());
    }

    public List<SecurityCamera> obtenerPorZona(String zona) {
        return cameras.values().stream()
                .filter(cam -> cam.getZona().equalsIgnoreCase(zona))
                .collect(Collectors.toList());
    }

    public List<SecurityCamera> obtenerCamarasConAlertas() {
        return cameras.values().stream()
                .filter(cam -> cam.getNivelAlerta() > 3)
                .sorted(Comparator.comparingInt(SecurityCamera::getNivelAlerta).reversed())
                .collect(Collectors.toList());
    }

    public SecurityCamera obtenerCamara(String id) {
        SecurityCamera cam = cameras.get(id);
        if (cam == null) {
            throw new RuntimeException("Cámara no encontrada: " + id);
        }
        return cam;
    }

    public SecurityCamera activarDeteccion(String cameraId, String tipoDeteccion) {
        SecurityCamera cam = obtenerCamara(cameraId);

        cam.setTipoDeteccion(tipoDeteccion);

        // Determinar nivel de alerta según tipo
        int nivelAlerta = calcularNivelAlerta(tipoDeteccion);
        cam.setNivelAlerta(nivelAlerta);

        // Si es crítico, generar alerta automática
        if (nivelAlerta >= 7) {
            String gravedad = nivelAlerta >= 9 ? "CRITICA" : "ALTA";
            SecurityAlert alert = new SecurityAlert(
                    tipoDeteccion,
                    gravedad,
                    cam.getUbicacion(),
                    cam.getZona(),
                    "Detección automática de " + tipoDeteccion + " por IA",
                    "CAMARA",
                    cameraId
            );
            alertService.crearAlerta(alert);
        }

        return cam;
    }

    private int calcularNivelAlerta(String tipo) {
        switch (tipo.toUpperCase()) {
            case "INCENDIO": return 10;
            case "ROBO": return 8;
            case "ACCIDENTE": return 7;
            case "AGLOMERACION": return 5;
            case "SOSPECHOSO": return 4;
            case "NORMAL": return 0;
            default: return 3;
        }
    }

    public void simularDetecciones() {
        cameras.values().forEach(cam -> {
            if ("ACTIVA".equals(cam.getEstado()) && cam.isDeteccionIA()) {
                // 5% probabilidad de detectar algo
                if (random.nextDouble() < 0.05) {
                    String[] tipos = {"NORMAL", "NORMAL", "NORMAL", "AGLOMERACION",
                            "SOSPECHOSO", "ROBO", "ACCIDENTE"};
                    String tipoDetectado = tipos[random.nextInt(tipos.length)];
                    activarDeteccion(cam.getId(), tipoDetectado);
                } else if (cam.getNivelAlerta() > 0) {
                    // Reducir nivel de alerta gradualmente
                    cam.setNivelAlerta(Math.max(0, cam.getNivelAlerta() - 1));
                    if (cam.getNivelAlerta() == 0) {
                        cam.setTipoDeteccion("NORMAL");
                    }
                }

                // Simular uso de almacenamiento
                if (cam.isGrabando()) {
                    int nuevoUso = Math.min(cam.getCapacidadAlmacenamiento(),
                            cam.getAlmacenamientoUsado() + random.nextInt(5));
                    cam.setAlmacenamientoUsado(nuevoUso);
                }
            }
        });
    }

    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();

        long activas = cameras.values().stream().filter(c -> "ACTIVA".equals(c.getEstado())).count();
        long conAlertas = cameras.values().stream().filter(c -> c.getNivelAlerta() > 3).count();

        Map<String, Long> porZona = cameras.values().stream()
                .collect(Collectors.groupingBy(SecurityCamera::getZona, Collectors.counting()));

        stats.put("total", cameras.size());
        stats.put("activas", activas);
        stats.put("conAlertas", conAlertas);
        stats.put("distribucionZonas", porZona);

        return stats;
    }

    public SecurityCamera reiniciarCamara(String id) {
        SecurityCamera cam = obtenerCamara(id);
        cam.setEstado("ACTIVA");
        cam.setNivelAlerta(0);
        cam.setTipoDeteccion("NORMAL");
        cam.setAlmacenamientoUsado(0);
        return cam;
    }
}
