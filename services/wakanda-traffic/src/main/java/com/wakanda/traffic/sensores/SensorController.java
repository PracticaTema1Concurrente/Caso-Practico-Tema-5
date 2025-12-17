package com.wakanda.traffic.sensores;

import com.wakanda.traffic.semaforos.SmartTrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/traffic")
public class SensorController {

    @Autowired
    private SmartTrafficService smartService;

    // ESTE ENDPOINT ES PARA EL PUNTO 2: Recibir datos del sensor
    @PostMapping("/sensor")
    public String recibirDatosSensor(@RequestBody TrafficSensorData sensorData) {
        return smartService.analyzeTrafficData(sensorData);
    }
}