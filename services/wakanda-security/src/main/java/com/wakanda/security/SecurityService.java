package com.wakanda.security;

import com.wakanda.security.live_alerts.AlertService;
import com.wakanda.security.live_alerts.SecurityAlert;
import com.wakanda.security.camaras_inteligentes.CameraService;
import com.wakanda.security.drones_seguridad.DroneService;
import com.wakanda.security.dto.SecurityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityService {

    @Autowired
    private CameraService cameraService;

    @Autowired
    private DroneService droneService;

    @Autowired
    private AlertService alertService;

    public SecurityResponse obtenerEstadoGeneral() {
        SecurityResponse response = new SecurityResponse();

        // Estadísticas de cámaras
        var statsCamaras = cameraService.obtenerEstadisticas();
        response.setTotalCamaras((int) statsCamaras.get("total"));
        response.setCamarasActivas(((Long) statsCamaras.get("activas")).intValue());

        // Estadísticas de drones
        var statsDrones = droneService.obtenerEstadisticas();
        response.setTotalDrones((int) statsDrones.get("total"));
        response.setDronesDisponibles(((Long) statsDrones.get("disponibles")).intValue());

        // Estadísticas de alertas
        List<SecurityAlert> alertasActivas = alertService.obtenerAlertasActivas();
        response.setAlertasActivas(alertasActivas.size());
        response.setAlertasRecientes(alertasActivas.stream().limit(5).toList());

        // Determinar nivel de seguridad
        response.setNivelSeguridad(calcularNivelSeguridad(alertasActivas));
        response.setMensaje(generarMensaje(response.getNivelSeguridad(), alertasActivas.size()));

        return response;
    }

    private String calcularNivelSeguridad(List<SecurityAlert> alertas) {
        long criticas = alertas.stream().filter(a -> "CRITICA".equals(a.getGravedad())).count();
        long altas = alertas.stream().filter(a -> "ALTA".equals(a.getGravedad())).count();

        if (criticas > 0) return "CRITICO";
        if (altas > 2) return "ALTO";
        if (alertas.size() > 5) return "MEDIO";
        return "BAJO";
    }

    private String generarMensaje(String nivel, int alertas) {
        return switch (nivel) {
            case "CRITICO" -> "🔴 ALERTA MÁXIMA - Situación crítica detectada. Todos los recursos activados.";
            case "ALTO" -> "🟠 NIVEL ALTO - Múltiples incidentes. Vigilancia intensificada.";
            case "MEDIO" -> "🟡 NIVEL MEDIO - Situación bajo control. Monitoreo activo.";
            default -> "🟢 SEGURIDAD ÓPTIMA - Ciudad bajo vigilancia normal.";
        };
    }

    public void actualizarSistemas() {
        cameraService.simularDetecciones();
        droneService.simularOperaciones();
        alertService.limpiarAlertasAntiguas();
    }
}