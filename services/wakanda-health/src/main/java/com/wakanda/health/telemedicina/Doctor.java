
package com.wakanda.health.telemedicina;

import java.util.ArrayList;
import java.util.List;

public class Doctor {
    private String id;
    private String nombre;
    private String especialidad;
    private String licencia;
    private boolean disponible;
    private int consultasHoy;
    private int consultasTotales;
    private double calificacion;
    private List<String> idiomas;
    private String hospital;
    private int aniosExperiencia;

    public Doctor() {
        this.idiomas = new ArrayList<>();
    }

    public Doctor(String id, String nombre, String especialidad, String hospital, int aniosExperiencia) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.hospital = hospital;
        this.aniosExperiencia = aniosExperiencia;
        this.disponible = true;
        this.consultasHoy = 0;
        this.consultasTotales = 0;
        this.calificacion = 5.0;
        this.idiomas.add("Español");
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getLicencia() { return licencia; }
    public void setLicencia(String licencia) { this.licencia = licencia; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public int getConsultasHoy() { return consultasHoy; }
    public void setConsultasHoy(int consultasHoy) { this.consultasHoy = consultasHoy; }

    public int getConsultasTotales() { return consultasTotales; }
    public void setConsultasTotales(int consultasTotales) { this.consultasTotales = consultasTotales; }

    public double getCalificacion() { return calificacion; }
    public void setCalificacion(double calificacion) { this.calificacion = calificacion; }

    public List<String> getIdiomas() { return idiomas; }
    public void setIdiomas(List<String> idiomas) { this.idiomas = idiomas; }

    public String getHospital() { return hospital; }
    public void setHospital(String hospital) { this.hospital = hospital; }

    public int getAniosExperiencia() { return aniosExperiencia; }
    public void setAniosExperiencia(int aniosExperiencia) { this.aniosExperiencia = aniosExperiencia; }
}