package com.wakanda.traffic.transporte_publico;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class PublicTransportService {

    private final Map<String, PublicTransport> flota = new ConcurrentHashMap<>();
    private final Random random = new Random();

    // Paradas por línea
    private final Map<String, List<String>> rutasPorLinea = new HashMap<>();

    @PostConstruct
    public void inicializarFlota() {
        System.out.println("🚌 Inicializando Sistema de Transporte Público...");

        // Definir rutas
        inicializarRutas();

        // Líneas de Bus
        crearUnidades("BUS", "L1", 10, 50);
        crearUnidades("BUS", "L2", 8, 45);
        crearUnidades("BUS", "L3", 12, 50);

        // Líneas de Metro
        crearUnidades("METRO", "M1", 15, 200);
        crearUnidades("METRO", "M2", 12, 200);

        // Tranvías
        crearUnidades("TRAM", "T1", 6, 80);

        System.out.println("🚌 Sistema de Transporte Público inicializado: "
                + flota.size() + " unidades en servicio");
        System.out.println("   📍 Líneas activas: " + rutasPorLinea.size());
    }

    private void inicializarRutas() {
        // Línea L1 - Centro a Zona Tech
        rutasPorLinea.put("L1", Arrays.asList(
                "Plaza Central", "Av. Vibranium", "Mercado Principal",
                "Hospital Wakanda", "Campus Tech", "Parque Innovación"
        ));

        // Línea L2 - Residencial a Centro
        rutasPorLinea.put("L2", Arrays.asList(
                "Barrio Dorado", "Zona Residencial Norte", "Plaza Real",
                "Museo Nacional", "Estación Central"
        ));

        // Línea L3 - Circunvalación
        rutasPorLinea.put("L3", Arrays.asList(
                "Terminal Norte", "Aeropuerto", "Zona Industrial",
                "Puerto", "Terminal Sur"
        ));

        // Metro M1
        rutasPorLinea.put("M1", Arrays.asList(
                "Estación Norte", "Plaza Mayor", "Centro Histórico",
                "Estadio Nacional", "Estación Sur"
        ));

        // Metro M2
        rutasPorLinea.put("M2", Arrays.asList(
                "Universidad", "Biblioteca Nacional", "Palacio Real",
                "Ministerios", "Congreso"
        ));

        // Tranvía T1
        rutasPorLinea.put("T1", Arrays.asList(
                "Puerto Marítimo", "Paseo Costero", "Playa Central",
                "Marina", "Faro"
        ));
    }

    private void crearUnidades(String tipo, String linea, int cantidad, int capacidad) {
        List<String> paradas = rutasPorLinea.get(linea);

        for (int i = 1; i <= cantidad; i++) {
            String id = tipo + "-" + linea + "-" + String.format("%03d", i);

            // Parada aleatoria
            int paradaIndex = random.nextInt(paradas.size());
            String paradaActual = paradas.get(paradaIndex);
            String proximaParada = paradas.get((paradaIndex + 1) % paradas.size());

            int minutos = 2 + random.nextInt(15);
            String[] estados = {"EN_RUTA", "EN_RUTA", "EN_RUTA", "DETENIDO"};
            String estado = estados[random.nextInt(estados.length)];

            int pasajeros = random.nextInt(capacidad);
            double velocidad = 20 + random.nextDouble() * 40;

            flota.put(id, new PublicTransport(
                    id, linea, tipo, paradaActual, proximaParada,
                    minutos, estado, capacidad, pasajeros, velocidad
            ));
        }
    }

    public TransportResponse obtenerEstadoGeneral() {
        List<PublicTransport> todas = new ArrayList<>(flota.values());
        return generarRespuesta(todas);
    }

    public TransportResponse obtenerPorLinea(String linea) {
        List<PublicTransport> unidadesLinea = flota.values().stream()
                .filter(t -> t.getLinea().equalsIgnoreCase(linea))
                .collect(Collectors.toList());

        if (unidadesLinea.isEmpty()) {
            throw new RuntimeException("Línea no encontrada: " + linea);
        }

        return generarRespuesta(unidadesLinea);
    }

    public TransportResponse obtenerPorTipo(String tipo) {
        List<PublicTransport> unidadesTipo = flota.values().stream()
                .filter(t -> t.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());

        if (unidadesTipo.isEmpty()) {
            throw new RuntimeException("Tipo no encontrado: " + tipo);
        }

        return generarRespuesta(unidadesTipo);
    }

    public List<ArrivalPrediction> predecirLlegadas(String parada) {
        return flota.values().stream()
                .filter(t -> t.getProximaParada().equalsIgnoreCase(parada)
                        || t.getParadaActual().equalsIgnoreCase(parada))
                .map(t -> {
                    String confianza = calcularConfianza(t);
                    return new ArrivalPrediction(
                            t.getLinea(), parada, t.getMinutosLlegada(),
                            confianza, t.getEstado()
                    );
                })
                .sorted(Comparator.comparingInt(ArrivalPrediction::getMinutosEstimados))
                .limit(5)
                .collect(Collectors.toList());
    }

    public PublicTransport activarDesvio(String unidadId, String motivo, String rutaAlternativa) {
        PublicTransport unidad = flota.get(unidadId);
        if (unidad == null) {
            throw new RuntimeException("Unidad no encontrada: " + unidadId);
        }

        unidad.setEstado("DESVIO");
        unidad.setMotivoDesvio(motivo);
        unidad.setRutaAlternativa(rutaAlternativa);
        unidad.setMinutosLlegada(unidad.getMinutosLlegada() + 10); // Añadir retraso

        return unidad;
    }

    public void simularMovimiento() {
        flota.values().forEach(unidad -> {
            if ("EN_RUTA".equals(unidad.getEstado())) {
                // Reducir tiempo de llegada
                int nuevoTiempo = Math.max(0, unidad.getMinutosLlegada() - 1);
                unidad.setMinutosLlegada(nuevoTiempo);

                // Si llegó, avanzar a siguiente parada
                if (nuevoTiempo == 0) {
                    avanzarParada(unidad);
                }

                // Simular cambios aleatorios
                if (random.nextDouble() < 0.05) { // 5% prob de retraso
                    unidad.setMinutosLlegada(unidad.getMinutosLlegada() + random.nextInt(5));
                }
            }
        });
    }

    private void avanzarParada(PublicTransport unidad) {
        List<String> ruta = rutasPorLinea.get(unidad.getLinea());
        int indexActual = ruta.indexOf(unidad.getProximaParada());
        int siguienteIndex = (indexActual + 1) % ruta.size();

        unidad.setParadaActual(unidad.getProximaParada());
        unidad.setProximaParada(ruta.get(siguienteIndex));
        unidad.setMinutosLlegada(3 + random.nextInt(10));

        // Simular pasajeros subiendo/bajando
        int cambio = random.nextInt(20) - random.nextInt(15);
        int nuevos = Math.max(0, Math.min(unidad.getCapacidad(),
                unidad.getPasajerosActuales() + cambio));
        unidad.setPasajerosActuales(nuevos);
    }

    private String calcularConfianza(PublicTransport t) {
        if ("DESVIO".equals(t.getEstado())) return "BAJA";
        if (t.getMinutosLlegada() > 15) return "MEDIA";
        return "ALTA";
    }

    private TransportResponse generarRespuesta(List<PublicTransport> unidades) {
        int total = unidades.size();
        long enRuta = unidades.stream().filter(u -> "EN_RUTA".equals(u.getEstado())).count();
        long retrasos = unidades.stream().filter(u -> u.getMinutosLlegada() > 10).count();
        long desvios = unidades.stream().filter(u -> "DESVIO".equals(u.getEstado())).count();

        List<String> alertas = new ArrayList<>();
        if (desvios > 0) {
            alertas.add("⚠️ " + desvios + " unidades con desvíos activos");
        }
        if (retrasos > total * 0.3) {
            alertas.add("🔴 Alto nivel de retrasos en el sistema");
        }

        String mensaje = generarMensaje(enRuta, total, desvios);

        return new TransportResponse(total, (int)enRuta, (int)retrasos,
                (int)desvios, unidades, mensaje, alertas);
    }

    private String generarMensaje(long enRuta, int total, long desvios) {
        double porcentaje = (enRuta * 100.0) / total;

        if (desvios > 0) {
            return "⚠️ Servicio con DESVÍOS - Consulte rutas alternativas";
        } else if (porcentaje > 80) {
            return "✅ Servicio ÓPTIMO - Cobertura completa";
        } else if (porcentaje > 60) {
            return "🟡 Servicio NORMAL - Algunos retrasos menores";
        } else {
            return "🔴 Servicio LIMITADO - Verifique horarios";
        }
    }

    public List<String> obtenerParadasDeLinea(String linea) {
        return rutasPorLinea.getOrDefault(linea, Collections.emptyList());
    }
}