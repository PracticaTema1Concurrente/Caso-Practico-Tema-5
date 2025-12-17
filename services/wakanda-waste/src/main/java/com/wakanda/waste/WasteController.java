package com.wakanda.waste;

import com.wakanda.waste.reciclaje_inteligente.ClasificadorAutomatico;
import com.wakanda.waste.plataformas_gestion.AppGestionResiduos;
import com.wakanda.waste.contenedores_inteligentes.ContenedorSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/waste")
public class WasteController {

    @Autowired
    private ClasificadorAutomatico clasificador; // Tu clase 1
    @Autowired
    private AppGestionResiduos appGestion;       // Tu clase 2

    // 1. Endpoint para CONTENEDORES INTELIGENTES
    // Recibe un JSON, comprueba si está lleno y responde
    @PostMapping("/sensor")
    public String verificarContenedor(@RequestBody ContenedorSensor contenedor) {
        return contenedor.verificarEstado();
    }

    // 2. Endpoint para RECICLAJE INTELIGENTE
    @GetMapping("/clasificar/{material}/{peso}")
    public String clasificar(@PathVariable String material, @PathVariable double peso) {
        String destino = clasificador.clasificarResiduo(material);
        String incentivo = clasificador.calcularIncentivos(material, peso);
        return "Tira esto al " + destino + ". \n" + incentivo;
    }

    // 3. Endpoint para PLATAFORMA DE GESTIÓN (Puntos cercanos)
    @GetMapping("/puntos")
    public List<String> verPuntosMapa() {
        return appGestion.obtenerPuntosRecoleccion();
    }

    // 4. Endpoint para RECOGIDA COMPOSTAJE
    @GetMapping("/recogida/{tipo}")
    public String pedirRecogida(@PathVariable String tipo) {
        return appGestion.solicitarRecogidaEspecial(tipo);
    }
}