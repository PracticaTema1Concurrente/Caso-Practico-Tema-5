package com.wakanda.security.live_alerts;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private final Map<String, SecurityAlert> alerts = new ConcurrentHashMap<>();
    private final List<String> notificacionesEnviadas = Collections.synchronizedList(new ArrayList<>());

    public SecurityAlert crearAlerta(SecurityAlert alert) {
        alerts.put(alert.getId(), alert);

        // Simular envío de notificación
        if ("CRITICA".equals(alert.getGravedad()) || "ALTA".equals(alert.getGravedad())) {
            enviarNotificacion(alert);
        }

        System.out.println("🚨 ALERTA CREADA: " + alert.getTipo() + " en " + alert.getUbicacion()
                + " [" + alert.getGravedad() + "]");

        return alert;
    }

    public SecurityAlert crearAlerta(String tipo, String gravedad, String ubicacion,
                                     String zona, String descripcion) {
        SecurityAlert alert = new SecurityAlert(tipo, gravedad, ubicacion, zona,
                descripcion, "MANUAL", "OPERADOR");
        return crearAlerta(alert);
    }

    private void enviarNotificacion(SecurityAlert alert) {
        String notificacion = String.format(
                "📱 NOTIFICACIÓN: %s - %s en %s. %s",
                alert.getGravedad(), alert.getTipo(), alert.getUbicacion(), alert.getDescripcion()
        );

        notificacionesEnviadas.add(notificacion);
        alert.setNotificacionEnviada(true);

        // Simular ciudadanos notificados
        int afectados = calcularCiudadanosAfectados(alert.getZona(), alert.getGravedad());
        alert.setCiudadanosAfectados(afectados);
    }

    private int calcularCiudadanosAfectados(String zona, String gravedad) {
        int base = switch (zona.toUpperCase()) {
            case "CENTRO" -> 5000;
            case "TECH" -> 3000;
            case "RESIDENCIAL" -> 8000;
            case "CRITICO" -> 10000;
            default -> 1000;
        };

        double multiplicador = switch (gravedad) {
            case "CRITICA" -> 1.5;
            case "ALTA" -> 1.2;
            case "MEDIA" -> 0.8;
            default -> 0.3;
        };

        return (int)(base * multiplicador);
    }

    public List<SecurityAlert> obtenerTodasLasAlertas() {
        return new ArrayList<>(alerts.values())
                .stream()
                .sorted(Comparator.comparing(SecurityAlert::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public List<SecurityAlert> obtenerAlertasActivas() {
        return alerts.values().stream()
                .filter(a -> "ACTIVA".equals(a.getEstado()))
                .sorted(Comparator.comparing(SecurityAlert::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public List<SecurityAlert> obtenerPorGravedad(String gravedad) {
        return alerts.values().stream()
                .filter(a -> a.getGravedad().equalsIgnoreCase(gravedad))
                .sorted(Comparator.comparing(SecurityAlert::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public List<SecurityAlert> obtenerPorZona(String zona) {
        return alerts.values().stream()
                .filter(a -> a.getZona().equalsIgnoreCase(zona))
                .sorted(Comparator.comparing(SecurityAlert::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public SecurityAlert actualizarEstado(String alertId, String nuevoEstado) {
        SecurityAlert alert = alerts.get(alertId);
        if (alert == null) {
            throw new RuntimeException("Alerta no encontrada: " + alertId);
        }

        alert.setEstado(nuevoEstado);

        if ("RESUELTA".equals(nuevoEstado)) {
            System.out.println("✅ Alerta resuelta: " + alertId);
        }

        return alert;
    }

    public List<String> obtenerNotificaciones() {
        return new ArrayList<>(notificacionesEnviadas);
    }

    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();

        long activas = alerts.values().stream().filter(a -> "ACTIVA".equals(a.getEstado())).count();
        long criticas = alerts.values().stream().filter(a -> "CRITICA".equals(a.getGravedad())).count();

        Map<String, Long> porTipo = alerts.values().stream()
                .collect(Collectors.groupingBy(SecurityAlert::getTipo, Collectors.counting()));

        Map<String, Long> porGravedad = alerts.values().stream()
                .collect(Collectors.groupingBy(SecurityAlert::getGravedad, Collectors.counting()));

        stats.put("total", alerts.size());
        stats.put("activas", activas);
        stats.put("criticas", criticas);
        stats.put("porTipo", porTipo);
        stats.put("porGravedad", porGravedad);
        stats.put("notificacionesEnviadas", notificacionesEnviadas.size());

        return stats;
    }

    public void limpiarAlertasAntiguas() {
        LocalDateTime limiteAntigüedad = LocalDateTime.now().minusHours(24);

        List<String> paraEliminar = alerts.values().stream()
                .filter(a -> "RESUELTA".equals(a.getEstado()) || "FALSA_ALARMA".equals(a.getEstado()))
                .filter(a -> a.getTimestamp().isBefore(limiteAntigüedad))
                .map(SecurityAlert::getId)
                .collect(Collectors.toList());

        paraEliminar.forEach(alerts::remove);

        if (!paraEliminar.isEmpty()) {
            System.out.println("🧹 Limpiadas " + paraEliminar.size() + " alertas antiguas");
        }
    }
}
