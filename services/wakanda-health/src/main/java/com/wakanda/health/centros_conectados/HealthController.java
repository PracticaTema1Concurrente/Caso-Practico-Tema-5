package com.wakanda.health.centros_conectados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private GestorCentroSalud gestorSalud;
    // Como están en el mismo paquete, NO hace falta hacer 'import ...GestorCentroSalud'

    // --- PUNTO 3: CENTROS DE SALUD CONECTADOS ---

    // 1. Optimización de citas (POST)
    @PostMapping("/centro/cita")
    public String solicitarAtencion(@RequestBody ExpedientePaciente paciente) {
        return gestorSalud.gestionarCita(paciente);
    }

    // 2. Gestión de datos (GET)
    @GetMapping("/centro/paciente/{id}")
    public String verDatosPaciente(@PathVariable String id) {
        return gestorSalud.consultarHistorial(id);
    }
}