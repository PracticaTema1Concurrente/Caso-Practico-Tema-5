package com.wakanda.emergency;

import com.wakanda.emergency.alertas_geolocalizadas.ServicioAlertas;
import com.wakanda.emergency.rescate_inteligente.DroneRescate;
import com.wakanda.emergency.respuesta_inteligente.GestorRespuesta;
import com.wakanda.emergency.respuesta_inteligente.VehiculoEmergencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emergency")
public class EmergencyController {

    @Autowired
    private GestorRespuesta gestorRespuesta; // Punto 1
    @Autowired
    private ServicioAlertas servicioAlertas; // Punto 2

    // --- PUNTO 1: RESPUESTA INTELIGENTE ---
    @PostMapping("/asignar")
    public String asignarRecurso(@RequestParam String tipo, @RequestParam String zona) {
        return gestorRespuesta.asignarVehiculo(tipo, zona);
    }

    @PostMapping("/vehiculo/actualizar")
    public String actualizarEstado(@RequestBody VehiculoEmergencia vehiculo) {
        return "📡 Vehículo " + vehiculo.getId() + " actualizado en " + vehiculo.getUbicacionActual();
    }

    // --- PUNTO 2: ALERTAS GEOLOCALIZADAS ---
    @GetMapping("/alerta/{desastre}/{coords}")
    public String lanzarAlerta(@PathVariable String desastre, @PathVariable String coords) {
        return servicioAlertas.enviarAlertaMasiva(desastre, coords);
    }

    // --- PUNTO 3: RESCATE INTELIGENTE (Drones) ---
    @PostMapping("/dron/escanear")
    public String usarDron(@RequestBody DroneRescate dron, @RequestParam String zona) {
        return dron.escanearZona(zona);
    }
}