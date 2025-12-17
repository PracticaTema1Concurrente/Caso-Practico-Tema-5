
package com.wakanda.health.dispositivos_monitoreo;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DeviceMonitoringService {

    private final Map<String, HealthDevice> devices = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @PostConstruct
    public void inicializarDispositivos() {
        System.out.println("⌚ Inicializando Sistema de Monitoreo de Salud...");

        // Smartwatches
        crearDispositivos("SMARTWATCH", "Apple Watch Series 9", 150);
        crearDispositivos("SMARTWATCH", "Samsung Galaxy Watch", 100);

        // Pulseras de actividad
        crearDispositivos("PULSERA", "Fitbit Charge 6", 200);
        crearDispositivos("PULSERA", "Xiaomi Mi Band", 180);

        // Monitores cardíacos
        crearDispositivos("MONITOR_CARDIACO", "Polar H10", 50);

        // Glucómetros
        crearDispositivos("GLUCOMETRO", "FreeStyle Libre", 80);

        // Oxímetros
        crearDispositivos("OXIMETRO", "Masimo MightySat", 40);

        System.out.println("⌚ Sistema de Monitoreo inicializado: " + devices.size() + " dispositivos conectados");
    }

    private void crearDispositivos(String tipo, String modelo, int cantidad) {
        for (int i = 1; i <= cantidad; i++) {
            String id = tipo + "-" + String.format("%04d", devices.size() + 1);
            String pacienteId = "PAC-" + String.format("%05d", i);
            String nombrePaciente = generarNombre();

            HealthDevice device = new HealthDevice(id, pacienteId, nombrePaciente, tipo, modelo);
            device.setBateria(50 + random.nextInt(50));

            // Generar signos vitales iniciales
            generarSignosVitales(device);

            devices.put(id, device);
        }
    }

    private String generarNombre() {
        String[] nombres = {"Juan", "María", "Carlos", "Ana", "Luis", "Carmen", "Pedro", "Laura",
                "Miguel", "Isabel", "Jorge", "Rosa", "Fernando", "Patricia"};
        String[] apellidos = {"García", "Rodríguez", "Martínez", "López", "González", "Pérez",
                "Sánchez", "Ramírez", "Torres", "Flores"};
        return nombres[random.nextInt(nombres.length)] + " " + apellidos[random.nextInt(apellidos.length)];
    }

    private void generarSignosVitales(HealthDevice device) {
        // Frecuencia cardíaca: 60-100 bpm normal
        device.setFrecuenciaCardiaca(60 + random.nextInt(40));

        // Presión arterial: 120/80 normal
        device.setPresionSistolica(110 + random.nextInt(30));
        device.setPresionDiastolica(70 + random.nextInt(20));

        // Saturación de oxígeno: 95-100% normal
        device.setSaturacionOxigeno(95 + random.nextInt(6));

        // Temperatura: 36.5-37.5°C normal
        device.setTemperatura(36.0 + random.nextDouble() * 2);

        // Actividad física
        device.setPasos(random.nextInt(15000));
        device.setCalorias(random.nextInt(3000));

        // Glucosa (solo para glucómetros): 70-140 mg/dL normal
        if ("GLUCOMETRO".equals(device.getTipo())) {
            device.setNivelGlucosa(70.0 + random.nextDouble() * 70);
        }

        // Evaluar y establecer nivel de alerta
        evaluarSignosVitales(device);
    }

    public List<HealthDevice> obtenerTodosLosDispositivos() {
        return new ArrayList<>(devices.values());
    }

    public List<HealthDevice> obtenerDispositivosActivos() {
        return devices.values().stream()
                .filter(HealthDevice::isActivo)
                .collect(Collectors.toList());
    }

    public List<HealthDevice> obtenerDispositivosConAlertas() {
        return devices.values().stream()
                .filter(HealthDevice::isAlertaMedica)
                .sorted(Comparator.comparing(d -> d.getNivelAlerta()))
                .collect(Collectors.toList());
    }

    public HealthDevice obtenerDispositivo(String id) {
        HealthDevice device = devices.get(id);
        if (device == null) {
            throw new RuntimeException("Dispositivo no encontrado: " + id);
        }
        return device;
    }

    public List<HealthDevice> obtenerPorPaciente(String pacienteId) {
        return devices.values().stream()
                .filter(d -> d.getPacienteId().equals(pacienteId))
                .collect(Collectors.toList());
    }

    public HealthDevice actualizarSignosVitales(String deviceId, Map<String, Object> signos) {
        HealthDevice device = obtenerDispositivo(deviceId);

        if (signos.containsKey("frecuenciaCardiaca")) {
            device.setFrecuenciaCardiaca((Integer) signos.get("frecuenciaCardiaca"));
        }
        if (signos.containsKey("presionSistolica")) {
            device.setPresionSistolica((Integer) signos.get("presionSistolica"));
        }
        if (signos.containsKey("presionDiastolica")) {
            device.setPresionDiastolica((Integer) signos.get("presionDiastolica"));
        }
        if (signos.containsKey("saturacionOxigeno")) {
            device.setSaturacionOxigeno((Integer) signos.get("saturacionOxigeno"));
        }
        if (signos.containsKey("temperatura")) {
            device.setTemperatura((Double) signos.get("temperatura"));
        }
        if (signos.containsKey("glucosa")) {
            device.setNivelGlucosa((Double) signos.get("glucosa"));
        }

        evaluarSignosVitales(device);
        return device;
    }

    private void evaluarSignosVitales(HealthDevice device) {
        List<String> alertas = new ArrayList<>();
        String nivelMaximo = "NORMAL";

        // Evaluar frecuencia cardíaca
        Integer fc = device.getFrecuenciaCardiaca();
        if (fc != null) {
            if (fc < 50 || fc > 120) {
                alertas.add("Frecuencia cardíaca anormal: " + fc + " bpm");
                nivelMaximo = "CRITICO";
            } else if (fc < 60 || fc > 100) {
                alertas.add("Frecuencia cardíaca fuera de rango: " + fc + " bpm");
                nivelMaximo = actualizarNivelAlerta(nivelMaximo, "ALERTA");
            }
        }

        // Evaluar presión arterial
        if (device.getPresionSistolica() != null && device.getPresionDiastolica() != null) {
            int sistolica = device.getPresionSistolica();
            int diastolica = device.getPresionDiastolica();

            if (sistolica > 140 || diastolica > 90) {
                alertas.add("Presión arterial alta: " + sistolica + "/" + diastolica);
                nivelMaximo = actualizarNivelAlerta(nivelMaximo, "ALERTA");
            } else if (sistolica < 90 || diastolica < 60) {
                alertas.add("Presión arterial baja: " + sistolica + "/" + diastolica);
                nivelMaximo = actualizarNivelAlerta(nivelMaximo, "PRECAUCION");
            }
        }

        // Evaluar saturación de oxígeno
        Integer spo2 = device.getSaturacionOxigeno();
        if (spo2 != null && spo2 < 95) {
            alertas.add("Saturación de oxígeno baja: " + spo2 + "%");
            nivelMaximo = actualizarNivelAlerta(nivelMaximo, spo2 < 90 ? "CRITICO" : "ALERTA");
        }

        // Evaluar temperatura
        Double temp = device.getTemperatura();
        if (temp != null) {
            if (temp > 38.0) {
                alertas.add("Temperatura elevada: " + String.format("%.1f", temp) + "°C");
                nivelMaximo = actualizarNivelAlerta(nivelMaximo, "ALERTA");
            } else if (temp < 35.0) {
                alertas.add("Hipotermia detectada: " + String.format("%.1f", temp) + "°C");
                nivelMaximo = actualizarNivelAlerta(nivelMaximo, "CRITICO");
            }
        }

        // Evaluar glucosa
        Double glucosa = device.getNivelGlucosa();
        if (glucosa != null) {
            if (glucosa > 180 || glucosa < 70) {
                alertas.add("Nivel de glucosa anormal: " + String.format("%.0f", glucosa) + " mg/dL");
                nivelMaximo = actualizarNivelAlerta(nivelMaximo, glucosa < 70 ? "CRITICO" : "ALERTA");
            }
        }

        device.setNivelAlerta(nivelMaximo);
        device.setAlertaMedica(!alertas.isEmpty());
        device.setMensajeAlerta(alertas.isEmpty() ? "Signos vitales normales" : String.join("; ", alertas));
    }

    private String actualizarNivelAlerta(String actual, String nuevo) {
        String[] niveles = {"NORMAL", "PRECAUCION", "ALERTA", "CRITICO"};
        int actualIdx = Arrays.asList(niveles).indexOf(actual);
        int nuevoIdx = Arrays.asList(niveles).indexOf(nuevo);
        return nuevoIdx > actualIdx ? nuevo : actual;
    }

    public void simularActualizaciones() {
        devices.values().stream()
                .filter(HealthDevice::isActivo)
                .forEach(device -> {
                    // Simular consumo de batería
                    device.setBateria(Math.max(0, device.getBateria() - random.nextInt(2)));

                    // Simular pequeñas variaciones en signos vitales
                    if (random.nextDouble() < 0.3) {
                        if (device.getFrecuenciaCardiaca() != null) {
                            int variacion = random.nextInt(10) - 5;
                            device.setFrecuenciaCardiaca(Math.max(40, Math.min(150,
                                    device.getFrecuenciaCardiaca() + variacion)));
                        }

                        evaluarSignosVitales(device);
                    }
                });
    }

    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();

        long activos = devices.values().stream().filter(HealthDevice::isActivo).count();
        long conAlertas = devices.values().stream().filter(HealthDevice::isAlertaMedica).count();
        long criticos = devices.values().stream()
                .filter(d -> "CRITICO".equals(d.getNivelAlerta())).count();

        Map<String, Long> porTipo = devices.values().stream()
                .collect(Collectors.groupingBy(HealthDevice::getTipo, Collectors.counting()));

        stats.put("total", devices.size());
        stats.put("activos", activos);
        stats.put("conAlertas", conAlertas);
        stats.put("criticos", criticos);
        stats.put("distribucionTipos", porTipo);

        return stats;
    }
}