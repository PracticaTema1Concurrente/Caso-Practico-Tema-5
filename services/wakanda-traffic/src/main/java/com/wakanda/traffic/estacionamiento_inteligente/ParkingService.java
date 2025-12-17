package com.wakanda.traffic.estacionamiento_inteligente;

import com.wakanda.traffic.estacionamiento_inteligente.ParkingResponse;
import com.wakanda.traffic.estacionamiento_inteligente.ParkingSpot;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private final Map<String, ParkingSpot> parkingSpots = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @PostConstruct
    public void inicializarParking() {
        // Zona Centro
        crearEspacios("CENTRO", "Av. Vibranium", 50, "COCHE", 2.5);
        crearEspacios("CENTRO", "Plaza Real", 30, "MOTO", 1.0);

        // Zona Tecnológica
        crearEspacios("TECH", "Parque Innovación", 100, "ELECTRICO", 1.5);
        crearEspacios("TECH", "Campus Wakanda", 80, "COCHE", 2.0);

        // Zona Residencial
        crearEspacios("RESIDENCIAL", "Barrio Dorado", 120, "COCHE", 1.5);
        crearEspacios("RESIDENCIAL", "Calle Paz", 20, "DISCAPACITADO", 0.5);

        System.out.println("🅿️ Sistema de Estacionamiento Inteligente inicializado: "
                + parkingSpots.size() + " espacios totales");
    }

    private void crearEspacios(String zona, String ubicacion, int cantidad, String tipo, double tarifa) {
        for (int i = 1; i <= cantidad; i++) {
            String id = zona + "-" + tipo + "-" + String.format("%03d", i);
            boolean ocupado = random.nextDouble() < 0.6; // 60% ocupación inicial
            parkingSpots.put(id, new ParkingSpot(id, zona, ubicacion, ocupado, tipo, tarifa));
        }
    }

    public ParkingResponse obtenerEstadoGeneral() {
        List<ParkingSpot> todosLosEspacios = new ArrayList<>(parkingSpots.values());
        return generarRespuesta(todosLosEspacios);
    }

    public ParkingResponse buscarPorZona(String zona) {
        List<ParkingSpot> espaciosZona = parkingSpots.values().stream()
                .filter(spot -> spot.getZona().equalsIgnoreCase(zona))
                .collect(Collectors.toList());

        if (espaciosZona.isEmpty()) {
            throw new RuntimeException("Zona no encontrada: " + zona);
        }

        return generarRespuesta(espaciosZona);
    }

    public ParkingResponse buscarPorTipo(String tipo) {
        List<ParkingSpot> espaciosTipo = parkingSpots.values().stream()
                .filter(spot -> spot.getTipoVehiculo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());

        if (espaciosTipo.isEmpty()) {
            throw new RuntimeException("Tipo de vehículo no encontrado: " + tipo);
        }

        return generarRespuesta(espaciosTipo);
    }

    public ParkingSpot buscarMasCercano(String zona, String tipo) {
        return parkingSpots.values().stream()
                .filter(spot -> !spot.isOcupado())
                .filter(spot -> spot.getZona().equalsIgnoreCase(zona))
                .filter(spot -> spot.getTipoVehiculo().equalsIgnoreCase(tipo))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay espacios disponibles en " + zona + " para " + tipo));
    }

    public ParkingSpot ocuparEspacio(String id) {
        ParkingSpot spot = parkingSpots.get(id);
        if (spot == null) {
            throw new RuntimeException("Espacio no encontrado: " + id);
        }
        if (spot.isOcupado()) {
            throw new RuntimeException("Espacio ya ocupado: " + id);
        }
        spot.setOcupado(true);
        return spot;
    }

    public ParkingSpot liberarEspacio(String id) {
        ParkingSpot spot = parkingSpots.get(id);
        if (spot == null) {
            throw new RuntimeException("Espacio no encontrado: " + id);
        }
        if (!spot.isOcupado()) {
            throw new RuntimeException("Espacio ya está libre: " + id);
        }
        spot.setOcupado(false);
        return spot;
    }

    public void simularSensores() {
        // Simula cambios aleatorios en la ocupación (sensores IoT)
        parkingSpots.values().forEach(spot -> {
            if (random.nextDouble() < 0.1) { // 10% probabilidad de cambio
                spot.setOcupado(!spot.isOcupado());
            }
        });
    }

    private ParkingResponse generarRespuesta(List<ParkingSpot> espacios) {
        int total = espacios.size();
        long ocupados = espacios.stream().filter(ParkingSpot::isOcupado).count();
        int disponibles = total - (int) ocupados;
        double porcentaje = total > 0 ? (ocupados * 100.0 / total) : 0;

        List<ParkingSpot> disponiblesList = espacios.stream()
                .filter(spot -> !spot.isOcupado())
                .limit(10) // Mostrar solo 10 para no saturar
                .collect(Collectors.toList());

        String mensaje = generarMensaje(porcentaje);

        return new ParkingResponse(total, disponibles, (int) ocupados,
                Math.round(porcentaje * 100.0) / 100.0,
                disponiblesList, mensaje);
    }

    private String generarMensaje(double porcentaje) {
        if (porcentaje < 30) {
            return "✅ Disponibilidad ALTA - Estacionamiento fácil";
        } else if (porcentaje < 70) {
            return "⚠️ Disponibilidad MEDIA - Búsqueda moderada";
        } else if (porcentaje < 90) {
            return "🔴 Disponibilidad BAJA - Considere alternativas";
        } else {
            return "🚫 CASI LLENO - Busque otras zonas";
        }
    }
}