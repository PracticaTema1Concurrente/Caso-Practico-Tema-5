package com.wakanda.health.centros_conectados;

public class ExpedientePaciente {
    private String idPaciente;
    private String nombre;
    private String sintoma;
    private boolean esUrgente;
    private String tratamientoActual;

    public ExpedientePaciente() {}

    public ExpedientePaciente(String idPaciente, String nombre, String sintoma, boolean esUrgente) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.sintoma = sintoma;
        this.esUrgente = esUrgente;
        this.tratamientoActual = "En evaluación";
    }

    // Getters y Setters
    public String getIdPaciente() { return idPaciente; }
    public void setIdPaciente(String idPaciente) { this.idPaciente = idPaciente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getSintoma() { return sintoma; }
    public void setSintoma(String sintoma) { this.sintoma = sintoma; }
    public boolean isEsUrgente() { return esUrgente; }
    public void setEsUrgente(boolean esUrgente) { this.esUrgente = esUrgente; }
    public String getTratamientoActual() { return tratamientoActual; }
    public void setTratamientoActual(String tratamientoActual) { this.tratamientoActual = tratamientoActual; }
}